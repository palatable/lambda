package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.functions.builtin.fn1.HeadM.headM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.FilterM.filterM;

/**
 * Iterate the elements in an <code>IterateT</code>, applying a predicate to each one, returning the first element that
 * matches the predicate, wrapped in a {@link Maybe}. If no elements match the predicate, the result is
 * {@link Maybe#nothing()}. This function short-circuits, and so is safe to use on potentially infinite {@link Iterable}
 * instances that guarantee to have an eventually matching element.
 *
 * @param <A> the IterateT element type
 * @param <M> the IterateT effect type
 */
public class FindM<M extends MonadRec<?, M>, A, MMA extends MonadRec<Maybe<A>, M>> implements Fn2<Fn1<? super A, Boolean>, IterateT<M, A>, MMA> {

    private static final FindM<?, ?, ?> INSTANCE = new FindM<>();

    private FindM() {
    }

    @Override
    public MMA checkedApply(Fn1<? super A, Boolean> predicate, IterateT<M, A> as) throws Throwable {
        return headM(filterM(predicate, as));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A, MMA extends MonadRec<Maybe<A>, M>> FindM<M, A, MMA> findM() {
        return (FindM<M, A, MMA>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A, MMA extends MonadRec<Maybe<A>, M>> Fn1<IterateT<M, A>, MMA> findM(Fn1<? super A, Boolean> predicate) {
        return FindM.<M, A, MMA>findM().apply(predicate);
    }

    public static <M extends MonadRec<?, M>, A, MMA extends MonadRec<Maybe<A>, M>> MMA findM(Fn1<? super A, Boolean> predicate, IterateT<M, A> as) {
        return FindM.<M, A, MMA>findM(predicate).apply(as);
    }
}
