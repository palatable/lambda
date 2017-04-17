package testsupport.traits;

import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;

public class BifunctorLaws<BF extends Bifunctor> implements Trait<Bifunctor<?, ?, BF>> {

    @Override
    public void test(Bifunctor<?, ?, BF> bifunctor) {
        Iterable<Optional<String>> testResults = Map.<Function<Bifunctor<?, ?, BF>, Optional<String>>, Optional<String>>map(
                f -> f.apply(bifunctor),
                asList(this::testLeftIdentity, this::testRightIdentity, this::testMutualIdentity)
        );
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .reduceLeft(testResults)
                .ifPresent(s -> {
                    throw new AssertionError("The following Bifunctor laws did not hold for instance of " + bifunctor.getClass() + ": \n\t - " + s);
                });
    }

    private Optional<String> testLeftIdentity(Bifunctor<?, ?, BF> bifunctor) {
        return bifunctor.biMapL(id()).equals(bifunctor)
                ? Optional.empty()
                : Optional.of("left identity (bifunctor.biMapL(id()).equals(bifunctor))");
    }

    private Optional<String> testRightIdentity(Bifunctor<?, ?, BF> bifunctor) {
        return bifunctor.biMapR(id()).equals(bifunctor)
                ? Optional.empty()
                : Optional.of("right identity (bifunctor.biMapR(id()).equals(bifunctor))");
    }

    private Optional<String> testMutualIdentity(Bifunctor<?, ?, BF> bifunctor) {
        return bifunctor.biMapL(id()).biMapR(id()).equals(bifunctor.biMap(id(), id()))
                ? Optional.empty()
                : Optional.of("mutual identity (bifunctor.biMapL(id()).biMapR(id()).equals(bifunctor.biMap(id(),id()))");
    }
}
