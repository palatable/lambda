package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monoid.builtin.Present;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.util.Arrays.asList;

public class FunctorLaws<F extends Functor<?, F>> implements EquivalenceTrait<Functor<?, F>> {

    @Override
    public Class<? super Functor<?, F>> type() {
        return Functor.class;
    }

    @Override
    public void test(Equivalence<Functor<?, F>> equivalence) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Equivalence<Functor<?, F>>, Maybe<String>>>foldMap(
                        fn -> fn.apply(equivalence),
                        asList(this::testIdentity,
                               this::testComposition))
                .peek(s -> IO.throwing(new AssertionError("The following Functor laws did not hold for instance of " +
                                                                  equivalence + ": \n\t - " + s)));
    }

    private Maybe<String> testIdentity(Equivalence<Functor<?, F>> equivalence) {
        return equivalence.invMap(f -> f.fmap(id())).equals(equivalence)
               ? nothing()
               : just("identity (f.fmap(identity()).equals(f))");
    }

    private Maybe<String> testComposition(Equivalence<Functor<?, F>> equivalence) {
        Fn1<Integer, Integer> g = x -> x * 3;
        Fn1<Integer, Integer> h = x -> x - 2;
        return equivalence.invMap(f -> f.fmap(constantly(1)).fmap(g).fmap(h))
                       .equals(equivalence.invMap(f -> f.fmap(constantly(1)).fmap(g.fmap(h))))
               ? nothing()
               : just("composition (functor.fmap(f.contraMap(g)).equals(functor.fmap(g).fmap(f)))");
    }
}
