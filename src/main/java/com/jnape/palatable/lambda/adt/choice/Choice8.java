package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct7;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct8;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into8.into8;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * Canonical ADT representation of {@link CoProduct8}.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @param <C> the third possible type
 * @param <D> the fourth possible type
 * @param <E> the fifth possible type
 * @param <F> the sixth possible type
 * @param <G> the seventh possible type
 * @param <H> the eighth possible type
 * @see Choice7
 */
public abstract class Choice8<A, B, C, D, E, F, G, H> implements
        CoProduct8<A, B, C, D, E, F, G, H, Choice8<A, B, C, D, E, F, G, H>>,
        Monad<H, Choice8<A, B, C, D, E, F, G, ?>>,
        Bifunctor<G, H, Choice8<A, B, C, D, E, F, ?, ?>>,
        Traversable<H, Choice8<A, B, C, D, E, F, G, ?>> {

    private Choice8() {
    }

    /**
     * Specialize this choice's projection to a {@link Tuple8}.
     *
     * @return a {@link Tuple8}
     */
    @Override
    public Tuple8<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>, Maybe<E>, Maybe<F>, Maybe<G>, Maybe<H>> project() {
        return into8(HList::tuple, CoProduct8.super.project());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Choice7<A, B, C, D, E, F, G> converge(
            Function<? super H, ? extends CoProduct7<A, B, C, D, E, F, G, ?>> convergenceFn) {
        return match(Choice7::a,
                     Choice7::b,
                     Choice7::c,
                     Choice7::d,
                     Choice7::e,
                     Choice7::f,
                     Choice7::g,
                     convergenceFn.andThen(cp7 -> cp7.match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, Choice7::f, Choice7::g)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <I> Choice8<A, B, C, D, E, F, G, I> fmap(Function<? super H, ? extends I> fn) {
        return Monad.super.<I>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <I> Choice8<A, B, C, D, E, F, I, H> biMapL(Function<? super G, ? extends I> fn) {
        return (Choice8<A, B, C, D, E, F, I, H>) Bifunctor.super.biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <I> Choice8<A, B, C, D, E, F, G, I> biMapR(Function<? super H, ? extends I> fn) {
        return (Choice8<A, B, C, D, E, F, G, I>) Bifunctor.super.biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <I, J> Choice8<A, B, C, D, E, F, I, J> biMap(Function<? super G, ? extends I> lFn,
                                                        Function<? super H, ? extends J> rFn) {
        return match(Choice8::a, Choice8::b, Choice8::c, Choice8::d, Choice8::e, Choice8::f, g -> g(lFn.apply(g)), h -> h(rFn.apply(h)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <I> Choice8<A, B, C, D, E, F, G, I> pure(I i) {
        return h(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <I> Choice8<A, B, C, D, E, F, G, I> zip(
            Applicative<Function<? super H, ? extends I>, Choice8<A, B, C, D, E, F, G, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     * @param lazyAppFn
     */
    @Override
    public <I> Lazy<Choice8<A, B, C, D, E, F, G, I>> lazyZip(
            Lazy<? extends Applicative<Function<? super H, ? extends I>, Choice8<A, B, C, D, E, F, G, ?>>> lazyAppFn) {
        return match(a -> lazy(a(a)),
                     b -> lazy(b(b)),
                     c -> lazy(c(c)),
                     d -> lazy(d(d)),
                     e -> lazy(e(e)),
                     f -> lazy(f(f)),
                     g -> lazy(g(g)),
                     h -> lazyAppFn.fmap(choiceF -> choiceF.<I>fmap(f -> f.apply(h)).coerce()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <I> Choice8<A, B, C, D, E, F, G, I> discardL(Applicative<I, Choice8<A, B, C, D, E, F, G, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <I> Choice8<A, B, C, D, E, F, G, H> discardR(Applicative<I, Choice8<A, B, C, D, E, F, G, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <I> Choice8<A, B, C, D, E, F, G, I> flatMap(
            Function<? super H, ? extends Monad<I, Choice8<A, B, C, D, E, F, G, ?>>> fn) {
        return match(Choice8::a, Choice8::b, Choice8::c, Choice8::d, Choice8::e, Choice8::f, Choice8::g, h -> fn.apply(h).coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <I, App extends Applicative<?, App>, TravB extends Traversable<I, Choice8<A, B, C, D, E, F, G, ?>>,
            AppB extends Applicative<I, App>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Function<? super H, ? extends AppB> fn,
                                                                      Function<? super TravB, ? extends AppTrav> pure) {
        return match(a -> pure.apply((TravB) Choice8.<A, B, C, D, E, F, G, I>a(a)).coerce(),
                     b -> pure.apply((TravB) Choice8.<A, B, C, D, E, F, G, I>b(b)).coerce(),
                     c -> pure.apply((TravB) Choice8.<A, B, C, D, E, F, G, I>c(c)),
                     d -> pure.apply((TravB) Choice8.<A, B, C, D, E, F, G, I>d(d)),
                     e -> pure.apply((TravB) Choice8.<A, B, C, D, E, F, G, I>e(e)),
                     f -> pure.apply((TravB) Choice8.<A, B, C, D, E, F, G, I>f(f)),
                     g -> pure.apply((TravB) Choice8.<A, B, C, D, E, F, G, I>g(g)),
                     h -> fn.apply(h).fmap(Choice8::h).<TravB>fmap(Applicative::coerce).coerce());
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice8}.
     *
     * @param a   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @param <H> the eighth possible type
     * @return the wrapped value as a {@link Choice8}&lt;A, B, C, D, E, F, G, H&gt;
     */
    public static <A, B, C, D, E, F, G, H> Choice8<A, B, C, D, E, F, G, H> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice8}.
     *
     * @param b   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @param <H> the eighth possible type
     * @return the wrapped value as a {@link Choice8}&lt;A, B, C, D, E, F, G, H&gt;
     */
    public static <A, B, C, D, E, F, G, H> Choice8<A, B, C, D, E, F, G, H> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>C</code> in a {@link Choice8}.
     *
     * @param c   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @param <H> the eighth possible type
     * @return the wrapped value as a {@link Choice8}&lt;A, B, C, D, E, F, G, H&gt;
     */
    public static <A, B, C, D, E, F, G, H> Choice8<A, B, C, D, E, F, G, H> c(C c) {
        return new _C<>(c);
    }

    /**
     * Static factory method for wrapping a value of type <code>D</code> in a {@link Choice8}.
     *
     * @param d   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @param <H> the eighth possible type
     * @return the wrapped value as a {@link Choice8}&lt;A, B, C, D, E, F, G, H&gt;
     */
    public static <A, B, C, D, E, F, G, H> Choice8<A, B, C, D, E, F, G, H> d(D d) {
        return new _D<>(d);
    }

    /**
     * Static factory method for wrapping a value of type <code>E</code> in a {@link Choice8}.
     *
     * @param e   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @param <H> the eighth possible type
     * @return the wrapped value as a {@link Choice8}&lt;A, B, C, D, E, F, G, H&gt;
     */
    public static <A, B, C, D, E, F, G, H> Choice8<A, B, C, D, E, F, G, H> e(E e) {
        return new _E<>(e);
    }

    /**
     * Static factory method for wrapping a value of type <code>F</code> in a {@link Choice8}.
     *
     * @param f   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @param <H> the eighth possible type
     * @return the wrapped value as a {@link Choice8}&lt;A, B, C, D, E, F, G, H&gt;
     */
    public static <A, B, C, D, E, F, G, H> Choice8<A, B, C, D, E, F, G, H> f(F f) {
        return new _F<>(f);
    }

    /**
     * Static factory method for wrapping a value of type <code>G</code> in a {@link Choice8}.
     *
     * @param g   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @param <H> the eighth possible type
     * @return the wrapped value as a {@link Choice8}&lt;A, B, C, D, E, F, G, H&gt;
     */
    public static <A, B, C, D, E, F, G, H> Choice8<A, B, C, D, E, F, G, H> g(G g) {
        return new _G<>(g);
    }

    /**
     * Static factory method for wrapping a value of type <code>H</code> in a {@link Choice8}.
     *
     * @param h   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @param <H> the eighth possible type
     * @return the wrapped value as a {@link Choice8}&lt;A, B, C, D, E, F, G, H&gt;
     */
    public static <A, B, C, D, E, F, G, H> Choice8<A, B, C, D, E, F, G, H> h(H h) {
        return new _H<>(h);
    }


    private static final class _A<A, B, C, D, E, F, G, H> extends Choice8<A, B, C, D, E, F, G, H> {

        private final A a;

        private _A(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                           Function<? super G, ? extends R> gFn, Function<? super H, ? extends R> hFn) {
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
            return "Choice8{a=" + a + '}';
        }
    }

    private static final class _B<A, B, C, D, E, F, G, H> extends Choice8<A, B, C, D, E, F, G, H> {

        private final B b;

        private _B(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                           Function<? super G, ? extends R> gFn, Function<? super H, ? extends R> hFn) {
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
            return "Choice8{b=" + b + '}';
        }
    }

    private static final class _C<A, B, C, D, E, F, G, H> extends Choice8<A, B, C, D, E, F, G, H> {

        private final C c;

        private _C(C c) {
            this.c = c;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                           Function<? super G, ? extends R> gFn, Function<? super H, ? extends R> hFn) {
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
            return "Choice8{c=" + c + '}';
        }
    }

    private static final class _D<A, B, C, D, E, F, G, H> extends Choice8<A, B, C, D, E, F, G, H> {

        private final D d;

        private _D(D d) {
            this.d = d;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                           Function<? super G, ? extends R> gFn, Function<? super H, ? extends R> hFn) {
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
            return "Choice8{d=" + d + '}';
        }
    }

    private static final class _E<A, B, C, D, E, F, G, H> extends Choice8<A, B, C, D, E, F, G, H> {

        private final E e;

        private _E(E e) {
            this.e = e;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                           Function<? super G, ? extends R> gFn, Function<? super H, ? extends R> hFn) {
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
            return "Choice8{e=" + e + '}';
        }
    }

    private static final class _F<A, B, C, D, E, F, G, H> extends Choice8<A, B, C, D, E, F, G, H> {

        private final F f;

        private _F(F f) {
            this.f = f;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                           Function<? super G, ? extends R> gFn, Function<? super H, ? extends R> hFn) {
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
            return "Choice8{f=" + f + '}';
        }
    }

    private static final class _G<A, B, C, D, E, F, G, H> extends Choice8<A, B, C, D, E, F, G, H> {

        private final G g;

        private _G(G g) {
            this.g = g;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                           Function<? super G, ? extends R> gFn, Function<? super H, ? extends R> hFn) {
            return gFn.apply(g);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof _G
                    && Objects.equals(g, ((_G) other).g);
        }

        @Override
        public int hashCode() {
            return Objects.hash(g);
        }

        @Override
        public String toString() {
            return "Choice8{g=" + g + '}';
        }
    }

    private static final class _H<A, B, C, D, E, F, G, H> extends Choice8<A, B, C, D, E, F, G, H> {

        private final H h;

        private _H(H h) {
            this.h = h;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                           Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                           Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                           Function<? super G, ? extends R> gFn, Function<? super H, ? extends R> hFn) {
            return hFn.apply(h);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof _H
                    && Objects.equals(h, ((_H) other).h);
        }

        @Override
        public int hashCode() {
            return Objects.hash(h);
        }

        @Override
        public String toString() {
            return "Choice8{h=" + h + '}';
        }
    }
}
