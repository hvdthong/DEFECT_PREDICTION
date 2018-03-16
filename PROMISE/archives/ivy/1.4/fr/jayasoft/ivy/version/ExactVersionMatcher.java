package fr.jayasoft.ivy.version;

import fr.jayasoft.ivy.ModuleRevisionId;

public class ExactVersionMatcher extends AbstractVersionMatcher {

	public ExactVersionMatcher() {
		super("exact");
	}
	
    public boolean isDynamic(ModuleRevisionId askedMrid) {
        return false;
    }

    public boolean accept(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) {
        return askedMrid.getRevision().equals(foundMrid.getRevision());
    }
}
