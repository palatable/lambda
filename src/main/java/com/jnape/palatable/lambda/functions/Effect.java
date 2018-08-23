package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

/**
 * A function returning "no result", and therefore only useful as a side-effect.
 *
 * @param <A> the argument type
 * @see Fn0
 * @see Consumer
 */
@FunctionalInterface
public interface Effect<A> extends Fn1<A, Unit>, Consumer<A> {

    @Override
    default Unit apply(A a) {
        accept(a);
        return UNIT;
    }

    @Override
    default <Z> Effect<Z> diMapL(Function<? super Z, ? extends A> fn) {
        return Fn1.super.diMapL(fn)::apply;
    }

    @Override
    default <Z> Effect<Z> contraMap(Function<? super Z, ? extends A> fn) {
        return Fn1.super.contraMap(fn)::apply;
    }

    @Override
    default <Z> Effect<Z> compose(Function<? super Z, ? extends A> before) {
        return Fn1.super.compose(before)::apply;
    }

    @Override
    default <C> Effect<A> discardR(Applicative<C, Fn1<A, ?>> appB) {
        return Fn1.super.discardR(appB)::apply;
    }

    @Override
    default Effect<A> andThen(Consumer<? super A> after) {
        return Consumer.super.andThen(after)::accept;
    }

    /**
     * Static factory method to aid in inference.
     *
     * @param effect the effect
     * @param <A>    the effect argument type
     * @return the effect
     */
    static <A> Effect<A> effect(Consumer<A> effect) {
        return effect::accept;
    }

    /**
     * Create an {@link Effect} from a {@link Runnable};
     *
     * @param runnable the runnable
     * @return the effect
     */
    static Effect<Unit> effect(Runnable runnable) {
        return effect(__ -> runnable.run());
    }
}