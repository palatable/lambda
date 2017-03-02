package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Coalgebra;
import com.jnape.palatable.lambda.recursionschemes.Fix;

import java.util.function.Function;

import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Anamorphism.ana;

public final class GApomorphism<A, B, F extends Functor, FT extends Functor<CoProduct2<A, B>, F>, FB extends Functor<B, F>>
        implements Fn3<Function<A, FT>, Coalgebra<B, FB>, A, Fix<F, Functor<Fix<F, ?>, F>>> {

    private static final GApomorphism INSTANCE = new GApomorphism();

    private GApomorphism() {
    }

    @Override
    public Fix<F, Functor<Fix<F, ?>, F>> apply(Function<A, FT> fn, Coalgebra<B, FB> coalgebra, A a) {
        return fix(fn.apply(a).fmap((Function<CoProduct2<A, B>, Fix<F, Functor<Fix<F, ?>, F>>>)
                                            cp2 -> cp2.match(gApo(fn, coalgebra), ana(coalgebra))));
    }

    @SuppressWarnings("unchecked")
    public static <A, B, F extends Functor, FT extends Functor<CoProduct2<A, B>, F>, FB extends Functor<B, F>> GApomorphism<A, B, F, FT, FB> gApo() {
        return INSTANCE;
    }

    public static <A, B, F extends Functor, FT extends Functor<CoProduct2<A, B>, F>, FB extends Functor<B, F>> Fn2<Coalgebra<B, FB>, A, Fix<F, Functor<Fix<F, ?>, F>>> gApo(
            Function<A, FT> fn) {
        return GApomorphism.<A, B, F, FT, FB>gApo().apply(fn);
    }

    public static <A, B, F extends Functor, FT extends Functor<CoProduct2<A, B>, F>, FB extends Functor<B, F>> Fn1<A, Fix<F, Functor<Fix<F, ?>, F>>> gApo(
            Function<A, FT> fn, Coalgebra<B, FB> coalgebra) {
        return GApomorphism.<A, B, F, FT, FB>gApo(fn).apply(coalgebra);
    }

    public static <A, B, F extends Functor, FT extends Functor<CoProduct2<A, B>, F>, FB extends Functor<B, F>> Fix<F, Functor<Fix<F, ?>, F>> gApo(
            Function<A, FT> fn, Coalgebra<B, FB> coalgebra, A a) {
        return gApo(fn, coalgebra).apply(a);
    }
}
