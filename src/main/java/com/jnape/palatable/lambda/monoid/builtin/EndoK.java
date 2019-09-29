package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.SafeT;
import com.jnape.palatable.lambda.monoid.Monoid;

import static com.jnape.palatable.lambda.functions.specialized.Kleisli.kleisli;
import static com.jnape.palatable.lambda.monad.SafeT.safeT;

/**
 * The monoid formed under monadic endomorphism.
 *
 * @param <M>  the {@link MonadRec} witness
 * @param <A>  the carrier type
 * @param <MA> the fully witnessed {@link MonadRec} type
 */
public final class EndoK<M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>> implements
        MonoidFactory<Pure<M>, Fn1<A, MA>> {

    private static final EndoK<?, ?, ?> INSTANCE = new EndoK<>();

    @Override
    public Monoid<Fn1<A, MA>> checkedApply(Pure<M> pureM) {
        return new Monoid<Fn1<A, MA>>() {
            @Override
            public Fn1<A, MA> identity() {
                return pureM::apply;
            }

            @Override
            public Fn1<A, MA> checkedApply(Fn1<A, MA> f, Fn1<A, MA> g) {
                return a -> kleisli(f).andThen(g::apply).apply(a);
            }

            @Override
            public <B> Fn1<A, MA> foldMap(Fn1<? super B, ? extends Fn1<A, MA>> fn, Iterable<B> bs) {
                return a -> FoldLeft.foldLeft((f, b) -> f.fmap(ma -> ma.flatMap(a_ -> safeT(fn.apply(b).apply(a_)))),
                                              safeT(identity()).fmap(SafeT::safeT),
                                              bs)
                        .<Fn1<A, SafeT<M, A>>>runSafeT()
                        .apply(a)
                        .runSafeT();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>> EndoK<M, A, MA> endoK() {
        return (EndoK<M, A, MA>) INSTANCE;
    }

    public static <M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>> Monoid<Fn1<A, MA>> endoK(Pure<M> pureM) {
        return EndoK.<M, A, MA>endoK().apply(pureM);
    }
}
