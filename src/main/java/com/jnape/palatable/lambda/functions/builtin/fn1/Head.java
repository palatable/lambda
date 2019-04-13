package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Iterator;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

/**
 * Retrieve the head element of an {@link Iterable}, wrapped in an {@link Maybe}. If the {@link Iterable} is empty, the
 * result is {@link Maybe#nothing()}.
 *
 * @param <A> the Iterable element type
 */
public final class Head<A> implements Fn1<Iterable<A>, Maybe<A>> {

    private static final Head<?> INSTANCE = new Head<>();

    private Head() {
    }

    @Override
    public Maybe<A> apply(Iterable<A> as) {
        Iterator<A> iterator = as.iterator();
        return iterator.hasNext() ? just(iterator.next()) : nothing();
    }

    @SuppressWarnings("unchecked")
    public static <A> Head<A> head() {
        return (Head<A>) INSTANCE;
    }

    public static <A> Maybe<A> head(Iterable<A> as) {
        return Head.<A>head().apply(as);
    }
}
