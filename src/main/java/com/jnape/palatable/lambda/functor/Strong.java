package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import java.util.function.Function;

/**
 * "Strong" {@link Profunctor profunctors} are profunctors that can be "strengthened" to preserve the pairing of an
 * arbitrary type under <code>dimap</code> (<code>p a b -&gt; p (c, a) (c, b)</code> for any type <code>c</code>).
 *
 * @param <A> the type of the left parameter
 * @param <B> the type of the left parameter
 * @param <S> the unification parameter
 * @see com.jnape.palatable.lambda.functions.Fn1
 */
public interface Strong<A, B, S extends Strong<?, ?, S>> extends Profunctor<A, B, S> {

    /**
     * Pair some type <code>C</code> to this profunctor's carrier types.
     *
     * @param <C> the paired type
     * @return the strengthened profunctor
     */
    <C> Strong<Tuple2<C, A>, Tuple2<C, B>, S> strengthen();

    /**
     * Pair the covariantly-positioned carrier type with the contravariantly-positioned carrier type. This can be
     * thought of as "carrying" or "inspecting" the left parameter.
     *
     * @return the profunctor with the first parameter carried
     */
    default Strong<A, Tuple2<A, B>, S> carry() {
        return this.<A>strengthen().contraMap(Tuple2::fill);
    }

    @Override
    <Z, C> Strong<Z, C, S> diMap(Function<? super Z, ? extends A> lFn, Function<? super B, ? extends C> rFn);

    @Override
    default <Z> Strong<Z, B, S> diMapL(Function<? super Z, ? extends A> fn) {
        return (Strong<Z, B, S>) Profunctor.super.<Z>diMapL(fn);
    }

    @Override
    default <C> Strong<A, C, S> diMapR(Function<? super B, ? extends C> fn) {
        return (Strong<A, C, S>) Profunctor.super.<C>diMapR(fn);
    }

    @Override
    default <Z> Strong<Z, B, S> contraMap(Function<? super Z, ? extends A> fn) {
        return (Strong<Z, B, S>) Profunctor.super.<Z>contraMap(fn);
    }
}
