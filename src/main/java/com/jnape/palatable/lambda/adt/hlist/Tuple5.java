package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A 5-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @param <_4> The fourth slot element type
 * @param <_5> The fifth slot element type
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 */
public class Tuple5<_1, _2, _3, _4, _5> extends HCons<_1, Tuple4<_2, _3, _4, _5>>
        implements Monad<_5, Tuple5<_1, _2, _3, _4, ?>>, Bifunctor<_4, _5, Tuple5<_1, _2, _3, ?, ?>>, Traversable<_5, Tuple5<_1, _2, _3, _4, ?>> {
    private final _1 _1;
    private final _2 _2;
    private final _3 _3;
    private final _4 _4;
    private final _5 _5;

    Tuple5(_1 _1, Tuple4<_2, _3, _4, _5> tail) {
        super(_1, tail);
        this._1 = _1;
        _2 = tail._1();
        _3 = tail._2();
        _4 = tail._3();
        _5 = tail._4();
    }

    @Override
    public <_0> Tuple6<_0, _1, _2, _3, _4, _5> cons(_0 _0) {
        return new Tuple6<>(_0, this);
    }

    /**
     * Retrieve the first (head) element in constant time.
     *
     * @return the head element
     */
    public _1 _1() {
        return _1;
    }

    /**
     * Retrieve the second element in constant time.
     *
     * @return the second element
     */
    public _2 _2() {
        return _2;
    }

    /**
     * Retrieve the third element in constant time.
     *
     * @return the third element
     */
    public _3 _3() {
        return _3;
    }

    /**
     * Retrieve the fourth element in constant time.
     *
     * @return the fourth element
     */
    public _4 _4() {
        return _4;
    }

    /**
     * Retrieve the fifth element in constant time.
     *
     * @return the fifth element
     */
    public _5 _5() {
        return _5;
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> fmap(Function<? super _5, ? extends _5Prime> fn) {
        return Monad.super.<_5Prime>fmap(fn).coerce();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_4Prime> Tuple5<_1, _2, _3, _4Prime, _5> biMapL(Function<? super _4, ? extends _4Prime> fn) {
        return (Tuple5<_1, _2, _3, _4Prime, _5>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> biMapR(Function<? super _5, ? extends _5Prime> fn) {
        return (Tuple5<_1, _2, _3, _4, _5Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_4Prime, _5Prime> Tuple5<_1, _2, _3, _4Prime, _5Prime> biMap(Function<? super _4, ? extends _4Prime> lFn,
                                                                         Function<? super _5, ? extends _5Prime> rFn) {
        return new Tuple5<>(_1(), tail().biMap(lFn, rFn));
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> pure(_5Prime _5Prime) {
        return tuple(_1, _2, _3, _4, _5Prime);
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> zip(
            Applicative<Function<? super _5, ? extends _5Prime>, Tuple5<_1, _2, _3, _4, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> discardL(Applicative<_5Prime, Tuple5<_1, _2, _3, _4, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5> discardR(Applicative<_5Prime, Tuple5<_1, _2, _3, _4, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> flatMap(
            Function<? super _5, ? extends Monad<_5Prime, Tuple5<_1, _2, _3, _4, ?>>> f) {
        return pure(f.apply(_5).<Tuple5<_1, _2, _3, _4, _5Prime>>coerce()._5());
    }

    @Override
    public <_5Prime, App extends Applicative> Applicative<Tuple5<_1, _2, _3, _4, _5Prime>, App> traverse(
            Function<? super _5, ? extends Applicative<_5Prime, App>> fn,
            Function<? super Traversable<_5Prime, Tuple5<_1, _2, _3, _4, ?>>, ? extends Applicative<? extends Traversable<_5Prime, Tuple5<_1, _2, _3, _4, ?>>, App>> pure) {
        return fn.apply(_5).fmap(_5Prime -> fmap(constantly(_5Prime)));
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
}
