package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.TraversableLaws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Try.failure;
import static com.jnape.palatable.lambda.adt.Try.pureTry;
import static com.jnape.palatable.lambda.adt.Try.success;
import static com.jnape.palatable.lambda.adt.Try.trying;
import static com.jnape.palatable.lambda.adt.Try.withResources;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static testsupport.assertion.MonadErrorAssert.assertLaws;
import static testsupport.matchers.EitherMatcher.isLeftThat;

@RunWith(Traits.class)
public class TryTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, TraversableLaws.class, MonadRecLaws.class})
    public Subjects<Try<Integer>> testSubject() {
        return subjects(failure(new IllegalStateException()), success(1));
    }

    @Test
    public void monadError() {
        assertLaws(subjects(failure(new IllegalStateException("a")),
                            success(1)),
                   new IOException("bar"),
                   t -> success(t.getMessage().length()));
    }

    @Test
    public void catchingWithGenericPredicate() {
        Try<String> caught = Try.<String>failure(new RuntimeException())
                .catching(__ -> false, r -> "caught first")
                .catching(__ -> true, r -> "caught second");

        assertEquals(success("caught second"), caught);
    }

    @Test
    public void catchingIsANoOpForSuccess() {
        Try<String> caught = success("success")
                .catching(__ -> true, __ -> "caught");

        assertEquals(success("success"), caught);
    }

    @Test
    public void firstMatchingCatchBlockWins() {
        Try<String> caught = Try.<String>failure(new IllegalStateException())
                .catching(__ -> true, __ -> "first")
                .catching(__ -> true, __ -> "second");

        assertEquals(success("first"), caught);
    }

    @Test
    public void catchBasedOnExceptionType() {
        Try<String> caught = Try.<String>failure(new IllegalStateException())
                .catching(IllegalArgumentException.class, __ -> "illegal argument exception")
                .catching(IllegalStateException.class, __ -> "illegal state exception")
                .catching(RuntimeException.class, __ -> "runtime exception");

        assertEquals(success("illegal state exception"), caught);
    }

    @Test
    public void ensureIfSuccess() {
        AtomicInteger invocations = new AtomicInteger(0);
        success(1).ensuring((invocations::incrementAndGet));
        assertEquals(1, invocations.get());
    }

    @Test
    public void ensureIfFailure() {
        AtomicInteger invocations = new AtomicInteger(0);
        Try.failure(new IllegalStateException()).ensuring((invocations::incrementAndGet));
        assertEquals(1, invocations.get());
    }

    @Test
    public void exceptionThrownInEnsuringBlockIsCaught() {
        IllegalStateException expected = new IllegalStateException();
        assertEquals(Try.failure(expected), success(1).ensuring(() -> {throw expected;}));

        Either<Throwable, Object> actual = Try.failure(new IllegalArgumentException())
                .ensuring(() -> { throw expected;})
                .toEither();
        assertThat(actual, isLeftThat(instanceOf(IllegalArgumentException.class)));
        assertEquals(left(expected), actual.biMapL(t -> t.getSuppressed()[0]));
    }

    @Test
    public void forfeitEnsuresFailure() {
        IllegalStateException expected = new IllegalStateException();
        assertEquals(expected, Try.failure(expected).forfeit(__ -> new IllegalArgumentException()));
        assertEquals(expected, Try.<Object>success(1).forfeit(__ -> expected));
    }

    @Test
    public void recoverEnsuresSuccess() {
        assertEquals((Integer) 1, Try.success(1).recover(constantly(1)));
        assertEquals((Integer) 1, Try.<Integer>failure(new IllegalArgumentException()).recover(constantly(1)));
    }

    @Test
    public void orThrow() {
        assertEquals((Integer) 1, trying(() -> 1).orThrow());

        Throwable expected = new Exception("expected");

        Try<Object> trying = trying(() -> {
            throw expected;
        });

        assertThrows(
            "expected",
            Exception.class,
            trying::orThrow
        );
    }

    @Test
    public void toMaybe() {
        assertEquals(just("foo"), success("foo").toMaybe());
        assertEquals(nothing(), Try.failure(new IllegalStateException()).toMaybe());
    }

    @Test
    public void toEither() {
        assertEquals(right("foo"), success("foo").toEither());

        IllegalStateException exception = new IllegalStateException();
        assertEquals(left(exception), Try.failure(exception).toEither());
    }

    @Test
    public void toEitherWithLeftMappingFunction() {
        assertEquals(right(1), success(1).toEither(__ -> "fail"));
        assertEquals(left("fail"), Try.failure(new IllegalStateException("fail")).toEither(Throwable::getMessage));
    }

    @Test
    public void tryingCatchesAnyThrowableThrownDuringEvaluation() {
        IllegalStateException expected = new IllegalStateException();
        assertEquals(failure(expected), trying(() -> {throw expected;}));

        assertEquals(success("foo"), trying(() -> "foo"));
    }

    @Test
    public void withResourcesCleansUpAutoCloseableInSuccessCase() {
        AtomicBoolean closed = new AtomicBoolean(false);
        assertEquals(success(1), withResources(() -> (AutoCloseable) () -> closed.set(true), resource -> success(1)));
        assertTrue(closed.get());
    }

    @Test
    public void withResourcesCleansUpAutoCloseableInFailureCase() {
        AtomicBoolean    closed    = new AtomicBoolean(false);
        RuntimeException exception = new RuntimeException();
        assertEquals(Try.failure(exception), withResources(() -> (AutoCloseable) () -> closed.set(true),
                                                           resource -> { throw exception; }));
        assertTrue(closed.get());
    }

    @Test
    public void withResourcesExposesResourceCreationFailure() {
        IOException ioException = new IOException();
        assertEquals(Try.failure(ioException), withResources(() -> { throw ioException; }, resource -> success(1)));
    }

    @Test
    public void withResourcesExposesResourceCloseFailure() {
        IOException ioException = new IOException();
        assertEquals(Try.failure(ioException), withResources(() -> (AutoCloseable) () -> { throw ioException; },
                                                             resource -> success(1)));
    }

    @Test
    public void withResourcesPreservesSuppressedExceptionThrownDuringClose() {
        RuntimeException rootException     = new RuntimeException();
        IOException      nestedIOException = new IOException();
        Try<Throwable> failure = withResources(() -> (AutoCloseable) () -> { throw nestedIOException; },
                                               resource -> { throw rootException; });
        Throwable thrown = failure.recover(id());

        assertEquals(thrown, rootException);
        assertArrayEquals(new Throwable[]{nestedIOException}, thrown.getSuppressed());
    }

    @Test
    public void cascadingWithResourcesClosesInInverseOrder() {
        List<String> closeMessages = new ArrayList<>();
        assertEquals(success(1), withResources(() -> (AutoCloseable) () -> closeMessages.add("close a"),
                                               a -> () -> closeMessages.add("close b"),
                                               b -> () -> closeMessages.add("close c"),
                                               c -> success(1)));
        assertEquals(asList("close c", "close b", "close a"), closeMessages);
    }

    @Test
    public void lazyZip() {
        assertEquals(success(2), success(1).lazyZip(lazy(success(x -> x + 1))).value());
        IllegalStateException e = new IllegalStateException();
        assertEquals(failure(e), failure(e).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
    }

    @Test
    public void orThrowCanStillThrowCheckedExceptions() {
        try {
            Try.trying(() -> {
                throw new RuntimeException();
            }).<IOException>orThrow();
            fail("Expected RuntimeException to be thrown, but nothing was");
        } catch (IOException ioException) {
            fail("Expected thrown exception to not be IOException, but merely proving it can still be caught");
        } catch (Exception expected) {
        }
    }

    @Test
    public void orThrowCanTransformFirst() {
        try {
            Try.trying(() -> {
                throw new IllegalStateException();
            }).<Exception>orThrow(IllegalArgumentException::new);
            fail("Expected RuntimeException to be thrown, but nothing was");
        } catch (IllegalStateException ioException) {
            fail("Expected thrown exception to not be IllegalStateException, but it was");
        } catch (IllegalArgumentException expected) {
        } catch (Exception e) {
            fail("A different exception altogether was thrown.");
        }
    }

    @Test
    public void staticPure() {
        Try<Integer> try_ = pureTry().apply(1);
        assertEquals(success(1), try_);
    }
}