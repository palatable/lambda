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
 * @param <App>  the applicative witness
 * @param <AppF> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA5<A, B, C, D, E, F, App extends Applicative<?, App>, AppF extends Applicative<F, App>>
        implements Fn6<Fn5<A, B, C, D, E, F>, Applicative<A, App>, Applicative<B, App>, Applicative<C, App>,
        Applicative<D, App>, Applicative<E, App>, AppF> {

    private static final LiftA5<?, ?, ?, ?, ?, ?, ?, ?> INSTANCE = new LiftA5<>();

    private LiftA5() {
    }

    @Override
    public AppF checkedApply(Fn5<A, B, C, D, E, F> fn, Applicative<A, App> appA, Applicative<B, App> appB,
                             Applicative<C, App> appC, Applicative<D, App> appD, Applicative<E, App> appE) {
        return appA.<F>zip(appB.zip(appC.zip(appD.zip(appE.fmap(e -> d -> c -> b -> a -> fn.apply(a, b, c, d, e))))))
                .coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F, App extends Applicative<?, App>, AppF extends Applicative<F, App>>
    LiftA5<A, B, C, D, E, F, App, AppF> liftA5() {
        return (LiftA5<A, B, C, D, E, F, App, AppF>) INSTANCE;
    }

    public static <A, B, C, D, E, F, App extends Applicative<?, App>, AppF extends Applicative<F, App>>
    Fn5<Applicative<A, App>, Applicative<B, App>, Applicative<C, App>, Applicative<D, App>, Applicative<E, App>, AppF>
    liftA5(Fn5<A, B, C, D, E, F> fn) {
        return LiftA5.<A, B, C, D, E, F, App, AppF>liftA5().apply(fn);
    }

    public static <A, B, C, D, E, F, App extends Applicative<?, App>, AppF extends Applicative<F, App>>
    Fn4<Applicative<B, App>, Applicative<C, App>, Applicative<D, App>, Applicative<E, App>, AppF>
    liftA5(Fn5<A, B, C, D, E, F> fn,
           Applicative<A, App> appA) {
        return LiftA5.<A, B, C, D, E, F, App, AppF>liftA5(fn).apply(appA);
    }

    public static <A, B, C, D, E, F, App extends Applicative<?, App>, AppF extends Applicative<F, App>>
    Fn3<Applicative<C, App>, Applicative<D, App>, Applicative<E, App>, AppF> liftA5(Fn5<A, B, C, D, E, F> fn,
                                                                                    Applicative<A, App> appA,
                                                                                    Applicative<B, App> appB) {
        return LiftA5.<A, B, C, D, E, F, App, AppF>liftA5(fn, appA).apply(appB);
    }

    public static <A, B, C, D, E, F, App extends Applicative<?, App>, AppF extends Applicative<F, App>>
    Fn2<Applicative<D, App>, Applicative<E, App>, AppF> liftA5(Fn5<A, B, C, D, E, F> fn,
                                                               Applicative<A, App> appA,
                                                               Applicative<B, App> appB,
                                                               Applicative<C, App> appC) {
        return LiftA5.<A, B, C, D, E, F, App, AppF>liftA5(fn, appA, appB).apply(appC);
    }

    public static <A, B, C, D, E, F, App extends Applicative<?, App>, AppF extends Applicative<F, App>>
    Fn1<Applicative<E, App>, AppF> liftA5(Fn5<A, B, C, D, E, F> fn,
                                          Applicative<A, App> appA,
                                          Applicative<B, App> appB,
                                          Applicative<C, App> appC,
                                          Applicative<D, App> appD) {
        return LiftA5.<A, B, C, D, E, F, App, AppF>liftA5(fn, appA, appB, appC).apply(appD);
    }

    public static <A, B, C, D, E, F, App extends Applicative<?, App>, AppF extends Applicative<F, App>>
    AppF liftA5(Fn5<A, B, C, D, E, F> fn,
                Applicative<A, App> appA,
                Applicative<B, App> appB,
                Applicative<C, App> appC,
                Applicative<D, App> appD,
                Applicative<E, App> appE) {
        return LiftA5.<A, B, C, D, E, F, App, AppF>liftA5(fn, appA, appB, appC, appD).apply(appE);
    }
}
