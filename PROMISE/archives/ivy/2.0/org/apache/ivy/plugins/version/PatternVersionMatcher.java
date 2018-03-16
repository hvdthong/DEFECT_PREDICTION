package org.apache.ivy.plugins.version;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.plugins.matcher.Matcher;

/**
 * 
 */
public class PatternVersionMatcher extends AbstractVersionMatcher {

    private List matches = new ArrayList();


    private boolean init = false;

    public void addMatch(Match match) {
        matches.add(match);
    }

    private void init() {
        if (!init) {
            for (Iterator it = matches.iterator(); it.hasNext();) {
                Match match = (Match) it.next();
                List revMatches = (List) revisionMatches.get(match.getRevision());
                if (revMatches == null) {
                    revMatches = new ArrayList();
                    revisionMatches.put(match.getRevision(), revMatches);
                }
                revMatches.add(match);
            }
            init = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) {
        init();
        boolean accept = false;

        String revision = askedMrid.getRevision();
        int bracketIndex = revision.indexOf('(');
        if (bracketIndex > 0) {
            revision = revision.substring(0, bracketIndex);
        }

        List revMatches = (List) revisionMatches.get(revision);

        if (revMatches != null) {
            Iterator it = revMatches.iterator();
            while (!accept && it.hasNext()) {
                Match match = (Match) it.next();
                Matcher matcher = match.getPatternMatcher(askedMrid);
                accept = matcher.matches(foundMrid.getRevision());
            }
        }

        return accept;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDynamic(ModuleRevisionId askedMrid) {
        init();
        String revision = askedMrid.getRevision();
        int bracketIndex = revision.indexOf('(');
        if (bracketIndex > 0) {
            revision = revision.substring(0, bracketIndex);
        }
        return revisionMatches.containsKey(revision);
    }

}
