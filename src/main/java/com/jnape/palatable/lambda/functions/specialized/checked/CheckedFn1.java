package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.specialized.checked.Runtime.throwChecked;

/**
 * Specialized {@link Fn1} that can throw any {@link Throwable}.
 *
 * @param <T> The {@link Throwable} type
 * @param <A> The input type
 * @param <B> The output type
 * @see CheckedSupplier
 * @see CheckedRunnable
 * @see Fn1
 */
@FunctionalInterface
public interface CheckedFn1<T extends Throwable, A, B> extends Fn1<A, B> {

    /**
     * {@inheritDoc}
     */
    @Override
    default B apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, A, C> fmap(Function<? super B, ? extends C> f) {
        return Fn1.super.<C>fmap(f)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, A, C> flatMap(Function<? super B, ? extends Monad<C, Fn1<A, ?>>> f) {
        return Fn1.super.flatMap(f).<Fn1<A, C>>coerce()::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, A, C> discardL(Applicative<C, Fn1<A, ?>> appB) {
        return Fn1.super.discardL(appB)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, A, B> discardR(Applicative<C, Fn1<A, ?>> appB) {
        return Fn1.super.discardR(appB)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, A, C> zip(Applicative<Function<? super B, ? extends C>, Fn1<A, ?>> appFn) {
        return Fn1.super.zip(appFn)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, A, C> zip(Fn2<A, B, C> appFn) {
        return Fn1.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> CheckedFn1<T, Z, B> diMapL(Function<? super Z, ? extends A> fn) {
        return Fn1.super.<Z>diMapL(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, A, C> diMapR(Function<? super B, ? extends C> fn) {
        return Fn1.super.<C>diMapR(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z, C> CheckedFn1<T, Z, C> diMap(Function<? super Z, ? extends A> lFn,
                                             Function<? super B, ? extends C> rFn) {
        return Fn1.super.diMap(lFn, rFn)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, Tuple2<C, A>, Tuple2<C, B>> cartesian() {
        return Fn1.super.<C>cartesian()::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default CheckedFn1<T, A, Tuple2<A, B>> carry() {
        return Fn1.super.carry()::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> CheckedFn1<T, Z, B> contraMap(Function<? super Z, ? extends A> fn) {
        return Fn1.super.<Z>contraMap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> CheckedFn1<T, Z, B> compose(Function<? super Z, ? extends A> before) {
        return Fn1.super.<Z>compose(before)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> CheckedFn1<T, A, C> andThen(Function<? super B, ? extends C> after) {
        return Fn1.super.<C>andThen(after)::apply;
    }

    /**
     * A version of {@link Fn1#apply} that can throw checked exceptions.
     *
     * @param a the function argument
     * @return the application of the argument to the function
     * @throws T any exception that can be thrown by this method
     */
    B checkedApply(A a) throws T;

    /**
     * Convenience static factory method for constructing a {@link CheckedFn1} without an explicit cast or type
     * attribution at the call site.
     *
     * @param checkedFn1 the checked fn1
     * @param <T>        the inferred Throwable type
     * @param <A>        the input type
     * @param <B>        the output type
     * @return the checked fn1
     */
    static <T extends Throwable, A, B> CheckedFn1<T, A, B> checked(CheckedFn1<T, A, B> checkedFn1) {
        return checkedFn1;
    }
}
