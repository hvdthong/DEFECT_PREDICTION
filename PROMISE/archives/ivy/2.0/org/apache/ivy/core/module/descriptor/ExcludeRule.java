package org.apache.ivy.core.module.descriptor;

import org.apache.ivy.core.module.id.ArtifactId;
import org.apache.ivy.plugins.matcher.PatternMatcher;
import org.apache.ivy.util.extendable.ExtendableItem;

/**
 * This describes a rule of exclusion. It is used to restrict the artifacts asked for a dependency,
 * by excluding a whole module or some artifacts of a dependency.
 */
public interface ExcludeRule extends ExtendableItem {

    /**
     * Returns the id of the described artifact, without revision information
     * 
     * @return
     */
    public ArtifactId getId();

    /**
     * Returns the configurations of the module in which the artifact is asked
     * 
     * @return an array of configuration names in which the artifact is asked
     */
    public String[] getConfigurations();

    /**
     * Returns the matcher to use to know if an artifact match the current descriptor
     * 
     * @return
     */
    public PatternMatcher getMatcher();
}
