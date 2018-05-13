package com.jnape.palatable.lambda.monoid.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.monoid.builtin.And.and;
import static org.junit.Assert.assertEquals;

public class AndTest {

    @Test
    public void identity() {
        assertEquals(true, and().identity());
    }

    @Test
    public void monoid() {
        And and = and();
        assertEquals(true, and.apply(true, true));
        assertEquals(false, and.apply(false, true));
        assertEquals(false, and.apply(true, false));
        assertEquals(false, and.apply(false, false));
    }

    @Test(timeout = 500)
    public void shortCircuiting() {
        Iterable<Boolean> bools = cons(false, repeat(true));
        And and = and();

        assertEquals(false, and.foldLeft(false, bools));
        assertEquals(false, and.foldLeft(true, bools));
        assertEquals(false, and.reduceLeft(bools));
        assertEquals(false, and.foldMap(id(), bools));
    }
}