package com.jnape.palatable.lambda.functions.builtin.triadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.triadic.FoldLeft.foldLeft;

/**
 * Given an <code>Iterable</code> of <code>A</code>s, a starting value <code>B</code>, and a <code>{@link
 * DyadicFunction}&lt;A, B, B&gt;</code>, iteratively accumulate over the <code>Iterable</code>, ultimately returning a
 * final <code>B</code> value. If the <code>Iterable</code> is empty, just return the starting <code>B</code> value.
 * This function is the iterative inverse of {@link FoldLeft}, such that <code>foldRight(f, 0, asList(1, 2, 3, 4,
 * 5))</code> is evaluated as <code>f(f(f(f(f(0, 5), 4), 3), 2), 1)</code>.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Catamorphism"
 * target="_top">Catamorphisms</a>.
 *
 * @param <A> The Iterable element type
 * @param <B> The accumulation type
 * @see FoldLeft
 */
public final class FoldRight<A, B> implements TriadicFunction<DyadicFunction<? super A, ? super B, ? extends B>, B, Iterable<A>, B> {

    @Override
    public B apply(DyadicFunction<? super A, ? super B, ? extends B> function, B initialAccumulation, Iterable<A> as) {
        return foldLeft(function.flip(), initialAccumulation, reverse(as));
    }

    public static <A, B> FoldRight<A, B> foldRight() {
        return new FoldRight<>();
    }

    public static <A, B> DyadicFunction<B, Iterable<A>, B> foldRight(
            DyadicFunction<? super A, ? super B, ? extends B> function) {
        return FoldRight.<A, B>foldRight().apply(function);
    }

    public static <A, B> MonadicFunction<Iterable<A>, B> foldRight(
            DyadicFunction<? super A, ? super B, ? extends B> function, B initialAccumulation) {
        return FoldRight.<A, B>foldRight(function).apply(initialAccumulation);
    }

    public static <A, B> B foldRight(DyadicFunction<? super A, ? super B, ? extends B> function, B initialAccumulation,
                                     Iterable<A> as) {
        return FoldRight.<A, B>foldRight(function, initialAccumulation).apply(as);
    }
}
