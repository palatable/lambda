package com.jnape.palatable.lambda.adt.product;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.product.Product7.product;
import static org.junit.Assert.assertEquals;

public class Product7Test {

    private Product7<String, String, String, String, String, String, String> product;

    @Before
    public void setUp() {
        product = product("a", "b", "c", "d", "e", "f", "g");
    }

    @Test
    public void staticFactoryMethod() {
        assertEquals("a", product._1());
        assertEquals("b", product._2());
        assertEquals("c", product._3());
        assertEquals("d", product._4());
        assertEquals("e", product._5());
        assertEquals("f", product._6());
        assertEquals("g", product._7());
    }

    @Test
    public void rotations() {
        assertEquals("bacdefg", product.invert().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("bcadefg", product.rotateL3().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("cabdefg", product.rotateR3().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("bcdaefg", product.rotateL4().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("dabcefg", product.rotateR4().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("bcdeafg", product.rotateL5().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("eabcdfg", product.rotateR5().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("bcdefag", product.rotateL6().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("fabcdeg", product.rotateR6().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("bcdefga", product.rotateL7().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
        assertEquals("gabcdef", product.rotateR7().into((a, b, c, d, e, f, g) -> a + b + c + d + e + f + g));
    }
}