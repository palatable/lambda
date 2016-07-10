package com.jnape.palatable.lambda.lens.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.builtin.Const;
import com.jnape.palatable.lambda.lens.Lens;

public final class View<S, T, A, B> implements Fn2<Lens<S, T, A, B>, S, A> {

    private View() {
    }

    @Override
    public A apply(Lens<S, T, A, B> lens, S s) {
        return lens.<Const<A, T>, Const<A, B>>fix().apply(Const::new, s).runConst();
    }

    public static <S, T, A, B> View<S, T, A, B> view() {
        return new View<>();
    }

    public static <S, T, A, B> Fn1<S, A> view(Lens<S, T, A, B> lens) {
        return View.<S, T, A, B>view().apply(lens);
    }

    public static <S, T, A, B> A view(Lens<S, T, A, B> lens, S s) {
        return view(lens).apply(s);
    }
}
