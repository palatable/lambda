package com.jnape.palatable.lambda.monad.transformer.interpreter;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.*;
import com.jnape.palatable.lambda.monad.transformer.interpreter.interpreters.Transformers;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.transformer.builtin.EitherT.eitherT;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IdentityT.identityT;
import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.maybeT;
import static com.jnape.palatable.lambda.monad.transformer.builtin.ReaderT.readerT;
import static com.jnape.palatable.lambda.monad.transformer.builtin.StateT.liftStateT;
import static com.jnape.palatable.lambda.monad.transformer.interpreter.InterpreterH.InterpreterHs.interpretIdentityT;
import static com.jnape.palatable.lambda.monad.transformer.interpreter.InterpreterH.lifting;
import static com.jnape.palatable.lambda.monad.transformer.interpreter.interpreters.Transformers.*;
import static com.jnape.palatable.lambda.monad.transformer.interpreter.interpreters.transformers.Construction.eitherT;

public class Example {
    public static void simpleCase() {
        EitherT<IO<?>, String, Integer> eitherT = eitherT(io(() -> right(1)));
        Transformers.<IO<?>, String, Integer>runEitherT()
            .<IO<Either<String, Integer>>>interpret(eitherT)
            .flatMap(res -> io(() -> System.out.println("res (" + res.getClass().getName() + "): " + res)))
            .unsafePerformIO();
    }

    public static void nested() {
        EitherT<MaybeT<IO<?>, ?>, String, Integer> effect =
            eitherT(maybeT(io(() -> just(left("yeh nah")))));

        Interpreter<EitherT<MaybeT<IO<?>, ?>, String, ?>, Integer, IO<?>, Maybe<Either<String, Integer>>> voila =
            Transformers.<MaybeT<IO<?>, ?>, String, Integer>runEitherT()
                .andThen(runMaybeT());

        voila
            .<IO<Maybe<Either<String, Integer>>>>interpret(effect)
            .flatMap(res -> io(() -> System.out.println("res (" + res.getClass().getName() + "): " + res)))
            .unsafePerformIO();
    }

    public static final class RoutingContext {
    }

    public static final class Envelope<A> {
    }

    public static interface EnvelopeHandler<M extends MonadRec<?, M>, A> {
        MonadRec<Envelope<A>, M> handle(RoutingContext routingContext);
    }

    public static interface IOEnvelopeHandler<A> extends EnvelopeHandler<IO<?>, A> {
    }

    public static void deeplyNested() {
        Fn1<String, Integer> recoveryFn = String::length;
        Fn0<Integer>         orElseGet  = () -> -1;

        Interpreter<EitherT<MaybeT<IdentityT<IO<?>, ?>, ?>, String, ?>, Integer, IO<?>, Integer> interpreter =
            Transformers.<MaybeT<IdentityT<IO<?>, ?>, ?>, String, Integer>interpretEitherT(recoveryFn)
                .andThen(interpretMaybeT(orElseGet))
                .andThenH(interpretIdentityT());

        interpreter
            .<IO<Integer>>interpret(eitherT(maybeT(identityT(io(() -> new Identity<>(just(right(42))))))))
            .flatMap(res -> io(() -> System.out.println("res (" + res.getClass().getName() + "): " + res)))
            .unsafePerformIO();
    }

    public static void readerTCase() {
        ReaderT<Boolean, EitherT<IO<?>, String, ?>, Integer> transactional =
            readerT(f -> eitherT(f ? io(() -> right((int) Math.round(Math.random() * 100))) : io(left("foo"))));

        // F a -> G b
        Interpreter<
            // F                                            a
            ReaderT<Boolean, EitherT<IO<?>, String, ?>, ?>, Integer,
            // G                                            b
            IO<?>,                                          Tuple2<Either<String, Integer>, Integer>
            > massiveInterpreter =
            Transformers.<Boolean, EitherT<IO<?>, String, ?>>runReaderT(true)
                .<IO<?>, Integer, Either<String, Integer>>andThen(runEitherT())
                .<StateT<Integer, IO<?>, ?>>andThenH(lifting(liftStateT()))
                .andThen(eitherT())
                .andThen(runEitherT())
                .andThen(runStateT(10));

        massiveInterpreter
            .<IO<Tuple2<Either<String, Integer>, Integer>>>interpret(transactional)
            .flatMap(res -> io(() -> System.out.println("res (" + res.getClass().getName() + "): " + res)))
            .unsafePerformIO();
    }

    public static void main(String[] args) {
        simpleCase();
        nested();
        deeplyNested();
        readerTCase();
    }
}
