package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import com.jnape.palatable.lambda.recursionschemes.Fix;

public final class Catamorphism<A, F extends Functor, FA extends Functor<A, F>> implements Fn2<Algebra<FA, A>, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> {

    private static final Catamorphism INSTANCE = new Catamorphism();

    private Catamorphism() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public A apply(Algebra<FA, A> algebra, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixed) {
        return algebra.apply((FA) fixed.unfix().fmap(x -> cata(algebra, (Fix<F, Functor<Fix<F, ?>, F>>) x)));
    }

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor, FA extends Functor<A, F>> Catamorphism<A, F, FA> cata() {
        return INSTANCE;
    }

    public static <A, F extends Functor, FA extends Functor<A, F>> Fn1<Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> cata(
            Algebra<FA, A> algebra) {
        return Catamorphism.<A, F, FA>cata().apply(algebra);
    }

    public static <A, F extends Functor, FA extends Functor<A, F>> A cata(
            Algebra<FA, A> algebra, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixed) {
        return cata(algebra).apply(fixed);
    }
}
