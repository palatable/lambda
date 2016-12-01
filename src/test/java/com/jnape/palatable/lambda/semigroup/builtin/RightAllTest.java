package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.semigroup.builtin.RightAll.rightAll;
import static org.junit.Assert.assertEquals;

public class RightAllTest {

    @Test
    public void semigroup() {
        Semigroup<Integer> addition = (x, y) -> x + y;

        assertEquals(right(3), rightAll(addition).apply(right(1), right(2)));
        assertEquals(left("foo"), rightAll(addition).apply(left("foo"), right(1)));
        assertEquals(left("bar"), rightAll(addition).apply(right(1), left("bar")));
        assertEquals(left("foo"), rightAll(addition).apply(left("foo"), left("bar")));
    }
}