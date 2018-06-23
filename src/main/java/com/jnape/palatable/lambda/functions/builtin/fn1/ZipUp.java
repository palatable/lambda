package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.traversable.Traversable;
import com.jnape.palatable.lambda.zipper.Zipper;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;

public final class ZipUp<A, TA extends Traversable<A, ?>> implements Fn1<Zipper<A, TA>, TA> {

    private static final ZipUp INSTANCE = new ZipUp();

    private ZipUp() {
    }

    @Override
    public TA apply(Zipper<A, TA> zipper) {
        return trampoline(doneOrFocus -> doneOrFocus.match(RecursiveResult::terminate,
                                                           into((__, focusFn) -> recurse(focusFn.apply(nothing())))),
                          zipper);
    }

    @SuppressWarnings("unchecked")
    public static <A, TA extends Traversable<A, ?>> ZipUp<A, TA> zipUp() {
        return INSTANCE;
    }

    public static <A, TA extends Traversable<A, ?>> TA zipUp(Zipper<A, TA> zipper) {
        return ZipUp.<A, TA>zipUp().apply(zipper);
    }
}
