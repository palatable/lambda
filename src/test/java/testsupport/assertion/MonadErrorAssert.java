package testsupport.assertion;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.MonadError;
import com.jnape.palatable.traitor.framework.Subjects;
import testsupport.traits.Equivalence;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static testsupport.traits.Equivalence.equivalence;

public final class MonadErrorAssert {

    private MonadErrorAssert() {
    }

    public static <E, A, M extends MonadError<E, ?, M>, MA extends MonadError<E, A, M>> void assertLaws(
            Subjects<MA> subjects,
            E e,
            Fn1<? super E, ? extends MA> recovery) {
        subjects.forEach(subject -> throwCatch(equivalence(subject, id()), e, recovery)
                .peek(failures -> IO.throwing(new AssertionError("MonadError law failures\n\n" + failures))));
    }

    public static <E, A, M extends MonadError<E, ?, M>, MA extends MonadError<E, A, M>> void assertLawsEq(
            Subjects<Equivalence<MA>> subjects,
            E e,
            Fn1<? super E, ? extends MA> recovery) {
        subjects.forEach(subject -> throwCatch(subject, e, recovery)
                .peek(failures -> IO.throwing(new AssertionError("MonadError law failures\n\n" + failures))));
    }

    private static <E, A, M extends MonadError<E, ?, M>, MA extends MonadError<E, A, M>> Maybe<String> throwCatch(
            Equivalence<MA> equivalence,
            E e,
            Fn1<? super E, ? extends MA> recovery) {
        return equivalence.invMap(ma -> ma.throwError(e).catchError(recovery).coerce())
                       .equals(equivalence.swap(recovery.apply(e)))
               ? Maybe.nothing()
               : Maybe.just("ThrowCatch failed: " + equivalence + ".throwError(" + e + ")" +
                                    ".catchError(recoveryFn) /= recovery.apply(" + e + ")");
    }
}
