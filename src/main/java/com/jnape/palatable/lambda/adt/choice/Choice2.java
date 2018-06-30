package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Apply;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Bind;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;

/**
 * Canonical ADT representation of {@link CoProduct2}. Unlike {@link Either}, there is no concept of "success" or
 * "failure", so the domain of reasonable function semantics is more limited.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @see Either
 * @see Choice3
 */
public abstract class Choice2<A, B> implements
        CoProduct2<A, B, Choice2<A, B>>,
        Monad<B, Choice2<A, ?>>,
        Bifunctor<A, B, Choice2>,
        Traversable<B, Choice2<A, ?>> {

    private Choice2() {
    }

    /**
     * Specialize this choice's projection to a {@link Tuple2}.
     *
     * @return a {@link Tuple2}
     */
    @Override
    public Tuple2<Maybe<A>, Maybe<B>> project() {
        return into(HList::tuple, CoProduct2.super.project());
    }

    @Override
    public final <C> Choice3<A, B, C> diverge() {
        return match(Choice3::a, Choice3::b);
    }

    @Override
    public Choice2<B, A> invert() {
        return match(Choice2::b, Choice2::a);
    }

    @Override
    public final <C> Choice2<A, C> fmap(Function<? super B, ? extends C> fn) {
        return Monad.super.<C>fmap(fn).coerce();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <C> Choice2<C, B> biMapL(Function<? super A, ? extends C> fn) {
        return (Choice2<C, B>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <C> Choice2<A, C> biMapR(Function<? super B, ? extends C> fn) {
        return (Choice2<A, C>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public final <C, D> Choice2<C, D> biMap(Function<? super A, ? extends C> lFn,
                                            Function<? super B, ? extends D> rFn) {
        return match(a -> a(lFn.apply(a)), b -> b(rFn.apply(b)));
    }

    @Override
    public <C> Choice2<A, C> pure(C c) {
        return b(c);
    }

    @Override
    public <C> Choice2<A, C> zip(Apply<Function<? super B, ? extends C>, Choice2<A, ?>> appFn) {
        return appFn.<Choice2<A, Function<? super B, ? extends C>>>coerce()
                .match(Choice2::a, this::biMapR);
    }

    @Override
    public <C> Choice2<A, C> discardL(Applicative<C, Choice2<A, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <C> Choice2<A, B> discardR(Applicative<C, Choice2<A, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public final <C> Choice2<A, C> flatMap(Function<? super B, ? extends Bind<C, Choice2<A, ?>>> f) {
        return match(Choice2::a, b -> f.apply(b).coerce());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, App extends Applicative, TravB extends Traversable<C, Choice2<A, ?>>, AppB extends Applicative<C, App>, AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Function<? super B, ? extends AppB> fn, Function<? super TravB, ? extends AppTrav> pure) {
        return match(a -> pure.apply((TravB) a(a)),
                     b -> fn.apply(b).fmap(Choice2::b).<TravB>fmap(Applicative::coerce).coerce());
    }

    /**
     * Static factory method for wrapping a value of type <code>A</code> in a {@link Choice2}.
     *
     * @param a   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @return the wrapped value as a {@link Choice2}&lt;A, B&gt;
     */
    public static <A, B> Choice2<A, B> a(A a) {
        return new _A<>(a);
    }

    /**
     * Static factory method for wrapping a value of type <code>B</code> in a {@link Choice2}.
     *
     * @param b   the value
     * @param <A> the first possible type
     * @param <B> the second possible type
     * @return the wrapped value as a {@link Choice2}&lt;A, B&gt;
     */
    public static <A, B> Choice2<A, B> b(B b) {
        return new _B<>(b);
    }

    private static final class _A<A, B> extends Choice2<A, B> {

        private final A a;

        private _A(A a) {
            this.a = a;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn) {
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
            return "Choice2{" +
                    "a=" + a +
                    '}';
        }
    }

    private static final class _B<A, B> extends Choice2<A, B> {

        private final B b;

        private _B(B b) {
            this.b = b;
        }

        @Override
        public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn) {
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
            return "Choice2{" +
                    "b=" + b +
                    '}';
        }
    }
}
