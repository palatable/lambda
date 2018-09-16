package com.jnape.palatable.lambda.functions.builtin.fn4;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functor.Applicative;

/**
 * Lift into and apply an {@link Fn3} to three {@link Applicative} values, returning the result inside the same
 * {@link Applicative} context.
 *
 * @param <A>    the function's first argument type
 * @param <B>    the function's second argument type
 * @param <C>    the function's third argument type
 * @param <D>    the function's return type
 * @param <App>  the applicative unification type
 * @param <AppA> the inferred first applicative argument type
 * @param <AppB> the inferred second applicative argument type
 * @param <AppC> the inferred third applicative argument type
 * @param <AppD> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA3<A, B, C, D,
        App extends Applicative,
        AppA extends Applicative<A, App>,
        AppB extends Applicative<B, App>,
        AppC extends Applicative<C, App>,
        AppD extends Applicative<D, App>> implements Fn4<Fn3<A, B, C, D>, AppA, AppB, AppC, AppD> {

    private static final LiftA3 INSTANCE = new LiftA3();

    private LiftA3() {
    }

    @Override
    public AppD apply(Fn3<A, B, C, D> fn, AppA appA, AppB appB, AppC appC) {
        return appC.zip(appB.zip(appA.fmap(fn))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>> LiftA3<A, B, C, D, App, AppA, AppB, AppC, AppD> liftA3() {
        return INSTANCE;
    }

    public static <A, B, C, D,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>> Fn3<AppA, AppB, AppC, AppD> liftA3(Fn3<A, B, C, D> fn) {
        return LiftA3.<A, B, C, D, App, AppA, AppB, AppC, AppD>liftA3().apply(fn);
    }

    public static <A, B, C, D,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>> Fn2<AppB, AppC, AppD> liftA3(Fn3<A, B, C, D> fn, AppA appA) {
        return LiftA3.<A, B, C, D, App, AppA, AppB, AppC, AppD>liftA3(fn).apply(appA);
    }

    public static <A, B, C, D,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>> Fn1<AppC, AppD> liftA3(Fn3<A, B, C, D> fn, AppA appA, AppB appB) {
        return LiftA3.<A, B, C, D, App, AppA, AppB, AppC, AppD>liftA3(fn, appA).apply(appB);
    }

    public static <A, B, C, D,
            App extends Applicative,
            AppA extends Applicative<A, App>,
            AppB extends Applicative<B, App>,
            AppC extends Applicative<C, App>,
            AppD extends Applicative<D, App>> AppD liftA3(Fn3<A, B, C, D> fn, AppA appA, AppB appB,
                                                          AppC appC) {
        return LiftA3.<A, B, C, D, App, AppA, AppB, AppC, AppD>liftA3(fn, appA, appB).apply(appC);
    }
}
