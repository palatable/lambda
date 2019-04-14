package testsupport.traits;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.traits.Trait;

import static org.junit.Assert.assertThat;
import static testsupport.Mocking.mockIterable;
import static testsupport.matchers.ZeroInvocationsMatcher.wasNeverInteractedWith;

public class Laziness implements Trait<Fn1<Iterable<?>, Iterable<?>>> {

    @Override
    public void test(Fn1<Iterable<?>, Iterable<?>> testSubject) {
        Iterable<?> iterable = mockIterable();
        testSubject.apply(iterable);

        assertThat(iterable, wasNeverInteractedWith());
        assertThat(iterable.iterator(), wasNeverInteractedWith());
    }
}
