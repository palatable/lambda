package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.HeadM.headM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.DropM.dropM;

/**
 * Retrieve the element of an {@link IterateT}, found at ordinal index <code>k</code> wrapped in an {@link Maybe} wrapped
 * in the monadic effect. If the index <code>k</code> is less than or equal to the size of the {@link IterateT}, the
 * result is {@link Maybe#nothing()} wrapped in the monadic effect.
 *
 * @param <A> the IterateT element type
 * @param <M> the IterateT effect type
 */
public class NthM<M extends MonadRec<?, M>, A> implements Fn2<Integer, IterateT<M, A>, MonadRec<Maybe<A>, M>> {

    private static final NthM<?, ?> INSTANCE = new NthM<>();

    private NthM() {
    }

    @Override
    public MonadRec<Maybe<A>, M> checkedApply(Integer k, IterateT<M, A> mas) throws Throwable {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrapped = mas.runIterateT();
        return k <= 0 ? unwrapped.pure(nothing()) : headM(dropM(k - 1, mas));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> NthM<M, A> nthM() {
        return (NthM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> Fn1<IterateT<M, A>, MonadRec<Maybe<A>, M>> nthM(Integer k) {
        return NthM.<M, A>nthM().apply(k);
    }

    public static <M extends MonadRec<?, M>, A> MonadRec<Maybe<A>, M> nthM(Integer k, IterateT<M, A> mas) {
        return NthM.<M, A>nthM(k).apply(mas);
    }
}
