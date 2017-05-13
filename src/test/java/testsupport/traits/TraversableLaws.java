package testsupport.traits;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.lambda.traversable.Traversable;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;

@SuppressWarnings("Convert2MethodRef")
public class TraversableLaws<Trav extends Traversable> implements Trait<Traversable<?, Trav>> {

    @Override
    public void test(Traversable<?, Trav> traversable) {
        Iterable<Optional<String>> testResults = Map.<Function<Traversable<?, Trav>, Optional<String>>, Optional<String>>map(
                f -> f.apply(traversable),
                asList(this::testNaturality, this::testIdentity, this::testComposition)
        );
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .reduceLeft(testResults)
                .ifPresent(s -> {
                    throw new AssertionError("The following Traversable laws did not hold for instance of " + traversable.getClass() + ": \n\t - " + s);
                });
    }

    private Optional<String> testNaturality(Traversable<?, Trav> trav) {
        Function<Object, Identity<Object>> f = Identity::new;
        Function<Identity<Object>, Either<String, Object>> t = id -> right(id.runIdentity());

        Function<Traversable<Object, Trav>, Applicative<Traversable<Object, Trav>, Identity>> pureFn = x -> new Identity<>(x);
        Function<Traversable<Object, Trav>, Applicative<Traversable<Object, Trav>, Either<String, ?>>> pureFn2 = x -> right(x);

        return t.apply(trav.traverse(f, pureFn).<Object>fmap(id()).coerce())
                .equals(trav.traverse(t.compose(f), pureFn2).<Object>fmap(id()).coerce())
                ? Optional.empty()
                : Optional.of("naturality (t.apply(trav.traverse(f, pureFn).<Object>fmap(id()).coerce())\n" +
                                      "                .equals(trav.traverse(t.compose(f), pureFn2).<Object>fmap(id()).coerce()))");
    }

    private Optional<String> testIdentity(Traversable<?, Trav> trav) {
        return trav.traverse(Identity::new, x -> new Identity<>(x)).equals(new Identity<>(trav))
                ? Optional.empty()
                : Optional.of("identity (trav.traverse(Identity::new, x -> new Identity<>(x)).equals(new Identity<>(trav))");
    }

    @SuppressWarnings("unchecked")
    private Optional<String> testComposition(Traversable<?, Trav> trav) {
        Function<Object, Identity<Object>> f = Identity::new;
        Function<Object, Applicative<Object, Identity>> g = x -> new Identity<>(x);

        return trav.traverse(f.andThen(x -> x.fmap(g)).andThen(Compose::new), x -> new Compose<>(new Identity<>(new Identity<>(x))))
                .equals(new Compose<Identity, Identity, Traversable<Object, Trav>>(trav.traverse(f, x -> new Identity<>(x)).fmap(t -> t.traverse(g, x -> new Identity<>(x)))))
                ? Optional.empty()
                : Optional.of("compose (trav.traverse(f.andThen(x -> x.fmap(g)).andThen(Compose::new), x -> new Compose<>(new Identity<>(new Identity<>(x))))\n" +
                                      "                .equals(new Compose<Identity, Identity, Traversable<Object, Trav>>(trav.traverse(f, x -> new Identity<>(x)).fmap(t -> t.traverse(g, x -> new Identity<>(x))))))");
    }
}
