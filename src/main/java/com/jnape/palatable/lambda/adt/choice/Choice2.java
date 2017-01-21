package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;
import java.util.function.Function;

/**
 * Canonical ADT representation of {@link CoProduct2} that is also a {@link Functor} and {@link Bifunctor}. Unlike
 * {@link Either}, there is no concept of "success" or "failure", so the domain of reasonable function semantics is
 * more limited.
 *
 * @param <A> a type parameter representing the first possible type of this choice
 * @param <B> a type parameter representing the second possible type of this choice
 * @see Either
 * @see Choice3
 */
public abstract class Choice2<A, B> implements CoProduct2<A, B>, Functor<B, Choice2<A, ?>>, Bifunctor<A, B> {

    private Choice2() {
    }

    @Override
    public final <C> Choice3<A, B, C> diverge() {
        return match(Choice3::a, Choice3::b);
    }

    @Override
    public final <C> Choice2<A, C> fmap(Function<? super B, ? extends C> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <C> Choice2<C, B> biMapL(Function<? super A, ? extends C> fn) {
        return (Choice2<C, B>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <C> Choice2<A, C> biMapR(Function<? super B, ? extends C> fn) {
        return (Choice2<A, C>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public final <C, D> Choice2<C, D> biMap(Function<? super A, ? extends C> lFn,
                                            Function<? super B, ? extends D> rFn) {
        return match(a -> a(lFn.apply(a)), b -> b(rFn.apply(b)));
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice2}.
     *
     * @param a   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @return the wrapped value as a Choice2&lt;A, B&gt;
     */
    public static <A, B> Choice2<A, B> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice2}.
     *
     * @param b   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @return the wrapped value as a Choice2&lt;A, B&gt;
     */
    public static <A, B> Choice2<A, B> b(B b) {
        return new _B<>(b);
    }

    private static final class _A<A, B> extends Choice2<A, B> {

        private final A a;

        private _A(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn) {
            return aFn.apply(a);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof _A
                    && Objects.equals(a, ((_A) other).a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }

        @Override
        public String toString() {
            return "Choice2{" +
                    "a=" + a +
                    '}';
        }
    }

    private static final class _B<A, B> extends Choice2<A, B> {

        private final B b;

        private _B(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn) {
            return bFn.apply(b);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof _B
                    && Objects.equals(b, ((_B) other).b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(b);
        }

        @Override
        public String toString() {
            return "Choice2{" +
                    "b=" + b +
                    '}';
        }
    }
}
