package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class UnfoldingIteratorTest {

    private static final Fn1<Integer, Maybe<Tuple2<String, Integer>>> STRINGIFY = x -> just(tuple(x.toString(), x + 1));

    @Test
    public void hasNextIfFunctionProducesPresentValue() {
        assertThat(new UnfoldingIterator<>(STRINGIFY, 0).hasNext(), is(true));
    }

    @Test
    public void doesNotHaveNextIfFunctionProducesEmptyValue() {
        assertThat(new UnfoldingIterator<String, Integer>(x -> nothing(), 0).hasNext(), is(false));
    }

    @Test
    public void defersNextCallForAsLongAsPossible() {
        AtomicInteger invocations = new AtomicInteger(0);
        UnfoldingIterator<String, Integer> iterator = new UnfoldingIterator<>(x -> {
            invocations.incrementAndGet();
            return just(tuple(x.toString(), x + 1));
        }, 1);

        assertEquals(0, invocations.get());
        iterator.next();
        assertEquals(1, invocations.get());
    }
}