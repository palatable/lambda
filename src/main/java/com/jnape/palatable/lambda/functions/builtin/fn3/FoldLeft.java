package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;

/**
 * Given an <code>Iterable</code> of <code>A</code>s, a starting value <code>B</code>, and a
 * <code>{@link Fn2}&lt;B, A, B&gt;</code>, iteratively accumulate over the <code>Iterable</code>, ultimately returning
 * a final <code>B</code> value. If the <code>Iterable</code> is empty, just return the starting <code>B</code> value.
 * Note that, as the name implies, this function accumulates from left to right, such that <code>foldLeft(f, 0,
 * asList(1, 2, 3, 4, 5))</code> is evaluated as <code>f(f(f(f(f(0, 1), 2), 3), 4), 5)</code>.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Catamorphism"
 * target="_top">Catamorphisms</a>.
 *
 * @param <A> The Iterable element type
 * @param <B> The accumulation type
 * @see FoldRight
 */
public final class FoldLeft<A, B> implements Fn3<Fn2<? super B, ? super A, ? extends B>, B, Iterable<A>, B> {

    private static final FoldLeft<?, ?> INSTANCE = new FoldLeft<>();

    private FoldLeft() {
    }

    @Override
    public B checkedApply(Fn2<? super B, ? super A, ? extends B> fn, B acc, Iterable<A> as) {
        B accumulation = acc;
        for (A a : as)
            accumulation = fn.apply(accumulation, a);
        return accumulation;
    }

    @SuppressWarnings("unchecked")
    public static <A, B> FoldLeft<A, B> foldLeft() {
        return (FoldLeft<A, B>) INSTANCE;
    }

    public static <A, B> Fn2<B, Iterable<A>, B> foldLeft(Fn2<? super B, ? super A, ? extends B> fn) {
        return FoldLeft.<A, B>foldLeft().apply(fn);
    }

    public static <A, B> Fn1<Iterable<A>, B> foldLeft(Fn2<? super B, ? super A, ? extends B> fn, B acc) {
        return FoldLeft.<A, B>foldLeft(fn).apply(acc);
    }

    public static <A, B> B foldLeft(Fn2<? super B, ? super A, ? extends B> fn, B acc, Iterable<A> as) {
        return FoldLeft.<A, B>foldLeft(fn, acc).apply(as);
    }
}
