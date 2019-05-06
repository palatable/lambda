package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.hmap.HMap;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

import static com.jnape.palatable.lambda.adt.hmap.HMap.emptyHMap;

/**
 * A {@link Monoid} instance formed by {@link HMap} that simply combines all the mappings.
 *
 * @see Monoid
 * @see HMap
 */
public final class PutAll implements Monoid<HMap> {

    private static final PutAll INSTANCE = new PutAll();

    private PutAll() {
    }

    @Override
    public HMap identity() {
        return emptyHMap();
    }

    @Override
    public HMap checkedApply(HMap x, HMap y) {
        return x.putAll(y);
    }

    public static PutAll putAll() {
        return INSTANCE;
    }

    public static Fn1<HMap, HMap> putAll(HMap x) {
        return putAll().apply(x);
    }

    public static HMap putAll(HMap x, HMap y) {
        return putAll(x).apply(y);
    }
}
