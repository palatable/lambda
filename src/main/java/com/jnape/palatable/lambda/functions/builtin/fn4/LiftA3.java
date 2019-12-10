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
 * @param <App>  the applicative witness
 * @param <AppD> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA3<A, B, C, D, App extends Applicative<?, App>, AppD extends Applicative<D, App>> implements
        Fn4<Fn3<A, B, C, D>, Applicative<A, App>, Applicative<B, App>, Applicative<C, App>, AppD> {

    private static final LiftA3<?, ?, ?, ?, ?, ?> INSTANCE = new LiftA3<>();

    private LiftA3() {
    }

    @Override
    public AppD checkedApply(Fn3<A, B, C, D> fn,
                             Applicative<A, App> appA,
                             Applicative<B, App> appB,
                             Applicative<C, App> appC) {
        return appA.<D>zip(appB.zip(appC.fmap(c -> b -> a -> fn.apply(a, b, c)))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, App extends Applicative<?, App>, AppD extends Applicative<D, App>>
    LiftA3<A, B, C, D, App, AppD> liftA3() {
        return (LiftA3<A, B, C, D, App, AppD>) INSTANCE;
    }

    public static <A, B, C, D, App extends Applicative<?, App>, AppD extends Applicative<D, App>>
    Fn3<Applicative<A, App>, Applicative<B, App>, Applicative<C, App>, AppD> liftA3(Fn3<A, B, C, D> fn) {
        return LiftA3.<A, B, C, D, App, AppD>liftA3().apply(fn);
    }

    public static <A, B, C, D, App extends Applicative<?, App>, AppD extends Applicative<D, App>>
    Fn2<Applicative<B, App>, Applicative<C, App>, AppD> liftA3(Fn3<A, B, C, D> fn, Applicative<A, App> appA) {
        return LiftA3.<A, B, C, D, App, AppD>liftA3(fn).apply(appA);
    }

    public static <A, B, C, D, App extends Applicative<?, App>, AppD extends Applicative<D, App>>
    Fn1<Applicative<C, App>, AppD> liftA3(Fn3<A, B, C, D> fn, Applicative<A, App> appA, Applicative<B, App> appB) {
        return LiftA3.<A, B, C, D, App, AppD>liftA3(fn, appA).apply(appB);
    }

    public static <A, B, C, D, App extends Applicative<?, App>, AppD extends Applicative<D, App>>
    AppD liftA3(Fn3<A, B, C, D> fn, Applicative<A, App> appA, Applicative<B, App> appB, Applicative<C, App> appC) {
        return LiftA3.<A, B, C, D, App, AppD>liftA3(fn, appA, appB).apply(appC);
    }
}
