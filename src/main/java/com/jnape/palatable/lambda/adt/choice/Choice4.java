package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct4;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;
import java.util.function.Function;

/**
 * Canonical ADT representation of {@link CoProduct4} that is also a {@link Functor} and {@link Bifunctor}.
 *
 * @param <A> a type parameter representing the first possible type of this choice
 * @param <B> a type parameter representing the second possible type of this choice
 * @param <C> a type parameter representing the third possible type of this choice
 * @param <D> a type parameter representing the fourth possible type of this choice
 * @see Choice3
 * @see Choice5
 */
public abstract class Choice4<A, B, C, D> implements CoProduct4<A, B, C, D>, Functor<D, Choice4<A, B, C, ?>>, Bifunctor<C, D, Choice4<A, B, ?, ?>> {

    private Choice4() {
    }

    @Override
    public <E> Choice5<A, B, C, D, E> diverge() {
        return match(Choice5::a, Choice5::b, Choice5::c, Choice5::d);
    }

    @Override
    public Choice3<A, B, C> converge(Function<? super D, ? extends CoProduct3<A, B, C>> convergenceFn) {
        return match(Choice3::a,
                     Choice3::b,
                     Choice3::c,
                     convergenceFn.andThen(cp3 -> cp3.match(Choice3::a, Choice3::b, Choice3::c)));
    }

    @Override
    public final <E> Choice4<A, B, C, E> fmap(Function<? super D, ? extends E> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <E> Choice4<A, B, E, D> biMapL(Function<? super C, ? extends E> fn) {
        return (Choice4<A, B, E, D>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <E> Choice4<A, B, C, E> biMapR(Function<? super D, ? extends E> fn) {
        return (Choice4<A, B, C, E>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public final <E, F> Choice4<A, B, E, F> biMap(Function<? super C, ? extends E> lFn,
                                                  Function<? super D, ? extends F> rFn) {
        return match(Choice4::a, Choice4::b, c -> c(lFn.apply(c)), d -> d(rFn.apply(d)));
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice4}.
     *
     * @param a   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @return the wrapped value as a Choice4&lt;A, B, C, D&gt;
     */
    public static <A, B, C, D> Choice4<A, B, C, D> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice4}.
     *
     * @param b   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @return the wrapped value as a Choice4&lt;A, B, C, D&gt;
     */
    public static <A, B, C, D> Choice4<A, B, C, D> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>C</code> in a {@link Choice4}.
     *
     * @param c   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @return the wrapped value as a Choice4&lt;A, B, C, D&gt;
     */
    public static <A, B, C, D> Choice4<A, B, C, D> c(C c) {
        return new _C<>(c);
    }

    /**
     * Static factory method for wrapping a value of type <code>D</code> in a {@link Choice4}.
     *
     * @param d   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @return the wrapped value as a Choice4&lt;A, B, C, D&gt;
     */
    public static <A, B, C, D> Choice4<A, B, C, D> d(D d) {
        return new _D<>(d);
    }

    private static final class _A<A, B, C, D> extends Choice4<A, B, C, D> {

        private final A a;

        private _A(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn) {
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
            return "Choice4{" +
                    "a=" + a +
                    '}';
        }
    }

    private static final class _B<A, B, C, D> extends Choice4<A, B, C, D> {

        private final B b;

        private _B(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn) {
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
            return "Choice4{" +
                    "b=" + b +
                    '}';
        }
    }

    private static final class _C<A, B, C, D> extends Choice4<A, B, C, D> {

        private final C c;

        private _C(C c) {
            this.c = c;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn) {
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
            return "Choice4{" +
                    "c=" + c +
                    '}';
        }
    }

    private static final class _D<A, B, C, D> extends Choice4<A, B, C, D> {

        private final D d;

        private _D(D d) {
            this.d = d;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn) {
            return dFn.apply(d);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof _D
                    && Objects.equals(d, ((_D) other).d);
        }

        @Override
        public int hashCode() {
            return Objects.hash(d);
        }

        @Override
        public String toString() {
            return "Choice4{" +
                    "d=" + d +
                    '}';
        }
    }
}
