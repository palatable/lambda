package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;

/**
 * Lenses for {@link Maybe}.
 */
public final class MaybeLens {
    private MaybeLens() {
    }

    public static <S, T, A, B> Lens<Maybe<S>, T, A, B> liftS(Lens<S, T, A, B> lens, S defaultValue) {
        return lens.mapS(m -> m.orElse(defaultValue));
    }

    public static <S, T, A, B> Lens<S, Maybe<T>, A, B> liftT(Lens<S, T, A, B> lens) {
        return lens.mapT(Maybe::just);
    }

    public static <S, T, A, B> Lens<S, T, Maybe<A>, B> liftA(Lens<S, T, A, B> lens) {
        return lens.mapA(Maybe::just);
    }

    public static <S, T, A, B> Lens<S, T, A, Maybe<B>> liftB(Lens<S, T, A, B> lens, B defaultValue) {
        return lens.mapB(m -> m.orElse(defaultValue));
    }

    public static <S, A> Lens.Simple<Maybe<S>, Maybe<A>> asMaybe(Lens<S, S, A, A> lens) {
        return simpleLens(m -> m.fmap(view(lens)),
                          (maybeS, maybeB) -> maybeS.flatMap(s -> maybeB.fmap(a -> {
                              return set(lens, a, s);
                          })));
    }
}
