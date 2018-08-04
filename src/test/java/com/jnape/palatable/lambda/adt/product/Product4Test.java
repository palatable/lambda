package com.jnape.palatable.lambda.adt.product;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.product.Product4.product;
import static org.junit.Assert.assertEquals;

public class Product4Test {

    private Product4<String, String, String, String> product;

    @Before
    public void setUp() {
        product = product("a", "b", "c", "d");
    }

    @Test
    public void staticFactoryMethod() {
        assertEquals("a", product._1());
        assertEquals("b", product._2());
        assertEquals("c", product._3());
        assertEquals("d", product._4());
    }

    @Test
    public void rotations() {
        assertEquals("bacd", product.invert().into((a, b, c, d) -> a + b + c + d));
        assertEquals("bcad", product.rotateL3().into((a, b, c, d) -> a + b + c + d));
        assertEquals("cabd", product.rotateR3().into((a, b, c, d) -> a + b + c + d));
        assertEquals("bcda", product.rotateL4().into((a, b, c, d) -> a + b + c + d));
        assertEquals("dabc", product.rotateR4().into((a, b, c, d) -> a + b + c + d));
    }
}