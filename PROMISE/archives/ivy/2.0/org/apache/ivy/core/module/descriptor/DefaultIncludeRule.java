package org.apache.ivy.core.module.descriptor;

import java.util.Map;

import org.apache.ivy.core.module.id.ArtifactId;
import org.apache.ivy.plugins.matcher.PatternMatcher;

public class DefaultIncludeRule extends AbstractIncludeExcludeRule implements IncludeRule {

    public DefaultIncludeRule(ArtifactId aid, PatternMatcher matcher, Map extraAttributes) {
        super(aid, matcher, extraAttributes);
    }

    public String toString() {
        return "I:" + super.toString();
    }
}
