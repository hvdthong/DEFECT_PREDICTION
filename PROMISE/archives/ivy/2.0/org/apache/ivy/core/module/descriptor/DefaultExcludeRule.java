package org.apache.ivy.core.module.descriptor;

import java.util.Map;

import org.apache.ivy.core.module.id.ArtifactId;
import org.apache.ivy.plugins.matcher.PatternMatcher;

public class DefaultExcludeRule extends AbstractIncludeExcludeRule implements ExcludeRule {

    public DefaultExcludeRule(ArtifactId aid, PatternMatcher matcher, Map extraAttributes) {
        super(aid, matcher, extraAttributes);
    }

    public String toString() {
        return "E:" + super.toString();
    }
}
