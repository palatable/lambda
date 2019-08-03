package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monoid.builtin.Present;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static java.util.Collections.singletonList;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

public class MonadRecLaws<M extends MonadRec<?, M>> implements EquivalenceTrait<MonadRec<?, M>> {

    @Override
    public Class<? super MonadRec<?, M>> type() {
        return MonadRec.class;
    }

    @Override
    public void test(Equivalence<MonadRec<?, M>> equivalence) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Equivalence<MonadRec<?, M>>, Maybe<String>>>foldMap(
                        fn -> fn.apply(equivalence),
                        singletonList(this::testStackSafety))
                .match(IO::io,
                       s -> IO.throwing(new AssertionError("The following MonadRec laws did not hold for instance of " +
                                                                   equivalence + ": \n\t - " + s)))
                .unsafePerformIO();
    }

    private Maybe<String> testStackSafety(Equivalence<MonadRec<?, M>> equivalence) {
        return equivalence.invMap(mr -> mr.pure(0)
                .trampolineM(x -> mr.pure(x < STACK_EXPLODING_NUMBER
                                          ? RecursiveResult.<Integer, Integer>recurse(x + 1)
                                          : RecursiveResult.<Integer, Integer>terminate(x))))
                       .equals(equivalence.invMap(mr -> mr.pure(STACK_EXPLODING_NUMBER)))
               ? nothing()
               : just("stack-safety (m.pure(" + STACK_EXPLODING_NUMBER + ").equals(m.pure(0).trampolineM(f)))");
    }
}
