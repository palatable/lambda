package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Applicative;

/**
 * Lift into and apply an {@link Fn2} to two {@link Applicative} values, returning the result inside the same
 * {@link Applicative} context. Functionally equivalent to <code>appB.zip(appA.fmap(fn))</code>.
 *
 * @param <A>    the function's first argument type
 * @param <B>    the function's second argument type
 * @param <C>    the function's return type
 * @param <App>  the applicative witness
 * @param <AppC> the inferred applicative return type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA2<A, B, C, App extends Applicative<?, App>, AppC extends Applicative<C, App>> implements
        Fn3<Fn2<? super A, ? super B, ? extends C>, Applicative<A, App>, Applicative<B, App>, AppC> {

    private static final LiftA2<?, ?, ?, ?, ?> INSTANCE = new LiftA2<>();

    private LiftA2() {
    }

    @Override
    public AppC checkedApply(Fn2<? super A, ? super B, ? extends C> fn,
                             Applicative<A, App> appA,
                             Applicative<B, App> appB) {
        return appA.<C>zip(appB.fmap(b -> a -> fn.apply(a, b))).coerce();
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, App extends Applicative<?, App>, AppC extends Applicative<C, App>>
    LiftA2<A, B, C, App, AppC> liftA2() {
        return (LiftA2<A, B, C, App, AppC>) INSTANCE;
    }

    public static <A, B, C, App extends Applicative<?, App>, AppC extends Applicative<C, App>>
    Fn2<Applicative<A, App>, Applicative<B, App>, AppC> liftA2(Fn2<? super A, ? super B, ? extends C> fn) {
        return LiftA2.<A, B, C, App, AppC>liftA2().apply(fn);
    }

    public static <A, B, C, App extends Applicative<?, App>, AppC extends Applicative<C, App>>
    Fn1<Applicative<B, App>, AppC> liftA2(Fn2<? super A, ? super B, ? extends C> fn, Applicative<A, App> appA) {
        return LiftA2.<A, B, C, App, AppC>liftA2(fn).apply(appA);
    }

    public static <A, B, C, App extends Applicative<?, App>, AppC extends Applicative<C, App>>
    AppC liftA2(Fn2<? super A, ? super B, ? extends C> fn, Applicative<A, App> appA, Applicative<B, App> appB) {
        return LiftA2.<A, B, C, App, AppC>liftA2(fn, appA).apply(appB);
    }
}
