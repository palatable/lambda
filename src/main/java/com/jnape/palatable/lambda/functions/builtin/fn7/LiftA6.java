package com.jnape.palatable.lambda.functions.builtin.fn7;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functions.Fn7;
import com.jnape.palatable.lambda.functor.Applicative;

/**
 * Lift into and apply an {@link Fn6} to six {@link Applicative} values, returning the result inside the same
 * {@link Applicative} context.
 *
 * @param <A>    the function's first argument type
 * @param <B>    the function's second argument type
 * @param <C>    the function's third argument type
 * @param <D>    the function's fourth argument type
 * @param <E>    the function's fifth argument type
 * @param <F>    the function's sixth argument type
 * @param <G>    the function's return type
 * @param <App>  the applicative witness
 * @param <AppG> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA6<A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
        implements Fn7<Fn6<A, B, C, D, E, F, G>,
        Applicative<A, App>,
        Applicative<B, App>,
        Applicative<C, App>,
        Applicative<D, App>,
        Applicative<E, App>,
        Applicative<F, App>,
        AppG> {

    private static final LiftA6<?, ?, ?, ?, ?, ?, ?, ?, ?> INSTANCE = new LiftA6<>();

    private LiftA6() {
    }

    @Override
    public AppG checkedApply(Fn6<A, B, C, D, E, F, G> fn,
                             Applicative<A, App> appA,
                             Applicative<B, App> appB,
                             Applicative<C, App> appC,
                             Applicative<D, App> appD,
                             Applicative<E, App> appE,
                             Applicative<F, App> appF) {
        return appA.<G>zip(appB.zip(appC.zip(appD.zip(appE.zip(appF.fmap(
                f -> e -> d -> c -> b -> a -> fn.apply(a, b, c, d, e, f))))))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
    LiftA6<A, B, C, D, E, F, G, App, AppG> liftA6() {
        return (LiftA6<A, B, C, D, E, F, G, App, AppG>) INSTANCE;
    }

    public static <A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
    Fn6<Applicative<A, App>, Applicative<B, App>, Applicative<C, App>, Applicative<D, App>, Applicative<E, App>,
            Applicative<F, App>, AppG> liftA6(Fn6<A, B, C, D, E, F, G> fn) {
        return LiftA6.<A, B, C, D, E, F, G, App, AppG>liftA6().apply(fn);
    }

    public static <A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
    Fn5<Applicative<B, App>, Applicative<C, App>, Applicative<D, App>, Applicative<E, App>, Applicative<F, App>, AppG>
    liftA6(Fn6<A, B, C, D, E, F, G> fn, Applicative<A, App> appA) {
        return LiftA6.<A, B, C, D, E, F, G, App, AppG>liftA6(fn).apply(appA);
    }

    public static <A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
    Fn4<Applicative<C, App>, Applicative<D, App>, Applicative<E, App>, Applicative<F, App>, AppG>
    liftA6(Fn6<A, B, C, D, E, F, G> fn,
           Applicative<A, App> appA,
           Applicative<B, App> appB) {
        return LiftA6.<A, B, C, D, E, F, G, App, AppG>liftA6(fn, appA).apply(appB);
    }

    public static <A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
    Fn3<Applicative<D, App>, Applicative<E, App>, Applicative<F, App>, AppG> liftA6(Fn6<A, B, C, D, E, F, G> fn,
                                                                                    Applicative<A, App> appA,
                                                                                    Applicative<B, App> appB,
                                                                                    Applicative<C, App> appC) {
        return LiftA6.<A, B, C, D, E, F, G, App, AppG>liftA6(fn, appA, appB).apply(appC);
    }

    public static <A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
    Fn2<Applicative<E, App>, Applicative<F, App>, AppG> liftA6(Fn6<A, B, C, D, E, F, G> fn,
                                                               Applicative<A, App> appA,
                                                               Applicative<B, App> appB,
                                                               Applicative<C, App> appC,
                                                               Applicative<D, App> appD) {
        return LiftA6.<A, B, C, D, E, F, G, App, AppG>liftA6(fn, appA, appB, appC).apply(appD);
    }

    public static <A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
    Fn1<Applicative<F, App>, AppG> liftA6(Fn6<A, B, C, D, E, F, G> fn,
                                          Applicative<A, App> appA,
                                          Applicative<B, App> appB,
                                          Applicative<C, App> appC,
                                          Applicative<D, App> appD,
                                          Applicative<E, App> appE) {
        return LiftA6.<A, B, C, D, E, F, G, App, AppG>liftA6(fn, appA, appB, appC, appD).apply(appE);
    }

    public static <A, B, C, D, E, F, G, App extends Applicative<?, App>, AppG extends Applicative<G, App>>
    AppG liftA6(Fn6<A, B, C, D, E, F, G> fn,
                Applicative<A, App> appA,
                Applicative<B, App> appB,
                Applicative<C, App> appC,
                Applicative<D, App> appD,
                Applicative<E, App> appE,
                Applicative<F, App> appF) {
        return LiftA6.<A, B, C, D, E, F, G, App, AppG>liftA6(fn, appA, appB, appC, appD, appE).apply(appF);
    }
}
