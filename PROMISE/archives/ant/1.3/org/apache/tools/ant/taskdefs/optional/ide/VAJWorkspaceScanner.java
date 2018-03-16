package org.apache.tools.ant.taskdefs.optional.ide;

import com.ibm.ivj.util.base.IvjException;
import com.ibm.ivj.util.base.Package;
import com.ibm.ivj.util.base.Project;
import java.io.File;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.DirectoryScanner;

/**
 * Class for scanning a Visual Age for Java workspace for packages matching
 * a certain criteria.
 * <p>
 * These criteria consist of a set of include and exclude patterns. With these
 * patterns, you can select which packages you want to have included, and which
 * packages you want to have excluded.  You can add patterns to be excluded by
 * default with the addDefaultExcludes method.  The patters that are excluded
 * by default include
 * <ul>
 *   <li>IBM*\**</li>
 *   <li>Java class libraries\**</li>
 *   <li>Sun class libraries*\**</li>
 *   <li>JSP Page Compile Generated Code\**</li>
 *   <li>VisualAge*\**</li>
 * </ul>
 * <p>
 * This class works like DirectoryScanner. 
 *
 * @see org.apache.tools.ant.DirectoryScanner
 *
 * @author Wolf Siberski, TUI Infotec (based on Arnout J. Kuipers DirectoryScanner)
 */
public class VAJWorkspaceScanner extends DirectoryScanner {

    /**
     * Patterns that should be excluded by default.
     *
     * @see #addDefaultExcludes()
     */
    private final static String[] DEFAULTEXCLUDES = 
    {
        "IBM*/**", 
        "Java class libraries/**", 
        "Sun class libraries*/**", 
        "JSP Page Compile Generated Code/**", 
        "VisualAge*/**", 
    }; 

    /**
     * The packages that where found and matched at least one includes, and
     * matched no excludes.
     */
    private Vector packagesIncluded = new Vector();
   
    /**
     * Adds the array with default exclusions to the current exclusions set.
     */
    public void addDefaultExcludes() {
        int excludesLength = excludes == null ? 0 : excludes.length;
        String[] newExcludes;
        newExcludes = new String[excludesLength + DEFAULTEXCLUDES.length];
        if (excludesLength > 0) {
            System.arraycopy(excludes, 0, newExcludes, 0, excludesLength);
        }
        for (int i = 0; i < DEFAULTEXCLUDES.length; i++) {
            newExcludes[i + excludesLength] = DEFAULTEXCLUDES[i].
                replace( '/', File.separatorChar ).replace( '\\', File.separatorChar ); 
        }
        excludes = newExcludes;
    }
        
    /**
     * Finds all Projects specified in include patterns.
     *
     * @return the projects
     */
    public Vector findMatchingProjects() {
        Project[] projects = VAJUtil.getWorkspace().getProjects();

        Vector matchingProjects = new Vector();

        boolean allProjectsMatch = false;
        for (int i = 0; i < projects.length; i++) {
            Project project = projects[i];
            for (int j = 0; j < includes.length && !allProjectsMatch; j++) {
                StringTokenizer tok = new StringTokenizer(includes[j], File.separator);
                String projectNamePattern = tok.nextToken();
                if (projectNamePattern.equals("**")) {
                    allProjectsMatch = true;
                } else
                    if (match(projectNamePattern, project.getName())) {
                        matchingProjects.addElement(project);
                        break;
                    }
            }
        }

        if (allProjectsMatch) {
            matchingProjects = new Vector();
            for (int i = 0; i < projects.length; i++) {
                matchingProjects.addElement(projects[i]);
            }
        }

        return matchingProjects;
    }
        
    /**
     * Get the names of the packages that matched at least one of the include
     * patterns, and didn't match one of the exclude patterns.
     *
     * @return the matching packages
     */
    public Package[] getIncludedPackages() {
        int count = packagesIncluded.size();
        Package[] packages = new Package[count];
        for (int i = 0; i < count; i++) {
            packages[i] = (Package) packagesIncluded.elementAt(i);
        }
        return packages;
    }
        
    /**
     * Matches a string against a pattern. The pattern contains two special
     * characters:
     * '*' which means zero or more characters,
     * '?' which means one and only one character.
     *
     * @param pattern the (non-null) pattern to match against
     * @param str     the (non-null) string that must be matched against the
     *                pattern
     *
     * @return <code>true</code> when the string matches against the pattern,
     *         <code>false</code> otherwise.
     */
    protected static boolean match(String pattern, String str) {
        return DirectoryScanner.match(pattern, str);
    }
    /**
     * Scans the workspace for packages that match at least one include
     * pattern, and don't match any exclude patterns.
     *
     */
    public void scan() {
        if (includes == null) {
            includes = new String[1];
            includes[0] = "**";
        }
        if (excludes == null) {
            excludes = new String[0];
        }

        Vector matchingProjects = findMatchingProjects();
        for (Enumeration e = matchingProjects.elements(); e.hasMoreElements();) {
            Project project = (Project) e.nextElement();
            scanProject(project);
        }
    }
        
    /**
     * Scans a project for packages that match at least one include
     * pattern, and don't match any exclude patterns.
     *
     */
    public void scanProject(Project project) {
        try {
            Package[] packages = project.getPackages();
            if (packages != null) {
                for (int i = 0; i < packages.length; i++) {
                    Package item = packages[i];
                    String name = 
                        project.getName()
                        + File.separator
                        + item.getName().replace('.', File.separatorChar); 
                    if (isIncluded(name) && !isExcluded(name)) {
                        packagesIncluded.addElement(item);
                    }
                }
            }
        } catch (IvjException e) {
            throw VAJUtil.createBuildException("VA Exception occured: ", e);
        }
    }
}
