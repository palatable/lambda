package com.jnape.palatable.lambda.monad.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

public final class Cont<R, A> implements Monad<A, Cont<R, ?>> {
    private final Fn1<Function<? super A, ? extends R>, R> continuation;

    public Cont(Fn1<Function<? super A, ? extends R>, R> continuation) {
        this.continuation = continuation;
    }

    public R runCont(Function<? super A, ? extends R> fn) {
        return continuation.apply(fn);
    }

    @Override
    public <B> Cont<R, B> fmap(Function<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    @Override
    public <B> Cont<R, B> zip(Applicative<Function<? super A, ? extends B>, Cont<R, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <B> Cont<R, B> discardL(Applicative<B, Cont<R, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <B> Cont<R, A> discardR(Applicative<B, Cont<R, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <B> Cont<R, B> pure(B b) {
        return new Cont<>(f -> f.apply(b));
    }

    @Override
    public <B> Cont<R, B> flatMap(Function<? super A, ? extends Monad<B, Cont<R, ?>>> f) {
        return new Cont<>(k -> runCont(f.<Cont<R, B>>andThen(Applicative::coerce).andThen(c -> c.runCont(k))));
    }
}
