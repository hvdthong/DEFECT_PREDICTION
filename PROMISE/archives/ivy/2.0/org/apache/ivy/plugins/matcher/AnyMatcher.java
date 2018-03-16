package org.apache.ivy.plugins.matcher;

/**
 * A matcher that will match everything.
 */
public/* @Immutable */class AnyMatcher implements Matcher {
    public static final Matcher INSTANCE = new AnyMatcher();

    public AnyMatcher() {
    }

    public boolean matches(String input) {
        if (input == null) {
            throw new NullPointerException();
        }
        return true;
    }

    public boolean isExact() {
        return false;
    }

}
