package testsupport.traits;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class ImmutableIteration implements Trait<MonadicFunction<Iterable, Iterable>> {

    @Override
    public void test(MonadicFunction<Iterable, Iterable> testSubject) {
        Iterable result = testSubject.apply(new ArrayList());
        try {
            result.iterator().remove();
            fail("Expected remove() to throw Exception, but it didn't.");
        } catch (Exception ignored) {
        }
    }
}
