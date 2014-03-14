package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.DroppingIterator;

import java.util.Iterator;

public final class Drop<A> extends DyadicFunction<Integer, Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final Integer n, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new DroppingIterator<A>(n, as.iterator());
            }
        };
    }

    public static <A> Drop<A> drop() {
        return new Drop<A>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> drop(Integer n) {
        return Drop.<A>drop().partial(n);
    }

    public static <A> Iterable<A> drop(Integer n, Iterable<A> as) {
        return Drop.<A>drop(n).apply(as);
    }
}
