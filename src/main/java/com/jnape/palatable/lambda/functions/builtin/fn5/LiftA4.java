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
 * @param <App>  the applicative witness
 * @param <AppE> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA4<A, B, C, D, E, App extends Applicative<?, App>, AppE extends Applicative<E, App>> implements
        Fn5<Fn4<A, B, C, D, E>, Applicative<A, App>, Applicative<B, App>, Applicative<C, App>, Applicative<D, App>,
                AppE> {

    private static final LiftA4<?, ?, ?, ?, ?, ?, ?> INSTANCE = new LiftA4<>();

    private LiftA4() {
    }

    @Override
    public AppE checkedApply(Fn4<A, B, C, D, E> fn, Applicative<A, App> appA, Applicative<B, App> appB,
                             Applicative<C, App> appC, Applicative<D, App> appD) {
        return appA.<E>zip(appB.zip(appC.zip(appD.fmap(d -> c -> b -> a -> fn.apply(a, b, c, d))))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, App extends Applicative<?, App>, AppE extends Applicative<E, App>>
    LiftA4<A, B, C, D, E, App, AppE> liftA4() {
        return (LiftA4<A, B, C, D, E, App, AppE>) INSTANCE;
    }

    public static <A, B, C, D, E, App extends Applicative<?, App>, AppE extends Applicative<E, App>>
    Fn4<Applicative<A, App>, Applicative<B, App>, Applicative<C, App>, Applicative<D, App>, AppE> liftA4(
            Fn4<A, B, C, D, E> fn) {
        return LiftA4.<A, B, C, D, E, App, AppE>liftA4().apply(fn);
    }

    public static <A, B, C, D, E, App extends Applicative<?, App>, AppE extends Applicative<E, App>>
    Fn3<Applicative<B, App>, Applicative<C, App>, Applicative<D, App>, AppE> liftA4(Fn4<A, B, C, D, E> fn,
                                                                                    Applicative<A, App> appA) {
        return LiftA4.<A, B, C, D, E, App, AppE>liftA4(fn).apply(appA);
    }

    public static <A, B, C, D, E, App extends Applicative<?, App>, AppE extends Applicative<E, App>>
    Fn2<Applicative<C, App>, Applicative<D, App>, AppE> liftA4(Fn4<A, B, C, D, E> fn,
                                                               Applicative<A, App> appA,
                                                               Applicative<B, App> appB) {
        return LiftA4.<A, B, C, D, E, App, AppE>liftA4(fn, appA).apply(appB);
    }

    public static <A, B, C, D, E, App extends Applicative<?, App>, AppE extends Applicative<E, App>>
    Fn1<Applicative<D, App>, AppE> liftA4(Fn4<A, B, C, D, E> fn,
                                          Applicative<A, App> appA,
                                          Applicative<B, App> appB,
                                          Applicative<C, App> appC) {
        return LiftA4.<A, B, C, D, E, App, AppE>liftA4(fn, appA, appB).apply(appC);
    }

    public static <A, B, C, D, E, App extends Applicative<?, App>, AppE extends Applicative<E, App>>
    AppE liftA4(Fn4<A, B, C, D, E> fn,
                Applicative<A, App> appA,
                Applicative<B, App> appB,
                Applicative<C, App> appC,
                Applicative<D, App> appD) {
        return LiftA4.<A, B, C, D, E, App, AppE>liftA4(fn, appA, appB, appC).apply(appD);
    }
}
