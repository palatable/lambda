package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.optics.Optic;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.optics.functions.Over.over;

/**
 * Given an {@link Optic}, a "smaller" value <code>B</code>, and a "larger" value <code>S</code>, produce a
 * <code>T</code> by lifting the {@link Optic} into the {@link Identity} functor.
 * <p>
 * More idiomatically, this function can be used to treat an {@link Optic} as a "setter" of
 * <code>B</code>s on <code>S</code>s, potentially producing a different "larger" value, <code>T</code>.
 *
 * @param <S> the type of the larger value
 * @param <T> the type of the larger updated value
 * @param <A> the type of the smaller retrieving value (unused, but necessary for composition)
 * @param <B> the type of the smaller setting value
 * @see Over
 * @see View
 */
public final class Set<S, T, A, B> implements Fn3<Optic<? super Fn1<?, ?>, ? super Identity<?>, S, T, A, B>, B, S, T> {

    private static final Set<?, ?, ?, ?> INSTANCE = new Set<>();

    private Set() {
    }

    @Override
    public T apply(Optic<? super Fn1<?, ?>, ? super Identity<?>, S, T, A, B> optic, B b, S s) {
        return over(optic, constantly(b), s);
    }

    @SuppressWarnings("unchecked")
    public static <S, T, A, B> Set<S, T, A, B> set() {
        return (Set<S, T, A, B>) INSTANCE;
    }

    public static <S, T, A, B> Fn2<B, S, T> set(Optic<? super Fn1<?, ?>, ? super Identity<?>, S, T, A, B> optic) {
        return Set.<S, T, A, B>set().apply(optic);
    }

    public static <S, T, A, B> Fn1<S, T> set(Optic<? super Fn1<?, ?>, ? super Identity<?>, S, T, A, B> optic, B b) {
        return set(optic).apply(b);
    }

    public static <S, T, A, B> T set(Optic<? super Fn1<?, ?>, ? super Identity<?>, S, T, A, B> optic, B b, S s) {
        return set(optic, b).apply(s);
    }
}
