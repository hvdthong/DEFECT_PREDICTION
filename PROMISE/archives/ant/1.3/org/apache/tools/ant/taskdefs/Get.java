package org.apache.tools.ant.taskdefs;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.tools.ant.*;

/**
 * Get a particular file from a URL source. 
 * Options include verbose reporting, timestamp based fetches and controlling 
 * actions on failures. NB: access through a firewall only works if the whole 
 * Java runtime is correctly configured.
 *
 * @author costin@dnt.ro
 */
public class Get extends Task {
    private boolean verbose = false;
    private boolean ignoreErrors = false;
    
    /**
     * Does the work.
     *
     * @exception BuildException Thrown in unrecoverable error.
     */
    public void execute() throws BuildException {
        if (source == null) {
            throw new BuildException("src attribute is required", location);
        }

        if (dest == null) {
            throw new BuildException("dest attribute is required", location);
        }

        if (dest.exists() && dest.isDirectory()) { 
            throw new BuildException("The specified destination is a directory",
                                     location);
        }

        if (dest.exists() && !dest.canWrite()) { 
            throw new BuildException("Can't write to " + dest.getAbsolutePath(),
                                     location);
        }

        try {

            log("Getting: " + source);

            long timestamp=0;

            boolean hasTimestamp=false;
            if(useTimestamp && dest.exists()) {
                timestamp=dest.lastModified();
                if (verbose)  {
                    Date t=new Date(timestamp);
                    log("local file date : "+t.toString());
                }
                
                hasTimestamp=true;
            }
        
            URLConnection connection=source.openConnection();
            if(useTimestamp && hasTimestamp) {
                connection.setIfModifiedSince(timestamp);
            }

            connection.connect();
            if(connection instanceof HttpURLConnection)  {
                HttpURLConnection httpConnection=(HttpURLConnection)connection;
                if(httpConnection.getResponseCode()==HttpURLConnection.HTTP_NOT_MODIFIED)  {
                    log("Not modified - so not downloaded");
                    return; 
                }
            }

                   
            FileOutputStream fos = new FileOutputStream(dest);

            InputStream is=null;
            for( int i=0; i< 3 ; i++ ) {
                try {
                    is = connection.getInputStream();
                    break;
                } catch( IOException ex ) {
                    log( "Error opening connection " + ex );
                }
            }
            if( is==null ) {
                log( "Can't get " + source + " to " + dest);
                if(ignoreErrors) 
                    return;
                throw new BuildException( "Can't get " + source + " to " + dest,
                                          location);
            }
                
            byte[] buffer = new byte[100 * 1024];
            int length;
            
            while ((length = is.read(buffer)) >= 0) {
                fos.write(buffer, 0, length);
                if (verbose) System.out.print(".");
            }
            if(verbose) System.out.println();
            fos.close();
            is.close();
           
            if(useTimestamp)  {
                long remoteTimestamp=connection.getLastModified();
                if (verbose)  {
                    Date t=new Date(remoteTimestamp);
                    log("last modified = "+t.toString()
                        +((remoteTimestamp==0)?" - using current time instead":""));
                }
                if(remoteTimestamp!=0)
                    touchFile(dest,remoteTimestamp);
            }

           

        } catch (IOException ioe) {
            log("Error getting " + source + " to " + dest );
            if(ignoreErrors) 
                return;
            throw new BuildException(ioe, location);
        }
    }
    
    /** 
     * set the timestamp of a named file to a specified time.
     *
     * @param filename
     * @param time in milliseconds since the start of the era
     * @return true if it succeeded. False means that this is a
     * java1.1 system and that file times can not be set
     *@exception BuildException Thrown in unrecoverable error. Likely
     *this comes from file access failures.
     */
    protected boolean touchFile(File file, long timemillis) 
        throws BuildException  {

        if (project.getJavaVersion() != Project.JAVA_1_1) {
            Touch touch = (Touch) project.createTask("touch");
            touch.setOwningTarget(target);
            touch.setTaskName(getTaskName());
            touch.setLocation(getLocation());
            touch.setFile(file);
            touch.setMillis(timemillis);
            touch.touch();
            return true;
            
        } else {
            return false;
        }
    }        

    /**
     * Set the URL.
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
     * Be verbose, if set to "<CODE>true</CODE>".
     *
     * @param v if "true" then be verbose
     */
    public void setVerbose(boolean v) {
        verbose = v;
    }

    /**
     * Don't stop if get fails if set to "<CODE>true</CODE>".
     *
     * @param v if "true" then don't report download errors up to ant
     */
    public void setIgnoreErrors(boolean v) {
        ignoreErrors = v;
    }

    /**
     * Use timestamps, if set to "<CODE>true</CODE>".
     *
     * <p>In this situation, the if-modified-since header is set so that the file is
     * only fetched if it is newer than the local file (or there is no local file)
     * This flag is only valid on HTTP connections, it is ignored in other cases.
     * When the flag is set, the local copy of the downloaded file will also 
     * have its timestamp set to the remote file time. 
     * <br>
     * Note that remote files of date 1/1/1970 (GMT) are treated as 'no timestamp', and
     * web servers often serve files with a timestamp in the future by replacing their timestamp
     * with that of the current time. Also, inter-computer clock differences can cause no end of 
     * grief. 
     * @param v "true" to enable file time fetching
     */
    public void setUseTimestamp(boolean v) {
        if (project.getJavaVersion() != Project.JAVA_1_1) {
            useTimestamp = v;
        }
    }

}
