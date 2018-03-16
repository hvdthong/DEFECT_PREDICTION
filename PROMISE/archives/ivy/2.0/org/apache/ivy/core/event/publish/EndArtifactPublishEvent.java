package org.apache.ivy.core.event.publish;

import java.io.File;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.plugins.resolver.DependencyResolver;

/**
 * Event fired after artifact publication has finished (possibly in error). Triggers registered on
 * {@link #NAME} will be notified of these events.
 * 
 * @see DependencyResolver#publish(Artifact, File, boolean)
 */
public class EndArtifactPublishEvent extends PublishEvent {

    private static final long serialVersionUID = -65690169431499422L;

    public static final String NAME = "post-publish-artifact";

    public static final String STATUS_SUCCESSFUL = "successful";

    public static final String STATUS_FAILED = "failed";

    private final boolean successful;

    public EndArtifactPublishEvent(DependencyResolver resolver, Artifact artifact, File data,
            boolean overwrite, boolean successful) {
        super(NAME, resolver, artifact, data, overwrite);
        this.successful = successful;
        addAttribute("status", isSuccessful() ? STATUS_SUCCESSFUL : STATUS_FAILED);
    }

    /**
     * @return true iff no errors were encountered during the publication
     */
    public boolean isSuccessful() {
        return successful;
    }
}
