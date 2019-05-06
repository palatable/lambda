package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;

/**
 * Retrieve the last element of an {@link Iterable}, wrapped in a {@link Maybe}. If the {@link Iterable} is empty, the
 * result is {@link Maybe#nothing()}.
 *
 * @param <A> the Iterable element type
 */
public final class Last<A> implements Fn1<Iterable<A>, Maybe<A>> {

    private static final Last<?> INSTANCE = new Last<>();

    private Last() {
    }

    @Override
    public Maybe<A> checkedApply(Iterable<A> as) {
        A last = null;
        for (A a : as) {
            last = a;
        }
        return maybe(last);
    }

    @SuppressWarnings("unchecked")
    public static <A> Last<A> last() {
        return (Last<A>) INSTANCE;
    }

    public static <A> Maybe<A> last(Iterable<A> as) {
        return Last.<A>last().apply(as);
    }
}
