package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Collection;

public final class Size implements Fn1<Iterable<?>, Long> {

    private static final Size INSTANCE = new Size();

    private Size() {
    }

    @Override
    public Long checkedApply(Iterable<?> iterable) {
        if (iterable instanceof Collection)
            return (long) ((Collection) iterable).size();

        long size = 0L;
        for (Object ignored : iterable) {
            size++;
        }
        return size;
    }

    public static Size size() {
        return INSTANCE;
    }

    public static Long size(Iterable<?> iterable) {
        return size().apply(iterable);
    }
}
