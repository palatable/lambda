package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

/**
 * A {@link Monoid} instance formed by <code>Boolean</code>. Equivalent to logical <code>&&</code>.
 *
 * @see Or
 * @see Monoid
 */
public class And implements Monoid<Boolean> {

    private static final And INSTANCE = new And();

    private And() {
    }

    @Override
    public Boolean identity() {
        return true;
    }

    @Override
    public Boolean apply(Boolean x, Boolean y) {
        return x && y;
    }

    public static And and() {
        return INSTANCE;
    }

    public static Fn1<Boolean, Boolean> and(Boolean x) {
        return and().apply(x);
    }

    public static Boolean and(Boolean x, Boolean y) {
        return and(x).apply(y);
    }
}
