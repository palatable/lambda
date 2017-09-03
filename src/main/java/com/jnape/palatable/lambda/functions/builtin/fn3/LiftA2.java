package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.functions.Fn2.fn2;

/**
 * Lift into and apply a {@link BiFunction} to two {@link Applicative} values, returning the result inside the same
 * {@link Applicative} context. Equivalent ot <code>appB.zip(appA.fmap(fn))</code>.
 *
 * @param <A>   the function's first argument type
 * @param <B>   the function's second argument type
 * @param <C>   the function's return type
 * @param <App> the applicative unification type
 * @see Applicative#zip(Applicative)
 */
public final class LiftA2<A, B, C, App extends Applicative> implements Fn3<BiFunction<? super A, ? super B, ? extends C>,
        Applicative<A, App>, Applicative<B, App>, Applicative<C, App>> {

    private static final LiftA2 INSTANCE = new LiftA2();

    private LiftA2() {
    }

    @Override
    public Applicative<C, App> apply(BiFunction<? super A, ? super B, ? extends C> fn,
                                     Applicative<A, App> appA,
                                     Applicative<B, App> appB) {
        return appB.zip(appA.fmap(fn2(fn)));
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, App extends Applicative> LiftA2<A, B, C, App> liftA2() {
        return INSTANCE;
    }

    public static <A, B, C, App extends Applicative> Fn2<Applicative<A, App>, Applicative<B, App>, Applicative<C, App>> liftA2(
            BiFunction<? super A, ? super B, ? extends C> fn) {
        return LiftA2.<A, B, C, App>liftA2().apply(fn);
    }

    public static <A, B, C, App extends Applicative> Fn1<Applicative<B, App>, Applicative<C, App>> liftA2(
            BiFunction<? super A, ? super B, ? extends C> fn, Applicative<A, App> appA) {
        return LiftA2.<A, B, C, App>liftA2(fn).apply(appA);
    }

    public static <A, B, C, App extends Applicative> Applicative<C, App> liftA2(
            BiFunction<? super A, ? super B, ? extends C> fn, Applicative<A, App> appA, Applicative<B, App> appB) {
        return LiftA2.<A, B, C, App>liftA2(fn, appA).apply(appB);
    }
}
