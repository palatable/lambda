package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Given a {@link Supplier} of some {@link Collection} <code>C</code>, create an instance of <code>C</code> and add
 * all of the elements in the provided <code>Iterable</code> to the instance. Note that instances of <code>C</code>
 * must support {@link Collection#add} (which is to say, must not throw on invocation).
 *
 * @param <A> the iterable element type
 * @param <C> the resulting collection type
 */
public final class ToCollection<A, C extends Collection<A>> implements Fn2<Supplier<C>, Iterable<A>, C> {

    private static final ToCollection<?, ?> INSTANCE = new ToCollection<>();

    private ToCollection() {
    }

    @Override
    public C apply(Supplier<C> cSupplier, Iterable<A> as) {
        C c = cSupplier.get();
        as.forEach(c::add);
        return c;
    }

    @SuppressWarnings("unchecked")
    public static <A, C extends Collection<A>> ToCollection<A, C> toCollection() {
        return (ToCollection<A, C>) INSTANCE;
    }

    public static <A, C extends Collection<A>> Fn1<Iterable<A>, C> toCollection(Supplier<C> cSupplier) {
        return ToCollection.<A, C>toCollection().apply(cSupplier);
    }

    public static <A, C extends Collection<A>> C toCollection(Supplier<C> cSupplier, Iterable<A> as) {
        return toCollection(cSupplier).apply(as);
    }
}
