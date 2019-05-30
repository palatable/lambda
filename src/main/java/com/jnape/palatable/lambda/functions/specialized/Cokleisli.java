package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Downcast;

/**
 * The Cokleisli arrow of a {@link Comonad}, as a convenience wrapper around a <code>{@link Fn1}&lt;WA, B&gt;</code>.
 * This can be thought of as a fixed, portable {@link Comonad#extend(Fn1)}.
 *
 * @param <A>  the extraction value of the input {@link Comonad}
 * @param <W>  the {@link Comonad} unification parameter
 * @param <B>  the output argument type
 */
@FunctionalInterface
public interface Cokleisli<A, B, W extends Comonad<?, W>> extends Fn1<Comonad<A, W>, B> {

    /**
     * Left-to-right composition of two compatible {@link Cokleisli} arrows, yielding a new {@link Cokleisli} arrow.
     *
     * @param after the arrow to execute after this one
     * @param <C>   the new return parameter type
     * @return the composition of the two arrows as a new {@link Cokleisli} arrow
     */
    default <C> Cokleisli<A, C, W> andThen(Cokleisli<B, C, W> after) {
        return wa -> after.apply(wa.extend(this));
    }

    /**
     * Right-to-left composition of two compatible {@link Cokleisli} arrows, yielding a new {@link Cokleisli} arrow.
     *
     * @param before the arrow to execute before this one
     * @param <Z>    the new input argument type
     * @return the composition of the two arrows as a new {@link Cokleisli} arrow
     */
    default <Z> Cokleisli<Z, B, W> compose(Cokleisli<Z, A, W> before) {
        return wz -> this.apply(wz.extend(before));
    }

    /**
     * Adapt a compatible function into a {@link Cokleisli} arrow.
     *
     * @param fn   the function
     * @param <A>  the input parameter type
     * @param <B>  the output argument type
     * @param <W>  the {@link Comonad} unification parameter
     * @param <WA> the input {@link Comonad} type
     * @return the function adapted as a {@link Cokleisli} arrow
     */
    static <A, B, W extends Comonad<?, W>, WA extends Comonad<A, W>> Cokleisli<A, B, W> cokleisli(Fn1<? super WA, ? extends B> fn) {
        return fn.contraMap((Fn1<? super Comonad<A, W>, ? extends WA>) Downcast::downcast)::apply;
    }
}