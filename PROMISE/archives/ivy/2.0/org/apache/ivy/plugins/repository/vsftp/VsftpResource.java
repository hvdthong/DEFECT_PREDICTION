package org.apache.ivy.plugins.repository.vsftp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ivy.plugins.repository.LazyResource;
import org.apache.ivy.plugins.repository.Resource;
import org.apache.ivy.util.Message;

public class VsftpResource extends LazyResource {
    private VsftpRepository repository;

    public VsftpResource(VsftpRepository repository, String file) {
        super(file);
        this.repository = repository;
    }

    protected void init() {
        try {
            init(repository.getInitResource(getName()));
        } catch (IOException e) {
            Message.verbose(e.toString());
        }
    }

    public InputStream openStream() throws IOException {
        throw new UnsupportedOperationException(
                "vsftp resource does not support openStream operation");
    }

    public Resource clone(String cloneName) {
        try {
            return repository.getResource(cloneName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
