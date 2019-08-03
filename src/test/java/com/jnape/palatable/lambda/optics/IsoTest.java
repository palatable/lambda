package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;

import java.util.List;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.optics.Iso.iso;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class IsoTest {

    private static final Iso<String, List<Character>, Integer, Double> ISO =
            iso(Integer::parseInt, dbl -> dbl.toString().chars().mapToObj(x -> (char) x).collect(toList()));

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadRecLaws.class})
    public Equivalence<Iso<String, List<Character>, Integer, Double>> testSubject() {
        return equivalence(ISO, iso -> view(iso, "123"));
    }

    @Test
    public void lensLike() {
        assertEquals((Integer) 123, view(ISO, "123"));
        assertEquals(asList('1', '.', '2', '3'), set(ISO, 1.23d, "234"));
    }

    @Test
    public void mirrorFlipsIso() {
        assertEquals(asList('1', '.', '2', '3'), view(ISO.mirror(), 1.23d));
        assertEquals((Integer) 240, set(ISO.mirror(), "240", 5.67d));
    }

    @Test
    public void mapsIndividuallyOverParameters() {
        Iso<Maybe<String>, Maybe<List<Character>>, Maybe<Integer>, Maybe<Double>> mapped = ISO
                .mapS((Maybe<String> maybeS) -> maybeS.orElse(""))
                .mapT(Maybe::maybe)
                .mapA(Maybe::maybe)
                .mapB((Maybe<Double> maybeD) -> maybeD.orElse(-1d));

        assertEquals(just(1), view(mapped, just("1")));
        assertEquals(just(asList('1', '.', '2')), view(mapped.mirror(), just(1.2d)));
    }

    @Test
    public void staticPure() {
        Iso<String, Character, Integer, Boolean> iso = Iso.<String, Integer, Boolean>pureIso(String::length).apply('1');
        assertEquals((Integer) 3, view(iso, "foo"));
        assertEquals((Character) '1', view(iso.mirror(), true));
    }
}