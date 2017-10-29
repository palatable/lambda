package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;

public class BifunctorLaws<BF extends Bifunctor> implements Trait<Bifunctor<?, ?, BF>> {

    @Override
    public void test(Bifunctor<?, ?, BF> bifunctor) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Function<Bifunctor<?, ?, BF>, Maybe<String>>>foldMap(
                        f -> f.apply(bifunctor),
                        asList(this::testLeftIdentity,
                               this::testRightIdentity,
                               this::testMutualIdentity)
                )
                .peek(s -> {
                    throw new AssertionError("The following Bifunctor laws did not hold for instance of " + bifunctor.getClass() + ": \n\t - " + s);
                });
    }

    private Maybe<String> testLeftIdentity(Bifunctor<?, ?, BF> bifunctor) {
        return bifunctor.biMapL(id()).equals(bifunctor)
                ? nothing()
                : just("left identity (bifunctor.biMapL(id()).equals(bifunctor))");
    }

    private Maybe<String> testRightIdentity(Bifunctor<?, ?, BF> bifunctor) {
        return bifunctor.biMapR(id()).equals(bifunctor)
                ? nothing()
                : just("right identity (bifunctor.biMapR(id()).equals(bifunctor))");
    }

    private Maybe<String> testMutualIdentity(Bifunctor<?, ?, BF> bifunctor) {
        return bifunctor.biMapL(id()).biMapR(id()).equals(bifunctor.biMap(id(), id()))
                ? nothing()
                : just("mutual identity (bifunctor.biMapL(id()).biMapR(id()).equals(bifunctor.biMap(id(),id()))");
    }
}
