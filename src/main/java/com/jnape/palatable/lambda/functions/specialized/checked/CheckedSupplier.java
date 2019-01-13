package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Supplier} that can throw any {@link Throwable}.
 *
 * @param <T> The {@link Throwable} type
 * @param <A> The return type
 * @see CheckedFn1
 * @see CheckedRunnable
 */
@FunctionalInterface
public interface CheckedSupplier<T extends Throwable, A> extends Supplier<A>, CheckedFn1<T, Unit, A> {

    /**
     * A version of {@link Supplier#get()} that can throw checked exceptions.
     *
     * @return the supplied result
     * @throws T any exception that can be thrown by this method
     */
    A checkedGet() throws T;

    /**
     * Convert this {@link CheckedSupplier} to a {@link CheckedRunnable}.
     *
     * @return the checked runnable
     */
    default CheckedRunnable<T> toRunnable() {
        return this::get;
    }

    @Override
    default A checkedApply(Unit unit) throws T {
        return checkedGet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default A get() {
        try {
            return checkedGet();
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> CheckedSupplier<T, B> fmap(Function<? super A, ? extends B> f) {
        return CheckedFn1.super.<B>fmap(f).thunk(UNIT)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedSupplier<T, C> flatMap(Function<? super A, ? extends Monad<C, Fn1<Unit, ?>>> f) {
        return CheckedFn1.super.flatMap(f).thunk(UNIT)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedSupplier<T, C> discardL(Applicative<C, Fn1<Unit, ?>> appB) {
        return CheckedFn1.super.discardL(appB).thunk(UNIT)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedSupplier<T, A> discardR(Applicative<C, Fn1<Unit, ?>> appB) {
        return CheckedFn1.super.discardR(appB).thunk(UNIT)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedSupplier<T, C> zip(Applicative<Function<? super A, ? extends C>, Fn1<Unit, ?>> appFn) {
        return CheckedFn1.super.zip(appFn).thunk(UNIT)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedSupplier<T, C> zip(Fn2<Unit, A, C> appFn) {
        return CheckedFn1.super.zip(appFn).thunk(UNIT)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedSupplier<T, C> diMapR(Function<? super A, ? extends C> fn) {
        return CheckedFn1.super.diMapR(fn).thunk(UNIT)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default CheckedSupplier<T, Tuple2<Unit, A>> carry() {
        return CheckedFn1.super.carry().thunk(UNIT)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedSupplier<T, C> andThen(Function<? super A, ? extends C> after) {
        return CheckedFn1.super.andThen(after).thunk(UNIT)::apply;
    }

    /**
     * Convenience static factory method for constructing a {@link CheckedSupplier} without an explicit cast or type
     * attribution at the call site.
     *
     * @param supplier the checked supplier
     * @param <T>      the inferred Throwable type
     * @param <A>      the supplier return type
     * @return the checked supplier
     */
    static <T extends Throwable, A> CheckedSupplier<T, A> checked(CheckedSupplier<T, A> supplier) {
        return supplier::get;
    }
}
