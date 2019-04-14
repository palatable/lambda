package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
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
 * @see Product4
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple5
 */
public class Tuple4<_1, _2, _3, _4> extends HCons<_1, Tuple3<_2, _3, _4>> implements
        Product4<_1, _2, _3, _4>,
        Monad<_4, Tuple4<_1, _2, _3, ?>>,
        Bifunctor<_3, _4, Tuple4<_1, _2, ?, ?>>,
        Traversable<_4, Tuple4<_1, _2, _3, ?>> {

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

    @Override
    public _1 _1() {
        return _1;
    }

    @Override
    public _2 _2() {
        return _2;
    }

    @Override
    public _3 _3() {
        return _3;
    }

    @Override
    public _4 _4() {
        return _4;
    }

    @Override
    public Tuple4<_2, _3, _4, _1> rotateL4() {
        return tuple(_2, _3, _4, _1);
    }

    @Override
    public Tuple4<_4, _1, _2, _3> rotateR4() {
        return tuple(_4, _1, _2, _3);
    }

    @Override
    public Tuple4<_2, _3, _1, _4> rotateL3() {
        return tuple(_2, _3, _1, _4);
    }

    @Override
    public Tuple4<_3, _1, _2, _4> rotateR3() {
        return tuple(_3, _1, _2, _4);
    }

    @Override
    public Tuple4<_2, _1, _3, _4> invert() {
        return tuple(_2, _1, _3, _4);
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> fmap(Function<? super _4, ? extends _4Prime> fn) {
        return (Tuple4<_1, _2, _3, _4Prime>) Monad.super.<_4Prime>fmap(fn);
    }

    @Override
    public <_3Prime> Tuple4<_1, _2, _3Prime, _4> biMapL(Function<? super _3, ? extends _3Prime> fn) {
        return (Tuple4<_1, _2, _3Prime, _4>) Bifunctor.super.<_3Prime>biMapL(fn);
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> biMapR(Function<? super _4, ? extends _4Prime> fn) {
        return (Tuple4<_1, _2, _3, _4Prime>) Bifunctor.super.<_4Prime>biMapR(fn);
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
    public <_4Prime> Lazy<Tuple4<_1, _2, _3, _4Prime>> lazyZip(
            Lazy<? extends Applicative<Function<? super _4, ? extends _4Prime>, Tuple4<_1, _2, _3, ?>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<_4Prime, Tuple4<_1, _2, _3, ?>>::coerce);
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> discardL(Applicative<_4Prime, Tuple4<_1, _2, _3, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4> discardR(Applicative<_4Prime, Tuple4<_1, _2, _3, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> flatMap(
            Function<? super _4, ? extends Monad<_4Prime, Tuple4<_1, _2, _3, ?>>> f) {
        return pure(f.apply(_4).<Tuple4<_1, _2, _3, _4Prime>>coerce()._4);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_4Prime, App extends Applicative<?, App>, TravB extends Traversable<_4Prime, Tuple4<_1, _2, _3, ?>>,
            AppB extends Applicative<_4Prime, App>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Function<? super _4, ? extends AppB> fn,
                                                                      Function<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(_4).fmap(_4Prime -> fmap(constantly(_4Prime))).<TravB>fmap(Applicative::coerce).coerce();
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
