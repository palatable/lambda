package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.adt.Either.fromOptional;
import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class EitherTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

        DyadicFunction<String, String, String> concat = String::concat;
        DyadicFunction<Integer, Integer, Integer> add = (r1, r2) -> r1 + r2;

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
    public void fromOptionalMapsOptionalToEither() {
        Optional<String> present = Optional.of("foo");
        Optional<String> absent = Optional.empty();

        assertThat(fromOptional(present, () -> -1), is(right("foo")));
        assertThat(fromOptional(absent, () -> -1), is(left(-1)));
    }

    @Test
    public void functorialProperties() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.fmap(r -> r + 1), is(left));
        assertThat(right.fmap(r -> r + 1), is(right(2)));
    }

    @Test
    public void biFunctorialProperties() {
        Either<String, Integer> left = left("foo");
        Either<String, Integer> right = right(1);

        assertThat(left.biMap(l -> l + "bar", r -> r + 1), is(left("foobar")));
        assertThat(right.biMap(l -> l + "bar", r -> r + 1), is(right(2)));
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