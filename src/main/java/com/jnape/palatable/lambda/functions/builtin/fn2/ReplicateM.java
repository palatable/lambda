package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.functions.builtin.fn1.RepeatM.repeatM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;

/**
 * Produce an {@link IterateT} of a value <code>n</code> times.
 *
 * @param <A> the output IterateT element type
 * @param <M> the output IterateT element type
 */

public class ReplicateM<M extends MonadRec<?, M>, A> implements Fn2<Integer, MonadRec<A, M>, IterateT<M, A>> {

    private static final ReplicateM<?, ?> INSTANCE = new ReplicateM<>();

    private ReplicateM() {
    }

    @Override
    public IterateT<M, A> checkedApply(Integer n, MonadRec<A, M> ma) throws Throwable {
        if (n < 0) throw new IllegalArgumentException("Replica count must not be negative: " + n);
        return takeM(n, repeatM(ma));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> ReplicateM<M, A> replicateM() {
        return (ReplicateM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> Fn1<MonadRec<A, M>, IterateT<M, A>> replicateM(Integer n) {
        return ReplicateM.<M, A>replicateM().apply(n);
    }

    public static <M extends MonadRec<?, M>, A> IterateT<M, A> replicateM(Integer n, MonadRec<A, M> a) {
        return ReplicateM.<M, A>replicateM(n).apply(a);
    }
}
