package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.lang.reflect.Array;
import java.util.Collection;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;

/**
 * Write all the elements of an {@link Iterable} directly into an array of the specified type. If the {@link Iterable}
 * is an instance of {@link Collection}, use {@link Collection#toArray(Object[])}.
 *
 * @param <A> the {@link Iterable} element type
 */
public final class ToArray<A> implements Fn2<Class<A[]>, Iterable<? extends A>, A[]> {

    private static final ToArray<?> INSTANCE = new ToArray<>();

    private ToArray() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public A[] apply(Class<A[]> arrayType, Iterable<? extends A> as) {
        A[] array = (A[]) Array.newInstance(arrayType.getComponentType(), size(as).intValue());
        if (as instanceof Collection)
            return ((Collection<A>) as).toArray(array);

        int index = 0;
        for (A a : as) {
            array[index++] = a;
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public static <A> ToArray<A> toArray() {
        return (ToArray<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<? extends A>, A[]> toArray(Class<A[]> arrayType) {
        return ToArray.<A>toArray().apply(arrayType);
    }

    public static <A> A[] toArray(Class<A[]> arrayType, Iterable<? extends A> as) {
        return toArray(arrayType).apply(as);
    }
}
