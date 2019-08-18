package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.MonadWriter;
import com.jnape.palatable.lambda.monoid.builtin.Present;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;

public class MonadWriterLaws<W, A, M extends MonadWriter<W, ?, M>> implements EquivalenceTrait<MonadWriter<W, A, M>> {

    @Override
    public Class<? super MonadWriter<?, ?, M>> type() {
        return MonadWriter.class;
    }

    @Override
    public void test(Equivalence<MonadWriter<W, A, M>> equivalence) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Equivalence<MonadWriter<W, A, M>>, Maybe<String>>>foldMap(
                        f -> f.apply(equivalence), asList(
                                this::testCensor,
                                this::testListens)
                )
                .peek(s -> IO.throwing(new AssertionError("The following MonadWriter laws did not hold for instance" +
                                                                  " of " + equivalence + ": \n\t - " + s)));
    }

    private Maybe<String> testCensor(Equivalence<MonadWriter<W, A, M>> equivalence) {
        return equivalence.invMap(mw -> mw.censor(id())).equals(equivalence)
               ? nothing()
               : just("censor (mw.censor(id()).equals(mw))");
    }

    private Maybe<String> testListens(Equivalence<MonadWriter<W, A, M>> equivalence) {
        return equivalence.invMap(mw -> mw.listens(id()).fmap(Tuple2::_1)).equals(equivalence)
               ? nothing()
               : just("censor (mw.censor(id()).equals(mw))");
    }
}
