package com.jnape.palatable.lambda.adt.product;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.product.Product5.product;
import static org.junit.Assert.assertEquals;

public class Product5Test {

    private Product5<String, String, String, String, String> product;

    @Before
    public void setUp() {
        product = product("a", "b", "c", "d", "e");
    }

    @Test
    public void staticFactoryMethod() {
        assertEquals("a", product._1());
        assertEquals("b", product._2());
        assertEquals("c", product._3());
        assertEquals("d", product._4());
        assertEquals("e", product._5());
    }

    @Test
    public void rotations() {
        assertEquals("bacde", product.invert().into((a, b, c, d, e) -> a + b + c + d + e));
        assertEquals("bcade", product.rotateL3().into((a, b, c, d, e) -> a + b + c + d + e));
        assertEquals("cabde", product.rotateR3().into((a, b, c, d, e) -> a + b + c + d + e));
        assertEquals("bcdae", product.rotateL4().into((a, b, c, d, e) -> a + b + c + d + e));
        assertEquals("dabce", product.rotateR4().into((a, b, c, d, e) -> a + b + c + d + e));
        assertEquals("bcdea", product.rotateL5().into((a, b, c, d, e) -> a + b + c + d + e));
        assertEquals("eabcd", product.rotateR5().into((a, b, c, d, e) -> a + b + c + d + e));
    }
}