package testsupport.traits;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.lambda.traversable.Traversable;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;

public class TraversableLaws<Trav extends Traversable<?, Trav>> implements EquivalenceTrait<Traversable<?, Trav>> {

    @Override
    public Class<? super Traversable<?, Trav>> type() {
        return Traversable.class;
    }

    @Override
    public void test(Equivalence<Traversable<?, Trav>> equivalence) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Equivalence<Traversable<?, Trav>>, Maybe<String>>>foldMap(
                        f -> f.apply(equivalence),
                        asList(this::testNaturality,
                               this::testIdentity,
                               this::testComposition)
                )
                .peek(s -> IO.throwing(new AssertionError("The following Traversable laws did not hold for instance of "
                                                                  + equivalence + ": \n\t - " + s)));
    }

    @SuppressWarnings("unchecked")
    private Maybe<String> testNaturality(Equivalence<Traversable<?, Trav>> equivalence) {
        Fn1<Object, Identity<Object>>                 f = Identity::new;
        Fn1<Identity<Object>, Either<String, Object>> t = id -> right(id.runIdentity());

        Fn1<Traversable<Object, Trav>, Identity<Traversable<Object, Trav>>>       pureFn  = Identity::new;
        Fn1<Traversable<Object, Trav>, Either<String, Traversable<Object, Trav>>> pureFn2 = Either::right;

        Traversable<?, Trav> trav = equivalence.getValue();
        return t.apply(trav.traverse(f, pureFn).fmap(id())).fmap(value -> equivalence.swap((Traversable<?, Trav>) value))
                       .equals(trav.traverse(t.contraMap(f), pureFn2)
                                       .fmap(equivalence::swap))
               ? nothing()
               : just("naturality (t.apply(trav.traverse(f, pureFn).<Object>fmap(id()).coerce())\n" +
                              "                .equals(trav.traverse(t.contraMap(f), pureFn2)" +
                              ".<Object>fmap(id()).coerce()))");
    }

    private Maybe<String> testIdentity(Equivalence<Traversable<?, Trav>> equivalence) {
        Traversable<?, Trav> trav = equivalence.getValue();
        return trav.traverse(Identity::new, Identity::new).fmap(equivalence::swap)
                       .equals(new Identity<>(trav).fmap(equivalence::swap))
               ? nothing()
               : just("identity (trav.traverse(Identity::new, x -> new Identity<>(x)).equals(new Identity<>(trav))");
    }

    private Maybe<String> testComposition(Equivalence<Traversable<?, Trav>> equivalence) {
        Fn1<Object, Identity<Object>>                 f = Identity::new;
        Fn1<Object, Applicative<Object, Identity<?>>> g = Identity::new;

        Traversable<?, Trav> trav = equivalence.getValue();
        return trav.traverse(f.fmap(x -> x.fmap(g)).fmap(Compose::new),
                             x -> new Compose<>(new Identity<>(new Identity<>(x))))
                       .fmap(equivalence::swap)
                       .equals(new Compose<>(trav.traverse(f, Identity::new)
                                                     .fmap(t -> t.traverse(g, Identity::new)))
                                       .fmap(equivalence::swap))
               ? nothing()
               : just("compose (trav.traverse(f.fmap(x -> x.fmap(g)).fmap(Compose::new), x -> new Compose<>(" +
                              "new Identity<>(new Identity<>(x))))\n" +
                              "                .equals(new Compose<Identity, Identity, Traversable<Object, Trav>>(" +
                              "trav.traverse(f, x -> new Identity<>(x))" +
                              ".fmap(t -> t.traverse(g, x -> new Identity<>(x))))))");
    }
}
