package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Const;
import com.jnape.palatable.lambda.functor.builtin.Identity;
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
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.optics.Lens.both;
import static com.jnape.palatable.lambda.optics.Lens.lens;
import static com.jnape.palatable.lambda.optics.Lens.simpleLens;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class LensTest {

    private static final Lens<Map<String, List<String>>, Map<String, Set<Integer>>, List<String>, Set<Integer>>
            EARLIER_LENS = lens(m -> m.get("foo"), (m, s) -> singletonMap("foo", s));
    private static final Lens<List<String>, Set<Integer>, String, Integer>
            LENS         = lens(xs -> xs.get(0), (xs, i) -> singleton(i));

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadRecLaws.class})
    public Equivalence<Lens<Map<String, Integer>, List<Integer>, Integer, String>> testSubject() {
        return equivalence(lens(m -> m.get("foo"), (m, s) -> singletonList(m.get(s))), lens -> view(lens, emptyMap()));
    }

    @Test
    public void setsUnderIdentity() {
        Set<Integer> ints = LENS.<Fn1<?, ?>, Identity<?>, Identity<Integer>, Identity<Set<Integer>>,
                Fn1<String, Identity<Integer>>, Fn1<List<String>, Identity<Set<Integer>>>>apply(
                s -> new Identity<>(s.length())).apply(asList("foo", "bar", "baz")).runIdentity();
        assertEquals(singleton(3), ints);
    }

    @Test
    public void viewsUnderConst() {
        Integer i = LENS.<Fn1<?, ?>, Const<Integer, ?>, Const<Integer, Integer>, Const<Integer, Set<Integer>>,
                Fn1<String, Const<Integer, Integer>>, Fn1<List<String>, Const<Integer, Set<Integer>>>>apply(
                s -> new Const<>(s.length())).apply(asList("foo", "bar", "baz")).runConst();
        assertEquals((Integer) 3, i);
    }

    @Test
    public void mapsIndividuallyOverParameters() {
        Lens<String, Boolean, Character, Integer> lens = lens(s -> s.charAt(0), (s, b) -> s.length() == b);
        Lens<Maybe<String>, Maybe<Boolean>, Maybe<Character>, Maybe<Integer>> theGambit = lens
                .mapS((Maybe<String> maybeS) -> maybeS.orElse(""))
                .mapT(Maybe::maybe)
                .mapA(Maybe::maybe)
                .mapB((Maybe<Integer> maybeI) -> maybeI.orElse(-1));

        assertEquals(just(true),
                     theGambit.<Fn1<?, ?>, Identity<?>, Identity<Maybe<Integer>>, Identity<Maybe<Boolean>>,
                             Fn1<Maybe<Character>, Identity<Maybe<Integer>>>,
                             Fn1<Maybe<String>, Identity<Maybe<Boolean>>>>apply(
                             maybeC -> new Identity<>(maybeC.fmap(c -> parseInt(Character.toString(c)))))
                             .apply(just("321"))
                             .runIdentity());
    }

    @Test
    public void composition() {
        Map<String, List<String>> map = singletonMap("foo", asList("one", "two", "three"));
        assertEquals("one", view(LENS.compose(EARLIER_LENS), map));
        assertEquals(singletonMap("foo", singleton(1)), set(LENS.compose(EARLIER_LENS), 1, map));
    }

    @Test
    public void andThenComposesInReverse() {
        Map<String, List<String>> map = singletonMap("foo", asList("one", "two", "three"));
        assertEquals("one", view(EARLIER_LENS.andThen(LENS), map));
        assertEquals(singletonMap("foo", singleton(1)), set(EARLIER_LENS.andThen(LENS), 1, map));
    }

    @Test
    public void bothSplitsFocusBetweenLenses() {
        Lens<String, String, Character, Character>                                   firstChar = simpleLens(s -> s.charAt(0), (s, c) -> c + s.substring(1));
        Lens<String, String, Integer, Integer>                                       length    = simpleLens(String::length, (s, k) -> s.substring(0, k));
        Lens<String, String, Tuple2<Character, Integer>, Tuple2<Character, Integer>> both      = both(firstChar, length);

        assertEquals(tuple('a', 3), view(both, "abc"));
        assertEquals("zb", set(both, tuple('z', 2), "abc"));
    }

    @Test
    public void bothForSimpleLenses() {
        Lens.Simple<String, Integer>   stringToInt  = simpleLens(Integer::parseInt, (s, i) -> s + i.toString());
        Lens.Simple<String, Character> stringToChar = simpleLens(s -> s.charAt(0), (s, c) -> s + c.toString());

        assertEquals(tuple(3, '3'), view(both(stringToInt, stringToChar), "3"));
        assertEquals("133", set(both(stringToInt, stringToChar), tuple(3, '3'), "1"));
    }

    @Test
    public void toIso() {
        Iso<List<String>, Set<Integer>, String, Integer> iso = LENS.toIso(singletonList(""));
        assertEquals("1", view(iso, asList("1", "2", "3")));
        assertEquals(singleton(1), view(iso.mirror(), 1));
    }
}