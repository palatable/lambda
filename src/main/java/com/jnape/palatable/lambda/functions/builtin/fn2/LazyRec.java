package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.Kleisli;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

import static com.jnape.palatable.lambda.functions.specialized.Kleisli.kleisli;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * Given a {@link Fn2} that receives a recursive function and an input and yields a {@link Lazy lazy} result, and an
 * input, produce a {@link Lazy lazy} result that, when forced, will recursively invoke the function until it terminates
 * in a stack-safe way.
 * <p>
 * Example:
 * <pre>
 * {@code
 * Lazy<BigInteger> lazyFactorial = lazyRec((fact, x) -&gt; x.equals(ONE)
 *                                                       ? lazy(x)
 *                                                       : fact.apply(x.subtract(ONE)).fmap(y -&gt; y.multiply(x)),
 *                                                  BigInteger.valueOf(50_000));
 * BigInteger value = lazyFactorial.value(); // 3.34732050959714483691547609407148647791277322381045 x 10^213236
 * }
 * </pre>
 *
 * @param <A> the input type
 * @param <B> the output type
 */
public final class LazyRec<A, B> implements
        Fn2<Fn2<Kleisli<? super A, ? extends B, Lazy<?>, Lazy<B>>, A, Lazy<B>>, A, Lazy<B>> {

    private static final LazyRec<?, ?> INSTANCE = new LazyRec<>();

    private LazyRec() {
    }

    @Override
    public Lazy<B> checkedApply(Fn2<Kleisli<? super A, ? extends B, Lazy<?>, Lazy<B>>, A, Lazy<B>> fn, A a) {
        return lazy(a).flatMap(fn.apply(lazyRec(fn)));
    }

    @SuppressWarnings("unchecked")
    public static <A, B> LazyRec<A, B> lazyRec() {
        return (LazyRec<A, B>) INSTANCE;
    }

    public static <A, B> Kleisli<? super A, ? extends B, Lazy<?>, Lazy<B>> lazyRec(
            Fn2<Kleisli<? super A, ? extends B, Lazy<?>, Lazy<B>>, A, Lazy<B>> fn) {
        return kleisli(LazyRec.<A, B>lazyRec().apply(fn));
    }

    public static <A, B> Lazy<B> lazyRec(Fn2<Kleisli<? super A, ? extends B, Lazy<?>, Lazy<B>>, A, Lazy<B>> fn, A a) {
        return lazyRec(fn).apply(a);
    }
}
