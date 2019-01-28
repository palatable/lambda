package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.IO.io;
import static com.jnape.palatable.lambda.semigroup.builtin.RunAll.runAll;
import static org.junit.Assert.assertEquals;

public class RunAllTest {

    @Test
    public void semigroup() {
        Semigroup<Integer> add = (x, y) -> x + y;
        assertEquals((Integer) 3, runAll(add).apply(io(1), io(2)).unsafePerformIO());
    }
}