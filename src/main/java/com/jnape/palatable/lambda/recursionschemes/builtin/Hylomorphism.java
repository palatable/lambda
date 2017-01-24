package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import fix.Coalgebra;

public final class Hylomorphism<A, B, F extends Functor, FA extends Functor<A, F>, FB extends Functor<B, F>> implements Fn3<Algebra<FB, B>, Coalgebra<A, FA>, A, B> {

    private static final Hylomorphism INSTANCE = new Hylomorphism();

    private Hylomorphism() {
    }

    @SuppressWarnings("unchecked")
    public A fapply(Algebra<FA, A> algebra, Coalgebra<A, FA> coalgebra, A a) {
        A x = a;
        FA fa = coalgebra.apply(x);
        FA previous = null;
        while (fa != previous) {
            previous = fa;
            if (fa.fmap(a1 -> algebra.apply(coalgebra.apply(a1))).equals(fa))
                return x;
            x = algebra.apply(previous);
            fa = coalgebra.apply(algebra.apply(fa));
        }

        return x;
//
//        return algebra.apply((FB) coalgebra.apply(a).fmap(hylo(algebra, coalgebra)));
//        return cata(algebra).compose(ana(coalgebra)).apply(a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public B apply(Algebra<FB, B> algebra, Coalgebra<A, FA> coalgebra, A a) {
        return algebra.apply((FB) coalgebra.apply(a).fmap(hylo(algebra, coalgebra)));
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
