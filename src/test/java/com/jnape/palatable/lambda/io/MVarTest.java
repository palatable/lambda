package com.jnape.palatable.lambda.io;

import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

@RunWith(Traits.class)
public class MVarTest {

    @Test(timeout = 100)
    public void putTake() {
        assertThat(MVar.<String>newMVar()
                           .flatMap(mvar -> mvar.put("foo"))
                           .flatMap(MVar::take),
                   yieldsValue(equalTo("foo")));
    }
}