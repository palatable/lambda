package testsupport.assertion;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.MonadError;
import com.jnape.palatable.traitor.framework.Subjects;
import testsupport.EquatableM;

public final class MonadErrorAssert {

    private MonadErrorAssert() {
    }

    public static <E, A, M extends MonadError<E, ?, M>> void assertLaws(
            Subjects<MonadError<E, A, M>> subjects,
            E e,
            Fn1<? super E, ? extends MonadError<E, A, M>> recovery) {

        subjects.forEach(subject -> throwCatch(subject, e, recovery)
                .peek(failures -> IO.throwing(new AssertionError("MonadError law failures\n\n" + failures))));
    }

    public static <E, A, M extends MonadError<E, ?, M>> void assertLawsEq(
            Subjects<EquatableM<M, A>> subjects,
            E e,
            Fn1<? super E, ? extends MonadError<E, A, M>> recovery) {
        subjects.forEach(subject -> throwCatch(subject, e, recovery)
                .peek(failures -> IO.throwing(new AssertionError("MonadError law failures\n\n" + failures))));
    }

    private static <E, A, M extends MonadError<E, ?, M>> Maybe<String> throwCatch(
            MonadError<E, A, M> monadError,
            E e,
            Fn1<? super E, ? extends MonadError<E, A, M>> recovery) {
        return throwCatch(new EquatableM<>(monadError, x -> x), e, recovery);
    }

    private static <E, A, M extends MonadError<E, ?, M>> Maybe<String> throwCatch(
            EquatableM<M, A> equatable,
            E e,
            Fn1<? super E, ? extends MonadError<E, A, M>> recovery) {
        EquatableM<M, A> eq = equatable.with(ma -> ma.<MonadError<E, A, M>>coerce().throwError(e).catchError(recovery));
        return eq.equals(eq.swap(recovery.apply(e)))
               ? Maybe.nothing()
               : Maybe.just("ThrowCatch failed: " + equatable + ".throwError(" + e + ")" +
                                    ".catchError(recoveryFn) /= recovery.apply(" + e + ")");
    }
}
