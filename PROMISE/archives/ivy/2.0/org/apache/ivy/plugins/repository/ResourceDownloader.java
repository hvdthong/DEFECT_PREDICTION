package org.apache.ivy.plugins.repository;

import java.io.File;
import java.io.IOException;

import org.apache.ivy.core.module.descriptor.Artifact;

/**
 * A {@link ResourceDownloader} is able to download a Resource to a File.
 * <p>
 * Depending on the implementation, the downloader may also choose to download checksums
 * automatically and check the consistency of the downloaded resource.
 * </p>
 * <p>
 * The implementation is also responsible for using a .part file during download, to ensure the
 * destination file will exist only if the download is completed successfully.
 * </p>
 */
public interface ResourceDownloader {
    public void download(Artifact artifact, Resource resource, File dest) throws IOException;
}
