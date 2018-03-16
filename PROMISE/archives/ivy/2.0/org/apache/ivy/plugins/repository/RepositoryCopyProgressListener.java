package org.apache.ivy.plugins.repository;

import org.apache.ivy.util.CopyProgressEvent;
import org.apache.ivy.util.CopyProgressListener;

public class RepositoryCopyProgressListener implements CopyProgressListener {
    private final AbstractRepository repository;

    public RepositoryCopyProgressListener(AbstractRepository repository) {
        this.repository = repository;
    }

    private Long totalLength = null;

    public void start(CopyProgressEvent evt) {
        if (totalLength != null) {
            repository.fireTransferStarted(totalLength.longValue());
        } else {
            repository.fireTransferStarted();
        }
    }

    public void progress(CopyProgressEvent evt) {
        repository.fireTransferProgress(evt.getReadBytes());
    }

    public void end(CopyProgressEvent evt) {
        repository.fireTransferProgress(evt.getReadBytes());
        repository.fireTransferCompleted();
    }

    public Long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Long totalLength) {
        this.totalLength = totalLength;
    }
}
