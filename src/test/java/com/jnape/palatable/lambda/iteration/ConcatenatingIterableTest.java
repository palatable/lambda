package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.builtin.Concat;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.Deforesting;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Last.last;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class ConcatenatingIterableTest {

    @TestTraits({Deforesting.class})
    public Subjects<Fn1<Iterable<Integer>, Iterable<Integer>>> testSubject() {
        return subjects(xs -> new ConcatenatingIterable<>(emptyList(), xs),
                        xs -> new ConcatenatingIterable<>(xs, emptyList()),
                        xs -> new ConcatenatingIterable<>(repeat(1), xs),
                        xs -> new ConcatenatingIterable<>(xs, repeat(1)));
    }

    @Test
    public void stackSafety() {
        Iterable<Integer> xs = Concat.<Integer>concat().reduceLeft(replicate(1_000_000, asList(1, 2, 3)));
        assertEquals(just(3), last(xs));
    }
}