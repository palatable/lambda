package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import org.junit.Test;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.optics.Prism.prism;
import static com.jnape.palatable.lambda.optics.Prism.simplePrism;
import static com.jnape.palatable.lambda.optics.functions.Matching.matching;
import static com.jnape.palatable.lambda.optics.functions.Pre.pre;
import static com.jnape.palatable.lambda.optics.functions.Re.re;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static org.junit.Assert.assertEquals;

public class PrismTest {

    private static final Fn1<String, Integer> PARSE_INT = Fn1.fn1(Integer::parseInt);

    @Test
    public void prismLaws() {
        Prism<String, String, Integer, Integer> prism = prism(PARSE_INT.choose(), Object::toString);

        assertEquals(just(1), view(pre(prism), view(re(prism), 1)));
        assertEquals(just(123), view(pre(prism), "123").filter(a -> view(re(prism), a).equals("123")));
        assertEquals(left("foo"), matching(prism, "foo").match(t -> matching(prism, t), Either::right));
    }

    @Test
    @SuppressWarnings("unused")
    public void simplePrismInference() {
        Prism.Simple<String, Integer> simplePrism = simplePrism(PARSE_INT.choose().fmap(CoProduct2::projectB),
                                                                Object::toString);
    }

    @Test
    public void unPrismExtractsMappings() {
        Prism<String, String, Integer, Integer>                     prism = prism(PARSE_INT.choose(), Object::toString);
        Function<? super Integer, ? extends String>                 is    = prism.unPrism()._1();
        Function<? super String, ? extends Either<String, Integer>> sis   = prism.unPrism()._2();

        assertEquals("123", is.apply(123));
        assertEquals(right(123), sis.apply("123"));
        assertEquals(left("foo"), sis.apply("foo"));
    }
}