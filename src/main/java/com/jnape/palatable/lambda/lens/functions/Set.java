package com.jnape.palatable.lambda.lens.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

public final class Set<S, T, A, B> implements Fn3<Lens<S, T, A, B>, B, S, T> {

    private Set() {
    }

    @Override
    public T apply(Lens<S, T, A, B> lens, B b, S s) {
        return lens.<Identity<T>, Identity<B>>fix()
                .apply(constantly(b).fmap(Identity::new), s)
                .runIdentity();
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
