package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Sequence.sequence;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

public class SequenceTest {

    @Test
    public void naturality() {
        Function<Identity<?>, Either<String, Object>> t = id -> right(id.runIdentity());
        Either<String, Identity<Integer>> traversable = right(new Identity<>(1));

        assertEquals(t.apply(sequence(traversable, Identity::new).fmap(id())),
                     sequence(traversable.fmap(t), Either::right));
    }

    @Test
    public void identity() {
        Either<String, Identity<Integer>> traversable = right(new Identity<>(1));
        assertEquals(sequence(traversable.fmap(Identity::new), Identity::new),
                     new Identity<>(traversable));
    }

    @Test
    public void composition() {
        Either<String, Identity<Either<String, Integer>>> traversable = right(new Identity<>(right(1)));
        assertEquals(sequence(traversable.fmap(x -> new Compose<>(x.fmap(id()))), x -> new Compose<>(new Identity<>(right(x)))),
                     new Compose<>(sequence(traversable, Identity::new).fmap(x -> sequence(x, Either::right)).fmap(id())));
    }

    @Test
    public void iterableSpecialization() {
        assertThat(sequence(asList(right(1), right(2)), Either::right)
                           .orThrow(l -> new AssertionError("Expected a right value, but was a left value of <" + l + ">")),
                   iterates(1, 2));
    }
}