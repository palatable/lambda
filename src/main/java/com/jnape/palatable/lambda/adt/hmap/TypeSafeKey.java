package com.jnape.palatable.lambda.adt.hmap;

/**
 * An interface representing a parametrized key for use in HMaps. The parameter specifies the type of the value stored
 * at this binding inside the HMap.
 * <p>
 * This is intentionally an interface so user-defined implementations are possible; however, it's important to note that
 * all hopes of type-safety hinge on equality being implemented such that no two TypeSafeKeys with differing parameters
 * may be considered equal. Reference equality is used here as the default, as that is sufficient.
 *
 * @param <T> The type of the value that this key maps to inside an HMap
 */
public interface TypeSafeKey<T> {

    /**
     * Static factory method for creating a unique type-safe key
     *
     * @param <T> the type of value stored at this key
     * @return a unique type-safe key
     */
    static <T> TypeSafeKey<T> typeSafeKey() {
        return new TypeSafeKey<T>() {
        };
    }
}
