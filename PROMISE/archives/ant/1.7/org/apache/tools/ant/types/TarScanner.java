package org.apache.tools.ant.types;

import java.io.IOException;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.resources.TarResource;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

/**
 * Scans tar archives for resources.
 */
public class TarScanner extends ArchiveScanner {

    /**
     * Fills the file and directory maps with resources read from the
     * archive.
     *
     * @param src the archive to scan.
     * @param encoding encoding used to encode file names inside the archive.
     * @param fileEntries Map (name to resource) of non-directory
     * resources found inside the archive.
     * @param matchFileEntries Map (name to resource) of non-directory
     * resources found inside the archive that matched all include
     * patterns and didn't match any exclude patterns.
     * @param dirEntries Map (name to resource) of directory
     * resources found inside the archive.
     * @param matchDirEntries Map (name to resource) of directory
     * resources found inside the archive that matched all include
     * patterns and didn't match any exclude patterns.
     */
    protected void fillMapsFromArchive(Resource src, String encoding,
                                       Map fileEntries, Map matchFileEntries,
                                       Map dirEntries, Map matchDirEntries) {
        TarEntry entry = null;
        TarInputStream ti = null;

        try {
            try {
                ti = new TarInputStream(src.getInputStream());
            } catch (IOException ex) {
                throw new BuildException("problem opening " + srcFile, ex);
            }
            while ((entry = ti.getNextEntry()) != null) {
                Resource r = new TarResource(src, entry);
                String name = entry.getName();
                if (entry.isDirectory()) {
                    name = trimSeparator(name);
                    dirEntries.put(name, r);
                    if (match(name)) {
                        matchDirEntries.put(name, r);
                    }
                } else {
                    fileEntries.put(name, r);
                    if (match(name)) {
                        matchFileEntries.put(name, r);
                    }
                }
            }
        } catch (IOException ex) {
            throw new BuildException("problem reading " + srcFile, ex);
        } finally {
            FileUtils.close(ti);
        }
    }
}
