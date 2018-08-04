package com.jnape.palatable.lambda.adt.product;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.product.Product8.product;
import static org.junit.Assert.assertEquals;

public class Product8Test {

    private Product8<String, String, String, String, String, String, String, String> product;

    @Before
    public void setUp() {
        product = product("a", "b", "c", "d", "e", "f", "g", "h");
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
        assertEquals("h", product._8());
    }

    @Test
    public void rotations() {
        assertEquals("bacdefgh", product.invert().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("bcadefgh", product.rotateL3().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("cabdefgh", product.rotateR3().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("bcdaefgh", product.rotateL4().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("dabcefgh", product.rotateR4().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("bcdeafgh", product.rotateL5().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("eabcdfgh", product.rotateR5().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("bcdefagh", product.rotateL6().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("fabcdegh", product.rotateR6().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("bcdefgah", product.rotateL7().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("gabcdefh", product.rotateR7().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("bcdefgha", product.rotateL8().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
        assertEquals("habcdefg", product.rotateR8().into((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h));
    }
}