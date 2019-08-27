package com.jnape.palatable.lambda.io;

import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.io.IO.io;

public final class MVar<A> {

    private final AtomicReference<A> ref;

    private MVar() {
        ref = new AtomicReference<>();
    }

    public IO<A> take() {
        return io(ref::get);
    }

    public IO<MVar<A>> put(A a) {
        return io(() -> {
            ref.set(a);
            return this;
        });
    }

    public static <A> IO<MVar<A>> newMVar() {
        return io(fn0(MVar::new));
    }
}
