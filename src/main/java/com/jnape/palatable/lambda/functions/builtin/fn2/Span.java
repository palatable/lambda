package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.DropWhile.dropWhile;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeWhile.takeWhile;

/**
 * Given a predicate, return a {@link Tuple2} where the first slot is the front contiguous elements of an {@link
 * Iterable} matching the predicate and the second slot is all the remaining elements.
 *
 * @param <A> the {@link Iterable} element type
 */
public final class Span<A> implements Fn2<Function<? super A, Boolean>, Iterable<A>, Tuple2<Iterable<A>, Iterable<A>>> {

    private static final Span INSTANCE = new Span();

    private Span() {
    }

    @Override
    public Tuple2<Iterable<A>, Iterable<A>> apply(Function<? super A, Boolean> predicate, Iterable<A> as) {
        return Tuple2.fill(as).biMap(takeWhile(predicate), dropWhile(predicate));
    }

    @SuppressWarnings("unchecked")
    public static <A> Span<A> span() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Tuple2<Iterable<A>, Iterable<A>>> span(Function<? super A, Boolean> predicate) {
        return Span.<A>span().apply(predicate);
    }

    public static <A> Tuple2<Iterable<A>, Iterable<A>> span(Function<? super A, Boolean> predicate,
                                                            Iterable<A> as) {
        return Span.<A>span(predicate).apply(as);
    }
}
