package com.jnape.palatable.lambda.adt.product;

import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.product.Product2.product;
import static org.junit.Assert.assertEquals;

public class Product2Test {

    private Product2<String, String> product;

    @Before
    public void setUp() {
        product = product("a", "b");
    }

    @Test
    public void staticFactoryMethod() {
        assertEquals("a", product._1());
        assertEquals("b", product._2());
    }

    @Test
    public void invert() {
        assertEquals("ba", product.invert().into((a, b) -> a + b));
    }
}