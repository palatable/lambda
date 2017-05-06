package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Collection;

/**
 * The {@link Semigroup} instance formed under mutative concatenation for an arbitrary {@link Collection}. The
 * collection subtype (<code>C</code>) must support {@link Collection#addAll(Collection)}.
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.AddAll}.
 *
 * @see Semigroup
 */
public final class AddAll<A, C extends Collection<A>> implements Semigroup<C> {

    private static final AddAll INSTANCE = new AddAll();

    private AddAll() {
    }

    @Override
    public C apply(C xs, C ys) {
        xs.addAll(ys);
        return xs;
    }

    @SuppressWarnings("unchecked")
    public static <A, C extends Collection<A>> AddAll<A, C> addAll() {
        return INSTANCE;
    }

    public static <A, C extends Collection<A>> Fn1<C, C> addAll(C xs) {
        return AddAll.<A, C>addAll().apply(xs);
    }

    public static <A, C extends Collection<A>> C addAll(C xs, C ys) {
        return addAll(xs).apply(ys);
    }
}
