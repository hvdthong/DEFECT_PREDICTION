package org.apache.ivy.core.event.retrieve;

import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.retrieve.RetrieveOptions;

public class EndRetrieveEvent extends RetrieveEvent {
    public static final String NAME = "post-retrieve";

    private long duration;

    private int nbCopied;

    private int nbUpToDate;

    private long totalCopiedSize;

    public EndRetrieveEvent(ModuleRevisionId mrid, String[] confs, long elapsedTime,
            int targetsCopied, int targetsUpToDate, long totalCopiedSize, RetrieveOptions options) {
        super(NAME, mrid, confs, options);

        this.duration = elapsedTime;
        this.nbCopied = targetsCopied;
        this.nbUpToDate = targetsUpToDate;
        this.totalCopiedSize = totalCopiedSize;
        addAttribute("duration", String.valueOf(elapsedTime));
        addAttribute("size", String.valueOf(totalCopiedSize));
        addAttribute("nbCopied", String.valueOf(targetsCopied));
        addAttribute("nbUptodate", String.valueOf(targetsUpToDate));
    }

    /**
     * Duration of the retrieve operation, in ms.
     * 
     * @return Duration of the retrieve operation, in ms.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Number of artifacts which were copied (or symlinked) during the retrieve
     * 
     * @return Number of artifacts which were copied during the retrieve.
     */
    public int getNbCopied() {
        return nbCopied;
    }

    /**
     * Number of artifacts which were not copied since they were already present and up to date.
     * 
     * @return Number of artifacts which were not copied since they were already present and up to
     *         date.
     */
    public int getNbUpToDate() {
        return nbUpToDate;
    }
    
    /**
     * Total size of all copied (or symlinked) artifacts, in bytes.
     * @return Total size of all copied (or symlinked) artifacts, in bytes.
     */
    public long getTotalCopiedSize() {
        return totalCopiedSize;
    }
}
