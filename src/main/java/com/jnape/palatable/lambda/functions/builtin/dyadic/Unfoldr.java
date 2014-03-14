package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.UnfoldingIterator;

import java.util.Iterator;

public final class Unfoldr<A> extends DyadicFunction<MonadicFunction<? super A, ? extends A>, A, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final MonadicFunction<? super A, ? extends A> fn, final A seed) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new UnfoldingIterator<A>(fn, seed);
            }
        };
    }

    public static <A> Unfoldr<A> unfoldr() {
        return new Unfoldr<A>();
    }

    public static <A> MonadicFunction<A, Iterable<A>> unfoldr(MonadicFunction<? super A, ? extends A> fn) {
        return Unfoldr.<A>unfoldr().partial(fn);
    }

    public static <A> Iterable<A> unfoldr(MonadicFunction<? super A, ? extends A> fn, A seed) {
        return unfoldr(fn).apply(seed);
    }
}
