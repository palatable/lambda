package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.TakingIterator;

import java.util.Iterator;

public final class Take<A> extends DyadicFunction<Integer, Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final Integer n, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new TakingIterator<A>(n, as.iterator());
            }
        };
    }

    public static <A> Take<A> take() {
        return new Take<A>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> take(int n) {
        return Take.<A>take().partial(n);
    }

    public static <A> Iterable<A> take(Integer n, Iterable<A> as) {
        return Take.<A>take(n).apply(as);
    }
}
