package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Cycle.cycle;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Partition.partition;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class PartitionTest {

    @TestTraits({Laziness.class, EmptyIterableSupport.class, FiniteIteration.class, ImmutableIteration.class})
    public Subjects<Fn1<? extends Iterable<?>, ?>> createTraitsTestSubject() {
        return subjects(partition(constantly(a(1))).fmap(Tuple2::_1),
                        partition(constantly(b(1))).fmap(Tuple2::_2));
    }

    @Test
    public void partitionsIterableIntoAsAndBs() {
        Iterable<String> strings = asList("one", "two", "three", "four", "five");
        Tuple2<Iterable<String>, Iterable<Integer>> partition = partition(s -> s.length() % 2 == 1 ? a(s) : b(s.length()), strings);

        assertThat(partition._1(), iterates("one", "two", "three"));
        assertThat(partition._2(), iterates(4, 4));
    }

    @Test
    public void infiniteListSupport() {
        Iterable<CoProduct2<String, Integer, ?>> coproducts = cycle(a("left"), b(1));
        Tuple2<Iterable<String>, Iterable<Integer>> partition = partition(id(), coproducts);

        assertThat(take(3, partition._1()), iterates("left", "left", "left"));
        assertThat(take(3, partition._2()), iterates(1, 1, 1));
    }
}