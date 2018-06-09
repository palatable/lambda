package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LTTest {

    @Test
    public void comparisons() {
        assertTrue(lt(1, 2));
        assertFalse(lt(1, 1));
        assertFalse(lt(2, 1));
    }
}