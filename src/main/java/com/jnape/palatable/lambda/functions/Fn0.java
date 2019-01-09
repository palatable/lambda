package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.io.IO.io;

/**
 * A function taking "no arguments", implemented as an <code>{@link Fn1}&lt;{@link Unit}, A&gt;</code>.
 *
 * @param <A> the result type
 * @see Fn1
 * @see Supplier
 * @see Callable
 */
@FunctionalInterface
public interface Fn0<A> extends Fn1<Unit, A>, Supplier<A>, Callable<A> {

    A apply();

    /**
     * Invoke this function with {@link Unit}.
     *
     * @param unit the only allowed input
     * @return the result value
     */
    @Override
    default A apply(Unit unit) {
        return apply();
    }

    @Override
    default <B> Fn0<B> flatMap(Function<? super A, ? extends Monad<B, Fn1<Unit, ?>>> f) {
        return Fn1.super.flatMap(f).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> fmap(Function<? super A, ? extends B> f) {
        return Fn1.super.<B>fmap(f).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> pure(B b) {
        return Fn1.super.pure(b).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> zip(Applicative<Function<? super A, ? extends B>, Fn1<Unit, ?>> appFn) {
        return Fn1.super.zip(appFn).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> zip(Fn2<Unit, A, B> appFn) {
        return Fn1.super.zip(appFn).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> discardL(Applicative<B, Fn1<Unit, ?>> appB) {
        return Fn1.super.discardL(appB).thunk(UNIT);
    }

    @Override
    default <B> Fn0<A> discardR(Applicative<B, Fn1<Unit, ?>> appB) {
        return Fn1.super.discardR(appB).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> diMapR(Function<? super A, ? extends B> fn) {
        return Fn1.super.<B>diMapR(fn).thunk(UNIT);
    }

    @Override
    default <Z> Fn1<Z, A> compose(Function<? super Z, ? extends Unit> before) {
        return Fn1.super.compose(before)::apply;
    }

    @Override
    default <B> Fn0<B> andThen(Function<? super A, ? extends B> after) {
        return Fn1.super.<B>andThen(after).thunk(UNIT);
    }

    @Override
    default A get() {
        return apply();
    }

    @Override
    default A call() {
        return apply();
    }

    /**
     * Convenience method for converting a {@link Supplier} to an {@link Fn0}.
     *
     * @param supplier the supplier
     * @param <A>      the output type
     * @return the {@link Fn0}
     */
    static <A> Fn0<A> fn0(Supplier<A> supplier) {
        return supplier::get;
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
     * @param runnable the {@link Runnable}
     * @return the {@link Fn0}
     */
    static Fn0<Unit> fn0(Runnable runnable) {
        return io(runnable)::unsafePerformIO;
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
