package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GT.gt;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.suspended;
import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.maybeT;

/**
 * Lazily limit the <code>IterateT</code> to <code>n</code> elements by returning an <code>IterateT</code> that stops
 * iteration after the <code>nth</code> element, or the last element of the <code>IterateT</code>, whichever comes
 * first.
 *
 * @param <A> The IterateT element type
 * @param <M> the IterateT effect type
 * @see TakeWhileM
 * @see DropM
 */
public class TakeM<A, M extends MonadRec<?, M>> implements Fn2<Integer, IterateT<M, A>, IterateT<M, A>> {

    private static final TakeM<?, ?> INSTANCE = new TakeM<>();

    @Override
    public IterateT<M, A> checkedApply(Integer n, IterateT<M, A> as) throws Throwable {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrapped = as.runIterateT();
        return suspended(() -> gt(0, n)
                               ? maybeT(unwrapped)
                                       .fmap(t -> t.fmap(takeM(n - 1)))
                                       .runMaybeT()
                               : unwrapped.pure(nothing()),
                         Pure.of(unwrapped));
    }

    @SuppressWarnings("unchecked")
    public static <A, M extends MonadRec<?, M>> TakeM<A, M> takeM() {
        return (TakeM<A, M>) INSTANCE;
    }

    public static <A, M extends MonadRec<?, M>> Fn1<IterateT<M, A>, IterateT<M, A>> takeM(Integer n) {
        return TakeM.<A, M>takeM().apply(n);
    }

    public static <A, M extends MonadRec<?, M>> IterateT<M, A> takeM(Integer n, IterateT<M, A> as) {
        return TakeM.<A, M>takeM(n).apply(as);
    }
}
