package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Coalgebra;
import com.jnape.palatable.lambda.recursionschemes.Fix;
import com.jnape.palatable.lambda.recursionschemes.Free;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.recursionschemes.Free.pure;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Anamorphism.ana;

public final class Futumorphism<A, F extends Functor, FR extends Functor<Free<A, F, ?>, F>> implements Fn2<Function<A, FR>, A, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>> {

    private static final Futumorphism INSTANCE = new Futumorphism();

    private Futumorphism() {
    }

    @Override
    @SuppressWarnings({"unchecked", "RedundantCast"})
    public Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> apply(Function<A, FR> fn, A a) {
        return ana((Coalgebra<Free<A, F, ?>, FR>) free -> (FR) free.match(fn, id()), pure(a));
    }

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor, FR extends Functor<Free<A, F, ?>, F>> Futumorphism<A, F, FR> futu() {
        return INSTANCE;
    }

    public static <A, F extends Functor, FR extends Functor<Free<A, F, ?>, F>> Fn1<A, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>> futu(
            Function<A, FR> fn) {
        return Futumorphism.<A, F, FR>futu().apply(fn);
    }

    public static <A, F extends Functor, FR extends Functor<Free<A, F, ?>, F>> Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> futu(
            Function<A, FR> fn, A a) {
        return futu(fn).apply(a);
    }
}
