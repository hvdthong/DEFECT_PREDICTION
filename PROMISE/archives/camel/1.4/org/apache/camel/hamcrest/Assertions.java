package org.apache.camel.hamcrest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * A set of useful assertions you can use when testing
 *
 * @version $Revision: 656656 $
 */
public final class Assertions {
    private Assertions() {
    }

    /**
     * Performs the assertion that the given value is an instance of the specified type
     *
     * @param value the value to be compared
     * @param type  the type to assert
     * @return the value cast as the type
     * @throws AssertionError if the instance is not of the correct type
     */
    public static <T> T assertInstanceOf(Object value, Class<T> type) {
        assertThat(value, instanceOf(type));
        return type.cast(value);
    }

    /**
     * Performs the assertion that the given value is an instance of the specified type
     *
     * @param message the description of the value
     * @param value   the value to be compared
     * @param type    the type to assert
     * @return the value cast as the type
     * @throws AssertionError if the instance is not of the correct type
     */
    public static <T> T assertInstanceOf(String message, Object value, Class<T> type) {
        assertThat(message, value, instanceOf(type));
        return type.cast(value);
    }
}
