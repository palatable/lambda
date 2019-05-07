package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

/**
 * A function taking "no arguments", implemented as an <code>{@link Fn1}&lt;{@link Unit}, A&gt;</code>.
 *
 * @param <A> the result type
 * @see Fn1
 * @see Callable
 */
@FunctionalInterface
public interface Fn0<A> extends Fn1<Unit, A> {

    A checkedApply() throws Throwable;

    /**
     * Convenience method for applying this {@link Fn0} without providing an explicit {@link Unit}.
     *
     * @return the result
     */
    default A apply() {
        return apply(UNIT);
    }

    /**
     * Convert this {@link Fn0} to a java {@link Supplier}
     *
     * @return the {@link Supplier}
     */
    default Supplier<A> toSupplier() {
        return this::apply;
    }

    /**
     * Convert this {@link Fn0} to a java {@link Callable}
     *
     * @return the {@link Callable}
     */
    default Callable<A> toCallable() {
        return this::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default A checkedApply(Unit unit) throws Throwable {
        return checkedApply();
    }

    @Override
    default <B> Fn0<B> flatMap(Fn1<? super A, ? extends Monad<B, Fn1<Unit, ?>>> f) {
        return Fn1.super.flatMap(f).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> fmap(Fn1<? super A, ? extends B> f) {
        return Fn1.super.<B>fmap(f).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> pure(B b) {
        return Fn1.super.pure(b).thunk(UNIT);
    }

    @Override
    default <B> Fn0<B> zip(Applicative<Fn1<? super A, ? extends B>, Fn1<Unit, ?>> appFn) {
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
    default <B> Fn0<B> diMapR(Fn1<? super A, ? extends B> fn) {
        return Fn1.super.<B>diMapR(fn).thunk(UNIT);
    }

    /**
     * Convenience method for converting a {@link Supplier} to an {@link Fn0}.
     *
     * @param supplier the supplier
     * @param <A>      the output type
     * @return the {@link Fn0}
     */
    static <A> Fn0<A> fromSupplier(Supplier<A> supplier) {
        return supplier::get;
    }

    /**
     * Convenience method for converting a {@link Callable} to an {@link Fn0}.
     *
     * @param callable the callable
     * @param <A>      the output type
     * @return the {@link Fn0}
     */
    static <A> Fn0<A> fromCallable(Callable<A> callable) {
        return callable::call;
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
     * Static factory method for adapting an <code>{@link Fn1}&lt;Unit, A&gt;</code> to an
     * <code>{@link Fn0}&lt;A&gt;</code>.
     *
     * @param fn  the {@link Fn1}
     * @param <A> the output type
     * @return the {@link Fn0}
     */
    static <A> Fn0<A> fn0(Fn1<Unit, A> fn) {
        return fn0(() -> fn.apply(UNIT));
    }
}
