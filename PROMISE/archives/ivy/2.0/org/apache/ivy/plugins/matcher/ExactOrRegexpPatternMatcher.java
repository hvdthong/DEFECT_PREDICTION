package org.apache.ivy.plugins.matcher;

/**
 * A pattern matcher that tries to match exactly the input with the expression, or match it as a
 * pattern. <p/> The evaluation for matching is perform first by checking if expression and input
 * are equals (via equals method) else it attempts to do it by trying to match the input using the
 * expression as a regexp.
 * 
 * @see ExactPatternMatcher
 * @see RegexpPatternMatcher
 */
public/* @Immutable */final class ExactOrRegexpPatternMatcher extends AbstractPatternMatcher {

    public static final ExactOrRegexpPatternMatcher INSTANCE = new ExactOrRegexpPatternMatcher();

    public ExactOrRegexpPatternMatcher() {
        super(EXACT_OR_REGEXP);
    }

    protected Matcher newMatcher(String expression) {
        return new ExactOrRegexpMatcher(expression);
    }

    private static final class ExactOrRegexpMatcher implements Matcher {
        private Matcher exact;

        private Matcher regexp;

        public ExactOrRegexpMatcher(String expression) {
            exact = ExactPatternMatcher.INSTANCE.getMatcher(expression);
            regexp = RegexpPatternMatcher.INSTANCE.getMatcher(expression);
        }

        public boolean matches(String input) {
            if (input == null) {
                throw new NullPointerException();
            }
            return exact.matches(input) || regexp.matches(input);
        }

        public boolean isExact() {
        }
    }
}
