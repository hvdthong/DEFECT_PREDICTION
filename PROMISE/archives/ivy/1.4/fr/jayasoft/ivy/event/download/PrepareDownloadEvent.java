package fr.jayasoft.ivy.event.download;

import fr.jayasoft.ivy.Artifact;
import fr.jayasoft.ivy.Ivy;
import fr.jayasoft.ivy.event.IvyEvent;

public class PrepareDownloadEvent extends IvyEvent {
	public static final String NAME = "prepare-download";
    private Artifact[] _artifacts;
    
    public PrepareDownloadEvent(Ivy source, Artifact[] artifacts) {
    	super(source, NAME);
        _artifacts = artifacts;
    }
    
    public Artifact[] getArtifacts() {
        return _artifacts;
    }
}
