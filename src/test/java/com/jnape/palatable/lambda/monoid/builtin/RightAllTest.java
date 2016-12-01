package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.monoid.builtin.RightAll.rightAll;
import static org.junit.Assert.assertEquals;

public class RightAllTest {


    @Test
    public void monoid() {
        final Monoid<Integer> addition = Monoid.monoid((x, y) -> x + y, 0);

        assertEquals(right(0), rightAll(addition).identity());
        assertEquals(right(3), rightAll(addition, right(1), right(2)));
        assertEquals(left("foo"), rightAll(addition, left("foo"), right(1)));
        assertEquals(left("bar"), rightAll(addition, right(1), left("bar")));
        assertEquals(left("foo"), rightAll(addition, left("foo"), left("bar")));
    }
}