package com.jnape.palatable.lambda.iterables;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.iterators.ImmutableIterator;

import java.util.Iterator;

public class MappingIterable<Input, Output> implements Iterable<Output> {

    private final MonadicFunction<? super Input, ? extends Output> function;
    private final Iterable<Input>                                  xs;

    public MappingIterable(MonadicFunction<? super Input, ? extends Output> function, Iterable<Input> xs) {
        this.xs = xs;
        this.function = function;
    }

    @Override
    public Iterator<Output> iterator() {
        final Iterator<Input> xsIterator = xs.iterator();

        return new ImmutableIterator<Output>() {
            @Override
            public boolean hasNext() {
                return xsIterator.hasNext();
            }

            @Override
            public Output next() {
                return function.apply(xsIterator.next());
            }
        };
    }

    public static <Input, Output> MappingIterable<Input, Output> map(
            MonadicFunction<? super Input, ? extends Output> function,
            Iterable<Input> xs) {
        return new MappingIterable<Input, Output>(function, xs);
    }
}
