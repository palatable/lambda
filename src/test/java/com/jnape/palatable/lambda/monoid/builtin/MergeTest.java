package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Before;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.monoid.builtin.Merge.merge;
import static org.junit.Assert.assertEquals;

public class MergeTest {

    private static final Semigroup<String> JOIN = (x, y) -> x + y;
    private static final Monoid<Integer>   ADD  = Monoid.monoid((x, y) -> x + y, 0);

    private Monoid<Either<String, Integer>> merge;

    @Before
    public void setUp() {
        merge = merge(JOIN, ADD);
    }

    @Test
    public void identity() {
        assertEquals(right(0), merge.identity());
    }

    @Test
    public void monoid() {
        assertEquals(left("onetwo"), merge.apply(left("one"), left("two")));
        assertEquals(right(3), merge.apply(right(1), right(2)));
        assertEquals(left("foo"), merge.apply(left("foo"), right(2)));
        assertEquals(left("foo"), merge.apply(right(2), left("foo")));
    }
}