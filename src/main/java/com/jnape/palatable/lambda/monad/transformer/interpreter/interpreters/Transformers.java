package com.jnape.palatable.lambda.monad.transformer.interpreter.interpreters;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.*;
import com.jnape.palatable.lambda.monad.transformer.interpreter.Interpreter;
import com.jnape.palatable.lambda.monad.transformer.interpreter.InterpreterH;

import static com.jnape.palatable.lambda.monad.transformer.interpreter.interpreters.Values.*;

public final class Transformers {
    private Transformers() {
    }

    public static <F extends MonadRec<?, F>, L, R> Interpreter<EitherT<F, L, ?>, R, F, Either<L, R>> runEitherT() {
        return new Interpreter<EitherT<F, L, ?>, R, F, Either<L, R>>() {
            @Override
            public <GB extends MonadRec<Either<L, R>, F>> GB interpret(MonadRec<R, EitherT<F, L, ?>> fa) {
                return fa.<EitherT<F, L, R>>coerce().runEitherT();
            }
        };
    }

    public static <F extends MonadRec<?, F>, L, R> Interpreter<EitherT<F, L, ?>, R, F, R> interpretEitherT(
        Fn1<? super L, ? extends R> recoveryFn) {
        return Transformers.<F, L, R>runEitherT().andThen(interpretEither(recoveryFn));
    }


    public static <F extends MonadRec<?, F>, A> Interpreter<MaybeT<F, ?>, A, F, Maybe<A>> runMaybeT() {
        return new Interpreter<MaybeT<F, ?>, A, F, Maybe<A>>() {
            @Override
            public <FMA extends MonadRec<Maybe<A>, F>> FMA interpret(MonadRec<A, MaybeT<F, ?>> maybeT) {
                return maybeT.<MaybeT<F, A>>coerce().runMaybeT();
            }
        };
    }

    public static <F extends MonadRec<?, F>, A> Interpreter<MaybeT<F, ?>, A, F, A> interpretMaybeT(Fn0<A> orElse) {
        return Transformers.<F, A>runMaybeT().andThen(interpretMaybe(orElse));
    }


    public static <F extends MonadRec<?, F>, A> Interpreter<IdentityT<F, ?>, A, F, Identity<A>> runIdentityT() {
        return new Interpreter<IdentityT<F, ?>, A, F, Identity<A>>() {
            @Override
            public <GB extends MonadRec<Identity<A>, F>> GB interpret(MonadRec<A, IdentityT<F, ?>> fa) {
                return fa.<IdentityT<F, A>>coerce().runIdentityT();
            }
        };
    }

    public static <F extends MonadRec<?, F>, A> Interpreter<IdentityT<F, ?>, A, F, A> interpretIdentityT() {
        return Transformers.<F, A>runIdentityT().andThen(interpretIdentity());
    }

    public static <R, M extends MonadRec<?, M>> InterpreterH<ReaderT<R, M, ?>, M> runReaderT(R r) {
        return new InterpreterH<ReaderT<R, M, ?>, M>() {
            @Override
            public <A, GA extends MonadRec<A, M>> GA interpretH(MonadRec<A, ReaderT<R, M, ?>> fa) {
                return fa.<ReaderT<R, M, A>>coerce().runReaderT(r);
            }
        };
    }

    public static <S, M extends MonadRec<?, M>, A> Interpreter<StateT<S, M, ?>, A, M, Tuple2<A, S>> runStateT(S s) {
        return new Interpreter<StateT<S, M, ?>, A, M, Tuple2<A, S>>() {
            @Override
            public <GB extends MonadRec<Tuple2<A, S>, M>> GB interpret(MonadRec<A, StateT<S, M, ?>> fa) {
                return fa.<StateT<S, M, A>>coerce().runStateT(s).coerce();
            }
        };
    }

    public static void main(String[] args) {
    }
}
