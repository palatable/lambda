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
 * @param <AppH> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA7<A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
        implements Fn8<Fn7<A, B, C, D, E, F, G, H>, Applicative<A, App>, Applicative<B, App>, Applicative<C, App>,
        Applicative<D, App>, Applicative<E, App>, Applicative<F, App>, Applicative<G, App>, AppH> {

    private static final LiftA7<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> INSTANCE = new LiftA7<>();

    private LiftA7() {
    }

    @Override
    public AppH checkedApply(Fn7<A, B, C, D, E, F, G, H> fn,
                             Applicative<A, App> appA,
                             Applicative<B, App> appB,
                             Applicative<C, App> appC,
                             Applicative<D, App> appD,
                             Applicative<E, App> appE,
                             Applicative<F, App> appF,
                             Applicative<G, App> appG) {
        return appA.<H>zip(appB.zip(appC.zip(appD.zip(appE.zip(appF.zip(appG.fmap(
                g -> f -> e -> d -> c -> b -> a -> fn.apply(a, b, c, d, e, f, g)))))))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    LiftA7<A, B, C, D, E, F, G, H, App, AppH> liftA7() {
        return (LiftA7<A, B, C, D, E, F, G, H, App, AppH>) INSTANCE;
    }

    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    Fn7<Applicative<A, App>, Applicative<B, App>, Applicative<C, App>, Applicative<D, App>, Applicative<E, App>,
            Applicative<F, App>, Applicative<G, App>, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppH>liftA7().apply(fn);
    }

    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    Fn6<Applicative<B, App>, Applicative<C, App>, Applicative<D, App>, Applicative<E, App>, Applicative<F, App>,
            Applicative<G, App>, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn,
                                              Applicative<A, App> appA) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppH>liftA7(fn).apply(appA);
    }

    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    Fn5<Applicative<C, App>, Applicative<D, App>, Applicative<E, App>, Applicative<F, App>, Applicative<G, App>, AppH>
    liftA7(Fn7<A, B, C, D, E, F, G, H> fn,
           Applicative<A, App> appA,
           Applicative<B, App> appB) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppH>liftA7(fn, appA).apply(appB);
    }

    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    Fn4<Applicative<D, App>, Applicative<E, App>, Applicative<F, App>, Applicative<G, App>, AppH> liftA7(
            Fn7<A, B, C, D, E, F, G, H> fn,
            Applicative<A, App> appA,
            Applicative<B, App> appB,
            Applicative<C, App> appC) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppH>liftA7(fn, appA, appB).apply(appC);
    }

    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    Fn3<Applicative<E, App>, Applicative<F, App>, Applicative<G, App>, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn,
                                                                                    Applicative<A, App> appA,
                                                                                    Applicative<B, App> appB,
                                                                                    Applicative<C, App> appC,
                                                                                    Applicative<D, App> appD) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppH>liftA7(fn, appA, appB, appC).apply(appD);
    }

    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    Fn2<Applicative<F, App>, Applicative<G, App>, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn,
                                                               Applicative<A, App> appA,
                                                               Applicative<B, App> appB,
                                                               Applicative<C, App> appC,
                                                               Applicative<D, App> appD,
                                                               Applicative<E, App> appE) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppH>liftA7(fn, appA, appB, appC, appD).apply(appE);
    }

    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    Fn1<Applicative<G, App>, AppH> liftA7(Fn7<A, B, C, D, E, F, G, H> fn,
                                          Applicative<A, App> appA,
                                          Applicative<B, App> appB,
                                          Applicative<C, App> appC,
                                          Applicative<D, App> appD,
                                          Applicative<E, App> appE,
                                          Applicative<F, App> appF) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppH>liftA7(fn, appA, appB, appC, appD, appE).apply(appF);
    }

    public static <A, B, C, D, E, F, G, H, App extends Applicative<?, App>, AppH extends Applicative<H, App>>
    AppH liftA7(Fn7<A, B, C, D, E, F, G, H> fn,
                Applicative<A, App> appA,
                Applicative<B, App> appB,
                Applicative<C, App> appC,
                Applicative<D, App> appD,
                Applicative<E, App> appE,
                Applicative<F, App> appF,
                Applicative<G, App> appG) {
        return LiftA7.<A, B, C, D, E, F, G, H, App, AppH>liftA7(fn, appA, appB, appC, appD, appE, appF).apply(appG);
    }
}
