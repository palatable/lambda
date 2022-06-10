package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiSemigroupFactory;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.internal.Runtime;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn3.LiftA2.liftA2;

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

    private static final Collapse<?, ?> INSTANCE = new Collapse<>();

    private Collapse() {
    }

    @SuppressWarnings("unchecked")
    public static <_1, _2> Collapse<_1, _2> collapse() {
        return (Collapse<_1, _2>) INSTANCE;
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

    @Override
    public Semigroup<Tuple2<_1, _2>> checkedApply(Semigroup<_1> _1Semigroup, Semigroup<_2> _2Semigroup) {
        return new Semigroup<Tuple2<_1, _2>>() {
            @Override
            public Tuple2<_1, _2> checkedApply(Tuple2<_1, _2> x, Tuple2<_1, _2> y) {
                return x.biMap(_1Semigroup.flip().apply(y._1()),
                               _2Semigroup.flip().apply(y._2()));
            }

            @Override
            public Tuple2<_1, _2> foldLeft(Tuple2<_1, _2> tuple2, Iterable<Tuple2<_1, _2>> tuple2s) {
                return tuple(_1Semigroup.foldLeft(tuple2._1(), map(Tuple2::_1, tuple2s)),
                             _2Semigroup.foldLeft(tuple2._2(), map(Tuple2::_2, tuple2s)));
            }

            @Override
            public Lazy<Tuple2<_1, _2>> foldRight(Tuple2<_1, _2> tuple2, Iterable<Tuple2<_1, _2>> tuple2s) {
                return liftA2(Tuple2::tuple,
                              _1Semigroup.foldRight(tuple2._1(), map(Tuple2::_1, tuple2s)),
                              _2Semigroup.foldRight(tuple2._2(), map(Tuple2::_2, tuple2s)));
            }
        };
    }

    @Override
    public Semigroup<Tuple2<_1, _2>> apply(Semigroup<_1> semigroup, Semigroup<_2> semigroup2) {
        try {
            return checkedApply(semigroup, semigroup2);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }

}
