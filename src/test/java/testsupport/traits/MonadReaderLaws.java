package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.MonadReader;
import com.jnape.palatable.lambda.monoid.builtin.Present;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.io.IO.throwing;
import static java.util.Collections.singletonList;

public class MonadReaderLaws<M extends MonadReader<?, ?, M>> implements EquivalenceTrait<MonadReader<?, ?, M>> {

    @Override
    public Class<? super MonadReader<?, ?, ?>> type() {
        return MonadReader.class;
    }

    @Override
    public void test(Equivalence<MonadReader<?, ?, M>> equivalence) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Equivalence<MonadReader<?, ?, M>>, Maybe<String>>>foldMap(
                        f -> f.apply(equivalence),
                        singletonList(this::testLocalIdentity)
                )
                .match(IO::io,
                       s -> throwing(new AssertionError("The following MonadReader laws did not hold for instance of "
                                                                + equivalence + ": \n\t - " + s)))
                .unsafePerformIO();
    }

    private Maybe<String> testLocalIdentity(Equivalence<MonadReader<?, ?, M>> equivalence) {
        return equivalence.invMap(mr -> mr.local(id())).equals(equivalence)
               ? nothing()
               : just("local identity (mr.local(id()).equals(mr))");
    }
}
