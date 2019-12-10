package testsupport.traits;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.traitor.traits.Trait;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;
import static java.util.Arrays.asList;

public class ComonadLaws<W extends Comonad<?, W>> implements Trait<Comonad<?, W>> {
    @Override
    public void test(Comonad<?, W> w) {
        Present.<String>present((x, y) -> x + "\n\t - " + y)
                .<Fn1<Comonad<?, W>, Maybe<String>>>foldMap(f -> f.apply(w), asList(
                        this::testLeftIdentity,
                        this::testRightIdentity,
                        this::testAssociativity,
                        this::testDuplicate)
                )
                .traverse(s -> IO.throwing(new AssertionError("The following Comonad laws did not hold for instance of " +
                        w.getClass() + ": \n\t - " + s)), IO::io)
                .unsafePerformIO();
    }

    private Maybe<String> testLeftIdentity(Comonad<?, W> w) {
        Fn1<Comonad<?, W>, ?> fn = Comonad::extract;
        return w.extend(fn).equals(w)
                ? nothing()
                : just("left identity w.extend(wa -> wa.extract()).equals(w)");
    }

    private Maybe<String> testRightIdentity(Comonad<?, W> w) {
        Fn1<Comonad<?, W>, Object> fn = constantly(new Object());
        return w.extend(fn).extract().equals(fn.apply(w))
                ? nothing()
                : just("right identity: w.extend(f).extract().equals(f.apply(w))");
    }

    private Maybe<String> testAssociativity(Comonad<?, W> w) {
        Fn1<Comonad<?, W>, Object> f = constantly(new Object());
        Fn1<Comonad<?, W>, Object> g = constantly(new Object());
        return w.extend(f).extend(g).equals(w.extend((Comonad<?, W> wa) -> g.apply(wa.extend(f).fmap(upcast()))))
                ? nothing()
                : just("associativity: w.extend(f).extend(g).equals(w.extend(wa -> g(wa.extend(f))))");
    }

    private Maybe<String> testDuplicate(Comonad<?, W> w) {
        Comonad<Comonad<Object, W>, W> wwa = Comonad.duplicate(w).fmap(wa -> wa.fmap(upcast()));
        boolean equals = wwa.extract().equals(w);
        return equals
                ? nothing()
                : just("duplicate: (Comonad.duplicate(w).extract()).equals(w)");
    }
}
