package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse.ifThenElse;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.*;

/**
 * Lazily apply a predicate to each element in an <code>IterateT</code>, returning an <code>IterateT</code> of just the
 * elements for which the predicate evaluated to <code>true</code>.
 *
 * @param <A> A type contravariant to the input IterateT element type
 * @param <M> the IterateT effect type
 * @see TakeWhileM
 * @see DropWhileM
 */
public class FilterM<A, M extends MonadRec<?, M>> implements Fn2<Fn1<? super A, ? extends Boolean>, IterateT<M, A>, IterateT<M, A>> {

    private static final FilterM<?, ?> INSTANCE = new FilterM<>();

    @Override
    public IterateT<M, A> checkedApply(Fn1<? super A, ? extends Boolean> predicate, IterateT<M, A> as) throws Throwable {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrapped = as.runIterateT();
        return iterateT(unwrapped)
                .flatMap(ifThenElse(predicate,
                                    a -> singleton(unwrapped.pure(a)),
                                    __ -> empty(Pure.of(unwrapped))));
    }

    @SuppressWarnings("unchecked")
    public static <A, M extends MonadRec<?, M>> FilterM<A, M> filterM() {
        return (FilterM<A, M>) INSTANCE;
    }

    public static <A, M extends MonadRec<?, M>> Fn1<IterateT<M, A>, IterateT<M, A>> filterM(Fn1<? super A, ? extends Boolean> predicate) {
        return FilterM.<A, M>filterM().apply(predicate);
    }

    public static <A, M extends MonadRec<?, M>> IterateT<M, A> filterM(Fn1<? super A, ? extends Boolean> predicate, IterateT<M, A> as) {
        return FilterM.<A, M>filterM(predicate).apply(as);
    }
}
