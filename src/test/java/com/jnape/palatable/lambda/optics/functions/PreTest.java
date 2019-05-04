package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.optics.Iso;
import com.jnape.palatable.lambda.optics.Prism;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.optics.Iso.iso;
import static com.jnape.palatable.lambda.optics.Prism.prism;
import static com.jnape.palatable.lambda.optics.functions.Pre.pre;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;

public class PreTest {

    @Test
    public void focusOnAtMostOneValue() {
        Iso<String, CharSequence, Number, Integer> iso = iso(Integer::parseInt, Object::toString);
        Prism<String, CharSequence, Number, Integer> prism = prism(s -> Either.trying(() -> parseInt(s),
                                                                                      constantly(s)),
                                                                   Object::toString);
        assertEquals(just(1), view(pre(prism), "1"));
        assertEquals(nothing(), view(pre(prism), "foo"));
        assertEquals(just(1), view(pre(iso), "1"));
    }
}