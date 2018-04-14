package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn3.LiftA2.liftA2;
import static org.junit.Assert.assertEquals;

public class LiftA2Test {

    @Test
    public void liftsAndAppliesDyadicFunctionToTwoApplicatives() {
        BiFunction<Integer, Integer, Integer> add = (x, y) -> x + y;
        assertEquals(right(3), liftA2(add, right(1), right(2)).coerce());
        assertEquals(tuple(1, 5), liftA2(add, tuple(1, 2), tuple(2, 3)).coerce());
        assertEquals(new Identity<>(3), liftA2(add, new Identity<>(1), new Identity<>(2)));
    }
}