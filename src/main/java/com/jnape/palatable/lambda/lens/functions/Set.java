package com.jnape.palatable.lambda.lens.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.lens.functions.Over.over;

/**
 * Given a lens, a "smaller" value <code>B</code>, and a "larger" value <code>S</code>, produce a <code>T</code> by
 * lifting the lens into {@link Identity}.
 * <p>
 * More idiomatically, this function can be used to treat a lens as a "setter" of <code>B</code>s on <code>S</code>s,
 * potentially producing a different "larger" value, <code>T</code>.
 *
 * @param <S> the type of the larger value
 * @param <T> the type of the larger updated value
 * @param <A> the type of the smaller retrieving value (unused, but necessary for composition)
 * @param <B> the type of the smaller setting value
 * @see Over
 * @see View
 */
public final class Set<S, T, A, B> implements Fn3<Lens<S, T, A, B>, B, S, T> {

    private Set() {
    }

    @Override
    public T apply(Lens<S, T, A, B> lens, B b, S s) {
        return over(lens, constantly(b), s);
    }

    public static <S, T, A, B> Set<S, T, A, B> set() {
        return new Set<>();
    }

    public static <S, T, A, B> Fn2<B, S, T> set(Lens<S, T, A, B> lens) {
        return Set.<S, T, A, B>set().apply(lens);
    }

    public static <S, T, A, B> Fn1<S, T> set(Lens<S, T, A, B> lens, B b) {
        return set(lens).apply(b);
    }

    public static <S, T, A, B> T set(Lens<S, T, A, B> lens, B b, S s) {
        return set(lens, b).apply(s);
    }
}
