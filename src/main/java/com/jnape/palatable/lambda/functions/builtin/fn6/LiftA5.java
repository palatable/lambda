package com.jnape.palatable.lambda.functions.builtin.fn6;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functor.Applicative;

/**
 * Lift into and apply an {@link Fn5} to five {@link Applicative} values, returning the result inside the same
 * {@link Applicative} context.
 *
 * @param <A>    the function's first argument type
 * @param <B>    the function's second argument type
 * @param <C>    the function's third argument type
 * @param <D>    the function's fourth argument type
 * @param <E>    the function's fifth argument type
 * @param <F>    the function's return type
 * @param <App>  the applicative unification type
 * @param <AppA> the inferred first applicative argument type
 * @param <AppB> the inferred second applicative argument type
 * @param <AppC> the inferred third applicative argument type
 * @param <AppD> the inferred fourth applicative argument type
 * @param <AppE> the inferred fifth applicative argument type
 * @param <AppF> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA5<A, B, C, D, E, F,
        App extends Applicative<?, App>,
        AppA extends Applicative<A, App>,
        AppB extends Applicative<B, App>,
        AppC extends Applicative<C, App>,
        AppD extends Applicative<D, App>,
        AppE extends Applicative<E, App>,
        AppF extends Applicative<F, App>> implements Fn6<Fn5<A, B, C, D, E, F>, AppA, AppB, AppC, AppD, AppE, AppF> {

    private static final LiftA5<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> INSTANCE = new LiftA5<>();

    private LiftA5() {
    }

    @Override
    public AppF apply(Fn5<A, B, C, D, E, F> fn, AppA appA, AppB appB, AppC appC, AppD appD, AppE appE) {
        return appE.zip(appD.zip(appC.zip(appB.zip(appA.fmap(fn))))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F,
            App extends Applicative<?, App>,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>> LiftA5<A, B, C, D, E, F, App, AppA, AppB, AppC, AppD, AppE, AppF> liftA5() {
        return (LiftA5<A, B, C, D, E, F, App, AppA, AppB, AppC, AppD, AppE, AppF>) INSTANCE;
    }

    public static <A, B, C, D, E, F,
            App extends Applicative<?, App>,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>> Fn5<AppA, AppB, AppC, AppD, AppE, AppF> liftA5(Fn5<A, B, C, D, E, F> fn) {
        return LiftA5.<A, B, C, D, E, F, App, AppA, AppB, AppC, AppD, AppE, AppF>liftA5().apply(fn);
    }

    public static <A, B, C, D, E, F,
            App extends Applicative<?, App>,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>> Fn4<AppB, AppC, AppD, AppE, AppF> liftA5(Fn5<A, B, C, D, E, F> fn,
                                                                                       AppA appA) {
        return LiftA5.<A, B, C, D, E, F, App, AppA, AppB, AppC, AppD, AppE, AppF>liftA5(fn).apply(appA);
    }

    public static <A, B, C, D, E, F,
            App extends Applicative<?, App>,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>> Fn3<AppC, AppD, AppE, AppF> liftA5(Fn5<A, B, C, D, E, F> fn, AppA appA,
                                                                                 AppB appB) {
        return LiftA5.<A, B, C, D, E, F, App, AppA, AppB, AppC, AppD, AppE, AppF>liftA5(fn, appA).apply(appB);
    }

    public static <A, B, C, D, E, F,
            App extends Applicative<?, App>,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>> Fn2<AppD, AppE, AppF> liftA5(Fn5<A, B, C, D, E, F> fn, AppA appA,
                                                                           AppB appB,
                                                                           AppC appC) {
        return LiftA5.<A, B, C, D, E, F, App, AppA, AppB, AppC, AppD, AppE, AppF>liftA5(fn, appA, appB).apply(appC);
    }

    public static <A, B, C, D, E, F,
            App extends Applicative<?, App>,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>> Fn1<AppE, AppF> liftA5(Fn5<A, B, C, D, E, F> fn, AppA appA, AppB appB,
                                                                     AppC appC, AppD appD) {
        return LiftA5.<A, B, C, D, E, F, App, AppA, AppB, AppC, AppD, AppE, AppF>liftA5(fn, appA, appB, appC).apply(appD);
    }

    public static <A, B, C, D, E, F,
            App extends Applicative<?, App>,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>> AppF liftA5(Fn5<A, B, C, D, E, F> fn, AppA appA, AppB appB,
                                                          AppC appC, AppD appD, AppE appE) {
        return LiftA5.<A, B, C, D, E, F, App, AppA, AppB, AppC, AppD, AppE, AppF>liftA5(fn, appA, appB, appC, appD).apply(appE);
    }
}
