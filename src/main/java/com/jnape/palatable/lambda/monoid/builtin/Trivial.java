package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Constantly;
import com.jnape.palatable.lambda.monoid.Monoid;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

/**
 * The trivial {@link Unit} {@link Monoid} formed under {@link Constantly constantly}.
 */
public final class Trivial implements Monoid<Unit> {

    private static final Trivial INSTANCE = new Trivial();

    private Trivial() {
    }

    @Override
    public Unit identity() {
        return UNIT;
    }

    @Override
    public Unit checkedApply(Unit x, Unit y) throws Throwable {
        return y;
    }

    public static Trivial trivial() {
        return INSTANCE;
    }

    public static Fn1<Unit, Unit> trivial(Unit x) {
        return trivial().apply(x);
    }

    public static Unit trivial(Unit x, Unit y) {
        return trivial(x).apply(y);
    }
}
