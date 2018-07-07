package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class TraversableTest {

    @Test
    public void inference() {
        Either<String, Maybe<Integer>> a = just(Either.<String, Integer>right(1)).traverse(id(), Either::right);
        assertEquals(right(just(1)), a);

        Maybe<Either<String, Integer>> b = Either.<String, Maybe<Integer>>right(just(1)).traverse(id(), Maybe::just);
        assertEquals(just(right(1)), b);

        Either<String, Maybe<Integer>> c = b.traverse(id(), Either::right);
        assertEquals(a, c);

        Maybe<LambdaIterable<Integer>> d = LambdaIterable.wrap(asList(just(1), just(2))).traverse(id(), Maybe::just);
        assertThat(d.orElseThrow(AssertionError::new).unwrap(), iterates(1, 2));
    }
}