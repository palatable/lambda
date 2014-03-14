package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.PredicatedDroppingIterator;

import java.util.Iterator;

public final class DropWhile<A> extends DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Iterable<A>> {
    @Override
    public final Iterable<A> apply(final MonadicFunction<? super A, Boolean> predicate, final Iterable<A> as) {
        return new Iterable<A>() {
            @Override
            public Iterator<A> iterator() {
                return new PredicatedDroppingIterator<A>(predicate, as.iterator());
            }
        };
    }

    public static <A> DropWhile<A> dropWhile() {
        return new DropWhile<A>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> dropWhile(
            MonadicFunction<? super A, Boolean> predicate) {
        return DropWhile.<A>dropWhile().partial(predicate);
    }

    public static <A> Iterable<A> dropWhile(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return dropWhile(predicate).apply(as);
    }

}
