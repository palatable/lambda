package com.jnape.palatable.lambda.adt.product;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.product.Product3.product;
import static org.junit.Assert.assertEquals;

public class Product3Test {

    private Product3<String, String, String> product;

    @Before
    public void setUp() {
        product = product("a", "b", "c");
    }

    @Test
    public void staticFactoryMethod() {
        assertEquals("a", product._1());
        assertEquals("b", product._2());
        assertEquals("c", product._3());
    }

    @Test
    public void rotations() {
        assertEquals("bac", product.invert().into((a, b, c) -> a + b + c));
        assertEquals("bca", product.rotateL3().into((a, b, c) -> a + b + c));
        assertEquals("cab", product.rotateR3().into((a, b, c) -> a + b + c));
    }
}