package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Cycle.cycle;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Partition.partition;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class PartitionTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Fn1<? extends Iterable, ?> createTraitsLeftTestSubject() {
        return partition(constantly(left(1))).andThen(Tuple2::_1);
    }

    @Test
    public void partitionsIterableIntoLeftsAndRights() {
        Iterable<String> strings = asList("one", "two", "three", "four", "five");
        Tuple2<Iterable<String>, Iterable<Integer>> partition = partition(s -> s.length() % 2 == 1 ? left(s) : right(s.length()), strings);

        assertThat(partition._1(), iterates("one", "two", "three"));
        assertThat(partition._2(), iterates(4, 4));
    }

    @Test
    public void infiniteListSupport() {
        Iterable<Either<String, Integer>> eithers = cycle(left("left"), right(1));
        Tuple2<Iterable<String>, Iterable<Integer>> partition = partition(id(), eithers);

        assertThat(take(3, partition._1()), iterates("left", "left", "left"));
        assertThat(take(3, partition._2()), iterates(1, 1, 1));
    }
}