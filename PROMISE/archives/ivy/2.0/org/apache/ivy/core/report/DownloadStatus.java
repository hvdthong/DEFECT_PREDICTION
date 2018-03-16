package org.apache.ivy.core.report;

/**
 *
 */
public final class DownloadStatus {
    private String name;

    private DownloadStatus(String name) {
        this.name = name;
    }

    /**
     * means that download was not required
     */
    public static final DownloadStatus NO = new DownloadStatus("no");

    public static final DownloadStatus SUCCESSFUL = new DownloadStatus("successful");

    public static final DownloadStatus FAILED = new DownloadStatus("failed");
    
    /**
     * Returns the {@link DownloadStatus} corresponding to the given String representation.
     * 
     * @return the {@link DownloadStatus} corresponding to the given String representation.
     * @throws IllegalArgumentException
     *             if the given String does not correspond to any {@link DownloadStatus}.
     */
    public static final DownloadStatus fromString(String status) {
        if (NO.name.equals(status)) {
            return NO;
        }
        if (SUCCESSFUL.name.equals(status)) {
            return SUCCESSFUL;
        }
        if (FAILED.name.equals(status)) {
            return FAILED;
        }
        throw new IllegalArgumentException("unknown download status '" + status + "'");
    }

    public String toString() {
        return name;
    }
}
