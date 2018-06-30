package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct5;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct6;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Apply;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Bind;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into6.into6;

/**
 * Canonical ADT representation of {@link CoProduct6}.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @param <C> the third possible type
 * @param <D> the fourth possible type
 * @param <E> the fifth possible type
 * @param <F> the sixth possible type
 * @see Choice5
 * @see Choice7
 */
public abstract class Choice6<A, B, C, D, E, F> implements
        CoProduct6<A, B, C, D, E, F, Choice6<A, B, C, D, E, F>>,
        Monad<F, Choice6<A, B, C, D, E, ?>>,
        Bifunctor<E, F, Choice6<A, B, C, D, ?, ?>>,
        Traversable<F, Choice6<A, B, C, D, E, ?>> {

    private Choice6() {
    }

    /**
     * Specialize this choice's projection to a {@link Tuple6}.
     *
     * @return a {@link Tuple6}
     */
    @Override
    public Tuple6<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>, Maybe<E>, Maybe<F>> project() {
        return into6(HList::tuple, CoProduct6.super.project());
    }

    @Override
    public <G> Choice7<A, B, C, D, E, F, G> diverge() {
        return match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, Choice7::f);
    }

    @Override
    public Choice5<A, B, C, D, E> converge(Function<? super F, ? extends CoProduct5<A, B, C, D, E, ?>> convergenceFn) {
        return match(Choice5::a,
                     Choice5::b,
                     Choice5::c,
                     Choice5::d,
                     Choice5::e,
                     convergenceFn.andThen(cp5 -> cp5.match(Choice5::a, Choice5::b, Choice5::c, Choice5::d, Choice5::e)));
    }

    @Override
    public <G> Choice6<A, B, C, D, E, G> fmap(Function<? super F, ? extends G> fn) {
        return Monad.super.<G>fmap(fn).coerce();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <G> Choice6<A, B, C, D, G, F> biMapL(Function<? super E, ? extends G> fn) {
        return (Choice6<A, B, C, D, G, F>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <G> Choice6<A, B, C, D, E, G> biMapR(Function<? super F, ? extends G> fn) {
        return (Choice6<A, B, C, D, E, G>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <G, H> Choice6<A, B, C, D, G, H> biMap(Function<? super E, ? extends G> lFn,
                                                  Function<? super F, ? extends H> rFn) {
        return match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, e -> e(lFn.apply(e)), f -> f(rFn.apply(f)));
    }

    @Override
    public <G> Choice6<A, B, C, D, E, G> pure(G g) {
        return f(g);
    }

    @Override
    public <G> Choice6<A, B, C, D, E, G> zip(
            Apply<Function<? super F, ? extends G>, Choice6<A, B, C, D, E, ?>> appFn) {
        return appFn.<Choice6<A, B, C, D, E, Function<? super F, ? extends G>>>coerce()
                .match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e, this::biMapR);
    }

    @Override
    public <G> Choice6<A, B, C, D, E, G> discardL(Applicative<G, Choice6<A, B, C, D, E, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <G> Choice6<A, B, C, D, E, F> discardR(Applicative<G, Choice6<A, B, C, D, E, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <G> Choice6<A, B, C, D, E, G> flatMap(
            Function<? super F, ? extends Bind<G, Choice6<A, B, C, D, E, ?>>> fn) {
        return match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e, f -> fn.apply(f).coerce());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <G, App extends Applicative, TravB extends Traversable<G, Choice6<A, B, C, D, E, ?>>, AppB extends Applicative<G, App>, AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Function<? super F, ? extends AppB> fn, Function<? super TravB, ? extends AppTrav> pure) {
        return match(a -> pure.apply((TravB) Choice6.<A, B, C, D, E, G>a(a)).coerce(),
                     b -> pure.apply((TravB) Choice6.<A, B, C, D, E, G>b(b)).coerce(),
                     c -> pure.apply((TravB) Choice6.<A, B, C, D, E, G>c(c)),
                     d -> pure.apply((TravB) Choice6.<A, B, C, D, E, G>d(d)),
                     e -> pure.apply((TravB) Choice6.<A, B, C, D, E, G>e(e)),
                     f -> fn.apply(f).fmap(Choice6::f).<TravB>fmap(Applicative::coerce).coerce());
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice6}.
     *
     * @param a   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @return the wrapped value as a {@link Choice6}&lt;A, B, C, D, E, F&gt;
     */
    public static <A, B, C, D, E, F> Choice6<A, B, C, D, E, F> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice6}.
     *
     * @param b   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @return the wrapped value as a {@link Choice6}&lt;A, B, C, D, E, F&gt;
     */
    public static <A, B, C, D, E, F> Choice6<A, B, C, D, E, F> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>C</code> in a {@link Choice6}.
     *
     * @param c   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @return the wrapped value as a {@link Choice6}&lt;A, B, C, D, E, F&gt;
     */
    public static <A, B, C, D, E, F> Choice6<A, B, C, D, E, F> c(C c) {
        return new _C<>(c);
    }

    /**
     * Static factory method for wrapping a value of type <code>D</code> in a {@link Choice6}.
     *
     * @param d   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @return the wrapped value as a {@link Choice6}&lt;A, B, C, D, E, F&gt;
     */
    public static <A, B, C, D, E, F> Choice6<A, B, C, D, E, F> d(D d) {
        return new _D<>(d);
    }

    /**
     * Static factory method for wrapping a value of type <code>E</code> in a {@link Choice6}.
     *
     * @param e   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @return the wrapped value as a {@link Choice6}&lt;A, B, C, D, E, F&gt;
     */
    public static <A, B, C, D, E, F> Choice6<A, B, C, D, E, F> e(E e) {
        return new _E<>(e);
    }

    /**
     * Static factory method for wrapping a value of type <code>F</code> in a {@link Choice6}.
     *
     * @param f   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @return the wrapped value as a {@link Choice6}&lt;A, B, C, D, E, F&gt;
     */
    public static <A, B, C, D, E, F> Choice6<A, B, C, D, E, F> f(F f) {
        return new _F<>(f);
    }

    private static final class _A<A, B, C, D, E, F> extends Choice6<A, B, C, D, E, F> {

        private final A a;

        private _A(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn) {
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
            return "Choice6{a=" + a + '}';
        }
    }

    private static final class _B<A, B, C, D, E, F> extends Choice6<A, B, C, D, E, F> {

        private final B b;

        private _B(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn) {
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
            return "Choice6{b=" + b + '}';
        }
    }

    private static final class _C<A, B, C, D, E, F> extends Choice6<A, B, C, D, E, F> {

        private final C c;

        private _C(C c) {
            this.c = c;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn) {
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
            return "Choice6{c=" + c + '}';
        }
    }

    private static final class _D<A, B, C, D, E, F> extends Choice6<A, B, C, D, E, F> {

        private final D d;

        private _D(D d) {
            this.d = d;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn) {
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
            return "Choice6{d=" + d + '}';
        }
    }

    private static final class _E<A, B, C, D, E, F> extends Choice6<A, B, C, D, E, F> {

        private final E e;

        private _E(E e) {
            this.e = e;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn) {
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
            return "Choice6{e=" + e + '}';
        }
    }

    private static final class _F<A, B, C, D, E, F> extends Choice6<A, B, C, D, E, F> {

        private final F f;

        private _F(F f) {
            this.f = f;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn) {
            return fFn.apply(f);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof _F
                    && Objects.equals(f, ((_F) other).f);
        }

        @Override
        public int hashCode() {
            return Objects.hash(f);
        }

        @Override
        public String toString() {
            return "Choice6{f=" + f + '}';
        }
    }
}
