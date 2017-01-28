package com.jnape.palatable.lambda.lens.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.function.Function;

/**
 * Given a lens, a function from <code>A</code> to <code>B</code>, and a "larger" value <code>S</code>, produce a
 * <code>T</code> by retrieving the <code>A</code> from the <code>S</code>, applying the function, and updating the
 * <code>S</code> with the <code>B</code> resulting from the function.
 * <p>
 * This function is similar to {@link Set}, except that it allows the setting value <code>B</code> to be derived from
 * <code>S</code> via function application, rather than provided.
 *
 * @param <S> the type of the larger value
 * @param <T> the type of the larger updated value
 * @param <A> the type of the smaller retrieving value
 * @param <B> the type of the smaller setting value
 * @see Set
 * @see View
 */
public final class Over<S, T, A, B> implements Fn3<Lens<S, T, A, B>, Function<? super A, ? extends B>, S, T> {

    private static final Over INSTANCE = new Over();

    private Over() {
    }

    @Override
    public T apply(Lens<S, T, A, B> lens, Function<? super A, ? extends B> fn, S s) {
        return lens.<Identity<T>, Identity<B>>fix()
                .apply(fn.andThen((Function<B, Identity<B>>) Identity::new), s)
                .runIdentity();
    }

    @SuppressWarnings("unchecked")
    public static <S, T, A, B> Over<S, T, A, B> over() {
        return (Over<S, T, A, B>) INSTANCE;
    }

    public static <S, T, A, B> Fn2<Function<? super A, ? extends B>, S, T> over(
            Lens<S, T, A, B> lens) {
        return Over.<S, T, A, B>over().apply(lens);
    }

    public static <S, T, A, B> Fn1<S, T> over(Lens<S, T, A, B> lens,
                                              Function<? super A, ? extends B> fn) {
        return over(lens).apply(fn);
    }

    public static <S, T, A, B> T over(Lens<S, T, A, B> lens, Function<? super A, ? extends B> fn, S s) {
        return over(lens, fn).apply(s);
    }
}
