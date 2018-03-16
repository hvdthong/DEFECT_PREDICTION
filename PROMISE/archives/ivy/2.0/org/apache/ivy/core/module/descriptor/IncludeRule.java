package org.apache.ivy.core.module.descriptor;

import org.apache.ivy.core.module.id.ArtifactId;
import org.apache.ivy.plugins.matcher.PatternMatcher;
import org.apache.ivy.util.extendable.ExtendableItem;

/**
 * This describes a rule of inclusion. It is used to resctrict the artifacts and modules asked for a
 * dependency, by including only modules and artifacts matching the rule
 */
public interface IncludeRule extends ExtendableItem {

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
