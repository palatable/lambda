package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monoid.builtin.Present;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.io.IO.throwing;
import static java.util.Arrays.asList;

public class BifunctorLaws<BF extends Bifunctor<?, ?, BF>> implements EquivalenceTrait<Bifunctor<?, ?, BF>> {

    @Override
    public Class<? super Bifunctor<?, ?, BF>> type() {
        return Bifunctor.class;
    }

    @Override
    public void test(Equivalence<Bifunctor<?, ?, BF>> equivalence) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Equivalence<Bifunctor<?, ?, BF>>, Maybe<String>>>foldMap(
                        f -> f.apply(equivalence),
                        asList(this::testLeftIdentity,
                               this::testRightIdentity,
                               this::testMutualIdentity)
                )
                .match(IO::io,
                       s -> throwing(new AssertionError("The following Bifunctor laws did not hold for instance of " +
                                                                equivalence + ": \n\t - " + s)))
                .unsafePerformIO();
    }

    private Maybe<String> testLeftIdentity(Equivalence<Bifunctor<?, ?, BF>> equivalence) {
        return equivalence.invMap(bf -> bf.biMapL(id())).equals(equivalence)
               ? nothing()
               : just("left identity (bifunctor.biMapL(id()).equals(bifunctor))");
    }

    private Maybe<String> testRightIdentity(Equivalence<Bifunctor<?, ?, BF>> equivalence) {
        return equivalence.invMap(bf -> bf.biMapR(id())).equals(equivalence)
               ? nothing()
               : just("right identity (bifunctor.biMapR(id()).equals(bifunctor))");
    }

    private Maybe<String> testMutualIdentity(Equivalence<Bifunctor<?, ?, BF>> equivalence) {
        return equivalence.invMap(bf -> bf.biMapL(id()).biMapR(id()))
                       .equals(equivalence.invMap(bf -> bf.biMap(id(), id())))
               ? nothing()
               : just("mutual identity (bifunctor.biMapL(id()).biMapR(id()).equals(bifunctor.biMap(id(),id()))");
    }
}
