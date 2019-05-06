package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

/**
 * Upcast a value of type <code>B</code> to a value of type <code>A</code> that <code>B</code> extends. This is
 * principally useful when dealing with parametric types that are invariant in their parameters and a cast is
 * necessary for compatibility purposes.
 * <p>
 * Example:
 * <pre>
 * {@code
 * Iterable<String> have = new ArrayList<>();
 * Iterable<CharSequence> want = map(upcast(), have); // necessary due to invariance in parameter
 * }
 * </pre>
 * <p>
 * Note that this is universally safe.
 *
 * @param <A> the covariant type
 * @param <B> the contravariant type
 */
public final class Upcast<A extends B, B> implements Fn1<A, B> {

    private static final Upcast<?, ?> INSTANCE = new Upcast<>();

    private Upcast() {
    }

    @Override
    public B checkedApply(A a) {
        return a;
    }

    @SuppressWarnings("unchecked")
    public static <A extends B, B> Upcast<A, B> upcast() {
        return (Upcast<A, B>) INSTANCE;
    }

    public static <A extends B, B> B upcast(A a) {
        return Upcast.<A, B>upcast().apply(a);
    }
}
