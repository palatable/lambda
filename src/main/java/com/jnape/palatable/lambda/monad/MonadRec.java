package com.jnape.palatable.lambda.monad;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

public interface MonadRec<A, M extends MonadRec<?, M>> extends Monad<A, M> {

    <B> MonadRec<B, M> trampolineM(Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, M>> fn);

    @Override
    <B> MonadRec<B, M> flatMap(Fn1<? super A, ? extends Monad<B, M>> f);

    @Override
    <B> MonadRec<B, M> pure(B b);

    @Override
    default <B> MonadRec<B, M> fmap(Fn1<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    @Override
    default <B> MonadRec<B, M> zip(Applicative<Fn1<? super A, ? extends B>, M> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    default <B> Lazy<? extends MonadRec<B, M>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, M>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Applicative<B, M>::coerce);
    }

    @Override
    default <B> MonadRec<B, M> discardL(Applicative<B, M> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    default <B> MonadRec<A, M> discardR(Applicative<B, M> appB) {
        return Monad.super.discardR(appB).coerce();
    }
}
