package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse.ifThenElse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.suspended;

/**
 * Lazily skip the first <code>n</code> elements from an <code>IterateT</code> by returning an <code>IterateT</code>
 * that begins iteration after the <code>nth</code> element. If <code>n</code> is greater than or equal to the length of
 * the <code>IterateT</code>, an empty <code>IterateT</code> is returned.
 *
 * @param <A> The IterateT element type
 * @param <M> the IterateT effect type
 * @see DropWhile
 * @see Take
 */
public class DropM<A, M extends MonadRec<?, M>> implements Fn2<Integer, IterateT<M, A>, IterateT<M, A>> {

    private static final DropM<?, ?> INSTANCE = new DropM<>();

    @Override
    public IterateT<M, A> checkedApply(Integer n, IterateT<M, A> as) throws Throwable {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrapped = as.runIterateT();
        return suspended(() -> unwrapped.fmap(tupler(0)).fmap(t -> t.fmap(unwrapped::pure))
                                        .trampolineM(into((dropCount, remaining) -> remaining
                                                .fmap(mta -> ifThenElse(lt(n),
                                                                        dropped -> mta.match(__ -> terminate(nothing()),
                                                                                             into((h, t) -> recurse(tuple(dropped + 1, t.runIterateT())))),
                                                                        constantly(terminate(mta)),
                                                                        dropCount)))),
                         Pure.of(unwrapped));
    }

    @SuppressWarnings("unchecked")
    public static <A, M extends MonadRec<?, M>> DropM<A, M> dropM() {
        return (DropM<A, M>) INSTANCE;
    }

    public static <A, M extends MonadRec<?, M>> Fn1<IterateT<M, A>, IterateT<M, A>> dropM(Integer n) {
        return DropM.<A, M>dropM().apply(n);
    }

    public static <A, M extends MonadRec<?, M>> IterateT<M, A> dropM(Integer n, IterateT<M, A> as) {
        return DropM.<A, M>dropM(n).apply(as);
    }
}
