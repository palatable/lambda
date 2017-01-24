package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Fix;
import com.jnape.palatable.lambda.recursionschemes.Thunk;
import fix.Coalgebra;

import static com.jnape.palatable.lambda.recursionschemes.builtin.Hylomorphism.hylo;

public final class Anamorphism<A, F extends Functor, FA extends Functor<A, F>> implements Fn2<Coalgebra<A, FA>, A, Fix<F, Functor<Fix<F, ?>, F>>> {

    private static final Anamorphism INSTANCE = new Anamorphism();

    private Anamorphism() {
    }

    @Override
    public Fix<F, Functor<Fix<F, ?>, F>> apply(Coalgebra<A, FA> coalgebra, A a) {
        return hylo(f -> () -> new Thunk<>(() -> {
            f.fmap(fixed -> coalgebra.apply(fixed.unfix()))
            FA apply = coalgebra.apply(a);
            return apply;
        }).<Fix<F, ?>>fmap(a1 -> ana(coalgebra, a1)).get(), coalgebra, a);

    }

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor, FA extends Functor<A, F>> Anamorphism<A, F, FA> ana() {
        return INSTANCE;
    }

    public static <A, F extends Functor, FA extends Functor<A, F>> Fn1<A, Fix<F, Functor<Fix<F, ?>, F>>> ana(
            Coalgebra<A, FA> coalgebra) {
        return Anamorphism.<A, F, FA>ana().apply(coalgebra);
    }

    public static <A, F extends Functor, FA extends Functor<A, F>> Fix<F, Functor<Fix<F, ?>, F>> ana(
            Coalgebra<A, FA> coalgebra, A a) {
        return ana(coalgebra).apply(a);
    }
}
