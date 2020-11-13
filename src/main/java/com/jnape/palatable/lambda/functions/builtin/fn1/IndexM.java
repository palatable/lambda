package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ZipM.zipM;

/**
 * Given an <code>{@link IterateT}&lt;M, A&gt;</code>, pair each element with its ordinal index.
 *
 * @param <A> the IterateT element type
 * @param <M> the IterateT effect type
 */
public class IndexM<M extends MonadRec<?, M>, A> implements Fn1<IterateT<M, A>, IterateT<M, Tuple2<Integer, A>>> {

    private static final IndexM<?, ?> INSTANCE = new IndexM<>();

    private IndexM() {
    }

    @Override
    public IterateT<M, Tuple2<Integer, A>> checkedApply(IterateT<M, A> as) throws Throwable {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrapped = as.runIterateT();
        return zipM(naturalNumbersM(Pure.of(unwrapped)), as);
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> IndexM<M, A> indexM() {
        return (IndexM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> IterateT<M, Tuple2<Integer, A>> indexM(IterateT<M, A> as) {
        return IndexM.<M, A>indexM().apply(as);
    }
}
