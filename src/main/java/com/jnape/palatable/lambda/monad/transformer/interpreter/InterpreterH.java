package com.jnape.palatable.lambda.monad.transformer.interpreter;

import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.MonadT;
import com.jnape.palatable.lambda.monad.transformer.builtin.IdentityT;

public interface InterpreterH<F extends MonadRec<?, F>, G extends MonadRec<?, G>> {

    <A, GA extends MonadRec<A, G>> GA interpretH(MonadRec<A, F> fa);

    default <H extends MonadRec<?, H>> InterpreterH<F, H> andThenH(InterpreterH<G, H> gh) {
        return new InterpreterH<F, H>() {
            @Override
            public <A, HA extends MonadRec<A, H>> HA interpretH(MonadRec<A, F> fa) {
                return gh.interpretH(InterpreterH.this.interpretH(fa));
            }
        };
    }

    default <H extends MonadRec<?, H>, A, B> Interpreter<F, A, H, B> andThen(Interpreter<G, A, H, B> gh) {
        return gh.compose(this);
    }

    default <E extends MonadRec<?, E>> InterpreterH<E, G> composeH(InterpreterH<E, F> ef) {
        return ef.andThenH(this);
    }

    default <E extends MonadRec<?, E>, A, B> Interpreter<E, A, G, B> compose(Interpreter<E, A, F, B> gh) {
        return gh.andThen(this);
    }

    default <A> Interpreter<F, A, G, A> monomorphize() {
        return new Interpreter<F, A, G, A>() {
            @Override
            public <GA extends MonadRec<A, G>> GA interpret(MonadRec<A, F> fa) {
                return interpretH(fa);
            }
        };
    }

    static <F extends MonadRec<?, F>> InterpreterH<F, F> identity() {
        return new InterpreterH<F, F>() {
            @Override
            public <A, GA extends MonadRec<A, F>> GA interpretH(MonadRec<A, F> fa) {
                return fa.coerce();
            }
        };
    }

    static <F extends MonadRec<?, F>, T extends MonadT<?, ?, ?, T>, G extends MonadT<F, ?, G, T>> InterpreterH<F, G>
    lifting(Lift<T> liftT) {
        return new InterpreterH<F, G>() {
            @Override
            public <A, GA extends MonadRec<A, G>> GA interpretH(MonadRec<A, F> fa) {
                return liftT.<A, F, MonadT<F, A, G, T>>apply(fa).coerce();
            }
        };
    }

    static <F extends MonadRec<?, F>, T extends MonadT<?, ?, ?, T>, G extends MonadT<F, ?, G, T>> InterpreterH<F, G>
    constructing(Lift<T> liftT) {
        return new InterpreterH<F, G>() {
            @Override
            public <A, GA extends MonadRec<A, G>> GA interpretH(MonadRec<A, F> fa) {
                return liftT.<A, F, MonadT<F, A, G, T>>apply(fa).coerce();
            }
        };
    }

    final class InterpreterHs {
        private InterpreterHs() {
        }

        public static <F extends MonadRec<?, F>> InterpreterH<IdentityT<F, ?>, F> interpretIdentityT() {
            return new InterpreterH<IdentityT<F, ?>, F>() {
                @Override
                public <A, GA extends MonadRec<A, F>> GA interpretH(MonadRec<A, IdentityT<F, ?>> fa) {
                    return fa.<IdentityT<F, A>>coerce().runIdentityT().fmap(Identity::runIdentity).coerce();
                }
            };
        }
    }

}
