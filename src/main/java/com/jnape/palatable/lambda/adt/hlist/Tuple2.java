package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A 2-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @see HList
 * @see SingletonHList
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public class Tuple2<_1, _2> extends HCons<_1, SingletonHList<_2>> implements Map.Entry<_1, _2>, Functor<_2>, Bifunctor<_1, _2> {

    private final _1 _1;
    private final _2 _2;

    Tuple2(_1 _1, SingletonHList<_2> tail) {
        super(_1, tail);
        this._1 = _1;
        _2 = tail.head();
    }

    @Override
    public <_0> Tuple3<_0, _1, _2> cons(_0 _0) {
        return new Tuple3<>(_0, this);
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
     * Destructure and apply this tuple to a function accepting the same number of arguments as this tuple's
     * slots. This can be thought of as a kind of dual to uncurrying a function and applying a tuple to it.
     *
     * @param fn the function to apply
     * @param <R>        the return type of the function
     * @return the result of applying the destructured tuple to the function
     */
    public <R> R into(BiFunction<? super _1, ? super _2, ? extends R> fn) {
        return fn.apply(_1, _2);
    }

    @Override
    public _1 getKey() {
        return _1();
    }

    @Override
    public _2 getValue() {
        return _2();
    }

    @Override
    public _2 setValue(_2 value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <_2Prime> Tuple2<_1, _2Prime> fmap(Function<? super _2, ? extends _2Prime> fn) {
        return tuple(_1(), fn.apply(_2()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_1Prime> Tuple2<_1Prime, _2> biMapL(Function<? super _1, ? extends _1Prime> fn) {
        return (Tuple2<_1Prime, _2>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_2Prime> Tuple2<_1, _2Prime> biMapR(Function<? super _2, ? extends _2Prime> fn) {
        return (Tuple2<_1, _2Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_1Prime, _2Prime> Tuple2<_1Prime, _2Prime> biMap(Function<? super _1, ? extends _1Prime> lFn,
                                                             Function<? super _2, ? extends _2Prime> rFn) {
        return new Tuple2<>(lFn.apply(_1()), tail().fmap(rFn));
    }

    /**
     * Static factory method for creating <code>Tuple2</code>s from {@link java.util.Map.Entry}s.
     *
     * @param entry the map entry
     * @param <K>   the key parameter type, and first (head) element type
     * @param <V>   the value parameter type, and second element type
     * @return the newly created Tuple2
     */
    public static <K, V> Tuple2<K, V> fromEntry(Map.Entry<K, V> entry) {
        return new Tuple2<>(entry.getKey(), singletonHList(entry.getValue()));
    }

}
