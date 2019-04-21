package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import java.util.function.Function;

/**
 * {@link Profunctor} strength in the cartesian sense: <code>p a b -&gt; p (c, a) (c, b)</code> for any type
 * <code>c</code>.
 *
 * @param <A> the type of the left parameter
 * @param <B> the type of the left parameter
 * @param <S> the unification parameter
 * @see com.jnape.palatable.lambda.functions.Fn1
 */
public interface Cartesian<A, B, S extends Cartesian<?, ?, S>> extends Profunctor<A, B, S> {

    /**
     * Pair some type <code>C</code> to this profunctor's carrier types.
     *
     * @param <C> the paired type
     * @return the cartesian-strengthened profunctor
     */
    <C> Cartesian<Tuple2<C, A>, Tuple2<C, B>, S> cartesian();

    /**
     * Pair the covariantly-positioned carrier type with the contravariantly-positioned carrier type. This can be
     * thought of as "carrying" or "inspecting" the left parameter.
     *
     * @return the profunctor with the first parameter carried
     */
    default Cartesian<A, Tuple2<A, B>, S> carry() {
        return this.<A>cartesian().contraMap(Tuple2::fill);
    }

    @Override
    <Z, C> Cartesian<Z, C, S> diMap(Function<? super Z, ? extends A> lFn, Function<? super B, ? extends C> rFn);

    @Override
    default <Z> Cartesian<Z, B, S> diMapL(Function<? super Z, ? extends A> fn) {
        return (Cartesian<Z, B, S>) Profunctor.super.<Z>diMapL(fn);
    }

    @Override
    default <C> Cartesian<A, C, S> diMapR(Function<? super B, ? extends C> fn) {
        return (Cartesian<A, C, S>) Profunctor.super.<C>diMapR(fn);
    }

    @Override
    default <Z> Cartesian<Z, B, S> contraMap(Function<? super Z, ? extends A> fn) {
        return (Cartesian<Z, B, S>) Profunctor.super.<Z>contraMap(fn);
    }
}
