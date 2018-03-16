package org.apache.ivy.core.sort;

public class SortOptions {
    public static final SortOptions DEFAULT = new SortOptions();
    public static final SortOptions SILENT = new SortOptions()
            .setNonMatchingVersionReporter(new SilentNonMatchingVersionReporter())
            .setUseCircularDependencyStrategy(false);
    
    /**
     * Used to report some non matching version (when a modules depends on a specific revision of an
     * other modules present in the of modules to sort with a different revision.
     */
    private NonMatchingVersionReporter nonMatchingVersionReporter 
                                            = new WarningNonMatchingVersionReporter();

    /**
     * Should the default circular dependency strategy be used when a circular dependency is found,
     * or should circular dependencies be ignored?
     */
    private boolean useCircularDependencyStrategy = true;
    
    
    public NonMatchingVersionReporter getNonMatchingVersionReporter() {
        return nonMatchingVersionReporter;
    }
    public SortOptions setNonMatchingVersionReporter(
                                NonMatchingVersionReporter nonMatchingVersionReporter) {
        this.nonMatchingVersionReporter = nonMatchingVersionReporter;
        return this;
    }
    public boolean isUseCircularDependencyStrategy() {
        return useCircularDependencyStrategy;
    }
    public SortOptions setUseCircularDependencyStrategy(boolean useCircularDependencyStrategy) {
        this.useCircularDependencyStrategy = useCircularDependencyStrategy;
        return this;
    }
}
