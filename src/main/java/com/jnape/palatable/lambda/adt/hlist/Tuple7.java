package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product7;
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
 * A 7-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @param <_4> The fourth slot element type
 * @param <_5> The fifth slot element type
 * @param <_6> The sixth slot element type
 * @param <_7> The seventh slot element type
 * @see Product7
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 * @see Tuple6
 */
public class Tuple7<_1, _2, _3, _4, _5, _6, _7> extends HCons<_1, Tuple6<_2, _3, _4, _5, _6, _7>> implements
        Product7<_1, _2, _3, _4, _5, _6, _7>,
        MonadRec<_7, Tuple7<_1, _2, _3, _4, _5, _6, ?>>,
        Bifunctor<_6, _7, Tuple7<_1, _2, _3, _4, _5, ?, ?>>,
        Traversable<_7, Tuple7<_1, _2, _3, _4, _5, _6, ?>> {

    private final _1 _1;
    private final _2 _2;
    private final _3 _3;
    private final _4 _4;
    private final _5 _5;
    private final _6 _6;
    private final _7 _7;

    Tuple7(_1 _1, Tuple6<_2, _3, _4, _5, _6, _7> tail) {
        super(_1, tail);
        this._1 = _1;
        _2 = tail._1();
        _3 = tail._2();
        _4 = tail._3();
        _5 = tail._4();
        _6 = tail._5();
        _7 = tail._6();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_0> Tuple8<_0, _1, _2, _3, _4, _5, _6, _7> cons(_0 _0) {
        return new Tuple8<>(_0, this);
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
    public _6 _6() {
        return _6;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _7 _7() {
        return _7;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_2, _3, _4, _5, _6, _7, _1> rotateL7() {
        return tuple(_2, _3, _4, _5, _6, _7, _1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_7, _1, _2, _3, _4, _5, _6> rotateR7() {
        return tuple(_7, _1, _2, _3, _4, _5, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_2, _3, _4, _5, _6, _1, _7> rotateL6() {
        return tuple(_2, _3, _4, _5, _6, _1, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_6, _1, _2, _3, _4, _5, _7> rotateR6() {
        return tuple(_6, _1, _2, _3, _4, _5, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_2, _3, _4, _5, _1, _6, _7> rotateL5() {
        return tuple(_2, _3, _4, _5, _1, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_5, _1, _2, _3, _4, _6, _7> rotateR5() {
        return tuple(_5, _1, _2, _3, _4, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_2, _3, _4, _1, _5, _6, _7> rotateL4() {
        return tuple(_2, _3, _4, _1, _5, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_4, _1, _2, _3, _5, _6, _7> rotateR4() {
        return tuple(_4, _1, _2, _3, _5, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_2, _3, _1, _4, _5, _6, _7> rotateL3() {
        return tuple(_2, _3, _1, _4, _5, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_3, _1, _2, _4, _5, _6, _7> rotateR3() {
        return tuple(_3, _1, _2, _4, _5, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple7<_2, _1, _3, _4, _5, _6, _7> invert() {
        return tuple(_2, _1, _3, _4, _5, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime> Tuple7<_1, _2, _3, _4, _5, _6, _7Prime> fmap(Fn1<? super _7, ? extends _7Prime> fn) {
        return MonadRec.super.<_7Prime>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <_6Prime> Tuple7<_1, _2, _3, _4, _5, _6Prime, _7> biMapL(Fn1<? super _6, ? extends _6Prime> fn) {
        return (Tuple7<_1, _2, _3, _4, _5, _6Prime, _7>) Bifunctor.super.biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <_7Prime> Tuple7<_1, _2, _3, _4, _5, _6, _7Prime> biMapR(Fn1<? super _7, ? extends _7Prime> fn) {
        return (Tuple7<_1, _2, _3, _4, _5, _6, _7Prime>) Bifunctor.super.biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime, _7Prime> Tuple7<_1, _2, _3, _4, _5, _6Prime, _7Prime> biMap(
            Fn1<? super _6, ? extends _6Prime> lFn,
            Fn1<? super _7, ? extends _7Prime> rFn) {
        return new Tuple7<>(_1(), tail().biMap(lFn, rFn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime> Tuple7<_1, _2, _3, _4, _5, _6, _7Prime> pure(_7Prime _7Prime) {
        return tuple(_1, _2, _3, _4, _5, _6, _7Prime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime> Tuple7<_1, _2, _3, _4, _5, _6, _7Prime> zip(
            Applicative<Fn1<? super _7, ? extends _7Prime>, Tuple7<_1, _2, _3, _4, _5, _6, ?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime> Lazy<Tuple7<_1, _2, _3, _4, _5, _6, _7Prime>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super _7, ? extends _7Prime>, Tuple7<_1, _2, _3, _4, _5, _6, ?>>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(Monad<_7Prime, Tuple7<_1, _2, _3, _4, _5, _6, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime> Tuple7<_1, _2, _3, _4, _5, _6, _7Prime> discardL(
            Applicative<_7Prime, Tuple7<_1, _2, _3, _4, _5, _6, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime> Tuple7<_1, _2, _3, _4, _5, _6, _7> discardR(
            Applicative<_7Prime, Tuple7<_1, _2, _3, _4, _5, _6, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime> Tuple7<_1, _2, _3, _4, _5, _6, _7Prime> flatMap(
            Fn1<? super _7, ? extends Monad<_7Prime, Tuple7<_1, _2, _3, _4, _5, _6, ?>>> f) {
        return pure(f.apply(_7).<Tuple7<_1, _2, _3, _4, _5, _6, _7Prime>>coerce()._7());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime> Tuple7<_1, _2, _3, _4, _5, _6, _7Prime> trampolineM(
            Fn1<? super _7, ? extends MonadRec<RecursiveResult<_7, _7Prime>, Tuple7<_1, _2, _3, _4, _5, _6, ?>>> fn) {
        return fmap(trampoline(x -> fn.apply(x).<Tuple7<_1, _2, _3, _4, _5, _6, RecursiveResult<_7, _7Prime>>>coerce()
                ._7()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_7Prime, App extends Applicative<?, App>,
            TravB extends Traversable<_7Prime, Tuple7<_1, _2, _3, _4, _5, _6, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Fn1<? super _7, ? extends Applicative<_7Prime, App>> fn,
            Fn1<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(_7).fmap(_7Prime -> fmap(constantly(_7Prime))).<TravB>fmap(Applicative::coerce).coerce();
    }

    /**
     * Given a value of type <code>A</code>, produced an instance of this tuple with each slot set to that value.
     *
     * @param a   the value to fill the tuple with
     * @param <A> the value type
     * @return the filled tuple
     * @see Tuple2#fill
     */
    public static <A> Tuple7<A, A, A, A, A, A, A> fill(A a) {
        return tuple(a, a, a, a, a, a, a);
    }

    /**
     * Return {@link Maybe#just(Object) just} the first seven elements from the given {@link Iterable}, or
     * {@link Maybe#nothing() nothing} if there are less than seven elements.
     *
     * @param as  the {@link Iterable}
     * @param <A> the {@link Iterable} element type
     * @return {@link Maybe} the first seven elements of the given {@link Iterable}
     */
    public static <A> Maybe<Tuple7<A, A, A, A, A, A, A>> fromIterable(Iterable<A> as) {
        return uncons(as).flatMap(Into.into((head, tail) -> Tuple6.fromIterable(tail).fmap(t -> t.cons(head))));
    }

    /**
     * The canonical {@link Pure} instance for {@link Tuple7}.
     *
     * @param _1   the head element
     * @param _2   the second element
     * @param _3   the third element
     * @param _4   the fourth element
     * @param _5   the fifth element
     * @param _6   the sixth element
     * @param <_1> the head element type
     * @param <_2> the second element type
     * @param <_3> the third element type
     * @param <_4> the fourth element type
     * @param <_5> the fifth element type
     * @param <_6> the sixth element type
     * @return the {@link Pure} instance
     */
    public static <_1, _2, _3, _4, _5, _6> Pure<Tuple7<_1, _2, _3, _4, _5, _6, ?>> pureTuple(_1 _1, _2 _2, _3 _3, _4 _4,
                                                                                             _5 _5, _6 _6) {
        return new Pure<Tuple7<_1, _2, _3, _4, _5, _6, ?>>() {
            @Override
            public <_7> Tuple7<_1, _2, _3, _4, _5, _6, _7> checkedApply(_7 _7) throws Throwable {
                return tuple(_1, _2, _3, _4, _5, _6, _7);
            }
        };
    }
}
