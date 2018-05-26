package com.jnape.palatable.lambda.functions.recursion;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import org.junit.Test;

import java.math.BigInteger;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;
import static java.math.BigInteger.ONE;
import static org.junit.Assert.assertEquals;

public class TrampolineTest {

    private static final Function<Tuple2<BigInteger, BigInteger>, RecursiveResult<Tuple2<BigInteger, BigInteger>, BigInteger>> FACTORIAL =
            into((x, acc) -> x.compareTo(ONE) > 0 ? recurse(tuple(x.subtract(ONE), x.multiply(acc))) : terminate(acc));

    @Test
    public void trampolinesCompatibleFunctionIntoResult() {
        assertEquals(BigInteger.valueOf(3628800), trampoline(FACTORIAL, tuple(BigInteger.valueOf(10), ONE)));
    }

    @Test
    public void stackSafety() {
        trampoline(FACTORIAL, tuple(BigInteger.valueOf(10000), ONE));
    }
}