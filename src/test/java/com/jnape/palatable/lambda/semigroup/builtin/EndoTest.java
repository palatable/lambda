package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.monoid.builtin.Endo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EndoTest {

    @Test
    public void identity() {
        assertEquals((Integer) 1, Endo.<Integer>endo().identity().apply(1));
    }

    @Test
    public void semigroup() {
        assertEquals((Integer) 2, Endo.<Integer>endo().apply(x -> x + 1, x -> x + 1).apply(0));
        assertEquals((Integer) 2, Endo.<Integer>endo().apply(x -> x + 1, x -> x + 1, 0));
        assertEquals((Integer) 2, Endo.<Integer>endo(x -> x + 1, x -> x + 1, 0));
    }
}