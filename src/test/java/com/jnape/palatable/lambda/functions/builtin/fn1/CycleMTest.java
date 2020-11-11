package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;
import testsupport.matchers.IterableMatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.CycleM.cycleM;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.DropM.dropM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.NthM.nthM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.fromIterator;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.matchers.IterateTMatcher.iterates;

public class CycleMTest {
    @Test
    public void cyclesTheSameSequence() {
        assertThat(takeM(9, cycleM(takeM(3, naturalNumbersM(pureIdentity())))),
                   iterates(1, 2, 3, 1, 2, 3, 1, 2, 3));
    }

    @Test
    public void cyclesTheSameSequenceForever() {
        assertThat(takeM(9, dropM(STACK_EXPLODING_NUMBER - STACK_EXPLODING_NUMBER % 3, cycleM(takeM(3, naturalNumbersM(pureIdentity()))))),
                   iterates(1, 2, 3, 1, 2, 3, 1, 2, 3));
    }

    @Test
    public void infinityInfinities() {
        Identity<Maybe<Integer>> actual = nthM(STACK_EXPLODING_NUMBER, cycleM(naturalNumbersM(pureIdentity()))).coerce();
        assertThat(actual.runIdentity(), equalTo(just(STACK_EXPLODING_NUMBER)));
    }

    @Test
    public void cyclesArePredictablyWeirdWithNonRepeatableSequences() {
        AtomicInteger theCount = new AtomicInteger(0);
        IterateT<IO<?>, Integer> numbers = fromIterator(new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return theCount.incrementAndGet();
            }
        });
        IterateT<IO<?>, Integer> x = takeM(10, cycleM(takeM(2, numbers)));
        IO<List<Integer>> y = x.toCollection(ArrayList::new);
        List<Integer> actual = y.unsafePerformIO();
        assertThat(actual, IterableMatcher.iterates(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }
}