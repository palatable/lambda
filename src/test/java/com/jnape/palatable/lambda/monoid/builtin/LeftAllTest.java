package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.monoid.builtin.LeftAll.leftAll;
import static org.junit.Assert.assertEquals;

public class LeftAllTest {

    @Test
    public void monoid() {
        Monoid<Integer> addition = Monoid.monoid((x, y) -> x + y, 0);

        assertEquals(left(0), leftAll(addition).identity());
        assertEquals(left(3), leftAll(addition, left(1), left(2)));
        assertEquals(right("foo"), leftAll(addition, right("foo"), left(1)));
        assertEquals(right("bar"), leftAll(addition, left(1), right("bar")));
        assertEquals(right("foo"), leftAll(addition, right("foo"), right("bar")));
    }

}