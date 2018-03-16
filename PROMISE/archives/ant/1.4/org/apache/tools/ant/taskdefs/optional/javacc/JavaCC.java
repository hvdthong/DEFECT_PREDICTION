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
 * Taskdef for the JavaCC compiler compiler.
 *
 * @author thomas.haas@softwired-inc.com
 * @author Michael Saunders <a href="mailto:michael@amtec.com">michael@amtec.com</a>
 */
public class JavaCC extends Task {

    private static final String LOOKAHEAD              = "LOOKAHEAD";
    private static final String CHOICE_AMBIGUITY_CHECK = "CHOICE_AMBIGUITY_CHECK";
    private static final String OTHER_AMBIGUITY_CHECK  = "OTHER_AMBIGUITY_CHECK";

    private static final String STATIC                 = "STATIC";
    private static final String DEBUG_PARSER           = "DEBUG_PARSER";
    private static final String DEBUG_LOOKAHEAD        = "DEBUG_LOOKAHEAD";
    private static final String DEBUG_TOKEN_MANAGER    = "DEBUG_TOKEN_MANAGER";
    private static final String OPTIMIZE_TOKEN_MANAGER = "OPTIMIZE_TOKEN_MANAGER";
    private static final String ERROR_REPORTING        = "ERROR_REPORTING";
    private static final String JAVA_UNICODE_ESCAPE    = "JAVA_UNICODE_ESCAPE";
    private static final String UNICODE_INPUT          = "UNICODE_INPUT";
    private static final String IGNORE_CASE            = "IGNORE_CASE";
    private static final String COMMON_TOKEN_ACTION    = "COMMON_TOKEN_ACTION";
    private static final String USER_TOKEN_MANAGER     = "USER_TOKEN_MANAGER";
    private static final String USER_CHAR_STREAM       = "USER_CHAR_STREAM";
    private static final String BUILD_PARSER           = "BUILD_PARSER";
    private static final String BUILD_TOKEN_MANAGER    = "BUILD_TOKEN_MANAGER";
    private static final String SANITY_CHECK           = "SANITY_CHECK";
    private static final String FORCE_LA_CHECK         = "FORCE_LA_CHECK";
    private static final String CACHE_TOKENS           = "CACHE_TOKENS";

    private final Hashtable optionalAttrs = new Hashtable();

    private File outputDirectory = null;
    private File target          = null;
    private File javaccHome      = null;

    private CommandlineJava cmdl = new CommandlineJava();


    public void setLookahead(int lookahead) {
        optionalAttrs.put(LOOKAHEAD, new Integer(lookahead));
    }

    public void setChoiceambiguitycheck(int choiceAmbiguityCheck) {
        optionalAttrs.put(CHOICE_AMBIGUITY_CHECK, new Integer(choiceAmbiguityCheck));
    }

    public void setOtherambiguityCheck(int otherAmbiguityCheck) {
        optionalAttrs.put(OTHER_AMBIGUITY_CHECK, new Integer(otherAmbiguityCheck));
    }

    public void setStatic(boolean staticParser) {
        optionalAttrs.put(STATIC, new Boolean(staticParser));
    }

    public void setDebugparser(boolean debugParser) {
        optionalAttrs.put(DEBUG_PARSER, new Boolean(debugParser));
    }

    public void setDebuglookahead(boolean debugLookahead) {
        optionalAttrs.put(DEBUG_LOOKAHEAD, new Boolean(debugLookahead));
    }

    public void setDebugtokenmanager(boolean debugTokenManager) {
        optionalAttrs.put(DEBUG_TOKEN_MANAGER, new Boolean(debugTokenManager));
    }

    public void setOptimizetokenmanager(boolean optimizeTokenManager) {
        optionalAttrs.put(OPTIMIZE_TOKEN_MANAGER, new Boolean(optimizeTokenManager));
    }

    public void setErrorreporting(boolean errorReporting) {
        optionalAttrs.put(ERROR_REPORTING, new Boolean(errorReporting));
    }

    public void setJavaunicodeescape(boolean javaUnicodeEscape) {
        optionalAttrs.put(JAVA_UNICODE_ESCAPE, new Boolean(javaUnicodeEscape));
    }

    public void setUnicodeinput(boolean unicodeInput) {
        optionalAttrs.put(UNICODE_INPUT, new Boolean(unicodeInput));
    }

    public void setIgnorecase(boolean ignoreCase) {
        optionalAttrs.put(IGNORE_CASE, new Boolean(ignoreCase));
    }

    public void setCommontokenaction(boolean commonTokenAction) {
        optionalAttrs.put(COMMON_TOKEN_ACTION, new Boolean(commonTokenAction));
    }

    public void setUsertokenmanager(boolean userTokenManager) {
        optionalAttrs.put(USER_TOKEN_MANAGER, new Boolean(userTokenManager));
    }

    public void setUsercharstream(boolean userCharStream) {
        optionalAttrs.put(USER_CHAR_STREAM, new Boolean(userCharStream));
    }

    public void setBuildparser(boolean buildParser) {
        optionalAttrs.put(BUILD_PARSER, new Boolean(buildParser));
    }

    public void setBuildtokenmanager(boolean buildTokenManager) {
        optionalAttrs.put(BUILD_TOKEN_MANAGER, new Boolean(buildTokenManager));
    }

    public void setSanitycheck(boolean sanityCheck) {
        optionalAttrs.put(SANITY_CHECK, new Boolean(sanityCheck));
    }

    public void setForcelacheck(boolean forceLACheck) {
        optionalAttrs.put(FORCE_LA_CHECK, new Boolean(forceLACheck));
    }

    public void setCachetokens(boolean cacheTokens) {
        optionalAttrs.put(CACHE_TOKENS, new Boolean(cacheTokens));
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

    public JavaCC() {
        cmdl.setVm("java");
        cmdl.setClassname("COM.sun.labs.javacc.Main");
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
        else if (!outputDirectory.isDirectory()) {
            throw new BuildException("Outputdir not a directory.");
        }
        cmdl.createArgument().setValue(
            "-OUTPUT_DIRECTORY:"+outputDirectory.getAbsolutePath());

        final File javaFile = getOutputJavaFile(outputDirectory, target);
        if (javaFile.exists() && target.lastModified() < javaFile.lastModified()) {
            log("Target is already built - skipping (" + target + ")", Project.MSG_VERBOSE);
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

        Execute.runCommand(this, cmdl.getCommandline());
    }

    /**
     * Determines the output Java file to be generated by the given grammar
     * file.
     * 
     */
    private File getOutputJavaFile(File outputdir, File srcfile)
    {
        String path = srcfile.getPath();

        int startBasename = path.lastIndexOf(File.separator);
        if ( startBasename != -1 ) {
            path = path.substring(startBasename+1);
        }

        int startExtn = path.lastIndexOf('.');
        if (startExtn != -1) {
            path = path.substring(0, startExtn) + ".java";
        }
        else {
            path += ".java";
        }

        if (outputdir != null) {
            path = outputdir + File.separator + path;
        }

        return new File(path);
    }
}
