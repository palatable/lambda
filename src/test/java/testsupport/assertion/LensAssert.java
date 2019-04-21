package testsupport.assertion;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.lambda.optics.Optic;

import java.util.Objects;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybes.catMaybes;
import static com.jnape.palatable.lambda.functions.builtin.fn2.CartesianProduct.cartesianProduct;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ReduceLeft.reduceLeft;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Arrays.asList;

public final class LensAssert {

    public static <S, A> void assertLensLawfulness(Optic<? super Fn1<?, ?>, Functor<?, ?>, S, S, A, A> lens,
                                                   Iterable<S> ss,
                                                   Iterable<A> bs) {
        Iterable<Tuple2<S, A>> cases = cartesianProduct(ss, bs);
        Present.<String>present((x, y) -> join("\n\n", x, y))
                .reduceLeft(asList(falsify("You get back what you put in", (s, b) -> view(lens, set(lens, b, s)), (s, b) -> b, cases),
                                   falsify("Putting back what you got changes nothing", (s, b) -> set(lens, view(lens, s), s), (s, b) -> s, cases),
                                   falsify("Setting twice is equivalent to setting once", (s, b) -> set(lens, b, set(lens, b, s)), (s, b) -> set(lens, b, s), cases)))
                .peek(failures -> {throw new AssertionError("Lens law failures\n\n" + failures);});
    }

    private static <S, A, X> Maybe<String> falsify(String label, Fn2<S, A, X> l, Fn2<S, A, X> r,
                                                   Iterable<Tuple2<S, A>> cases) {
        return Map.<Tuple2<S, A>, Maybe<String>>map(into((s, b) -> {
            X x = l.apply(s, b);
            X y = r.apply(s, b);
            return Objects.equals(x, y) ? nothing() : just(format("S <%s>, B <%s> (%s != %s)", s, b, x, y));
        }))
                .fmap(catMaybes())
                .fmap(reduceLeft((x, y) -> x + "\n\t - " + y))
                .fmap(maybeFailures -> maybeFailures.fmap(failures -> "\"" + label + "\" failed for the following cases:\n\n\t - " + failures))
                .apply(cases);
    }
}
