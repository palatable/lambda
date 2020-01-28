package com.jnape.palatable.lambda.monad.transformer.interpreter.interpreters;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Writer;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.interpreter.Interpreter;
import com.jnape.palatable.lambda.monoid.Monoid;

public final class Values {
    private Values() {
    }

    public static <F extends MonadRec<?, F>, L, R> Interpreter<F, Either<L, R>, F, R>
    interpretEither(Fn1<? super L, ? extends R> recoveryFn) {
        return new Interpreter<F, Either<L, R>, F, R>() {
            @Override
            public <GB extends MonadRec<R, F>> GB interpret(MonadRec<Either<L, R>, F> fa) {
                return fa.fmap(lOrR -> lOrR.recover(recoveryFn)).coerce();
            }
        };
    }

    public static <F extends MonadRec<?, F>, A> Interpreter<F, Maybe<A>, F, A> interpretMaybe(Fn0<A> orElse) {
        return new Interpreter<F, Maybe<A>, F, A>() {
            @Override
            public <GB extends MonadRec<A, F>> GB interpret(MonadRec<Maybe<A>, F> fa) {
                return fa.fmap(maybeA -> maybeA.orElseGet(orElse)).coerce();
            }
        };
    }

    public static <F extends MonadRec<?, F>, A> Interpreter<F, Identity<A>, F, A> interpretIdentity() {
        return new Interpreter<F, Identity<A>, F, A>() {
            @Override
            public <GB extends MonadRec<A, F>> GB interpret(MonadRec<Identity<A>, F> fa) {
                return fa.fmap(Identity::runIdentity).coerce();
            }
        };
    }

    public static <F extends MonadRec<?, F>, W, A> Interpreter<F, Writer<W, A>, F, Tuple2<A, W>>
    interpretWriter(Monoid<W> monoidW) {
        return new Interpreter<F, Writer<W, A>, F, Tuple2<A, W>>() {
            @Override
            public <GB extends MonadRec<Tuple2<A, W>, F>> GB interpret(MonadRec<Writer<W, A>, F> fa) {
                return fa.fmap(writer -> writer.runWriter(monoidW)).coerce();
            }
        };
    }
}
