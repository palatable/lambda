package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iterators.UnfoldingIterator;

import java.util.Optional;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

/**
 * Lazily generate an infinite <code>Iterable</code> from the successive applications of the function first to the
 * initial seed value, then to the result, and so on; i.e., the result of <code>iterate(x -&gt; x + 1, 0)</code> would
 * produce an infinite <code>Iterable</code> over the elements <code>0, 1, 2, 3, ... </code> and so on.
 *
 * @param <A> The Iterable element type
 */
public final class Iterate<A> implements Fn2<Fn1<? super A, ? extends A>, A, Iterable<A>> {

    private Iterate() {
    }

    @Override
    public Iterable<A> apply(Fn1<? super A, ? extends A> fn, A seed) {
        return () -> new UnfoldingIterator<>(a -> Optional.of(tuple(a, fn.apply(a))), seed);
    }

    public static <A> Iterate<A> iterate() {
        return new Iterate<>();
    }

    public static <A> Fn1<A, Iterable<A>> iterate(Fn1<? super A, ? extends A> fn) {
        return Iterate.<A>iterate().apply(fn);
    }

    public static <A> Iterable<A> iterate(Fn1<? super A, ? extends A> fn, A seed) {
        return Iterate.<A>iterate(fn).apply(seed);
    }
}
