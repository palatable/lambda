package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product3;
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
 * A 3-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @see Product3
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple4
 * @see Tuple5
 */
public class Tuple3<_1, _2, _3> extends HCons<_1, Tuple2<_2, _3>> implements
        Product3<_1, _2, _3>,
        MonadRec<_3, Tuple3<_1, _2, ?>>,
        Bifunctor<_2, _3, Tuple3<_1, ?, ?>>,
        Traversable<_3, Tuple3<_1, _2, ?>> {

    private final _1 _1;
    private final _2 _2;
    private final _3 _3;

    Tuple3(_1 _1, Tuple2<_2, _3> tail) {
        super(_1, tail);
        this._1 = _1;
        _2 = tail._1();
        _3 = tail._2();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_0> Tuple4<_0, _1, _2, _3> cons(_0 _0) {
        return new Tuple4<>(_0, this);
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
    public Tuple3<_2, _3, _1> rotateL3() {
        return tuple(_2, _3, _1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3<_3, _1, _2> rotateR3() {
        return tuple(_3, _1, _2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3<_2, _1, _3> invert() {
        return tuple(_2, _1, _3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <_3Prime> Tuple3<_1, _2, _3Prime> fmap(Fn1<? super _3, ? extends _3Prime> fn) {
        return (Tuple3<_1, _2, _3Prime>) MonadRec.super.fmap(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <_2Prime> Tuple3<_1, _2Prime, _3> biMapL(Fn1<? super _2, ? extends _2Prime> fn) {
        return (Tuple3<_1, _2Prime, _3>) Bifunctor.super.biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <_3Prime> Tuple3<_1, _2, _3Prime> biMapR(Fn1<? super _3, ? extends _3Prime> fn) {
        return (Tuple3<_1, _2, _3Prime>) Bifunctor.super.biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime, _3Prime> Tuple3<_1, _2Prime, _3Prime> biMap(Fn1<? super _2, ? extends _2Prime> lFn,
                                                                 Fn1<? super _3, ? extends _3Prime> rFn) {
        return new Tuple3<>(_1(), tail().biMap(lFn, rFn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3Prime> Tuple3<_1, _2, _3Prime> pure(_3Prime _3Prime) {
        return tuple(_1, _2, _3Prime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3Prime> Tuple3<_1, _2, _3Prime> zip(
            Applicative<Fn1<? super _3, ? extends _3Prime>, Tuple3<_1, _2, ?>> appFn) {
        return biMapR(appFn.<Tuple3<_1, _2, Fn1<? super _3, ? extends _3Prime>>>coerce()._3()::apply);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3Prime> Lazy<Tuple3<_1, _2, _3Prime>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super _3, ? extends _3Prime>, Tuple3<_1, _2, ?>>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(Monad<_3Prime, Tuple3<_1, _2, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3Prime> Tuple3<_1, _2, _3Prime> discardL(Applicative<_3Prime, Tuple3<_1, _2, ?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3Prime> Tuple3<_1, _2, _3> discardR(Applicative<_3Prime, Tuple3<_1, _2, ?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3Prime> Tuple3<_1, _2, _3Prime> flatMap(
            Fn1<? super _3, ? extends Monad<_3Prime, Tuple3<_1, _2, ?>>> f) {
        return pure(f.apply(_3).<Tuple3<_1, _2, _3Prime>>coerce()._3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3Prime> Tuple3<_1, _2, _3Prime> trampolineM(
            Fn1<? super _3, ? extends MonadRec<RecursiveResult<_3, _3Prime>, Tuple3<_1, _2, ?>>> fn) {
        return fmap(trampoline(x -> fn.apply(x).<Tuple3<_1, _2, RecursiveResult<_3, _3Prime>>>coerce()._3()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3Prime, App extends Applicative<?, App>, TravB extends Traversable<_3Prime, Tuple3<_1, _2, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Fn1<? super _3, ? extends Applicative<_3Prime, App>> fn,
            Fn1<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(_3).fmap(_3Prime -> fmap(constantly(_3Prime))).<TravB>fmap(Applicative::coerce).coerce();
    }

    /**
     * Given a value of type <code>A</code>, produced an instance of this tuple with each slot set to that value.
     *
     * @param a   the value to fill the tuple with
     * @param <A> the value type
     * @return the filled tuple
     * @see Tuple2#fill
     */
    public static <A> Tuple3<A, A, A> fill(A a) {
        return tuple(a, a, a);
    }

    /**
     * Return {@link Maybe#just(Object) just} the first three elements from the given {@link Iterable}, or
     * {@link Maybe#nothing() nothing} if there are less than three elements.
     *
     * @param as  the {@link Iterable}
     * @param <A> the {@link Iterable} element type
     * @return {@link Maybe} the first three elements of the given {@link Iterable}
     */
    public static <A> Maybe<Tuple3<A, A, A>> fromIterable(Iterable<A> as) {
        return uncons(as).flatMap(Into.into((head, tail) -> Tuple2.fromIterable(tail).fmap(t -> t.cons(head))));
    }

    /**
     * The canonical {@link Pure} instance for {@link Tuple3}.
     *
     * @param _1   the head element
     * @param _2   the second element
     * @param <_1> the head element type
     * @param <_2> the second element type
     * @return the {@link Pure} instance
     */
    public static <_1, _2> Pure<Tuple3<_1, _2, ?>> pureTuple(_1 _1, _2 _2) {
        return new Pure<Tuple3<_1, _2, ?>>() {
            @Override
            public <_3> Tuple3<_1, _2, _3> checkedApply(_3 _3) throws Throwable {
                return tuple(_1, _2, _3);
            }
        };
    }
}
