package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiMonoidFactory;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * A {@link Monoid} instance formed by {@link java.util.Map#merge(Object, Object, BiFunction)} and a semigroup over
 * <code>V</code>. Combines together multiple maps using the provided semigroup for key collisions.
 *
 * @param <K> The key parameter type of the Map
 * @param <V> The value parameter type of the Map
 * @see Monoid
 * @see java.util.Map
 */
public final class MergeMaps<K, V> implements BiMonoidFactory<Fn0<Map<K, V>>, Semigroup<V>, Map<K, V>> {

    private static final MergeMaps<?, ?> INSTANCE = new MergeMaps<>();

    private MergeMaps() {
    }

    @Override
    public Monoid<Map<K, V>> checkedApply(Fn0<Map<K, V>> mFn0, Semigroup<V> semigroup) {
        return Monoid.<Map<K, V>>monoid((x, y) -> {
            Map<K, V> copy = mFn0.apply();
            copy.putAll(x);
            y.forEach((k, v) -> copy.merge(k, v, semigroup.toBiFunction()));
            return copy;
        }, mFn0);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> MergeMaps<K, V> mergeMaps() {
        return (MergeMaps<K, V>) INSTANCE;
    }

    public static <K, V> MonoidFactory<Semigroup<V>, Map<K, V>> mergeMaps(Fn0<Map<K, V>> mFn0) {
        return MergeMaps.<K, V>mergeMaps().apply(mFn0);
    }

    public static <K, V> Monoid<Map<K, V>> mergeMaps(Fn0<Map<K, V>> mFn0, Semigroup<V> semigroup) {
        return mergeMaps(mFn0).apply(semigroup);
    }

    public static <K, V> Fn1<Map<K, V>, Map<K, V>> mergeMaps(Fn0<Map<K, V>> mFn0, Semigroup<V> semigroup,
                                                             Map<K, V> x) {
        return mergeMaps(mFn0, semigroup).apply(x);
    }

    public static <K, V> Map<K, V> mergeMaps(Fn0<Map<K, V>> mFn0, Semigroup<V> semigroup, Map<K, V> x,
                                             Map<K, V> y) {
        return mergeMaps(mFn0, semigroup, x).apply(y);
    }
}
