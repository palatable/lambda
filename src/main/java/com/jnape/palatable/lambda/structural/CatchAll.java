package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.functions.specialized.Predicate;

//todo: this sort of sucks having to have a separate type to distinguish this, but maybe can't be helped
public final class CatchAll implements Predicate<Object> {

    public static final CatchAll __ = new CatchAll();

    private CatchAll() {
    }

    @Override
    public Boolean apply(Object o) {
        return true;
    }
}
