package com.jnape.palatable.lambda.monad.transformer.interpreter;

import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

public interface Interpreter<F extends MonadRec<?, F>, A, G extends MonadRec<?, G>, B> {

    <GB extends MonadRec<B, G>> GB interpret(MonadRec<A, F> fa);

    default <H extends MonadRec<?, H>, C> Interpreter<F, A, H, C> andThen(Interpreter<G, B, H, C> ghbc) {
        return new Interpreter<F, A, H, C>() {
            @Override
            public <HC extends MonadRec<C, H>> HC interpret(MonadRec<A, F> fa) {
                return ghbc.interpret(Interpreter.this.interpret(fa));
            }
        };
    }

    default <H extends MonadRec<?, H>> Interpreter<F, A, H, B> andThen(InterpreterH<G, H> gh) {
        return new Interpreter<F, A, H, B>() {
            @Override
            public <GB extends MonadRec<B, H>> GB interpret(MonadRec<A, F> fa) {
                return gh.interpretH(Interpreter.this.interpret(fa));
            }
        };
    }

    default <E extends MonadRec<?, E>, Z> Interpreter<E, Z, G, B> compose(Interpreter<E, Z, F, A> efza) {
        return efza.andThen(this);
    }

    default <E extends MonadRec<?, E>> Interpreter<E, A, G, B> compose(InterpreterH<E, F> ef) {
        return new Interpreter<E, A, G, B>() {
            @Override
            public <GB extends MonadRec<B, G>> GB interpret(MonadRec<A, E> ea) {
                return Interpreter.this.interpret(ef.interpretH(ea));
            }
        };
    }

    static <F extends MonadRec<?, F>, A> Interpreter<F, A, F, A> identity() {
        return new Interpreter<F, A, F, A>() {
            @Override
            public <GB extends MonadRec<A, F>> GB interpret(MonadRec<A, F> fa) {
                return fa.coerce();
            }
        };
    }

    static <F extends MonadRec<?, F>, T extends MonadT<?, ?, ?, T>, G extends MonadT<F, ?, G, T>, A>
    Interpreter<F, A, G, A> lifting(Lift<T> liftT) {
        return new Interpreter<F, A, G, A>() {
            @Override
            public <GB extends MonadRec<A, G>> GB interpret(MonadRec<A, F> fa) {
                return liftT.<A, F, MonadT<F, A, G, T>>apply(fa).coerce();
            }
        };
    }
}
