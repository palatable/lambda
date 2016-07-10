package com.jnape.palatable.lambda.lens;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;

@FunctionalInterface
public interface Lens<S, T, A, B> {

    <FT extends Functor<T>, FB extends Functor<B>> FT apply(Fn1<? super A, ? extends FB> fn, S s);

    default <FT extends Functor<T>, FB extends Functor<B>> Fixed<S, T, A, B, FT, FB> fix() {
        return this::<FT, FB>apply;
    }

    static <S, T, A, B> Lens<S, T, A, B> lens(Fn1<? super S, ? extends A> getter,
                                              Fn2<? super S, ? super B, ? extends T> setter) {
        return new Lens<S, T, A, B>() {
            @Override
            @SuppressWarnings("unchecked")
            public <FT extends Functor<T>, FB extends Functor<B>> FT apply(Fn1<? super A, ? extends FB> fn,
                                                                           S s) {
                return (FT) fn.apply(getter.apply(s)).fmap(setter.apply(s));
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <S, A> Lens.Simple<S, A> simpleLens(Fn1<? super S, ? extends A> getter,
                                               Fn2<? super S, ? super A, ? extends S> setter) {
        return lens(getter, setter)::apply;
    }

    interface Simple<S, A> extends Lens<S, S, A, A> {
    }

    interface Fixed<S, T, A, B, FT extends Functor<T>, FB extends Functor<B>>
            extends Fn2<Fn1<? super A, ? extends FB>, S, FT> {
    }
}
