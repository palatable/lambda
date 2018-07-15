package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

/**
 * A function taking "no arguments", implemented as an <code>{@link Fn1}&lt;{@link Unit}, A&gt;</code>.
 *
 * @param <A> the result type
 * @see Fn1
 * @see Supplier
 */
@FunctionalInterface
public interface Effect<A> extends Fn1<A, Unit>, Consumer<A> {

    @Override
    default void accept(A a) {
        apply(a);
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
        return a -> {
            Consumer.super.andThen(after).accept(a);
            return UNIT;
        };
    }
}
