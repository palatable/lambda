package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.specialized.Predicate;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Not.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotTest {

    @Test
    public void negatesPredicate() {
        Predicate<Boolean> isTrue = not(a -> !a);
        assertTrue(isTrue.apply(true));
        assertFalse(isTrue.apply(false));
    }
}