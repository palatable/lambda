package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import com.jnape.palatable.lambda.recursionschemes.Fix;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism.cata;

public final class Zygomorphism<A, B, F extends Functor, FB extends Functor<B, F>, FAB extends Functor<Tuple2<A, B>, F>> implements
        Fn3<Function<FAB, A>, Algebra<FB, B>, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> {

    private static final Zygomorphism INSTANCE = new Zygomorphism();

    private Zygomorphism() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public A apply(Function<FAB, A> fn, Algebra<FB, B> algebra,
                   Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixed) {
        return fn.apply((FAB) fixed.unfix().fmap(f -> {
            A a = zygo(fn, algebra, (Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>) f);
            B b = cata(algebra, fixed);
            return tuple(a, b);
        }));
    }

    @SuppressWarnings("unchecked")
    public static <A, B, F extends Functor, FB extends Functor<B, F>, FAB extends Functor<Tuple2<A, B>, F>> Zygomorphism<A, B, F, FB, FAB> zygo() {
        return INSTANCE;
    }

    public static <A, B, F extends Functor, FB extends Functor<B, F>, FAB extends Functor<Tuple2<A, B>, F>> Fn2<Algebra<FB, B>, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> zygo(
            Function<FAB, A> fn) {
        return Zygomorphism.<A, B, F, FB, FAB>zygo().apply(fn);
    }

    public static <A, B, F extends Functor, FB extends Functor<B, F>, FAB extends Functor<Tuple2<A, B>, F>> Fn1<Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, A> zygo(
            Function<FAB, A> fn, Algebra<FB, B> algebra) {
        return Zygomorphism.<A, B, F, FB, FAB>zygo(fn).apply(algebra);
    }

    public static <A, B, F extends Functor, FB extends Functor<B, F>, FAB extends Functor<Tuple2<A, B>, F>> A zygo(
            Function<FAB, A> fn, Algebra<FB, B> algebra, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixed) {
        return zygo(fn, algebra).apply(fixed);
    }
}
