package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

/**
 * A (surprisingly useful) functor over some phantom type <code>B</code>, retaining a value of type <code>A</code> that
 * can be retrieved later. This is useful in situations where it is desirable to retain constant information throughout
 * arbitrary functor transformations, such that at the end of the chain, regardless of how <code>B</code> has been
 * altered, <code>A</code> is still pristine and retrievable.
 *
 * @param <A> the left parameter type, and the type of the stored value
 * @param <B> the right (phantom) parameter type
 */
public final class Const<A, B> implements
        MonadRec<B, Const<A, ?>>,
        Bifunctor<A, B, Const<?, ?>>,
        Traversable<B, Const<A, ?>> {

    private final A a;

    public Const(A a) {
        this.a = a;
    }

    /**
     * Retrieve the stored value.
     *
     * @return the value
     */
    public A runConst() {
        return a;
    }

    /**
     * Map over the right parameter. Note that because <code>B</code> is never actually known quantity outside of a type
     * signature, this is effectively a no-op that serves only to alter <code>Const's</code> type signature.
     *
     * @param fn  the mapping function
     * @param <C> the new right parameter type
     * @return a Const over A (the same value) and C (the new phantom parameter)
     */
    @Override
    public <C> Const<A, C> fmap(Fn1<? super B, ? extends C> fn) {
        return MonadRec.super.<C>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> pure(C c) {
        return (Const<A, C>) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Const<A, C> zip(Applicative<Fn1<? super B, ? extends C>, Const<A, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Lazy<Const<A, C>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super B, ? extends C>, Const<A, ?>>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(Monad<C, Const<A, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Const<A, C> discardL(Applicative<C, Const<A, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Const<A, B> discardR(Applicative<C, Const<A, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> flatMap(Fn1<? super B, ? extends Monad<C, Const<A, ?>>> f) {
        return (Const<A, C>) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> trampolineM(Fn1<? super B, ? extends MonadRec<RecursiveResult<B, C>, Const<A, ?>>> fn) {
        return (Const<A, C>) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C, App extends Applicative<?, App>, TravB extends Traversable<C, Const<A, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super B, ? extends Applicative<C, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return pure.apply(coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Z> Const<Z, B> biMapL(Fn1<? super A, ? extends Z> fn) {
        return (Const<Z, B>) Bifunctor.super.<Z>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> biMapR(Fn1<? super B, ? extends C> fn) {
        return (Const<A, C>) Bifunctor.super.biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C, D> Const<C, D> biMap(Fn1<? super A, ? extends C> lFn,
                                    Fn1<? super B, ? extends D> rFn) {
        return new Const<>(lFn.apply(a));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Const && Objects.equals(a, ((Const) other).a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public String toString() {
        return "Const{" +
                "a=" + a +
                '}';
    }

    /**
     * The canonical {@link Pure} instance for {@link Const}.
     *
     * @param a   the stored value
     * @param <A> the left parameter type, and the type of the stored value
     * @return the {@link Pure} instance
     */
    public static <A> Pure<Const<A, ?>> pureConst(A a) {
        return new Pure<Const<A, ?>>() {
            @Override
            public <B> Const<A, B> checkedApply(B b) {
                return new Const<>(a);
            }
        };
    }
}
