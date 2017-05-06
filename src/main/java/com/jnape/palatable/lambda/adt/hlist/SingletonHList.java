package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.HList.HNil;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.Function;

/**
 * A singleton HList. Supports random access.
 *
 * @param <_1> The single slot element type
 * @see HList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public class SingletonHList<_1> extends HCons<_1, HNil> implements Applicative<_1, SingletonHList> {

    SingletonHList(_1 _1) {
        super(_1, nil());
    }

    @Override
    public <_0> Tuple2<_0, _1> cons(_0 _0) {
        return new Tuple2<>(_0, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_1Prime> SingletonHList<_1Prime> fmap(Function<? super _1, ? extends _1Prime> fn) {
        return (SingletonHList<_1Prime>) Applicative.super.fmap(fn);
    }

    @Override
    public <_1Prime> SingletonHList<_1Prime> pure(_1Prime _1Prime) {
        return singletonHList(_1Prime);
    }

    @Override
    public <_1Prime> SingletonHList<_1Prime> zip(
            Applicative<Function<? super _1, ? extends _1Prime>, SingletonHList> appFn) {
        return new SingletonHList<>(appFn.<SingletonHList<Function<? super _1, ? extends _1Prime>>>coerce()
                                            .head()
                                            .apply(head()));
    }

    @Override
    public <_1Prime> SingletonHList<_1Prime> discardL(Applicative<_1Prime, SingletonHList> appB) {
        return Applicative.super.discardL(appB).coerce();
    }

    @Override
    public <_1Prime> SingletonHList<_1> discardR(Applicative<_1Prime, SingletonHList> appB) {
        return Applicative.super.discardR(appB).coerce();
    }
}
