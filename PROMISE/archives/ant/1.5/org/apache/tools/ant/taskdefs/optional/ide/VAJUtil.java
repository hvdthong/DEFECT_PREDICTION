package org.apache.tools.ant.taskdefs.optional.ide;

import java.util.Vector;
import java.io.File;

/**
 * Helper interface for VAJ tasks. Encapsulates
 * the interface to the VAJ tool API.
 *
 * @author Wolf Siberski, TUI Infotec GmbH
 */
interface VAJUtil {
    public static final int MSG_DEBUG = 4;
    public static final int MSG_ERR = 0;
    public static final int MSG_INFO = 2;
    public static final int MSG_VERBOSE = 3;
    public static final int MSG_WARN = 1;

    /**
     * export the array of Packages
     */
    void exportPackages(
        File dest,
        String[] includePatterns, String[] excludePatterns,
        boolean exportClasses, boolean exportDebugInfo,
        boolean exportResources, boolean exportSources,
        boolean useDefaultExcludes, boolean overwrite);

    /**
     * Do the import.
     */
    void importFiles(
        String importProject, File srcDir,
        String[] includePatterns, String[] excludePatterns,
        boolean importClasses, boolean importResources,
        boolean importSources, boolean useDefaultExcludes);

    /**
     * Load specified projects.
     */
    void loadProjects(Vector projectDescriptions);

    /**
     * Logs a message with the specified log level.
     */
    void log(String msg, int level);
}
