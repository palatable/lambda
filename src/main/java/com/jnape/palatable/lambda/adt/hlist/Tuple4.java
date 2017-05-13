package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A 4-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @param <_4> The fourth slot element type
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple5
 */
public class Tuple4<_1, _2, _3, _4> extends HCons<_1, Tuple3<_2, _3, _4>>
        implements Applicative<_4, Tuple4<_1, _2, _3, ?>>, Bifunctor<_3, _4, Tuple4<_1, _2, ?, ?>>, Traversable<_4, Tuple4<_1, _2, _3, ?>> {
    private final _1 _1;
    private final _2 _2;
    private final _3 _3;
    private final _4 _4;

    Tuple4(_1 _1, Tuple3<_2, _3, _4> tail) {
        super(_1, tail);
        this._1 = _1;
        _2 = tail._1();
        _3 = tail._2();
        _4 = tail._3();
    }

    @Override
    public <_0> Tuple5<_0, _1, _2, _3, _4> cons(_0 _0) {
        return new Tuple5<>(_0, this);
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
     * Destructure and apply this tuple to a function accepting the same number of arguments as this tuple's
     * slots.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured tuple to the function
     * @see Tuple2#into
     */
    public <R> R into(Fn4<? super _1, ? super _2, ? super _3, ? super _4, ? extends R> fn) {
        return fn.apply(_1, _2, _3, _4);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> fmap(Function<? super _4, ? extends _4Prime> fn) {
        return (Tuple4<_1, _2, _3, _4Prime>) Applicative.super.fmap(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_3Prime> Tuple4<_1, _2, _3Prime, _4> biMapL(Function<? super _3, ? extends _3Prime> fn) {
        return (Tuple4<_1, _2, _3Prime, _4>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> biMapR(Function<? super _4, ? extends _4Prime> fn) {
        return (Tuple4<_1, _2, _3, _4Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_3Prime, _4Prime> Tuple4<_1, _2, _3Prime, _4Prime> biMap(Function<? super _3, ? extends _3Prime> lFn,
                                                                     Function<? super _4, ? extends _4Prime> rFn) {
        return new Tuple4<>(_1(), tail().biMap(lFn, rFn));
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> pure(_4Prime _4Prime) {
        return tuple(_1, _2, _3, _4Prime);
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> zip(
            Applicative<Function<? super _4, ? extends _4Prime>, Tuple4<_1, _2, _3, ?>> appFn) {
        return biMapR(appFn.<Tuple4<_1, _2, _3, Function<? super _4, ? extends _4Prime>>>coerce()._4());
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> discardL(Applicative<_4Prime, Tuple4<_1, _2, _3, ?>> appB) {
        return Applicative.super.discardL(appB).coerce();
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4> discardR(Applicative<_4Prime, Tuple4<_1, _2, _3, ?>> appB) {
        return Applicative.super.discardR(appB).coerce();
    }

    @Override
    public <_4Prime, App extends Applicative> Applicative<Tuple4<_1, _2, _3, _4Prime>, App> traverse(
            Function<? super _4, ? extends Applicative<_4Prime, App>> fn,
            Function<? super Traversable<_4Prime, Tuple4<_1, _2, _3, ?>>, ? extends Applicative<? extends Traversable<_4Prime, Tuple4<_1, _2, _3, ?>>, App>> pure) {
        return fn.apply(_4).fmap(_4Prime -> fmap(constantly(_4Prime)));
    }

    /**
     * Given a value of type <code>A</code>, produced an instance of this tuple with each slot set to that value.
     *
     * @param a   the value to fill the tuple with
     * @param <A> the value type
     * @return the filled tuple
     * @see Tuple2#fill
     */
    public static <A> Tuple4<A, A, A, A> fill(A a) {
        return tuple(a, a, a, a);
    }
}
