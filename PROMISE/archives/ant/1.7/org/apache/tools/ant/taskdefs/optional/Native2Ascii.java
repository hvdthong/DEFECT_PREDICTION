package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.optional.native2ascii.Native2AsciiAdapter;
import org.apache.tools.ant.taskdefs.optional.native2ascii.Native2AsciiAdapterFactory;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;
import org.apache.tools.ant.util.facade.ImplementationSpecificArgument;

/**
 * Converts files from native encodings to ASCII.
 *
 * @since Ant 1.2
 */
public class Native2Ascii extends MatchingTask {


    private Mapper mapper;
    private FacadeTaskHelper facade = null;

    /** No args constructor */
    public Native2Ascii() {
        facade = new FacadeTaskHelper(Native2AsciiAdapterFactory.getDefault());
    }

    /**
     * Flag the conversion to run in the reverse sense,
     * that is Ascii to Native encoding.
     *
     * @param reverse True if the conversion is to be reversed,
     *                otherwise false;
     */
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    /**
     * The value of the reverse attribute.
     * @return the reverse attribute.
     * @since Ant 1.6.3
     */
    public boolean getReverse() {
        return reverse;
    }

    /**
     * Set the encoding to translate to/from.
     * If unset, the default encoding for the JVM is used.
     *
     * @param encoding String containing the name of the Native
     *                 encoding to convert from or to.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * The value of the encoding attribute.
     * @return the encoding attribute.
     * @since Ant 1.6.3
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Set the source directory in which to find files to convert.
     *
     * @param srcDir directory to find input file in.
     */
    public void setSrc(File srcDir) {
        this.srcDir = srcDir;
    }


    /**
     * Set the destination directory to place converted files into.
     *
     * @param destDir directory to place output file into.
     */
    public void setDest(File destDir) {
        this.destDir = destDir;
    }

    /**
     * Set the extension which converted files should have.
     * If unset, files will not be renamed.
     *
     * @param ext File extension to use for converted files.
     */
    public void setExt(String ext) {
        this.extension = ext;
    }

    /**
     * Choose the implementation for this particular task.
     * @param impl the name of the implemenation
     * @since Ant 1.6.3
     */
    public void setImplementation(String impl) {
        if ("default".equals(impl)) {
            facade.setImplementation(Native2AsciiAdapterFactory.getDefault());
        } else {
            facade.setImplementation(impl);
        }
    }

    /**
     * Defines the FileNameMapper to use (nested mapper element).
     *
     * @return the mapper to use for file name translations.
     *
     * @throws BuildException if more than one mapper is defined.
     */
    public Mapper createMapper() throws BuildException {
        if (mapper != null) {
            throw new BuildException("Cannot define more than one mapper",
                                     getLocation());
        }
        mapper = new Mapper(getProject());
        return mapper;
    }

    /**
     * A nested filenamemapper
     * @param fileNameMapper the mapper to add
     * @since Ant 1.6.3
     */
    public void add(FileNameMapper fileNameMapper) {
        createMapper().add(fileNameMapper);
    }

    /**
     * Adds an implementation specific command-line argument.
     * @return a ImplementationSpecificArgument to be configured
     *
     * @since Ant 1.6.3
     */
    public ImplementationSpecificArgument createArg() {
        ImplementationSpecificArgument arg =
            new ImplementationSpecificArgument();
        facade.addImplementationArgument(arg);
        return arg;
    }

    /**
     * Execute the task
     *
     * @throws BuildException is there is a problem in the task execution.
     */
    public void execute() throws BuildException {


        if (srcDir == null) {
            srcDir = getProject().resolveFile(".");
        }

        if (destDir == null) {
            throw new BuildException("The dest attribute must be set.");
        }

        if (srcDir.equals(destDir) && extension == null && mapper == null) {
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
        String message = "Converting " + count + " file"
            + (count != 1 ? "s" : "") + " from ";
        log(message + srcDir + " to " + destDir);
        for (int i = 0; i < files.length; i++) {
            convert(files[i], m.mapFileName(files[i])[0]);
        }
    }

    /**
     * Convert a single file.
     *
     * @param srcName name of the input file.
     * @param destName name of the input file.
     */
    private void convert(String srcName, String destName)
        throws BuildException {

        srcFile = new File(srcDir, srcName);
        destFile = new File(destDir, destName);

        if (srcFile.equals(destFile)) {
            throw new BuildException("file " + srcFile
                                     + " would overwrite its self");
        }

        String parentName = destFile.getParent();
        if (parentName != null) {
            File parentFile = new File(parentName);

            if ((!parentFile.exists()) && (!parentFile.mkdirs())) {
                throw new BuildException("cannot create parent directory "
                                         + parentName);
            }
        }

        log("converting " + srcName, Project.MSG_VERBOSE);
        Native2AsciiAdapter ad =
            Native2AsciiAdapterFactory.getAdapter(facade.getImplementation(),
                                                  this);
        if (!ad.convert(this, srcFile, destFile)) {
            throw new BuildException("conversion failed");
        }
    }

    /**
     * Returns the (implementation specific) settings given as nested
     * arg elements.
     * @return the arguments.
     * @since Ant 1.6.3
     */
    public String[] getCurrentArgs() {
        return facade.getArgs();
    }

    private class ExtMapper implements FileNameMapper {

        public void setFrom(String s) {
        }
        public void setTo(String s) {
        }

        public String[] mapFileName(String fileName) {
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot >= 0) {
                return new String[] {fileName.substring(0, lastDot)
                                         + extension};
            } else {
                return new String[] {fileName + extension};
            }
        }
    }
}
