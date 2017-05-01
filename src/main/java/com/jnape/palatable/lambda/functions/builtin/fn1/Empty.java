package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.specialized.Predicate;

/**
 * A predicate that returns true if <code>as</code> is empty; false otherwise.
 *
 * @param <A> the iterable element type
 */
public final class Empty<A> implements Predicate<Iterable<A>> {

    private static final Empty INSTANCE = new Empty();

    private Empty() {
    }

    @Override
    public Boolean apply(Iterable<A> as) {
        return !as.iterator().hasNext();
    }

    @SuppressWarnings("unchecked")
    public static <A> Empty<A> empty() {
        return INSTANCE;
    }

    public static <A> Boolean empty(Iterable<A> as) {
        return Empty.<A>empty().test(as);
    }
}
