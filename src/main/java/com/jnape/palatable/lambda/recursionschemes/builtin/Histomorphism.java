package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import com.jnape.palatable.lambda.recursionschemes.Cofree;
import com.jnape.palatable.lambda.recursionschemes.Fix;

import java.util.function.Function;

import static com.jnape.palatable.lambda.recursionschemes.Cofree.cofree;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism.cata;

public final class Histomorphism<A, F extends Functor, CofreeF extends Functor<Cofree<F, A, ?>, F>>
        implements Fn2<Function<CofreeF, A>, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> {

    private static final Histomorphism INSTANCE = new Histomorphism();

    private Histomorphism() {
    }

    @Override
    public A apply(Function<CofreeF, A> fn, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fix) {
        return cata((Algebra<CofreeF, Cofree<F, A, ?>>) cofreeF -> cofree(cofreeF, fn.apply(cofreeF)), fix)
                .attr();
    }

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor, CofreeF extends Functor<Cofree<F, A, ?>, F>> Histomorphism<A, F, CofreeF> histo() {
        return INSTANCE;
    }

    public static <A, F extends Functor, CofreeF extends Functor<Cofree<F, A, ?>, F>> Fn1<Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> histo(
            Function<CofreeF, A> fn) {
        return Histomorphism.<A, F, CofreeF>histo().apply(fn);
    }

    public static <A, F extends Functor, CofreeF extends Functor<Cofree<F, A, ?>, F>> A histo(
            Function<CofreeF, A> fn,
            Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fix) {
        return histo(fn).apply(fix);
    }
}
