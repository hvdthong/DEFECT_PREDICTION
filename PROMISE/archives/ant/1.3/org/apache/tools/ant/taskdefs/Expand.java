package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import java.io.*;
import java.util.zip.*;
/**
 * Unzip a file. 
 *
 * @author costin@dnt.ro
 * @author <a href="mailto:stefan.bodewig@megabit.net">Stefan Bodewig</a>
 */
public class Expand extends MatchingTask {
    
    /**
     * Do the work.
     *
     * @exception BuildException Thrown in unrecoverable error.
     */
    public void execute() throws BuildException {
        if ("expand".equals(taskType)) {
            log("!! expand is deprecated. Use unzip instead. !!");
        }
        

        Touch touch = (Touch) project.createTask("touch");
        touch.setOwningTarget(target);
        touch.setTaskName(getTaskName());
        touch.setLocation(getLocation());
        
        if (source == null) {
            throw new BuildException("Source attribute must be specified");
        }
        if (dest == null) {
            throw new BuildException("Dest attribute must be specified");
        }

        if (source.isDirectory()) {
            DirectoryScanner ds = super.getDirectoryScanner(source);
    
            String[] files = ds.getIncludedFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = new File(source, files[i]);
                expandFile(touch, file, dest);
            }
        }
        else {
            expandFile(touch, source, dest);
        }
        

    }

    private void expandFile(Touch touch, File srcF, File dir) {
        ZipInputStream zis = null;
        try {
            log("Expanding: " + srcF + " into " + dir, Project.MSG_INFO);
            zis = new ZipInputStream(new FileInputStream(srcF));
            ZipEntry ze = null;

            while ((ze = zis.getNextEntry()) != null) {
                File f = new File(dir, project.translatePath(ze.getName()));
                try {
                    log("expand-file " + ze.getName() , Project.MSG_VERBOSE );
                    File dirF=new File(f.getParent());
                    dirF.mkdirs();
                    
                    if (ze.isDirectory()) {
                        f.mkdirs(); 
                    } else {
                        byte[] buffer = new byte[1024];
                        int length = 0;
                        FileOutputStream fos = new FileOutputStream(f);
                        
                        while ((length = zis.read(buffer)) >= 0) {
                            fos.write(buffer, 0, length);
                        }
                        
                        fos.close();
                    }
                    
                    if (project.getJavaVersion() != Project.JAVA_1_1) {
                        touch.setFile(f);
                        touch.setMillis(ze.getTime());
                        touch.touch();
                    }
                    
                } catch( FileNotFoundException ex ) {
                    log("Unable to expand to file " + f.getPath(), Project.MSG_WARN);
                }
            }
            log("expand complete", Project.MSG_VERBOSE );
        } catch (IOException ioe) {
            throw new BuildException("Error while expanding " + srcF.getPath(), ioe);
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                }
                catch (IOException e) {}
            }
        }
    }
    
    /**
     * Set the destination directory. File will be unzipped into the
     * destination directory.
     *
     * @param d Path to the directory.
     */
    public void setDest(File d) {
        this.dest=d;
    }

    /**
     * Set the path to zip-file.
     *
     * @param s Path to zip-file.
     */
    public void setSrc(File s) {
        this.source = s;
    }
}
