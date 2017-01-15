package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Given an <code>Iterable</code> of <code>A</code>s, a starting value <code>B</code>, and a <code>{@link
 * BiFunction}&lt;A, B, B&gt;</code>, iteratively accumulate over the <code>Iterable</code>, ultimately returning a
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
public final class FoldRight<A, B> implements Fn3<BiFunction<? super A, ? super B, ? extends B>, B, Iterable<A>, B> {

    private static final FoldRight INSTANCE = new FoldRight();

    private FoldRight() {
    }

    @Override
    public B apply(BiFunction<? super A, ? super B, ? extends B> fn, B acc, Iterable<A> as) {
        return foldLeft((b, a) -> fn.apply(a, b), acc, reverse(as));
    }

    @SuppressWarnings("unchecked")
    public static <A, B> FoldRight<A, B> foldRight() {
        return INSTANCE;
    }

    public static <A, B> Fn2<B, Iterable<A>, B> foldRight(BiFunction<? super A, ? super B, ? extends B> fn) {
        return FoldRight.<A, B>foldRight().apply(fn);
    }

    public static <A, B> Fn1<Iterable<A>, B> foldRight(BiFunction<? super A, ? super B, ? extends B> fn, B acc) {
        return FoldRight.<A, B>foldRight(fn).apply(acc);
    }

    public static <A, B> B foldRight(BiFunction<? super A, ? super B, ? extends B> fn, B acc, Iterable<A> as) {
        return FoldRight.<A, B>foldRight(fn, acc).apply(as);
    }
}
