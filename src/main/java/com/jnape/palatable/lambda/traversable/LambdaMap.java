package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToMap.toMap;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static java.util.Collections.emptyMap;

/**
 * Extension point for {@link Map} to adapt lambda core types like {@link Functor} and {@link Traversable}.
 *
 * @param <A> the {@link Map} element type
 * @see LambdaIterable
 */
public final class LambdaMap<A, B> implements Functor<B, LambdaMap<A, ?>>, Traversable<B, LambdaMap<A, ?>> {
    private final Map<A, B> map;

    private LambdaMap(Map<A, B> map) {
        this.map = map;
    }

    /**
     * Unwrap the underlying {@link Map}.
     *
     * @return the wrapped {@link Map}
     */
    public Map<A, B> unwrap() {
        return map;
    }

    @Override
    public <C> LambdaMap<A, C> fmap(Function<? super B, ? extends C> fn) {
        return wrap(toMap(HashMap::new, map(entry -> tuple(entry.getKey(), fn.apply(entry.getValue())), map.entrySet())));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, App extends Applicative, TravC extends Traversable<C, LambdaMap<A, ?>>, AppC extends Applicative<C, App>, AppTrav extends Applicative<TravC, App>> AppTrav traverse(
            Function<? super B, ? extends AppC> fn, Function<? super TravC, ? extends AppTrav> pure) {
        return foldLeft(Fn2.<AppTrav, Map.Entry<A, AppC>, AppTrav>fn2(appTrav -> into((k, appV) -> (AppTrav) appTrav.<TravC>zip(appV.fmap(v -> m -> {
                            ((LambdaMap<A, C>) m).unwrap().put(k, v);
                            return (TravC) m;
                        })))).toBiFunction(),
                        pure.apply((TravC) LambdaMap.<A, C>wrap(new HashMap<>())),
                        this.<AppC>fmap(fn).unwrap().entrySet());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof LambdaMap && Objects.equals(map, ((LambdaMap) other).map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return "LambdaMap{map=" + map + '}';
    }

    /**
     * Wrap a {@link Map} in a {@link LambdaMap}.
     *
     * @param map the {@link Map}
     * @param <A> the key type
     * @param <B> the value type
     * @return the {@link Map} wrapped in a {@link LambdaMap}
     */
    public static <A, B> LambdaMap<A, B> wrap(Map<A, B> map) {
        return new LambdaMap<>(map);
    }

    /**
     * Construct an empty {@link LambdaMap} by wrapping {@link Collections#emptyMap()}
     *
     * @param <A> the key type
     * @param <B> the value type
     * @return an empty {@link LambdaMap}
     */
    public static <A, B> LambdaMap<A, B> empty() {
        return wrap(emptyMap());
    }
}
