package org.apache.ivy.core.module.descriptor;

import java.net.URL;
import java.util.Date;

import org.apache.ivy.core.module.id.ArtifactRevisionId;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.util.extendable.ExtendableItem;

/**
 * Representation of a published 'file' in the development environment. An artifact is generally a
 * file that is produced by a project build. This is typically a <code>jar</code>, 
 * a <code>war</code>, an <code>ear</code>, a <code>zip</code>, a <code>deb</code>, etc.
 */
public interface Artifact extends ExtendableItem {

    /**
     * Returns the resolved module revision id for this artifact
     * 
     * @return the resolved module revision id.
     */
    ModuleRevisionId getModuleRevisionId();

    /**
     * Returns the resolved publication date for this artifact
     * 
     * @return the resolved publication date. Never null.
     */
    Date getPublicationDate();

    /**
     * Return the name of the artifact, generally 'part' of the basename of the file.
     * 
     * @return the name of the artifact. Never null.
     */
    String getName();

    /**
     * Returns the type of the artifact, typically 'jar', 'source', 'javadoc', 'debian', ...
     * 
     * @return the type of the artifact. Never null.
     */
    String getType();

    /**
     * Retrieve the extension of the artifact. The extension is without dot (ie. 'jar' and not
     * '.jar')
     * 
     * @return the extension of the artifact. Never null.
     */
    String getExt();

    /**
     * Returns the url at which this artifact can be found independently of ivy configuration. This
     * can be null (and is usually for standard artifacts)
     * 
     * @return url at which this artifact can be found independently of ivy configuration
     */
    URL getUrl();

    /**
     * Returns the list of configurations where this artifact is associated to.
     * 
     * @return the list of configuration this artifact is associated to. Never null.
     */
    String[] getConfigurations();

    /**
     * Return the specific identifier of this artifact.
     * 
     * @return the id of the artifact
     */
    ArtifactRevisionId getId();
    
    /**
     * Returns true if this artifact represents a module metadata artifact, false if it's a
     * published artifact
     * 
     * @return true if this artifact represents a module metadata artifact, false if it's a
     *         published artifact
     */
    boolean isMetadata();
}
