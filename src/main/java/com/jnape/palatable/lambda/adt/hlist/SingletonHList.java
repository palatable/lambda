package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.HList.HNil;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

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
public class SingletonHList<_1> extends HCons<_1, HNil> implements Monad<_1, SingletonHList>, Traversable<_1, SingletonHList> {

    SingletonHList(_1 _1) {
        super(_1, nil());
    }

    @Override
    public <_0> Tuple2<_0, _1> cons(_0 _0) {
        return new Tuple2<>(_0, this);
    }

    @Override
    public <_1Prime> SingletonHList<_1Prime> fmap(Function<? super _1, ? extends _1Prime> fn) {
        return Monad.super.<_1Prime>fmap(fn).coerce();
    }

    @Override
    public <_1Prime> SingletonHList<_1Prime> pure(_1Prime _1Prime) {
        return singletonHList(_1Prime);
    }

    @Override
    public <_1Prime> SingletonHList<_1Prime> zip(
            Applicative<Function<? super _1, ? extends _1Prime>, SingletonHList> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <_1Prime> SingletonHList<_1Prime> discardL(Applicative<_1Prime, SingletonHList> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <_1Prime> SingletonHList<_1> discardR(Applicative<_1Prime, SingletonHList> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <_1Prime> SingletonHList<_1Prime> flatMap(Function<? super _1, ? extends Monad<_1Prime, SingletonHList>> f) {
        return f.apply(head()).coerce();
    }

    @Override
    public <_1Prime, App extends Applicative> Applicative<SingletonHList<_1Prime>, App> traverse(
            Function<? super _1, ? extends Applicative<_1Prime, App>> fn,
            Function<? super Traversable<_1Prime, SingletonHList>, ? extends Applicative<? extends Traversable<_1Prime, SingletonHList>, App>> pure) {
        return fn.apply(head()).fmap(SingletonHList::new);
    }
}
