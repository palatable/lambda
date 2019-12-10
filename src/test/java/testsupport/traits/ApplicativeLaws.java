package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monoid.builtin.Present;

import java.util.Random;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.io.IO.throwing;
import static java.util.Arrays.asList;

public class ApplicativeLaws<App extends Applicative<?, App>> implements EquivalenceTrait<Applicative<?, App>> {

    @Override
    public Class<? super Applicative<?, App>> type() {
        return Applicative.class;
    }

    @Override
    public void test(Equivalence<Applicative<?, App>> equivalence) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Equivalence<Applicative<?, App>>, Maybe<String>>>foldMap(
                        f -> f.apply(equivalence),
                        asList(this::testIdentity,
                               this::testComposition,
                               this::testHomomorphism,
                               this::testInterchange,
                               this::testDiscardL,
                               this::testDiscardR,
                               this::testLazyZip))
                .match(IO::io,
                       s -> throwing(new AssertionError("The following Applicative laws did not hold for instance of "
                                                                + equivalence + ": \n\t - " + s)))
                .unsafePerformIO();
    }

    private Maybe<String> testIdentity(Equivalence<Applicative<?, App>> equivalence) {
        return equivalence.invMap(app -> app.zip(app.pure(id()))).equals(equivalence)
               ? nothing()
               : just("identity (v.zip(pureId).equals(v))");
    }

    private Maybe<String> testComposition(Equivalence<Applicative<?, App>> equivalence) {
        Random  random    = new Random();
        Integer firstInt  = random.nextInt(100);
        Integer secondInt = random.nextInt(100);

        return equivalence.invMap(app -> app.pure("result: ")
                .zip(app.<Fn1<? super String, ? extends String>>pure(x1 -> x1 + secondInt)
                             .zip(app.<Fn1<? super String, ? extends String>>pure(x1 -> x1 + firstInt)
                                          .zip(app.pure(x1 -> x1::contraMap)))))
                       .equals(equivalence.invMap(app -> app.pure("result: ")
                               .zip(app.<Fn1<? super String, ? extends String>>pure(x -> x + secondInt))
                               .zip(app.<Fn1<? super String, ? extends String>>pure(x -> x + firstInt))))
               ? nothing()
               : just("composition (w.zip(v.zip(u.zip(pureCompose))).equals((w.zip(v)).zip(u)))");
    }

    private Maybe<String> testHomomorphism(Equivalence<Applicative<?, App>> equivalence) {
        Fn1<Integer, Integer> f = x -> x + 1;
        int                   x = 1;

        return equivalence.invMap(app -> app.pure(x).zip(app.pure(f)))
                       .equals(equivalence.invMap(app -> app.pure(f.apply(x))))
               ? nothing()
               : just("homomorphism (pureX.zip(pureF).equals(pureFx))");
    }

    private Maybe<String> testInterchange(Equivalence<Applicative<?, App>> equivalence) {
        int y = 1;
        return equivalence.invMap(app -> app.pure(y).zip(app.pure(x -> x + 1)))
                       .equals(equivalence.invMap(app -> app.<Fn1<? super Integer, ? extends Integer>>pure(x -> x + 1)
                               .zip(app.pure(f -> f.apply(y)))))
               ? nothing()
               : just("interchange (pureY.zip(u).equals(u.zip(applicative.pure(f -> f.apply(y)))))");
    }

    private Maybe<String> testDiscardL(Equivalence<Applicative<?, App>> equivalence) {
        return equivalence.invMap(app -> app.pure("u").discardL(app.pure("v")))
                       .equals(equivalence.invMap(app -> app.pure("v")
                               .zip(app.pure("u").zip(app.pure(constantly(id()))))))
               ? nothing()
               : just("discardL u.discardL(v).equals(v.zip(u.zip(applicative.pure(constantly(identity())))))");
    }

    private Maybe<String> testDiscardR(Equivalence<Applicative<?, App>> equivalence) {
        return equivalence.invMap(app -> app.pure("u").discardR(app.pure("v")))
                       .equals(equivalence.invMap(app -> app.pure("v").zip(app.pure("u").zip(app.pure(constantly())))))
               ? nothing()
               : just("discardR u.discardR(v).equals(v.zip(u.zip(applicative.pure(constantly()))))");
    }

    private Maybe<String> testLazyZip(Equivalence<Applicative<?, App>> equivalence) {
        return equivalence.invMap(app -> app.lazyZip(lazy(app.pure(id()))).value())
                       .equals(equivalence.invMap(app -> app.zip(app.pure(id()))))
               ? nothing()
               : just("lazyZip app.zip(lazy(app.pure(id()))).equals(app.zip(app.pure(id())))");
    }
}
