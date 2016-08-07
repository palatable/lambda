package com.jnape.palatable.lambda.lens;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.lens.functions.Over.over;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;

@FunctionalInterface
public interface Lens<S, T, A, B> extends Functor<T>, Profunctor<S, T> {

    <FT extends Functor<T>, FB extends Functor<B>> FT apply(Function<? super A, ? extends FB> fn, S s);

    default <FT extends Functor<T>, FB extends Functor<B>> Fixed<S, T, A, B, FT, FB> fix() {
        return this::<FT, FB>apply;
    }

    @Override
    default <U> Lens<S, U, A, B> fmap(Function<? super T, ? extends U> fn) {
        return this.compose(Lens.<S, U, S, T>lens(id(), (s, t) -> fn.apply(t)));
    }

    @Override
    @SuppressWarnings("unchecked")
    default <R> Lens<R, T, A, B> diMapL(Function<R, S> fn) {
        return (Lens<R, T, A, B>) Profunctor.super.diMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> Lens<S, U, A, B> diMapR(Function<T, U> fn) {
        return (Lens<S, U, A, B>) Profunctor.super.diMapR(fn);
    }

    @Override
    default <R, U> Lens<R, U, A, B> diMap(Function<R, S> lFn, Function<T, U> rFn) {
        return this.compose(lens(lFn, (r, t) -> rFn.apply(t)));
    }

    default <C, D> Lens<S, T, C, D> andThen(Lens<A, B, C, D> f) {
        return f.compose(this);
    }

    default <Q, R> Lens<Q, R, A, B> compose(Lens<Q, R, S, T> g) {
        return lens(view(g).fmap(view(this)), (q, b) -> over(g, set(this, b), q));
    }

    static <S, T, A, B> Lens<S, T, A, B> lens(Function<? super S, ? extends A> getter,
                                              BiFunction<? super S, ? super B, ? extends T> setter) {
        return new Lens<S, T, A, B>() {
            @Override
            @SuppressWarnings("unchecked")
            public <FT extends Functor<T>, FB extends Functor<B>> FT apply(Function<? super A, ? extends FB> fn,
                                                                           S s) {
                return (FT) fn.apply(getter.apply(s)).fmap(b -> setter.apply(s, b));
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <S, A> Lens.Simple<S, A> simpleLens(Function<? super S, ? extends A> getter,
                                               BiFunction<? super S, ? super A, ? extends S> setter) {
        return lens(getter, setter)::apply;
    }

    @FunctionalInterface
    interface Simple<S, A> extends Lens<S, S, A, A> {

        @Override
        default <FS extends Functor<S>, FA extends Functor<A>> Fixed<S, A, FS, FA> fix() {
            return Lens.super.<FS, FA>fix()::apply;
        }

        @SuppressWarnings("unchecked")
        default <Q> Lens.Simple<Q, A> compose(Lens.Simple<Q, S> g) {
            return Lens.super.compose(g)::apply;
        }

        default <B> Lens.Simple<S, B> andThen(Lens.Simple<A, B> f) {
            return f.compose(this);
        }

        @FunctionalInterface
        interface Fixed<S, A, FS extends Functor<S>, FA extends Functor<A>>
                extends Lens.Fixed<S, S, A, A, FS, FA> {
        }
    }

    @FunctionalInterface
    interface Fixed<S, T, A, B, FT extends Functor<T>, FB extends Functor<B>>
            extends Fn2<Function<? super A, ? extends FB>, S, FT> {
    }
}
