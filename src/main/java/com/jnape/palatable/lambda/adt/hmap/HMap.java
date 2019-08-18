package com.jnape.palatable.lambda.adt.hmap;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static java.util.Collections.emptyMap;

/**
 * An immutable heterogeneous mapping from a parametrized type-safe key to any value, supporting a minimal mapping
 * interface.
 *
 * @see TypeSafeKey
 * @see com.jnape.palatable.lambda.adt.hlist.HList
 */
public final class HMap implements Iterable<Tuple2<TypeSafeKey<?, ?>, Object>> {

    private static final HMap EMPTY = new HMap(emptyMap());

    private final Map<TypeSafeKey<?, ?>, Object> table;

    private HMap(Map<TypeSafeKey<?, ?>, Object> table) {
        this.table = table;
    }

    /**
     * Retrieve the value at this key.
     *
     * @param key the key
     * @param <A> the value type
     * @param <B> the value type
     * @return Maybe the value at this key
     */
    @SuppressWarnings("unchecked")
    public <A, B> Maybe<B> get(TypeSafeKey<A, B> key) {
        return maybe((A) table.get(key)).fmap(view(key));
    }

    /**
     * Retrieve the value at this key, throwing a {@link NoSuchElementException} if this key is unmapped.
     *
     * @param key the key
     * @param <V> the value type
     * @return the value at this key
     * @throws NoSuchElementException if the key is unmapped
     */
    public <V> V demand(TypeSafeKey<?, V> key) throws NoSuchElementException {
        return get(key).orElseThrow(() -> new NoSuchElementException("Demanded value for key " + key
                                                                             + ", but couldn't find one."));
    }

    /**
     * Store a value for the given key.
     *
     * @param key   the key
     * @param value the value
     * @param <V>   the value type
     * @return the updated HMap
     */
    public <V> HMap put(TypeSafeKey<?, V> key, V value) {
        return alter(t -> t.put(key, view(key.mirror(), value)));
    }

    /**
     * Store all the key/value mappings in <code>hMap</code> in this HMap.
     *
     * @param hMap the other HMap
     * @return the updated HMap
     */
    public HMap putAll(HMap hMap) {
        return alter(t -> t.putAll(hMap.table));
    }

    /**
     * Determine if a key is mapped.
     *
     * @param key the key
     * @return true if the key is mapped; false otherwise
     */
    public boolean containsKey(TypeSafeKey<?, ?> key) {
        return table.containsKey(key);
    }

    /**
     * Remove a mapping from this HMap.
     *
     * @param key the key
     * @return the updated HMap
     */
    public HMap remove(TypeSafeKey<?, ?> key) {
        return alter(t -> t.remove(key));
    }

    /**
     * Remove all the key/value mappings in <code>hMap</code> from this HMap.
     *
     * @param hMap the other HMap
     * @return the updated HMap
     */
    public HMap removeAll(HMap hMap) {
        return alter(t -> t.keySet().removeAll(hMap.table.keySet()));
    }

    /**
     * Retrieve all the mapped keys.
     * <p>
     * Note that unlike with {@link Map#keySet()}, the resulting key set is not "live"; in fact
     * that is, alterations to the resulting key set have no effect on the backing {@link HMap}.
     *
     * @return a {@link Set} of all the mapped keys
     */
    public Set<TypeSafeKey<?, ?>> keys() {
        return new HashSet<>(table.keySet());
    }

    /**
     * Retrieve all the mapped values.
     *
     * @return a {@link List} of all the mapped values
     */
    public Collection<Object> values() {
        return new ArrayList<>(table.values());
    }

    /**
     * Return a standard {@link Map} view of the current snapshot of this {@link HMap}. Note that updates to either the
     * {@link Map} view or to the original {@link HMap} do not propagate to the other.
     *
     * @return the map view
     */
    public Map<TypeSafeKey<?, ?>, Object> toMap() {
        return new HashMap<>(table);
    }

    @Override
    public Iterator<Tuple2<TypeSafeKey<?, ?>, Object>> iterator() {
        return map(Tuple2::fromEntry, table.entrySet()).iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof HMap) {
            HMap that = (HMap) other;
            return Objects.equals(this.table, that.table);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(table);
    }

    @Override
    public String toString() {
        return "HMap{" +
                "table=" + table +
                '}';
    }

    private HMap alter(Consumer<Map<TypeSafeKey<?, ?>, Object>> alterFn) {
        HashMap<TypeSafeKey<?, ?>, Object> copy = new HashMap<>(table);
        alterFn.accept(copy);
        return new HMap(copy);
    }

    /**
     * Static factory method for creating an empty HMap.
     *
     * @return an empty HMap
     */
    public static HMap emptyHMap() {
        return EMPTY;
    }

    /**
     * Static factory method for creating a singleton HMap.
     *
     * @param key   the only mapped key
     * @param value the only mapped value
     * @param <V>   the only mapped value type
     * @return a singleton HMap
     */
    public static <V> HMap singletonHMap(TypeSafeKey<?, V> key, V value) {
        return emptyHMap().put(key, value);
    }

    /**
     * Static factory method for creating an HMap from two given associations.
     *
     * @param key1   the first mapped key
     * @param value1 the value mapped at key1
     * @param key2   the second mapped key
     * @param value2 the value mapped at key2
     * @param <V1>   value1's type
     * @param <V2>   value2's type
     * @return an HMap with the given associations
     */
    public static <V1, V2> HMap hMap(TypeSafeKey<?, V1> key1, V1 value1,
                                     TypeSafeKey<?, V2> key2, V2 value2) {
        return singletonHMap(key1, value1).put(key2, value2);
    }

    /**
     * Static factory method for creating an HMap from three given associations.
     *
     * @param key1   the first mapped key
     * @param value1 the value mapped at key1
     * @param key2   the second mapped key
     * @param value2 the value mapped at key2
     * @param key3   the third mapped key
     * @param value3 the value mapped at key3
     * @param <V1>   value1's type
     * @param <V2>   value2's type
     * @param <V3>   value3's type
     * @return an HMap with the given associations
     */
    public static <V1, V2, V3> HMap hMap(TypeSafeKey<?, V1> key1, V1 value1,
                                         TypeSafeKey<?, V2> key2, V2 value2,
                                         TypeSafeKey<?, V3> key3, V3 value3) {
        return hMap(key1, value1,
                    key2, value2)
                .put(key3, value3);
    }

    /**
     * Static factory method for creating an HMap from four given associations.
     *
     * @param key1   the first mapped key
     * @param value1 the value mapped at key1
     * @param key2   the second mapped key
     * @param value2 the value mapped at key2
     * @param key3   the third mapped key
     * @param value3 the value mapped at key3
     * @param key4   the fourth mapped key
     * @param value4 the value mapped at key4
     * @param <V1>   value1's type
     * @param <V2>   value2's type
     * @param <V3>   value3's type
     * @param <V4>   value4's type
     * @return an HMap with the given associations
     */
    public static <V1, V2, V3, V4> HMap hMap(TypeSafeKey<?, V1> key1, V1 value1,
                                             TypeSafeKey<?, V2> key2, V2 value2,
                                             TypeSafeKey<?, V3> key3, V3 value3,
                                             TypeSafeKey<?, V4> key4, V4 value4) {
        return hMap(key1, value1,
                    key2, value2,
                    key3, value3)
                .put(key4, value4);
    }

    /**
     * Static factory method for creating an HMap from five given associations.
     *
     * @param key1   the first mapped key
     * @param value1 the value mapped at key1
     * @param key2   the second mapped key
     * @param value2 the value mapped at key2
     * @param key3   the third mapped key
     * @param value3 the value mapped at key3
     * @param key4   the fourth mapped key
     * @param value4 the value mapped at key4
     * @param key5   the fifth mapped key
     * @param value5 the value mapped at key5
     * @param <V1>   value1's type
     * @param <V2>   value2's type
     * @param <V3>   value3's type
     * @param <V4>   value4's type
     * @param <V5>   value5's type
     * @return an HMap with the given associations
     */
    public static <V1, V2, V3, V4, V5> HMap hMap(TypeSafeKey<?, V1> key1, V1 value1,
                                                 TypeSafeKey<?, V2> key2, V2 value2,
                                                 TypeSafeKey<?, V3> key3, V3 value3,
                                                 TypeSafeKey<?, V4> key4, V4 value4,
                                                 TypeSafeKey<?, V5> key5, V5 value5) {
        return hMap(key1, value1,
                    key2, value2,
                    key3, value3,
                    key4, value4)
                .put(key5, value5);
    }

    /**
     * Static factory method for creating an HMap from six given associations.
     *
     * @param key1   the first mapped key
     * @param value1 the value mapped at key1
     * @param key2   the second mapped key
     * @param value2 the value mapped at key2
     * @param key3   the third mapped key
     * @param value3 the value mapped at key3
     * @param key4   the fourth mapped key
     * @param value4 the value mapped at key4
     * @param key5   the fifth mapped key
     * @param value5 the value mapped at key5
     * @param key6   the sixth mapped key
     * @param value6 the value mapped at key6
     * @param <V1>   value1's type
     * @param <V2>   value2's type
     * @param <V3>   value3's type
     * @param <V4>   value4's type
     * @param <V5>   value5's type
     * @param <V6>   value6's type
     * @return an HMap with the given associations
     */
    public static <V1, V2, V3, V4, V5, V6> HMap hMap(TypeSafeKey<?, V1> key1, V1 value1,
                                                     TypeSafeKey<?, V2> key2, V2 value2,
                                                     TypeSafeKey<?, V3> key3, V3 value3,
                                                     TypeSafeKey<?, V4> key4, V4 value4,
                                                     TypeSafeKey<?, V5> key5, V5 value5,
                                                     TypeSafeKey<?, V6> key6, V6 value6) {
        return hMap(key1, value1,
                    key2, value2,
                    key3, value3,
                    key4, value4,
                    key5, value5)
                .put(key6, value6);
    }

    /**
     * Static factory method for creating an HMap from seven given associations.
     *
     * @param key1   the first mapped key
     * @param value1 the value mapped at key1
     * @param key2   the second mapped key
     * @param value2 the value mapped at key2
     * @param key3   the third mapped key
     * @param value3 the value mapped at key3
     * @param key4   the fourth mapped key
     * @param value4 the value mapped at key4
     * @param key5   the fifth mapped key
     * @param value5 the value mapped at key5
     * @param key6   the sixth mapped key
     * @param value6 the value mapped at key6
     * @param key7   the seventh mapped key
     * @param value7 the value mapped at key7
     * @param <V1>   value1's type
     * @param <V2>   value2's type
     * @param <V3>   value3's type
     * @param <V4>   value4's type
     * @param <V5>   value5's type
     * @param <V6>   value6's type
     * @param <V7>   value7's type
     * @return an HMap with the given associations
     */
    public static <V1, V2, V3, V4, V5, V6, V7> HMap hMap(TypeSafeKey<?, V1> key1, V1 value1,
                                                         TypeSafeKey<?, V2> key2, V2 value2,
                                                         TypeSafeKey<?, V3> key3, V3 value3,
                                                         TypeSafeKey<?, V4> key4, V4 value4,
                                                         TypeSafeKey<?, V5> key5, V5 value5,
                                                         TypeSafeKey<?, V6> key6, V6 value6,
                                                         TypeSafeKey<?, V7> key7, V7 value7) {
        return hMap(key1, value1,
                    key2, value2,
                    key3, value3,
                    key4, value4,
                    key5, value5,
                    key6, value6)
                .put(key7, value7);
    }

    /**
     * Static factory method for creating an HMap from eight given associations.
     *
     * @param key1   the first mapped key
     * @param value1 the value mapped at key1
     * @param key2   the second mapped key
     * @param value2 the value mapped at key2
     * @param key3   the third mapped key
     * @param value3 the value mapped at key3
     * @param key4   the fourth mapped key
     * @param value4 the value mapped at key4
     * @param key5   the fifth mapped key
     * @param value5 the value mapped at key5
     * @param key6   the sixth mapped key
     * @param value6 the value mapped at key6
     * @param key7   the seventh mapped key
     * @param value7 the value mapped at key7
     * @param key8   the eighth mapped key
     * @param value8 the value mapped at key8
     * @param <V1>   value1's type
     * @param <V2>   value2's type
     * @param <V3>   value3's type
     * @param <V4>   value4's type
     * @param <V5>   value5's type
     * @param <V6>   value6's type
     * @param <V7>   value7's type
     * @param <V8>   value8's type
     * @return an HMap with the given associations
     */
    public static <V1, V2, V3, V4, V5, V6, V7, V8> HMap hMap(TypeSafeKey<?, V1> key1, V1 value1,
                                                             TypeSafeKey<?, V2> key2, V2 value2,
                                                             TypeSafeKey<?, V3> key3, V3 value3,
                                                             TypeSafeKey<?, V4> key4, V4 value4,
                                                             TypeSafeKey<?, V5> key5, V5 value5,
                                                             TypeSafeKey<?, V6> key6, V6 value6,
                                                             TypeSafeKey<?, V7> key7, V7 value7,
                                                             TypeSafeKey<?, V8> key8, V8 value8) {
        return hMap(key1, value1,
                    key2, value2,
                    key3, value3,
                    key4, value4,
                    key5, value5,
                    key6, value6,
                    key7, value7)
                .put(key8, value8);
    }

}
