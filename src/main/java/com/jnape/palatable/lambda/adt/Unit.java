package com.jnape.palatable.lambda.adt;

/**
 * The empty return type. Unlike {@link Void}, this type is actually inhabited by a singleton instance that can be used,
 * rather than having to resort to <code>null</code>.
 */
public final class Unit {

    /**
     * The singleton instance.
     */
    public static final Unit UNIT = new Unit();

    private Unit() {
    }

    @Override
    public String toString() {
        return "Unit{}";
    }
}
