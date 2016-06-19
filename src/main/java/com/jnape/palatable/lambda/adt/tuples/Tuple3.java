package com.jnape.palatable.lambda.adt.tuples;

import com.jnape.palatable.lambda.functions.MonadicFunction;

import java.util.Objects;

import static java.lang.String.format;

/**
 * The ternary tuple product type. This class extends <code>{@link Tuple2}</code> and as such embodies the same
 * functorial properties.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 */
public class Tuple3<_1, _2, _3> extends Tuple2<_1, _2> {

    public final _3 _3;

    Tuple3(_1 _1, _2 _2, _3 _3) {
        super(_1, _2);
        this._3 = _3;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_2A> Tuple3<_1, _2A, _3> fmap(MonadicFunction<? super _2, ? extends _2A> fn) {
        return (Tuple3<_1, _2A, _3>) super.fmap(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_1A, _2A> Tuple3<_1A, _2A, _3> biMap(MonadicFunction<? super _1, ? extends _1A> f1,
                                                 MonadicFunction<? super _2, ? extends _2A> f2) {
        return (Tuple3<_1A, _2A, _3>) super.biMap(f1, f2);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_1A> Tuple3<_1A, _2, _3> biMapL(MonadicFunction<? super _1, ? extends _1A> fn) {
        return (Tuple3<_1A, _2, _3>) super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_2A> Tuple3<_1, _2A, _3> biMapR(MonadicFunction<? super _2, ? extends _2A> fn) {
        return (Tuple3<_1, _2A, _3>) super.biMapR(fn);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Tuple3) {
            Tuple3 that = (Tuple3) other;
            return super.equals(other)
                    && Objects.equals(this._3, that._3);
        }
        return false;
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
