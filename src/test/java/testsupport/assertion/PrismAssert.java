package testsupport.assertion;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monoid.builtin.Present;
import com.jnape.palatable.lambda.optics.Prism;

import java.util.Objects;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.Fn2.fn2;
import static com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybes.catMaybes;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.CartesianProduct.cartesianProduct;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ReduceLeft.reduceLeft;
import static com.jnape.palatable.lambda.optics.functions.Matching.matching;
import static com.jnape.palatable.lambda.optics.functions.Pre.pre;
import static com.jnape.palatable.lambda.optics.functions.Re.re;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Arrays.asList;

public final class PrismAssert {

    public static <S, A> void assertPrismLawfulness(Prism<S, S, A, A> prism,
                                                    Iterable<S> ss,
                                                    Iterable<A> bs) {
        Iterable<Tuple2<S, A>> cases = cartesianProduct(ss, bs);
        Present.<String>present((x, y) -> join("\n\n", x, y))
                .reduceLeft(asList(falsify("The result of a review can always be successfully previewed:",
                                           (s, b) -> view(pre(prism), view(re(prism), b)), (s, b) -> just(b), cases),
                                   falsify("If I can preview a value from an input, I can review the input to the value",
                                           (s, b) -> new PrismResult<>(view(pre(prism), s).fmap(constantly(s))),
                                           (s, b) -> new PrismResult<>(just(view(re(prism), b))), cases),
                                   falsify("A non-match result can always be converted back to an input",
                                           (s, b) -> new PrismResult<>(matching(prism, s).projectA().fmap(matching(prism))),
                                           (s, b) -> new PrismResult<>(just(left(s))), cases)))
                .match(IO::io, failures -> IO.throwing(new AssertionError("Lens law failures\n\n" + failures)))
                .unsafePerformIO();
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

    private record PrismResult<S>(Maybe<S> maybeS) {

        @Override
            public boolean equals(Object other) {
                if (other instanceof PrismResult) {
                    return maybeS.zip(((PrismResult<?>) other).maybeS.fmap(fn2(Objects::equals))).orElse(true);
                }
                return false;
            }

        @Override
            public String toString() {
                return maybeS.toString();
            }
        }
}
