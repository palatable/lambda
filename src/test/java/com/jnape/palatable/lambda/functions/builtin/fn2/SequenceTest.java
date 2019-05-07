package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Sequence.sequence;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class SequenceTest {

    @Test
    public void naturality() {
        Fn1<Identity<?>, Either<String, Object>> t           = id -> right(id.runIdentity());
        Either<String, Identity<Integer>>        traversable = right(new Identity<>(1));

        assertEquals(t.apply(sequence(traversable, Identity::new).fmap(id())),
                     sequence(traversable.fmap(t), Either::right));
    }

    @Test
    public void identity() {
        Either<String, Identity<Integer>> traversable = right(new Identity<>(1));
        assertEquals(new Identity<>(traversable),
                     sequence(traversable.fmap(Identity::new), Identity::new));
    }

    @Test
    public void composition() {
        Either<String, Identity<Either<String, Integer>>> traversable = right(new Identity<>(right(1)));
        assertEquals(new Compose<>(sequence(traversable, Identity::new).fmap(x -> sequence(x, Either::right)).fmap(id())),
                     sequence(traversable.fmap(x -> new Compose<>(x.fmap(id()))), x -> new Compose<>(new Identity<>(right(x)))));
    }

    @Test
    public void iterableSpecialization() {
        assertThat(sequence(asList(right(1), right(2)), Either::right)
                           .orThrow(l -> new AssertionError("Expected a right value, but was a left value of <" + l + ">")),
                   iterates(1, 2));
    }

    @Test
    public void mapSpecialization() {
        assertEquals(right(singletonMap("foo", 1)),
                     sequence(singletonMap("foo", right(1)), Either::right));
    }

    @Test
    public void compilation() {
        Either<String, Maybe<Integer>> a = sequence(just(right(1)), Either::right);
        assertEquals(right(just(1)), a);

        Maybe<Either<String, Integer>> b = sequence(right(just(1)), Maybe::just);
        assertEquals(just(right(1)), b);

        Either<String, Maybe<Integer>> c = sequence(b, Either::right);
        assertEquals(a, c);

        Maybe<Iterable<Integer>> d = sequence(asList(just(1), just(2)), Maybe::just);
        assertThat(d.orElseThrow(AssertionError::new), iterates(1, 2));
    }
}