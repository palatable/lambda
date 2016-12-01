package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiSemigroupFactory;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

/**
 * A {@link Semigroup} instance formed by a <code>{@link Tuple2}&lt;_1, _2&gt;</code> and semigroups over
 * <code>_1</code> and <code>_2</code>. Successively collapses multiple {@link Tuple2}s into a single {@link Tuple2} by
 * collapsing the values of each slot under the provided semigroup instance.
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.Collapse}.
 *
 * @param <_1> the first slot parameter type
 * @param <_2> the second slot parameter type
 * @see Semigroup
 * @see Tuple2
 */
public final class Collapse<_1, _2> implements BiSemigroupFactory<Semigroup<_1>, Semigroup<_2>, Tuple2<_1, _2>> {

    private static final Collapse INSTANCE = new Collapse();

    private Collapse() {
    }

    @Override
    public Semigroup<Tuple2<_1, _2>> apply(Semigroup<_1> _1Semigroup, Semigroup<_2> _2Semigroup) {
        return (x, y) -> x.biMap(_1Semigroup.flip().apply(y._1()),
                                 _2Semigroup.flip().apply(y._2()));
    }

    @SuppressWarnings("unchecked")
    public static <_1, _2> Collapse<_1, _2> collapse() {
        return INSTANCE;
    }

    public static <_1, _2> SemigroupFactory<Semigroup<_2>, Tuple2<_1, _2>> collapse(Semigroup<_1> _1Semigroup) {
        return Collapse.<_1, _2>collapse().apply(_1Semigroup);
    }

    public static <_1, _2> Semigroup<Tuple2<_1, _2>> collapse(Semigroup<_1> _1Semigroup, Semigroup<_2> _2Semigroup) {
        return Collapse.<_1, _2>collapse(_1Semigroup).apply(_2Semigroup);
    }

    public static <_1, _2> Fn1<Tuple2<_1, _2>, Tuple2<_1, _2>> collapse(Semigroup<_1> _1Semigroup,
                                                                        Semigroup<_2> _2Semigroup,
                                                                        Tuple2<_1, _2> x) {
        return collapse(_1Semigroup, _2Semigroup).apply(x);
    }

    public static <_1, _2> Tuple2<_1, _2> collapse(Semigroup<_1> _1Semigroup, Semigroup<_2> _2Semigroup,
                                                   Tuple2<_1, _2> x,
                                                   Tuple2<_1, _2> y) {
        return collapse(_1Semigroup, _2Semigroup, x).apply(y);
    }
}
