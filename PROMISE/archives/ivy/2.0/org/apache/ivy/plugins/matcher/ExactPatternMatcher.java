package org.apache.ivy.plugins.matcher;

/**
 * Implementation of an exact matcher. <p/> The matching will be performed against an expression
 * being a string. It will only matches if both strings are equal (per equals()) rule or if both
 * strings are null.
 */
public/* @Immutable */final class ExactPatternMatcher extends AbstractPatternMatcher {

    public static final ExactPatternMatcher INSTANCE = new ExactPatternMatcher();

    public ExactPatternMatcher() {
        super(EXACT);
    }

    protected Matcher newMatcher(String expression) {
        return new ExactMatcher(expression);
    }

    private static/* @Immutable */class ExactMatcher implements Matcher {
        private String expression;

        public ExactMatcher(String expression) {
            this.expression = expression;
        }

        public boolean matches(String input) {
            if (input == null) {
                throw new NullPointerException();
            }
            return input.equals(expression);
        }

        public boolean isExact() {
            return true;
        }
    }
}
