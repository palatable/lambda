package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.optics.Iso;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.jnape.palatable.lambda.optics.Iso.iso;
import static com.jnape.palatable.lambda.optics.functions.Under.under;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class UnderTest {

    @Test
    public void reversesTypeRingWalk() {
        Iso<List<String>, Set<Integer>, String, Integer> iso = iso(xs -> xs.get(0), Collections::singleton);
        assertEquals("1", under(iso, set -> singletonList(set.iterator().next().toString()), 1));
    }
}