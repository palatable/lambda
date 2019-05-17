package com.jnape.palatable.lambda.functions.recursion;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

/**
 * Specialized {@link CoProduct2} representing the possible results of a primitive recursive function.
 * Used by {@link Trampoline} to cheat around {@link CoProduct2#match} and quickly unpack values via
 * <code>instanceof</code> checks to package private inner subtypes.
 *
 * @param <A> the recursive function's input type
 * @param <B> the recursive function's output type
 * @see Trampoline
 */
public abstract class RecursiveResult<A, B> implements CoProduct2<A, B, RecursiveResult<A, B>>, Bifunctor<A, B, RecursiveResult<?, ?>>, Monad<B, RecursiveResult<A, ?>>, Traversable<B, RecursiveResult<A, ?>> {

    private RecursiveResult() {
    }

    @Override
    public RecursiveResult<B, A> invert() {
        return match(RecursiveResult::terminate, RecursiveResult::recurse);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> RecursiveResult<C, B> biMapL(Fn1<? super A, ? extends C> fn) {
        return (RecursiveResult<C, B>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> RecursiveResult<A, C> biMapR(Fn1<? super B, ? extends C> fn) {
        return (RecursiveResult<A, C>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <C, D> RecursiveResult<C, D> biMap(Fn1<? super A, ? extends C> lFn,
                                              Fn1<? super B, ? extends D> rFn) {
        return match(a -> recurse(lFn.apply(a)), b -> terminate(rFn.apply(b)));
    }

    @Override
    public <C> RecursiveResult<A, C> flatMap(Fn1<? super B, ? extends Monad<C, RecursiveResult<A, ?>>> f) {
        return match(RecursiveResult::recurse, b -> f.apply(b).coerce());
    }

    @Override
    public <C> RecursiveResult<A, C> pure(C c) {
        return terminate(c);
    }

    @Override
    public <C> RecursiveResult<A, C> fmap(Fn1<? super B, ? extends C> fn) {
        return Monad.super.<C>fmap(fn).coerce();
    }

    @Override
    public <C> RecursiveResult<A, C> zip(Applicative<Fn1<? super B, ? extends C>, RecursiveResult<A, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <C> RecursiveResult<A, C> discardL(Applicative<C, RecursiveResult<A, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <C> RecursiveResult<A, B> discardR(Applicative<C, RecursiveResult<A, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <C, App extends Applicative<?, App>, TravB extends Traversable<C, RecursiveResult<A, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super B, ? extends Applicative<C, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return match(__ -> pure.apply(coerce()),
                     b -> fn.apply(b).fmap(this::pure).<TravB>fmap(RecursiveResult::coerce).coerce());
    }

    public static <A, B> RecursiveResult<A, B> recurse(A a) {
        return new Recurse<>(a);
    }

    public static <A, B> RecursiveResult<A, B> terminate(B b) {
        return new Terminate<>(b);
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
