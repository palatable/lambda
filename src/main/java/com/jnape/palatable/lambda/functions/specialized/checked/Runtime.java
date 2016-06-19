package com.jnape.palatable.lambda.functions.specialized.checked;

class Runtime {

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> RuntimeException throwChecked(Throwable ex) throws T {
        throw (T) ex;
    }
}
