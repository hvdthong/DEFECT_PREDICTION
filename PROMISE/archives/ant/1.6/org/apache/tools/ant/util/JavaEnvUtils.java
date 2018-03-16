package org.apache.tools.ant.util;

import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.taskdefs.condition.Os;

/**
 * A set of helper methods related to locating executables or checking
 * conditons of a given Java installation.
 *
 * @since Ant 1.5
 */
public final class JavaEnvUtils {

    private JavaEnvUtils() {
    }

    /** Are we on a DOS-based system */
    private static final boolean isDos = Os.isFamily("dos");
    /** Are we on Novell NetWare */
    private static final boolean isNetware = Os.isName("netware");
    /** Are we on AIX */
    private static final boolean isAix = Os.isName("aix");

    /** shortcut for System.getProperty("java.home") */
    private static final String javaHome = System.getProperty("java.home");

    /** FileUtils instance for path normalization */
    private static final FileUtils fileUtils = FileUtils.newFileUtils();

    /** Version of currently running VM. */
    private static String javaVersion;

    /** floating version of the JVM */
    private static int javaVersionNumber;

    /** Version constant for Java 1.0 */
    public static final String JAVA_1_0 = "1.0";
    /** Version constant for Java 1.1 */
    public static final String JAVA_1_1 = "1.1";
    /** Version constant for Java 1.2 */
    public static final String JAVA_1_2 = "1.2";
    /** Version constant for Java 1.3 */
    public static final String JAVA_1_3 = "1.3";
    /** Version constant for Java 1.4 */
    public static final String JAVA_1_4 = "1.4";
    /** Version constant for Java 1.5 */
    public static final String JAVA_1_5 = "1.5";

    /** Whether this is the Kaffe VM */
    private static boolean kaffeDetected;

    /** array of packages in the runtime */
    private static Vector jrePackages;


    static {


        try {
            javaVersion = JAVA_1_0;
            javaVersionNumber = 10;
            Class.forName("java.lang.Void");
            javaVersion = JAVA_1_1;
            javaVersionNumber++;
            Class.forName("java.lang.ThreadLocal");
            javaVersion = JAVA_1_2;
            javaVersionNumber++;
            Class.forName("java.lang.StrictMath");
            javaVersion = JAVA_1_3;
            javaVersionNumber++;
            Class.forName("java.lang.CharSequence");
            javaVersion = JAVA_1_4;
            javaVersionNumber++;
            Class.forName("java.lang.Readable");
            javaVersion = JAVA_1_5;
            javaVersionNumber++;
        } catch (Throwable t) {
        }
        kaffeDetected = false;
        try {
            Class.forName("kaffe.util.NotImplemented");
            kaffeDetected = true;
        } catch (Throwable t) {
        }
    }

    /**
     * Returns the version of Java this class is running under.
     * @return the version of Java as a String, e.g. "1.1"
     */
    public static String getJavaVersion() {
        return javaVersion;
    }

    /**
     * Compares the current Java version to the passed in String -
     * assumes the argument is one of the constants defined in this
     * class.
     * @param version the version to check against the current version.
     * @return true if the version of Java is the same as the given version.
     * @since Ant 1.5
     */
    public static boolean isJavaVersion(String version) {
        return javaVersion.equals(version);
    }

    /**
     * Checks whether the current Java VM is Kaffe.
     * @return true if the current Java VM is Kaffe.
     * @since Ant 1.6.3
     */
    public static boolean isKaffe() {
        return kaffeDetected;
    }

    /**
     * Finds an executable that is part of a JRE installation based on
     * the java.home system property.
     *
     * <p><code>java</code>, <code>keytool</code>,
     * <code>policytool</code>, <code>orbd</code>, <code>rmid</code>,
     * <code>rmiregistry</code>, <code>servertool</code> and
     * <code>tnameserv</code> are JRE executables on Sun based
     * JRE's.</p>
     *
     * <p>You typically find them in <code>JAVA_HOME/jre/bin</code> if
     * <code>JAVA_HOME</code> points to your JDK installation.  JDK
     * &lt; 1.2 has them in the same directory as the JDK
     * executables.</p>
     * @param command the java executable to find.
     * @return the path to the command.
     * @since Ant 1.5
     */
    public static String getJreExecutable(String command) {
        if (isNetware) {
            return command;
        }

        File jExecutable = null;

        if (isAix) {
            jExecutable = findInDir(javaHome + "/sh", command);
        }

        if (jExecutable == null) {
            jExecutable = findInDir(javaHome + "/bin", command);
        }

        if (jExecutable != null) {
            return jExecutable.getAbsolutePath();
        } else {
            return addExtension(command);
        }
    }

    /**
     * Finds an executable that is part of a JDK installation based on
     * the java.home system property.
     *
     * <p>You typically find them in <code>JAVA_HOME/bin</code> if
     * <code>JAVA_HOME</code> points to your JDK installation.</p>
     * @param command the java executable to find.
     * @return the path to the command.
     * @since Ant 1.5
     */
    public static String getJdkExecutable(String command) {
        if (isNetware) {
            return command;
        }

        File jExecutable = null;

        if (isAix) {
            jExecutable = findInDir(javaHome + "/../sh", command);
        }

        if (jExecutable == null) {
            jExecutable = findInDir(javaHome + "/../bin", command);
        }

        if (jExecutable != null) {
            return jExecutable.getAbsolutePath();
        } else {
            return getJreExecutable(command);
        }
    }

    /**
     * Adds a system specific extension to the name of an executable.
     *
     * @since Ant 1.5
     */
    private static String addExtension(String command) {
        return command + (isDos ? ".exe" : "");
    }

    /**
     * Look for an executable in a given directory.
     *
     * @return null if the executable cannot be found.
     */
    private static File findInDir(String dirName, String commandName) {
        File dir = fileUtils.normalize(dirName);
        File executable = null;
        if (dir.exists()) {
            executable = new File(dir, addExtension(commandName));
            if (!executable.exists()) {
                executable = null;
            }
        }
        return executable;
    }

    /**
     * demand creation of the package list.
     * When you add a new package, add a new test below.
     */

    private static void buildJrePackages() {
        jrePackages = new Vector();
        switch(javaVersionNumber) {
            case 15:
            case 14:
                jrePackages.addElement("org.apache.crimson");
                jrePackages.addElement("org.apache.xalan");
                jrePackages.addElement("org.apache.xml");
                jrePackages.addElement("org.apache.xpath");
                jrePackages.addElement("org.ietf.jgss");
                jrePackages.addElement("org.w3c.dom");
                jrePackages.addElement("org.xml.sax");
            case 13:
                jrePackages.addElement("org.omg");
                jrePackages.addElement("com.sun.corba");
                jrePackages.addElement("com.sun.jndi");
                jrePackages.addElement("com.sun.media");
                jrePackages.addElement("com.sun.naming");
                jrePackages.addElement("com.sun.org.omg");
                jrePackages.addElement("com.sun.rmi");
                jrePackages.addElement("sunw.io");
                jrePackages.addElement("sunw.util");
            case 12:
                jrePackages.addElement("com.sun.java");
                jrePackages.addElement("com.sun.image");
            case 11:
            default:
                jrePackages.addElement("sun");
                jrePackages.addElement("java");
                jrePackages.addElement("javax");
                break;
        }
    }

    /**
     * Testing helper method; kept here for unification of changes.
     * @return a list of test classes depending on the java version.
     */
    public static Vector getJrePackageTestCases() {
        Vector tests = new Vector();
        tests.addElement("java.lang.Object");
        switch(javaVersionNumber) {
            case 15:
            case 14:
                tests.addElement("sun.audio.AudioPlayer");
                tests.addElement("org.apache.crimson.parser.ContentModel");
                tests.addElement("org.apache.xalan.processor.ProcessorImport");
                tests.addElement("org.apache.xml.utils.URI");
                tests.addElement("org.apache.xpath.XPathFactory");
                tests.addElement("org.ietf.jgss.Oid");
                tests.addElement("org.w3c.dom.Attr");
                tests.addElement("org.xml.sax.XMLReader");
            case 13:
                tests.addElement("org.omg.CORBA.Any");
                tests.addElement("com.sun.corba.se.internal.corba.AnyImpl");
                tests.addElement("com.sun.jndi.ldap.LdapURL");
                tests.addElement("com.sun.media.sound.Printer");
                tests.addElement("com.sun.naming.internal.VersionHelper");
                tests.addElement("com.sun.org.omg.CORBA.Initializer");
                tests.addElement("sunw.io.Serializable");
                tests.addElement("sunw.util.EventListener");
            case 12:
                tests.addElement("javax.accessibility.Accessible");
                tests.addElement("sun.misc.BASE64Encoder");
                tests.addElement("com.sun.image.codec.jpeg.JPEGCodec");
            case 11:
            default:
                tests.addElement("sun.reflect.SerializationConstructorAccessorImpl");
                tests.addElement("sun.net.www.http.HttpClient");
                tests.addElement("sun.audio.AudioPlayer");
                break;
        }
        return tests;
    }
    /**
     * get a vector of strings of packages built into
     * that platforms runtime jar(s)
     * @return list of packages.
     */
    public static Vector getJrePackages() {
        if (jrePackages == null) {
            buildJrePackages();
        }
        return jrePackages;
    }
}
