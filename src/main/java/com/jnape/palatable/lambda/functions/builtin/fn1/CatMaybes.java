package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Collections;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Flatten.flatten;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;

/**
 * Given an <code>{@link Iterable}&lt;{@link Maybe}&lt;A&gt;&gt;</code>, return an
 * <code>{@link Iterable}&lt;A&gt;</code> of only the present values.
 *
 * @param <A> the {@link Maybe} element type, as well as the resulting {@link Iterable} element type
 */
public final class CatMaybes<A> implements Fn1<Iterable<Maybe<A>>, Iterable<A>> {
    private static final CatMaybes INSTANCE = new CatMaybes();

    private CatMaybes() {
    }

    @Override
    public Iterable<A> apply(Iterable<Maybe<A>> maybes) {
        return flatten(map(m -> m.<Iterable<A>>fmap(Collections::singletonList)
                .orElse(Collections::emptyIterator), maybes));
    }

    @SuppressWarnings("unchecked")
    public static <A> CatMaybes<A> catMaybes() {
        return INSTANCE;
    }

    public static <A> Iterable<A> catMaybes(Iterable<Maybe<A>> as) {
        return CatMaybes.<A>catMaybes().apply(as);
    }
}
