package org.apache.tools.ant.taskdefs.optional.ssh;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.File;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

/**
 * Ant task for sending files to remote machine over ssh/scp.
 *
 * @author charliehubbard76@yahoo.com
 * @since Ant 1.6
 */
public class Scp extends SSHBase {

    private String fromUri;
    private String toUri;
    private List fileSets = null;

    /**
     * Sets the file to be transferred.  This can either be a remote
     * file or a local file.  Remote files take the form:<br>
     * <i>user:password@host:/directory/path/file.example</i><br>
     * Files to transfer can also include a wildcard to include all
     * files in a remote directory.  For example:<br>
     * <i>user:password@host:/directory/path/*</i><br>
     * @param aFromUri a string representing the file to transfer.
     */
    public void setFile(String aFromUri) {
        this.fromUri = aFromUri;
    }

    /**
     * Sets the location where files will be transferred to.
     * This can either be a remote directory or a local directory.
     * Remote directories take the form of:<br>
     * <i>user:password@host:/directory/path/</i><br>
     * This parameter is required.

     * @param aToUri a string representing the target of the copy.
     */
    public void setTodir(String aToUri) {
        this.toUri = aToUri;
    }



    /**
     * Adds a FileSet tranfer to remote host.  NOTE: Either
     * addFileSet() or setFile() are required.  But, not both.
     *
     * @param set FileSet to send to remote host.
     */
    public void addFileset(FileSet set) {
        if (fileSets == null) {
            fileSets = new LinkedList();
        }
        fileSets.add(set);
    }

    public void init() throws BuildException {
        super.init();
        this.toUri = null;
        this.fromUri = null;
        this.fileSets = null;
    }

    public void execute() throws BuildException {
        if (toUri == null) {
            throw new BuildException("The 'todir' attribute is required.");
        }

        if (fromUri == null && fileSets == null) {
            throw new BuildException("Either the 'file' attribute or one "
                + "FileSet is required.");
        }

        boolean isFromRemote = false;
        if (fromUri != null) {
            isFromRemote = isRemoteUri(fromUri);
        }
        boolean isToRemote = isRemoteUri(toUri);
        try {
            if (isFromRemote && !isToRemote) {
                download(fromUri, toUri);
            } else if (!isFromRemote && isToRemote) {
                if (fileSets != null) {
                    upload(fileSets, toUri);
                } else {
                    upload(fromUri, toUri);
                }
            } else if (isFromRemote && isToRemote) {
            } else {
                throw new BuildException("'todir' and 'file' attributes "
                    + "must have syntax like the following: "
                    + "user:password@host:/path");
            }
        } catch (Exception e) {
            if (getFailonerror()) {
                throw new BuildException(e);
            } else {
                log("Caught exception: " + e.getMessage(), Project.MSG_ERR);
            }
        }
    }

    private void download(String fromSshUri, String toPath)
        throws JSchException, IOException {
        String file = parseUri(fromSshUri);

        Session session = null;
        try {
            session = openSession();
            ScpFromMessage message = 
                new ScpFromMessage(session, file, 
                                   getProject().resolveFile(toPath),
                                   fromSshUri.endsWith("*"));
            log("Receiving file: " + file);
            message.setLogListener(this);
            message.execute();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private void upload(List fileSet, String toSshUri)
        throws IOException, JSchException {
        String file = parseUri(toSshUri);

        Session session = null;
        try {
            List list = new ArrayList(fileSet.size());
            for (Iterator i = fileSet.iterator(); i.hasNext();) {
                FileSet set = (FileSet) i.next();
                Directory d = createDirectory(set);
                if (d != null) {
                    list.add(d);
                }
            }
            if (!list.isEmpty()) {
                session = openSession();
                ScpToMessage message = new ScpToMessage(session, list, file);
                message.setLogListener(this);
                message.execute();
            }
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private void upload(String fromPath, String toSshUri)
        throws IOException, JSchException {
        String file = parseUri(toSshUri);

        Session session = null;
        try {
            session = openSession();
            ScpToMessage message = 
                new ScpToMessage(session, getProject().resolveFile(fromPath),
                                 file);
            message.setLogListener(this);
            message.execute();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private String parseUri(String uri) {
        int indexOfAt = uri.indexOf('@');
        int indexOfColon = uri.indexOf(':');
        if (indexOfColon > -1 && indexOfColon < indexOfAt) {
            setUsername(uri.substring(0, indexOfColon));
            setPassword(uri.substring(indexOfColon + 1, indexOfAt));
        } else {
            setUsername(uri.substring(0, indexOfAt));
        }

        if (getUserInfo().getPassword() == null
            && getUserInfo().getPassphrase() == null) {
            throw new BuildException("neither password nor passphrase for user "
                                     + getUserInfo().getName() + " has been "
                                     + "given.  Can't authenticate.");
        }

        int indexOfPath = uri.indexOf(':', indexOfAt + 1);
        if (indexOfPath == -1) {
            throw new BuildException("no remote path in " + uri);
        }

        setHost(uri.substring(indexOfAt + 1, indexOfPath));
        String remotePath = uri.substring(indexOfPath + 1);
        if (remotePath.equals("")) {
            remotePath = ".";
        }
        return remotePath;
    }

    private boolean isRemoteUri(String uri) {
        boolean isRemote = true;
        int indexOfAt = uri.indexOf('@');
        if (indexOfAt < 0) {
            isRemote = false;
        }
        return isRemote;
    }

    private Directory createDirectory(FileSet set) {
        DirectoryScanner scanner = set.getDirectoryScanner(getProject());
        Directory root = new Directory(scanner.getBasedir());
        String[] files = scanner.getIncludedFiles();
        if (files.length != 0) {
            for (int j = 0; j < files.length; j++) {
                String[] path = Directory.getPath(files[j]);
                Directory current = root;
                File currentParent = scanner.getBasedir();
                for (int i = 0; i < path.length; i++) {
                    File file = new File(currentParent, path[i]);
                    if (file.isDirectory()) {
                        current.addDirectory(new Directory(file));
                        current = current.getChild(file);
                        currentParent = current.getDirectory();
                    } else if (file.isFile()) {
                        current.addFile(file);
                    }
                }
            }
        } else {
            root = null;
        }
        return root;
    }
}