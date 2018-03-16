package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FileList;

import org.apache.tools.ant.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;


/**
 * This class contains the 'concat' task, used to concatenate a series
 * of files into a single stream. The destination of this stream may
 * be the system console, or a file. The following is a sample
 * invocation:
 *
 * <pre>
 * &lt;concat destfile=&quot;${build.dir}/index.xml&quot;
 *   append=&quot;false&quot;&gt;
 *
 *   &lt;fileset dir=&quot;${xml.root.dir}&quot;
 *     includes=&quot;*.xml&quot; /&gt;
 *
 * &lt;/concat&gt;
 * </pre>
 *
 * @author <a href="mailto:derek@activate.net">Derek Slager</a>
 */
public class Concat extends Task {


    /**
     * The destination of the stream. If <code>null</code>, the system
     * console is used.
     */
    private File destinationFile = null;

    /**
     * Whether or not the stream should be appended if the destination file 
     * exists.
     * Defaults to <code>false</code>.
     */
    private boolean append = false;

    /**
     * Stores the input file encoding.
     */
    private String encoding = null;


    /**
     * This buffer stores the text within the 'concat' element.
     */
    private StringBuffer textBuffer;

    /**
     * Stores a collection of file sets and/or file lists, used to
     * select multiple files for concatenation.
     */


    /**
     * Public, no-argument constructor. Required by Ant.
     */
    public Concat() {}


    /**
     * Sets the destination file, or uses the console if not specified.
     */
    public void setDestfile(File destinationFile) {
        this.destinationFile = destinationFile;
    }

    /**
     * Sets the behavior when the destination file exists. If set to
     * <code>true</code> the stream data will be appended to the
     * existing file, otherwise the existing file will be
     * overwritten. Defaults to <code>false</code>.
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    /**
     * Sets the encoding for the input files, used when displaying the
     * data via the console.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }


    /**
     * Set of files to concatenate.
     */
    public void addFileset(FileSet set) {
        fileSets.addElement(set);
    }

    /**
     * List of files to concatenate.
     */
    public void addFilelist(FileList list) {
        fileSets.addElement(list);
    }

    /**
     * This method adds text which appears in the 'concat' element.
     */
    public void addText(String text) {
        if (textBuffer == null) {
            textBuffer = new StringBuffer(text.length());
        }

        textBuffer.append(text);
    }

    /**
     * This method performs the concatenation.
     */
    public void execute() 
        throws BuildException {

        sanitizeText();

        if (fileSets.size() == 0 && textBuffer == null) {
            throw new BuildException("At least one file " + 
                                     "must be provided, or " + 
                                     "some text.");
        }

        if (fileSets.size() > 0 && textBuffer != null) {
            throw new BuildException("Cannot include inline text " + 
                                     "when using filesets.");
        }

        boolean savedAppend = append;
        try {
            for (Enumeration e = fileSets.elements(); e.hasMoreElements();) {
                
                File fileSetBase = null;
                
                String[] srcFiles = null;
                
                Object next = e.nextElement();
                
                if (next instanceof FileSet) {
                    
                    FileSet fileSet = (FileSet) next;
                    
                    DirectoryScanner scanner = 
                        fileSet.getDirectoryScanner(getProject());
                    
                    fileSetBase = fileSet.getDir(getProject());
                    
                    srcFiles = scanner.getIncludedFiles();
                    
                } else if (next instanceof FileList) {
                    
                    FileList fileList = (FileList) next;
                    
                    fileSetBase = fileList.getDir(getProject());
                    
                    srcFiles = fileList.getFiles(getProject());
                    
                }

                if (srcFiles != null) {
                    catFiles(fileSetBase, srcFiles);
                } else {
                    log("Warning: Concat received empty fileset.", 
                        Project.MSG_WARN);
                }
            }
        } finally {
            append = savedAppend;
        }
        
        catText();
    }

    /**
     * Reset state to default.
     */
    public void reset() {
        append = false;
        destinationFile = null;
        encoding = null;
        fileSets = new Vector();
    }

    /**
     * This method concatenates a series of files to a single
     * destination.
     *
     * @param base the base directory for the list of file names.
     *
     * @param files the names of the files to be concatenated,
     * relative to the <code>base</code>.
     */
    private void catFiles(File base, String[] files) {

        Vector inputFileNames = new Vector();
        for (int i = 0; i < files.length; i++) {

            File current = new File(base, files[i]);

            if (!current.exists()) {
                log("File " + current + " does not exist.", 
                    Project.MSG_ERR);
                continue;
            }

            inputFileNames.addElement(current.getAbsolutePath());
        }

        final int len = inputFileNames.size();
        if (len == 0) {
            log("Warning: Could not find any of the files specified " +
                "in concat task.", Project.MSG_WARN);
            return;
        }

        String[] input = new String[len];
        inputFileNames.copyInto(input);

        if (encoding == null) {
            OutputStream os = null;
            InputStream is = null;

            try {

                if (destinationFile == null) {
                    os = new LogOutputStream(this, Project.MSG_WARN);
                } else {
                    os = 
                        new FileOutputStream(destinationFile.getAbsolutePath(),
                                             append);
                    
                    append = true;
                }
            
                for (int i = 0; i < len; i++) {

                    if (destinationFile != null &&
                        destinationFile.getAbsolutePath().equals(input[i])) {
                        throw new BuildException("Input file \"" 
                            + destinationFile.getName() 
                            + "\" is the same as the output file.");
                    }

                    is = new FileInputStream(input[i]);
                    byte[] buffer = new byte[8192];
                    while (true) {
                        int bytesRead = is.read(buffer);
                            break;
                        }
                        
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                    is.close();
                    is = null;
                }
            } catch (IOException ioex) {
                throw new BuildException("Error while concatenating: "
                                         + ioex.getMessage(), ioex);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception ignore) {}
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (Exception ignore) {}
                }
            }


            Writer out = null;
            BufferedReader in = null;

            try {
                if (destinationFile == null) {
                    out = new OutputStreamWriter(
                              new LogOutputStream(this, Project.MSG_WARN));
                } else {
                    out = new OutputStreamWriter(
                              new FileOutputStream(destinationFile
                                                   .getAbsolutePath(),
                                                   append),
                              encoding);
                    
                    append = true;
                }

                for (int i = 0; i < len; i++) {
                    in = new BufferedReader(
                            new InputStreamReader(new FileInputStream(input[i]), 
                                encoding));

                    String line;
                    char[] buffer = new char[4096];
                    while (true) {
                        int charsRead = in.read(buffer);
                            break;
                        }
                        
                        out.write(buffer, 0, charsRead);
                    }
                    out.flush();
                    in.close();
                    in = null;
                }
            } catch (IOException ioe) {
                throw new BuildException("Error while concatenating: " 
                                         + ioe.getMessage(), ioe);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ignore) {}
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception ignore) {}
                }
            }
        }
    }

    /**
     * This method concatenates the text which was added inside the
     * 'concat' tags. If the text between the tags consists only of
     * whitespace characters, it is ignored.
     */
    private void catText() {

        if (textBuffer == null) {
            return;
        }

        String text = textBuffer.toString();

        text = ProjectHelper.replaceProperties(getProject(), text,
                                               getProject().getProperties());

        FileWriter writer = null;
        if (destinationFile != null) {
            try {
                writer = new FileWriter(destinationFile.getAbsolutePath(), 
                                        append);
            } catch (IOException ioe) {
                throw new BuildException("Error creating destination " + 
                                         "file.", ioe);
            }
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(text));

            String line;
            while ((line = reader.readLine()) != null) {
                if (destinationFile == null) {
                    log(line, Project.MSG_WARN);
                } else {
                    writer.write(line);
                    writer.write(StringUtils.LINE_SEP);
                    writer.flush();
                }
            }

        } catch (IOException ioe) {
            throw new BuildException("Error while concatenating " + 
                                     "text.", ioe);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignore) {}
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ignore) {}
            }
        }
    }

    /**
     * Treat empty nested text as no text.
     *
     * <p>Depending on the XML parser, addText may have been called
     * for &quot;ignorable whitespace&quot; as well.</p>
     */
    private void sanitizeText() {
        if (textBuffer != null) {
            if (textBuffer.toString().trim().length() == 0) {
                textBuffer = null;
            }
        }
    }

}
