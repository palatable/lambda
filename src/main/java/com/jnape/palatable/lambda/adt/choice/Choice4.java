package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct4;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * Canonical ADT representation of {@link CoProduct4}.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @param <C> the third possible type
 * @param <D> the fourth possible type
 * @see Choice3
 * @see Choice5
 */
public abstract class Choice4<A, B, C, D> implements
        CoProduct4<A, B, C, D, Choice4<A, B, C, D>>,
        MonadRec<D, Choice4<A, B, C, ?>>,
        Bifunctor<C, D, Choice4<A, B, ?, ?>>,
        Traversable<D, Choice4<A, B, C, ?>> {

    private Choice4() {
    }

    /**
     * Specialize this choice's projection to a {@link Tuple4}.
     *
     * @return a {@link Tuple4}
     */
    @Override
    public Tuple4<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>> project() {
        return into4(HList::tuple, CoProduct4.super.project());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> Choice5<A, B, C, D, E> diverge() {
        return match(Choice5::a, Choice5::b, Choice5::c, Choice5::d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Choice3<A, B, C> converge(Fn1<? super D, ? extends CoProduct3<A, B, C, ?>> convergenceFn) {
        return match(Choice3::a, Choice3::b, Choice3::c,
                     convergenceFn.fmap(cp3 -> cp3.match(Choice3::a, Choice3::b, Choice3::c)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <E> Choice4<A, B, C, E> fmap(Fn1<? super D, ? extends E> fn) {
        return MonadRec.super.<E>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <E> Choice4<A, B, E, D> biMapL(Fn1<? super C, ? extends E> fn) {
        return (Choice4<A, B, E, D>) Bifunctor.super.<E>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <E> Choice4<A, B, C, E> biMapR(Fn1<? super D, ? extends E> fn) {
        return (Choice4<A, B, C, E>) Bifunctor.super.<E>biMapR(fn);
    }

    @Override
    public final <E, F> Choice4<A, B, E, F> biMap(Fn1<? super C, ? extends E> lFn,
                                                  Fn1<? super D, ? extends F> rFn) {
        return match(Choice4::a, Choice4::b, c -> c(lFn.apply(c)), d -> d(rFn.apply(d)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> Choice4<A, B, C, E> pure(E e) {
        return d(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> Choice4<A, B, C, E> zip(Applicative<Fn1<? super D, ? extends E>, Choice4<A, B, C, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> Lazy<Choice4<A, B, C, E>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super D, ? extends E>, Choice4<A, B, C, ?>>> lazyAppFn) {
        return match(a -> lazy(a(a)),
                     b -> lazy(b(b)),
                     c -> lazy(c(c)),
                     d -> lazyAppFn.fmap(choiceF -> choiceF.<E>fmap(f -> f.apply(d)).coerce()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> Choice4<A, B, C, E> discardL(Applicative<E, Choice4<A, B, C, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> Choice4<A, B, C, D> discardR(Applicative<E, Choice4<A, B, C, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> Choice4<A, B, C, E> flatMap(Fn1<? super D, ? extends Monad<E, Choice4<A, B, C, ?>>> f) {
        return match(Choice4::a, Choice4::b, Choice4::c, d -> f.apply(d).coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <E, App extends Applicative<?, App>, TravB extends Traversable<E, Choice4<A, B, C, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super D, ? extends Applicative<E, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return match(a -> pure.apply((TravB) Choice4.<A, B, C, E>a(a)).coerce(),
                     b -> pure.apply((TravB) Choice4.<A, B, C, E>b(b)).coerce(),
                     c -> pure.apply((TravB) Choice4.<A, B, C, E>c(c)),
                     d -> fn.apply(d).<Choice4<A, B, C, E>>fmap(Choice4::d).<TravB>fmap(Functor::coerce).coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> Choice4<A, B, C, E> trampolineM(
            Fn1<? super D, ? extends MonadRec<RecursiveResult<D, E>, Choice4<A, B, C, ?>>> fn) {
        return match(Choice4::a,
                     Choice4::b,
                     Choice4::c,
                     trampoline(d -> fn.apply(d).<Choice4<A, B, C, RecursiveResult<D, E>>>coerce()
                             .match(a -> terminate(a(a)),
                                    b -> terminate(b(b)),
                                    c -> terminate(c(c)),
                                    dOrE -> dOrE.fmap(Choice4::d))));
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice4}.
     *
     * @param a   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @return the wrapped value as a {@link Choice4}&lt;A, B, C, D&gt;
     */
    public static <A, B, C, D> Choice4<A, B, C, D> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice4}.
     *
     * @param b   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @return the wrapped value as a {@link Choice4}&lt;A, B, C, D&gt;
     */
    public static <A, B, C, D> Choice4<A, B, C, D> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>C</code> in a {@link Choice4}.
     *
     * @param c   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @return the wrapped value as a {@link Choice4}&lt;A, B, C, D&gt;
     */
    public static <A, B, C, D> Choice4<A, B, C, D> c(C c) {
        return new _C<>(c);
    }

    /**
     * Static factory method for wrapping a value of type <code>D</code> in a {@link Choice4}.
     *
     * @param d   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @param <D> the fourth possible type
     * @return the wrapped value as a {@link Choice4}&lt;A, B, C, D&gt;
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn) {
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn) {
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn) {
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
        public <R> R match(Fn1<? super A, ? extends R> aFn, Fn1<? super B, ? extends R> bFn,
                           Fn1<? super C, ? extends R> cFn, Fn1<? super D, ? extends R> dFn) {
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
