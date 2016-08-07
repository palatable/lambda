package com.jnape.palatable.lambda.lens.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.function.Function;

public final class Over<S, T, A, B> implements Fn3<Lens<S, T, A, B>, Function<? super A, ? extends B>, S, T> {

    private Over() {
    }

    @Override
    public T apply(Lens<S, T, A, B> lens, Function<? super A, ? extends B> fn, S s) {
        return lens.<Identity<T>, Identity<B>>fix()
                .apply(fn.andThen((Function<B, Identity<B>>) Identity::new), s)
                .runIdentity();
    }

    public static <S, T, A, B> Over<S, T, A, B> over() {
        return new Over<>();
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
