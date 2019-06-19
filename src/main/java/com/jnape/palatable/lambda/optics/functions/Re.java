package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.functor.builtin.Const;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Tagged;
import com.jnape.palatable.lambda.optics.Iso;
import com.jnape.palatable.lambda.optics.Optic;
import com.jnape.palatable.lambda.optics.Prism;

/**
 * Turn an {@link Optic} with a unary mapping that can be used for setting (e.g. {@link Prism}, {@link Iso}) around for
 * viewing through the other direction.
 *
 * @param <S> used for unification of the {@link Optic optic's} unused morphism
 * @param <T> the result to read out
 * @param <A> used for unification of the {@link Optic optic's} unused morphism
 * @param <B> the value to read from
 */
public final class Re<S, T, A, B> implements
        Fn1<Optic<? super Tagged<?, ?>, ? super Identity<?>, S, T, A, B>,
                Optic<Profunctor<?, ?, ?>, Const<T, ?>, B, B, T, T>> {

    private static final Re<?, ?, ?, ?> INSTANCE = new Re<>();

    private Re() {
    }

    @Override
    public Optic<Profunctor<?, ?, ?>, Const<T, ?>, B, B, T, T> checkedApply(
            Optic<? super Tagged<?, ?>, ? super Identity<?>, S, T, A, B> optic) {
        return Optic.<Profunctor<?, ?, ?>, Const<T, ?>, B, B, T, T,
                Const<T, T>, Const<T, B>,
                Profunctor<T, Const<T, T>, ? extends Profunctor<?, ?, ?>>,
                Profunctor<B, Const<T, B>, ? extends Profunctor<?, ?, ?>>>optic(
                pafb -> pafb.diMap(
                        b -> optic.<Tagged<?, ?>, Identity<?>, Identity<B>, Identity<T>,
                                Tagged<A, Identity<B>>, Tagged<S, Identity<T>>>apply(
                                new Tagged<>(new Identity<>(b))).unTagged().runIdentity(),
                        fb -> new Const<>(fb.runConst())));
    }

    @SuppressWarnings("unchecked")
    public static <S, T, A, B> Re<S, T, A, B> re() {
        return (Re<S, T, A, B>) INSTANCE;
    }

    public static <S, T, A, B> Optic<Profunctor<?, ?, ?>, Const<T, ?>, B, B, T, T> re(
            Optic<? super Tagged<?, ?>, ? super Identity<?>, S, T, A, B> optic) {
        return Re.<S, T, A, B>re().apply(optic);
    }
}
