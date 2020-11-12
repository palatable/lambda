package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.functions.builtin.fn1.CycleM.cycleM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.EchoM.echoM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.MagnetizeByM.magnetizeByM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ZipM.zipM;

/**
 * Lazily group the <code>IterateT</code> by returning an <code>IterateT</code> of smaller <code>IterateT</code>s of
 * size <code>k</code>. Note that groups are <em>not</em> padded; that is, if <code>k &gt;= n</code>, where
 * <code>n</code> is the number of remaining elements, the final <code>IterateT</code> will have only <code>n</code>
 * elements.
 *
 * @param <A> The IterateT element type
 * @param <M> The IterateT effect type
 */
public class InGroupsOfM<M extends MonadRec<?, M>, A> implements Fn2<Integer, IterateT<M, A>, IterateT<M, IterateT<M, A>>> {

    private static final InGroupsOfM<?, ?> INSTANCE = new InGroupsOfM<>();

    private InGroupsOfM() {
    }

    @Override
    public IterateT<M, IterateT<M, A>> checkedApply(Integer k, IterateT<M, A> mas) throws Throwable {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrapped = mas.runIterateT();
        return magnetizeByM((t1, t2) -> eq(t1._1(), t2._1()), zipM(echoM(k, cycleM(IterateT.of(unwrapped.pure(true), unwrapped.pure(false)))), mas))
                .fmap(it -> it.fmap(Tuple2::_2));
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> InGroupsOfM<M, A> inGroupsOfM() {
        return (InGroupsOfM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> Fn1<IterateT<M, A>, IterateT<M, IterateT<M, A>>> inGroupsOfM(Integer k) {
        return InGroupsOfM.<M, A>inGroupsOfM().apply(k);
    }

    public static <M extends MonadRec<?, M>, A> IterateT<M, IterateT<M, A>> inGroupsOfM(Integer k, IterateT<M, A> mas) {
        return InGroupsOfM.<M, A>inGroupsOfM(k).apply(mas);
    }
}
