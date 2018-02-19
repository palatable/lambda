package testsupport.traits;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.traits.Trait;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;

public final class Deforesting implements Trait<Fn1<Iterable<?>, Iterable<?>>> {

    @Override
    public void test(Fn1<Iterable<?>, Iterable<?>> fn) {
        times(10_000, fn, repeat(1)).iterator().next();
    }
}
