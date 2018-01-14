package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Peek2.peek2;
import static org.junit.Assert.assertEquals;

public class Peek2Test {

    @Test
    public void peeksAtBothBifunctorValues() {
        AtomicInteger counter = new AtomicInteger(0);
        Tuple2<Integer, Integer> tuple = tuple(1, 2);
        assertEquals(tuple, peek2(__ -> counter.incrementAndGet(), __ -> counter.incrementAndGet(), tuple));
        assertEquals(2, counter.get());
    }

    @Test
    public void followsSameConventionsAsBimap() {
        AtomicInteger counter = new AtomicInteger(0);
        Either<Object, Integer> either = right(1);
        peek2(__ -> counter.incrementAndGet(), __ -> counter.incrementAndGet(), either);
        assertEquals(1, counter.get());
    }
}