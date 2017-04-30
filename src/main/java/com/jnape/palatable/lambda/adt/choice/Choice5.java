package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct4;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct5;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;
import java.util.function.Function;

/**
 * Canonical ADT representation of {@link CoProduct5} that is also a {@link Functor} and {@link Bifunctor}.
 *
 * @param <A> a type parameter representing the first possible type of this choice
 * @param <B> a type parameter representing the second possible type of this choice
 * @param <C> a type parameter representing the third possible type of this choice
 * @param <D> a type parameter representing the fourth possible type of this choice
 * @param <E> a type parameter representing the fifth possible type of this choice
 * @see Choice4
 */
public abstract class Choice5<A, B, C, D, E> implements CoProduct5<A, B, C, D, E, Choice5<A, B, C, D, E>>, Applicative<E, Choice5<A, B, C, D, ?>>, Bifunctor<D, E, Choice5<A, B, C, ?, ?>> {

    private Choice5() {
    }

    @Override
    public Choice4<A, B, C, D> converge(Function<? super E, ? extends CoProduct4<A, B, C, D, ?>> convergenceFn) {
        return match(Choice4::a,
                     Choice4::b,
                     Choice4::c,
                     Choice4::d,
                     convergenceFn.andThen(cp4 -> cp4.match(Choice4::a, Choice4::b, Choice4::c, Choice4::d)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> Choice5<A, B, C, D, F> fmap(Function<? super E, ? extends F> fn) {
        return (Choice5<A, B, C, D, F>) Applicative.super.fmap(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> Choice5<A, B, C, F, E> biMapL(Function<? super D, ? extends F> fn) {
        return (Choice5<A, B, C, F, E>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> Choice5<A, B, C, D, F> biMapR(Function<? super E, ? extends F> fn) {
        return (Choice5<A, B, C, D, F>) Bifunctor.super.biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F, G> Choice5<A, B, C, F, G> biMap(Function<? super D, ? extends F> lFn,
                                               Function<? super E, ? extends G> rFn) {
        return match(Choice5::a, Choice5::b, Choice5::c, d -> d(lFn.apply(d)), e -> e(rFn.apply(e)));
    }

    @Override
    public <F> Choice5<A, B, C, D, F> pure(F f) {
        return e(f);
    }

    @Override
    public <F> Choice5<A, B, C, D, F> zip(Applicative<Function<? super E, ? extends F>, Choice5<A, B, C, D, ?>> appFn) {
        return appFn.<Choice5<A, B, C, D, Function<? super E, ? extends F>>>coerce()
                .match(Choice5::a, Choice5::b, Choice5::c, Choice5::d, this::biMapR);
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice5}.
     *
     * @param a   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @param <E> a type parameter representing the fifth possible type of this choice
     * @return the wrapped value as a Choice5&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice5}.
     *
     * @param b   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @param <E> a type parameter representing the fifth possible type of this choice
     * @return the wrapped value as a Choice5&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>C</code> in a {@link Choice5}.
     *
     * @param c   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @param <E> a type parameter representing the fifth possible type of this choice
     * @return the wrapped value as a Choice5&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> c(C c) {
        return new _C<>(c);
    }

    /**
     * Static factory method for wrapping a value of type <code>D</code> in a {@link Choice5}.
     *
     * @param d   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @param <E> a type parameter representing the fifth possible type of this choice
     * @return the wrapped value as a Choice5&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> d(D d) {
        return new _D<>(d);
    }

    /**
     * Static factory method for wrapping a value of type <code>E</code> in a {@link Choice5}.
     *
     * @param e   the value
     * @param <A> a type parameter representing the first possible type of this choice
     * @param <B> a type parameter representing the second possible type of this choice
     * @param <C> a type parameter representing the third possible type of this choice
     * @param <D> a type parameter representing the fourth possible type of this choice
     * @param <E> a type parameter representing the fifth possible type of this choice
     * @return the wrapped value as a Choice5&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> e(E e) {
        return new _E<>(e);
    }

    private static final class _A<A, B, C, D, E> extends Choice5<A, B, C, D, E> {

        private final A a;

        private _A(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn) {
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
            return "Choice5{" +
                    "a=" + a +
                    '}';
        }
    }

    private static final class _B<A, B, C, D, E> extends Choice5<A, B, C, D, E> {

        private final B b;

        private _B(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn) {
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
            return "Choice5{" +
                    "b=" + b +
                    '}';
        }
    }

    private static final class _C<A, B, C, D, E> extends Choice5<A, B, C, D, E> {

        private final C c;

        private _C(C c) {
            this.c = c;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn) {
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
            return "Choice5{" +
                    "c=" + c +
                    '}';
        }
    }

    private static final class _D<A, B, C, D, E> extends Choice5<A, B, C, D, E> {

        private final D d;

        private _D(D d) {
            this.d = d;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn) {
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
            return "Choice5{" +
                    "d=" + d +
                    '}';
        }
    }

    private static final class _E<A, B, C, D, E> extends Choice5<A, B, C, D, E> {

        private final E e;

        private _E(E e) {
            this.e = e;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn) {
            return eFn.apply(e);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof _E
                    && Objects.equals(e, ((_E) other).e);
        }

        @Override
        public int hashCode() {
            return Objects.hash(e);
        }

        @Override
        public String toString() {
            return "Choice5{" +
                    "e=" + e +
                    '}';
        }
    }
}
