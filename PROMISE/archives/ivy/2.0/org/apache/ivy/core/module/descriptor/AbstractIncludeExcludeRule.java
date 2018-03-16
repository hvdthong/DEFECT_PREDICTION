package org.apache.ivy.core.module.descriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.ivy.core.IvyPatternHelper;
import org.apache.ivy.core.module.id.ArtifactId;
import org.apache.ivy.plugins.matcher.PatternMatcher;
import org.apache.ivy.util.extendable.UnmodifiableExtendableItem;

/**
 * Abstract class used as implementation for both {@link IncludeRule} and {@link ExcludeRule},
 * since their contract is almost identical
 */
public abstract class AbstractIncludeExcludeRule extends UnmodifiableExtendableItem implements
        ConfigurationAware {

    private ArtifactId id;

    private Collection confs = new ArrayList();

    private PatternMatcher patternMatcher;

    public AbstractIncludeExcludeRule(ArtifactId aid, PatternMatcher matcher, Map extraAttributes) {
        super(null, extraAttributes);
        id = aid;
        patternMatcher = matcher;
        initStandardAttributes();
    }

    private void initStandardAttributes() {
        setStandardAttribute(IvyPatternHelper.ORGANISATION_KEY, id.getModuleId().getOrganisation());
        setStandardAttribute(IvyPatternHelper.MODULE_KEY, id.getModuleId().getName());
        setStandardAttribute(IvyPatternHelper.ARTIFACT_KEY, id.getName());
        setStandardAttribute(IvyPatternHelper.TYPE_KEY, id.getType());
        setStandardAttribute(IvyPatternHelper.EXT_KEY, id.getExt());
        setStandardAttribute("matcher", patternMatcher.getName());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractIncludeExcludeRule)) {
            return false;
        }
        AbstractIncludeExcludeRule rule = (AbstractIncludeExcludeRule) obj;
        return getId().equals(rule.getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Add a configuration for this rule
     * 
     * @param conf
     */
    public void addConfiguration(String conf) {
        confs.add(conf);
    }

    public ArtifactId getId() {
        return id;
    }

    public String[] getConfigurations() {
        return (String[]) confs.toArray(new String[confs.size()]);
    }

    public PatternMatcher getMatcher() {
        return patternMatcher;
    }

    public String toString() {
        return id + "(" + confs + ")";
    }
}
