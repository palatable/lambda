package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Apply;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Bind;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Map;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A 2-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @see Product2
 * @see HList
 * @see SingletonHList
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public class Tuple2<_1, _2> extends HCons<_1, SingletonHList<_2>> implements
        Product2<_1, _2>,
        Map.Entry<_1, _2>,
        Monad<_2, Tuple2<_1, ?>>,
        Bifunctor<_1, _2, Tuple2>,
        Traversable<_2, Tuple2<_1, ?>> {

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

    @Override
    public _1 _1() {
        return _1;
    }

    @Override
    public _2 _2() {
        return _2;
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
        return Monad.super.<_2Prime>fmap(fn).coerce();
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

    @Override
    public <_2Prime> Tuple2<_1, _2Prime> pure(_2Prime _2Prime) {
        return tuple(_1, _2Prime);
    }

    @Override
    public <_2Prime> Tuple2<_1, _2Prime> zip(
            Apply<Function<? super _2, ? extends _2Prime>, Tuple2<_1, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <_2Prime> Tuple2<_1, _2Prime> discardL(Applicative<_2Prime, Tuple2<_1, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <_2Prime> Tuple2<_1, _2> discardR(Applicative<_2Prime, Tuple2<_1, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <_2Prime> Tuple2<_1, _2Prime> flatMap(Function<? super _2, ? extends Bind<_2Prime, Tuple2<_1, ?>>> f) {
        return pure(f.apply(_2).<Tuple2<_1, _2Prime>>coerce()._2());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_2Prime, App extends Applicative, TravB extends Traversable<_2Prime, Tuple2<_1, ?>>, AppB extends Applicative<_2Prime, App>, AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Function<? super _2, ? extends AppB> fn, Function<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(_2).fmap(_2Prime -> fmap(constantly(_2Prime))).<TravB>fmap(Applicative::coerce).coerce();
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

    /**
     * Given a value of type <code>A</code>, produce an instance of this tuple with each slot set to that value.
     *
     * @param a   the value to fill the tuple with
     * @param <A> the value type
     * @return the filled tuple
     */
    public static <A> Tuple2<A, A> fill(A a) {
        return tuple(a, a);
    }
}
