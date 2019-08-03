package com.jnape.palatable.lambda.monad;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.transformer.MonadT;
import com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT;

/**
 * A class of {@link Monad monads} that offer a stack-safe interface for performing arbitrarily many
 * {@link Monad#flatMap(Fn1) flatmap-like} operations via {@link MonadRec#trampolineM(Fn1)}.
 *
 * @param <A> the carrier type
 * @param <M> the {@link MonadRec witness}
 */
public interface MonadRec<A, M extends MonadRec<?, M>> extends Monad<A, M> {

    /**
     * Given some operation yielding a {@link RecursiveResult} inside this {@link MonadRec}, internally trampoline the
     * operation until it yields a {@link RecursiveResult#terminate(Object) termination} instruction.
     * <p>
     * Stack-safety depends on implementations guaranteeing that the growth of the call stack is a constant factor
     * independent of the number of invocations of the operation. For various examples of how this can be achieved in
     * stereotypical circumstances, see the referenced types.
     *
     * @param fn  the function to internally trampoline
     * @param <B> the ultimate resulting carrier type
     * @return the trampolined {@link MonadRec}
     * @see Identity#trampolineM(Fn1) for a basic implementation
     * @see Maybe#trampolineM(Fn1) for a {@link CoProduct2 coproduct} implementation
     * @see Lazy#trampolineM(Fn1) for an implementation leveraging an already stack-safe {@link Monad#flatMap(Fn1)}
     * @see MaybeT#trampolineM(Fn1) for a {@link MonadT monad transformer} implementation
     */
    <B> MonadRec<B, M> trampolineM(Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, M>> fn);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadRec<B, M> flatMap(Fn1<? super A, ? extends Monad<B, M>> f);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadRec<B, M> pure(B b);

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadRec<B, M> fmap(Fn1<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadRec<B, M> zip(Applicative<Fn1<? super A, ? extends B>, M> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Lazy<? extends MonadRec<B, M>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, M>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Applicative<B, M>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadRec<B, M> discardL(Applicative<B, M> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadRec<A, M> discardR(Applicative<B, M> appB) {
        return Monad.super.discardR(appB).coerce();
    }
}
