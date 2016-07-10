package testsupport.traits;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class ImmutableIteration implements Trait<Fn1<Iterable, Iterable>> {

    @Override
    public void test(Fn1<Iterable, Iterable> testSubject) {
        Iterable result = testSubject.apply(new ArrayList());
        try {
            result.iterator().remove();
            fail("Expected remove() to throw Exception, but it didn't.");
        } catch (Exception ignored) {
        }
    }
}
