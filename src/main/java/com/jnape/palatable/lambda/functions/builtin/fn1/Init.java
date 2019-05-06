package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.iteration.InitIterator;

/**
 * Given an <code>{@link Iterable}&lt;A&gt;</code>, produce an
 * <code>{@link Iterable}&lt;A&gt;</code> of all elements but the last one.
 *
 * @param <A> the Iterable element type
 */
public final class Init<A> implements Fn1<Iterable<A>, Iterable<A>> {

    private static final Init<?> INSTANCE = new Init<>();

    private Init() {
    }

    @Override
    public Iterable<A> checkedApply(Iterable<A> as) {
        return () -> new InitIterator<>(as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Init<A> init() {
        return (Init<A>) INSTANCE;
    }

    public static <A> Iterable<A> init(Iterable<A> as) {
        return Init.<A>init().apply(as);
    }
}
