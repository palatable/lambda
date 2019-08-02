package testsupport.traits;

import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.traitor.traits.Trait;

import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

public class MonadRecLaws<M extends MonadRec<?, M>> implements Trait<MonadRec<?, M>> {

    @Override
    public void test(MonadRec<?, M> monadRec) {


        boolean equals = monadRec.pure(STACK_EXPLODING_NUMBER)
                .equals(monadRec.pure(0)
                                .trampolineM(x -> monadRec.pure(x < STACK_EXPLODING_NUMBER
                                                                ? recurse(x + 1)
                                                                : terminate(x))));
        if (!equals)
            throw new AssertionError("Expected m.pure(" + STACK_EXPLODING_NUMBER + ") == " +
                                             "m.pure(0).trampolineM(f)");
    }
}
