package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Cocartesian;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.choice.Choice2.b;

public final class Tagged<S, B> implements Monad<B, Tagged<S, ?>>, Cocartesian<S, B, Tagged<?, ?>> {
    private final B b;

    public Tagged(B b) {
        this.b = b;
    }

    public B unTagged() {
        return b;
    }

    @Override
    public <C> Tagged<S, C> flatMap(Function<? super B, ? extends Monad<C, Tagged<S, ?>>> f) {
        return f.apply(b).coerce();
    }

    @Override
    public <C> Tagged<S, C> pure(C c) {
        return new Tagged<>(c);
    }

    @Override
    public <C> Tagged<S, C> fmap(Function<? super B, ? extends C> fn) {
        return Monad.super.<C>fmap(fn).coerce();
    }

    @Override
    public <C> Tagged<S, C> zip(Applicative<Function<? super B, ? extends C>, Tagged<S, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <C> Tagged<S, C> discardL(Applicative<C, Tagged<S, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <C> Tagged<S, B> discardR(Applicative<C, Tagged<S, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <C> Tagged<Choice2<C, S>, Choice2<C, B>> cocartesian() {
        return new Tagged<>(b(b));
    }

    @Override
    public <Z, C> Tagged<Z, C> diMap(Function<? super Z, ? extends S> lFn,
                                     Function<? super B, ? extends C> rFn) {
        return new Tagged<>(rFn.apply(b));
    }

    @Override
    public <Z> Tagged<Z, B> diMapL(Function<? super Z, ? extends S> fn) {
        return (Tagged<Z, B>) Cocartesian.super.<Z>diMapL(fn);
    }

    @Override
    public <C> Tagged<S, C> diMapR(Function<? super B, ? extends C> fn) {
        return (Tagged<S, C>) Cocartesian.super.<C>diMapR(fn);
    }

    @Override
    public <Z> Tagged<Z, B> contraMap(Function<? super Z, ? extends S> fn) {
        return (Tagged<Z, B>) Cocartesian.super.<Z>contraMap(fn);
    }
}
