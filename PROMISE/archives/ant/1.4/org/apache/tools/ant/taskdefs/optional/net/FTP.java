package org.apache.tools.ant.taskdefs.optional.net;

import com.oroinc.net.ftp.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

/**
 * Basic FTP client that performs the following actions:
 * <ul>
 *   <li><strong>send</strong> - send files to a remote server.  This is the
 *              default action.</li>
 *   <li><strong>get</strong> - retrive files from a remote server.</li>
 *   <li><strong>del</strong> - delete files from a remote server.</li>
 *   <li><strong>list</strong> - create a file listing.</li>
 * </ul>
 *
 * <strong>Note:</strong>
 * Some FTP servers - notably the Solaris server - seem to hold data ports
 * open after a "retr" operation, allowing them to timeout instead of
 * shutting them down cleanly.  This happens in active or passive mode,
 * and the ports will remain open even after ending the FTP session.
 * FTP "send" operations seem to close ports immediately.  This behavior
 * may cause problems on some systems when downloading large sets of files.
 *
 * @author Roger Vaughn <a href="mailto:rvaughn@seaconinc.com">rvaughn@seaconinc.com</a>
 * @author Glenn McAllister <a href="mailto:glennm@ca.ibm.com">glennm@ca.ibm.com</a>
 */
public class FTP
    extends Task
{
    protected final static int SEND_FILES   = 0;
    protected final static int GET_FILES    = 1;
    protected final static int DEL_FILES    = 2;
    protected final static int LIST_FILES   = 3;
    protected final static int MK_DIR       = 4;

    private String remotedir;
    private String server;
    private String userid;
    private String password;
    private File listing;
    private boolean binary = true;
    private boolean passive = false;
    private boolean verbose = false;
    private boolean newerOnly = false;
    private int action = SEND_FILES;
    private Vector filesets = new Vector();
    private Vector dirCache = new Vector();
    private int transferred = 0;
    private String remoteFileSep = "/";
    private int port = 21;
    private boolean skipFailedTransfers=false;
    private int skipped=0;
    private boolean ignoreNoncriticalErrors=false;

    protected final static String[] ACTION_STRS = {
        "sending",
        "getting",
        "deleting",
        "listing",
        "making directory"
    };

    protected final static String[] COMPLETED_ACTION_STRS = {
        "sent",
        "retrieved",
        "deleted",
        "listed",
        "created directory"
    };

    protected class FTPDirectoryScanner extends DirectoryScanner {
        protected FTPClient ftp = null;

        public FTPDirectoryScanner(FTPClient ftp) {
            super();
            this.ftp = ftp;
        }

        public void scan() {
            if (includes == null) {
                includes = new String[1];
                includes[0] = "**";
            }
            if (excludes == null) {
                excludes = new String[0];
            }

            filesIncluded = new Vector();
            filesNotIncluded = new Vector();
            filesExcluded = new Vector();
            dirsIncluded = new Vector();
            dirsNotIncluded = new Vector();
            dirsExcluded = new Vector();

            try {
                String cwd = ftp.printWorkingDirectory();
                ftp.changeWorkingDirectory(cwd);
            } catch (IOException e) {
                throw new BuildException("Unable to scan FTP server: ", e);
            }
        }

        protected void scandir(String dir, String vpath, boolean fast) {
            try {
                if (!ftp.changeWorkingDirectory(dir)) {
                    return;
                }

                FTPFile[] newfiles = ftp.listFiles();
                if (newfiles == null) {
                    ftp.changeToParentDirectory();
                    return;
                }

                for (int i = 0; i < newfiles.length; i++) {
                    FTPFile file = newfiles[i];
                    if (!file.getName().equals(".") && !file.getName().equals("..")) {
                        if (file.isDirectory()) {
                            String name = file.getName();
                            if (isIncluded(name)) {
                                if (!isExcluded(name)) {
                                    dirsIncluded.addElement(name);
                                    if (fast) {
                                        scandir(name, vpath + name + File.separator, fast);
                                    }
                                } else {
                                    dirsExcluded.addElement(name);
                                }
                            } else {
                                dirsNotIncluded.addElement(name);
                                if (fast && couldHoldIncluded(name)) {
                                    scandir(name, vpath + name + File.separator, fast);
                                }
                            }
                            if (!fast) {
                                scandir(name, vpath + name + File.separator, fast);
                            }
                        } else {
                            if (file.isFile()) {
                                String name = vpath + file.getName();
                                if (isIncluded(name)) {
                                    if (!isExcluded(name)) {
                                        filesIncluded.addElement(name);
                                    } else {
                                        filesExcluded.addElement(name);
                                    }
                                } else {
                                    filesNotIncluded.addElement(name);
                                }
                            }
                        }
                    }
                }
                ftp.changeToParentDirectory();
            } catch (IOException e) {
                throw new BuildException("Error while communicating with FTP server: ", e);
            }
        }
    }

    /**
     * Sets the remote directory where files will be placed.  This may
     * be a relative or absolute path, and must be in the path syntax
     * expected by the remote server.  No correction of path syntax will
     * be performed.
     */
    public void setRemotedir(String dir)
    {
        this.remotedir = dir;
    }

    /**
     * Sets the FTP server to send files to.
     */
    public void setServer(String server)
    {
        this.server = server;
    }

    /**
     * Sets the FTP port used by the remote server.
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Sets the login user id to use on the specified server.
     */
    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    /**
     * Sets the login password for the given user id.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Specifies whether to use binary-mode or text-mode transfers.  Set
     * to true to send binary mode.  Binary mode is enabled by default.
     */
    public void setBinary(boolean binary)
    {
        this.binary = binary;
    }

    /**
     * Specifies whether to use passive mode.  Set to true if you
     * are behind a firewall and cannot connect without it.  Passive mode
     * is disabled by default.
     */
    public void setPassive(boolean passive)
    {
        this.passive = passive;
    }

    /**
     * Set to true to receive notification about each file as it is
     * transferred.
     */
    public void setVerbose(boolean verbose)
    {
        this.verbose = verbose;
    }

    /**
     * Set to true to transmit only files that are new or changed from their
     * remote counterparts.  The default is to transmit all files.
     */
    public void setNewer(boolean newer)
    {
        this.newerOnly = newer;
    }

    /**
     * A synonym for setNewer.  Set to true to transmit only new or changed
     * files.
     */
    public void setDepends(boolean depends)
    {
        this.newerOnly = depends;
    }

    /**
     * Sets the remote file separator character.  This normally defaults to
     * the Unix standard forward slash, but can be manually overridden using
     * this call if the remote server requires some other separator.  Only
     * the first character of the string is used.
     */
    public void setSeparator(String separator)
    {
        remoteFileSep = separator;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * Sets the FTP action to be taken.  Currently accepts "put", "get",
     * "del", and "list".
     */
    public void setAction(String action) throws BuildException
    {
        if (action.toLowerCase().equals("send") ||
            action.toLowerCase().equals("put"))
        {
            this.action = SEND_FILES;
        }
        else if (action.toLowerCase().equals("recv") ||
                 action.toLowerCase().equals("get"))
        {
            this.action = GET_FILES;
        }
        else if (action.toLowerCase().equals("del") ||
                 action.toLowerCase().equals("delete" ))
        {
            this.action = DEL_FILES;
        }
        else if (action.toLowerCase().equals("list"))
        {
            this.action = LIST_FILES;
        }
        else if (action.toLowerCase().equals("mkdir"))
        {
            this.action = MK_DIR;
        }
        else
        {
            throw new BuildException("action " + action + " is not supported");
        }
    }

    /**
     * The output file for the "list" action.  This attribute is ignored for
     * any other actions.
     */
    public void setListing(File listing) throws BuildException {
        this.listing = listing;
    }


    /**
     * set the failed transfer flag
     */
    public void setSkipFailedTransfers(boolean skipFailedTransfers) {
        this.skipFailedTransfers=skipFailedTransfers;
    }

    /**
     * set the flag to skip errors on dir creation (and maybe later other
     * server specific errors)
     */
     public void setIgnoreNoncriticalErrors(boolean ignoreNoncriticalErrors) {
         this.ignoreNoncriticalErrors=ignoreNoncriticalErrors;
     }

    /**
     * Checks to see that all required parameters are set.
     */
    protected void checkConfiguration() throws BuildException
    {
        if (server == null)
        {
            throw new BuildException("server attribute must be set!");
        }
        if (userid == null)
        {
            throw new BuildException("userid attribute must be set!");
        }
        if (password == null)
        {
            throw new BuildException("password attribute must be set!");
        }

        if ((action == LIST_FILES) && (listing == null))
        {
            throw new BuildException("listing attribute must be set for list action!");
        }

        if( action == MK_DIR && remotedir == null ) {
            throw new BuildException("remotedir attribute must be set for mkdir action!");
        }
    }

    /**
     * For each file in the fileset, do the appropriate action: send, get, delete,
     * or list.
     */
    protected int transferFiles(FTPClient ftp, FileSet fs)
        throws IOException, BuildException
    {
        FileScanner ds;

        if (action == SEND_FILES) {
            ds = fs.getDirectoryScanner(project);
        } else {
            ds = new FTPDirectoryScanner(ftp);
            fs.setupDirectoryScanner(ds, project);
            ds.scan();
        }

        String[] dsfiles = ds.getIncludedFiles();
        String dir = null;
        if ((ds.getBasedir() == null) && ((action == SEND_FILES) || (action == GET_FILES))) {
            throw new BuildException( "the dir attribute must be set for send and get actions" );
        } else {
            if ((action == SEND_FILES) || (action == GET_FILES)) {
                dir = ds.getBasedir().getAbsolutePath();
            }
        }

        BufferedWriter bw = null;
        if (action == LIST_FILES) {
            File pd = new File(listing.getParent());
            if (!pd.exists()) {
                pd.mkdirs();
            }
            bw = new BufferedWriter(new FileWriter(listing));
        }

        for (int i = 0; i < dsfiles.length; i++)
        {
            switch (action) {
            case SEND_FILES: {
                sendFile(ftp, dir, dsfiles[i]);
                break;
            }

            case GET_FILES: {
                getFile(ftp, dir, dsfiles[i]);
                break;
            }

            case DEL_FILES: {
                delFile(ftp, dsfiles[i]);
                break;
            }

            case LIST_FILES: {
                listFile(ftp, bw, dsfiles[i]);
                break;
            }

            default: {
                throw new BuildException("unknown ftp action " + action );
            }
            }
        }

        if (action == LIST_FILES) {
            bw.close();
        }

        return dsfiles.length;
    }

    /**
     * Sends all files specified by the configured filesets to the remote
     * server.
     */
    protected void transferFiles(FTPClient ftp)
        throws IOException, BuildException
    {
        transferred = 0;
        skipped=0;

        if (filesets.size() == 0)
        {
            throw new BuildException("at least one fileset must be specified.");
        }
        else
        {
            for (int i = 0; i < filesets.size(); i++)
            {
                FileSet fs = (FileSet) filesets.elementAt(i);
                if (fs != null)
                {
                    transferFiles(ftp, fs);
                }
            }
        }

        log(transferred + " files " + COMPLETED_ACTION_STRS[action]);
        if(skipped!=0) {
            log(skipped + " files were not successfully "+ COMPLETED_ACTION_STRS[action]);
        }
    }

    /**
     * Correct a file path to correspond to the remote host requirements.
     * This implementation currently assumes that the remote end can
     * handle Unix-style paths with forward-slash separators.  This can
     * be overridden with the <code>separator</code> task parameter.  No
     * attempt is made to determine what syntax is appropriate for the
     * remote host.
     */
    protected String resolveFile(String file)
    {
        return file.replace(System.getProperty("file.separator").charAt(0),
                            remoteFileSep.charAt(0));
    }

    /**
     * Creates all parent directories specified in a complete relative
     * pathname.  Attempts to create existing directories will not cause
     * errors.
     */
    protected void createParents(FTPClient ftp, String filename)
        throws IOException, BuildException
    {
        Vector parents = new Vector();
        File dir = new File(filename);
        String dirname;

        while ((dirname = dir.getParent()) != null)
        {
            dir = new File(dirname);
            parents.addElement(dir);
        }

        for (int i = parents.size() - 1; i >= 0; i--)
        {
            dir = (File)parents.elementAt(i);
            if (!dirCache.contains(dir))
            {
                log("creating remote directory " + resolveFile(dir.getPath()),
                    Project.MSG_VERBOSE);
                ftp.makeDirectory(resolveFile(dir.getPath()));
                int result=ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(result) &&
                     (result != 550) && (result!= 553)   &&
                     !ignoreNoncriticalErrors)
                {
                    throw new BuildException(
                                             "could not create directory: " +
                                             ftp.getReplyString());
                }
                dirCache.addElement(dir);
            }
        }
    }

    /**
     * Checks to see if the remote file is current as compared with the
     * local file.  Returns true if the remote file is up to date.
     */
    protected boolean isUpToDate(FTPClient ftp, File localFile, String remoteFile)
        throws IOException, BuildException
    {
        log("checking date for " + remoteFile, Project.MSG_VERBOSE);

        FTPFile[] files = ftp.listFiles(remoteFile);

        if (files == null || files.length == 0)
        {

            if( action == SEND_FILES ) {
                log("Could not date test remote file: " + remoteFile
                    + "assuming out of date.", Project.MSG_VERBOSE);
                return false;
            } else {
                throw new BuildException("could not date test remote file: " +
                                         ftp.getReplyString());
            }
        }

        long remoteTimestamp = files[0].getTimestamp().getTime().getTime();
        long localTimestamp = localFile.lastModified();
        if (this.action == SEND_FILES) {
            return remoteTimestamp > localTimestamp;
        } else {
            return localTimestamp > remoteTimestamp;
        }
    }

    /**
     * Sends a single file to the remote host.
     * <code>filename</code> may contain a relative path specification.
     * When this is the case, <code>sendFile</code> will attempt to create
     * any necessary parent directories before sending the file.  The file
     * will then be sent using the entire relative path spec - no attempt
     * is made to change directories.  It is anticipated that this may
     * eventually cause problems with some FTP servers, but it simplifies
     * the coding.
     */
    protected void sendFile(FTPClient ftp, String dir, String filename)
        throws IOException, BuildException
    {
        InputStream instream = null;
        try
        {
            File file = project.resolveFile(new File(dir, filename).getPath());

            if (newerOnly && isUpToDate(ftp, file, resolveFile(filename)))
                return;

            if (verbose)
            {
                log("transferring " + file.getAbsolutePath());
            }

            instream = new BufferedInputStream(new FileInputStream(file));

            createParents(ftp, filename);

            ftp.storeFile(resolveFile(filename), instream);
            boolean success=FTPReply.isPositiveCompletion(ftp.getReplyCode());
            if (!success)
            {
                String s="could not put file: " + ftp.getReplyString();
                if(skipFailedTransfers==true) {
                    log(s,Project.MSG_WARN);
                    skipped++;
                }
                else {
                    throw new BuildException(s);
                }

            }
            else {

                log("File " + file.getAbsolutePath() +
                    " copied to " + server,
                    Project.MSG_VERBOSE);
                transferred++;
            }
        }
        finally
        {
            if (instream != null)
            {
                try
                {
                    instream.close();
                }
                catch(IOException ex)
                {
                }
            }
        }
    }

    /**
     * Delete a file from the remote host.
     */
    protected void delFile(FTPClient ftp, String filename)
        throws IOException, BuildException {
        if (verbose) {
            log("deleting " + filename);
        }

        if (!ftp.deleteFile(resolveFile(filename))) {
            String s="could not delete file: " + ftp.getReplyString();
            if(skipFailedTransfers==true) {
                log(s,Project.MSG_WARN);
                skipped++;
            }
            else {
                throw new BuildException(s);
            }
        }
        else {
            log("File " + filename + " deleted from " + server, Project.MSG_VERBOSE);
            transferred++;
        }
    }

    /**
     * Retrieve a single file to the remote host.
     * <code>filename</code> may contain a relative path specification.
     * The file will then be retreived using the entire relative path spec -
     * no attempt is made to change directories.  It is anticipated that this may
     * eventually cause problems with some FTP servers, but it simplifies
     * the coding.
     */
    protected void getFile(FTPClient ftp, String dir, String filename)
        throws IOException, BuildException
    {
        OutputStream outstream = null;
        try
        {
            File file = project.resolveFile(new File(dir, filename).getPath());

            if (newerOnly && isUpToDate(ftp, file, resolveFile(filename)))
                return;

            if (verbose)
            {
                log("transferring " + filename + " to " + file.getAbsolutePath());
            }


            if (!pdir.exists()) {
                pdir.mkdirs();
            }
            outstream = new BufferedOutputStream(new FileOutputStream(file));
            ftp.retrieveFile(resolveFile(filename), outstream);

            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode()))
            {
                String s="could not get file: " + ftp.getReplyString();
                if(skipFailedTransfers==true) {
                    log(s,Project.MSG_WARN);
                    skipped++;
                }
                else {
                    throw new BuildException(s);
                }

            }
            else {
                log("File " + file.getAbsolutePath() + " copied from " + server,
                    Project.MSG_VERBOSE);
                transferred++;
            }
        }
        finally
        {
            if (outstream != null)
            {
                try
                {
                    outstream.close();
                }
                catch(IOException ex)
                {
                }
            }
        }
    }

    /**
     * List information about a single file from the remote host.
     * <code>filename</code> may contain a relative path specification.
     * The file listing will then be retrieved using the entire relative path spec
     * - no attempt is made to change directories.  It is anticipated that this may
     * eventually cause problems with some FTP servers, but it simplifies
     * the coding.
     */
    protected void listFile(FTPClient ftp, BufferedWriter bw, String filename)
        throws IOException, BuildException
    {
        if (verbose) {
            log("listing " + filename);
        }

        FTPFile ftpfile = ftp.listFiles(resolveFile(filename))[0];
        bw.write(ftpfile.toString());
        bw.newLine();

        transferred++;
    }

    /**
     * Create the specified directory on the remote host.
     * @param ftp The FTP client connection
     * @param dir The directory to create (format must be correct for host type)
     */
    protected void makeRemoteDir( FTPClient ftp, String dir )
        throws IOException, BuildException
    {
        if (verbose) {
            log("creating directory: " + dir);
        }

        if( ! ftp.makeDirectory( dir ) ) {

            int rc = ftp.getReplyCode();
            if( !(ignoreNoncriticalErrors && (rc == 550 || rc == 553 || rc==521))) {
                throw new BuildException( "could not create directory: " +
                                          ftp.getReplyString() );
            }

            if( verbose ) {
                log( "directory already exists" );
            }
        } else {
            if( verbose ) {
                log( "directory created OK" );
            }
        }
    }

    /**
     * Runs the task.
     */
    public void execute()
        throws BuildException
    {
        checkConfiguration();

        FTPClient ftp = null;

        try
        {
            log("Opening FTP connection to " + server, Project.MSG_VERBOSE);

            ftp = new FTPClient();

            ftp.connect(server, port);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode()))
            {
                throw new BuildException("FTP connection failed: " + ftp.getReplyString());
            }

            log("connected", Project.MSG_VERBOSE);
            log("logging in to FTP server", Project.MSG_VERBOSE);

            if (!ftp.login(userid, password))
            {
                throw new BuildException("Could not login to FTP server");
            }

            log("login succeeded", Project.MSG_VERBOSE);

            if (binary)
            {
                ftp.setFileType(com.oroinc.net.ftp.FTP.IMAGE_FILE_TYPE);
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode()))
                {
                    throw new BuildException(
                                             "could not set transfer type: " +
                                             ftp.getReplyString());
                }
            }

            if (passive)
            {
                log("entering passive mode", Project.MSG_VERBOSE);
                ftp.enterLocalPassiveMode();
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode()))
                {
                    throw new BuildException(
                                             "could not enter into passive mode: " +
                                             ftp.getReplyString());
                }
            }


            if( action == MK_DIR ) {

                makeRemoteDir( ftp, remotedir );

            } else {
                if (remotedir != null)
                {
                    log("changing the remote directory", Project.MSG_VERBOSE);
                    ftp.changeWorkingDirectory(remotedir);
                    if (!FTPReply.isPositiveCompletion(ftp.getReplyCode()))
                    {
                        throw new BuildException(
                                                 "could not change remote directory: " +
                                                 ftp.getReplyString());
                    }

                    log(ACTION_STRS[action] + " files");
                    transferFiles(ftp);
                }
            }

        }
        catch(IOException ex)
        {
            throw new BuildException("error during FTP transfer: " + ex);
        }
        finally
        {
            if (ftp != null && ftp.isConnected())
            {
                try
                {
                    log("disconnecting", Project.MSG_VERBOSE);
                    ftp.logout();
                    ftp.disconnect();
                }
                catch(IOException ex)
                {
                }
            }
        }
    }
}
