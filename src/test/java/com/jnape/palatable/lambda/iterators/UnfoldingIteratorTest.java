package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UnfoldingIteratorTest {

    private static final MonadicFunction<Integer, Optional<Tuple2<String, Integer>>> STRINGIFY = x -> Optional.of(tuple(x.toString(), x + 1));

    @Test
    public void hasNextIfFunctionProducesPresentValue() {
        UnfoldingIterator<String, Integer> unfoldingIterator = new UnfoldingIterator<>(STRINGIFY, 0);
        assertThat(unfoldingIterator.hasNext(), is(true));
    }

    @Test
    public void doesNotHaveNextIfFunctionProducesEmptyValue() {
        UnfoldingIterator<String, Integer> unfoldingIterator = new UnfoldingIterator<>(x -> Optional.<Tuple2<String, Integer>>empty(), 0);
        assertThat(unfoldingIterator.hasNext(), is(false));
    }
}