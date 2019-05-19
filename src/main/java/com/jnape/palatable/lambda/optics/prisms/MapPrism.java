package com.jnape.palatable.lambda.optics.prisms;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Prism;

import java.util.HashMap;
import java.util.Map;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.optics.Prism.Simple.adapt;
import static com.jnape.palatable.lambda.optics.Prism.prism;
import static java.util.Collections.singletonMap;

/**
 * {@link Prism Prisms} for {@link Map Maps}.
 */
public final class MapPrism {
    private MapPrism() {
    }

    /**
     * A {@link Prism} that focuses on the value at a key in a {@link Map}, and produces an instance of <code>M</code>
     * on the way back out.
     *
     * @param copyFn the copy function
     * @param k      the key to focus on
     * @param <M>    the {@link Map} subtype
     * @param <K>    the key type
     * @param <V>    the value type
     * @return the {@link Prism}
     */
    public static <M extends Map<K, V>, K, V> Prism<Map<K, V>, M, V, V> valueAt(Fn1<Map<K, V>, M> copyFn, K k) {
        return prism(m -> maybe(m.get(k)).toEither(copyFn.thunk(m)),
                     v -> copyFn.apply(singletonMap(k, v)));
    }

    /**
     * A {@link Prism} that focuses on the value at a key in a {@link Map} making no guarantees about the {@link Map}
     * interface.
     *
     * @param k   the key to focus on
     * @param <K> the key type
     * @param <V> the value type
     * @return the {@link Prism}
     */
    public static <K, V> Prism.Simple<Map<K, V>, V> valueAt(K k) {
        return adapt(valueAt(HashMap::new, k));
    }
}
