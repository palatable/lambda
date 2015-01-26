package com.jnape.palatable.lambda.tuples;

import com.jnape.palatable.lambda.applicative.BiFunctor;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import static java.lang.String.format;

public class Tuple2<_1, _2> implements BiFunctor<_1, _2> {

    public final _1 _1;
    public final _2 _2;

    public Tuple2(_1 _1, _2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    @Override
    public <C, D> Tuple2<C, D> biMap(MonadicFunction<? super _1, ? extends C> f1,
                                     MonadicFunction<? super _2, ? extends D> f2) {
        return tuple(f1.apply(_1), f2.apply(_2));
    }

    @Override
    public <C> Tuple2<C, _2> biMapL(MonadicFunction<? super _1, ? extends C> fn) {
        return (Tuple2<C, _2>) BiFunctor.super.biMapL(fn);
    }

    @Override
    public <C> Tuple2<_1, C> biMapR(MonadicFunction<? super _2, ? extends C> fn) {
        return (Tuple2<_1, C>) BiFunctor.super.biMapR(fn);
    }

    @Override
    public String toString() {
        return format("(%s, %s)", _1, _2);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Tuple2) {
            Tuple2 that = (Tuple2) other;
            return this._1.equals(that._1) && this._2.equals(that._2);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = _1.hashCode();
        return 31 * result + _2.hashCode();
    }

    public static <_1, _2> Tuple2<_1, _2> tuple(_1 _1, _2 _2) {
        return new Tuple2<>(_1, _2);
    }
}
