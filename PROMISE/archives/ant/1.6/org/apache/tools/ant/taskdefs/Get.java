package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Gets a particular file from a URL source.
 * Options include verbose reporting, timestamp based fetches and controlling
 * actions on failures. NB: access through a firewall only works if the whole
 * Java runtime is correctly configured.
 *
 * @since Ant 1.1
 *
 * @ant.task category="network"
 */
public class Get extends Task {

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    private boolean verbose = false;
    private boolean ignoreErrors = false;
    private String uname = null;
    private String pword = null;



    /**
     * Does the work.
     *
     * @exception BuildException Thrown in unrecoverable error.
     */
    public void execute() throws BuildException {

        int logLevel = Project.MSG_INFO;
        DownloadProgress progress = null;
        if (verbose) {
            progress = new VerboseProgress(System.out);
        }

        try {
            doGet(logLevel, progress);
        } catch (IOException ioe) {
            log("Error getting " + source + " to " + dest);
            if (!ignoreErrors) {
                throw new BuildException(ioe, getLocation());
            }
        }
    }

    /**
     * make a get request, with the supplied progress and logging info.
     * All the other config parameters are set at the task level,
     * source, dest, ignoreErrors, etc.
     * @param logLevel level to log at, see {@link Project#log(String, int)}
     * @param progress progress callback; null for no-callbacks
     * @return true for a successful download, false otherwise.
     * The return value is only relevant when {@link #ignoreErrors} is true, as
     * when false all failures raise BuildExceptions.
     * @throws IOException for network trouble
     * @throws BuildException for argument errors, or other trouble when ignoreErrors
     * is false.
     */
    public boolean doGet(int logLevel, DownloadProgress progress)
            throws IOException {
        if (source == null) {
            throw new BuildException("src attribute is required", getLocation());
        }

        if (dest == null) {
            throw new BuildException("dest attribute is required", getLocation());
        }

        if (dest.exists() && dest.isDirectory()) {
            throw new BuildException("The specified destination is a directory",
                    getLocation());
        }

        if (dest.exists() && !dest.canWrite()) {
            throw new BuildException("Can't write to " + dest.getAbsolutePath(),
                    getLocation());
        }
        if (progress == null) {
            progress = new NullProgress();
        }
        log("Getting: " + source, logLevel);
        log("To: " + dest.getAbsolutePath(), logLevel);

        long timestamp = 0;

        boolean hasTimestamp = false;
        if (useTimestamp && dest.exists()) {
            timestamp = dest.lastModified();
            if (verbose) {
                Date t = new Date(timestamp);
                log("local file date : " + t.toString(), logLevel);
            }
            hasTimestamp = true;
        }

        URLConnection connection = source.openConnection();
        if (hasTimestamp) {
            connection.setIfModifiedSince(timestamp);
        }
        if (uname != null || pword != null) {
            String up = uname + ":" + pword;
            String encoding;
            Base64Converter encoder = new Base64Converter();
            encoding = encoder.encode(up.getBytes());
            connection.setRequestProperty ("Authorization",
                    "Basic " + encoding);
        }

        connection.connect();
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection
                    = (HttpURLConnection) connection;
            long lastModified = httpConnection.getLastModified();
            if (httpConnection.getResponseCode()
                    == HttpURLConnection.HTTP_NOT_MODIFIED
                || (lastModified != 0 && hasTimestamp
                && timestamp > lastModified)) {
                log("Not modified - so not downloaded", logLevel);
                return false;
            }
            if (httpConnection.getResponseCode()
                    == HttpURLConnection.HTTP_UNAUTHORIZED)  {
                String message = "HTTP Authorization failure";
                if (ignoreErrors) {
                    log(message, logLevel);
                    return false;
                } else {
                    throw new BuildException(message);
                }
            }

        }


        InputStream is = null;
        for (int i = 0; i < 3; i++) {
            try {
                is = connection.getInputStream();
                break;
            } catch (IOException ex) {
                log("Error opening connection " + ex, logLevel);
            }
        }
        if (is == null) {
            log("Can't get " + source + " to " + dest, logLevel);
            if (ignoreErrors) {
                return false;
            }
            throw new BuildException("Can't get " + source + " to " + dest,
                    getLocation());
        }

        FileOutputStream fos = new FileOutputStream(dest);
        progress.beginDownload();
        boolean finished = false;
        try {
            byte[] buffer = new byte[100 * 1024];
            int length;
            while ((length = is.read(buffer)) >= 0) {
                fos.write(buffer, 0, length);
                progress.onTick();
            }
            finished = true;
        } finally {
            FileUtils.close(fos);
            FileUtils.close(is);

            if (!finished) {
                dest.delete();
            }
        }
        progress.endDownload();

        if (useTimestamp)  {
            long remoteTimestamp = connection.getLastModified();
            if (verbose)  {
                Date t = new Date(remoteTimestamp);
                log("last modified = " + t.toString()
                        + ((remoteTimestamp == 0)
                        ? " - using current time instead"
                        : ""), logLevel);
            }
            if (remoteTimestamp != 0) {
                FILE_UTILS.setFileLastModified(dest, remoteTimestamp);
            }
        }

        return true;
    }


    /**
     * Set the URL to get.
     *
     * @param u URL for the file.
     */
    public void setSrc(URL u) {
        this.source = u;
    }

    /**
     * Where to copy the source file.
     *
     * @param dest Path to file.
     */
    public void setDest(File dest) {
        this.dest = dest;
    }

    /**
     * If true, show verbose progress information.
     *
     * @param v if "true" then be verbose
     */
    public void setVerbose(boolean v) {
        verbose = v;
    }

    /**
     * If true, log errors but do not treat as fatal.
     *
     * @param v if "true" then don't report download errors up to ant
     */
    public void setIgnoreErrors(boolean v) {
        ignoreErrors = v;
    }

    /**
     * If true, conditionally download a file based on the timestamp
     * of the local copy.
     *
     * <p>In this situation, the if-modified-since header is set so
     * that the file is only fetched if it is newer than the local
     * file (or there is no local file) This flag is only valid on
     * HTTP connections, it is ignored in other cases.  When the flag
     * is set, the local copy of the downloaded file will also have
     * its timestamp set to the remote file time.</p>
     *
     * <p>Note that remote files of date 1/1/1970 (GMT) are treated as
     * 'no timestamp', and web servers often serve files with a
     * timestamp in the future by replacing their timestamp with that
     * of the current time. Also, inter-computer clock differences can
     * cause no end of grief.</p>
     * @param v "true" to enable file time fetching
     */
    public void setUseTimestamp(boolean v) {
        useTimestamp = v;
    }


    /**
     * Username for basic auth.
     *
     * @param u username for authentication
     */
    public void setUsername(String u) {
        this.uname = u;
    }

    /**
     * password for the basic authentication.
     *
     * @param p password for authentication
     */
    public void setPassword(String p) {
        this.pword = p;
    }

    /*********************************************************************
    * BASE 64 encoding of a String or an array of bytes.
    *
    * Based on RFC 1421.
    *
    *********************************************************************/

    protected static class Base64Converter {

        public final char[] alphabet = {

        public String encode(String s) {
            return encode(s.getBytes());
        }

        public String encode(byte[] octetString) {
            int bits24;
            int bits6;

            char[] out = new char[((octetString.length - 1) / 3 + 1) * 4];
            int outIndex = 0;
            int i = 0;

            while ((i + 3) <= octetString.length) {
                bits24 = (octetString[i++] & 0xFF) << 16;
                bits24 |= (octetString[i++] & 0xFF) << 8;
                bits24 |= octetString[i++];

                bits6 = (bits24 & 0x00FC0000) >> 18;
                out[outIndex++] = alphabet[bits6];
                bits6 = (bits24 & 0x0003F000) >> 12;
                out[outIndex++] = alphabet[bits6];
                bits6  = (bits24 & 0x00000FC0) >> 6;
                out[outIndex++] = alphabet[bits6];
                bits6 = (bits24 & 0x0000003F);
                out[outIndex++] = alphabet[bits6];
            }
            if (octetString.length - i == 2) {
                bits24 = (octetString[i] & 0xFF) << 16;
                bits24 |= (octetString[i + 1] & 0xFF) << 8;
                bits6 = (bits24 & 0x00FC0000) >> 18;
                out[outIndex++] = alphabet[bits6];
                bits6 = (bits24 & 0x0003F000) >> 12;
                out[outIndex++] = alphabet[bits6];
                bits6 = (bits24 & 0x00000FC0) >> 6;
                out[outIndex++] = alphabet[bits6];

                out[outIndex++] = '=';
            } else if (octetString.length - i == 1) {
                bits24 = (octetString[i] & 0xFF) << 16;
                bits6 = (bits24 & 0x00FC0000) >> 18;
                out[outIndex++] = alphabet[bits6];
                bits6 = (bits24 & 0x0003F000) >> 12;
                out[outIndex++] = alphabet[bits6];

                out[outIndex++] = '=';
                out[outIndex++] = '=';
            }
            return new String(out);
        }
    }

    public interface DownloadProgress {
        /**
         * begin a download
         */
        public void beginDownload();

        /**
         * tick handler
         *
         */
        public void onTick();

        /**
         * end a download
         */
        public void endDownload();
    }

    /**
     * do nothing with progress info
     */
    public static class NullProgress implements DownloadProgress {

        /**
         * begin a download
         */
        public void beginDownload() {

        }

        /**
         * tick handler
         *
         */
        public void onTick() {
        }

        /**
         * end a download
         */
        public void endDownload() {

        }
    }

    /**
     * verbose progress system prints to some output stream
     */
    public static class VerboseProgress implements DownloadProgress  {
        private int dots = 0;
        PrintStream out;

        public VerboseProgress(PrintStream out) {
            this.out = out;
        }

        /**
         * begin a download
         */
        public void beginDownload() {
            dots = 0;
        }

        /**
         * tick handler
         *
         */
        public void onTick() {
            out.print(".");
            if (dots++ > 50) {
                out.flush();
                dots = 0;
            }
        }

        /**
         * end a download
         */
        public void endDownload() {
            out.println();
            out.flush();
        }
    }

}
