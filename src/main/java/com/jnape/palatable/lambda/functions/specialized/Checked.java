package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.specialized.unchecked.CheckedMonadicFunction;
import com.jnape.palatable.lambda.functions.specialized.unchecked.CheckedSupplier;

import java.util.function.Supplier;

public class Checked {

    public static <T> CheckedSupplier<RuntimeException, T> checked(Supplier<T> supplier) {
        return supplier::get;
    }

    public static <A, B> CheckedMonadicFunction<A, B> checked(MonadicFunction<A, B> fn) {
        return fn::apply;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> RuntimeException throwChecked(Throwable ex) throws T {
        throw (T) ex;
    }
}
