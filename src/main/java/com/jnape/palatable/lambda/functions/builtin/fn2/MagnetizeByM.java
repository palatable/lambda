package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.suspended;

/**
 * Given a binary predicate and an <code>{@link IterateT}&lt;M, A&gt;</code>, return an <code>{@link IterateT}&lt;M, {@link
 * IterateT}&lt;M, A&gt;&gt;</code> of the contiguous groups of elements that match the predicate pairwise.
 * <p>
 * See <code>{@link MagnetizeBy}</code> for an example using <code>Iterable</code>
 *
 * @param <A> the {@link IterateT} element type
 * @param <M> the {@link IterateT} effect type
 */
public class MagnetizeByM<M extends MonadRec<?, M>, A> implements Fn2<Fn2<? super A, ? super A, Boolean>, IterateT<M, A>, IterateT<M, IterateT<M, A>>> {

    private static final MagnetizeByM<?, ?> INSTANCE = new MagnetizeByM<>();

    private MagnetizeByM() {
    }

    @Override
    public IterateT<M, IterateT<M, A>> checkedApply(Fn2<? super A, ? super A, Boolean> predicate, IterateT<M, A> mas) {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrappedA = mas.runIterateT();
        return suspended(() -> unwrappedA
                                 .fmap(m -> m.fmap(into((pivot, ys) -> tuple(pivot, mas.pure(pivot), ys))))
                                 .trampolineM(m -> m.match(
                                         __ -> unwrappedA.pure(terminate(nothing())),
                                         into3((pivot, group, ys) -> ys.runIterateT().<RecursiveResult<Maybe<Tuple3<A, IterateT<M, A>, IterateT<M, A>>>, Maybe<Tuple2<IterateT<M, A>, IterateT<M, A>>>>>fmap(m2 -> m2.match(
                                                 __ -> terminate(just(tuple(group, empty(Pure.of(unwrappedA))))),
                                                 into((y, tail) ->
                                                              predicate.apply(pivot, y)
                                                              ? recurse(just(tuple(y, group.concat(mas.pure(y)), tail)))
                                                              : terminate(just(tuple(group, mas.pure(y).concat(tail))))))))))
                                 .fmap(m -> m.fmap(t -> t.fmap(magnetizeByM(predicate)))),
                         Pure.of(unwrappedA));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> MagnetizeByM<M, A> magnetizeByM() {
        return (MagnetizeByM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> Fn1<IterateT<M, A>, IterateT<M, IterateT<M, A>>> magnetizeByM(Fn2<? super A, ? super A, Boolean> predicate) {
        return MagnetizeByM.<M, A>magnetizeByM().apply(predicate);
    }

    public static <M extends MonadRec<?, M>, A> IterateT<M, IterateT<M, A>> magnetizeByM(Fn2<? super A, ? super A, Boolean> predicate, IterateT<M, A> mas) {
        return MagnetizeByM.<M, A>magnetizeByM(predicate).apply(mas);
    }
}
