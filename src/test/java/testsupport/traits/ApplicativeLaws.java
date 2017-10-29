package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.Random;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;

public class ApplicativeLaws<App extends Applicative> implements Trait<Applicative<?, App>> {

    @Override
    public void test(Applicative<?, App> applicative) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Function<Applicative<?, App>, Maybe<String>>>foldMap(
                        f -> f.apply(applicative),
                        asList(this::testIdentity,
                               this::testComposition,
                               this::testHomomorphism,
                               this::testInterchange,
                               this::testDiscardL,
                               this::testDiscardR)
                )
                .peek(s -> {
                    throw new AssertionError("The following Applicative laws did not hold for instance of " + applicative.getClass() + ": \n\t - " + s);
                });
    }

    private Maybe<String> testIdentity(Applicative<?, App> applicative) {
        Applicative<Integer, App> v = applicative.pure(1);
        Applicative<Function<? super Integer, ? extends Integer>, App> pureId = v.pure(identity());
        return v.zip(pureId).equals(v)
                ? nothing()
                : just("identity (v.zip(pureId).equals(v))");
    }

    private Maybe<String> testComposition(Applicative<?, App> applicative) {
        Random random = new Random();
        Integer firstInt = random.nextInt(100);
        Integer secondInt = random.nextInt(100);

        Function<? super Function<? super String, ? extends String>, ? extends Function<? super Function<? super String, ? extends String>, ? extends Function<? super String, ? extends String>>> compose = x -> x::compose;
        Applicative<Function<? super String, ? extends String>, App> u = applicative.pure(x -> x + firstInt);
        Applicative<Function<? super String, ? extends String>, App> v = applicative.pure(x -> x + secondInt);
        Applicative<String, App> w = applicative.pure("result: ");

        Applicative<Function<? super Function<? super String, ? extends String>, ? extends Function<? super Function<? super String, ? extends String>, ? extends Function<? super String, ? extends String>>>, App> pureCompose = u.pure(compose);
        return w.zip(v.zip(u.zip(pureCompose))).equals(w.zip(v).zip(u))
                ? nothing()
                : just("composition (w.zip(v.zip(u.zip(pureCompose))).equals((w.zip(v)).zip(u)))");
    }

    private Maybe<String> testHomomorphism(Applicative<?, App> applicative) {
        Function<Integer, Integer> f = x -> x + 1;
        int x = 1;

        Applicative<Integer, App> pureX = applicative.pure(x);
        Applicative<Function<? super Integer, ? extends Integer>, App> pureF = applicative.pure(f);
        Applicative<Integer, App> pureFx = applicative.pure(f.apply(x));
        return pureX.zip(pureF).equals(pureFx)
                ? nothing()
                : just("homomorphism (pureX.zip(pureF).equals(pureFx))");
    }

    private Maybe<String> testInterchange(Applicative<?, App> applicative) {
        Applicative<Function<? super Integer, ? extends Integer>, App> u = applicative.pure(x -> x + 1);
        int y = 1;

        Applicative<Integer, App> pureY = applicative.pure(y);
        return pureY.zip(u).equals(u.zip(applicative.pure(f -> f.apply(y))))
                ? nothing()
                : just("interchange (pureY.zip(u).equals(u.zip(applicative.pure(f -> f.apply(y)))))");
    }

    private Maybe<String> testDiscardL(Applicative<?, App> applicative) {
        Applicative<String, App> u = applicative.pure("u");
        Applicative<String, App> v = applicative.pure("v");

        return u.discardL(v).equals(v.zip(u.zip(applicative.pure(constantly(identity())))))
                ? nothing()
                : just("discardL u.discardL(v).equals(v.zip(u.zip(applicative.pure(constantly(identity())))))");
    }

    private Maybe<String> testDiscardR(Applicative<?, App> applicative) {
        Applicative<String, App> u = applicative.pure("u");
        Applicative<String, App> v = applicative.pure("v");

        return u.discardR(v).equals(v.zip(u.zip(applicative.pure(constantly()))))
                ? nothing()
                : just("discardR u.discardR(v).equals(v.zip(u.zip(applicative.pure(constantly()))))");
    }
}
