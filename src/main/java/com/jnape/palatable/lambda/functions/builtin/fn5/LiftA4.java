package com.jnape.palatable.lambda.functions.builtin.fn5;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functor.Applicative;

/**
 * Lift into and apply an {@link Fn4} to four {@link Applicative} values, returning the result inside the same
 * {@link Applicative} context.
 *
 * @param <A>    the function's first argument type
 * @param <B>    the function's second argument type
 * @param <C>    the function's third argument type
 * @param <D>    the function's fourth argument type
 * @param <E>    the function's return type
 * @param <App>  the applicative unification type
 * @param <AppA> the inferred first applicative argument type
 * @param <AppB> the inferred second applicative argument type
 * @param <AppC> the inferred third applicative argument type
 * @param <AppD> the inferred fourth applicative argument type
 * @param <AppE> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA4<A, B, C, D, E,
        App extends Applicative,
        AppA extends Applicative<A, App>,
        AppB extends Applicative<B, App>,
        AppC extends Applicative<C, App>,
        AppD extends Applicative<D, App>,
        AppE extends Applicative<E, App>> implements Fn5<Fn4<A, B, C, D, E>, AppA, AppB, AppC, AppD, AppE> {

    private static final LiftA4 INSTANCE = new LiftA4();

    private LiftA4() {
    }

    @Override
    public AppE apply(Fn4<A, B, C, D, E> fn, AppA appA, AppB appB, AppC appC, AppD appD) {
        return appD.zip(appC.zip(appB.zip(appA.fmap(fn)))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>> LiftA4<A, B, C, D, E, App, AppA, AppB, AppC, AppD, AppE> liftA4() {
        return INSTANCE;
    }

    public static <A, B, C, D, E,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>> Fn4<AppA, AppB, AppC, AppD, AppE> liftA4(Fn4<A, B, C, D, E> fn) {
        return LiftA4.<A, B, C, D, E, App, AppA, AppB, AppC, AppD, AppE>liftA4().apply(fn);
    }

    public static <A, B, C, D, E,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>> Fn3<AppB, AppC, AppD, AppE> liftA4(Fn4<A, B, C, D, E> fn, AppA appA) {
        return LiftA4.<A, B, C, D, E, App, AppA, AppB, AppC, AppD, AppE>liftA4(fn).apply(appA);
    }

    public static <A, B, C, D, E,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>> Fn2<AppC, AppD, AppE> liftA4(Fn4<A, B, C, D, E> fn, AppA appA,
                                                                           AppB appB) {
        return LiftA4.<A, B, C, D, E, App, AppA, AppB, AppC, AppD, AppE>liftA4(fn, appA).apply(appB);
    }

    public static <A, B, C, D, E,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>> Fn1<AppD, AppE> liftA4(Fn4<A, B, C, D, E> fn, AppA appA, AppB appB,
                                                                     AppC appC) {
        return LiftA4.<A, B, C, D, E, App, AppA, AppB, AppC, AppD, AppE>liftA4(fn, appA, appB).apply(appC);
    }

    public static <A, B, C, D, E,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>,
            AppE extends Applicative<E, App>> AppE liftA4(Fn4<A, B, C, D, E> fn, AppA appA, AppB appB,
                                                          AppC appC, AppD appD) {
        return LiftA4.<A, B, C, D, E, App, AppA, AppB, AppC, AppD, AppE>liftA4(fn, appA, appB, appC).apply(appD);
    }
}
