package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;
import static com.jnape.palatable.lambda.lens.functions.View.view;

public final class MapLens {

    private MapLens() {
    }

    public static <K, V> Lens.Simple<Map<K, V>, Map<K, V>> asCopy() {
        return simpleLens(HashMap::new, (__, copy) -> copy);
    }

    public static <K, V> Lens.Simple<Map<K, V>, V> atKey(K k) {
        return simpleLens(m -> m.get(k), (m, v) -> {
            m.put(k, v);
            return m;
        });
    }

    public static <K, V> Lens.Simple<Map<K, V>, Set<K>> keys() {
        return simpleLens(Map::keySet, (m, ks) -> {
            Set<K> keys = m.keySet();
            keys.retainAll(ks);
            ks.removeAll(keys);
            ks.forEach(k -> m.put(k, null));
            return m;
        });
    }

    public static <K, V> Lens<Map<K, V>, Map<K, V>, Collection<V>, Fn2<K, V, V>> values() {
        return lens(Map::values, (m, kvFn) -> {
            m.entrySet().forEach(entry -> entry.setValue(kvFn.apply(entry.getKey(), entry.getValue())));
            return m;
        });
    }

    public static <K, V> Lens.Simple<Map<K, V>, Map<V, K>> inverted() {
        return simpleLens(m -> {
            Map<V, K> inverted = new HashMap<>();
            m.entrySet().forEach(entry -> inverted.put(entry.getValue(), entry.getKey()));
            return inverted;
        }, (m, im) -> {
            m.clear();
            m.putAll(view(inverted(), im));
            return m;
        });
    }
}
