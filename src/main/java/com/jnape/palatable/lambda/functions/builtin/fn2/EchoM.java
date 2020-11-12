package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.functions.builtin.fn2.ReplicateM.replicateM;

/**
 * Repeat each element in an <code>IterateT</code> <code>n</code> times.
 *
 * @param <M> the IterateT effect type
 * @param <A> The IterateT element type
 */
public class EchoM<M extends MonadRec<?, M>, A> implements Fn2<Integer, IterateT<M, A>, IterateT<M, A>> {

    private static final EchoM<?, ?> INSTANCE = new EchoM<>();

    private EchoM() {
    }

    @Override
    public IterateT<M, A> checkedApply(Integer n, IterateT<M, A> mas) throws Throwable {
        return mas.flatMap(a -> replicateM(n, mas.runIterateT().pure(a)));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> EchoM<M, A> echoM() {
        return (EchoM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> Fn1<IterateT<M, A>, IterateT<M, A>> echoM(Integer n) {
        return EchoM.<M, A>echoM().apply(n);
    }

    public static <M extends MonadRec<?, M>, A> IterateT<M,A> echoM(Integer n, IterateT<M,A> mas) {
        return EchoM.<M,A>echoM(n).apply(mas);
    }
}
