package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.semigroup.builtin.LeftAll.leftAll;
import static org.junit.Assert.assertEquals;

public class LeftAllTest {

    @Test
    public void semigroup() {
        Semigroup<Integer> addition = (x, y) -> x + y;

        assertEquals(left(3), leftAll(addition, left(1), left(2)));
        assertEquals(right("foo"), leftAll(addition, right("foo"), left(1)));
        assertEquals(right("bar"), leftAll(addition, left(1), right("bar")));
        assertEquals(right("foo"), leftAll(addition, right("foo"), right("bar")));
    }
}