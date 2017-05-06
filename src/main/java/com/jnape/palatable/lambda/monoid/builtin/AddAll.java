package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Collection;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.monoid.Monoid.monoid;

/**
 * The {@link Monoid} instance formed under mutative concatenation for an arbitrary {@link Collection}. The collection
 * subtype (<code>C</code>) must support {@link Collection#addAll(Collection)}.
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.AddAll}.
 *
 * @see Monoid
 */
public final class AddAll<A, C extends Collection<A>> implements MonoidFactory<Supplier<C>, C> {

    private static final AddAll INSTANCE = new AddAll();

    private AddAll() {
    }

    @Override
    public Monoid<C> apply(Supplier<C> cSupplier) {
        Semigroup<C> semigroup = com.jnape.palatable.lambda.semigroup.builtin.AddAll.addAll();
        return monoid(semigroup, cSupplier);
    }

    @SuppressWarnings("unchecked")
    public static <A, C extends Collection<A>> AddAll<A, C> addAll() {
        return INSTANCE;
    }

    public static <A, C extends Collection<A>> Monoid<C> addAll(Supplier<C> collectionSupplier) {
        return AddAll.<A, C>addAll().apply(collectionSupplier);
    }

    public static <A, C extends Collection<A>> Fn1<C, C> addAll(Supplier<C> collectionSupplier, C xs) {
        return addAll(collectionSupplier).apply(xs);
    }

    public static <A, C extends Collection<A>> C addAll(Supplier<C> collectionSupplier, C xs, C ys) {
        return addAll(collectionSupplier, xs).apply(ys);
    }
}
