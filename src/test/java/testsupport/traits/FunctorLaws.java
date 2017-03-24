package testsupport.traits;

import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;

public class FunctorLaws<F extends Functor> implements Trait<Functor<?, F>> {

    @Override
    public void test(Functor<?, F> f) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .reduceLeft(asList(testIdentity(f), testComposition(f)))
                .ifPresent(s -> {
                    throw new AssertionError("The following Functor laws did not hold for instance of " + f.getClass() + ": \n\t - " + s);
                });
    }

    private Optional<String> testIdentity(Functor<?, F> f) {
        return f.fmap(identity()).equals(f)
                ? Optional.empty()
                : Optional.of("identity (f.fmap(identity()).equals(f))");
    }

    private Optional<String> testComposition(Functor<?, F> functor) {
        Functor<Integer, F> subject = functor.fmap(constantly(1));
        Function<Integer, Integer> f = x -> x * 3;
        Function<Integer, Integer> g = x -> x - 2;
        return subject.fmap(f.compose(g)).equals(subject.fmap(g).fmap(f))
                ? Optional.empty()
                : Optional.of("composition (functor.fmap(f.compose(g)).equals(functor.fmap(g).fmap(f)))");
    }
}
