package org.apache.ivy.plugins.matcher;

/**
 * A matcher that matches nothing.
 */
public final/* @Immutable */class NoMatcher implements Matcher {

    public static final Matcher INSTANCE = new NoMatcher();

    public NoMatcher() {
    }

    public boolean matches(String input) {
        if (input == null) {
            throw new NullPointerException();
        }
        return false;
    }

    public boolean isExact() {
        return true;
    }

}
