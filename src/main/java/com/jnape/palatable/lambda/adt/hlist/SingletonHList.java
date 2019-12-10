package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.HList.HNil;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.traversable.Traversable;

import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;

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
public class SingletonHList<_1> extends HCons<_1, HNil> implements
        MonadRec<_1, SingletonHList<?>>,
        Traversable<_1, SingletonHList<?>> {

    SingletonHList(_1 _1) {
        super(_1, nil());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_0> Tuple2<_0, _1> cons(_0 _0) {
        return new Tuple2<>(_0, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> SingletonHList<_1Prime> fmap(Fn1<? super _1, ? extends _1Prime> fn) {
        return MonadRec.super.<_1Prime>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> SingletonHList<_1Prime> pure(_1Prime _1Prime) {
        return singletonHList(_1Prime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> SingletonHList<_1Prime> zip(
            Applicative<Fn1<? super _1, ? extends _1Prime>, SingletonHList<?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> Lazy<SingletonHList<_1Prime>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super _1, ? extends _1Prime>, SingletonHList<?>>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(Monad<_1Prime, SingletonHList<?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> SingletonHList<_1Prime> discardL(Applicative<_1Prime, SingletonHList<?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> SingletonHList<_1> discardR(Applicative<_1Prime, SingletonHList<?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> SingletonHList<_1Prime> flatMap(Fn1<? super _1, ? extends Monad<_1Prime, SingletonHList<?>>> f) {
        return f.apply(head()).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> SingletonHList<_1Prime> trampolineM(
            Fn1<? super _1, ? extends MonadRec<RecursiveResult<_1, _1Prime>, SingletonHList<?>>> fn) {
        return fmap(trampoline(head -> fn.apply(head).<SingletonHList<RecursiveResult<_1, _1Prime>>>coerce().head()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, App extends Applicative<?, App>, TravB extends Traversable<B, SingletonHList<?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super _1, ? extends Applicative<B, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(head()).fmap(SingletonHList::new).<TravB>fmap(Applicative::coerce).coerce();
    }

    /**
     * Apply {@link SingletonHList#head()} to <code>fn</code> and return the result.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the head to the function
     */
    public <R> R into(Fn1<? super _1, ? extends R> fn) {
        return fn.apply(head());
    }

    /**
     * The canonical {@link Pure} instance for {@link SingletonHList}.
     *
     * @return the {@link Pure} instance
     */
    public static Pure<SingletonHList<?>> pureSingletonHList() {
        return HList::singletonHList;
    }
}
