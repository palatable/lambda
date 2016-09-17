package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.Index;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.lens.functions.Over.over;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.HListLens.elementAt;
import static org.junit.Assert.assertEquals;

public class HListLensTest {

    @Test
    public void elementAtFocusesOnInvariantElementAtIndex() {
        Lens.Simple<HCons<Boolean, ? extends HCons<Integer, ? extends HCons<String, ?>>>, String> lens =
                elementAt(Index.<String>index().<Integer>after().after());

        assertEquals("foo", view(lens, tuple(true, 0, "foo")));
        assertEquals(tuple(true, 0, "bar"), set(lens, "bar", tuple(true, 0, "foo")));
        assertEquals(tuple(true, 0, "FOO"), over(lens, String::toUpperCase, tuple(true, 0, "foo")));
    }

    @Test
    public void headFocusesOnHead() {
        Lens.Simple<HCons<Integer, Tuple2<String, Character>>, Integer> index = HListLens.head();

        assertEquals((Integer) 2, view(index, tuple(2, "3", '4')));
        assertEquals(tuple(0, "3", '4'), set(index, 0, tuple(2, "3", '4')));
        assertEquals(tuple(3, "3", '4'), over(index, x -> x + 1, tuple(2, "3", '4')));
    }

    @Test
    public void tailFocusesOnTail() {
        Lens.Simple<HCons<Integer, Tuple2<String, Character>>, Tuple2<String, Character>> index = HListLens.tail();

        assertEquals(tuple("3", '4'), view(index, tuple(2, "3", '4')));
        assertEquals(tuple(2, "foo", '1'), set(index, tuple("foo", '1'), tuple(2, "3", '4')));
        assertEquals(tuple(2, "FOO", 'A'), over(index, t -> t.biMap(String::toUpperCase, Character::toUpperCase), tuple(2, "foo", 'a')));
    }
}