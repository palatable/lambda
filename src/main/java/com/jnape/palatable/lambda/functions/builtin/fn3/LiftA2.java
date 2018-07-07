package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.BiFunction;

/**
 * Lift into and apply a {@link BiFunction} to two {@link Applicative} values, returning the result inside the same
 * {@link Applicative} context. Functionally equivalent to <code>appB.zip(appA.fmap(fn))</code>.
 *
 * @param <A>    the function's first argument type
 * @param <B>    the function's second argument typ
 * @param <C>    the function's return type
 * @param <App>  the applicative unification type
 * @param <AppA> the inferred first applicative argument type
 * @param <AppB> the inferred second applicative argument type
 * @param <AppC> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA2<A, B, C, App extends Applicative,
        AppA extends Applicative<A, App>,
        AppB extends Applicative<B, App>,
        AppC extends Applicative<C, App>> implements Fn3<BiFunction<? super A, ? super B, ? extends C>, AppA, AppB, AppC> {

    private static final LiftA2 INSTANCE = new LiftA2();

    private LiftA2() {
    }

    @Override
    public AppC apply(BiFunction<? super A, ? super B, ? extends C> fn, AppA appA, AppB appB) {
        return appB.zip(appA.fmap(Fn2.<A, B, C>fn2(fn))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, App extends Applicative, AppA extends Applicative<A, App>, AppB extends Applicative<B, App>, AppC extends Applicative<C, App>> LiftA2<A, B, C, App, AppA, AppB, AppC> liftA2() {
        return INSTANCE;
    }

    public static <A, B, C, App extends Applicative, AppA extends Applicative<A, App>, AppB extends Applicative<B, App>, AppC extends Applicative<C, App>> Fn2<AppA, AppB, AppC> liftA2(
            BiFunction<? super A, ? super B, ? extends C> fn) {
        return LiftA2.<A, B, C, App, AppA, AppB, AppC>liftA2().apply(fn);
    }

    public static <A, B, C, App extends Applicative, AppA extends Applicative<A, App>, AppB extends Applicative<B, App>, AppC extends Applicative<C, App>> Fn1<AppB, AppC> liftA2(
            BiFunction<? super A, ? super B, ? extends C> fn,
            AppA appA) {
        return LiftA2.<A, B, C, App, AppA, AppB, AppC>liftA2(fn).apply(appA);
    }

    public static <A, B, C, App extends Applicative, AppA extends Applicative<A, App>, AppB extends Applicative<B, App>, AppC extends Applicative<C, App>> AppC liftA2(
            BiFunction<? super A, ? super B, ? extends C> fn,
            AppA appA,
            AppB appB) {
        return LiftA2.<A, B, C, App, AppA, AppB, AppC>liftA2(fn, appA).apply(appB);
    }
}
