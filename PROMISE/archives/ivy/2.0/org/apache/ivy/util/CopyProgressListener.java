package org.apache.ivy.util;

/**
 * Listen to copy progression
 */
public interface CopyProgressListener {
    void start(CopyProgressEvent evt);

    void progress(CopyProgressEvent evt);

    void end(CopyProgressEvent evt);
}
