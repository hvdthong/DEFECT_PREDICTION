package org.apache.ivy.core.event.publish;

import java.io.File;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.plugins.resolver.DependencyResolver;

/**
 * Event fired just before an artifact is published into a resolver. Triggers registered on
 * {@link #NAME} will be notified of these events.
 * 
 * @see DependencyResolver#publish(Artifact, File, boolean)
 */
public class StartArtifactPublishEvent extends PublishEvent {

    private static final long serialVersionUID = -1134274781039590219L;

    public static final String NAME = "pre-publish-artifact";

    public StartArtifactPublishEvent(DependencyResolver resolver, Artifact artifact, File data,
            boolean overwrite) {
        super(NAME, resolver, artifact, data, overwrite);
    }

}
