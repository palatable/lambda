package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.specialized.checked.Runtime;
import com.jnape.palatable.lambda.monoid.Monoid;

@FunctionalInterface
public interface BiMonoidFactory<A, B, C> extends BiSemigroupFactory<A, B, C> {

    @Override
    Monoid<C> checkedApply(A a, B b) throws Throwable;

    @Override
    default MonoidFactory<B, C> checkedApply(A a) throws Throwable {
        return b -> checkedApply(a, b);
    }

    @Override
    default Monoid<C> apply(A a, B b) {
        try {
            return checkedApply(a, b);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }

    @Override
    default MonoidFactory<B, C> apply(A a) {
        return b -> apply(a, b);
    }

    @Override
    default BiMonoidFactory<B, A, C> flip() {
        return (b, a) -> checkedApply(a, b);
    }

    @Override
    default MonoidFactory<? super Product2<? extends A, ? extends B>, C> uncurry() {
        return ab -> apply(ab._1()).apply(ab._2());
    }
}
