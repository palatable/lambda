package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;

/**
 * Given an <code>{@link IterateT}&lt;M, {@link Maybe}&lt;A&gt;&gt;</code>, return an
 * <code>{@link IterateT}&lt;M, A&gt;</code> of only the present values.
 *
 * @param <M> the IterateT effect type
 * @param <A> the {@link Maybe} element type, as well as the resulting {@link IterateT} element type
 */
public class CatMaybesM<M extends MonadRec<?, M>, A> implements Fn1<IterateT<M, Maybe<A>>, IterateT<M, A>> {

    private static final CatMaybesM<?, ?> INSTANCE = new CatMaybesM<>();

    private CatMaybesM() {
    }

    @Override
    public IterateT<M, A> checkedApply(IterateT<M, Maybe<A>> mas) throws Throwable {
        return mas.flatMap((Maybe<A> ma) -> ma.match(__ -> empty(Pure.of(mas.<MonadRec<Maybe<Tuple2<Maybe<A>, IterateT<M, Maybe<A>>>>, M>>runIterateT())),
                                                     mas::pure));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> CatMaybesM<M, A> catMaybesM() {
        return (CatMaybesM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> IterateT<M, A> catMaybesM(IterateT<M, Maybe<A>> mas) {
        return CatMaybesM.<M, A>catMaybesM().apply(mas);
    }
}
