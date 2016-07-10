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
 * A heterogeneous mapping from a parametrized type-safe key to any value, supporting a minimal mapping interface.
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

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(TypeSafeKey<T> key) {
        return Optional.ofNullable((T) table.get(key));
    }

    public <V> V demand(TypeSafeKey<V> key) {
        return get(key).orElseThrow(() -> new NoSuchElementException("Demanded value for key " + key + ", but couldn't find one."));
    }

    public <V> HMap put(TypeSafeKey<V> key, V value) {
        return alter(t -> t.put(key, value));
    }

    public HMap putAll(HMap hMap) {
        return alter(t -> t.putAll(hMap.table));
    }

    public boolean containsKey(TypeSafeKey key) {
        return table.containsKey(key);
    }

    public Iterable<TypeSafeKey> keys() {
        return map(Tuple2::_1, this);
    }

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

    public static HMap emptyHMap() {
        return EMPTY;
    }

    public static <V> HMap singletonHMap(TypeSafeKey<V> key, V value) {
        return new HMap(singletonMap(key, value));
    }

    public static <V1, V2> HMap hMap(TypeSafeKey<V1> key1, V1 value1,
                                     TypeSafeKey<V2> key2, V2 value2) {
        return singletonHMap(key1, value1).put(key2, value2);
    }

    public static <V1, V2, V3> HMap hMap(TypeSafeKey<V1> key1, V1 value1,
                                         TypeSafeKey<V2> key2, V2 value2,
                                         TypeSafeKey<V3> key3, V3 value3) {
        return hMap(key1, value1, key2, value2).put(key3, value3);
    }
}
