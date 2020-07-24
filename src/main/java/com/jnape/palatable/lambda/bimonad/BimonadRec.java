package com.jnape.palatable.lambda.bimonad;

import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.MonadRec;

public interface BimonadRec<A, B extends BimonadRec<?, B>> extends MonadRec<A, B>, Comonad<A, B> {
    /**
     * {@inheritDoc}
     */
    @Override
    default <C> BimonadRec<C, B> fmap(Fn1<? super A, ? extends C> fn) {
        return MonadRec.super.<C>fmap(fn).coerce();
    }
}
