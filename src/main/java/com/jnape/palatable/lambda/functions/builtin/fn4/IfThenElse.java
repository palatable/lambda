package com.jnape.palatable.lambda.functions.builtin.fn4;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;

public final class IfThenElse<A, B> implements
        Fn4<Fn1<? super A, ? extends Boolean>, Fn1<? super A, ? extends B>, Fn1<? super A, ? extends B>, A, B> {

    private static final IfThenElse<?, ?> INSTANCE = new IfThenElse<>();

    private IfThenElse() {
    }

    @Override
    public B checkedApply(Fn1<? super A, ? extends Boolean> predicate, Fn1<? super A, ? extends B> thenCase,
                          Fn1<? super A, ? extends B> elseCase, A a) {
        return predicate.apply(a) ? thenCase.apply(a) : elseCase.apply(a);
    }

    @SuppressWarnings("unchecked")
    public static <A, B> IfThenElse<A, B> ifThenElse() {
        return (IfThenElse<A, B>) INSTANCE;
    }

    public static <A, B> Fn3<Fn1<? super A, ? extends B>, Fn1<? super A, ? extends B>, A, B> ifThenElse(
            Fn1<? super A, ? extends Boolean> predicate) {
        return IfThenElse.<A, B>ifThenElse().apply(predicate);
    }

    public static <A, B> Fn2<Fn1<? super A, ? extends B>, A, B> ifThenElse(Fn1<? super A, ? extends Boolean> predicate,
                                                                           Fn1<? super A, ? extends B> thenCase) {
        return IfThenElse.<A, B>ifThenElse(predicate).apply(thenCase);
    }

    public static <A, B> Fn1<A, B> ifThenElse(Fn1<? super A, ? extends Boolean> predicate,
                                              Fn1<? super A, ? extends B> thenCase,
                                              Fn1<? super A, ? extends B> elseCase) {
        return IfThenElse.<A, B>ifThenElse(predicate, thenCase).apply(elseCase);
    }

    public static <A, B> B ifThenElse(Fn1<? super A, ? extends Boolean> predicate, Fn1<? super A, ? extends B> thenCase,
                                      Fn1<? super A, ? extends B> elseCase, A a) {
        return ifThenElse(predicate, thenCase, elseCase).apply(a);
    }
}
