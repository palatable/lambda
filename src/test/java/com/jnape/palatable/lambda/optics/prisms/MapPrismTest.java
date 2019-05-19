package com.jnape.palatable.lambda.optics.prisms;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static testsupport.assertion.PrismAssert.assertPrismLawfulness;

public class MapPrismTest {

    @Test
    public void valueAtWithConstructor() {
        assertPrismLawfulness(MapPrism.valueAt(LinkedHashMap::new, "foo"),
                              asList(new LinkedHashMap<>(),
                                     new LinkedHashMap<>(singletonMap("foo", 1)),
                                     new LinkedHashMap<>(singletonMap("bar", 2))),
                              singleton(1));
    }

    @Test
    public void valueAtWithoutConstructor() {
        assertPrismLawfulness(MapPrism.valueAt("foo"),
                              asList(new HashMap<>(),
                                     new HashMap<>(singletonMap("foo", 1)),
                                     new HashMap<>(singletonMap("bar", 2))),
                              singleton(1));
    }

}