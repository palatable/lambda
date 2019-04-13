package com.jnape.palatable.lambda.functions.builtin.fn4;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;

import java.util.function.Function;

public final class IfThenElse<A, B> implements Fn4<Function<? super A, ? extends Boolean>, Function<? super A, ? extends B>, Function<? super A, ? extends B>, A, B> {

    private static final IfThenElse<?, ?> INSTANCE = new IfThenElse<>();

    private IfThenElse() {
    }

    @Override
    public B apply(Function<? super A, ? extends Boolean> predicate, Function<? super A, ? extends B> thenCase,
                   Function<? super A, ? extends B> elseCase, A a) {
        return predicate.apply(a) ? thenCase.apply(a) : elseCase.apply(a);
    }

    @SuppressWarnings("unchecked")
    public static <A, B> IfThenElse<A, B> ifThenElse() {
        return (IfThenElse<A, B>) INSTANCE;
    }

    public static <A, B> Fn3<Function<? super A, ? extends B>, Function<? super A, ? extends B>, A, B> ifThenElse(
            Function<? super A, ? extends Boolean> predicate) {
        return IfThenElse.<A, B>ifThenElse().apply(predicate);
    }

    public static <A, B> Fn2<Function<? super A, ? extends B>, A, B> ifThenElse(
            Function<? super A, ? extends Boolean> predicate, Function<? super A, ? extends B> thenCase) {
        return IfThenElse.<A, B>ifThenElse(predicate).apply(thenCase);
    }

    public static <A, B> Fn1<A, B> ifThenElse(
            Function<? super A, ? extends Boolean> predicate, Function<? super A, ? extends B> thenCase,
            Function<? super A, ? extends B> elseCase) {
        return IfThenElse.<A, B>ifThenElse(predicate, thenCase).apply(elseCase);
    }

    public static <A, B> B ifThenElse(
            Function<? super A, ? extends Boolean> predicate, Function<? super A, ? extends B> thenCase,
            Function<? super A, ? extends B> elseCase,
            A a) {
        return ifThenElse(predicate, thenCase, elseCase).apply(a);
    }
}
