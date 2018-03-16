package org.apache.tools.ant.util;

import java.io.*;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.util.Stack;

import org.apache.tools.ant.BuildException; 
import org.apache.tools.ant.Project; 
import org.apache.tools.ant.types.FilterSetCollection; 

/**
 * This class also encapsulates methods which allow Files to be
 * refered to using abstract path names which are translated to native
 * system file paths at runtime as well as copying files or setting
 * there last modification time.
 *
 * @author duncan@x180.com
 * @author <a href="mailto:conor@apache.org">Conor MacNeill</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
 
public class FileUtils {
    private static Object lockReflection = new Object();
    private static java.lang.reflect.Method setLastModified = null;

    /**
     * Factory method.
     */
    public static FileUtils newFileUtils() {
        return new FileUtils();
    }

    /**
     * Empty constructor.
     */
    protected FileUtils() {}

    /**
     * Convienence method to copy a file from a source to a destination.
     * No filtering is performed.
     *
     * @throws IOException
     */
    public void copyFile(String sourceFile, String destFile) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), null, false, false);
    }

    /**
     * Convienence method to copy a file from a source to a destination
     * specifying if token filtering must be used.
     *
     * @throws IOException
     */
    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters)
        throws IOException
    {
        copyFile(new File(sourceFile), new File(destFile), filters, false, false);
    }

    /**
     * Convienence method to copy a file from a source to a
     * destination specifying if token filtering must be used and if
     * source files may overwrite newer destination files.
     *
     * @throws IOException 
     */
    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters,
                         boolean overwrite) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), filters, 
                 overwrite, false);
    }

     /**
     * Convienence method to copy a file from a source to a
     * destination specifying if token filtering must be used, if
     * source files may overwrite newer destination files and the
     * last modified time of <code>destFile</code> file should be made equal
     * to the last modified time of <code>sourceFile</code>.
     *
     * @throws IOException 
     */
    public void copyFile(String sourceFile, String destFile, FilterSetCollection filters,
                         boolean overwrite, boolean preserveLastModified)
        throws IOException {
        copyFile(new File(sourceFile), new File(destFile), filters, 
                 overwrite, preserveLastModified);
    }

    /**
     * Convienence method to copy a file from a source to a destination.
     * No filtering is performed.
     *
     * @throws IOException
     */
    public void copyFile(File sourceFile, File destFile) throws IOException {
        copyFile(sourceFile, destFile, null, false, false);
    }

    /**
     * Convienence method to copy a file from a source to a destination
     * specifying if token filtering must be used.
     *
     * @throws IOException
     */
    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters)
        throws IOException {
        copyFile(sourceFile, destFile, filters, false, false);
    }

    /**
     * Convienence method to copy a file from a source to a
     * destination specifying if token filtering must be used and if
     * source files may overwrite newer destination files.
     *
     * @throws IOException 
     */
    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters,
                         boolean overwrite) throws IOException {
        copyFile(sourceFile, destFile, filters, overwrite, false);
    }

    /**
     * Convienence method to copy a file from a source to a
     * destination specifying if token filtering must be used, if
     * source files may overwrite newer destination files and the
     * last modified time of <code>destFile</code> file should be made equal
     * to the last modified time of <code>sourceFile</code>.
     *
     * @throws IOException 
     */
    public void copyFile(File sourceFile, File destFile, FilterSetCollection filters,
                         boolean overwrite, boolean preserveLastModified)
        throws IOException {
        
        if (overwrite || !destFile.exists() ||
            destFile.lastModified() < sourceFile.lastModified()) {

            if (destFile.exists() && destFile.isFile()) {
                destFile.delete();
            }

            File parent = new File(destFile.getParent());
            if (!parent.exists()) {
                parent.mkdirs();
            }

            if (filters != null && filters.hasFilters()) {
                BufferedReader in = new BufferedReader(new FileReader(sourceFile));
                BufferedWriter out = new BufferedWriter(new FileWriter(destFile));

                int length;
                String newline = null;
                String line = in.readLine();
                while (line != null) {
                    if (line.length() == 0) {
                        out.newLine();
                    } else {
                        newline = filters.replaceTokens(line);
                        out.write(newline);
                        out.newLine();
                    }
                    line = in.readLine();
                }

                out.close();
                in.close();
            } else {
                FileInputStream in = new FileInputStream(sourceFile);
                FileOutputStream out = new FileOutputStream(destFile);

                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    out.write(buffer, 0, count);
                    count = in.read(buffer, 0, buffer.length);
                } while (count != -1);

                in.close();
                out.close();
            }

            if (preserveLastModified) {
                setFileLastModified(destFile, sourceFile.lastModified());
            }
        }
    }

    /**
     * see whether we have a setLastModified method in File and return it.
     */
    protected final Method getSetLastModified() {
        if (Project.getJavaVersion() == Project.JAVA_1_1) {
            return null;
        }
        if (setLastModified == null) {
            synchronized (lockReflection) {
                if (setLastModified == null) {
                    try {
                        setLastModified = 
                            java.io.File.class.getMethod("setLastModified", 
                                                         new Class[] {Long.TYPE});
                    } catch (NoSuchMethodException nse) {
                        throw new BuildException("File.setlastModified not in JDK > 1.1?",
                                                 nse);
                    }
                }
            }
        }
        return setLastModified;
    }

    /**
     * Calls File.setLastModified(long time) in a Java 1.1 compatible way.
     */
    public void setFileLastModified(File file, long time) throws BuildException {
        if (Project.getJavaVersion() == Project.JAVA_1_1) {
            return;
        }
        Long[] times = new Long[1];
        if (time < 0) {
            times[0] = new Long(System.currentTimeMillis());
        } else {
            times[0] = new Long(time);
        }

        try {
            getSetLastModified().invoke(file, times);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            Throwable nested = ite.getTargetException();
            throw new BuildException("Exception setting the modification time "
                                     + "of " + file, nested);
        } catch (Throwable other) {
            throw new BuildException("Exception setting the modification time "
                                     + "of " + file, other);
        }
    }

    /**
     * Interpret the filename as a file relative to the given file -
     * unless the filename already represents an absolute filename.
     *
     * @param file the "reference" file for relative paths. This
     * instance must be an absolute file and must not contain
     * &quot;./&quot; or &quot;../&quot; sequences (same for \ instead
     * of /).
     * @param filename a file name
     *
     * @return an absolute file that doesn't contain &quot;./&quot; or
     * &quot;../&quot; sequences and uses the correct separator for
     * the current platform.
     */
    public File resolveFile(File file, String filename) {
        filename = filename.replace('/', File.separatorChar)
            .replace('\\', File.separatorChar);

        if (filename.startsWith(File.separator) ||

            (filename.length() >= 2 &&
             Character.isLetter(filename.charAt(0)) &&
             filename.charAt(1) == ':')

            ) {
            return normalize(filename);
        }

        if (filename.length() >= 2 &&
            Character.isLetter(filename.charAt(0)) &&
            filename.charAt(1) == ':') {
            return normalize(filename);
        }

        File helpFile = new File(file.getAbsolutePath());
        StringTokenizer tok = new StringTokenizer(filename, File.separator);
        while (tok.hasMoreTokens()) {
            String part = tok.nextToken();
            if (part.equals("..")) {
                String parentFile = helpFile.getParent();
                if (parentFile == null) {
                    String msg = "The file or path you specified ("
                        + filename + ") is invalid relative to " 
                        + file.getPath();
                    throw new BuildException(msg);
                }
                helpFile = new File(parentFile);
            } else if (part.equals(".")) {
            } else {
                helpFile = new File(helpFile, part);
            }
        }

        return new File(helpFile.getAbsolutePath());
    }

    /**
     * &quot;normalize&quot; the given absolute path.
     *
     * <p>This includes:
     * <ul>
     *   <li>Uppercase the drive letter if there is one.</li>
     *   <li>Remove redundant slashes after the drive spec.</li>
     *   <li>resolve all ./, .\, ../ and ..\ sequences.</li>
     *   <li>DOS style paths that start with a drive letter will have
     *     \ as the separator.</li> 
     * </ul>
     */
    public File normalize(String path) {
        String orig = path;

        path = path.replace('/', File.separatorChar)
            .replace('\\', File.separatorChar);

        if (!path.startsWith(File.separator) &&
            ! (path.length() >= 2 &&
               Character.isLetter(path.charAt(0)) &&
               path.charAt(1) == ':')
            ) {             
            String msg = path + " is not an absolute path";
            throw new BuildException(msg);
        }
            
        boolean dosWithDrive = false;
        String root = null;
        if (path.length() >= 2 &&
            Character.isLetter(path.charAt(0)) &&
            path.charAt(1) == ':') {

            dosWithDrive = true;

            char[] ca = path.replace('/', '\\').toCharArray();
            StringBuffer sb = new StringBuffer();
            sb.append(Character.toUpperCase(ca[0])).append(':');

            for (int i = 2; i < ca.length; i++) {
                if ((ca[i] != '\\') ||
                    (ca[i] == '\\' && ca[i - 1] != '\\')
                    ) {
                    sb.append(ca[i]);
                }
            }

            path = sb.toString().replace('\\', File.separatorChar);
            if (path.length() == 2) {
                root = path;
                path = "";
            } else {
                root = path.substring(0, 3);
                path = path.substring(3);
            }
            
        } else {
            if (path.length() == 1) {
                root = File.separator;
                path = "";
            } else if (path.charAt(1) == File.separatorChar) {
                root = File.separator+File.separator;
                path = path.substring(2);
            } else {
                root = File.separator;
                path = path.substring(1);
            }
        }

        Stack s = new Stack();
        s.push(root);
        StringTokenizer tok = new StringTokenizer(path, File.separator);
        while (tok.hasMoreTokens()) {
            String thisToken = tok.nextToken();
            if (".".equals(thisToken)) {
                continue;
            } else if ("..".equals(thisToken)) {
                if (s.size() < 2) {
                    throw new BuildException("Cannot resolve path "+orig);
                } else {
                    s.pop();
                }
                s.push(thisToken);
            }
        }

        StringBuffer sb = new StringBuffer();
        for (int i=0; i<s.size(); i++) {
            if (i > 1) {
                sb.append(File.separatorChar);
            }
            sb.append(s.elementAt(i));
        }
        

        path = sb.toString();
        if (dosWithDrive) {
            path = path.replace('/', '\\');
        }
        return new File(path);
    }
}

