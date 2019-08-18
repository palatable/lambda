package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monoid.builtin.Present;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.monad.Monad.join;
import static java.util.Arrays.asList;

public class MonadLaws<M extends Monad<?, M>> implements EquivalenceTrait<Monad<?, M>> {

    @Override
    public Class<? super Monad<?, ?>> type() {
        return Monad.class;
    }

    @Override
    public void test(Equivalence<Monad<?, M>> equivalence) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Equivalence<Monad<?, M>>, Maybe<String>>>foldMap(f -> f.apply(equivalence), asList(
                        this::testLeftIdentity,
                        this::testRightIdentity,
                        this::testAssociativity,
                        this::testJoin))
                .peek(s -> IO.throwing(new AssertionError("The following Monad laws did not hold for instance of " +
                                                                  equivalence + ": \n\t - " + s)));
    }

    private Maybe<String> testLeftIdentity(Equivalence<Monad<?, M>> equivalence) {
        Object a = new Object();
        return equivalence.invMap(m -> m.pure(a.hashCode()))
                       .equals(equivalence.invMap(m -> m.pure(a.hashCode())))
               ? nothing()
               : just("left identity (m.pure(a).flatMap(fn).equals(fn.apply(a)))");
    }

    private Maybe<String> testRightIdentity(Equivalence<Monad<?, M>> equivalence) {
        return equivalence.invMap(m -> m.flatMap(m::pure)).equals(equivalence)
               ? nothing()
               : just("right identity: (m.flatMap(m::pure).equals(m))");
    }

    private Maybe<String> testAssociativity(Equivalence<Monad<?, M>> equivalence) {
        Object a = new Object();
        Object b = new Object();
        return equivalence.invMap(m -> m.flatMap(constantly(m.pure(a))).flatMap(constantly(m.pure(b))))
                       .equals(equivalence.invMap(m -> m.flatMap(__ -> m.pure(a).flatMap(constantly(m.pure(b))))))
               ? nothing()
               : just("associativity: (m.flatMap(f).flatMap(g).equals(m.flatMap(a -> f.apply(a).flatMap(g))))");
    }

    private Maybe<String> testJoin(Equivalence<Monad<?, M>> equivalence) {
        return equivalence.invMap(m -> m.pure(m).flatMap(id()))
                       .equals(equivalence.invMap(m -> join(m.pure(m))))
               ? nothing()
               : just("join: (m.pure(m).flatMap(id())).equals(Monad.join(m.pure(m)))");
    }
}
