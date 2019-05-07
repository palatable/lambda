package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Snoc;

import java.util.Collections;

import static com.jnape.palatable.lambda.functions.builtin.fn3.ScanLeft.scanLeft;

/**
 * Given an <code>{@link Iterable}&lt;A&gt;</code>, produce an
 * <code>{@link Iterable}&lt;{@link Iterable}&lt;A&gt;&gt;</code>, representing all of the subsequences of initial
 * elements, ordered by size, starting with the empty {@link Iterable}.
 * <p>
 * For example, <code>inits(asList(1,2,3))</code> would iterate <code>[]</code>, <code>[1]</code>, <code>[1,2]</code>,
 * and <code>[1,2,3]</code>.
 *
 * @param <A> the Iterable element type
 */
public final class Inits<A> implements Fn1<Iterable<A>, Iterable<Iterable<A>>> {

    private static final Inits<?> INSTANCE = new Inits<>();

    private Inits() {
    }

    @Override
    public Iterable<Iterable<A>> checkedApply(Iterable<A> as) {
        return scanLeft(Snoc.<A>snoc().flip(), Collections::emptyIterator, as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Inits<A> inits() {
        return (Inits<A>) INSTANCE;
    }

    public static <A> Iterable<Iterable<A>> inits(Iterable<A> as) {
        return Inits.<A>inits().apply(as);
    }
}
