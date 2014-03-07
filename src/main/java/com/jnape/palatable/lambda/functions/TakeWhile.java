package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.PredicatedTakingIterator;

import java.util.Iterator;

public final class TakeWhile<A> extends DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new PredicatedTakingIterator<A>(predicate, as.iterator());
            }
        };
    }

    public static <A> TakeWhile<A> takeWhile() {
        return new TakeWhile<A>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> takeWhile(
            MonadicFunction<? super A, Boolean> predicate) {
        return TakeWhile.<A>takeWhile().partial(predicate);
    }

    public static <A> Iterable<A> takeWhile(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return takeWhile(predicate).apply(as);
    }
}
