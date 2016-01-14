package com.jnape.palatable.lambda.adt.tuples;

import com.jnape.palatable.lambda.functions.MonadicFunction;

import java.util.Objects;

import static java.lang.String.format;

public class Tuple3<_1, _2, _3> extends Tuple2<_1, _2> {

    public final _3 _3;

    public Tuple3(_1 _1, _2 _2, _3 _3) {
        super(_1, _2);
        this._3 = _3;
    }

    @Override
    public <_2A> Tuple3<_1, _2A, _3> fmap(MonadicFunction<? super _2, ? extends _2A> fn) {
        return (Tuple3<_1, _2A, _3>) super.fmap(fn);
    }

    @Override
    public <_1A, _2A> Tuple3<_1A, _2A, _3> biMap(MonadicFunction<? super _1, ? extends _1A> f1,
                                                 MonadicFunction<? super _2, ? extends _2A> f2) {
        return (Tuple3<_1A, _2A, _3>) super.biMap(f1, f2);
    }

    @Override
    public <_1A> Tuple3<_1A, _2, _3> biMapL(MonadicFunction<? super _1, ? extends _1A> fn) {
        return (Tuple3<_1A, _2, _3>) super.biMapL(fn);
    }

    @Override
    public <_2A> Tuple3<_1, _2A, _3> biMapR(MonadicFunction<? super _2, ? extends _2A> fn) {
        return (Tuple3<_1, _2A, _3>) super.biMapR(fn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        return Objects.equals(_3, tuple3._3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), _3);
    }

    @Override
    public String toString() {
        return format("(%s, %s, %s)", _1, _2, _3);
    }

    public static <_1, _2, _3> Tuple3<_1, _2, _3> tuple(_1 _1, _2 _2, _3 _3) {
        return new Tuple3<>(_1, _2, _3);
    }
}
