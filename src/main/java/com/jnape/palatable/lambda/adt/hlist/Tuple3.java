package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;

import java.util.function.Function;

/**
 * A 3-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple4
 * @see Tuple5
 */
public class Tuple3<_1, _2, _3> extends HCons<_1, Tuple2<_2, _3>>
        implements Applicative<_3, Tuple3<_1, _2, ?>>, Bifunctor<_2, _3, Tuple3<_1, ?, ?>> {
    private final _1 _1;
    private final _2 _2;
    private final _3 _3;

    Tuple3(_1 _1, Tuple2<_2, _3> tail) {
        super(_1, tail);
        this._1 = _1;
        _2 = tail._1();
        _3 = tail._2();
    }

    @Override
    public <_0> Tuple4<_0, _1, _2, _3> cons(_0 _0) {
        return new Tuple4<>(_0, this);
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
     * Destructure and apply this tuple to a function accepting the same number of arguments as this tuple's
     * slots.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured tuple to the function
     * @see Tuple2#into
     */
    public <R> R into(Fn3<? super _1, ? super _2, ? super _3, ? extends R> fn) {
        return fn.apply(_1, _2, _3);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_3Prime> Tuple3<_1, _2, _3Prime> fmap(Function<? super _3, ? extends _3Prime> fn) {
        return (Tuple3<_1, _2, _3Prime>) Applicative.super.fmap(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_2Prime> Tuple3<_1, _2Prime, _3> biMapL(Function<? super _2, ? extends _2Prime> fn) {
        return (Tuple3<_1, _2Prime, _3>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_3Prime> Tuple3<_1, _2, _3Prime> biMapR(Function<? super _3, ? extends _3Prime> fn) {
        return (Tuple3<_1, _2, _3Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_2Prime, _3Prime> Tuple3<_1, _2Prime, _3Prime> biMap(Function<? super _2, ? extends _2Prime> lFn,
                                                                 Function<? super _3, ? extends _3Prime> rFn) {
        return new Tuple3<>(_1(), tail().biMap(lFn, rFn));
    }

    @Override
    public <_3Prime> Tuple3<_1, _2, _3Prime> pure(_3Prime _3Prime) {
        return tuple(_1, _2, _3Prime);
    }

    @Override
    public <_3Prime> Tuple3<_1, _2, _3Prime> zip(
            Applicative<Function<? super _3, ? extends _3Prime>, Tuple3<_1, _2, ?>> appFn) {
        return biMapR(appFn.<Tuple3<_1, _2, Function<? super _3, ? extends _3Prime>>>coerce()._3());
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
}
