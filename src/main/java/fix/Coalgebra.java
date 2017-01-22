package fix;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

@FunctionalInterface
public interface Coalgebra<A, F extends Functor<A, ?>> extends Fn1<A, F> {
}
