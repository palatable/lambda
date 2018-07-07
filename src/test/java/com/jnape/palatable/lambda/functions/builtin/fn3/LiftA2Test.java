package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.Test;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn3.LiftA2.liftA2;
import static org.junit.Assert.assertEquals;

public class LiftA2Test {

    @Test
    public void inference() {
        BiFunction<Integer, Integer, Integer> add = (x, y) -> x + y;

        Maybe<Integer> a = liftA2(add, just(1), just(2));
        assertEquals(just(3), a);

        Maybe<Integer> b = liftA2(add, just(1), nothing());
        assertEquals(nothing(), b);

        Maybe<Integer> c = liftA2(add, nothing(), just(2));
        assertEquals(nothing(), c);

        Maybe<Integer> d = liftA2(add, nothing(), nothing());
        assertEquals(nothing(), d);

        Either<String, Integer> e = liftA2(add, Either.<String, Integer>right(1), right(2));
        assertEquals(right(3), e);

        Either<String, Integer> f = liftA2(add, left("error"), right(2));
        assertEquals(left("error"), f);

        Either<String, Integer> g = liftA2(add, right(1), left("error"));
        assertEquals(left("error"), g);

        Either<String, Integer> h = liftA2(add, left("error"), left("another error"));
        assertEquals(left("error"), h);
    }
}