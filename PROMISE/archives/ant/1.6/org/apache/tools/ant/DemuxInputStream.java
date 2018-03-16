package org.apache.tools.ant;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Passes input requests to the project object for demuxing into
 * individual tasks and threads.
 *
 * @since Ant 1.6
 */
public class DemuxInputStream extends InputStream {

    /**
     * The project to from which to get input.
     */
    private Project project;

    /**
     * Create a DemuxInputStream for the given project
     *
     * @param project the project instance
     */
    public DemuxInputStream(Project project) {
        this.project = project;
    }

    public int read() throws IOException {
        byte[] buffer = new byte[1];
        if (project.demuxInput(buffer, 0, 1) == -1) {
            return -1;
        }
        return buffer[0];
    }


    public int read(byte[] buffer, int offset, int length) throws IOException {
        return project.demuxInput(buffer, offset, length);
    }

}
