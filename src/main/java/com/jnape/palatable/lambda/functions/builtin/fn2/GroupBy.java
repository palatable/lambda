package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Given an <code>Iterable&lt;V&gt;</code> <code>vs</code> and a key function <code>V -> K</code> <code>f</code>, fold
 * <code>vs</code> into a <code>Map&lt;K, List&lt;V&gt;&gt;</code> by applying <code>f</code> to each element of
 * <code>vs</code>, retaining values that map to the same key in a list, in the order they were iterated in.
 *
 * @param <K> the Map key type
 * @param <V> the Map value type
 * @see InGroupsOf
 */
public class GroupBy<K, V> implements Fn2<Function<? super V, ? extends K>, Iterable<V>, Map<K, List<V>>> {

    private static final GroupBy INSTANCE = new GroupBy();

    private GroupBy() {
    }

    @Override
    public Map<K, List<V>> apply(Function<? super V, ? extends K> keyFn, Iterable<V> vs) {
        return foldLeft((m, v) -> {
            m.computeIfAbsent(keyFn.apply(v), __ -> new ArrayList<>()).add(v);
            return m;
        }, new HashMap<K, List<V>>(), vs);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> GroupBy<K, V> groupBy() {
        return INSTANCE;
    }

    public static <K, V> Fn1<Iterable<V>, Map<K, List<V>>> groupBy(Function<? super V, ? extends K> keyFn) {
        return GroupBy.<K, V>groupBy().apply(keyFn);
    }

    public static <K, V> Map<K, List<V>> groupBy(Function<? super V, ? extends K> keyFn, Iterable<V> vs) {
        return GroupBy.<K, V>groupBy(keyFn).apply(vs);
    }
}