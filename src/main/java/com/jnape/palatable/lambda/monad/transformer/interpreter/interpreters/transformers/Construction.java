package com.jnape.palatable.lambda.monad.transformer.interpreter.interpreters.transformers;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.EitherT;
import com.jnape.palatable.lambda.monad.transformer.builtin.IdentityT;
import com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT;
import com.jnape.palatable.lambda.monad.transformer.interpreter.Interpreter;

public final class Construction {
    private Construction() {
    }

    public static <F extends MonadRec<?, F>, A> Interpreter<F, Maybe<A>, MaybeT<F, ?>, A> maybeT() {
        return new Interpreter<F, Maybe<A>, MaybeT<F, ?>, A>() {
            @Override
            public <GB extends MonadRec<A, MaybeT<F, ?>>> GB interpret(MonadRec<Maybe<A>, F> fa) {
                return MaybeT.maybeT(fa).coerce();
            }
        };
    }

    public static <F extends MonadRec<?, F>, L, R> Interpreter<F, Either<L, R>, EitherT<F, L, ?>, R> eitherT() {
        return new Interpreter<F, Either<L, R>, EitherT<F, L, ?>, R>() {
            @Override
            public <GB extends MonadRec<R, EitherT<F, L, ?>>> GB interpret(MonadRec<Either<L, R>, F> fa) {
                return EitherT.eitherT(fa).coerce();
            }
        };
    }

    public static <F extends MonadRec<?, F>, A> Interpreter<F, Identity<A>, IdentityT<F, ?>, A> identityT() {
        return new Interpreter<F, Identity<A>, IdentityT<F, ?>, A>() {
            @Override
            public <GB extends MonadRec<A, IdentityT<F, ?>>> GB interpret(MonadRec<Identity<A>, F> fa) {
                return IdentityT.identityT(fa).coerce();
            }
        };
    }
}
