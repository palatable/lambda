package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import com.jnape.palatable.lambda.recursionschemes.Fix;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism.cata;

public final class Paramorphism<A, F extends Functor, Tuple extends Tuple2<? extends Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A>, FA extends Functor<Tuple, F>>
        implements Fn2<Function<FA, A>, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> {
    private static final Paramorphism INSTANCE = new Paramorphism();

    private Paramorphism() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public A apply(Function<FA, A> fn, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixed) {
        Algebra<FA, Tuple> algebra = fa -> (Tuple) tuple(fix(fa.fmap(Tuple2::_1)), fn.apply(fa));
        return cata(algebra, fixed)._2();
    }

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor, Tuple extends Tuple2<? extends Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A>, FA extends Functor<Tuple, F>> Paramorphism<A, F, Tuple, FA> para() {
        return (Paramorphism<A, F, Tuple, FA>) INSTANCE;
    }

    public static <A, F extends Functor, Tuple extends Tuple2<? extends Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A>, FA extends Functor<Tuple, F>> Fn1<Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> para(
            Function<FA, A> fn) {
        return Paramorphism.<A, F, Tuple, FA>para().apply(fn);
    }

    public static <A, F extends Functor, Tuple extends Tuple2<? extends Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A>, FA extends Functor<Tuple, F>> A para(
            Function<FA, A> fn, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixed) {
        return para(fn).apply(fixed);
    }
}
