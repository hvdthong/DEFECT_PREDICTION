package org.apache.ivy.plugins.resolver.packager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ivy.core.IvyPatternHelper;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.plugins.repository.Resource;

/**
 * Represents an artifact built by a {@link PackagerResolver}.
 */
public class BuiltFileResource implements Resource {

    /**
     * Where the build file should put built artifacts (relative
     * to the build directory). Value is: {@value}
     */
    public static final String BUILT_ARTIFACT_PATTERN = "artifacts/[type]s/[artifact].[ext]";

    private final File file;

    public BuiltFileResource(File file) {
        this.file = file;
    }

    public BuiltFileResource(File dir, Artifact artifact) {
        this(new File(dir, IvyPatternHelper.substitute(BUILT_ARTIFACT_PATTERN, artifact)));
    }

    public String getName() {
        return file.toURI().toString();
    }

    public Resource clone(String name) {
        return new BuiltFileResource(new File(name));
    }

    public long getLastModified() {
        return file.lastModified();
    }

    public long getContentLength() {
        return file.length();
    }

    public boolean exists() {
        return file.exists();
    }

    public String toString() {
        return getName();
    }

    public File getFile() {
        return file;
    }

    public boolean isLocal() {
        return false;
    }

    public InputStream openStream() throws IOException {
        return new FileInputStream(file);
    }
}
