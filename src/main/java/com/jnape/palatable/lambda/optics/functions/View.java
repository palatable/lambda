package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.builtin.Const;
import com.jnape.palatable.lambda.optics.Optic;

/**
 * Given an {@link Optic} and a "larger" value <code>S</code>, retrieve a "smaller" value <code>A</code> by lifting the
 * {@link Optic} into the {@link Const} functor.
 * <p>
 * More idiomatically, this function can be used to treat a {@link Optic} as a "getter" of <code>A</code>s from
 * <code>S</code>s.
 *
 * @param <S> the type of the larger value
 * @param <T> the type of the larger updated value (unused, but necessary for composition)
 * @param <A> the type of the smaller retrieving value
 * @param <B> the type of the smaller setting value (unused, but necessary for composition)
 * @see Set
 * @see Over
 */
public final class View<S, T, A, B> implements Fn2<Optic<? super Fn1<?, ?>, ? super Const<A, ?>, S, T, A, B>, S, A> {

    private static final View<?, ?, ?, ?> INSTANCE = new View<>();

    private View() {
    }

    @Override
    public A apply(Optic<? super Fn1<?, ?>, ? super Const<A, ?>, S, T, A, B> optic, S s) {
        return optic.<Fn1<?, ?>, Const<A, ?>, Const<A, B>, Const<A, T>, Fn1<A, Const<A, B>>, Fn1<S, Const<A, T>>>apply(
                Const::new).apply(s).runConst();
    }

    @SuppressWarnings("unchecked")
    public static <S, T, A, B> View<S, T, A, B> view() {
        return (View<S, T, A, B>) INSTANCE;
    }

    public static <S, T, A, B> Fn1<S, A> view(Optic<? super Fn1<?, ?>, ? super Const<A, ?>, S, T, A, B> optic) {
        return View.<S, T, A, B>view().apply(optic);
    }

    public static <S, T, A, B> A view(Optic<? super Fn1<?, ?>, ? super Const<A, ?>, S, T, A, B> optic, S s) {
        return view(optic).apply(s);
    }
}
