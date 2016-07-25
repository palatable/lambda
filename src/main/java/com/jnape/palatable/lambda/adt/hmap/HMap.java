package com.jnape.palatable.lambda.adt.hmap;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

/**
 * An immutable heterogeneous mapping from a parametrized type-safe key to any value, supporting a minimal mapping
 * interface.
 *
 * @see TypeSafeKey
 * @see com.jnape.palatable.lambda.adt.hlist.HList
 */
public class HMap implements Iterable<Tuple2<TypeSafeKey, Object>> {

    private static final HMap EMPTY = new HMap(emptyMap());

    private final Map<TypeSafeKey, Object> table;

    HMap(Map<TypeSafeKey, Object> table) {
        this.table = table;
    }

    /**
     * Retrieve the value at this key.
     *
     * @param key the key
     * @param <T> the value type
     * @return the value at this key wrapped in an {@link Optional}, or {@link Optional#empty}.
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(TypeSafeKey<T> key) {
        return Optional.ofNullable((T) table.get(key));
    }

    /**
     * Retrieve the value at this key, throwing a {@link NoSuchElementException} if this key is unmapped.
     *
     * @param key the key
     * @param <V> the value type
     * @return the value at this key
     * @throws NoSuchElementException if the key is unmapped
     */
    public <V> V demand(TypeSafeKey<V> key) throws NoSuchElementException {
        return get(key).orElseThrow(() -> new NoSuchElementException("Demanded value for key " + key + ", but couldn't find one."));
    }

    /**
     * Store a value for the given key.
     *
     * @param key   the key
     * @param value the value
     * @param <V>   the value type
     * @return the updated HMap
     */
    public <V> HMap put(TypeSafeKey<V> key, V value) {
        return alter(t -> t.put(key, value));
    }

    /**
     * Store all the key/value mappings in <code>hMap</code> in this HMap.
     *
     * @param hMap the other hMap
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
    public boolean containsKey(TypeSafeKey key) {
        return table.containsKey(key);
    }

    /**
     * Retrieve all the mapped keys.
     *
     * @return an Iterable of all the mapped keys
     */
    public Iterable<TypeSafeKey> keys() {
        return map(Tuple2::_1, this);
    }

    /**
     * Retrieve all the mapped values.
     *
     * @return an Iterable of all the mapped values
     */
    public Iterable<Object> values() {
        return map(Tuple2::_2, this);
    }

    @Override
    public Iterator<Tuple2<TypeSafeKey, Object>> iterator() {
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

    private HMap alter(Consumer<Map<TypeSafeKey, Object>> alterFn) {
        HashMap<TypeSafeKey, Object> copy = new HashMap<>(table);
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
    public static <V> HMap singletonHMap(TypeSafeKey<V> key, V value) {
        return new HMap(singletonMap(key, value));
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
    public static <V1, V2> HMap hMap(TypeSafeKey<V1> key1, V1 value1,
                                     TypeSafeKey<V2> key2, V2 value2) {
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
    public static <V1, V2, V3> HMap hMap(TypeSafeKey<V1> key1, V1 value1,
                                         TypeSafeKey<V2> key2, V2 value2,
                                         TypeSafeKey<V3> key3, V3 value3) {
        return hMap(key1, value1, key2, value2).put(key3, value3);
    }
}
