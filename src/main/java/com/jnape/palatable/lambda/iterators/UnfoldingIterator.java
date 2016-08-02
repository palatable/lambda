package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public class UnfoldingIterator<A, B> extends ImmutableIterator<A> {
    private final Function<B, Optional<Tuple2<A, B>>> function;
    private       Optional<Tuple2<A, B>>              optionalAcc;

    public UnfoldingIterator(Function<B, Optional<Tuple2<A, B>>> function, B b) {
        this.function = function;
        optionalAcc = function.apply(b);
    }

    @Override
    public boolean hasNext() {
        return optionalAcc.isPresent();
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        Tuple2<A, B> acc = optionalAcc.get();
        A next = acc._1();
        optionalAcc = function.apply(acc._2());
        return next;
    }
}
