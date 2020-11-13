package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Zip;

import static com.jnape.palatable.lambda.functions.builtin.fn0.NaturalNumbers.naturalNumbers;
import static com.jnape.palatable.lambda.functions.builtin.fn2.$.$;

/**
 * Given an <code>{@link Iterable}&lt;A&gt;</code>, pair each element with its ordinal index.
 *
 * @param <A> the Iterable element type
 */
public class Index<A> implements Fn1<Iterable<A>, Iterable<Tuple2<Integer, A>>> {

    private static final Index<?> INSTANCE = new Index<>();

    private Index() {
    }

    @Override
    public Iterable<Tuple2<Integer, A>> checkedApply(Iterable<A> as) throws Throwable {
        return Zip.zip(naturalNumbers(), as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Index<A> index() {
        return (Index<A>) INSTANCE;
    }

    public static <A> Iterable<Tuple2<Integer, A>> index(Iterable<A> as) {
        return $(index(), as);
    }
}
