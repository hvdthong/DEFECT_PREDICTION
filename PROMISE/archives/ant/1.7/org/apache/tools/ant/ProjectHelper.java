package org.apache.tools.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import org.xml.sax.AttributeList;

import org.apache.tools.ant.helper.ProjectHelper2;
import org.apache.tools.ant.util.LoaderUtils;

/**
 * Configures a Project (complete with Targets and Tasks) based on
 * a XML build file. It'll rely on a plugin to do the actual processing
 * of the xml file.
 *
 * This class also provide static wrappers for common introspection.
 *
 * All helper plugins must provide backward compatibility with the
 * original ant patterns, unless a different behavior is explicitly
 * specified. For example, if namespace is used on the &lt;project&gt; tag
 * the helper can expect the entire build file to be namespace-enabled.
 * Namespaces or helper-specific tags can provide meta-information to
 * the helper, allowing it to use new ( or different policies ).
 *
 * However, if no namespace is used the behavior should be exactly
 * identical with the default helper.
 *
 */
public class ProjectHelper {
    /** The URI for ant name space */
    public static final String ANT_CORE_URI    = "antlib:org.apache.tools.ant";

    /** The URI for antlib current definitions */
    public static final String ANT_CURRENT_URI      = "ant:current";

    /** The URI for defined types/tasks - the format is antlib:<package> */
    public static final String ANTLIB_URI     = "antlib:";

    /** Polymorphic attribute  */
    public static final String ANT_TYPE = "ant-type";

    /**
     * Name of JVM system property which provides the name of the
     * ProjectHelper class to use.
     */
    public static final String HELPER_PROPERTY = MagicNames.PROJECT_HELPER_CLASS;

    /**
     * The service identifier in jars which provide Project Helper
     * implementations.
     */
    public static final String SERVICE_ID = MagicNames.PROJECT_HELPER_SERVICE;

    /**
     * name of project helper reference that we add to a project
     */
    public static final String PROJECTHELPER_REFERENCE = MagicNames.REFID_PROJECT_HELPER;

    /**
     * Configures the project with the contents of the specified XML file.
     *
     * @param project The project to configure. Must not be <code>null</code>.
     * @param buildFile An XML file giving the project's configuration.
     *                  Must not be <code>null</code>.
     *
     * @exception BuildException if the configuration is invalid or cannot be read
     */
    public static void configureProject(Project project, File buildFile) throws BuildException {
        ProjectHelper helper = ProjectHelper.getProjectHelper();
        project.addReference(PROJECTHELPER_REFERENCE, helper);
        helper.parse(project, buildFile);
    }

    /** Default constructor */
    public ProjectHelper() {
    }



    private Vector importStack = new Vector();

    /** EXPERIMENTAL WILL_CHANGE
     *
     */

    /** EXPERIMENTAL WILL_CHANGE
     *  Import stack.
     *  Used to keep track of imported files. Error reporting should
     *  display the import path.
     *
     * @return the stack of import source objects.
     */
    public Vector getImportStack() {
        return importStack;
    }

    /**
     * Parses the project file, configuring the project as it goes.
     *
     * @param project The project for the resulting ProjectHelper to configure.
     *                Must not be <code>null</code>.
     * @param source The source for XML configuration. A helper must support
     *               at least File, for backward compatibility. Helpers may
     *               support URL, InputStream, etc or specialized types.
     *
     * @since Ant1.5
     * @exception BuildException if the configuration is invalid or cannot
     *                           be read
     */
    public void parse(Project project, Object source) throws BuildException {
        throw new BuildException("ProjectHelper.parse() must be implemented "
            + "in a helper plugin " + this.getClass().getName());
    }

    /**
     * Discovers a project helper instance. Uses the same patterns
     * as JAXP, commons-logging, etc: a system property, a JDK1.3
     * service discovery, default.
     *
     * @return a ProjectHelper, either a custom implementation
     * if one is available and configured, or the default implementation
     * otherwise.
     *
     * @exception BuildException if a specified helper class cannot
     * be loaded/instantiated.
     */
    public static ProjectHelper getProjectHelper() throws BuildException {
        ProjectHelper helper = null;

        String helperClass = System.getProperty(HELPER_PROPERTY);
        try {
            if (helperClass != null) {
                helper = newHelper(helperClass);
            }
        } catch (SecurityException e) {
            System.out.println("Unable to load ProjectHelper class \""
                + helperClass + " specified in system property "
                + HELPER_PROPERTY);
        }

        if (helper == null) {
            try {
                ClassLoader classLoader = LoaderUtils.getContextClassLoader();
                InputStream is = null;
                if (classLoader != null) {
                    is = classLoader.getResourceAsStream(SERVICE_ID);
                }
                if (is == null) {
                    is = ClassLoader.getSystemResourceAsStream(SERVICE_ID);
                }
                if (is != null) {
                    InputStreamReader isr;
                    try {
                        isr = new InputStreamReader(is, "UTF-8");
                    } catch (java.io.UnsupportedEncodingException e) {
                        isr = new InputStreamReader(is);
                    }
                    BufferedReader rd = new BufferedReader(isr);

                    String helperClassName = rd.readLine();
                    rd.close();

                    if (helperClassName != null && !"".equals(helperClassName)) {
                        helper = newHelper(helperClassName);
                    }
                }
            } catch (Exception ex) {
                System.out.println("Unable to load ProjectHelper from service " + SERVICE_ID);
            }
        }
        return helper == null ? new ProjectHelper2() : helper;
    }

    /**
     * Creates a new helper instance from the name of the class.
     * It'll first try the thread class loader, then Class.forName()
     * will load from the same loader that loaded this class.
     *
     * @param helperClass The name of the class to create an instance
     *                    of. Must not be <code>null</code>.
     *
     * @return a new instance of the specified class.
     *
     * @exception BuildException if the class cannot be found or
     * cannot be appropriate instantiated.
     */
    private static ProjectHelper newHelper(String helperClass)
        throws BuildException {
        ClassLoader classLoader = LoaderUtils.getContextClassLoader();
        try {
            Class clazz = null;
            if (classLoader != null) {
                try {
                    clazz = classLoader.loadClass(helperClass);
                } catch (ClassNotFoundException ex) {
                }
            }
            if (clazz == null) {
                clazz = Class.forName(helperClass);
            }
            return ((ProjectHelper) clazz.newInstance());
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    /**
     * JDK1.1 compatible access to the context class loader. Cut & paste from JAXP.
     *
     * @deprecated since 1.6.x.
     *             Use LoaderUtils.getContextClassLoader()
     *
     * @return the current context class loader, or <code>null</code>
     * if the context class loader is unavailable.
     */
    public static ClassLoader getContextClassLoader() {
        return LoaderUtils.isContextLoaderAvailable() ? LoaderUtils.getContextClassLoader() : null;
    }


    /**
     * Configures an object using an introspection handler.
     *
     * @param target The target object to be configured.
     *               Must not be <code>null</code>.
     * @param attrs  A list of attributes to configure within the target.
     *               Must not be <code>null</code>.
     * @param project The project containing the target.
     *                Must not be <code>null</code>.
     *
     * @deprecated since 1.6.x.
     *             Use IntrospectionHelper for each property.
     *
     * @exception BuildException if any of the attributes can't be handled by
     *                           the target
     */
    public static void configure(Object target, AttributeList attrs,
                                 Project project) throws BuildException {
        if (target instanceof TypeAdapter) {
            target = ((TypeAdapter) target).getProxy();
        }
        IntrospectionHelper ih = IntrospectionHelper.getHelper(project, target.getClass());

        for (int i = 0, length = attrs.getLength(); i < length; i++) {
            String value = replaceProperties(project, attrs.getValue(i), project.getProperties());
            try {
                ih.setAttribute(project, target, attrs.getName(i).toLowerCase(Locale.US), value);
            } catch (BuildException be) {
                if (!attrs.getName(i).equals("id")) {
                    throw be;
                }
            }
        }
    }

    /**
     * Adds the content of #PCDATA sections to an element.
     *
     * @param project The project containing the target.
     *                Must not be <code>null</code>.
     * @param target  The target object to be configured.
     *                Must not be <code>null</code>.
     * @param buf A character array of the text within the element.
     *            Will not be <code>null</code>.
     * @param start The start element in the array.
     * @param count The number of characters to read from the array.
     *
     * @exception BuildException if the target object doesn't accept text
     */
    public static void addText(Project project, Object target, char[] buf,
        int start, int count) throws BuildException {
        addText(project, target, new String(buf, start, count));
    }

    /**
     * Adds the content of #PCDATA sections to an element.
     *
     * @param project The project containing the target.
     *                Must not be <code>null</code>.
     * @param target  The target object to be configured.
     *                Must not be <code>null</code>.
     * @param text    Text to add to the target.
     *                May be <code>null</code>, in which case this
     *                method call is a no-op.
     *
     * @exception BuildException if the target object doesn't accept text
     */
    public static void addText(Project project, Object target, String text)
        throws BuildException {

        if (text == null) {
            return;
        }
        if (target instanceof TypeAdapter) {
            target = ((TypeAdapter) target).getProxy();
        }
        IntrospectionHelper.getHelper(project, target.getClass()).addText(project, target, text);
    }

    /**
     * Stores a configured child element within its parent object.
     *
     * @param project Project containing the objects.
     *                May be <code>null</code>.
     * @param parent  Parent object to add child to.
     *                Must not be <code>null</code>.
     * @param child   Child object to store in parent.
     *                Should not be <code>null</code>.
     * @param tag     Name of element which generated the child.
     *                May be <code>null</code>, in which case
     *                the child is not stored.
     */
    public static void storeChild(Project project, Object parent, Object child, String tag) {
        IntrospectionHelper ih = IntrospectionHelper.getHelper(project, parent.getClass());
        ih.storeElement(project, parent, child, tag);
    }

    /**
     * Replaces <code>${xxx}</code> style constructions in the given value with
     * the string value of the corresponding properties.
     *
     * @param project The project containing the properties to replace.
     *                Must not be <code>null</code>.
     *
     * @param value The string to be scanned for property references.
     *              May be <code>null</code>.
     *
     * @exception BuildException if the string contains an opening
     *                           <code>${</code> without a closing
     *                           <code>}</code>
     * @return the original string with the properties replaced, or
     *         <code>null</code> if the original string is <code>null</code>.
     *
     * @deprecated since 1.6.x.
     *             Use project.replaceProperties().
     * @since 1.5
     */
     public static String replaceProperties(Project project, String value) throws BuildException {
         return project.replaceProperties(value);
     }

    /**
     * Replaces <code>${xxx}</code> style constructions in the given value
     * with the string value of the corresponding data types.
     *
     * @param project The container project. This is used solely for
     *                logging purposes. Must not be <code>null</code>.
     * @param value The string to be scanned for property references.
     *              May be <code>null</code>, in which case this
     *              method returns immediately with no effect.
     * @param keys  Mapping (String to String) of property names to their
     *              values. Must not be <code>null</code>.
     *
     * @exception BuildException if the string contains an opening
     *                           <code>${</code> without a closing
     *                           <code>}</code>
     * @return the original string with the properties replaced, or
     *         <code>null</code> if the original string is <code>null</code>.
     * @deprecated since 1.6.x.
     *             Use PropertyHelper.
     */
     public static String replaceProperties(Project project, String value, Hashtable keys)
             throws BuildException {
        PropertyHelper ph = PropertyHelper.getPropertyHelper(project);
        return ph.replaceProperties(null, value, keys);
    }

    /**
     * Parses a string containing <code>${xxx}</code> style property
     * references into two lists. The first list is a collection
     * of text fragments, while the other is a set of string property names.
     * <code>null</code> entries in the first list indicate a property
     * reference from the second list.
     *
     * @param value     Text to parse. Must not be <code>null</code>.
     * @param fragments List to add text fragments to.
     *                  Must not be <code>null</code>.
     * @param propertyRefs List to add property names to.
     *                     Must not be <code>null</code>.
     *
     * @deprecated since 1.6.x.
     *             Use PropertyHelper.
     * @exception BuildException if the string contains an opening
     *                           <code>${</code> without a closing <code>}</code>
     */
    public static void parsePropertyString(String value, Vector fragments, Vector propertyRefs)
            throws BuildException {
        PropertyHelper.parsePropertyStringDefault(value, fragments, propertyRefs);
    }

    /**
     * Map a namespaced {uri,name} to an internal string format.
     * For BC purposes the names from the ant core uri will be
     * mapped to "name", other names will be mapped to
     * uri + ":" + name.
     * @param uri   The namepace URI
     * @param name  The localname
     * @return      The stringified form of the ns name
     */
    public static String genComponentName(String uri, String name) {
        if (uri == null || uri.equals("") || uri.equals(ANT_CORE_URI)) {
            return name;
        }
        return uri + ":" + name;
    }

    /**
     * extract a uri from a component name
     *
     * @param componentName  The stringified form for {uri, name}
     * @return               The uri or "" if not present
     */
    public static String extractUriFromComponentName(String componentName) {
        if (componentName == null) {
            return "";
        }
        int index = componentName.lastIndexOf(':');
        if (index == -1) {
            return "";
        }
        return componentName.substring(0, index);
    }

    /**
     * extract the element name from a component name
     *
     * @param componentName  The stringified form for {uri, name}
     * @return               The element name of the component
     */
    public static String extractNameFromComponentName(String componentName) {
        int index = componentName.lastIndexOf(':');
        if (index == -1) {
            return componentName;
        }
        return componentName.substring(index + 1);
    }

    /**
     * Add location to build exception.
     * @param ex the build exception, if the build exception
     *           does not include
     * @param newLocation the location of the calling task (may be null)
     * @return a new build exception based in the build exception with
     *         location set to newLocation. If the original exception
     *         did not have a location, just return the build exception
     */
    public static BuildException addLocationToBuildException(
            BuildException ex, Location newLocation) {
        if (ex.getLocation() == null || ex.getMessage() == null) {
            return ex;
        }
        String errorMessage
            = "The following error occurred while executing this line:"
            + System.getProperty("line.separator")
            + ex.getLocation().toString()
            + ex.getMessage();
        if (newLocation == null) {
            return new BuildException(errorMessage, ex);
        }
        return new BuildException(errorMessage, ex, newLocation);
    }
}