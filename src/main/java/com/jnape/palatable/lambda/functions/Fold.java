package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.exceptions.EmptyIterableException;

import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.Reverse.reverse;

public class Fold {

    public static <A, B> B foldLeft(DyadicFunction<B, A, B> function, B initialAccumulation, Iterable<A> as) {
        B accumulation = initialAccumulation;
        for (A a : as)
            accumulation = function.apply(accumulation, a);
        return accumulation;
    }

    public static <A> A reduceLeft(DyadicFunction<A, A, A> function, Iterable<A> as) {
        final Iterator<A> iterator = as.iterator();

        if (!iterator.hasNext())
            throw new EmptyIterableException();

        return foldLeft(function, iterator.next(), new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return iterator;
            }
        });
    }

    public static <A, B> B foldRight(DyadicFunction<A, B, B> function, B initialAccumulation, Iterable<A> as) {
        return foldLeft(function.flip(), initialAccumulation, reverse(as));
    }

    public static <A> A reduceRight(DyadicFunction<A, A, A> function, Iterable<A> as) {
        return reduceLeft(function.flip(), reverse(as));
    }
}
