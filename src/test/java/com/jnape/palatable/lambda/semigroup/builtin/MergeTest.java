package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.semigroup.builtin.Merge.merge;
import static org.junit.Assert.assertEquals;

public class MergeTest {

    @Test
    public void semigroup() {
        Semigroup<String> join = (x, y) -> x + y;
        Semigroup<Integer> add = (x, y) -> x + y;

        Semigroup<Either<String, Integer>> merge = merge(join, add);
        assertEquals(left("onetwo"), merge.apply(left("one"), left("two")));
        assertEquals(right(3), merge.apply(right(1), right(2)));
        assertEquals(left("foo"), merge.apply(left("foo"), right(2)));
        assertEquals(left("foo"), merge.apply(right(2), left("foo")));
    }
}