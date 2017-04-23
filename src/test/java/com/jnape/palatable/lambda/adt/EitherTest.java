package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.adt.Either.fromOptional;
import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(Traits.class)
public class EitherTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, BifunctorLaws.class})
    public Subjects<Either<String, Integer>> testSubjects() {
        return subjects(left("foo"), right(1));
    }

    @Test
    public void recoverLiftsLeftAndFlattensRight() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.recover(l -> -1), is(-1));
        assertThat(right.recover(l -> -1), is(1));
    }

    @Test
    public void forfeitLiftsRightAndFlattensLeft() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.forfeit(r -> "bar"), is("foo"));
        assertThat(right.forfeit(r -> "bar"), is("bar"));
    }

    @Test
    public void orReplacesLeftAndFlattensRight() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.or(-1), is(-1));
        assertThat(right.or(-1), is(1));
    }

    @Test
    public void orThrowFlattensRightOrThrowsException() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(right.orThrow(IllegalStateException::new), is(1));

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("foo");

        left.orThrow(IllegalStateException::new);
    }

    @Test
    public void filterLiftsRight() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.filter(x -> true, () -> "bar"), is(left));
        assertThat(left.filter(x -> false, () -> "bar"), is(left));
        assertThat(right.filter(x -> true, () -> "bar"), is(right));
        assertThat(right.filter(x -> false, () -> "bar"), is(left("bar")));
    }

    @Test
    public void monadicFlatMapLiftsRightAndFlattensBackToEither() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.flatMap(r -> right(r + 1)), is(left("foo")));
        assertThat(right.flatMap(r -> right(r + 1)), is(right(2)));
    }

    @Test
    public void dyadicFlatMapDuallyLiftsAndFlattensBackToEither() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.flatMap(l -> left(l + "bar"), r -> right(r + 1)), is(left("foobar")));
        assertThat(right.flatMap(l -> left(l + "bar"), r -> right(r + 1)), is(right(2)));
    }

    @Test
    public void mergeDuallyLiftsAndCombinesBiasingLeft() {
        Either<String, Integer> left1 = left("foo");
        Either<String, Integer> right1 = right(1);

        Either<String, Integer> left2 = left("bar");
        Either<String, Integer> right2 = right(2);

        BiFunction<String, String, String> concat = String::concat;
        BiFunction<Integer, Integer, Integer> add = (r1, r2) -> r1 + r2;

        assertThat(left1.merge(concat, add, left2), is(left("foobar")));
        assertThat(left1.merge(concat, add, right2), is(left1));
        assertThat(right1.merge(concat, add, left2), is(left2));
        assertThat(right1.merge(concat, add, right1), is(right(2)));
    }

    @Test
    public void matchDuallyLiftsAndFlattens() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.match(l -> l + "bar", r -> r + 1), is("foobar"));
        assertThat(right.match(l -> l + "bar", r -> r + 1), is(2));
    }

    @Test
    public void toOptionalMapsEitherToOptional() {
        assertEquals(Optional.of(1), Either.<String, Integer>right(1).toOptional());
        assertEquals(Optional.empty(), Either.<String, Integer>right(null).toOptional());
        assertEquals(Optional.empty(), Either.<String, Integer>left("fail").toOptional());
    }

    @Test
    public void fromOptionalMapsOptionalToEither() {
        Optional<Integer> present = Optional.of(1);
        Optional<Integer> absent = Optional.empty();

        assertThat(fromOptional(present, () -> "fail"), is(right(1)));
        assertThat(fromOptional(absent, () -> "fail"), is(left("fail")));
    }

    @Test
    public void fromOptionalDoesNotEvaluateLeftFnForRight() {
        Optional<Integer> present = Optional.of(1);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        fromOptional(present, atomicInteger::incrementAndGet);

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
    public void monadicPeekLiftsIOToTheRight() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        AtomicInteger intRef = new AtomicInteger();

        left.peek(intRef::set);
        assertEquals(0, intRef.get());

        right.peek(intRef::set);
        assertEquals(1, intRef.get());
    }

    @Test
    public void dyadicPeekDuallyLiftsIO() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        AtomicReference<String> stringRef = new AtomicReference<>();
        AtomicInteger intRef = new AtomicInteger();

        left.peek(stringRef::set, intRef::set);
        assertEquals("foo", stringRef.get());
        assertEquals(0, intRef.get());

        right.peek(stringRef::set, intRef::set);
        assertEquals("foo", stringRef.get());
        assertEquals(1, intRef.get());
    }
}