package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Collection;

/**
 * Given an {@link Fn0} of some {@link Collection} <code>C</code>, create an instance of <code>C</code> and add all of
 * the elements in the provided <code>Iterable</code> to the instance. Note that instances of <code>C</code> must
 * support {@link Collection#add} (which is to say, must not throw on invocation).
 *
 * @param <A> the iterable element type
 * @param <C> the resulting collection type
 */
public final class ToCollection<A, C extends Collection<A>> implements Fn2<Fn0<C>, Iterable<A>, C> {

    private static final ToCollection<?, ?> INSTANCE = new ToCollection<>();

    private ToCollection() {
    }

    @Override
    public C checkedApply(Fn0<C> cFn0, Iterable<A> as) {
        C c = cFn0.apply();
        as.forEach(c::add);
        return c;
    }

    @SuppressWarnings("unchecked")
    public static <A, C extends Collection<A>> ToCollection<A, C> toCollection() {
        return (ToCollection<A, C>) INSTANCE;
    }

    public static <A, C extends Collection<A>> Fn1<Iterable<A>, C> toCollection(Fn0<C> cFn0) {
        return ToCollection.<A, C>toCollection().apply(cFn0);
    }

    public static <A, C extends Collection<A>> C toCollection(Fn0<C> cFn0, Iterable<A> as) {
        return toCollection(cFn0).apply(as);
    }
}
