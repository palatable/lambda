package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

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
public interface Fn0<A> extends Fn1<Unit, A>, Supplier<A> {

    /**
     * Invoke this function with {@link Unit}.
     *
     * @param unit the only allowed input
     * @return the result value
     */
    @Override
    A apply(Unit unit);

    /**
     * Apply this {@link Fn0}, supplying {@link Unit} as the input.
     *
     * @return the output
     */
    default A apply() {
        return apply(UNIT);
    }

    @Override
    default <B> Fn0<B> flatMap(Function<? super A, ? extends Monad<B, Fn1<Unit, ?>>> f) {
        return Fn1.super.flatMap(f)::apply;
    }

    @Override
    default <B> Fn0<B> fmap(Function<? super A, ? extends B> f) {
        return Fn1.super.fmap(f)::apply;
    }

    @Override
    default <B> Fn0<B> pure(B b) {
        return Fn1.super.pure(b)::apply;
    }

    @Override
    default <B> Fn0<B> zip(Applicative<Function<? super A, ? extends B>, Fn1<Unit, ?>> appFn) {
        return Fn1.super.zip(appFn)::apply;
    }

    @Override
    default <B> Fn0<B> zip(Fn2<Unit, A, B> appFn) {
        return Fn1.super.zip(appFn)::apply;
    }

    @Override
    default <B> Fn0<B> discardL(Applicative<B, Fn1<Unit, ?>> appB) {
        return Fn1.super.discardL(appB)::apply;
    }

    @Override
    default <B> Fn0<A> discardR(Applicative<B, Fn1<Unit, ?>> appB) {
        return Fn1.super.discardR(appB)::apply;
    }

    @Override
    default <B> Fn0<B> diMapR(Function<? super A, ? extends B> fn) {
        return Fn1.super.diMapR(fn)::apply;
    }

    @Override
    default <Z> Fn1<Z, A> compose(Function<? super Z, ? extends Unit> before) {
        return Fn1.super.compose(before)::apply;
    }

    @Override
    default <B> Fn0<B> andThen(Function<? super A, ? extends B> after) {
        return Fn1.super.andThen(after)::apply;
    }

    @Override
    default A get() {
        return apply(UNIT);
    }

    /**
     * Convenience method for converting a {@link Supplier} to an {@link Fn0}.
     *
     * @param supplier the supplier
     * @param <A>      the output type
     * @return the {@link Fn0}
     */
    static <A> Fn0<A> fn0(Supplier<A> supplier) {
        return __ -> supplier.get();
    }

    /**
     * Static factory method for coercing a lambda to an {@link Fn0}.
     *
     * @param fn  the lambda to coerce
     * @param <A> the output type
     * @return the {@link Fn0}
     */
    static <A> Fn0<A> fn0(Fn0<A> fn) {
        return fn;
    }

    /**
     * Static factory method for adapting a {@link Runnable} to an <code>{@link Fn0}&lt;{@link Unit}&gt;</code>.
     *
     * @param fn the {@link Runnable}
     * @return the {@link Fn0}
     */
    static Fn0<Unit> fn0(Runnable fn) {
        return unit -> {
            fn.run();
            return unit;
        };
    }

    /**
     * Static factory method for adapting a {@link Function} to an {@link Fn0}.
     *
     * @param fn  the {@link Function}
     * @param <A> the output type
     * @return the {@link Fn0}
     */
    static <A> Fn0<A> fn0(Function<Unit, A> fn) {
        return fn0(() -> fn.apply(UNIT));
    }
}
