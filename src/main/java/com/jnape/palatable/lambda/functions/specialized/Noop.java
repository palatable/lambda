package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Effect;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

/**
 * As the name might suggest, this is an {@link Effect} that, *ahem*, has no effect.
 *
 * @param <A> the argument type
 */
public final class Noop<A> implements Effect<A> {
    private static final Noop INSTANCE = new Noop();

    private Noop() {
    }

    @Override
    public Unit apply(A a) {
        return UNIT;
    }

    /**
     * Static factory method that returns the singleton {@link Noop} instance.
     *
     * @param <A> the argument type
     * @return the singleton {@link Noop} instance
     */
    @SuppressWarnings("unchecked")
    public static <A> Noop<A> noop() {
        return INSTANCE;
    }
}
