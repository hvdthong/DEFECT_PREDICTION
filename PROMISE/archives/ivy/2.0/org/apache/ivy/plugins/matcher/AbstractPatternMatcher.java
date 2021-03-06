package org.apache.ivy.plugins.matcher;

/**
 * An abstract implementation of the pattern matcher providing base template methods
 */
public abstract class AbstractPatternMatcher implements PatternMatcher {
    private final String name;

    /**
     * Create a new instance of a pattern matcher
     * 
     * @param name
     *            the name of the pattern matcher. Never null.
     */
    public AbstractPatternMatcher(/* @NotNull */String name) {
        this.name = name;
    }

    public/* @NotNull */Matcher getMatcher(/* @NotNull */String expression) {
        if (expression == null) {
            throw new NullPointerException();
        }
        if (ANY_EXPRESSION.equals(expression)) {
            return AnyMatcher.INSTANCE;
        }
        return newMatcher(expression);
    }

    public/* @NotNull */String getName() {
        return name;
    }

    /**
     * Returns an instance of the implementation specific matcher.
     * 
     * @param expression
     *            the string to be matched.
     * @return the instance of the related matcher. Never null.
     */
    protected abstract/* @NotNull */Matcher newMatcher(/* @NotNull */String expression);

    public String toString() {
        return getName();
    }
}
