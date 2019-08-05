package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct4;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct5;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into5.into5;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * Canonical ADT representation of {@link CoProduct5}.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @param <C> the third possible type
 * @param <D> the fourth possible type
 * @param <E> the fifth possible type
 * @see Choice4
 * @see Choice6
 */
public abstract class Choice5<A, B, C, D, E> implements
        CoProduct5<A, B, C, D, E, Choice5<A, B, C, D, E>>,
        MonadRec<E, Choice5<A, B, C, D, ?>>,
        Bifunctor<D, E, Choice5<A, B, C, ?, ?>>,
        Traversable<E, Choice5<A, B, C, D, ?>> {

    private Choice5() {
    }

    /**
     * Specialize this choice's projection to a {@link Tuple5}.
     *
     * @return a {@link Tuple5}
     */
    @Override
    public Tuple5<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>, Maybe<E>> project() {
        return into5(HList::tuple, CoProduct5.super.project());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice6<A, B, C, D, E, F> diverge() {
        return match(Choice6::a, Choice6::b, Choice6::c, Choice6::d, Choice6::e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Choice4<A, B, C, D> converge(Fn1<? super E, ? extends CoProduct4<A, B, C, D, ?>> convergenceFn) {
        return match(Choice4::a, Choice4::b, Choice4::c, Choice4::d,
                     convergenceFn.fmap(cp4 -> cp4.match(Choice4::a, Choice4::b, Choice4::c, Choice4::d)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, D, F> fmap(Fn1<? super E, ? extends F> fn) {
        return MonadRec.super.<F>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, F, E> biMapL(Fn1<? super D, ? extends F> fn) {
        return (Choice5<A, B, C, F, E>) Bifunctor.super.<F>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, D, F> biMapR(Fn1<? super E, ? extends F> fn) {
        return (Choice5<A, B, C, D, F>) Bifunctor.super.<F>biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F, G> Choice5<A, B, C, F, G> biMap(Fn1<? super D, ? extends F> lFn,
                                               Fn1<? super E, ? extends G> rFn) {
        return match(Choice5::a, Choice5::b, Choice5::c, d -> d(lFn.apply(d)), e -> e(rFn.apply(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, D, F> pure(F f) {
        return e(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, D, F> zip(Applicative<Fn1<? super E, ? extends F>, Choice5<A, B, C, D, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Lazy<Choice5<A, B, C, D, F>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super E, ? extends F>, Choice5<A, B, C, D, ?>>> lazyAppFn) {
        return match(a -> lazy(a(a)),
                     b -> lazy(b(b)),
                     c -> lazy(c(c)),
                     d -> lazy(d(d)),
                     e -> lazyAppFn.fmap(choiceF -> choiceF.<F>fmap(f -> f.apply(e)).coerce()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, D, F> discardL(Applicative<F, Choice5<A, B, C, D, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, D, E> discardR(Applicative<F, Choice5<A, B, C, D, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, D, F> flatMap(Fn1<? super E, ? extends Monad<F, Choice5<A, B, C, D, ?>>> f) {
        return match(Choice5::a, Choice5::b, Choice5::c, Choice5::d, e -> f.apply(e).coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F> Choice5<A, B, C, D, F> trampolineM(Fn1<? super E, ? extends MonadRec<RecursiveResult<E, F>, Choice5<A, B, C, D, ?>>> fn) {
        return match(Choice5::a,
                     Choice5::b,
                     Choice5::c,
                     Choice5::d,
                     trampoline(e -> fn.apply(e).<Choice5<A, B, C, D, RecursiveResult<E, F>>>coerce().match(
                                a -> terminate(Choice5.a(a)),
                                b -> terminate(Choice5.b(b)),
                                c -> terminate(Choice5.c(c)),
                                d -> terminate(Choice5.d(d)),
                                eRec -> eRec.fmap(Choice5::e))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <F, App extends Applicative<?, App>, TravB extends Traversable<F, Choice5<A, B, C, D, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super E, ? extends Applicative<F, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return match(a -> pure.apply((TravB) Choice5.<A, B, C, D, F>a(a)).coerce(),
                     b -> pure.apply((TravB) Choice5.<A, B, C, D, F>b(b)).coerce(),
                     c -> pure.apply((TravB) Choice5.<A, B, C, D, F>c(c)),
                     d -> pure.apply((TravB) Choice5.<A, B, C, D, F>d(d)),
                     e -> fn.apply(e).<Choice5<A, B, C, D, F>>fmap(Choice5::e)
                             .<TravB>fmap(Applicative::coerce).coerce());
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice5}.
     *
     * @param a   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @return the wrapped value as a {@link Choice5}&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice5}.
     *
     * @param b   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @return the wrapped value as a {@link Choice5}&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>C</code> in a {@link Choice5}.
     *
     * @param c   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @return the wrapped value as a {@link Choice5}&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> c(C c) {
        return new _C<>(c);
    }

    /**
     * Static factory method for wrapping a value of type <code>D</code> in a {@link Choice5}.
     *
     * @param d   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @return the wrapped value as a {@link Choice5}&lt;A, B, C, D, E&gt;
     */
    public static <A, B, C, D, E> Choice5<A, B, C, D, E> d(D d) {
        return new _D<>(d);
    }

    /**
     * Static factory method for wrapping a value of type <code>E</code> in a {@link Choice5}.
     *
     * @param e   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @param <E> the fifth possible type
     * @return the wrapped value as a {@link Choice5}&lt;A, B, C, D, E&gt;
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn) {
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn) {
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn) {
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn) {
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn,
                           Fn1<? super E, ? extends R> eFn) {
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
