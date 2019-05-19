package com.jnape.palatable.lambda.optics.prisms;

import org.junit.Test;

import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static testsupport.assertion.PrismAssert.assertPrismLawfulness;

public class UUIDPrismTest {

    @Test
    public void uuid() {
        UUID uuid = UUID.randomUUID();
        assertPrismLawfulness(UUIDPrism.uuid(),
                              asList("", "123", uuid.toString()),
                              singleton(uuid));
    }
}