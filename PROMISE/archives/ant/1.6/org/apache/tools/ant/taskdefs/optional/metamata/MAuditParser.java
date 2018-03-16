package org.apache.tools.ant.taskdefs.optional.metamata;

import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.util.regexp.RegexpMatcher;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;

/**
 * Parser that will parse an output line of MAudit and return an
 * interpreted violation if any.
 *
 * <p>
 * MAudit is supposed to be configured with -fullpath so that it can
 * correctly locate the file and attribute violation to the appropriate
 * file (there might be several classes with the same name in
 * different packages)
 * </p>
 *
 */
final class MAuditParser {

    /** pattern used by maudit to report the error for a file */
    /** RE does not seems to support regexp pattern with comments so i'm stripping it*/
    private static final String AUDIT_PATTERN = "(?:file:)?(.+):(\\d+)\\s*:\\s+(.*)";

    /** matcher that will be used to extract the info from the line */
    private final RegexpMatcher matcher;

    MAuditParser() {
        /** the matcher should be the Oro one. I don't know about the other one */
        matcher = (new RegexpMatcherFactory()).newRegexpMatcher();
        matcher.setPattern(AUDIT_PATTERN);
    }

    /**
     * Parse a line obtained from MAudit.
     * @param line a line obtained from the MAudit output.
     * @return the violation corresponding to the displayed line
     * or <tt>null</tt> if it could not parse it. (might be a
     * message info or copyright or summary).
     */
    Violation parseLine(String line) {
        Vector matches = matcher.getGroups(line);
        if (matches == null) {
            return null;
        }
        final String file = (String) matches.elementAt(1);
        Violation violation = new Violation();
        violation.file = file;
        violation.line = (String) matches.elementAt(2);
        violation.error = (String) matches.elementAt(3);
        final int pos = file.lastIndexOf(File.separatorChar);
        if ((pos != -1) && (pos != file.length() - 1)) {
            String filename = file.substring(pos + 1);
            violation.error = StringUtils.replace(violation.error,
                    "file:" + file, filename);
        }
        return violation;
    }

    /** the inner class used to report violation information */
    static final class Violation {
        String file;
        String line;
        String error;
    }
}
