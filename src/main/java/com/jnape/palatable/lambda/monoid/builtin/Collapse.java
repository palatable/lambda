package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiMonoidFactory;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

/**
 * A {@link Monoid} instance formed by a <code>{@link Tuple2}&lt;_1, _2&gt;</code> and monoids over <code>_1</code> and
 * <code>_2</code>. Successively collapses multiple {@link Tuple2}s into a single {@link Tuple2} by collapsing the
 * values of each slot under the provided monoid instance.
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.Collapse}.
 *
 * @param <_1> the first slot parameter type
 * @param <_2> the second slot parameter type
 * @see Monoid
 * @see Tuple2
 */
public final class Collapse<_1, _2> implements BiMonoidFactory<Monoid<_1>, Monoid<_2>, Tuple2<_1, _2>> {

    private static final Collapse<?, ?> INSTANCE = new Collapse<>();

    private Collapse() {
    }

    @Override
    public Monoid<Tuple2<_1, _2>> checkedApply(Monoid<_1> _1Monoid, Monoid<_2> _2Monoid) {
        return Monoid.<Tuple2<_1, _2>>monoid(
                com.jnape.palatable.lambda.semigroup.builtin.Collapse.collapse(_1Monoid, _2Monoid),
                () -> tuple(_1Monoid.identity(), _2Monoid.identity()));
    }

    @SuppressWarnings("unchecked")
    public static <_1, _2> Collapse<_1, _2> collapse() {
        return (Collapse<_1, _2>) INSTANCE;
    }

    public static <_1, _2> MonoidFactory<Monoid<_2>, Tuple2<_1, _2>> collapse(Monoid<_1> _1Monoid) {
        return Collapse.<_1, _2>collapse().apply(_1Monoid);
    }

    public static <_1, _2> Monoid<Tuple2<_1, _2>> collapse(Monoid<_1> _1Monoid, Monoid<_2> _2Monoid) {
        return Collapse.<_1, _2>collapse(_1Monoid).apply(_2Monoid);
    }

    public static <_1, _2> Fn1<Tuple2<_1, _2>, Tuple2<_1, _2>> collapse(Monoid<_1> _1Monoid, Monoid<_2> _2Monoid,
                                                                        Tuple2<_1, _2> x) {
        return collapse(_1Monoid, _2Monoid).apply(x);
    }

    public static <_1, _2> Tuple2<_1, _2> collapse(Monoid<_1> _1Monoid, Monoid<_2> _2Monoid, Tuple2<_1, _2> x,
                                                   Tuple2<_1, _2> y) {
        return collapse(_1Monoid, _2Monoid, x).apply(y);
    }
}
