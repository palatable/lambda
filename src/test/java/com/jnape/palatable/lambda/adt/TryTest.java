package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
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
import static com.jnape.palatable.lambda.adt.Try.success;
import static com.jnape.palatable.lambda.adt.Try.trying;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static testsupport.matchers.LeftMatcher.isLeftThat;

@RunWith(Traits.class)
public class TryTest {

    @Rule public ExpectedException thrown = ExpectedException.none();

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, TraversableLaws.class})
    public Subjects<Try<Throwable, Integer>> testSubject() {
        return subjects(failure(new IllegalStateException()), success(1));
    }

    @Test
    public void catchingWithGenericPredicate() {
        Try<RuntimeException, String> caught = Try.<RuntimeException, String>failure(new RuntimeException())
                .catching(__ -> false, r -> "caught first")
                .catching(__ -> true, r -> "caught second");

        assertEquals(success("caught second"), caught);
    }

    @Test
    public void catchingIsANoOpForSuccess() {
        Try<RuntimeException, String> caught = Try.<RuntimeException, String>success("success")
                .catching(__ -> true, __ -> "caught");

        assertEquals(success("success"), caught);
    }

    @Test
    public void firstMatchingCatchBlockWins() {
        Try<RuntimeException, String> caught = Try.<RuntimeException, String>failure(new IllegalStateException())
                .catching(__ -> true, __ -> "first")
                .catching(__ -> true, __ -> "second");

        assertEquals(success("first"), caught);
    }

    @Test
    public void catchBasedOnExceptionType() {
        Try<RuntimeException, String> caught = Try.<RuntimeException, String>failure(new IllegalStateException())
                .catching(IllegalArgumentException.class, __ -> "illegal argument exception")
                .catching(IllegalStateException.class, __ -> "illegal state exception")
                .catching(RuntimeException.class, __ -> "runtime exception");

        assertEquals(success("illegal state exception"), caught);
    }

    @Test
    public void ensureIfSuccess() {
        AtomicInteger invocations = new AtomicInteger(0);
        Try.success(1).ensuring((invocations::incrementAndGet));
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
        assertEquals(Try.failure(expected), Try.success(1).ensuring(() -> {throw expected;}));

        Either<IllegalArgumentException, Object> actual = Try.failure(new IllegalArgumentException())
                .ensuring(() -> { throw expected;})
                .toEither();
        assertThat(actual, isLeftThat(instanceOf(IllegalArgumentException.class)));
        assertEquals(left(expected), actual.biMapL(t -> t.getSuppressed()[0]));
    }

    @Test
    public void forfeitEnsuresFailure() {
        IllegalStateException expected = new IllegalStateException();
        assertEquals(expected, Try.<RuntimeException, Object>failure(expected).forfeit(__ -> new IllegalArgumentException()));
        assertEquals(expected, Try.<RuntimeException, Object>success(1).forfeit(__ -> expected));
    }

    @Test
    public void recoverEnsuresSuccess() {
        assertEquals((Integer) 1, Try.<RuntimeException, Integer>success(1).recover(constantly(1)));
        assertEquals((Integer) 1, Try.<RuntimeException, Integer>failure(new IllegalArgumentException()).recover(constantly(1)));
    }

    @Test
    public void orThrow() throws Throwable {
        assertEquals((Integer) 1, trying(() -> 1).orThrow());

        Throwable expected = new Exception("expected");
        thrown.expect(equalTo(expected));
        trying(() -> {throw expected;}).orThrow();
    }

    @Test
    public void toMaybe() {
        assertEquals(just("foo"), Try.success("foo").toMaybe());
        assertEquals(nothing(), Try.failure(new IllegalStateException()).toMaybe());
    }

    @Test
    public void toEither() {
        assertEquals(right("foo"), Try.success("foo").toEither());

        IllegalStateException exception = new IllegalStateException();
        assertEquals(left(exception), Try.failure(exception).toEither());
    }

    @Test
    public void toEitherWithLeftMappingFunction() {
        assertEquals(right(1), Try.success(1).toEither(__ -> "fail"));
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
        assertEquals(Try.success(1), Try.withResources(() -> () -> closed.set(true), resource -> success(1)));
        assertTrue(closed.get());
    }

    @Test
    public void withResourcesCleansUpAutoCloseableInFailureCase() {
        AtomicBoolean closed = new AtomicBoolean(false);
        RuntimeException exception = new RuntimeException();
        assertEquals(Try.failure(exception), Try.withResources(() -> () -> closed.set(true),
                                                               resource -> { throw exception; }));
        assertTrue(closed.get());
    }

    @Test
    public void withResourcesExposesResourceCreationFailure() {
        IOException ioException = new IOException();
        assertEquals(Try.failure(ioException), Try.withResources(() -> { throw ioException; }, resource -> success(1)));
    }

    @Test
    public void withResourcesExposesResourceCloseFailure() {
        IOException ioException = new IOException();
        assertEquals(Try.failure(ioException), Try.withResources(() -> () -> { throw ioException; },
                                                                 resource -> success(1)));
    }

    @Test
    public void withResourcesPreservesSuppressedExceptionThrownDuringClose() {
        RuntimeException rootException = new RuntimeException();
        IOException nestedIOException = new IOException();
        Try<Exception, Exception> failure = Try.withResources(() -> () -> { throw nestedIOException; },
                                                              resource -> { throw rootException; });
        Exception thrown = failure.recover(id());

        assertEquals(thrown, rootException);
        assertArrayEquals(new Throwable[]{nestedIOException}, thrown.getSuppressed());
    }

    @Test
    public void cascadingWithResourcesClosesInInverseOrder() {
        List<String> closeMessages = new ArrayList<>();
        assertEquals(success(1), Try.withResources(() -> (AutoCloseable) () -> closeMessages.add("close a"),
                                                   a -> () -> closeMessages.add("close b"),
                                                   b -> () -> closeMessages.add("close c"),
                                                   c -> success(1)));
        assertEquals(asList("close c", "close b", "close a"), closeMessages);
    }
}