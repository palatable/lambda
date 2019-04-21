package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.adt.choice.Choice2;

import java.util.function.Function;

/**
 * {@link Profunctor} strength in the cocartesian coproduct sense: <code>p a b -&gt; p (c v a) (c v b)</code> for any
 * type <code>c</code>.
 *
 * @param <A> the type of the left parameter
 * @param <B> the type of the left parameter
 * @param <P> the unification parameter
 * @see com.jnape.palatable.lambda.functions.Fn1
 * @see Cartesian
 */
public interface Cocartesian<A, B, P extends Cocartesian<?, ?, P>> extends Profunctor<A, B, P> {

    /**
     * Choose some type <code>C</code> or this profunctor's carrier types.
     *
     * @param <C> the choice type
     * @return the cocartesian-costrengthened profunctor
     */
    <C> Cocartesian<Choice2<C, A>, Choice2<C, B>, P> cocartesian();

    /**
     * Choose between the covariantly-positioned carrier type and the contravariantly-positioned carrier type. This can
     * be used to encode partial functions <code>a -> (⊥ v b)</code> as total functions <code>a -> (a v b)</code>.
     *
     * @return the profunctor with a choice
     */
    default Cocartesian<A, Choice2<A, B>, P> choose() {
        return this.<A>cocartesian().contraMap(Choice2::b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    <Z, C> Cocartesian<Z, C, P> diMap(Function<? super Z, ? extends A> lFn, Function<? super B, ? extends C> rFn);

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Cocartesian<Z, B, P> diMapL(Function<? super Z, ? extends A> fn) {
        return (Cocartesian<Z, B, P>) Profunctor.super.<Z>diMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Cocartesian<A, C, P> diMapR(Function<? super B, ? extends C> fn) {
        return (Cocartesian<A, C, P>) Profunctor.super.<C>diMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Cocartesian<Z, B, P> contraMap(Function<? super Z, ? extends A> fn) {
        return (Cocartesian<Z, B, P>) Profunctor.super.<Z>contraMap(fn);
    }
}
