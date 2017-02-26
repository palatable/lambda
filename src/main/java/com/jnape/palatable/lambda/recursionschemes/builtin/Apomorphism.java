package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Fix;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;

public final class Apomorphism<A, F extends Functor, FT extends Functor<? extends CoProduct2<A, ? extends Fix<F, ? extends Functor<Fix<F, ?>, F>>>, F>>
        implements Fn2<Function<A, FT>, A, Fix<F, Functor<Fix<F, ?>, F>>> {

    private static final Apomorphism INSTANCE = new Apomorphism();

    private Apomorphism() {
    }

    @Override
    public Fix<F, Functor<Fix<F, ?>, F>> apply(Function<A, FT> fn, A a) {
        return fix(fn.apply(a).fmap(cp2 -> cp2.match(apo(fn), id())));
    }

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor, FT extends Functor<? extends CoProduct2<A, ? extends Fix<F, ? extends Functor<Fix<F, ?>, F>>>, F>> Apomorphism<A, F, FT> apo() {
        return INSTANCE;
    }

    public static <A, F extends Functor, FT extends Functor<? extends CoProduct2<A, ? extends Fix<F, ? extends Functor<Fix<F, ?>, F>>>, F>> Fn1<A, Fix<F, Functor<Fix<F, ?>, F>>> apo(
            Function<A, FT> fn) {
        return Apomorphism.<A, F, FT>apo().apply(fn);
    }

    public static <A, F extends Functor, FT extends Functor<? extends CoProduct2<A, ? extends Fix<F, ? extends Functor<Fix<F, ?>, F>>>, F>> Fix<F, Functor<Fix<F, ?>, F>> apo(
            Function<A, FT> fn, A a) {
        return apo(fn).apply(a);
    }
}
