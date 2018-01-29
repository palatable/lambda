package com.jnape.palatable.lambda.functions.specialized.checked;

class Runtime {

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> RuntimeException throwChecked(Throwable t) throws T {
        throw (T) t;
    }
}
