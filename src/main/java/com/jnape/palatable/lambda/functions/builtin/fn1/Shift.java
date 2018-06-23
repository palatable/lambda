package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.builtin.Cont;

import java.util.function.Function;

public final class Shift<A, R> implements Fn1<Function<? super Function<? super A, ? extends R>, Cont<R, R>>, Cont<R, A>> {

    private static final Shift INSTANCE = new Shift();

    private Shift() {
    }

    @Override
    public Cont<R, A> apply(Function<? super Function<? super A, ? extends R>, Cont<R, R>> contFn) {
        return new Cont<>(k -> Reset.reset(contFn.apply(k)));
    }

    @SuppressWarnings("unchecked")
    public static <A, R> Shift<A, R> shift() {
        return INSTANCE;
    }

    public static <A, R> Cont<R, A> shift(Function<? super Function<? super A, ? extends R>, Cont<R, R>> contFn) {
        return Shift.<A, R>shift().apply(contFn);
    }
}
