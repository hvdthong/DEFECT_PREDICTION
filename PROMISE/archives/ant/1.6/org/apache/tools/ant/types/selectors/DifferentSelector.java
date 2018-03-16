package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.IOException;

/**
 * This selector selects files against a mapped set of target files, selecting
 * all those files which are different.
 * Files with different lengths are deemed different
 * automatically
 * Files with identical timestamps are viewed as matching by
 * default, unless you specify otherwise.
 * Contents are compared if the lengths are the same
 * and the timestamps are ignored or the same,
 * except if you decide to ignore contents to gain speed.
 * <p>
 * This is a useful selector to work with programs and tasks that don't handle
 * dependency checking properly; Even if a predecessor task always creates its
 * output files, followup tasks can be driven off copies made with a different
 * selector, so their dependencies are driven on the absolute state of the
 * files, not a timestamp.
 * <p>
 * Clearly, however, bulk file comparisons is inefficient; anything that can
 * use timestamps is to be preferred. If this selector must be used, use it
 * over as few files as possible, perhaps following it with an &lt;uptodate;&gt
 * to keep the descendent routines conditional.
 *
 */
public class DifferentSelector extends MappingSelector {

    private FileUtils fileUtils = FileUtils.newFileUtils();

    private boolean ignoreFileTimes = true;
    private boolean ignoreContents = false;


    /**
     * This flag tells the selector to ignore file times in the comparison
     * @param ignoreFileTimes if true ignore file times
     */
    public void setIgnoreFileTimes(boolean ignoreFileTimes) {
        this.ignoreFileTimes = ignoreFileTimes;
    }
    /**
     * This flag tells the selector to ignore contents
     * @param ignoreContents if true ignore contents
     * @since ant 1.6.3
     */
    public void setIgnoreContents(boolean ignoreContents) {
        this.ignoreContents = ignoreContents;
    }
    /**
     * this test is our selection test that compared the file with the destfile
     * @param srcfile the source file
     * @param destfile the destination file
     * @return true if the files are different
     */
    protected boolean selectionTest(File srcfile, File destfile) {

        if (srcfile.exists() != destfile.exists()) {
            return true;
        }

        if (srcfile.length() != destfile.length()) {
            return true;
        }

        if (!ignoreFileTimes) {
            boolean sameDate;
            sameDate = destfile.lastModified() >= srcfile.lastModified() - granularity
                    && destfile.lastModified() <= srcfile.lastModified() + granularity;

            if (!sameDate) {
                return true;
            }
        }
        if (!ignoreContents) {
            try {
                return !fileUtils.contentEquals(srcfile, destfile);
            } catch (IOException e) {
                throw new BuildException("while comparing " + srcfile + " and "
                        + destfile, e);
            }
        } else {
            return false;
        }
    }
}
