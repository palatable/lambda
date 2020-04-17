package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.TraversableLaws;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Either.fromMaybe;
import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static testsupport.assertion.MonadErrorAssert.assertLaws;

@RunWith(Traits.class)
public class EitherTest {

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class,
            MonadRecLaws.class})
    public Subjects<Either<String, Integer>> testSubjects() {
        return subjects(left("foo"), right(1));
    }

    @Test
    public void monadError() {
        assertLaws(subjects(left("a"), right(1)), "bar", e -> right(e.length()));
    }

    @Test
    public void recoverLiftsLeftAndFlattensRight() {
        Either<String, Integer> left  = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.recover(l -> -1), is(-1));
        assertThat(right.recover(l -> -1), is(1));
    }

    @Test
    public void forfeitLiftsRightAndFlattensLeft() {
        Either<String, Integer> left  = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.forfeit(r -> "bar"), is("foo"));
        assertThat(right.forfeit(r -> "bar"), is("bar"));
    }

    @Test
    public void orReplacesLeftAndFlattensRight() {
        Either<String, Integer> left  = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.or(-1), is(-1));
        assertThat(right.or(-1), is(1));
    }

    @Test
    public void orThrowFlattensRightOrThrowsException() {
        Either<String, Integer> left  = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(right.orThrow(IllegalStateException::new), is(1));

        assertThrows(
            "foo",
            IllegalStateException.class,
            () -> left.orThrow(IllegalStateException::new)
        );
    }

    @Test
    public void filterLiftsRight() {
        Either<String, Integer> left  = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.filter(x -> true, () -> "bar"), is(left));
        assertThat(left.filter(x -> false, () -> "bar"), is(left));
        assertThat(right.filter(x -> true, () -> "bar"), is(right));
        assertThat(right.filter(x -> false, () -> "bar"), is(left("bar")));
    }

    @Test
    public void filterSupportsFunctionFromRToL() {
        Either<String, Integer> left  = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.filter(x -> true, Object::toString), is(left));
        assertThat(left.filter(x -> false, Object::toString), is(left));
        assertThat(right.filter(x -> true, Object::toString), is(right));
        assertThat(right.filter(x -> false, Object::toString), is(left("1")));
    }

    @Test
    public void monadicFlatMapLiftsRightAndFlattensBackToEither() {
        Either<String, Integer> left  = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.flatMap(r -> right(r + 1)), is(left("foo")));
        assertThat(right.flatMap(r -> right(r + 1)), is(right(2)));
    }

    @Test
    public void mergeDuallyLiftsAndCombinesBiasingLeft() {
        Either<String, Integer> left1  = left("foo");
        Either<String, Integer> right1 = right(1);

        Either<String, Integer> left2  = left("bar");
        Either<String, Integer> right2 = right(2);

        Fn2<String, String, String>    concat = String::concat;
        Fn2<Integer, Integer, Integer> add    = Integer::sum;

        assertThat(left1.merge(concat, add, left2), is(left("foobar")));
        assertThat(left1.merge(concat, add, right2), is(left1));
        assertThat(right1.merge(concat, add, left2), is(left2));
        assertThat(right1.merge(concat, add, right1), is(right(2)));
    }

    @Test
    public void matchDuallyLiftsAndFlattens() {
        Either<String, Integer> left  = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.match(l -> l + "bar", r -> r + 1), is("foobar"));
        assertThat(right.match(l -> l + "bar", r -> r + 1), is(2));
    }

    @Test
    public void toMaybeMapsEitherToOptional() {
        assertEquals(just(1), Either.<String, Integer>right(1).toMaybe());
        assertEquals(nothing(), Either.<String, Integer>left("fail").toMaybe());
    }

    @Test
    public void fromMaybeMapsMaybeToEither() {
        Maybe<Integer> just    = just(1);
        Maybe<Integer> nothing = nothing();

        assertThat(fromMaybe(just, () -> "fail"), is(right(1)));
        assertThat(fromMaybe(nothing, () -> "fail"), is(left("fail")));
    }

    @Test
    public void fromMaybeDoesNotEvaluateLeftFnForRight() {
        Maybe<Integer> just          = just(1);
        AtomicInteger  atomicInteger = new AtomicInteger(0);
        fromMaybe(just, atomicInteger::incrementAndGet);

        assertThat(atomicInteger.get(), is(0));
    }

    @Test
    public void monadicTryingLiftsCheckedSupplier() {
        assertEquals(right(1), Either.trying(() -> 1));

        Exception checkedException = new Exception("expected");
        assertEquals(left(checkedException), Either.trying(() -> {
            throw checkedException;
        }));
    }

    @Test
    public void dyadicTryingLiftsCheckedSupplierMappingAnyThrownExceptions() {
        assertEquals(right(1), Either.trying(() -> 1, Throwable::getMessage));
        assertEquals(left("expected"), Either.trying(() -> {
            throw new Exception("expected");
        }, Throwable::getMessage));
    }

    @Test
    public void dyadicTryingWithRunnable() {
        assertEquals(right(UNIT), Either.trying(() -> {}, Throwable::getMessage));
        assertEquals(left("expected"), Either.trying(() -> {
            throw new IllegalStateException("expected");
        }, Throwable::getMessage));
    }

    @Test
    public void monadTryingWithRunnable() {
        assertEquals(right(UNIT), Either.trying(() -> {}));
        IllegalStateException expected = new IllegalStateException("expected");
        assertEquals(left(expected), Either.trying(() -> {throw expected;}));
    }

    @Test
    public void lazyZip() {
        assertEquals(right(2), right(1).lazyZip(lazy(right(x -> x + 1))).value());
        assertEquals(left("foo"), left("foo").lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
    }

    @Test
    public void staticPure() {
        Either<String, Integer> either = Either.<String>pureEither().apply(1);
        assertEquals(right(1), either);
    }
}