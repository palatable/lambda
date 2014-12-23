package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.PredicatedDroppingIterator;

public final class DropWhile<A> implements DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Iterable<A>> {
    @Override
    public final Iterable<A> apply(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return () -> new PredicatedDroppingIterator<>(predicate, as.iterator());
    }

    public static <A> DropWhile<A> dropWhile() {
        return new DropWhile<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> dropWhile(
            MonadicFunction<? super A, Boolean> predicate) {
        return DropWhile.<A>dropWhile().apply(predicate);
    }

    public static <A> Iterable<A> dropWhile(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return dropWhile(predicate).apply(as);
    }

}
