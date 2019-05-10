package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static java.util.Collections.singletonList;

public final class MappingIterable<A, B> implements Iterable<B> {
    private final Iterable<A>     as;
    private final List<Fn1<?, ?>> mappers;

    @SuppressWarnings("unchecked")
    public MappingIterable(Fn1<? super A, ? extends B> fn, Iterable<A> as) {
        List<Fn1<?, ?>> mappers = new ArrayList<>(singletonList(fn));
        while (as instanceof MappingIterable<?, ?>) {
            MappingIterable<?, ?> nested = (MappingIterable<?, ?>) as;
            as = (Iterable<A>) nested.as;
            mappers.addAll(0, nested.mappers);
        }
        this.as = as;
        this.mappers = mappers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<B> iterator() {
        Fn1<Object, Object> fnComposedOnTheHeap = a -> foldLeft((x, fn) -> ((Fn1<Object, Object>) fn).apply(x),
                                                                a, mappers);
        return new MappingIterator<>((Fn1<? super A, ? extends B>) fnComposedOnTheHeap, as.iterator());
    }
}
