package org.apache.ivy.plugins.matcher;

/**
 * Interface for a pattern matcher. <p/> The pattern matcher is the main abstraction regarding the
 * matching of an expression. Implementation may vary depending on the expression syntax handling
 * that is desired.
 */
public interface PatternMatcher {

    /**
     * 'exact' pattern matcher name
     */
    public static final String EXACT = "exact";

    /**
     * pattern matcher name 'regexp'
     */
    public static final String REGEXP = "regexp";

    /**
     * pattern matcher 'glob'
     */
    public static final String GLOB = "glob";

    /**
     * pattern matcher name 'exactOrRegexp'
     */
    public static final String EXACT_OR_REGEXP = "exactOrRegexp";

    /**
     * Any expression string: '*'
     */
    public static final String ANY_EXPRESSION = "*";

    /**
     * Return the matcher for the given expression.
     * 
     * @param expression
     *            the expression to be matched. Cannot be null ?
     * @return the matcher instance for the given expression. Never null.
     */
    public/* @NotNull */Matcher getMatcher(/* @NotNull */String expression);

    /**
     * return the name of this pattern matcher
     * 
     * @return the name of this pattern matcher. Never null.
     * @see #EXACT
     * @see #REGEXP
     * @see #GLOB
     * @see #EXACT_OR_REGEXP
     */
    public/* @NotNull */String getName();
}
