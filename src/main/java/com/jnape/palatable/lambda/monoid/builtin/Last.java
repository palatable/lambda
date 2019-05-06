package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.monoid.builtin.First.first;


/**
 * A {@link Monoid} instance formed by <code>{@link Maybe}&lt;A&gt;</code>. The application to two {@link Maybe}
 * values produces the last non-empty value, or {@link Maybe#nothing()} if all values are empty.
 *
 * @param <A> the Maybe value parameter type
 * @see First
 * @see Present
 * @see Monoid
 * @see Maybe
 */
public final class Last<A> implements Monoid<Maybe<A>> {
    private static final Last<?> INSTANCE = new Last<>();

    private Last() {
    }

    @Override
    public Maybe<A> identity() {
        return nothing();
    }

    @Override
    public Maybe<A> checkedApply(Maybe<A> x, Maybe<A> y) {
        return first(y, x);
    }

    @SuppressWarnings("unchecked")
    public static <A> Last<A> last() {
        return (Last<A>) INSTANCE;
    }

    public static <A> Fn1<Maybe<A>, Maybe<A>> last(Maybe<A> x) {
        return Last.<A>last().apply(x);
    }

    public static <A> Maybe<A> last(Maybe<A> x, Maybe<A> y) {
        return last(x).apply(y);
    }
}
