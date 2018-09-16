package com.jnape.palatable.lambda.functions.builtin.fn8;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functions.Fn7;
import com.jnape.palatable.lambda.functions.Fn8;
import com.jnape.palatable.lambda.functor.Applicative;

/**
 * Lift into and apply an {@link Fn7} to seven {@link Applicative} values, returning the result inside the same
 * {@link Applicative} context.
 *
 * @param <A>    the function's first argument type
 * @param <B>    the function's second argument type
 * @param <C>    the function's third argument type
 * @param <D>    the function's fourth argument type
 * @param <E>    the function's fifth argument type
 * @param <F>    the function's sixth argument type
 * @param <G>    the function's seventh argument type
 * @param <H>    the function's return type
 * @param <App>  the applicative unification type
 * @param <AppA> the inferred first applicative argument type
 * @param <AppB> the inferred second applicative argument type
 * @param <AppC> the inferred third applicative argument type
 * @param <AppD> the inferred fourth applicative argument type
 * @param <AppE> the inferred fifth applicative argument type
 * @param <AppF> the inferred sixth applicative argument type
 * @param <AppG> the inferred seventh applicative argument type
 * @param <AppH> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA7<A, B, C, D, E, F, G, H,
        App extends Applicative,
        AppA extends Applicative<A, App>,
        AppB extends Applicative<B, App>,
        AppC extends Applicative<C, App>,
        AppD extends Applicative<D, App>,
        AppE extends Applicative<E, App>,
        AppF extends Applicative<F, App>,
        AppG extends Applicative<G, App>,
        AppH extends Applicative<H, App>> implements Fn8<Fn7<A, B, C, D, E, F, G, H>, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH> {

    private static final LiftA7 INSTANCE = new LiftA7();

    private LiftA7() {
    }

    @Override
    public AppH apply(Fn7<A, B, C, D, E, F, G, H> fn, AppA appA, AppB appB, AppC appC, AppD appD, AppE appE,
                      AppF appF, AppG appG) {
        return appG.zip(appF.zip(appE.zip(appD.zip(appC.zip(appB.zip(appA.fmap(fn))))))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> LiftA7<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH> liftA7() {
        return INSTANCE;
    }

    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> Fn7<AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH> liftA7(
            Fn7<A, B, C, D, E, F, G, H> fn) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH>liftA7().apply(fn);
    }

    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> Fn6<AppB, AppC, AppD, AppE, AppF, AppG, AppH> liftA7(
            Fn7<A, B, C, D, E, F, G, H> fn, AppA appA) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH>liftA7(fn).apply(appA);
    }

    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> Fn5<AppC, AppD, AppE, AppF, AppG, AppH> liftA7(
            Fn7<A, B, C, D, E, F, G, H> fn, AppA appA, AppB appB) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH>liftA7(fn, appA).apply(appB);
    }

    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> Fn4<AppD, AppE, AppF, AppG, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn,
                                                                                       AppA appA, AppB appB,
                                                                                       AppC appC) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH>liftA7(fn, appA, appB).apply(appC);
    }

    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> Fn3<AppE, AppF, AppG, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn,
                                                                                 AppA appA, AppB appB, AppC appC,
                                                                                 AppD appD) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH>liftA7(fn, appA, appB, appC).apply(appD);
    }

    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> Fn2<AppF, AppG, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn, AppA appA,
                                                                           AppB appB, AppC appC, AppD appD, AppE appE) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH>liftA7(fn, appA, appB, appC, appD).apply(appE);
    }

    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> Fn1<AppG, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn, AppA appA,
                                                                     AppB appB, AppC appC, AppD appD, AppE appE,
                                                                     AppF appF) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH>liftA7(fn, appA, appB, appC, appD, appE).apply(appF);
    }

    public static <A, B, C, D, E, F, G, H,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>,
            AppF extends Applicative<F, App>,
            AppG extends Applicative<G, App>,
            AppH extends Applicative<H, App>> AppH liftA7(Fn7<A, B, C, D, E, F, G, H> fn, AppA appA, AppB appB,
                                                          AppC appC, AppD appD, AppE appE, AppF appF, AppG appG) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppA, AppB, AppC, AppD, AppE, AppF, AppG, AppH>liftA7(fn, appA, appB, appC, appD, appE, appF).apply(appG);
    }
}
