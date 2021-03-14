package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight;
import com.jnape.palatable.lambda.functions.builtin.fn3.LiftA2;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * A {@link Semigroup} instance formed by <code>{@link Maybe}&lt;A&gt;</code> and a semigroup over <code>A</code>. The
 * application to two {@link Maybe} values is absence-biased, such that for a given {@link Maybe} <code>x</code>
 * and <code>y</code>:
 * <ul>
 * <li> if <code>x</code> is absent, the result is <code>x</code></li>
 * <li> if <code>x</code> is present and <code>y</code> is absent, the result is <code>y</code></li>
 * <li> if both <code>x</code> and <code>y</code> are present, the result is the application of the x and y values in
 * terms of the provided semigroup, wrapped in {@link Maybe#just}</li>
 * </ul>
 *
 * @param <A> the Maybe value parameter type
 * @see Semigroup
 * @see Present
 * @see Maybe
 */
public final class Absent<A> implements SemigroupFactory<Semigroup<A>, Maybe<A>> {

    private static final Absent<?> INSTANCE = new Absent<>();

    private Absent() {
    }

    @Override
    public Semigroup<Maybe<A>> checkedApply(Semigroup<A> aSemigroup) {
        return shortCircuitSemigroup(aSemigroup);
    }

    @SuppressWarnings("unchecked")
    public static <A> Absent<A> absent() {
        return (Absent<A>) INSTANCE;
    }

    public static <A> Semigroup<Maybe<A>> absent(Semigroup<A> aSemigroup) {
        return shortCircuitSemigroup(aSemigroup);
    }

    public static <A> Fn1<Maybe<A>, Maybe<A>> absent(Semigroup<A> aSemigroup, Maybe<A> x) {
        return absent(aSemigroup).apply(x);
    }

    public static <A> Maybe<A> absent(Semigroup<A> semigroup, Maybe<A> x, Maybe<A> y) {
        return absent(semigroup, x).apply(y);
    }

    private static <A> Semigroup<Maybe<A>> shortCircuitSemigroup(Semigroup<A> aSemigroup) {
        return new Semigroup<Maybe<A>>() {
            @Override
            public Maybe<A> checkedApply(Maybe<A> maybeX, Maybe<A> maybeY) {
                return LiftA2.liftA2(aSemigroup, maybeX, maybeY);
            }

            @Override
            public Maybe<A> foldLeft(Maybe<A> aMaybe, Iterable<Maybe<A>> maybes) {
                Maybe<A> accumulation = aMaybe;
                for (Maybe<A> a : maybes) {
                    boolean shouldShortCircuit = accumulation == Maybe.nothing();
                    if (shouldShortCircuit)
                        return accumulation;
                    accumulation = checkedApply(accumulation, a);
                }
                return accumulation;
            }

            @Override
            public Lazy<Maybe<A>> foldRight(Maybe<A> accumulation, Iterable<Maybe<A>> as) {
                boolean shouldShortCircuit = accumulation == Maybe.nothing();
                if (shouldShortCircuit)
                    return lazy(accumulation);
                return FoldRight.foldRight(
                        (maybeX, acc) -> maybeX.lazyZip(acc.fmap(maybeY -> maybeY.fmap(aSemigroup.flip()))),
                        lazy(accumulation),
                        as
                );
            }
        };
    }
}
