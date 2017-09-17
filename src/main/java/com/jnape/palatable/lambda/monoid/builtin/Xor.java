package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

/**
 * Logical exclusive-or. Note that this implementation behaves as a cascade of binary exclusive-or operations, as is the
 * only possible monoidal behavior when applied to an unknown number of inputs.
 *
 * @see Or
 * @see And
 */
public final class Xor implements Monoid<Boolean> {

    private static final Xor INSTANCE = new Xor();

    @Override
    public Boolean identity() {
        return false;
    }

    @Override
    public Boolean apply(Boolean x, Boolean y) {
        return x ? !y : y;
    }

    public static Xor xor() {
        return INSTANCE;
    }

    public static Fn1<Boolean, Boolean> xor(Boolean x) {
        return xor().apply(x);
    }

    public static Boolean xor(Boolean x, Boolean y) {
        return xor(x).apply(y);
    }
}
