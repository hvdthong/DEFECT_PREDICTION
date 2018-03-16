package org.apache.tools.ant.taskdefs;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.*;

/**
 * This task makes it easy to generate Javadoc documentation for a collection
 * of source code.
 *
 * <P>Current known limitations are:
 *
 * <P><UL>
 *    <LI>patterns must be of the form "xxx.*", every other pattern doesn't
 *        work.
 *    <LI>the java comment-stripper reader is horribly slow
 *    <LI>there is no control on arguments sanity since they are left
 *        to the javadoc implementation.
 *    <LI>argument J in javadoc1 is not supported (what is that for anyway?)
 * </UL>
 *
 * <P>If no <CODE>doclet</CODE> is set, then the <CODE>version</CODE> and
 * <CODE>author</CODE> are by default <CODE>"yes"</CODE>.
 *
 * <P>Note: This task is run on another VM because the Javadoc code calls
 * <CODE>System.exit()</CODE> which would break Ant functionality.
 *
 * @author Jon S. Stevens <a href="mailto:jon@clearink.com">jon@clearink.com</a>
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author Patrick Chanezon <a href="mailto:chanezon@netscape.com">chanezon@netscape.com</a>
 * @author Ernst de Haan <a href="mailto:ernst@jollem.com">ernst@jollem.com</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */

public class Javadoc extends Task {

    public class DocletParam {
        private String name;
        private String value;
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public class DocletInfo {
        private String name;
        private Path path;
        
        private Vector params = new Vector();

        public void setName(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void setPath(Path path) {
            if (this.path == null) {
                this.path = path;
            } else {
                this.path.append(path);
            }
        }

        public Path getPath() {
            return path;
        }
        
        public Path createPath() {
            if (path == null) {
                path = new Path(getProject());
            }
            return path.createPath();
        }

        /**
         * Adds a reference to a CLASSPATH defined elsewhere.
         */
        public void setPathRef(Reference r) {
            createPath().setRefid(r);
        }

        public DocletParam createParam() {
            DocletParam param = new DocletParam();
            params.addElement(param);
            
            return param;
        }
        
        public Enumeration getParams() {
            return params.elements();
        }
    }

    private Commandline cmd = new Commandline();
    private static boolean javadoc1 = 
        (Project.getJavaVersion() == Project.JAVA_1_1);


    private void addArgIf(boolean b, String arg) {
        if (b) {
            cmd.createArgument().setValue(arg);
        }
    }

    private void add12ArgIfNotEmpty(String key, String value) {
        if (!javadoc1) {
            if (value != null && value.length() != 0) {
                cmd.createArgument().setValue(key);
                cmd.createArgument().setValue(value);
            } else {
                project.log(this, 
                            "Warning: Leaving out empty argument '" + key + "'", 
                            Project.MSG_WARN);
            }
        } 
    }

    private void add11ArgIf(boolean b, String arg) {
        if (javadoc1 && b) {
            cmd.createArgument().setValue(arg);
        }
    }

    private void add12ArgIf(boolean b, String arg) {
        if (!javadoc1 && b) {
            cmd.createArgument().setValue(arg);
        }
    }

    private boolean foundJavaFile = false;
    private boolean failOnError = false;
    private Path sourcePath = null;
    private File destDir = null;
    private String sourceFiles = null;
    private String packageNames = null;
	private String excludePackageNames = null;
    private boolean author = true;
    private boolean version = true;
    private DocletInfo doclet = null;
    private Path classpath = null;
    private Path bootclasspath = null;
    private String group = null;
    private Vector compileList = new Vector(10);
    private String packageList = null;
    private Vector links = new Vector(2);
    private Vector groups = new Vector(2);
	private boolean useDefaultExcludes = true;

    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions 
     *                           should be used, "false"|"off"|"no" when they
     *                           shouldn't be used.
     */
    public void setDefaultexcludes(boolean useDefaultExcludes) {
       this.useDefaultExcludes = useDefaultExcludes;
    }

    public void setMaxmemory(String max){
        if(javadoc1){
            cmd.createArgument().setValue("-J-mx" + max);
        } else{
            cmd.createArgument().setValue("-J-Xmx" + max);
        }
    }

    public void setAdditionalparam(String add){
        cmd.createArgument().setLine(add);
    }
    
    public void setSourcepath(Path src) {
        if (sourcePath == null) {
            sourcePath = src;
        } else {
            sourcePath.append(src);
        }
    }
    public Path createSourcepath() {
        if (sourcePath == null) {
            sourcePath = new Path(project);
        }
        return sourcePath.createPath();
    }

    /**
     * Adds a reference to a CLASSPATH defined elsewhere.
     */
    public void setSourcepathRef(Reference r) {
        createSourcepath().setRefid(r);
    }

    public void setDestdir(File dir) {
        destDir = dir;
        cmd.createArgument().setValue("-d");
        cmd.createArgument().setFile(destDir);
    }
    public void setSourcefiles(String src) {
        sourceFiles = src;
    }
    public void setPackagenames(String src) {
        packageNames = src;
    }

	public void setExcludePackageNames(String src) {
		excludePackageNames = src;
	}

    public void setOverview(File f) {
        if (!javadoc1) {
            cmd.createArgument().setValue("-overview");
            cmd.createArgument().setFile(f);
        }
    }
    public void setPublic(boolean b) {
        addArgIf(b, "-public");
    }
    public void setProtected(boolean b) {
        addArgIf(b, "-protected");
    }
    public void setPackage(boolean b) {
        addArgIf(b, "-package");
    }
    public void setPrivate(boolean b) {
        addArgIf(b, "-private");
    }
    public void setDoclet(String src) {
        if (doclet == null) {
            doclet = new DocletInfo();
        }
        doclet.setName(src);
    }
    
    public void setDocletPath(Path src) {
        if (doclet == null) {
            doclet = new DocletInfo();
        }
        doclet.setPath(src);
    }

    public void setDocletPathRef(Reference r) {
        if (doclet == null) {
            doclet = new DocletInfo();
        }
        doclet.createPath().setRefid(r);
    }

    public DocletInfo createDoclet() {
        doclet = new DocletInfo();
        return doclet;
    }

    public void setOld(boolean b) {
        add12ArgIf(b, "-1.1");
    }
    public void setClasspath(Path src) {
        if (classpath == null) {
            classpath = src;
        } else {
            classpath.append(src);
        }
    }
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(project);
        }
        return classpath.createPath();
    }

    /**
     * Adds a reference to a CLASSPATH defined elsewhere.
     */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setBootclasspath(Path src) {
        if (bootclasspath == null) {
            bootclasspath = src;
        } else {
            bootclasspath.append(src);
        }
    }
    public Path createBootclasspath() {
        if (bootclasspath == null) {
            bootclasspath = new Path(project);
        }
        return bootclasspath.createPath();
    }

    /**
     * Adds a reference to a CLASSPATH defined elsewhere.
     */
    public void setBootClasspathRef(Reference r) {
        createBootclasspath().setRefid(r);
    }

    public void setExtdirs(String src) {
        if (!javadoc1) {
            cmd.createArgument().setValue("-extdirs");
            cmd.createArgument().setValue(src);
        }
    }
    public void setVerbose(boolean b) {
        add12ArgIf(b, "-verbose");
    }
    public void setLocale(String src) {
        if (!javadoc1) {
            cmd.createArgument().setValue("-locale");
            cmd.createArgument().setValue(src);
        }
    }
    public void setEncoding(String enc) {
        cmd.createArgument().setValue("-encoding");
        cmd.createArgument().setValue(enc);
    }
    public void setVersion(boolean src) {
        version = src;
    }
    public void setUse(boolean b) {
        add12ArgIf(b, "-use");
    }
    public void setAuthor(boolean src) {
        author = src;
    }
    public void setSplitindex(boolean b) {
        add12ArgIf(b, "-splitindex");
    }
    public void setWindowtitle(String src) {
        add12ArgIfNotEmpty("-windowtitle", src);
    }
    public void setDoctitle(String src) {
        add12ArgIfNotEmpty("-doctitle", src);
    }
    public void setHeader(String src) {
        add12ArgIfNotEmpty("-header", src);
    }

    public void setFooter(String src) {
        add12ArgIfNotEmpty("-footer", src);
    }

    public void setBottom(String src) {
        add12ArgIfNotEmpty("-bottom", src);
    }

    public void setLinkoffline(String src) {
        if (!javadoc1) {
            LinkArgument le = createLink();
            le.setOffline(true);
            String linkOfflineError = "The linkoffline attribute must include a URL and " + 
                "a package-list file location separated by a space";
            if (src.trim().length() == 0) {
                throw new BuildException(linkOfflineError);
            }                
            StringTokenizer tok = new StringTokenizer(src, " ", false);
            le.setHref(tok.nextToken());

            if (!tok.hasMoreTokens()) {
                throw new BuildException(linkOfflineError);
            }                                        
            le.setPackagelistLoc(tok.nextToken());
        }
    }
    public void setGroup(String src) {
        group = src;
    }
    public void setLink(String src) {
        if (!javadoc1) {
            createLink().setHref(src);
        }
    }
    public void setNodeprecated(boolean b) {
        addArgIf(b, "-nodeprecated");
    }
    public void setNodeprecatedlist(boolean b) {
        add12ArgIf(b, "-nodeprecatedlist");
    }
    public void setNotree(boolean b) {
        addArgIf(b, "-notree");
    }
    public void setNoindex(boolean b) {
        addArgIf(b, "-noindex");
    }
    public void setNohelp(boolean b) {
        add12ArgIf(b, "-nohelp");
    }
    public void setNonavbar(boolean b) {
        add12ArgIf(b, "-nonavbar");
    }
    public void setSerialwarn(boolean b) {
        add12ArgIf(b, "-serialwarn");
    }
    public void setStylesheetfile(File f) {
        if (!javadoc1) {
            cmd.createArgument().setValue("-stylesheetfile");
            cmd.createArgument().setFile(f);
        }
    }
    public void setHelpfile(File f) {
        if (!javadoc1) {
            cmd.createArgument().setValue("-helpfile");
            cmd.createArgument().setFile(f);
        }
    }
    public void setDocencoding(String enc) {
        cmd.createArgument().setValue("-docencoding");
        cmd.createArgument().setValue(enc);
    }
    public void setPackageList(String src) {
        packageList = src;
    }
    
    public LinkArgument createLink() {
        LinkArgument la = new LinkArgument();
        links.addElement(la);
        return la;
    }
    
    public class LinkArgument {
        private String href;
        private boolean offline = false;
        private String packagelistLoc;
        
        public LinkArgument() {
        }

        public void setHref(String hr) {
            href = hr;
        }
        
        public String getHref() {
            return href;
        }
        
        public void setPackagelistLoc(String src) {
            packagelistLoc = src;
        }
        
        public String getPackagelistLoc() {
            return packagelistLoc;
        }
        
        public void setOffline(boolean offline) {
            this.offline = offline;
        }
        
        public boolean isLinkOffline() {
            return offline;
        }
    }
    
    public GroupArgument createGroup() {
        GroupArgument ga = new GroupArgument();
        groups.addElement(ga);
        return ga;
    }

    public class GroupArgument {
        private String title;
        private String packages;

        public GroupArgument() {
        }

        public void setTitle(String src) {
            title = src;
        }

        public String getTitle() {
            return title;
        }

        public void setPackages(String src) {
            packages = src;
        }

        public String getPackages() {
            return packages;
        }
    }
    
    public void setCharset(String src) {
        this.add12ArgIfNotEmpty("-charset", src);
    }

    /**
     * Should the build process fail if javadoc fails (as indicated by
     * a non zero return code)?
     *
     * <p>Default is false.</p>
     */
    public void setFailonerror(boolean b) {
        failOnError = b;
    }

    public void execute() throws BuildException {
        if ("javadoc2".equals(taskType)) {
            log("!! javadoc2 is deprecated. Use javadoc instead. !!");
        }

        if (sourcePath == null) {
            String msg = "sourcePath attribute must be set!";
            throw new BuildException(msg);
        }

        log("Generating Javadoc", Project.MSG_INFO);

        Commandline toExecute = (Commandline)cmd.clone();
        toExecute.setExecutable("javadoc");

        if (classpath == null)
            classpath = Path.systemClasspath;
        else
            classpath = classpath.concatSystemClasspath("ignore");

        if (!javadoc1) {
            toExecute.createArgument().setValue("-classpath");
            toExecute.createArgument().setPath(classpath);
            toExecute.createArgument().setValue("-sourcepath");
            toExecute.createArgument().setPath(sourcePath);
        } else {
            toExecute.createArgument().setValue("-classpath");
            toExecute.createArgument().setValue(sourcePath.toString() +
                                                System.getProperty("path.separator") + classpath.toString());
        }

        if (version && doclet == null)
            toExecute.createArgument().setValue("-version");
        if (author && doclet == null)
            toExecute.createArgument().setValue("-author");

        if (javadoc1 || doclet == null) {
            if (destDir == null) {
                String msg = "destDir attribute must be set!";
                throw new BuildException(msg);
            }
        }
        



        if (!javadoc1) {
            if (doclet != null) {
                if (doclet.getName() == null) {
                    throw new BuildException("The doclet name must be specified.", location);
                }
                else {                
                    toExecute.createArgument().setValue("-doclet");
                    toExecute.createArgument().setValue(doclet.getName());
                    if (doclet.getPath() != null) {
                        toExecute.createArgument().setValue("-docletpath");
                        toExecute.createArgument().setPath(doclet.getPath());
                    }
                    for (Enumeration e = doclet.getParams(); e.hasMoreElements();) {
                        DocletParam param = (DocletParam)e.nextElement();
                        if (param.getName() == null) {
                            throw new BuildException("Doclet parameters must have a name");
                        }
                        
                        toExecute.createArgument().setValue(param.getName());
                        if (param.getValue() != null) {
                            toExecute.createArgument().setValue(param.getValue());
                        }
                    }                        
                }
            } 
            if (bootclasspath != null) {
                toExecute.createArgument().setValue("-bootclasspath");
                toExecute.createArgument().setPath(bootclasspath);
            }
            
            if (links.size() != 0) {
                for (Enumeration e = links.elements(); e.hasMoreElements(); ) {
                    LinkArgument la = (LinkArgument)e.nextElement();
                
                    if (la.getHref() == null) {
                        throw new BuildException("Links must provide the URL to the external class documentation.");
                    }
                
                    if (la.isLinkOffline()) {
                        String packageListLocation = la.getPackagelistLoc();
                        if (packageListLocation == null) {
                            throw new BuildException("The package list location for link " + la.getHref() +
                                                     " must be provided because the link is offline");
                        }
                        toExecute.createArgument().setValue("-linkoffline");
                        toExecute.createArgument().setValue(la.getHref());
                        toExecute.createArgument().setValue(packageListLocation);
                    }
                    else {
                        toExecute.createArgument().setValue("-link");
                        toExecute.createArgument().setValue(la.getHref());
                    }
                }
            }                                   
                                                

            if (group != null) {
                StringTokenizer tok = new StringTokenizer(group, ",", false);
                while (tok.hasMoreTokens()) {
                    String grp = tok.nextToken().trim();
                    int space = grp.indexOf(" ");
                    if (space > 0){
                        String name = grp.substring(0, space);
                        String pkgList = grp.substring(space + 1);
                        toExecute.createArgument().setValue("-group");
                        toExecute.createArgument().setValue(name);
                        toExecute.createArgument().setValue(pkgList);
                    }
                }
            }
            
            if (groups.size() != 0) {
                for (Enumeration e = groups.elements(); e.hasMoreElements(); ) {
                    GroupArgument ga = (GroupArgument)e.nextElement();
                    String title = ga.getTitle();
                    String packages = ga.getPackages();
                    if (title == null || packages == null) {
                        throw new BuildException("The title and packages must be specified for group elements.");
                    }
                    toExecute.createArgument().setValue("-group");
                    toExecute.createArgument().setValue(title);
                    toExecute.createArgument().setValue(packages);
                }
            }

        }

        if ((packageNames != null) && (packageNames.length() > 0)) {
            Vector packages = new Vector();
            StringTokenizer tok = new StringTokenizer(packageNames, ",", false);
            while (tok.hasMoreTokens()) {
                String name = tok.nextToken().trim();
                if (name.endsWith(".*")) {
                    packages.addElement(name);
                } else {
                    toExecute.createArgument().setValue(name);
                }
            }

			Vector excludePackages = new Vector();
			if ((excludePackageNames != null) && (excludePackageNames.length() > 0)) {
				StringTokenizer exTok = new StringTokenizer(excludePackageNames, ",", false);
				while (exTok.hasMoreTokens()) {
					excludePackages.addElement(exTok.nextToken().trim());
				}
			}
            if (packages.size() > 0) {
                evaluatePackages(toExecute, sourcePath, packages, excludePackages);
            }
        }

        if ((sourceFiles != null) && (sourceFiles.length() > 0)) {
            StringTokenizer tok = new StringTokenizer(sourceFiles, ",", false);
            while (tok.hasMoreTokens()) {
                toExecute.createArgument().setValue(tok.nextToken().trim());
            }
        }

        if (packageList != null) {
            toExecute.createArgument().setValue("@" + packageList);
        }
        log("Javadoc args: " + toExecute, Project.MSG_VERBOSE);

        log("Javadoc execution", Project.MSG_INFO);

        JavadocOutputStream out = new JavadocOutputStream(Project.MSG_INFO);
        JavadocOutputStream err = new JavadocOutputStream(Project.MSG_WARN);
        Execute exe = new Execute(new PumpStreamHandler(out, err));
        exe.setAntRun(project);
        exe.setWorkingDirectory(project.getBaseDir());
        try {
            exe.setCommandline(toExecute.getCommandline());
            int ret = exe.execute();
            if (ret != 0 && failOnError) {
                throw new BuildException("Javadoc returned "+ret, location);
            }
        } catch (IOException e) {
            throw new BuildException("Javadoc failed: " + e, e, location);
        } finally {
            out.logFlush();
            err.logFlush();
            try {
                out.close();
                err.close();
            } catch (IOException e) {}
        }
    }

    /**
     * Given a source path, a list of package patterns, fill the given list
     * with the packages found in that path subdirs matching one of the given
     * patterns.
     */
    private void evaluatePackages(Commandline toExecute, Path sourcePath, 
                                  Vector packages, Vector excludePackages) {
        log("Source path = " + sourcePath.toString(), Project.MSG_VERBOSE);
        log("Packages = " + packages, Project.MSG_VERBOSE);
		log("Exclude Packages = " + excludePackages, Project.MSG_VERBOSE);

        Vector addedPackages = new Vector();

        String[] list = sourcePath.list();
        if (list == null) list = new String[0];

        FileSet fs = new FileSet();
        fs.setDefaultexcludes(useDefaultExcludes);

        Enumeration e = packages.elements();
        while (e.hasMoreElements()) {
            String pkg = (String)e.nextElement();
            pkg = pkg.replace('.','/');
            if (pkg.endsWith("*")) {
                pkg += "*";
            }

            fs.createInclude().setName(pkg);

		e = excludePackages.elements();
		while (e.hasMoreElements()) {
			String pkg = (String)e.nextElement();
			pkg = pkg.replace('.','/');
			if (pkg.endsWith("*")) {
				pkg += "*";
			}

			fs.createExclude().setName(pkg);
		}

        for (int j=0; j<list.length; j++) {
            File source = project.resolveFile(list[j]);
            fs.setDir(source);

            DirectoryScanner ds = fs.getDirectoryScanner(project);
            String[] packageDirs = ds.getIncludedDirectories();

            for (int i=0; i<packageDirs.length; i++) {
                File pd = new File(source, packageDirs[i]);
                String[] files = pd.list(new FilenameFilter () {
                    public boolean accept(File dir1, String name) {
                        if (name.endsWith(".java")) {
                            return true;
                        }
                    }
                });

                if (files.length > 0) {
                    String pkgDir = packageDirs[i].replace('/','.').replace('\\','.');
                    if (!addedPackages.contains(pkgDir)) {
                        toExecute.createArgument().setValue(pkgDir);
                        addedPackages.addElement(pkgDir);
                    }
                }
            }
        }
    }

    private class JavadocOutputStream extends LogOutputStream {
        JavadocOutputStream(int level) {
            super(Javadoc.this, level);
        }

        private String queuedLine = null;
        protected void processLine(String line, int messageLevel) {
            if (messageLevel == Project.MSG_INFO && line.startsWith("Generating ")) {
                if (queuedLine != null) {
                    super.processLine(queuedLine, Project.MSG_VERBOSE);
                }
                queuedLine = line;
            } else {
                if (queuedLine != null) {
                    if (line.startsWith("Building "))
                        super.processLine(queuedLine, Project.MSG_VERBOSE);
                    else
                        super.processLine(queuedLine, Project.MSG_INFO);
                    queuedLine = null;
                }
                super.processLine(line, messageLevel);
            }
        }

        
        protected void logFlush() {
            if (queuedLine != null) {
                super.processLine(queuedLine, Project.MSG_VERBOSE);
                queuedLine = null;
            }
        }
    }

}
