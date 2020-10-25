package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.suspended;

public class DropWhileM<A, M extends MonadRec<?, M>> implements Fn2<Fn1<? super A, ? extends Boolean>, IterateT<M, A>, IterateT<M, A>> {

    private static final DropWhileM<?, ?> INSTANCE = new DropWhileM<>();

    @Override
    public IterateT<M, A> checkedApply(Fn1<? super A, ? extends Boolean> predicate, IterateT<M, A> as) throws Throwable {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrapped = as.runIterateT();
        return suspended(
                () -> unwrapped.trampolineM(mta -> mta
                        .match(constantly(unwrapped.pure(terminate(nothing()))),
                               into((A h, IterateT<M, A> t) ->
                                            predicate.apply(h) ? t.runIterateT().fmap(RecursiveResult::recurse)
                                                               : unwrapped.pure(terminate(mta))))),
                Pure.of(unwrapped));
    }

    @SuppressWarnings("unchecked")
    public static <A, M extends MonadRec<?, M>> DropWhileM<A, M> dropWhileM() {
        return (DropWhileM<A, M>) INSTANCE;
    }

    public static <A, M extends MonadRec<?, M>> Fn1<IterateT<M, A>, IterateT<M, A>> dropWhileM(Fn1<? super A, ? extends Boolean> predicate) {
        return DropWhileM.<A, M>dropWhileM().apply(predicate);
    }

    public static <A, M extends MonadRec<?, M>> IterateT<M, A> dropWhileM(Fn1<? super A, ? extends Boolean> predicate, IterateT<M, A> as) {
        return DropWhileM.<A, M>dropWhileM(predicate).apply(as);
    }
}
