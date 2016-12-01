package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Collection;

/**
 * The {@link Semigroup} instance formed under concatenation for an arbitrary {@link Collection}. The collection subtype
 * (<code>C</code>) must support {@link Collection#addAll(Collection)}.
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.Concat}.
 *
 * @see Semigroup
 */
public final class Concat<A, C extends Collection<A>> implements Semigroup<C> {

    private static final Concat INSTANCE = new Concat();

    private Concat() {
    }

    @Override
    public C apply(C xs, C ys) {
        xs.addAll(ys);
        return xs;
    }

    @SuppressWarnings("unchecked")
    public static <A, C extends Collection<A>> Concat<A, C> concat() {
        return INSTANCE;
    }

    public static <A, C extends Collection<A>> Fn1<C, C> concat(C xs) {
        return Concat.<A, C>concat().apply(xs);
    }

    public static <A, C extends Collection<A>> C concat(C xs, C ys) {
        return concat(xs).apply(ys);
    }
}
