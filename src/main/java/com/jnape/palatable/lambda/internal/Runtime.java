package com.jnape.palatable.lambda.internal;

public final class Runtime {

    private Runtime() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> RuntimeException throwChecked(Throwable t) throws T {
        throw (T) t;
    }
}
