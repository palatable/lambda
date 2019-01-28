package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Filter;
import com.jnape.palatable.lambda.functions.IO;
import com.jnape.palatable.lambda.lens.Iso;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Alter.alter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToMap.toMap;
import static com.jnape.palatable.lambda.lens.Lens.Simple.adapt;
import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.MaybeLens.unLiftA;
import static com.jnape.palatable.lambda.lens.lenses.MaybeLens.unLiftB;

/**
 * Lenses that operate on {@link Map}s.
 */
public final class MapLens {

    private MapLens() {
    }

    /**
     * A lens that focuses on a copy of a {@link Map} as a subtype <code>M</code>. Useful for composition to avoid
     * mutating a map reference.
     *
     * @param <M> the map subtype
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on copies of maps as a specific subtype
     */
    public static <M extends Map<K, V>, K, V> Lens<Map<K, V>, M, M, M> asCopy(
            Function<? super Map<K, V>, ? extends M> copyFn) {
        return lens(copyFn, (__, copy) -> copy);
    }

    /**
     * A lens that focuses on a copy of a Map. Useful for composition to avoid mutating a map reference.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on copies of maps
     */
    public static <K, V> Lens.Simple<Map<K, V>, Map<K, V>> asCopy() {
        return adapt(asCopy(HashMap::new));
    }

    /**
     * A lens that focuses on a value at a key in a map, as a {@link Maybe}, and produces a subtype <code>M</code> on
     * the way back out.
     *
     * @param <M> the map subtype
     * @param <K> the key type
     * @param <V> the value type
     * @param k   the key to focus on
     * @return a lens that focuses on the value at key, as a {@link Maybe}
     */
    public static <M extends Map<K, V>, K, V> Lens<Map<K, V>, M, Maybe<V>, Maybe<V>> valueAt(
            Function<? super Map<K, V>, ? extends M> copyFn, K k) {
        return lens(m -> maybe(m.get(k)), (m, maybeV) -> maybeV
                .<Fn1<M, IO<M>>>fmap(v -> alter(updated -> updated.put(k, v)))
                .orElse(alter(updated -> updated.remove(k)))
                .apply(copyFn.apply(m))
                .unsafePerformIO());
    }

    /**
     * A lens that focuses on a value at a key in a map, as a {@link Maybe}.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k   the key to focus on
     * @return a lens that focuses on the value at key, as a {@link Maybe}
     */
    public static <K, V> Lens.Simple<Map<K, V>, Maybe<V>> valueAt(K k) {
        return adapt(valueAt(HashMap::new, k));
    }

    /**
     * A lens that focuses on a value at a key in a map, falling back to <code>defaultV</code> if the value is missing.
     * <p>
     * Note that this lens is NOT lawful, since "putting back what you got changes nothing" fails for any value
     * <code>B</code> where <code>S</code> is the empty map
     *
     * @param k            the key to focus on
     * @param defaultValue the default value to use in case of a missing value at key
     * @param <K>          the key type
     * @param <V>          the value type
     * @return a lens that focuses on the value at the key
     */
    public static <K, V> Lens.Simple<Map<K, V>, V> valueAt(K k, V defaultValue) {
        return adapt(unLiftB(unLiftA(valueAt(k), defaultValue)));
    }

    /**
     * A lens that focuses on the keys of a map.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on the keys of a map
     */
    public static <K, V> Lens.Simple<Map<K, V>, Set<K>> keys() {
        return simpleLens(m -> new HashSet<>(m.keySet()), (m, ks) -> {
            HashSet<K> ksCopy = new HashSet<>(ks);
            Map<K, V> updated = new HashMap<>(m);
            Set<K> keys = updated.keySet();
            keys.retainAll(ksCopy);
            ksCopy.removeAll(keys);
            ksCopy.forEach(k -> updated.put(k, null));
            return updated;
        });
    }

    /**
     * A lens that focuses on the values of a map. In the case of updating the map, only the entries with a value listed
     * in the update collection of values are kept.
     * <p>
     * Note that this lens is NOT lawful, since "you get back what you put in" fails for all values <code>B</code> that
     * represent a non-surjective superset of the existing values in <code>S</code>.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on the values of a map
     */
    public static <K, V> Lens.Simple<Map<K, V>, Collection<V>> values() {
        return simpleLens(m -> new ArrayList<>(m.values()), (m, vs) -> {
            Map<K, V> updated = new HashMap<>(m);
            Set<V> valueSet = new HashSet<>(vs);
            Set<K> matchingKeys = Filter.<Map.Entry<K, V>>filter(kv -> valueSet.contains(kv.getValue()))
                    .andThen(map(Map.Entry::getKey))
                    .andThen(toCollection(HashSet::new))
                    .apply(updated.entrySet());
            updated.keySet().retainAll(matchingKeys);
            return updated;
        });
    }

    /**
     * A lens that focuses on the inverse of a map (keys and values swapped). In the case of multiple equal values
     * becoming keys, the last one wins.
     * <p>
     * Note that this lens is very likely to NOT be lawful, since "you get back what you put in" will fail for any keys
     * that map to the same value.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a lens that focuses on the inverse of a map
     */
    public static <K, V> Lens.Simple<Map<K, V>, Map<V, K>> inverted() {
        return simpleLens(m -> {
            Map<V, K> inverted = new HashMap<>();
            m.forEach((key, value) -> inverted.put(value, key));
            return inverted;
        }, (m, im) -> {
            Map<K, V> updated = new HashMap<>(m);
            updated.clear();
            updated.putAll(view(inverted(), im));
            return updated;
        });
    }

    /**
     * A lens that focuses on a map while mapping its values with the mapping {@link Iso}.
     * <p>
     * Note that for this lens to be lawful, <code>iso</code> must be lawful.
     *
     * @param iso  the mapping {@link Iso}
     * @param <K>  the key type
     * @param <V>  the unfocused map value type
     * @param <V2> the focused map value type
     * @return a lens that focuses on a map while mapping its values
     */
    public static <K, V, V2> Lens.Simple<Map<K, V>, Map<K, V2>> mappingValues(Iso<V, V, V2, V2> iso) {
        return simpleLens(m -> toMap(HashMap::new, map(t -> t.biMapR(view(iso)), map(Tuple2::fromEntry, m.entrySet()))),
                          (s, b) -> view(mappingValues(iso.mirror()), b));
    }
}
