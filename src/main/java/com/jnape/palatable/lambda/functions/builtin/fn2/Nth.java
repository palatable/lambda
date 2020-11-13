package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Drop.drop;
import static com.jnape.palatable.lambda.functions.builtin.fn2.$.$;

/**
 * Retrieve the element of an <code>{@link Iterable}&lt;A&gt;</code> found at ordinal index <code>k</code>
 * wrapped in an {@link Maybe}. If the index <code>k</code> is greater than or equal to the size of the
 * {@link Iterable}, the result is {@link Maybe#nothing()}.
 *
 * @param <A> the Iterable element type
 */
public class Nth<A> implements Fn2<Integer, Iterable<A>, Maybe<A>> {

    private static final Nth<?> INSTANCE = new Nth<>();

    private Nth() {
    }

    @Override
    public Maybe<A> checkedApply(Integer k, Iterable<A> as) throws Throwable {
        return k <= 0 ? nothing() : head(drop(k - 1, as));
    }

    @SuppressWarnings("unchecked")
    public static <A> Nth<A> nth() {
        return (Nth<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Maybe<A>> nth(Integer k) {
        return $(nth(), k);
    }

    public static <A> Maybe<A> nth(Integer k, Iterable<A> as) {
        return $(nth(k), as);
    }
}
