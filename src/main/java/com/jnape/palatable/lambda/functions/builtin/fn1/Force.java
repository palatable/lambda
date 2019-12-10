package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.traversable.LambdaIterable;

/**
 * Force a full iteration of an {@link Iterable}, presumably to perform any side-effects contained therein. Returns the
 * {@link Iterable} back.
 *
 * @param <A> the Iterable element type
 * @deprecated in favor of {@link LambdaIterable#traverse(Fn1, Fn1) traversing} into an {@link IO} and running it
 */
@Deprecated
public final class Force<A> implements Fn1<Iterable<A>, Iterable<A>> {

    private static final Force<?> INSTANCE = new Force<>();

    private Force() {
    }

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public Iterable<A> checkedApply(Iterable<A> as) {
        for (A ignored : as) {
        }
        return as;
    }

    @SuppressWarnings("unchecked")
    public static <A> Force<A> force() {
        return (Force<A>) INSTANCE;
    }

    public static <A> Iterable<A> force(Iterable<A> as) {
        return Force.<A>force().apply(as);
    }
}
