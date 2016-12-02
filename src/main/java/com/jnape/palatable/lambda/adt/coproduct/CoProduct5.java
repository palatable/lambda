package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

/**
 * A generalization of the coproduct of five types <code>A</code>, <code>B</code>, <code>C</code>, <code>D</code>, and
 * <code>E</code>.
 *
 * @param <A> a type parameter representing the first possible type of this coproduct
 * @param <B> a type parameter representing the second possible type of this coproduct
 * @param <C> a type parameter representing the third possible type of this coproduct
 * @param <D> a type parameter representing the fourth possible type of this coproduct
 * @param <E> a type parameter representing the fifth possible type of this coproduct
 * @see CoProduct2
 */
@FunctionalInterface
public interface CoProduct5<A, B, C, D, E> extends Functor<E>, Bifunctor<D, E> {

    /**
     * Type-safe convergence requiring a match against all potential types.
     *
     * @param aFn morphism <code>A -&gt; R</code>
     * @param bFn morphism <code>B -&gt; R</code>
     * @param cFn morphism <code>C -&gt; R</code>
     * @param dFn morphism <code>D -&gt; R</code>
     * @param eFn morphism <code>E -&gt; R</code>
     * @param <R> result type
     * @return the result of applying the appropriate morphism from whichever type is represented by this coproduct to R
     * @see CoProduct2#match(Function, Function)
     */
    <R> R match(Function<? super A, ? extends R> aFn,
                Function<? super B, ? extends R> bFn,
                Function<? super C, ? extends R> cFn,
                Function<? super D, ? extends R> dFn,
                Function<? super E, ? extends R> eFn);

    /**
     * Project this coproduct onto a tuple.
     *
     * @return a tuple of the coproduct projection
     * @see CoProduct2#project()
     */
    default Tuple5<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>> project() {
        return match(a -> tuple(Optional.of(a), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
                     b -> tuple(Optional.empty(), Optional.of(b), Optional.empty(), Optional.empty(), Optional.empty()),
                     c -> tuple(Optional.empty(), Optional.empty(), Optional.of(c), Optional.empty(), Optional.empty()),
                     d -> tuple(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(d), Optional.empty()),
                     e -> tuple(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(e)));
    }

    @Override
    default <F> CoProduct5<A, B, C, D, F> fmap(Function<? super E, ? extends F> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <F> CoProduct5<A, B, C, F, E> biMapL(Function<? super D, ? extends F> fn) {
        return (CoProduct5<A, B, C, F, E>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <F> CoProduct5<A, B, C, D, F> biMapR(Function<? super E, ? extends F> fn) {
        return (CoProduct5<A, B, C, D, F>) Bifunctor.super.biMapR(fn);
    }

    @Override
    default <F, G> CoProduct5<A, B, C, F, G> biMap(Function<? super D, ? extends F> lFn,
                                                   Function<? super E, ? extends G> rFn) {
        return match(CoProduct5::a, CoProduct5::b, CoProduct5::c, d -> d(lFn.apply(d)), e -> e(rFn.apply(e)));
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link CoProduct5}.
     *
     * @param a   the value
     * @param <A> a type parameter representing the first possible type of this coproduct
     * @param <B> a type parameter representing the second possible type of this coproduct
     * @param <C> a type parameter representing the third possible type of this coproduct
     * @param <D> a type parameter representing the fourth possible type of this coproduct
     * @param <E> a type parameter representing the fifth possible type of this coproduct
     * @return the wrapped value as a CoProduct5&lt;A, B, C, D, E&gt;
     */
    static <A, B, C, D, E> CoProduct5<A, B, C, D, E> a(A a) {
        class _A implements CoProduct5<A, B, C, D, E> {

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
                return "CoProduct5{" +
                        "a=" + a +
                        '}';
            }
        }

        return new _A(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link CoProduct5}.
     *
     * @param b   the value
     * @param <A> a type parameter representing the first possible type of this coproduct
     * @param <B> a type parameter representing the second possible type of this coproduct
     * @param <C> a type parameter representing the third possible type of this coproduct
     * @param <D> a type parameter representing the fourth possible type of this coproduct
     * @param <E> a type parameter representing the fifth possible type of this coproduct
     * @return the wrapped value as a CoProduct5&lt;A, B, C, D, E&gt;
     */
    static <A, B, C, D, E> CoProduct5<A, B, C, D, E> b(B b) {
        class _B implements CoProduct5<A, B, C, D, E> {

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
                return "CoProduct5{" +
                        "b=" + b +
                        '}';
            }
        }

        return new _B(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>C</code> in a {@link CoProduct5}.
     *
     * @param c   the value
     * @param <A> a type parameter representing the first possible type of this coproduct
     * @param <B> a type parameter representing the second possible type of this coproduct
     * @param <C> a type parameter representing the third possible type of this coproduct
     * @param <D> a type parameter representing the fourth possible type of this coproduct
     * @param <E> a type parameter representing the fifth possible type of this coproduct
     * @return the wrapped value as a CoProduct5&lt;A, B, C, D, E&gt;
     */
    static <A, B, C, D, E> CoProduct5<A, B, C, D, E> c(C c) {
        class _C implements CoProduct5<A, B, C, D, E> {

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
                return "CoProduct5{" +
                        "c=" + c +
                        '}';
            }
        }

        return new _C(c);
    }

    /**
     * Static factory method for wrapping a value of type <code>D</code> in a {@link CoProduct5}.
     *
     * @param d   the value
     * @param <A> a type parameter representing the first possible type of this coproduct
     * @param <B> a type parameter representing the second possible type of this coproduct
     * @param <C> a type parameter representing the third possible type of this coproduct
     * @param <D> a type parameter representing the fourth possible type of this coproduct
     * @param <E> a type parameter representing the fifth possible type of this coproduct
     * @return the wrapped value as a CoProduct5&lt;A, B, C, D, E&gt;
     */
    static <A, B, C, D, E> CoProduct5<A, B, C, D, E> d(D d) {
        class _D implements CoProduct5<A, B, C, D, E> {

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
                return "CoProduct5{" +
                        "d=" + d +
                        '}';
            }
        }

        return new _D(d);
    }

    /**
     * Static factory method for wrapping a value of type <code>E</code> in a {@link CoProduct5}.
     *
     * @param e   the value
     * @param <A> a type parameter representing the first possible type of this coproduct
     * @param <B> a type parameter representing the second possible type of this coproduct
     * @param <C> a type parameter representing the third possible type of this coproduct
     * @param <D> a type parameter representing the fourth possible type of this coproduct
     * @param <E> a type parameter representing the fifth possible type of this coproduct
     * @return the wrapped value as a CoProduct5&lt;A, B, C, D, E&gt;
     */
    static <A, B, C, D, E> CoProduct5<A, B, C, D, E> e(E e) {
        class _E implements CoProduct5<A, B, C, D, E> {

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
                return "CoProduct5{" +
                        "e=" + e +
                        '}';
            }
        }

        return new _E(e);
    }
}
