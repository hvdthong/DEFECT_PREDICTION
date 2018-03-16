package org.apache.ivy.plugins.matcher;

import org.apache.ivy.core.module.id.ArtifactId;
import org.apache.ivy.core.module.id.ModuleId;
import org.apache.ivy.core.module.id.ModuleRevisionId;

/**
 * Set of helper methods to match ModuleId, ModuleRevisionId, ArtifactId
 */
public final class MatcherHelper {
    
    private MatcherHelper() {
    }

    public static boolean matches(PatternMatcher m, String expression, String input) {
        return m.getMatcher(expression).matches(input);
    }

    public static boolean matches(PatternMatcher m, ModuleId exp, ModuleId mid) {
        return matches(m, exp.getOrganisation(), mid.getOrganisation())
                && matches(m, exp.getName(), mid.getName());
    }

    public static boolean matches(PatternMatcher m, ModuleRevisionId exp, ModuleRevisionId mrid) {
        return matches(m, exp.getOrganisation(), mrid.getOrganisation())
                && matches(m, exp.getName(), mrid.getName())
                && matches(m, exp.getRevision(), mrid.getRevision());
    }

    public static boolean matches(PatternMatcher m, ArtifactId exp, ArtifactId aid) {
        return matches(m, exp.getModuleId(), aid.getModuleId())
                && matches(m, exp.getName(), aid.getName())
                && matches(m, exp.getExt(), aid.getExt())
                && matches(m, exp.getType(), aid.getType());
    }

    public static boolean isExact(PatternMatcher m, ModuleRevisionId exp) {
        return isExact(m, exp.getOrganisation()) && isExact(m, exp.getName())
                && isExact(m, exp.getRevision());
    }

    public static boolean isExact(PatternMatcher m, ModuleId exp) {
        return isExact(m, exp.getOrganisation()) && isExact(m, exp.getName());
    }

    public static boolean isExact(PatternMatcher m, String exp) {
        return m.getMatcher(exp).isExact();
    }
}
