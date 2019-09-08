package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.monoid.builtin.Or.or;
import static org.junit.Assert.assertEquals;

public class OrTest {

    @Test
    public void identity() {
        assertEquals(false, or().identity());
    }

    @Test
    public void monoid() {
        Or or = or();
        assertEquals(true, or.apply(true, true));
        assertEquals(true, or.apply(true, false));
        assertEquals(true, or.apply(false, true));
        assertEquals(false, or.apply(false, false));
    }

    @Test(timeout = 500)
    public void shortCircuiting() {
        Iterable<Boolean> bools = cons(true, repeat(false));
        Or                or    = or();

        assertEquals(true, or.foldLeft(false, bools));
        assertEquals(true, or.foldLeft(true, bools));
        assertEquals(true, or.reduceLeft(bools));
        assertEquals(true, or.foldMap(id(), bools));
    }
}