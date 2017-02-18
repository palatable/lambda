package com.jnape.palatable.lambda.recursionschemes;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Functor;
import testsupport.recursion.NatF;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism.cata;
import static testsupport.recursion.NatF.s;
import static testsupport.recursion.NatF.z;

public class Spike {

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor, Unfixed extends Functor<? extends Fix<F, ?>, F>> A para(
            Function<Functor<Tuple2<Fix<F, Unfixed>, A>, F>, A> fn,
            Fix<F, Unfixed> fixed) {
        Algebra<Functor<Tuple2<Fix<F, Unfixed>, A>, F>, Tuple2<Fix<F, Unfixed>, A>> alg =
                f -> tuple(fix((Unfixed) f.fmap(Tuple2::_1)), fn.apply(f));
        return cata(alg, fixed)._2();
    }

    public static void main(String[] args) {
        Function<Functor<Tuple2<Fix<NatF, NatF<Fix<NatF, ?>>>, Integer>, NatF>, Integer> x = new Function<Functor<Tuple2<Fix<NatF, NatF<Fix<NatF, ?>>>, Integer>, NatF>, Integer>() {
            @Override
            public Integer apply(Functor<Tuple2<Fix<NatF, NatF<Fix<NatF, ?>>>, Integer>, NatF> fnat) {
                NatF<Tuple2<Fix<NatF, NatF<Fix<NatF, ?>>>, Integer>> xn = (NatF<Tuple2<Fix<NatF, NatF<Fix<NatF, ?>>>, Integer>>) fnat;
                return xn.match(constantly(0), s -> s.carrier()._2() + 1);
            }
        };
        System.out.println(para(x, fix(s(fix(z())))));
    }
}
