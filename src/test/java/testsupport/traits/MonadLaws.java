package testsupport.traits;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;

public class MonadLaws<M extends Monad> implements Trait<Monad<?, M>> {

    @Override
    public void test(Monad<?, M> m) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Function<Monad<?, M>, Optional<String>>>foldMap(f -> f.apply(m), asList(
                        this::testLeftIdentity,
                        this::testRightIdentity,
                        this::testAssociativity))
                .ifPresent(s -> {
                    throw new AssertionError("The following Monad laws did not hold for instance of " + m.getClass() + ": \n\t - " + s);
                });
    }

    private Optional<String> testLeftIdentity(Monad<?, M> m) {
        Object a = new Object();
        Fn1<Object, Monad<Object, M>> fn = id().andThen(m::pure);
        return m.pure(a).flatMap(fn).equals(fn.apply(a))
                ? Optional.empty()
                : Optional.of("left identity (m.pure(a).flatMap(fn).equals(fn.apply(a)))");
    }

    private Optional<String> testRightIdentity(Monad<?, M> m) {
        return m.flatMap(m::pure).equals(m)
                ? Optional.empty()
                : Optional.of("right identity: (m.flatMap(m::pure).equals(m))");
    }

    private Optional<String> testAssociativity(Monad<?, M> m) {
        Fn1<Object, Monad<Object, M>> f = constantly(m.pure(new Object()));
        Function<Object, Monad<Object, M>> g = constantly(m.pure(new Object()));
        return m.flatMap(f).flatMap(g).equals(m.flatMap(a -> f.apply(a).flatMap(g)))
                ? Optional.empty()
                : Optional.of("associativity: (m.flatMap(f).flatMap(g).equals(m.flatMap(a -> f.apply(a).flatMap(g))))");
    }
}
