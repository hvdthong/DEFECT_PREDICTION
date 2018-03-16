package org.apache.tools.ant.taskdefs.optional.javacc;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Taskdef for the JJTree compiler compiler.
 *
 * @author thomas.haas@softwired-inc.com
 * @author Michael Saunders <a href="mailto:michael@amtec.com">michael@amtec.com</a>
 */
public class JJTree extends Task {

    private static final String BUILD_NODE_FILES  = "BUILD_NODE_FILES";
    private static final String MULTI             = "MULTI";
    private static final String NODE_DEFAULT_VOID = "NODE_DEFAULT_VOID";
    private static final String NODE_FACTORY      = "NODE_FACTORY";
    private static final String NODE_SCOPE_HOOK   = "NODE_SCOPE_HOOK";
    private static final String NODE_USES_PARSER  = "NODE_USES_PARSER";
    private static final String STATIC            = "STATIC";
    private static final String VISITOR           = "VISITOR";

    private static final String NODE_PACKAGE      = "NODE_PACKAGE";
    private static final String VISITOR_EXCEPTION = "VISITOR_EXCEPTION";
    private static final String NODE_PREFIX       = "NODE_PREFIX";

    private final Hashtable optionalAttrs = new Hashtable();

    private File outputDirectory = null;
    private File target          = null;
    private File javaccHome      = null;

    private CommandlineJava cmdl = new CommandlineJava();


    public void setBuildnodefiles(boolean buildNodeFiles) {
        optionalAttrs.put(BUILD_NODE_FILES, new Boolean(buildNodeFiles));
    }

    public void setMulti(boolean multi) {
        optionalAttrs.put(MULTI, new Boolean(multi));
    }

    public void setNodedefaultvoid(boolean nodeDefaultVoid) {
        optionalAttrs.put(NODE_DEFAULT_VOID, new Boolean(nodeDefaultVoid));
    }

    public void setNodefactory(boolean nodeFactory) {
        optionalAttrs.put(NODE_FACTORY, new Boolean(nodeFactory));
    }

    public void setNodescopehook(boolean nodeScopeHook) {
        optionalAttrs.put(NODE_SCOPE_HOOK, new Boolean(nodeScopeHook));
    }

    public void setNodeusesparser(boolean nodeUsesParser) {
        optionalAttrs.put(NODE_USES_PARSER, new Boolean(nodeUsesParser));
    }

    public void setStatic(boolean staticParser) {
        optionalAttrs.put(STATIC, new Boolean(staticParser));
    }

    public void setVisitor(boolean visitor) {
        optionalAttrs.put(VISITOR, new Boolean(visitor));
    }

    public void setNodepackage(String nodePackage) {
        optionalAttrs.put(NODE_PACKAGE, new String(nodePackage));
    }

    public void setVisitorException(String visitorException) {
        optionalAttrs.put(VISITOR_EXCEPTION, new String(visitorException));
    }

    public void setNodeprefix(String nodePrefix) {
        optionalAttrs.put(NODE_PREFIX, new String(nodePrefix));
    }

    public void setOutputdirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setTarget(File target) {
        this.target = target;
    }

    public void setJavacchome(File javaccHome) {
        this.javaccHome = javaccHome;
    }

    public JJTree() {
        cmdl.setVm("java");
        cmdl.setClassname("COM.sun.labs.jjtree.Main");
    }

    public void execute() throws BuildException {

        Enumeration iter = optionalAttrs.keys();
        while (iter.hasMoreElements()) {
            String name  = (String)iter.nextElement();
            Object value = optionalAttrs.get(name);
            cmdl.createArgument().setValue("-"+name+":"+value.toString());
        }

        if (target == null || !target.isFile()) {
            throw new BuildException("Invalid target: " + target);
        }
        
        if (outputDirectory == null) {
            outputDirectory = new File(target.getParent());
        }        
        if (!outputDirectory.isDirectory() ) {
            throw new BuildException("'outputdirectory' " + outputDirectory + " is not a directory.");
        }
        cmdl.createArgument().setValue(
            "-OUTPUT_DIRECTORY:"+outputDirectory.getAbsolutePath().replace('\\', '/'));
        
        String targetName = target.getName();
        final File javaFile = new File(outputDirectory,
            targetName.substring(0, targetName.indexOf(".jjt")) + ".jj");
        if (javaFile.exists() && target.lastModified() < javaFile.lastModified()) {
            project.log("Target is already built - skipping (" + target + ")");
            return;
        }
        cmdl.createArgument().setValue(target.getAbsolutePath());

        if (javaccHome == null || !javaccHome.isDirectory()) {
            throw new BuildException("Javacchome not set.");
        }
        final Path classpath = cmdl.createClasspath(project);
        classpath.createPathElement().setPath(javaccHome.getAbsolutePath() +
                                                  "/JavaCC.zip");

        final Commandline.Argument arg = cmdl.createVmArgument();
        arg.setValue("-mx140M");
        arg.setValue("-Dinstall.root="+javaccHome.getAbsolutePath());

        final Execute process =
            new Execute(new LogStreamHandler(this,
                                             Project.MSG_INFO,
                                             Project.MSG_INFO),
                        null);
        log(cmdl.toString(), Project.MSG_VERBOSE);
        process.setCommandline(cmdl.getCommandline());

        try {
            if (process.execute() != 0) {
                throw new BuildException("JJTree failed.");
            }
        }
        catch (IOException e) {
            throw new BuildException("Failed to launch JJTree: " + e);
        }
    }
}
