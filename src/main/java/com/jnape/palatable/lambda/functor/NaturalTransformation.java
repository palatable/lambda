package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.Fn1;

@FunctionalInterface
public interface NaturalTransformation<A, FA extends Functor<A, ?>, GA extends Functor<A, ?>> extends Fn1<FA, GA> {

    static <A, FA extends Functor<A, ?>> NaturalTransformation<A, FA, FA> identity() {
        return fa -> fa;
    }
}
