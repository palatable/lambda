package com.jnape.palatable.lambda.functions.builtin.fn0;

import com.jnape.palatable.lambda.functions.Fn0;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;

public class NaturalNumbers implements Fn0<Iterable<Integer>> {

    private static final NaturalNumbers INSTANCE = new NaturalNumbers();

    private NaturalNumbers() {
    }

    @Override
    public Iterable<Integer> checkedApply() {
        return iterate(x -> x + 1, 1);
    }

    public static Iterable<Integer> naturalNumbers() {
        return INSTANCE.apply();
    }
}
