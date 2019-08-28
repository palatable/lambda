package com.jnape.palatable.lambda.io;

import com.jnape.palatable.lambda.functions.builtin.fn1.Id;

import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse.ifThenElse;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.io.IO.monitorSync;

public final class MVar<A> {

    private final AtomicReference<A> ref;

    private MVar() {
        ref = new AtomicReference<>();
    }

    public IO<A> take() {
        return monitorSync(this, io(() -> maybe(ref.get()).peek(__ -> io(() -> ref.set(null)))))
                .flatMap(ma -> ma
                        .fmap(IO::io)
                        .orElse(take()));
    }

    public IO<MVar<A>> put(A a) {
        return monitorSync(this, io(() -> maybe(ref.get())
                .fmap(constantly(false))
                .orElseGet(() -> {
                    ref.set(a);
                    return true;
                })))
                .flatMap(ifThenElse(Id.id(), constantly(IO.io(this)), __ -> put(a)));
    }

    public static <A> IO<MVar<A>> newMVar() {
        return io(fn0(MVar::new));
    }
}
