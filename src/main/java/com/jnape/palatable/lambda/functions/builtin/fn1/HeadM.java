package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.maybeT;

/**
 * Retrieve the head element of an {@link IterateT}, wrapped in an {@link Maybe}. If the {@link IterateT} is empty, the
 * result is {@link Maybe#nothing()}.
 *
 * @param <A> the IterateT element type
 * @param <M> the IterateT effect type
 */
public class HeadM<A, M extends MonadRec<?, M>, MMA extends MonadRec<Maybe<A>, M>> implements Fn1<IterateT<M, A>, MMA> {

    private static final HeadM<?, ?, ?> INSTANCE = new HeadM<>();

    @Override
    public MMA checkedApply(IterateT<M, A> as) {
        return maybeT(as.runIterateT())
                .fmap(Tuple2::_1)
                .runMaybeT();
    }

    @SuppressWarnings("unchecked")
    public static <A, M extends MonadRec<?, M>, MMA extends MonadRec<Maybe<A>, M>> HeadM<A, M, MMA> headM() {
        return (HeadM<A, M, MMA>) INSTANCE;
    }

    public static <A, M extends MonadRec<?, M>, MMA extends MonadRec<Maybe<A>, M>> MMA headM(IterateT<M, A> as) {
        return HeadM.<A, M, MMA>headM().apply(as);
    }
}
