package com.jnape.palatable.lambda.semigroup;

import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;

public interface ShortCircuitingSemigroup<A> extends Semigroup<A> {
    RecursiveResult<A, A> shortCircuitApply(A a1, A a2);

    @Override
    default A checkedApply(A a, A a2) throws Throwable {
        return shortCircuitApply(a, a2).match(id(), id());
    }

    @Override
    default A foldLeft(A a, Iterable<A> as) {
        return trampoline(
                into((acc, it) -> !it.hasNext() ? terminate(acc) : shortCircuitApply(acc, it.next())
                        .<RecursiveResult<A, A>>match(RecursiveResult::recurse,
                                                      RecursiveResult::terminate)
                        .biMapL(a3 -> tuple(a3, it))),
                tuple(a, as.iterator()));
    }

    @Override
    default Lazy<A> foldRight(A a, Iterable<A> as) {
        return Semigroup.super.foldRight(a, as);
    }

}
