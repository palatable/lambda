package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.lens.Lens;
import com.jnape.palatable.lambda.lens.lenses.impure.ImpureMapLens;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class MapLens {

    private MapLens() {
    }

    public static <K, V> Lens.Simple<Map<K, V>, Map<K, V>> asCopy() {
        return simpleLens(HashMap::new, (__, copy) -> copy);
    }

    public static <K, V> Lens.Simple<Map<K, V>, V> atKey(K k) {
        return ImpureMapLens.<K, V>atKey(k).compose(asCopy());
    }

    public static <K, V> Lens.Simple<Map<K, V>, Set<K>> keys() {
        return ImpureMapLens.<K, V>keys().compose(asCopy());
    }

    public static <K, V> Lens<Map<K, V>, Map<K, V>, Collection<V>, Fn2<K, V, V>> values() {
        return ImpureMapLens.<K, V>values().compose(asCopy());
    }

    public static <K, V> Lens.Simple<Map<K, V>, Map<V, K>> inverted() {
        return ImpureMapLens.<K, V>inverted().compose(asCopy());
    }
}
