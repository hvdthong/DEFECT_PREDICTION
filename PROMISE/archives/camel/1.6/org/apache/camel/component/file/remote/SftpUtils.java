package org.apache.camel.component.file.remote;

import java.io.IOException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility methods for SFTP.
 */
public final class SftpUtils {
    private static final transient Log LOG = LogFactory.getLog(SftpUtils.class);

    private SftpUtils() {
    }

    public static boolean buildDirectory(ChannelSftp sftpClient, String dirName)
        throws IOException, SftpException {
        String originalDirectory = sftpClient.pwd();

        boolean success = false;
        try {
            try {
                sftpClient.cd(dirName);
                success = true;
            } catch (SftpException e) {
            }

            if (!success) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Trying to build remote directory: " + dirName);
                }

                try {
                    sftpClient.mkdir(dirName);
                    success = true;
                } catch (SftpException e) {
                    success = buildDirectoryChunks(sftpClient, dirName);
                }
            }
        } finally {
            sftpClient.cd(originalDirectory);
        }

        return success;
    }

    public static boolean buildDirectoryChunks(ChannelSftp sftpClient, String dirName)
        throws IOException, SftpException {
        final StringBuilder sb = new StringBuilder(dirName.length());
        final String[] dirs = dirName.split("\\/");

        boolean success = false;
        for (String dir : dirs) {
            sb.append(dir).append('/');
            String directory = sb.toString();
            if (LOG.isTraceEnabled()) {
                LOG.trace("Trying to build remote directory: " + directory);
            }

            try {
                sftpClient.mkdir(directory);
                success = true;
            } catch (SftpException e) {
            }
        }

        return success;
    }
}
