package com.jnape.palatable.lambda.io;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.monad.Monad;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.io.IO.memoize;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class MVarTest {

    @Test(timeout = 1000)
    public void putTake() {
        assertThat(MVar.<String>newMVar()
                        .flatMap(mvar -> mvar.put("foo"))
                        .flatMap(MVar::take),
                yieldsValue(equalTo("foo")));
    }


    @Test(timeout = 1000)
    public void putPutTakeTake() {
        IO<MVar<Integer>> mVarIO = memoize(MVar.newMVar());

        IO<Tuple2<Integer, Integer>> join = Monad.join(mVarIO.flatMap(mvar1 -> mvar1.take().fmap(tupler(mvar1)))
                .fmap(into((mvar, i) -> mvar.take().fmap(tupler(i)))));

        mVarIO.flatMap(mvar -> mvar.put(1)).flatMap(mvar -> mvar.put(2)).discardL(join)
                .flatMap(t2 -> io(() -> System.out.println(t2)))
                .unsafePerformAsyncIO()
                .join();
    }
}