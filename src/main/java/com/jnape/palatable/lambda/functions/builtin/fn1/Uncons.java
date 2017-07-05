package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Optional;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Tail.tail;

/**
 * Destructure an {@link Iterable} into a {@link Tuple2} of its head and tail, wrapped in an {@link Optional}. If the
 * {@link Iterable} is empty, returns <code>Optional.empty()</code>.
 *
 * @param <A> the Iterable element type
 */
public final class Uncons<A> implements Fn1<Iterable<A>, Optional<Tuple2<A, Iterable<A>>>> {

    private static final Uncons INSTANCE = new Uncons();

    private Uncons() {
    }

    @Override
    public Optional<Tuple2<A, Iterable<A>>> apply(Iterable<A> as) {
        return head(as).map(a -> tuple(a, tail(as)));
    }

    @SuppressWarnings("unchecked")
    public static <A> Uncons<A> uncons() {
        return INSTANCE;
    }

    public static <A> Optional<Tuple2<A, Iterable<A>>> uncons(Iterable<A> as) {
        return Uncons.<A>uncons().apply(as);
    }
}
