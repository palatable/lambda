package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.unfold;

public class NaturalNumbersM<M extends MonadRec<?, M>> implements Fn1<Pure<M>, IterateT<M, Integer>> {

    private static final NaturalNumbersM<?> INSTANCE = new NaturalNumbersM<>();

    private NaturalNumbersM() {
    }

    @Override
    public IterateT<M, Integer> checkedApply(Pure<M> pureM) {
        return unfold(i -> pureM.apply(just(tuple(i, i + 1))), pureM.<Integer, MonadRec<Integer, M>>apply(1));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>> NaturalNumbersM<M> naturalNumbersM() {
        return (NaturalNumbersM<M>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>> IterateT<M, Integer> naturalNumbersM(Pure<M> pureM) {
        return NaturalNumbersM.<M>naturalNumbersM().apply(pureM);
    }
}
