package com.jnape.palatable.lambda.adt.product;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.product.Product6.product;
import static org.junit.Assert.assertEquals;

public class Product6Test {

    private Product6<String, String, String, String, String, String> product;

    @Before
    public void setUp() {
        product = product("a", "b", "c", "d", "e", "f");
    }

    @Test
    public void staticFactoryMethod() {
        assertEquals("a", product._1());
        assertEquals("b", product._2());
        assertEquals("c", product._3());
        assertEquals("d", product._4());
        assertEquals("e", product._5());
        assertEquals("f", product._6());
    }

    @Test
    public void rotations() {
        assertEquals("bacdef", product.invert().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
        assertEquals("bcadef", product.rotateL3().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
        assertEquals("cabdef", product.rotateR3().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
        assertEquals("bcdaef", product.rotateL4().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
        assertEquals("dabcef", product.rotateR4().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
        assertEquals("bcdeaf", product.rotateL5().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
        assertEquals("eabcdf", product.rotateR5().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
        assertEquals("bcdefa", product.rotateL6().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
        assertEquals("fabcde", product.rotateR6().into((a, b, c, d, e, f) -> a + b + c + d + e + f));
    }
}