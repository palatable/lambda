package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.specialized.SideEffect;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.internal.Runtime;
import com.jnape.palatable.lambda.io.IO;

import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.specialized.SideEffect.NOOP;
import static com.jnape.palatable.lambda.io.IO.io;

/**
 * A function returning "no result", and therefore only useful as a side-effect.
 *
 * @param <A> the argument type
 * @see Fn0
 */
@FunctionalInterface
public interface Effect<A> extends Fn1<A, IO<Unit>> {

    @Override
    IO<Unit> checkedApply(A a) throws Throwable;

    /**
     * Convert this {@link Effect} to a java {@link Consumer}
     *
     * @return the {@link Consumer}
     */
    default Consumer<A> toConsumer() {
        return a -> apply(a).unsafePerformIO();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default IO<Unit> apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }

    /**
     * Left-to-right composition of {@link Effect Effects}.
     *
     * @param effect the other {@link Effect}
     * @return the composed {@link Effect}
     */
    default Effect<A> andThen(Effect<A> effect) {
        return a -> apply(a).flatMap(constantly(effect.apply(a)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Effect<Z> diMapL(Fn1<? super Z, ? extends A> fn) {
        return effect(Fn1.super.diMapL(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Effect<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return effect(Fn1.super.contraMap(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Effect<A> discardR(Applicative<C, Fn1<A, ?>> appB) {
        return effect(Fn1.super.discardR(appB));
    }

    /**
     * Static factory method to create an {@link Effect} from a java {@link Consumer}.
     *
     * @param consumer the {@link Consumer}
     * @param <A>      the input type
     * @return the {@link Effect}
     */
    static <A> Effect<A> fromConsumer(Consumer<A> consumer) {
        return a -> io(() -> consumer.accept(a));
    }

    /**
     * Create an {@link Effect} from a {@link SideEffect};
     *
     * @param sideEffect the {@link SideEffect}
     * @return the {@link Effect}
     */
    static <A> Effect<A> effect(SideEffect sideEffect) {
        return effect(constantly(io(sideEffect)));
    }

    /**
     * Create an {@link Effect} that accepts an input and does nothing;
     *
     * @return the noop {@link Effect}
     */
    @SuppressWarnings("unused")
    static <A> Effect<A> noop() {
        return effect(NOOP);
    }

    /**
     * Create an {@link Effect} from an {@link Fn1} that yields an {@link IO}.
     *
     * @param fn  the function
     * @param <A> the effect argument type
     * @return the effect
     */
    static <A> Effect<A> effect(Fn1<? super A, ? extends IO<?>> fn) {
        return fn.fmap(io -> io.fmap(constantly(UNIT)))::apply;
    }
}