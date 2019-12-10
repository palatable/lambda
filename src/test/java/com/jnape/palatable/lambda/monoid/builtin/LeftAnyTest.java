package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.monoid.builtin.LeftAny.leftAny;
import static org.junit.Assert.assertEquals;

public class LeftAnyTest {

    @Test
    public void monoid() {
        LeftAny<String, Integer> leftAny = leftAny();
        Monoid<String>           join    = Monoid.monoid((x, y) -> x + y, "");

        assertEquals(left(""), leftAny.apply(join).identity());
        assertEquals(left("foo"), leftAny.apply(join).apply(left("foo"), right(1)));
        assertEquals(left("foo"), leftAny.apply(join).apply(right(1), left("foo")));
        assertEquals(left("foobar"), leftAny.apply(join).apply(left("foo"), left("bar")));
        assertEquals(right(2), leftAny.apply(join).apply(right(1), right(2)));
    }
}