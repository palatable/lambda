package com.jnape.palatable.lambda.tuples;

import static java.lang.String.format;

public class Tuple3<_1, _2, _3> extends Tuple2<_1, _2> {

    public final _3 _3;

    public Tuple3(_1 _1, _2 _2, _3 _3) {
        super(_1, _2);
        this._3 = _3;
    }

    @Override
    public String toString() {
        return format("(%s, %s, %s)", _1, _2, _3);
    }

    public static <_1, _2, _3> Tuple3<_1, _2, _3> tuple(_1 _1, _2 _2, _3 _3) {
        return new Tuple3<>(_1, _2, _3);
    }
}
