package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.OptionalLens.unLiftA;
import static java.util.stream.Collectors.toSet;

/**
 * Lenses that operate on {@link Map}s.
 */
public final class MapLens {

    private MapLens() {
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a copy of a Map. Useful for composition to
     * avoid mutating a map reference.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on copies of maps
     */
    public static <K, V> Lens.Simple<Map<K, V>, Map<K, V>> asCopy() {
        return simpleLens(HashMap::new, (__, copy) -> copy);
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a value at a key in a map, as an {@link
     * Optional}.
     *
     * @param k   the key to focus on
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on the value at key, as an {@link Optional}
     */
    public static <K, V> Lens<Map<K, V>, Map<K, V>, Optional<V>, V> valueAt(K k) {
        return lens(m -> Optional.ofNullable(m.get(k)), (m, v) -> {
            m.put(k, v);
            return m;
        });
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a value at a key in a map, falling back to
     * <code>defaultV</code> if the value is missing.
     *
     * @param k            the key to focus on
     * @param defaultValue the default value to use in case of a missing value at key
     * @param <K>          the key type
     * @param <V>          the value type
     * @return a lens that focuses on the value at the key
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Lens.Simple<Map<K, V>, V> valueAt(K k, V defaultValue) {
        return unLiftA(valueAt(k), defaultValue)::apply;
    }

    /**
     * Convenience static factory method for creating a lens that focuses on the keys of a map.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on the keys of a map
     */
    public static <K, V> Lens.Simple<Map<K, V>, Set<K>> keys() {
        return simpleLens(Map::keySet, (m, ks) -> {
            Set<K> keys = m.keySet();
            keys.retainAll(ks);
            ks.removeAll(keys);
            ks.forEach(k -> m.put(k, null));
            return m;
        });
    }

    /**
     * Convenience static factory method for creating a lens that focuses on the values of a map. In the case of
     * updating the map, only the entries with a value listed in the update collection of values are kept.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on the values of a map
     */
    public static <K, V> Lens.Simple<Map<K, V>, Collection<V>> values() {
        return simpleLens(Map::values, (m, vs) -> {
            Set<V> valueSet = new HashSet<>(vs);
            Set<K> matchingKeys = m.entrySet().stream()
                    .filter(kv -> valueSet.contains(kv.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(toSet());
            m.keySet().retainAll(matchingKeys);
            return m;
        });
    }

    /**
     * Convenience static factory method for creating a lens that focuses on the inverse of a map (keys and values
     * swapped). In the case of multiple equal values becoming keys, the last one wins.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on the inverse of a map
     */
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
