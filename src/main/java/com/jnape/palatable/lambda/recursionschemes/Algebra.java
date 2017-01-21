package com.jnape.palatable.lambda.recursionschemes;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

public interface Algebra<F extends Functor<A, ?>, A> extends Fn1<F, A> {
}
