package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.monoid.Monoid;

/**
 * Logical exclusive-or. Equivalent to logical <code>^</code>.
 * <p>
 * Note that this implementation behaves as a cascade of binary exclusive-or operations, as is the only possible
 * monoidal behavior when applied to an unknown number of inputs.
 *
 * @see Or
 * @see And
 */
public final class Xor implements Monoid<Boolean>, BiPredicate<Boolean, Boolean> {

    private static final Xor INSTANCE = new Xor();

    private Xor() {
    }

    @Override
    public Boolean identity() {
        return false;
    }

    @Override
    public Boolean checkedApply(Boolean x, Boolean y) {
        return x ^ y;
    }

    @Override
    public Xor flip() {
        return this;
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
