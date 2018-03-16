package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.ant.util.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.ant.types.EnumeratedAttribute;



/**
 * Untar a file.
 * <p>For JDK 1.1 &quot;last modified time&quot; field is set to current time instead of being
 * carried from the archive file.</p>
 * <p>PatternSets are used to select files to extract
 * <I>from</I> the archive.  If no patternset is used, all files are extracted.
 * </p>
 * <p>FileSet>s may be used used to select archived files
 * to perform unarchival upon.
 * </p>
 * <p>File permissions will not be restored on extracted files.</p>
 * <p>The untar task recognizes the long pathname entries used by GNU tar.<p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author Magesh Umasankar
 *
 * @since Ant 1.1
 *
 * @ant.task category="packaging"
 */
public class Untar extends Expand {
    /**
     *   compression method
     */
    private UntarCompressionMethod compression = new UntarCompressionMethod();

    /**
     * Set decompression algorithm to use; default=none.
     *
     * Allowable values are
     * <ul>
     *   <li>none - no compression
     *   <li>gzip - Gzip compression
     *   <li>bzip2 - Bzip2 compression
     * </ul>
     *
     * @param method compression method
     */
    public void setCompression(UntarCompressionMethod method) {
        compression = method;
    }

    protected void expandFile(FileUtils fileUtils, File srcF, File dir) {
        TarInputStream tis = null;
        try {
            log("Expanding: " + srcF + " into " + dir, Project.MSG_INFO);
            tis = new TarInputStream(
                compression.decompress(srcF,
                    new BufferedInputStream(
                        new FileInputStream(srcF))));
            TarEntry te = null;

            while ((te = tis.getNextEntry()) != null) {
                extractFile(fileUtils, srcF, dir, tis,
                            te.getName(), te.getModTime(), te.isDirectory());
            }
            log("expand complete", Project.MSG_VERBOSE);

        } catch (IOException ioe) {
            throw new BuildException("Error while expanding " + srcF.getPath(),
                                     ioe, location);
        } finally {
            if (tis != null) {
                try {
                    tis.close();
                } catch (IOException e) {}
            }
        }
    }

    /**
     * Valid Modes for Compression attribute to Untar Task
     *
     */
    public static final class UntarCompressionMethod
        extends EnumeratedAttribute {

        /**
         *  No compression
         */
        private static final String NONE = "none";
        /**
         *  GZIP compression
         */
        private static final String GZIP = "gzip";
        /**
         *  BZIP2 compression
         */
        private static final String BZIP2 = "bzip2";


        /**
         *  Constructor
         */
        public UntarCompressionMethod() {
            super();
            setValue(NONE);
        }

        /**
         * Get valid enumeration values
         *
         * @return valid values
         */
        public String[] getValues() {
            return new String[] { NONE, GZIP, BZIP2 };
        }

        /**
         *  This method wraps the input stream with the
         *     corresponding decompression method
         *
         *  @param file provides location information for BuildException
         *  @param istream input stream
         *  @return input stream with on-the-fly decompression
         *  @exception IOException thrown by GZIPInputStream constructor
         *  @exception BuildException thrown if bzip stream does not
         *     start with expected magic values
         */
        private InputStream decompress(final File file,
                                       final InputStream istream)
            throws IOException, BuildException {
            final String value = getValue();
            if (GZIP.equals(value)) {
                return new GZIPInputStream(istream);
            } else {
                if (BZIP2.equals(value)) {
                    final char[] magic = new char[] { 'B', 'Z' };
                    for (int i = 0; i < magic.length; i++) {
                        if (istream.read() != magic[i]) {
                            throw new BuildException(
                                "Invalid bz2 file." + file.toString());
                        }
                    }
                    return new CBZip2InputStream(istream);
                }
            }
            return istream;
        }
    }
}
