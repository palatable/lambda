package com.jnape.palatable.lambda.functions.specialized.checked;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn1.Constantly;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.specialized.checked.CheckedSupplier.checked;
import static org.junit.Assert.assertEquals;

public class CheckedSupplierTest {

    @Test
    public void assignment() {
        CheckedSupplier<Throwable, Integer> intSupplier = checked(() -> 1);
        CheckedSupplier<Throwable, String> fmap = intSupplier.fmap(Object::toString);
        @SuppressWarnings("RedundantTypeArguments")
        CheckedSupplier<Throwable, Integer> flatMap = intSupplier.flatMap(Constantly::<Integer, Unit>constantly);
        CheckedSupplier<Throwable, Integer> discardL = intSupplier.discardL(constantly(1));
        CheckedSupplier<Throwable, Integer> discardR = intSupplier.discardR(constantly(2));
        CheckedSupplier<Throwable, Integer> zipA = intSupplier.zip(constantly(id()));
        CheckedSupplier<Throwable, Unit> zipF = intSupplier.zip(constantly());
        CheckedSupplier<Throwable, Integer> diMapR = intSupplier.diMapR(id());
        CheckedSupplier<Throwable, Tuple2<Unit, Integer>> carry = intSupplier.carry();
        CheckedSupplier<Throwable, Integer> andThen = intSupplier.andThen(id());

        assertEquals((Integer) 1, intSupplier.get());
        assertEquals("1", fmap.get());
        assertEquals((Integer) 1, flatMap.get());
        assertEquals((Integer) 1, discardL.get());
        assertEquals((Integer) 1, discardR.get());
        assertEquals((Integer) 1, zipA.get());
        assertEquals(UNIT, zipF.get());
        assertEquals((Integer) 1, diMapR.get());
        assertEquals(tuple(UNIT, 1), carry.get());
        assertEquals((Integer) 1, andThen.get());
    }
}