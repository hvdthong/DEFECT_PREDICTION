package org.apache.ivy.plugins.repository.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ivy.plugins.repository.Resource;

public class FileResource implements Resource {
    private File file;

    private FileRepository repository;

    public FileResource(FileRepository repository, File f) {
        this.repository = repository;
        this.file = f;
    }

    public String getName() {
        return file.getPath();
    }

    public Resource clone(String cloneName) {
        return new FileResource(repository, repository.getFile(cloneName));
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

    public FileRepository getRepository() {
        return repository;
    }

    public boolean isLocal() {
        return repository.isLocal();
    }

    public InputStream openStream() throws IOException {
        return new FileInputStream(file);
    }
}
