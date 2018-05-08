package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Map;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Given a {@link Supplier} of some {@link Map} <code>M</code>, create an instance of <code>M</code> and put
 * all of the entries in the provided <code>Iterable</code> into the instance. Note that instances of <code>M</code>
 * must support {@link java.util.Map#put} (which is to say, must not throw on invocation).
 *
 * @param <K> the key element type
 * @param <V> the value element type
 * @param <M> the resulting map type
 */
public final class ToMap<K, V, M extends Map<K, V>> implements Fn2<Supplier<M>, Iterable<? extends Map.Entry<K, V>>, M> {

    private static final ToMap INSTANCE = new ToMap<>();

    private ToMap() {
    }

    @Override
    public M apply(Supplier<M> mSupplier, Iterable<? extends Map.Entry<K, V>> entries) {
        return foldLeft((m, kv) -> {
            m.put(kv.getKey(), kv.getValue());
            return m;
        }, mSupplier.get(), entries);
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> ToMap<K, V, M> toMap() {
        return INSTANCE;
    }

    public static <K, V, M extends Map<K, V>> Fn1<Iterable<? extends Map.Entry<K, V>>, M> toMap(Supplier<M> mSupplier) {
        return ToMap.<K, V, M>toMap().apply(mSupplier);
    }

    public static <K, V, M extends Map<K, V>> M toMap(Supplier<M> mSupplier,
                                                      Iterable<? extends Map.Entry<K, V>> entries) {
        return toMap(mSupplier).apply(entries);
    }
}
