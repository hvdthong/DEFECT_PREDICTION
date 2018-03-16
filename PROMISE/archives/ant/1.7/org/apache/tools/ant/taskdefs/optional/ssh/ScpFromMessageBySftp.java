package org.apache.tools.ant.taskdefs.optional.ssh;

import java.io.File;
import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpProgressMonitor;

/**
 * A helper object representing an scp download.
 */
public class ScpFromMessageBySftp extends ScpFromMessage {

    private static final int HUNDRED_KILOBYTES = 102400;

    private String remoteFile;
    private File localFile;
    private boolean isRecursive = false;
    private boolean verbose = false;

    /**
     * Constructor for ScpFromMessageBySftp.
     * @param verbose if true log extra information
     * @param session the Scp session to use
     * @param aRemoteFile the remote file name
     * @param aLocalFile  the local file
     * @param recursive   if true use recursion
     * @since Ant 1.7
     */
    public ScpFromMessageBySftp(boolean verbose,
                                Session session,
                                String aRemoteFile,
                                File aLocalFile,
                                boolean recursive) {
        super(verbose, session);
        this.verbose = verbose;
        this.remoteFile = aRemoteFile;
        this.localFile = aLocalFile;
        this.isRecursive = recursive;
    }

    /**
     * Constructor for ScpFromMessageBySftp.
     * @param session the Scp session to use
     * @param aRemoteFile the remote file name
     * @param aLocalFile  the local file
     * @param recursive   if true use recursion
     */
    public ScpFromMessageBySftp(Session session,
                                String aRemoteFile,
                                File aLocalFile,
                                boolean recursive) {
        this(false, session, aRemoteFile, aLocalFile, recursive);
    }

    /**
     * Carry out the transfer.
     * @throws IOException on i/o errors
     * @throws JSchException on errors detected by scp
     */
    public void execute() throws IOException, JSchException {
        ChannelSftp channel = openSftpChannel();
        try {
            channel.connect();
            try {
                SftpATTRS attrs = channel.stat(remoteFile);
                if (attrs.isDir() && !remoteFile.endsWith("/")) {
                    remoteFile = remoteFile + "/";
                }
            } catch (SftpException ee) {
            }
            getDir(channel, remoteFile, localFile);
        } catch (SftpException e) {
            throw new JSchException(e.toString());
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
        log("done\n");
    }

    private void getDir(ChannelSftp channel,
                        String remoteFile,
                        File localFile) throws IOException, SftpException {
        String pwd = remoteFile;
        if (remoteFile.lastIndexOf('/') != -1) {
            if (remoteFile.length() > 1) {
                pwd = remoteFile.substring(0, remoteFile.lastIndexOf('/'));
            }
        }
        channel.cd(pwd);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        java.util.Vector files = channel.ls(remoteFile);
        for (int i = 0; i < files.size(); i++) {
            ChannelSftp.LsEntry le = (ChannelSftp.LsEntry) files.elementAt(i);
            String name = le.getFilename();
            if (le.getAttrs().isDir()) {
                if (name.equals(".") || name.equals("..")) {
                    continue;
                }
                getDir(channel,
                       channel.pwd() + "/" + name + "/",
                       new File(localFile, le.getFilename()));
            } else {
                getFile(channel, le, localFile);
            }
        }
        channel.cd("..");
    }

    private void getFile(ChannelSftp channel,
                         ChannelSftp.LsEntry le,
                         File localFile) throws IOException, SftpException {
        String remoteFile = le.getFilename();
        if (!localFile.exists()) {
            String path = localFile.getAbsolutePath();
            int i = 0;
            if ((i = path.lastIndexOf(File.pathSeparator)) != -1) {
                if (path.length() > File.pathSeparator.length()) {
                    new File(path.substring(0, i)).mkdirs();
                }
            }
        }

        if (localFile.isDirectory()) {
            localFile = new File(localFile, remoteFile);
        }

        long startTime = System.currentTimeMillis();
        long totalLength = le.getAttrs().getSize();

        SftpProgressMonitor monitor = null;
        boolean trackProgress = getVerbose() && totalLength > HUNDRED_KILOBYTES;
        if (trackProgress) {
            monitor = getProgressMonitor();
        }
        try {
            log("Receiving: " + remoteFile + " : " + le.getAttrs().getSize());
            channel.get(remoteFile, localFile.getAbsolutePath(), monitor);
        } finally {
            long endTime = System.currentTimeMillis();
            logStats(startTime, endTime, (int) totalLength);
        }
    }
}
