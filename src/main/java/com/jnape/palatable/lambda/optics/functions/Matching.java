package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Market;
import com.jnape.palatable.lambda.optics.Optic;

public final class Matching<S, T, A, B> implements
        Fn2<Optic<? super Market<A, B, ?, ?>, ? super Identity<?>, S, T, A, B>, S, Either<T, A>> {

    private static final Matching<?, ?, ?, ?> INSTANCE = new Matching<>();

    private Matching() {
    }

    @Override
    public Either<T, A> apply(Optic<? super Market<A, B, ?, ?>, ? super Identity<?>, S, T, A, B> optic, S s) {
        Market<A, B, A, Identity<B>> market = new Market<>(Identity::new, Either::right);
        return optic.<Market<A, B, ?, ?>,
                Identity<?>,
                Identity<B>,
                Identity<T>,
                Market<A, B, A, Identity<B>>,
                Market<A, B, S, Identity<T>>>
                apply(market).sta().apply(s)
                .biMapL(Identity::runIdentity)
                .match(Either::left, Either::right);
    }

    @SuppressWarnings("unchecked")
    public static <S, T, A, B> Matching<S, T, A, B> matching() {
        return (Matching<S, T, A, B>) INSTANCE;
    }

    public static <S, T, A, B> Fn1<S, Either<T, A>> matching(
            Optic<? super Market<A, B, ?, ?>, ? super Identity<?>, S, T, A, B> optic) {
        return Matching.<S, T, A, B>matching().apply(optic);
    }

    public static <S, T, A, B> Either<T, A> matching(
            Optic<? super Market<A, B, ?, ?>, ? super Identity<?>, S, T, A, B> optic, S s) {
        return matching(optic).apply(s);
    }
}
