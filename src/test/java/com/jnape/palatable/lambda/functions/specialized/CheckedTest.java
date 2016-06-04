package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.specialized.unchecked.CheckedMonadicFunction;
import com.jnape.palatable.lambda.functions.specialized.unchecked.CheckedSupplier;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.specialized.Checked.checked;


public class CheckedTest {

    @Test(expected = IllegalStateException.class)
    public void checkedSupplier() {
        CheckedSupplier<RuntimeException, String> supplier = checked(() -> {
            throw new IllegalStateException("expected");
        });
        supplier.get();
    }

    @Test(expected = IllegalStateException.class)
    public void uncheckedMonadicFunction() {
        CheckedMonadicFunction<String, String> fn = checked(x -> {
            throw new IllegalStateException("expected");
        });
        fn.apply("foo");
    }
}