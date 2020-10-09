package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Into;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.traversable.Traversable;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Uncons.uncons;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;

/**
 * A 5-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @param <_4> The fourth slot element type
 * @param <_5> The fifth slot element type
 * @see Product5
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 */
public class Tuple5<_1, _2, _3, _4, _5> extends HCons<_1, Tuple4<_2, _3, _4, _5>> implements
        Product5<_1, _2, _3, _4, _5>,
        MonadRec<_5, Tuple5<_1, _2, _3, _4, ?>>,
        Bifunctor<_4, _5, Tuple5<_1, _2, _3, ?, ?>>,
        Traversable<_5, Tuple5<_1, _2, _3, _4, ?>> {

    private final _1 _1;
    private final _2 _2;
    private final _3 _3;
    private final _4 _4;
    private final _5 _5;

    Tuple5(_1 _1, Tuple4<_2, _3, _4, _5> tail) {
        super(_1, tail);
        this._1 = _1;
        _2      = tail._1();
        _3      = tail._2();
        _4      = tail._3();
        _5      = tail._4();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_0> Tuple6<_0, _1, _2, _3, _4, _5> cons(_0 _0) {
        return new Tuple6<>(_0, this);
    }

    /**
     * Snoc an element onto the back of this {@link Tuple5}.
     *
     * @param _6   the new last element
     * @param <_6> the new last element type
     * @return the new {@link Tuple6}
     */
    public <_6> Tuple6<_1, _2, _3, _4, _5, _6> snoc(_6 _6) {
        return tuple(_1, _2, _3, _4, _5, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _1 _1() {
        return _1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _2 _2() {
        return _2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _3 _3() {
        return _3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _4 _4() {
        return _4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _5 _5() {
        return _5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple5<_2, _3, _4, _5, _1> rotateL5() {
        return tuple(_2, _3, _4, _5, _1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple5<_5, _1, _2, _3, _4> rotateR5() {
        return tuple(_5, _1, _2, _3, _4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple5<_2, _3, _4, _1, _5> rotateL4() {
        return tuple(_2, _3, _4, _1, _5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple5<_4, _1, _2, _3, _5> rotateR4() {
        return tuple(_4, _1, _2, _3, _5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple5<_2, _3, _1, _4, _5> rotateL3() {
        return tuple(_2, _3, _1, _4, _5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple5<_3, _1, _2, _4, _5> rotateR3() {
        return tuple(_3, _1, _2, _4, _5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple5<_2, _1, _3, _4, _5> invert() {
        return tuple(_2, _1, _3, _4, _5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> fmap(Fn1<? super _5, ? extends _5Prime> fn) {
        return MonadRec.super.<_5Prime>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_4Prime> Tuple5<_1, _2, _3, _4Prime, _5> biMapL(Fn1<? super _4, ? extends _4Prime> fn) {
        return (Tuple5<_1, _2, _3, _4Prime, _5>) Bifunctor.super.<_4Prime>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> biMapR(Fn1<? super _5, ? extends _5Prime> fn) {
        return (Tuple5<_1, _2, _3, _4, _5Prime>) Bifunctor.super.<_5Prime>biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_4Prime, _5Prime> Tuple5<_1, _2, _3, _4Prime, _5Prime> biMap(Fn1<? super _4, ? extends _4Prime> lFn,
                                                                         Fn1<? super _5, ? extends _5Prime> rFn) {
        return new Tuple5<>(_1(), tail().biMap(lFn, rFn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> pure(_5Prime _5Prime) {
        return tuple(_1, _2, _3, _4, _5Prime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> zip(
            Applicative<Fn1<? super _5, ? extends _5Prime>, Tuple5<_1, _2, _3, _4, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Lazy<Tuple5<_1, _2, _3, _4, _5Prime>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super _5, ? extends _5Prime>, Tuple5<_1, _2, _3, _4, ?>>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(Monad<_5Prime, Tuple5<_1, _2, _3, _4, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> discardL(Applicative<_5Prime, Tuple5<_1, _2, _3, _4, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5> discardR(Applicative<_5Prime, Tuple5<_1, _2, _3, _4, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> flatMap(
            Fn1<? super _5, ? extends Monad<_5Prime, Tuple5<_1, _2, _3, _4, ?>>> f) {
        return pure(f.apply(_5).<Tuple5<_1, _2, _3, _4, _5Prime>>coerce()._5());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> trampolineM(
            Fn1<? super _5, ? extends MonadRec<RecursiveResult<_5, _5Prime>, Tuple5<_1, _2, _3, _4, ?>>> fn) {
        return fmap(trampoline(x -> fn.apply(x).<Tuple5<_1, _2, _3, _4, RecursiveResult<_5, _5Prime>>>coerce()._5()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime, App extends Applicative<?, App>, TravB extends Traversable<_5Prime, Tuple5<_1, _2, _3, _4, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Fn1<? super _5, ? extends Applicative<_5Prime, App>> fn,
            Fn1<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(_5).fmap(_3Prime -> fmap(constantly(_3Prime))).<TravB>fmap(Applicative::coerce).coerce();
    }

    /**
     * Returns a <code>{@link Tuple4}&lt;_1, _2, _3, _4&gt;</code> of all the elements of this
     * <code>{@link Tuple5}&lt;_1, _2, _3, _4, _5&gt;</code> except the last.
     *
     * @return The {@link Tuple4}&lt;_1, _2, _3, _4&gt; representing all but the last element
     */
    public Tuple4<_1, _2, _3, _4> init() {
        return rotateR5().tail();
    }

    /**
     * Given a value of type <code>A</code>, produced an instance of this tuple with each slot set to that value.
     *
     * @param a   the value to fill the tuple with
     * @param <A> the value type
     * @return the filled tuple
     * @see Tuple2#fill
     */
    public static <A> Tuple5<A, A, A, A, A> fill(A a) {
        return tuple(a, a, a, a, a);
    }

    /**
     * Return {@link Maybe#just(Object) just} the first five elements from the given {@link Iterable}, or
     * {@link Maybe#nothing() nothing} if there are less than five elements.
     *
     * @param as  the {@link Iterable}
     * @param <A> the {@link Iterable} element type
     * @return {@link Maybe} the first five elements of the given {@link Iterable}
     */
    public static <A> Maybe<Tuple5<A, A, A, A, A>> fromIterable(Iterable<A> as) {
        return uncons(as).flatMap(Into.into((head, tail) -> Tuple4.fromIterable(tail).fmap(t -> t.cons(head))));
    }

    /**
     * The canonical {@link Pure} instance for {@link Tuple5}.
     *
     * @param _1   the head element
     * @param _2   the second element
     * @param _3   the third element
     * @param _4   the fourth element
     * @param <_1> the head element type
     * @param <_2> the second element type
     * @param <_3> the third element type
     * @param <_4> the fourth element type
     * @return the {@link Pure} instance
     */
    public static <_1, _2, _3, _4> Pure<Tuple5<_1, _2, _3, _4, ?>> pureTuple(_1 _1, _2 _2, _3 _3, _4 _4) {
        return new Pure<Tuple5<_1, _2, _3, _4, ?>>() {
            @Override
            public <_5> Tuple5<_1, _2, _3, _4, _5> checkedApply(_5 _5) throws Throwable {
                return tuple(_1, _2, _3, _4, _5);
            }
        };
    }
}
