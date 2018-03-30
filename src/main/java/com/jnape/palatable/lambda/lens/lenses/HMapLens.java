package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hmap.HMap;
import com.jnape.palatable.lambda.adt.hmap.TypeSafeKey;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link HMap}s.
 */
public final class HMapLens {
    private HMapLens() {
    }

    /**
     * A lens that focuses on a value at a {@link TypeSafeKey}&lt;A&gt; in an {@link HMap}, as a {@link Maybe}.
     *
     * @param key the key
     * @param <A> the value type at the key
     * @return the lens
     */
    public static <A> Lens.Simple<HMap, Maybe<A>> valueAt(TypeSafeKey<A> key) {
        return simpleLens(m -> m.get(key), (m, maybeA) -> maybeA.fmap(a -> m.put(key, a)).orElseGet(() -> m.remove(key)));
    }
}
