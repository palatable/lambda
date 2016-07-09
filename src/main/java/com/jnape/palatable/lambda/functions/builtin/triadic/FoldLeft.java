package com.jnape.palatable.lambda.functions.builtin.triadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;

/**
 * Given an <code>Iterable</code> of <code>A</code>s, a starting value <code>B</code>, and a <code>{@link
 * DyadicFunction}&lt;B, A, B&gt;</code>, iteratively accumulate over the <code>Iterable</code>, ultimately returning a
 * final <code>B</code> value. If the <code>Iterable</code> is empty, just return the starting <code>B</code> value.
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
public final class FoldLeft<A, B> implements TriadicFunction<DyadicFunction<? super B, ? super A, ? extends B>, B, Iterable<A>, B> {

    private FoldLeft() {
    }

    @Override
    public B apply(DyadicFunction<? super B, ? super A, ? extends B> function, B initialAccumulation,
                   Iterable<A> as) {
        B accumulation = initialAccumulation;
        for (A a : as)
            accumulation = function.apply(accumulation, a);
        return accumulation;
    }

    public static <A, B> FoldLeft<A, B> foldLeft() {
        return new FoldLeft<>();
    }

    public static <A, B> DyadicFunction<B, Iterable<A>, B> foldLeft(
            DyadicFunction<? super B, ? super A, ? extends B> function) {
        return FoldLeft.<A, B>foldLeft().apply(function);
    }

    public static <A, B> MonadicFunction<Iterable<A>, B> foldLeft(
            DyadicFunction<? super B, ? super A, ? extends B> function, B initialAccumulation) {
        return FoldLeft.<A, B>foldLeft(function).apply(initialAccumulation);
    }

    public static <A, B> B foldLeft(DyadicFunction<? super B, ? super A, ? extends B> function, B initialAccumulation,
                                    Iterable<A> as) {
        return FoldLeft.<A, B>foldLeft(function, initialAccumulation).apply(as);
    }
}
