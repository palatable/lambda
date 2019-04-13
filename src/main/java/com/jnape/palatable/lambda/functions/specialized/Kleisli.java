package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

/**
 * The Kleisli arrow of a {@link Monad}, manifest as simply an <code>{@link Fn1}&lt;A, MB&gt;</code>. This can be
 * thought of as a fixed, portable {@link Monad#flatMap(Function)}.
 *
 * @param <A>  the input argument type
 * @param <M>  the {@link Monad} unification parameter
 * @param <MB> the output {@link Monad} type
 */
@FunctionalInterface
public interface Kleisli<A, B, M extends Monad<?, M>, MB extends Monad<B, M>> extends Fn1<A, MB> {

    /**
     * Left-to-right composition of two compatible {@link Kleisli} arrows, yielding a new {@link Kleisli} arrow.
     *
     * @param after the arrow to execute after this one
     * @param <C>   the new return parameter type
     * @param <MC>  the {@link Monad} instance to return
     * @return the composition of the two arrows as a new {@link Kleisli} arrow
     */
    default <C, MC extends Monad<C, M>> Kleisli<A, C, M, MC> andThen(Kleisli<B, C, M, MC> after) {
        return a -> apply(a).flatMap(after).coerce();
    }

    /**
     * Right-to-left composition of two compatible {@link Kleisli} arrows, yielding a new {@link Kleisli} arrow.
     *
     * @param before the arrow to execute before this one
     * @param <Z>    the new input argument type
     * @param <MA>   the {@link Monad} instance to flatMap with this arrow
     * @return the composition of the two arrows as a new {@link Kleisli} arrow
     */
    default <Z, MA extends Monad<A, M>> Kleisli<Z, B, M, MB> compose(Kleisli<Z, A, M, MA> before) {
        return z -> before.apply(z).flatMap(this).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Kleisli<Z, B, M, MB> compose(Function<? super Z, ? extends A> before) {
        return Fn1.super.compose(before)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Kleisli<A, B, M, MB> discardR(Applicative<C, Fn1<A, ?>> appB) {
        return Fn1.super.discardR(appB)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Kleisli<Z, B, M, MB> contraMap(Function<? super Z, ? extends A> fn) {
        return Fn1.super.contraMap(fn)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Kleisli<Z, B, M, MB> diMapL(Function<? super Z, ? extends A> fn) {
        return Fn1.super.diMapL(fn)::apply;
    }

    /**
     * Adapt a compatible function into a {@link Kleisli} arrow.
     *
     * @param fn   the function
     * @param <A>  the input argument type
     * @param <B>  the output parameter type
     * @param <M>  the {@link Monad} unification parameter
     * @param <MB> the returned {@link Monad} instance
     * @return the function adapted as a {@link Kleisli} arrow
     */
    static <A, B, M extends Monad<?, M>, MB extends Monad<B, M>> Kleisli<A, B, M, MB> kleisli(
            Function<? super A, ? extends MB> fn) {
        return fn::apply;
    }
}