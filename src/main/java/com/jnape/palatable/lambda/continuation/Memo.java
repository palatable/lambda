package com.jnape.palatable.lambda.continuation;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class Memo<T> {
    private final AtomicReference<T> valueRef;
    private final Supplier<T>        fn;

    private Memo(Supplier<T> fn) {
        this.fn = fn;
        valueRef = new AtomicReference<>();
    }

    public T get() {
        if (valueRef.get() == null)
            valueRef.compareAndSet(null, fn.get());
        return valueRef.get();
    }

    public static <T> Memo<T> memoize(Supplier<T> fn) {
        return new Memo<>(fn);
    }

    public static <T> Memo<T> memoize(T result) {
        return memoize(() -> result);
    }


}
