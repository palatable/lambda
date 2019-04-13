package com.jnape.palatable.lambda.lens.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.builtin.Const;
import com.jnape.palatable.lambda.lens.LensLike;

/**
 * Given a lens and a "larger" value <code>S</code>, retrieve a "smaller" value <code>A</code> by lifting the lens into
 * {@link Const}.
 * <p>
 * More idiomatically, this function can be used to treat a lens as a "getter" of <code>A</code>s from <code>S</code>s.
 *
 * @param <S> the type of the larger value
 * @param <T> the type of the larger updated value (unused, but necessary for composition)
 * @param <A> the type of the smaller retrieving value
 * @param <B> the type of the smaller setting value (unused, but necessary for composition)
 * @see Set
 * @see Over
 */
public final class View<S, T, A, B> implements Fn2<LensLike<S, T, A, B, ?>, S, A> {

    private static final View<?,?,?,?> INSTANCE = new View<>();

    private View() {
    }

    @Override
    public A apply(LensLike<S, T, A, B, ?> lens, S s) {
        return lens.<Const<A, ?>, Const<A, T>, Const<A, B>>apply(Const::new, s).runConst();
    }

    @SuppressWarnings("unchecked")
    public static <S, T, A, B> View<S, T, A, B> view() {
        return (View<S, T, A, B>) INSTANCE;
    }

    public static <S, T, A, B> Fn1<S, A> view(LensLike<S, T, A, B, ?> lens) {
        return View.<S, T, A, B>view().apply(lens);
    }

    public static <S, T, A, B> A view(LensLike<S, T, A, B, ?> lens, S s) {
        return view(lens).apply(s);
    }
}
