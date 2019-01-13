package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.Assert.assertEquals;

public class CheckedFn1Test {

    @Test
    public void assignment() {
        CheckedFn1<Throwable, String, Integer> parseInt = Integer::parseInt;
        CheckedFn1<Throwable, String, String> fmap = parseInt.fmap(Object::toString);
        CheckedFn1<Throwable, String, String> flatMap = parseInt.flatMap(i -> constantly(i + ""));
        CheckedFn1<Throwable, String, String> discardL = parseInt.discardL(constantly("0"));
        CheckedFn1<Throwable, String, Integer> discardR = parseInt.discardR(constantly("0"));
        CheckedFn1<Throwable, String, Integer> zipApp = parseInt.zip(constantly(id()));
        CheckedFn1<Throwable, String, String> zipF = parseInt.zip(constantly());
        CheckedFn1<Throwable, String, Integer> diMapL = parseInt.diMapL(id());
        CheckedFn1<Throwable, String, Integer> diMapR = parseInt.diMapR(id());
        CheckedFn1<Throwable, String, Integer> diMap = parseInt.diMap(id(), id());
        CheckedFn1<Throwable, Tuple2<Object, String>, Tuple2<Object, Integer>> strengthen = parseInt.strengthen();
        CheckedFn1<Throwable, String, Tuple2<String, Integer>> carry = parseInt.carry();
        CheckedFn1<Throwable, String, Integer> contraMap = parseInt.contraMap(id());
        CheckedFn1<Throwable, String, Integer> compose = parseInt.compose(id());
        CheckedFn1<Throwable, String, Integer> andThen = parseInt.andThen(id());

        assertEquals((Integer) 1, parseInt.apply("1"));
        assertEquals("1", fmap.apply("1"));
        assertEquals("1", flatMap.apply("1"));
        assertEquals("0", discardL.apply("1"));
        assertEquals((Integer) 1, discardR.apply("1"));
        assertEquals((Integer) 1, zipApp.apply("1"));
        assertEquals("1", zipF.apply("1"));
        assertEquals((Integer) 1, diMapL.apply("1"));
        assertEquals((Integer) 1, diMapR.apply("1"));
        assertEquals((Integer) 1, diMap.apply("1"));
        assertEquals(tuple("foo", 1), strengthen.apply(tuple("foo", "1")));
        assertEquals(tuple("1", 1), carry.apply("1"));
        assertEquals((Integer) 1, contraMap.apply("1"));
        assertEquals((Integer) 1, compose.apply("1"));
        assertEquals((Integer) 1, andThen.apply("1"));
    }
}