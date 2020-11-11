package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.functions.builtin.fn1.RepeatM.repeatM;

/**
 * Given an <code>IterateT</code>, return an infinite <code>IterateT</code> that repeatedly cycles its elements, in
 * order.
 *
 * @param <A> The IterateT element type
 * @param <M> The IterateT effect type
 */
public class CycleM<M extends MonadRec<?, M>, A> implements Fn1<IterateT<M, A>, IterateT<M, A>> {

    private static final CycleM<?, ?> INSTANCE = new CycleM<>();

    private CycleM() {
    }

    @Override
    public IterateT<M, A> checkedApply(IterateT<M, A> as) throws Throwable {
        return Monad.join(repeatM(as.runIterateT().pure(as)));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> CycleM<M, A> cycleM() {
        return (CycleM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> IterateT<M, A> cycleM(IterateT<M, A> as) {
        return CycleM.<M, A>cycleM().apply(as);
    }
}
