package testsupport.traits;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.concurrent.CountDownLatch;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.fail;

public class InfiniteIterableSupport implements Trait<Fn1<Iterable<?>, Iterable<?>>> {

    @Override
    public void test(Fn1<Iterable<?>, Iterable<?>> testSubject) {
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            testSubject.apply(repeat(0)).iterator().next();
            latch.countDown();
        }).start();
        try {
            if (!latch.await(1, SECONDS))
                fail("Termination when given an infinite iterable could not be proven in time");
        } catch (InterruptedException e) {
            throw new AssertionError("interrupted", e);
        }
    }
}
