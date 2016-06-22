package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.HList.HNil;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * A singleton HList.
 *
 * @param <_1> The single slot element type
 * @see HList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public final class Singleton<_1> extends HCons<_1, HNil> implements Functor<_1> {

    Singleton(_1 _1) {
        super(_1, nil());
    }

    @Override
    public <_0> Tuple2<_0, _1> cons(_0 _0) {
        return new Tuple2<>(_0, this);
    }

    @Override
    public <_1Prime> Singleton<_1Prime> fmap(MonadicFunction<? super _1, ? extends _1Prime> fn) {
        return new Singleton<>(fn.apply(head()));
    }
}
