package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.MagnetizeByM.magnetizeByM;

/**
 * {@link Magnetize} an {@link IterateT} using value equality as the magnetizing function.
 *
 * @param <A> the IterateT element type
 * @param <M> the IterateT effect type
 */
public class MagnetizeM<M extends MonadRec<?, M>, A> implements Fn1<IterateT<M, A>, IterateT<M, IterateT<M, A>>> {

    private static final MagnetizeM<?, ?> INSTANCE = new MagnetizeM<>();

    private MagnetizeM() {
    }

    @Override
    public IterateT<M, IterateT<M, A>> checkedApply(IterateT<M, A> mas) throws Throwable {
        return magnetizeByM(eq(), mas);
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A> MagnetizeM<M, A> magnetizeM() {
        return (MagnetizeM<M, A>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A> IterateT<M, IterateT<M, A>> magnetizeM(IterateT<M, A> mas) {
        return MagnetizeM.<M, A>magnetizeM().apply(mas);
    }
}
