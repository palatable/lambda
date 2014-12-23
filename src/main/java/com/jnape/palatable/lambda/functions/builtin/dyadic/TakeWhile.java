package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.PredicatedTakingIterator;

public final class TakeWhile<A> implements DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Iterable<A>> {

    @Override
    public final Iterable<A> apply(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return () -> new PredicatedTakingIterator<>(predicate, as.iterator());
    }

    public static <A> TakeWhile<A> takeWhile() {
        return new TakeWhile<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> takeWhile(
            MonadicFunction<? super A, Boolean> predicate) {
        return TakeWhile.<A>takeWhile().apply(predicate);
    }

    public static <A> Iterable<A> takeWhile(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return takeWhile(predicate).apply(as);
    }
}
