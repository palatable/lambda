package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.MagnetizeBy.magnetizeBy;

/**
 * {@link Magnetize} an {@link Iterable} using value equality as the magnetizing function.
 *
 * @param <A> the Iterable element type
 */
public final class Magnetize<A> implements Fn1<Iterable<A>, Iterable<Iterable<A>>> {

    private static final Magnetize<?> INSTANCE = new Magnetize<>();

    private Magnetize() {
    }

    @Override
    public Iterable<Iterable<A>> checkedApply(Iterable<A> as) {
        return magnetizeBy(eq(), as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Magnetize<A> magnetize() {
        return (Magnetize<A>) INSTANCE;
    }

    public static <A> Iterable<Iterable<A>> magnetize(Iterable<A> as) {
        return Magnetize.<A>magnetize().apply(as);
    }
}
