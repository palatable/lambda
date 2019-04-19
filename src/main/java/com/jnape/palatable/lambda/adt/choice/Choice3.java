package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * Canonical ADT representation of {@link CoProduct3}.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @param <C> the third possible type
 * @see Choice2
 * @see Choice4
 */
public abstract class Choice3<A, B, C> implements
        CoProduct3<A, B, C, Choice3<A, B, C>>,
        Monad<C, Choice3<A, B, ?>>,
        Bifunctor<B, C, Choice3<A, ?, ?>>,
        Traversable<C, Choice3<A, B, ?>> {

    private Choice3() {
    }

    /**
     * Specialize this choice's projection to a {@link Tuple3}.
     *
     * @return a {@link Tuple3}
     */
    @Override
    public Tuple3<Maybe<A>, Maybe<B>, Maybe<C>> project() {
        return into3(HList::tuple, CoProduct3.super.project());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <D> Choice4<A, B, C, D> diverge() {
        return match(Choice4::a, Choice4::b, Choice4::c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Choice2<A, B> converge(Function<? super C, ? extends CoProduct2<A, B, ?>> convergenceFn) {
        return match(Choice2::a, Choice2::b, convergenceFn.andThen(cp2 -> cp2.match(Choice2::a, Choice2::b)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <D> Choice3<A, B, D> fmap(Function<? super C, ? extends D> fn) {
        return Monad.super.<D>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <D> Choice3<A, D, C> biMapL(Function<? super B, ? extends D> fn) {
        return (Choice3<A, D, C>) Bifunctor.super.biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <D> Choice3<A, B, D> biMapR(Function<? super C, ? extends D> fn) {
        return (Choice3<A, B, D>) Bifunctor.super.biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <D, E> Choice3<A, D, E> biMap(Function<? super B, ? extends D> lFn,
                                               Function<? super C, ? extends E> rFn) {
        return match(Choice3::a, b -> b(lFn.apply(b)), c -> c(rFn.apply(c)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <D> Choice3<A, B, D> pure(D d) {
        return c(d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <D> Choice3<A, B, D> zip(Applicative<Function<? super C, ? extends D>, Choice3<A, B, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     *
     * @param lazyAppFn
     */
    @Override
    public <D> Lazy<Choice3<A, B, D>> lazyZip(
            Lazy<? extends Applicative<Function<? super C, ? extends D>, Choice3<A, B, ?>>> lazyAppFn) {
        return match(a -> lazy(a(a)),
                     b -> lazy(b(b)),
                     c -> lazyAppFn.fmap(choiceF -> choiceF.<D>fmap(f -> f.apply(c)).coerce()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <D> Choice3<A, B, D> discardL(Applicative<D, Choice3<A, B, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <D> Choice3<A, B, C> discardR(Applicative<D, Choice3<A, B, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <D> Choice3<A, B, D> flatMap(Function<? super C, ? extends Monad<D, Choice3<A, B, ?>>> f) {
        return match(Choice3::a, Choice3::b, c -> f.apply(c).coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <D, App extends Applicative<?, App>, TravB extends Traversable<D, Choice3<A, B, ?>>,
            AppB extends Applicative<D, App>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Function<? super C, ? extends AppB> fn,
                                                                      Function<? super TravB, ? extends AppTrav> pure) {
        return match(a -> pure.apply((TravB) Choice3.<A, B, D>a(a)).coerce(),
                     b -> pure.apply((TravB) Choice3.<A, B, D>b(b)).coerce(),
                     c -> fn.apply(c).<Choice3<A, B, D>>fmap(Choice3::c).<TravB>fmap(Functor::coerce).coerce());
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice3}.
     *
     * @param a   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @return the wrapped value as a {@link Choice3}&lt;A, B, C&gt;
     */
    public static <A, B, C> Choice3<A, B, C> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice3}.
     *
     * @param b   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @return the wrapped value as a {@link Choice3}&lt;A, B, C&gt;
     */
    public static <A, B, C> Choice3<A, B, C> b(B b) {
        return new _B<>(b);
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice3}.
     *
     * @param c   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @param <C> the third possible type
     * @return the wrapped value as a {@link Choice3}&lt;A, B, C&gt;
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
