package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hmap.HMap;
import com.jnape.palatable.lambda.adt.hmap.TypeSafeKey;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.Map;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.monoid.builtin.Last.last;
import static com.jnape.palatable.lambda.monoid.builtin.Present.present;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.lenses.MapLens.valueAt;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;

/**
 * A {@link Monoid} instance formed by merging {@link HMap HMaps} using the chosen
 * <code>{@link TypeSafeKey} -&gt; {@link Semigroup}</code>
 * {@link MergeHMaps#key(TypeSafeKey, Semigroup) mappings}, defaulting to {@link Last} in case no
 * {@link Semigroup} has been chosen for a given {@link TypeSafeKey}.
 */
public final class MergeHMaps implements Monoid<HMap> {

    private final Map<TypeSafeKey<?, ?>, Fn2<HMap, HMap, HMap>> bindings;
    private final Φ<Fn2<HMap, HMap, HMap>>                      defaultBinding;

    private MergeHMaps(Map<TypeSafeKey<?, ?>, Fn2<HMap, HMap, HMap>> bindings,
                       Φ<Fn2<HMap, HMap, HMap>> defaultBinding) {
        this.bindings = bindings;
        this.defaultBinding = defaultBinding;
    }

    public <A> MergeHMaps key(TypeSafeKey<?, A> key, Semigroup<A> semigroup) {
        return new MergeHMaps(set(valueAt(key), just(merge(key, present(semigroup))), bindings), defaultBinding);
    }

    @Override
    public HMap identity() {
        return HMap.emptyHMap();
    }

    @Override
    public HMap checkedApply(HMap x, HMap y) throws Throwable {
        return reduceLeft(asList(x, y));
    }

    @Override
    public <B> HMap foldMap(Fn1<? super B, ? extends HMap> fn, Iterable<B> bs) {
        return FoldLeft.foldLeft((acc, m) -> FoldLeft.foldLeft((result, k) -> maybe(bindings.get(k))
                .orElseGet(() -> defaultBinding.eliminate(k))
                .apply(result, m), acc, m.keys()), identity(), map(fn, bs));
    }

    public static MergeHMaps mergeHMaps() {
        return new MergeHMaps(emptyMap(), new Φ<Fn2<HMap, HMap, HMap>>() {
            @Override
            public <A> Fn2<HMap, HMap, HMap> eliminate(TypeSafeKey<?, A> key) {
                return merge(key, last());
            }
        });
    }

    private static <A> Fn2<HMap, HMap, HMap> merge(TypeSafeKey<?, A> key, Semigroup<Maybe<A>> semigroup) {
        return (x, y) -> semigroup.apply(x.get(key), y.get(key))
                .fmap(a -> x.put(key, a))
                .orElse(x);
    }

    @SuppressWarnings({"NonAsciiCharacters"})
    private interface Φ<R> {
        <A> R eliminate(TypeSafeKey<?, A> key);
    }
}
