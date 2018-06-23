package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.builtin.Cont;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

public final class Reset<R> implements Fn1<Cont<R, R>, R> {

    private static final Reset INSTANCE = new Reset<>();

    private Reset() {
    }

    @Override
    public R apply(Cont<R, R> cont) {
        return cont.runCont(id());
    }

    @SuppressWarnings("unchecked")
    public static <R> Reset<R> reset() {
        return INSTANCE;
    }

    public static <R> R reset(Cont<R, R> cont) {
        return Reset.<R>reset().apply(cont);
    }
}
