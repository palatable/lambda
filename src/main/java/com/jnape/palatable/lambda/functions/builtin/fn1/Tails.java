package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Collections;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Empty.empty;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Tail.tail;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Snoc.snoc;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Unfoldr.unfoldr;

/**
 * Given an <code>{@link Iterable}&lt;A&gt;</code>, produce an
 * <code>{@link Iterable}&lt;{@link Iterable}&lt;A&gt;&gt;</code>, representing all of the subsequences of tail
 * elements, ordered by size, starting with the full {@link Iterable}.
 * <p>
 * For example, <code>tails(asList(1,2,3))</code> would iterate <code>[1,2,3]</code>, <code>[2,3]</code>,
 * <code>[3]</code>, and <code>[]</code>.
 *
 * @param <A> the Iterable element type
 */
public final class Tails<A> implements Fn1<Iterable<A>, Iterable<Iterable<A>>> {

    private static final Tails INSTANCE = new Tails();

    private Tails() {
    }

    @Override
    public Iterable<Iterable<A>> apply(Iterable<A> as) {
        return snoc(Collections::emptyIterator,
                    unfoldr(next -> empty(next)
                            ? nothing()
                            : just(tuple(next, tail(next))), as));
    }

    @SuppressWarnings("unchecked")
    public static <A> Tails<A> tails() {
        return INSTANCE;
    }

    public static <A> Iterable<Iterable<A>> tails(Iterable<A> as) {
        return Tails.<A>tails().apply(as);
    }
}
