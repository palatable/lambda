package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Unfoldr.unfoldr;

/**
 * Lazily generate an infinite <code>Iterable</code> from the successive applications of the function first to the
 * initial seed value, then to the result, and so on; i.e., the result of <code>iterate(x -&gt; x + 1, 0)</code> would
 * produce an infinite <code>Iterable</code> over the elements <code>0, 1, 2, 3, ... </code> and so on.
 *
 * @param <A> The Iterable element type
 */
public final class Iterate<A> implements Fn2<Function<? super A, ? extends A>, A, Iterable<A>> {

    private static final Iterate<?> INSTANCE = new Iterate<>();

    private Iterate() {
    }

    @Override
    public Iterable<A> apply(Function<? super A, ? extends A> fn, A seed) {
        return unfoldr(a -> just(tuple(a, fn.apply(a))), seed);
    }

    @SuppressWarnings("unchecked")
    public static <A> Iterate<A> iterate() {
        return (Iterate<A>) INSTANCE;
    }

    public static <A> Fn1<A, Iterable<A>> iterate(Function<? super A, ? extends A> fn) {
        return Iterate.<A>iterate().apply(fn);
    }

    public static <A> Iterable<A> iterate(Function<? super A, ? extends A> fn, A seed) {
        return Iterate.<A>iterate(fn).apply(seed);
    }
}
