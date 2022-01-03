package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn1.Constantly;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Cartesian;
import com.jnape.palatable.lambda.functor.Cocartesian;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.internal.Runtime;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadReader;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.MonadWriter;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.Fn2.curried;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;

/**
 * A function taking a single argument. This is the core function type that all other function types extend and
 * auto-curry with.
 *
 * @param <A> The argument type
 * @param <B> The result type
 */
@FunctionalInterface
public interface Fn1<A, B> extends
        MonadRec<B, Fn1<A, ?>>,
        MonadReader<A, B, Fn1<A, ?>>,
        MonadWriter<A, B, Fn1<A, ?>>,
        Cartesian<A, B, Fn1<?, ?>>,
        Cocartesian<A, B, Fn1<?, ?>> {

    /**
     * Invoke this function explosively with the given argument.
     *
     * @param a the argument
     * @return the result of the function application
     */
    default B apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }

    /**
     * Invoke this function with the given argument, potentially throwing any {@link Throwable}.
     *
     * @param a the argument
     * @return the result of the function application
     * @throws Throwable anything possibly thrown by the function
     */
    B checkedApply(A a) throws Throwable;

    /**
     * Convert this {@link Fn1} to an {@link Fn0} by supplying an argument to this function. Useful for fixing an
     * argument now, but deferring application until a later time.
     *
     * @param a the argument
     * @return an {@link Fn0}
     */
    default Fn0<B> thunk(A a) {
        return () -> apply(a);
    }

    /**
     * Widen this function's argument list by prepending an ignored argument of any type to the front.
     *
     * @param <Z> the new first argument type
     * @return the widened function
     */
    default <Z> Fn2<Z, A, B> widen() {
        return curried(constantly(this));
    }

    /**
     * Convert this {@link Fn1} to a java {@link Function}.
     *
     * @return the {@link Function}
     */
    default Function<A, B> toFunction() {
        return this::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Fn1<A, B> local(Fn1<? super A, ? extends A> fn) {
        return contraMap(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, Tuple2<B, C>> listens(Fn1<? super A, ? extends C> fn) {
        return carry().fmap(t -> t.<C>biMapL(fn).invert());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Fn1<A, B> censor(Fn1<? super A, ? extends A> fn) {
        return a -> apply(fn.apply(a));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> flatMap(Fn1<? super B, ? extends Monad<C, Fn1<A, ?>>> f) {
        return a -> f.apply(apply(a)).<Fn1<A, C>>coerce().apply(a);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> rflatMap(Fn2<? super A, ? super B, ? extends MonadReader<A, C, Fn1<A, ?>>> f) {
    	return a -> f.apply(a, apply(a)).<Fn1<A, C>>coerce().apply(a);
    }

    /**
     * Left-to-right composition.
     *
     * @param <C> the return type of the next function to invoke
     * @param f   the function to invoke with this function's return value
     * @return a function representing the composition of this function and f
     */
    @Override
    default <C> Fn1<A, C> fmap(Fn1<? super B, ? extends C> f) {
        return a -> f.apply(apply(a));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> pure(C c) {
        return __ -> c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> zip(Applicative<Fn1<? super B, ? extends C>, Fn1<A, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    default <C> Fn1<A, C> zip(Fn2<A, B, C> appFn) {
        return zip((Fn1<A, Fn1<? super B, ? extends C>>) (Object) appFn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Lazy<Fn1<A, C>> lazyZip(Lazy<? extends Applicative<Fn1<? super B, ? extends C>, Fn1<A, ?>>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(Monad<C, Fn1<A, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> trampolineM(Fn1<? super B, ? extends MonadRec<RecursiveResult<B, C>, Fn1<A, ?>>> fn) {
        return a -> trampoline(b -> fn.apply(b).<Fn1<A, RecursiveResult<B, C>>>coerce().apply(a), apply(a));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> discardL(Applicative<C, Fn1<A, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, B> discardR(Applicative<C, Fn1<A, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * Contravariantly map over the argument to this function, producing a function that takes the new argument type,
     * and produces the same result.
     *
     * @param <Z> the new argument type
     * @param fn  the contravariant argument mapping function
     * @return an {@link Fn1}&lt;Z, B&gt;
     */
    @Override
    default <Z> Fn1<Z, B> diMapL(Fn1<? super Z, ? extends A> fn) {
        return (Fn1<Z, B>) Cartesian.super.<Z>diMapL(fn);
    }

    /**
     * Covariantly map over the return value of this function, producing a function that takes the same argument, and
     * produces the new result type.
     *
     * @param <C> the new result type
     * @param fn  the covariant result mapping function
     * @return an {@link Fn1}&lt;A, C&gt;
     */
    @Override
    default <C> Fn1<A, C> diMapR(Fn1<? super B, ? extends C> fn) {
        return (Fn1<A, C>) Cartesian.super.<C>diMapR(fn);
    }

    /**
     * Exercise both <code>diMapL</code> and <code>diMapR</code> over this function in the same invocation.
     *
     * @param <Z> the new argument type
     * @param <C> the new result type
     * @param lFn the contravariant argument mapping function
     * @param rFn the covariant result mapping function
     * @return an {@link Fn1}&lt;Z, C&gt;
     */
    @Override
    default <Z, C> Fn1<Z, C> diMap(Fn1<? super Z, ? extends A> lFn, Fn1<? super B, ? extends C> rFn) {
        return lFn.fmap(this).fmap(rFn)::apply;
    }

    /**
     * Pair a value with the input to this function, and preserve the paired value through to the output.
     *
     * @param <C> the paired value
     * @return the strengthened {@link Fn1}
     */
    @Override
    default <C> Fn1<Tuple2<C, A>, Tuple2<C, B>> cartesian() {
        return t -> t.fmap(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Fn1<A, Tuple2<A, B>> carry() {
        return (Fn1<A, Tuple2<A, B>>) Cartesian.super.carry();
    }

    /**
     * Choose between either applying this function or returning back a different result altogether.
     *
     * @param <C> the potentially different result
     * @return teh strengthened {@link Fn1}
     */
    @Override
    default <C> Fn1<Choice2<C, A>, Choice2<C, B>> cocartesian() {
        return a -> a.fmap(this);
    }

    /**
     * Choose between a successful result <code>b</code> or returning back the input, <code>a</code>.
     *
     * @return an {@link Fn1} that chooses between its input (in case of failure) or its output.
     */
    @Override
    default Fn1<A, Choice2<A, B>> choose() {
        return a -> Either.trying(() -> apply(a), constantly(a)).match(Choice2::a, Choice2::b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Fn1<Z, B> contraMap(Fn1<? super Z, ? extends A> fn) {
        return (Fn1<Z, B>) Cartesian.super.<Z>contraMap(fn);
    }

    /**
     * Right-to-left composition between different arity functions. Preserves highest arity in the return type.
     *
     * @param before the function to pass its return value to this function's input
     * @param <Y>    the resulting function's first argument type
     * @param <Z>    the resulting function's second argument type
     * @return an {@link Fn2}&lt;Y, Z, B&gt;
     */
    default <Y, Z> Fn2<Y, Z, B> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return curried(before.fmap(this::contraMap))::apply;
    }

    /**
     * Left-to-right composition between different arity functions. Preserves highest arity in the return type.
     *
     * @param after the function to invoke on this function's return value
     * @param <C>   the resulting function's second argument type
     * @param <D>   the resulting function's return type
     * @return an {@link Fn2}&lt;A, C, D&gt;
     */
    default <C, D> Fn2<A, C, D> andThen(Fn2<? super B, ? super C, ? extends D> after) {
        return (a, c) -> after.apply(apply(a), c);
    }

    default Fn1<A, B> self() {
        return this;
    }

    /**
     * Static factory method for avoid explicit casting when using method references as {@link Fn1}s.
     *
     * @param fn  the function to adapt
     * @param <A> the input type
     * @param <B> the output type
     * @return the {@link Fn1}
     */
    static <A, B> Fn1<A, B> fn1(Fn1<? super A, ? extends B> fn) {
        return fn::apply;
    }

    /**
     * Static factory method for wrapping a java {@link Function} in an {@link Fn1}.
     *
     * @param function the function
     * @param <A>      the input type
     * @param <B>      the output type
     * @return the {@link Fn1}
     */
    static <A, B> Fn1<A, B> fromFunction(Function<? super A, ? extends B> function) {
        return function::apply;
    }

    /**
     * The canonical {@link Pure} instance for {@link Fn1}.
     *
     * @param <A> the input type
     * @return the {@link Pure} instance
     */
    static <A> Pure<Fn1<A, ?>> pureFn1() {
        return Constantly::constantly;
    }

    /**
     * Construct an {@link Fn1} that has a reference to itself in scope at the time it is executed (presumably for
     * recursive invocations).
     *
     * @param fn  the body of the function, with access to itself
     * @param <A> the input type
     * @param <B> the output type
     * @return the {@link Fn1}
     */
    static <A, B> Fn1<A, B> withSelf(Fn2<? super Fn1<? super A, ? extends B>, ? super A, ? extends B> fn) {
        return a -> fn.apply(withSelf(fn), a);
    }
}
