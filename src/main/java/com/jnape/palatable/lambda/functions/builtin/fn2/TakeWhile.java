package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iteration.PredicatedTakingIterable;

import java.util.function.Function;

/**
 * Lazily limit the <code>Iterable</code> to the first group of contiguous elements that satisfy the predicate by
 * iterating up to, but not including, the first element for which the predicate evaluates to <code>false</code>.
 *
 * @param <A> The Iterable element type
 * @see Take
 * @see Filter
 * @see DropWhile
 */
public final class TakeWhile<A> implements Fn2<Function<? super A, ? extends Boolean>, Iterable<A>, Iterable<A>> {

    private static final TakeWhile INSTANCE = new TakeWhile();

    private TakeWhile() {
    }

    @Override
    public Iterable<A> apply(Function<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return new PredicatedTakingIterable<>(predicate, as);
    }

    @SuppressWarnings("unchecked")
    public static <A> TakeWhile<A> takeWhile() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> takeWhile(Function<? super A, ? extends Boolean> predicate) {
        return TakeWhile.<A>takeWhile().apply(predicate);
    }

    public static <A> Iterable<A> takeWhile(Function<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return TakeWhile.<A>takeWhile(predicate).apply(as);
    }
}
