package com.jnape.palatable.lambda.functions.recursion;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Sequence.sequence;

/**
 * Specialized {@link CoProduct2} representing the possible results of a primitive recursive function.
 * Used by {@link Trampoline} to cheat around {@link CoProduct2#match} and quickly unpack values via
 * <code>instanceof</code> checks to package private inner subtypes.
 *
 * @param <A> the recursive function's input type
 * @param <B> the recursive function's output type
 * @see Trampoline
 */
public abstract class RecursiveResult<A, B> implements
        CoProduct2<A, B, RecursiveResult<A, B>>,
        Bifunctor<A, B, RecursiveResult<?, ?>>,
        MonadRec<B, RecursiveResult<A, ?>>,
        Traversable<B, RecursiveResult<A, ?>> {

    private RecursiveResult() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecursiveResult<B, A> invert() {
        return match(RecursiveResult::terminate, RecursiveResult::recurse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<C, B> biMapL(Fn1<? super A, ? extends C> fn) {
        return (RecursiveResult<C, B>) Bifunctor.super.<C>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<A, C> biMapR(Fn1<? super B, ? extends C> fn) {
        return (RecursiveResult<A, C>) Bifunctor.super.<C>biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C, D> RecursiveResult<C, D> biMap(Fn1<? super A, ? extends C> lFn,
                                              Fn1<? super B, ? extends D> rFn) {
        return match(a -> recurse(lFn.apply(a)), b -> terminate(rFn.apply(b)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<A, C> flatMap(Fn1<? super B, ? extends Monad<C, RecursiveResult<A, ?>>> f) {
        return match(RecursiveResult::recurse, b -> f.apply(b).coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<A, C> pure(C c) {
        return terminate(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<A, C> fmap(Fn1<? super B, ? extends C> fn) {
        return MonadRec.super.<C>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<A, C> zip(Applicative<Fn1<? super B, ? extends C>, RecursiveResult<A, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<A, C> discardL(Applicative<C, RecursiveResult<A, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<A, B> discardR(Applicative<C, RecursiveResult<A, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> RecursiveResult<A, C> trampolineM(
            Fn1<? super B, ? extends MonadRec<RecursiveResult<B, C>, RecursiveResult<A, ?>>> fn) {
        return flatMap(Trampoline.<B, RecursiveResult<A, C>>trampoline(
                b -> sequence(fn.apply(b).<RecursiveResult<A, RecursiveResult<B, C>>>coerce(),
                              RecursiveResult::terminate)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C, App extends Applicative<?, App>, TravB extends Traversable<C, RecursiveResult<A, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super B, ? extends Applicative<C, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return match(__ -> pure.apply(coerce()),
                     b -> fn.apply(b).fmap(this::pure).<TravB>fmap(RecursiveResult::coerce).coerce());
    }

    /**
     * Static factory method for creating a "recurse" value.
     *
     * @param a   the value
     * @param <A> the recurse type
     * @param <B> the terminate type
     * @return the {@link RecursiveResult}
     */
    public static <A, B> RecursiveResult<A, B> recurse(A a) {
        return new Recurse<>(a);
    }

    /**
     * Static factory method for creating a "terminate" value.
     *
     * @param b   the value
     * @param <A> the recurse type
     * @param <B> the terminate type
     * @return the {@link RecursiveResult}
     */
    public static <A, B> RecursiveResult<A, B> terminate(B b) {
        return new Terminate<>(b);
    }

    /**
     * The canonical {@link Pure} instance for {@link RecursiveResult}.
     *
     * @param <A> the recursive function's input type
     * @return the {@link Pure} instance
     */
    public static <A> Pure<RecursiveResult<A, ?>> pureRecursiveResult() {
        return RecursiveResult::terminate;
    }

    static final class Recurse<A, B> extends RecursiveResult<A, B> {
        final A a;

        private Recurse(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn) {
            return aFn.apply(a);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Recurse && Objects.equals(a, ((Recurse) other).a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }

        @Override
        public String toString() {
            return "Recurse{" +
                    "a=" + a +
                    '}';
        }
    }

    static final class Terminate<A, B> extends RecursiveResult<A, B> {
        final B b;

        private Terminate(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn) {
            return bFn.apply(b);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Terminate && Objects.equals(b, ((Terminate) other).b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(b);
        }

        @Override
        public String toString() {
            return "Terminate{" +
                    "b=" + b +
                    '}';
        }
    }
}
