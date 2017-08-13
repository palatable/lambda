package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.NaturalTransformation;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import com.jnape.palatable.lambda.recursionschemes.Fix;

import static com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism.cata;

public final class Prepromorphism<A, F extends Functor, FA extends Functor<A, F>, GA extends Functor<A, F>> implements
        Fn3<Algebra<FA, A>, NaturalTransformation<A, GA, FA>, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> {

    private static final Prepromorphism INSTANCE = new Prepromorphism();

    private Prepromorphism() {
    }

    @Override
    public A apply(Algebra<FA, A> algebra, NaturalTransformation<A, GA, FA> naturalTransformation,
                   Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixed) {
        return cata((Algebra<GA, A>) ga -> algebra.apply(naturalTransformation.apply(ga)), fixed);
    }

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor, FA extends Functor<A, F>, GA extends Functor<A, F>> Prepromorphism<A, F, FA, GA> prepro() {
        return INSTANCE;
    }

    public static <A, F extends Functor, FA extends Functor<A, F>, GA extends Functor<A, F>> Fn2<NaturalTransformation<A, GA, FA>, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> prepro(
            Algebra<FA, A> algebra) {
        return Prepromorphism.<A, F, FA, GA>prepro().apply(algebra);
    }

    public static <A, F extends Functor, FA extends Functor<A, F>, GA extends Functor<A, F>> Fn1<Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> prepro(
            Algebra<FA, A> algebra, NaturalTransformation<A, GA, FA> naturalTransformation) {
        return Prepromorphism.<A, F, FA, GA>prepro(algebra).apply(naturalTransformation);
    }

    public static <A, F extends Functor, FA extends Functor<A, F>, GA extends Functor<A, F>> A prepro(
            Algebra<FA, A> algebra,
            NaturalTransformation<A, GA, FA> naturalTransformation,
            Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixed) {
        return prepro(algebra, naturalTransformation).apply(fixed);
    }
}
