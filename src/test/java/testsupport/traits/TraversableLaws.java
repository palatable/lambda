package testsupport.traits;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.lambda.traversable.Traversable;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;

@SuppressWarnings("Convert2MethodRef")
public class TraversableLaws<Trav extends Traversable<?, Trav>> implements Trait<Traversable<?, Trav>> {

    @Override
    public void test(Traversable<?, Trav> traversable) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Traversable<?, Trav>, Maybe<String>>>foldMap(
                        f -> f.apply(traversable),
                        asList(this::testNaturality,
                               this::testIdentity,
                               this::testComposition)
                )
                .peek(s -> {
                    throw new AssertionError("The following Traversable laws did not hold for instance of "
                                                     + traversable.getClass() + ": \n\t - " + s);
                });
    }

    private Maybe<String> testNaturality(Traversable<?, Trav> trav) {
        Fn1<Object, Identity<Object>>                 f = Identity::new;
        Fn1<Identity<Object>, Either<String, Object>> t = id -> right(id.runIdentity());

        Fn1<Traversable<Object, Trav>, Applicative<Traversable<Object, Trav>, Identity<?>>> pureFn =
                x -> new Identity<>(x);
        Fn1<Traversable<Object, Trav>, Applicative<Traversable<Object, Trav>, Either<String, ?>>> pureFn2 =
                x -> right(x);

        return t.apply(trav.traverse(f, pureFn).<Object>fmap(id()).coerce())
                       .equals(trav.traverse(t.contraMap(f), pureFn2).<Object>fmap(id()).coerce())
               ? nothing()
               : just("naturality (t.apply(trav.traverse(f, pureFn).<Object>fmap(id()).coerce())\n" +
                              "                .equals(trav.traverse(t.contraMap(f), pureFn2).<Object>fmap(id()).coerce()))");
    }

    private Maybe<String> testIdentity(Traversable<?, Trav> trav) {
        return trav.traverse(Identity::new, x -> new Identity<>(x)).equals(new Identity<>(trav))
               ? nothing()
               : just("identity (trav.traverse(Identity::new, x -> new Identity<>(x)).equals(new Identity<>(trav))");
    }

    private Maybe<String> testComposition(Traversable<?, Trav> trav) {
        Fn1<Object, Identity<Object>>                 f = Identity::new;
        Fn1<Object, Applicative<Object, Identity<?>>> g = x -> new Identity<>(x);

        return trav.traverse(f.fmap(x -> x.fmap(g)).fmap(Compose::new), x -> new Compose<>(new Identity<>(new Identity<>(x))))
                       .equals(new Compose<>(trav.traverse(f, x -> new Identity<>(x)).fmap(t -> t.traverse(g, x -> new Identity<>(x)))))
               ? nothing()
               : just("compose (trav.traverse(f.fmap(x -> x.fmap(g)).fmap(Compose::new), x -> new Compose<>(new Identity<>(new Identity<>(x))))\n" +
                              "                .equals(new Compose<Identity, Identity, Traversable<Object, Trav>>(trav.traverse(f, x -> new Identity<>(x)).fmap(t -> t.traverse(g, x -> new Identity<>(x))))))");
    }
}
