package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static org.junit.Assert.assertEquals;

public class OptionalLensTest {

    @Test
    public void asOptionalWrapsValuesInOptional() {
        Lens.Simple<String, Optional<String>> asOptional = OptionalLens.asOptional();

        assertEquals(Optional.of("foo"), view(asOptional, "foo"));
        assertEquals(Optional.empty(), view(asOptional, null));
        assertEquals("bar", set(asOptional, Optional.of("bar"), "foo"));
        assertEquals("foo", set(asOptional, Optional.empty(), "foo"));
    }
}