package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.semigroup.builtin.RightAny.rightAny;
import static org.junit.Assert.assertEquals;

public class RightAnyTest {

    @Test
    public void semigroup() {
        RightAny<String, Integer> rightAny = rightAny();
        Semigroup<Integer>        add      = Integer::sum;

        assertEquals(right(1), rightAny.apply(add).apply(right(1), left("foo")));
        assertEquals(right(1), rightAny.apply(add).apply(left("foo"), right(1)));
        assertEquals(right(3), rightAny.apply(add).apply(right(1), right(2)));
        assertEquals(left("bar"), rightAny.apply(add).apply(left("foo"), left("bar")));
    }
}