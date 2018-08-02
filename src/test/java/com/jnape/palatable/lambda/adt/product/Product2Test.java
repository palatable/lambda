package com.jnape.palatable.lambda.adt.product;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.product.Product2.product;
import static org.junit.Assert.assertEquals;

public class Product2Test {

    @Test
    public void staticFactoryMethod() {
        Product2<String, String> product = product("a", "b");
        assertEquals("a", product._1());
        assertEquals("b", product._2());
    }

    @Test
    public void invert() {
        Product2<String, String> inverted = product("a", "b").invert();
        assertEquals("b", inverted._1());
        assertEquals("a", inverted._2());
    }
}