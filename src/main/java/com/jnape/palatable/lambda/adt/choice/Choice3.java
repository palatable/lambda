package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;
import java.util.function.Function;

/**
 * Canonical ADT representation of {@link CoProduct3} that is also a {@link Functor} and {@link Bifunctor}.
 *
 * @param <A> a type parameter representing the first possible type of this choice
 * @param <B> a type parameter representing the second possible type of this choice
 * @param <C> a type parameter representing the third possible type of this choice
 * @see Choice2
 * @see Choice4
 */
public abstract class Choice3<A, B, C> implements CoProduct3<A, B, C>, Functor<C, Choice3<A, B, ?>>, Bifunctor<B, C, Choice3<A, ?, ?>> {

    private Choice3() {
    }

    @Override
    public final <D> Choice4<A, B, C, D> diverge() {
        return match(Choice4::a, Choice4::b, Choice4::c);
    }

    @Override
    public final Choice2<A, B> converge(Function<? super C, ? extends CoProduct2<A, B>> convergenceFn) {
        return match(Choice2::a, Choice2::b, convergenceFn.andThen(cp2 -> cp2.match(Choice2::a, Choice2::b)));
    }

    @Override
    public final <D> Choice3<A, B, D> fmap(Function<? super C, ? extends D> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <D> Choice3<A, D, C> biMapL(Function<? super B, ? extends D> fn) {
        return (Choice3<A, D, C>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <D> Choice3<A, B, D> biMapR(Function<? super C, ? extends D> fn) {
        return (Choice3<A, B, D>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public final <D, E> Choice3<A, D, E> biMap(Function<? super B, ? extends D> lFn,
                                               Function<? super C, ? extends E> rFn) {
        return match(Choice3::a, b -> b(lFn.apply(b)), c -> c(rFn.apply(c)));
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice3}.
     *
     * @param a   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @return the wrapped value as a Choice3&lt;A, B, C&gt;
     */
    public static <A, B, C> Choice3<A, B, C> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice3}.
     *
     * @param b   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @return the wrapped value as a Choice3&lt;A, B, C&gt;
     */
    public static <A, B, C> Choice3<A, B, C> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice3}.
     *
     * @param c   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @return the wrapped value as a Choice3&lt;A, B, C&gt;
     */
    public static <A, B, C> Choice3<A, B, C> c(C c) {
        return new _C<>(c);
    }

    private static final class _A<A, B, C> extends Choice3<A, B, C> {

        private final A a;

        private _A(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn) {
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
            return "Choice3{" +
                    "a=" + a +
                    '}';
        }
    }

    private static final class _B<A, B, C> extends Choice3<A, B, C> {

        private final B b;

        private _B(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn) {
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
            return "Choice3{" +
                    "b=" + b +
                    '}';
        }
    }

    private static final class _C<A, B, C> extends Choice3<A, B, C> {

        private final C c;

        private _C(C c) {
            this.c = c;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn) {
            return cFn.apply(c);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof _C
                    && Objects.equals(c, ((_C) other).c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(c);
        }

        @Override
        public String toString() {
            return "Choice3{" +
                    "c=" + c +
                    '}';
        }
    }


}
