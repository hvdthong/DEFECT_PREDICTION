package fr.jayasoft.ivy.version;

import fr.jayasoft.ivy.ModuleRevisionId;

public class SubVersionMatcher  extends AbstractVersionMatcher {
	public SubVersionMatcher() {
		super("sub-version");
	}

    public boolean isDynamic(ModuleRevisionId askedMrid) {
        return askedMrid.getRevision().endsWith("+");
    }

    public boolean accept(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) {
        String prefix = askedMrid.getRevision().substring(0, askedMrid.getRevision().length() - 1);
        return foundMrid.getRevision().startsWith(prefix);
    }
}
