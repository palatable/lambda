package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Collection;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.monoid.Monoid.monoid;

/**
 * The {@link Monoid} instance formed under concatenation for an arbitrary {@link Collection}. The collection subtype
 * (<code>C</code>) must support {@link Collection#addAll(Collection)}.
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.Concat}.
 *
 * @see Monoid
 */
public final class Concat<A, C extends Collection<A>> implements MonoidFactory<Supplier<C>, C> {

    private static final Concat INSTANCE = new Concat();

    private Concat() {
    }

    @Override
    public Monoid<C> apply(Supplier<C> cSupplier) {
        Semigroup<C> semigroup = com.jnape.palatable.lambda.semigroup.builtin.Concat.concat();
        return monoid(semigroup, cSupplier);
    }

    @SuppressWarnings("unchecked")
    public static <A, C extends Collection<A>> Concat<A, C> concat() {
        return INSTANCE;
    }

    public static <A, C extends Collection<A>> Monoid<C> concat(Supplier<C> collectionSupplier) {
        return Concat.<A, C>concat().apply(collectionSupplier);
    }

    public static <A, C extends Collection<A>> Fn1<C, C> concat(Supplier<C> collectionSupplier, C xs) {
        return concat(collectionSupplier).apply(xs);
    }

    public static <A, C extends Collection<A>> C concat(Supplier<C> collectionSupplier, C xs, C ys) {
        return concat(collectionSupplier, xs).apply(ys);
    }
}
