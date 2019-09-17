package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct6;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct7;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into7.into7;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * Canonical ADT representation of {@link CoProduct7}.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @param <C> the third possible type
 * @param <D> the fourth possible type
 * @param <E> the fifth possible type
 * @param <F> the sixth possible type
 * @param <G> the seventh possible type
 * @see Choice6
 * @see Choice8
 */
public abstract class Choice7<A, B, C, D, E, F, G> implements
        CoProduct7<A, B, C, D, E, F, G, Choice7<A, B, C, D, E, F, G>>,
        MonadRec<G, Choice7<A, B, C, D, E, F, ?>>,
        Bifunctor<F, G, Choice7<A, B, C, D, E, ?, ?>>,
        Traversable<G, Choice7<A, B, C, D, E, F, ?>> {

    private Choice7() {
    }

    /**
     * Specialize this choice's projection to a {@link Tuple7}.
     *
     * @return a {@link Tuple7}
     */
    @Override
    public Tuple7<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>, Maybe<E>, Maybe<F>, Maybe<G>> project() {
        return into7(HList::tuple, CoProduct7.super.project());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice8<A, B, C, D, E, F, G, H> diverge() {
        return match(Choice8::a, Choice8::b, Choice8::c, Choice8::d, Choice8::e, Choice8::f, Choice8::g);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Choice6<A, B, C, D, E, F> converge(Fn1<? super G, ? extends CoProduct6<A, B, C, D, E, F, ?>> convergenceFn) {
        return match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e, Choice6::f,
                     convergenceFn.fmap(cp6 -> cp6.match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e,
                                                         Choice6::f)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, F, H> fmap(Fn1<? super G, ? extends H> fn) {
        return MonadRec.super.<H>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, H, G> biMapL(Fn1<? super F, ? extends H> fn) {
        return (Choice7<A, B, C, D, E, H, G>) Bifunctor.super.<H>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, F, H> biMapR(Fn1<? super G, ? extends H> fn) {
        return (Choice7<A, B, C, D, E, F, H>) Bifunctor.super.<H>biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H, I> Choice7<A, B, C, D, E, H, I> biMap(Fn1<? super F, ? extends H> lFn,
                                                     Fn1<? super G, ? extends I> rFn) {
        return match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, f -> f(lFn.apply(f)), g -> g(rFn.apply(g)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, F, H> pure(H h) {
        return g(h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, F, H> zip(
            Applicative<Fn1<? super G, ? extends H>, Choice7<A, B, C, D, E, F, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Lazy<Choice7<A, B, C, D, E, F, H>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super G, ? extends H>, Choice7<A, B, C, D, E, F, ?>>> lazyAppFn) {
        return match(a -> lazy(a(a)),
                     b -> lazy(b(b)),
                     c -> lazy(c(c)),
                     d -> lazy(d(d)),
                     e -> lazy(e(e)),
                     f -> lazy(f(f)),
                     g -> lazyAppFn.fmap(choiceF -> choiceF.<H>fmap(f -> f.apply(g)).coerce()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, F, H> discardL(Applicative<H, Choice7<A, B, C, D, E, F, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, F, G> discardR(Applicative<H, Choice7<A, B, C, D, E, F, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, F, H> flatMap(
            Fn1<? super G, ? extends Monad<H, Choice7<A, B, C, D, E, F, ?>>> fn) {
        return match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, Choice7::f, g -> fn.apply(g).coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H> Choice7<A, B, C, D, E, F, H> trampolineM(
            Fn1<? super G, ? extends MonadRec<RecursiveResult<G, H>, Choice7<A, B, C, D, E, F, ?>>> fn) {
        return flatMap(trampoline(g -> fn.apply(g).<Choice7<A, B, C, D, E, F, RecursiveResult<G, H>>>coerce().match(
                a -> terminate(a(a)),
                b -> terminate(b(b)),
                c -> terminate(c(c)),
                d -> terminate(d(d)),
                e -> terminate(e(e)),
                f -> terminate(f(f)),
                gRec -> gRec.fmap(Choice7::g))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H, App extends Applicative<?, App>, TravB extends Traversable<H, Choice7<A, B, C, D, E, F, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super G, ? extends Applicative<H, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return match(a -> pure.apply(Choice7.<A, B, C, D, E, F, H>a(a).<TravB>coerce()),
                     b -> pure.apply(Choice7.<A, B, C, D, E, F, H>b(b).<TravB>coerce()),
                     c -> pure.apply(Choice7.<A, B, C, D, E, F, H>c(c).<TravB>coerce()),
                     d -> pure.apply(Choice7.<A, B, C, D, E, F, H>d(d).<TravB>coerce()),
                     e -> pure.apply(Choice7.<A, B, C, D, E, F, H>e(e).<TravB>coerce()),
                     f -> pure.apply(Choice7.<A, B, C, D, E, F, H>f(f).<TravB>coerce()),
                     g -> fn.apply(g).<Choice7<A, B, C, D, E, F, H>>fmap(Choice7::g)
                             .<TravB>fmap(Applicative::coerce))
                .coerce();
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice7}.
     *
     * @param a   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @return the wrapped value as a {@link Choice7}&lt;A, B, C, D, E, F, G&gt;
     */
    public static <A, B, C, D, E, F, G> Choice7<A, B, C, D, E, F, G> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice7}.
     *
     * @param b   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @return the wrapped value as a {@link Choice7}&lt;A, B, C, D, E, F, G&gt;
     */
    public static <A, B, C, D, E, F, G> Choice7<A, B, C, D, E, F, G> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>C</code> in a {@link Choice7}.
     *
     * @param c   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @return the wrapped value as a {@link Choice7}&lt;A, B, C, D, E, F, G&gt;
     */
    public static <A, B, C, D, E, F, G> Choice7<A, B, C, D, E, F, G> c(C c) {
        return new _C<>(c);
    }

    /**
     * Static factory method for wrapping a value of type <code>D</code> in a {@link Choice7}.
     *
     * @param d   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @return the wrapped value as a {@link Choice7}&lt;A, B, C, D, E, F, G&gt;
     */
    public static <A, B, C, D, E, F, G> Choice7<A, B, C, D, E, F, G> d(D d) {
        return new _D<>(d);
    }

    /**
     * Static factory method for wrapping a value of type <code>E</code> in a {@link Choice7}.
     *
     * @param e   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @return the wrapped value as a {@link Choice7}&lt;A, B, C, D, E, F, G&gt;
     */
    public static <A, B, C, D, E, F, G> Choice7<A, B, C, D, E, F, G> e(E e) {
        return new _E<>(e);
    }

    /**
     * Static factory method for wrapping a value of type <code>F</code> in a {@link Choice7}.
     *
     * @param f   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @return the wrapped value as a {@link Choice7}&lt;A, B, C, D, E, F, G&gt;
     */
    public static <A, B, C, D, E, F, G> Choice7<A, B, C, D, E, F, G> f(F f) {
        return new _F<>(f);
    }

    /**
     * Static factory method for wrapping a value of type <code>G</code> in a {@link Choice7}.
     *
     * @param g   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @param <G> the seventh possible type
     * @return the wrapped value as a {@link Choice7}&lt;A, B, C, D, E, F, G&gt;
     */
    public static <A, B, C, D, E, F, G> Choice7<A, B, C, D, E, F, G> g(G g) {
        return new _G<>(g);
    }

    /**
     * The canonical {@link Pure} instance for {@link Choice7}.
     *
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @param <F> the sixth possible type
     * @return the {@link Pure} instance
     */
    public static <A, B, C, D, E, F> Pure<Choice7<A, B, C, D, E, F, ?>> pureChoice() {
        return Choice7::g;
    }

    private static final class _A<A, B, C, D, E, F, G> extends Choice7<A, B, C, D, E, F, G> {

        private final A a;

        private _A(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn, Fn1<? super F, ? extends R> fFn,
                           Fn1<? super G, ? extends R> gFn) {
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
            return "Choice7{a=" + a + '}';
        }
    }

    private static final class _B<A, B, C, D, E, F, G> extends Choice7<A, B, C, D, E, F, G> {

        private final B b;

        private _B(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn, Fn1<? super F, ? extends R> fFn,
                           Fn1<? super G, ? extends R> gFn) {
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
            return "Choice7{b=" + b + '}';
        }
    }

    private static final class _C<A, B, C, D, E, F, G> extends Choice7<A, B, C, D, E, F, G> {

        private final C c;

        private _C(C c) {
            this.c = c;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn, Fn1<? super F, ? extends R> fFn,
                           Fn1<? super G, ? extends R> gFn) {
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
            return "Choice7{c=" + c + '}';
        }
    }

    private static final class _D<A, B, C, D, E, F, G> extends Choice7<A, B, C, D, E, F, G> {

        private final D d;

        private _D(D d) {
            this.d = d;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn, Fn1<? super F, ? extends R> fFn,
                           Fn1<? super G, ? extends R> gFn) {
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
            return "Choice7{d=" + d + '}';
        }
    }

    private static final class _E<A, B, C, D, E, F, G> extends Choice7<A, B, C, D, E, F, G> {

        private final E e;

        private _E(E e) {
            this.e = e;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn, Fn1<? super F, ? extends R> fFn,
                           Fn1<? super G, ? extends R> gFn) {
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
            return "Choice7{e=" + e + '}';
        }
    }

    private static final class _F<A, B, C, D, E, F, G> extends Choice7<A, B, C, D, E, F, G> {

        private final F f;

        private _F(F f) {
            this.f = f;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn, Fn1<? super F, ? extends R> fFn,
                           Fn1<? super G, ? extends R> gFn) {
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
            return "Choice7{f=" + f + '}';
        }
    }

    private static final class _G<A, B, C, D, E, F, G> extends Choice7<A, B, C, D, E, F, G> {

        private final G g;

        private _G(G g) {
            this.g = g;
        }

        @Override
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn, Fn1<? super F, ? extends R> fFn,
                           Fn1<? super G, ? extends R> gFn) {
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
            return "Choice7{g=" + g + '}';
        }
    }
}
