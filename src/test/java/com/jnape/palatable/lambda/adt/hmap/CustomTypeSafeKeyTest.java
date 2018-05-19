package com.jnape.palatable.lambda.adt.hmap;

import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.lens.Iso;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Objects;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hmap.CustomTypeSafeKeyTest.CustomTypeSafeKey.customKey;
import static com.jnape.palatable.lambda.adt.hmap.HMap.emptyHMap;
import static com.jnape.palatable.lambda.adt.hmap.HMap.singletonHMap;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static java.math.BigInteger.ONE;
import static org.junit.Assert.assertEquals;

public class CustomTypeSafeKeyTest {

    public static class CustomTypeSafeKey<T> implements TypeSafeKey<String, T> {
        int tag;
        Iso.Simple<String, T> iso;

        private CustomTypeSafeKey(int tag, Iso.Simple<String, T> iso) {
            this.tag = tag;
            this.iso = iso;
        }

        @Override
        public <P extends Profunctor, F extends Functor, FB extends Functor<T, F>, FT extends Functor<String, F>, PAFB extends Profunctor<T, FB, P>, PSFT extends Profunctor<String, FT, P>> PSFT apply(PAFB pafb) {
            return iso.<P, F, FB, FT, PAFB, PSFT>apply(pafb);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomTypeSafeKey<?> customTypeSafeKey = (CustomTypeSafeKey<?>) o;
            return Objects.equals(tag, customTypeSafeKey.tag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tag);
        }

        static <T> CustomTypeSafeKey<T> customKey(int tag, Iso.Simple<String, T> iso) {
            return new CustomTypeSafeKey<>(tag, iso);
        }

        static CustomTypeSafeKey<String> customKey(int tag) {
            return customKey(tag, Iso.simpleIso(id(), id()));
        }
    }

    private static final Iso.Simple<String, Long> STRING_TO_LONG = Iso.simpleIso(Long::parseLong, String::valueOf);
    private static final Iso.Simple<Long, BigInteger> LONG_TO_BIG_INTEGER = Iso.simpleIso(BigInteger::valueOf, BigInteger::longValue);

    private static final CustomTypeSafeKey<String> KEY_1_AS_STRING = CustomTypeSafeKey.customKey(1);
    private static final CustomTypeSafeKey<String> KEY_1_AS_STRING_AGAIN = CustomTypeSafeKey.customKey(1);
    private static final CustomTypeSafeKey<Long> KEY_1_AS_LONG = customKey(1, STRING_TO_LONG);
    private static final CustomTypeSafeKey<BigInteger> KEY_1_AS_BIG_INTEGER = customKey(1, STRING_TO_LONG.andThen(LONG_TO_BIG_INTEGER));

    @Test
    public void getDifferentViewsOfSameKey() {
        HMap hMap = singletonHMap(KEY_1_AS_STRING, "1");
        assertEquals(just("1"), hMap.get(KEY_1_AS_STRING));
        assertEquals(just("1"), hMap.get(KEY_1_AS_STRING_AGAIN));
        assertEquals(just(1L), hMap.get(KEY_1_AS_LONG));
        assertEquals(just(ONE), hMap.get(KEY_1_AS_BIG_INTEGER));
        assertEquals(nothing(), hMap.get(customKey(2)));
    }

    @Test
    public void putStoresA() {
        assertEquals(emptyHMap().put(KEY_1_AS_STRING, "1"), emptyHMap().put(KEY_1_AS_LONG, 1L));
        assertEquals(emptyHMap().put(KEY_1_AS_STRING, "1"), emptyHMap().put(KEY_1_AS_BIG_INTEGER, ONE));
    }

    @Test
    public void singletonHMapStoresA() {
        assertEquals(singletonHMap(KEY_1_AS_STRING, "1"), singletonHMap(KEY_1_AS_LONG, 1L));
        assertEquals(singletonHMap(KEY_1_AS_STRING, "1"), singletonHMap(KEY_1_AS_BIG_INTEGER, ONE));
    }
}
