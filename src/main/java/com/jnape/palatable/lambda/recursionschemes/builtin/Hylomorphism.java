package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import fix.Coalgebra;

import static com.jnape.palatable.lambda.recursionschemes.builtin.Anamorphism.ana;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism.cata;

public final class Hylomorphism<A, B, F extends Functor, FA extends Functor<A, F>, FB extends Functor<B, F>> implements Fn3<Algebra<FB, B>, Coalgebra<A, FA>, A, B> {

    private static final Hylomorphism INSTANCE = new Hylomorphism();

    private Hylomorphism() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public B apply(Algebra<FB, B> algebra, Coalgebra<A, FA> coalgebra, A a) {
        return cata(algebra).compose(ana(coalgebra)).apply(a);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, F extends Functor, FA extends Functor<A, F>, FB extends Functor<B, F>> Hylomorphism<A, B, F, FA, FB> hylo() {
        return INSTANCE;
    }

    public static <A, B, F extends Functor, FA extends Functor<A, F>, FB extends Functor<B, F>> Fn2<Coalgebra<A, FA>, A, B> hylo(
            Algebra<FB, B> algebra) {
        return Hylomorphism.<A, B, F, FA, FB>hylo().apply(algebra);
    }

    public static <A, B, F extends Functor, FA extends Functor<A, F>, FB extends Functor<B, F>> Fn1<A, B> hylo(
            Algebra<FB, B> algebra, Coalgebra<A, FA> coalgebra) {
        return Hylomorphism.<A, B, F, FA, FB>hylo(algebra).apply(coalgebra);
    }

    public static <A, B, F extends Functor, FA extends Functor<A, F>, FB extends Functor<B, F>> B hylo(
            Algebra<FB, B> algebra, Coalgebra<A, FA> coalgebra, A a) {
        return hylo(algebra, coalgebra).apply(a);
    }
}
