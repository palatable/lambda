package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.semigroup.Semigroup;

@FunctionalInterface
public interface BiSemigroupFactory<A, B, C> extends Fn4<A, B, C, C, C> {

    @Override
    Semigroup<C> checkedApply(A a, B b) throws Throwable;

    @Override
    default C checkedApply(A a, B b, C c, C d) throws Throwable {
        return checkedApply(a, b).checkedApply(c, d);
    }

    @Override
    default Semigroup<C> apply(A a, B b) {
        return Fn4.super.apply(a, b)::apply;
    }

    @Override
    default SemigroupFactory<B, C> apply(A a) {
        return b -> apply(a, b);
    }

    @Override
    default BiSemigroupFactory<B, A, C> flip() {
        return (b, a) -> apply(a, b);
    }

    @Override
    default SemigroupFactory<? super Product2<? extends A, ? extends B>, C> uncurry() {
        return ab -> apply(ab._1(), ab._2());
    }
}
