package org.apache.tools.ant.taskdefs.optional;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.*;

import java.io.File;

/**
 * Convert files from native encodings to ascii.
 *
 * @author Drew Sudell <asudell@acm.org>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class Native2Ascii extends MatchingTask {


    private Mapper mapper;

    /**
     * Flag the conversion to run in the reverse sense,
     * that is Ascii to Native encoding.
     * 
     * @param reverse True if the conversion is to be reversed,
     *                otherwise false;
     */
    public void setReverse(boolean reverse){
        this.reverse = reverse;
    }

    /**
     * Set the encoding to translate to/from.
     * If unset, the default encoding for the JVM is used.
     *
     * @param encoding String containing the name of the Native 
     *                 encoding to convert from or to.
     */
    public void setEncoding(String encoding){
        this.encoding = encoding;
    }

    /**
     * Set the source directory in which to find files to convert.
     *
     * @param srcDir Direcrory to find input file in.
     */
    public void setSrc(File srcDir){
        this.srcDir = srcDir;
    }


    /**
     * Set the destination dirctory to place converted files into.
     *
     * @param destDir directory to place output file into.
     */
    public void setDest(File destDir){
        this.destDir = destDir;
    }

    /**
     * Set the extension which converted files should have.
     * If unset, files will not be renamed.
     *
     * @param ext File extension to use for converted files.
     */
    public void setExt(String ext){
        this.extension = ext;
    }

    /**
     * Defines the FileNameMapper to use (nested mapper element).
     */
    public Mapper createMapper() throws BuildException {
        if (mapper != null) {
            throw new BuildException("Cannot define more than one mapper",
                                     location);
        }
        mapper = new Mapper(project);
        return mapper;
    }

    public void execute() throws BuildException {


        if (srcDir == null){
            srcDir = project.resolveFile(".");
        }

        if (destDir == null){
            throw new BuildException("The dest attribute must be set.");
        }

        if (srcDir.equals(destDir) && extension == null && mapper == null){
            throw new BuildException("The ext attribute or a mapper must be set if"
                                     + " src and dest dirs are the same.");
        }

        FileNameMapper m = null;
        if (mapper == null) {
            if (extension == null) {
                m = new IdentityMapper();
            } else {
                m = new ExtMapper();
            }
        } else {
            m = mapper.getImplementation();
        }
        
        scanner = getDirectoryScanner(srcDir);
        files = scanner.getIncludedFiles();
        SourceFileScanner sfs = new SourceFileScanner(this);
        files = sfs.restrict(files, srcDir, destDir, m);
        int count = files.length;
        if (count == 0) {
            return;
        }
        String message = "Converting "+ count + " file"
            + (count != 1 ? "s" : "") + " from ";
        log(message + srcDir + " to " + destDir);
        for (int i = 0; i < files.length; i++){
            convert(files[i], m.mapFileName(files[i])[0]);
        }
    }

    /**
     * Convert a single file.
     *
     * @param fileName Name of the file to convert (relative to srcDir).
     */
    private void convert(String srcName, String destName) throws BuildException {


        if (reverse){
            cmd.createArgument().setValue("-reverse");
        }

        if (encoding != null){
            cmd.createArgument().setValue("-encoding");
            cmd.createArgument().setValue(encoding);
        }

        srcFile = new File(srcDir, srcName);
        destFile = new File(destDir, destName);

        cmd.createArgument().setFile(srcFile);
        cmd.createArgument().setFile(destFile);
        if (srcFile.equals(destFile)){
            throw new BuildException("file " + srcFile 
                                     + " would overwrite its self");
        }

        String parentName = destFile.getParent();
        if (parentName != null){
            File parentFile = new File(parentName);
            
            if ((! parentFile.exists()) && ( ! parentFile.mkdirs())){
                throw new BuildException("cannot create parent directory "
                                         + parentName);
            }
        }
                        
        log("converting " + srcName, Project.MSG_VERBOSE);
        sun.tools.native2ascii.Main n2a
            = new sun.tools.native2ascii.Main();
        if(! n2a.convert(cmd.getArguments())){
            throw new BuildException("conversion failed");
        }
    }

    private class ExtMapper implements FileNameMapper {

        public void setFrom(String s) {}
        public void setTo(String s) {}

        public String[] mapFileName(String fileName) {
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot >= 0) {
                return new String[] {fileName.substring(0, lastDot) + extension};
            } else {
                return new String[] {fileName + extension};
            }
        }
    }
}
