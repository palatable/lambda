package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

/**
 * A {@link Monoid} instance formed by <code>String</code> that concats two strings together.
 *
 * @see Monoid
 */
public final class Join implements Monoid<String> {

    private static final Join INSTANCE = new Join();

    private Join() {
    }

    @Override
    public String identity() {
        return "";
    }

    @Override
    public String checkedApply(String x, String y) {
        return x + y;
    }

    public static Join join() {
        return INSTANCE;
    }

    public static Fn1<String, String> join(String x) {
        return join().apply(x);
    }

    public static String join(String x, String y) {
        return join(x).apply(y);
    }
}
