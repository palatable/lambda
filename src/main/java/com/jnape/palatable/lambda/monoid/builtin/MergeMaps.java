package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiMonoidFactory;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * A {@link Monoid} instance formed by {@link Map#merge(K, V, BiFunction)} and a semigroup over <code>V</code>.
 * Combines together multiple maps using the provided semigroup for key collisions.
 *
 * @param <K> The key parameter type of the Map
 * @param <V> The value parameter type of the Map
 * @see Monoid
 * @see java.util.Map
 */
public class MergeMaps<K, V> implements BiMonoidFactory<Supplier<Map<K, V>>, Semigroup<V>, Map<K, V>> {
    private MergeMaps() {
    }

    @Override
    public Monoid<Map<K, V>> apply(Supplier<Map<K, V>> mSupplier, Semigroup<V> semigroup) {
        return Monoid.<Map<K, V>>monoid((x, y) -> {
            Map<K, V> copy = mSupplier.get();
            copy.putAll(x);
            y.forEach((k, v) -> copy.merge(k, v, semigroup.toBiFunction()));
            return copy;
        }, mSupplier);
    }

    public static <A, B> MergeMaps<A, B> mergeMaps() {
        return new MergeMaps<>();
    }

    public static <A, B> MonoidFactory<Semigroup<B>, Map<A, B>> mergeMaps(Supplier<Map<A, B>> mSupplier) {
        return MergeMaps.<A, B>mergeMaps().apply(mSupplier);
    }

    public static <A, B> Monoid<Map<A, B>> mergeMaps(Supplier<Map<A, B>> mSupplier, Semigroup<B> semigroup) {
        return mergeMaps(mSupplier).apply(semigroup);
    }

    public static <A, B> Fn1<Map<A, B>, Map<A, B>> mergeMaps(Supplier<Map<A, B>> mSupplier, Semigroup<B> semigroup, Map<A, B> x) {
        return mergeMaps(mSupplier, semigroup).apply(x);
    }

    public static <A, B> Map<A, B> mergeMaps(Supplier<Map<A, B>> mSupplier, Semigroup<B> semigroup, Map<A, B> x, Map<A, B> y) {
        return mergeMaps(mSupplier, semigroup, x).apply(y);
    }
}
