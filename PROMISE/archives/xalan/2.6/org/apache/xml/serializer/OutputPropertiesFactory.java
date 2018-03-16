package org.apache.xml.serializer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Properties;
import javax.xml.transform.OutputKeys;

import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.Constants;
import org.apache.xml.utils.WrappedRuntimeException;

/**
 * This class acts as a factory to generate properties for the given output type
 * ("xml", "text", "html")..
 */
public class OutputPropertiesFactory
{
    /** Built-in extensions namespace, reexpressed in {namespaceURI} syntax
     * suitable for prepending to a localname to produce a "universal
     * name".
     */
    public static final String S_BUILTIN_EXTENSIONS_UNIVERSAL =
        "{" + Constants.S_BUILTIN_EXTENSIONS_URL + "}";


    /** The number of whitespaces to indent by, if indent="yes". */
    public static final String S_KEY_INDENT_AMOUNT =
        S_BUILTIN_EXTENSIONS_UNIVERSAL + "indent-amount";

    /**
     * Fully qualified name of class with a default constructor that
     *  implements the ContentHandler interface, where the result tree events
     *  will be sent to.
     */
    public static final String S_KEY_CONTENT_HANDLER =
        S_BUILTIN_EXTENSIONS_UNIVERSAL + "content-handler";

    /** File name of file that specifies character to entity reference mappings. */
    public static final String S_KEY_ENTITIES =
        S_BUILTIN_EXTENSIONS_UNIVERSAL + "entities";

    /** Use a value of "yes" if the href values for HTML serialization should
     *  use %xx escaping. */
    public static final String S_USE_URL_ESCAPING =
        S_BUILTIN_EXTENSIONS_UNIVERSAL + "use-url-escaping";

    /** Use a value of "yes" if the META tag should be omitted where it would
     *  otherwise be supplied.
     */
    public static final String S_OMIT_META_TAG =
        S_BUILTIN_EXTENSIONS_UNIVERSAL + "omit-meta-tag";

    /**
     * The old built-in extension namespace
     */
    public static final String S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL =
        "{" + Constants.S_BUILTIN_OLD_EXTENSIONS_URL + "}";

    /**
     * The length of the old built-in extension namespace
     */
    public static final int S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL_LEN =
        S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL.length();


    private static final String S_XSLT_PREFIX = "xslt.output.";
    private static final int S_XSLT_PREFIX_LEN = S_XSLT_PREFIX.length();
    private static final String S_XALAN_PREFIX = "org.apache.xslt.";
    private static final int S_XALAN_PREFIX_LEN = S_XALAN_PREFIX.length();

    /** Synchronization object for lazy initialization of the above tables. */
    private static Integer m_synch_object = new Integer(1);

    /** the directory in which the various method property files are located */
    private static final String PROP_DIR = "org/apache/xml/serializer/";
    /** property file for default XML properties */
    private static final String PROP_FILE_XML = "output_xml.properties";
    /** property file for default TEXT properties */
    private static final String PROP_FILE_TEXT = "output_text.properties";
    /** property file for default HTML properties */
    private static final String PROP_FILE_HTML = "output_html.properties";
    /** property file for default UNKNOWN (Either XML or HTML, to be determined later) properties */
    private static final String PROP_FILE_UNKNOWN = "output_unknown.properties";


    /** The default properties of all output files. */
    private static Properties m_xml_properties = null;

    /** The default properties when method="html". */
    private static Properties m_html_properties = null;

    /** The default properties when method="text". */
    private static Properties m_text_properties = null;

    /** The properties when method="" for the "unknown" wrapper */
    private static Properties m_unknown_properties = null;

    private static final Class
        ACCESS_CONTROLLER_CLASS = findAccessControllerClass();

    private static Class findAccessControllerClass() {
        try
        {

            return Class.forName("java.security.AccessController");
        }
        catch (Exception e)
        {
        }

        return null;
    }

    /**
     * Creates an empty OutputProperties with the defaults specified by
     * a property file.  The method argument is used to construct a string of
     * the form output_[method].properties (for instance, output_html.properties).
     * The output_xml.properties file is always used as the base.
     * <p>At the moment, anything other than 'text', 'xml', and 'html', will
     * use the output_xml.properties file.</p>
     *
     * @param   method non-null reference to method name.
     *
     * @return Properties object that holds the defaults for the given method.
     */
    static public Properties getDefaultMethodProperties(String method)
    {
        String fileName = null;
        Properties defaultProperties = null;
        try
        {
            synchronized (m_synch_object)
            {
                {
                    fileName = PROP_FILE_XML;
                    m_xml_properties = loadPropertiesFile(fileName, null);
                }
            }

            if (method.equals(Method.XML))
            {
                defaultProperties = m_xml_properties;
            }
            else if (method.equals(Method.HTML))
            {
                {
                    fileName = PROP_FILE_HTML;
                    m_html_properties =
                        loadPropertiesFile(fileName, m_xml_properties);
                }

                defaultProperties = m_html_properties;
            }
            else if (method.equals(Method.TEXT))
            {
                {
                    fileName = PROP_FILE_TEXT;
                    m_text_properties =
                        loadPropertiesFile(fileName, m_xml_properties);
                    if (null
                        == m_text_properties.getProperty(OutputKeys.ENCODING))
                    {
                        String mimeEncoding = Encodings.getMimeEncoding(null);
                        m_text_properties.put(
                            OutputKeys.ENCODING,
                            mimeEncoding);
                    }
                }

                defaultProperties = m_text_properties;
            }
            else if (method.equals(org.apache.xml.serializer.Method.UNKNOWN))
            {
                {
                    fileName = PROP_FILE_UNKNOWN;
                    m_unknown_properties =
                        loadPropertiesFile(fileName, m_xml_properties);
                }

                defaultProperties = m_unknown_properties;
            }
            else
            {
                defaultProperties = m_xml_properties;
            }
        }
        catch (IOException ioe)
        {
            throw new WrappedRuntimeException(
                XMLMessages.createXMLMessage(
                    XMLErrorResources.ER_COULD_NOT_LOAD_METHOD_PROPERTY,
                    new Object[] { fileName, method }),
                ioe);
        }

        return new Properties(defaultProperties);
    }

    /**
     * Load the properties file from a resource stream.  If a
     * key name such as "org.apache.xslt.xxx", fix up the start of
     * string to be a curly namespace.  If a key name starts with
     * "xslt.output.xxx", clip off "xslt.output.".  If a key name *or* a
     * key value is discovered, check for \u003a in the text, and
     * fix it up to be ":", since earlier versions of the JDK do not
     * handle the escape sequence (at least in key names).
     *
     * @param resourceName non-null reference to resource name.
     * @param defaults Default properties, which may be null.
     */
    static private Properties loadPropertiesFile(
        final String resourceName,
        Properties defaults)
        throws IOException
    {


        Properties props = new Properties(defaults);

        InputStream is = null;
        BufferedInputStream bis = null;

        try
        {
            if (ACCESS_CONTROLLER_CLASS != null)
            {
                is = (InputStream) AccessController
                    .doPrivileged(new PrivilegedAction() {
                        public Object run()
                        {
                            return OutputPropertiesFactory.class
                                .getResourceAsStream(resourceName);
                        }
                    });
            }
            else
            {
                is = OutputPropertiesFactory.class
                    .getResourceAsStream(resourceName);
            }

            bis = new BufferedInputStream(is);
            props.load(bis);
        }
        catch (IOException ioe)
        {
            if (defaults == null)
            {
                throw ioe;
            }
            else
            {
                throw new WrappedRuntimeException(
                    XMLMessages.createXMLMessage(
                        XMLErrorResources.ER_COULD_NOT_LOAD_RESOURCE,
                        new Object[] { resourceName }),
                    ioe);
            }
        }
        catch (SecurityException se)
        {
            if (defaults == null)
            {
                throw se;
            }
            else
            {
                throw new WrappedRuntimeException(
                    XMLMessages.createXMLMessage(
                        XMLErrorResources.ER_COULD_NOT_LOAD_RESOURCE,
                        new Object[] { resourceName }),
                    se);
            }
        }
        finally
        {
            if (bis != null)
            {
                bis.close();
            }
            if (is != null)
            {
                is.close();
            }
        }


        Enumeration keys = ((Properties) props.clone()).keys();
        while (keys.hasMoreElements())
        {
            String key = (String) keys.nextElement();
            String value = null;
            try
            {
                value = System.getProperty(key);
            }
            catch (SecurityException se)
            {
            }
            if (value == null)
                value = (String) props.get(key);

            String newKey = fixupPropertyString(key, true);
            String newValue = null;
            try
            {
                newValue = System.getProperty(newKey);
            }
            catch (SecurityException se)
            {
            }
            if (newValue == null)
                newValue = fixupPropertyString(value, false);
            else
                newValue = fixupPropertyString(newValue, false);

            if (key != newKey || value != newValue)
            {
                props.remove(key);
                props.put(newKey, newValue);
            }

        }

        return props;
    }

    /**
     * Fix up a string in an output properties file according to
     * the rules of {@link #loadPropertiesFile}.
     *
     * @param s non-null reference to string that may need to be fixed up.
     * @return A new string if fixup occured, otherwise the s argument.
     */
    static private String fixupPropertyString(String s, boolean doClipping)
    {
        int index;
        if (doClipping && s.startsWith(S_XSLT_PREFIX))
        {
            s = s.substring(S_XSLT_PREFIX_LEN);
        }
        if (s.startsWith(S_XALAN_PREFIX))
        {
            s =
                S_BUILTIN_EXTENSIONS_UNIVERSAL
                    + s.substring(S_XALAN_PREFIX_LEN);
        }
        if ((index = s.indexOf("\\u003a")) > 0)
        {
            String temp = s.substring(index + 6);
            s = s.substring(0, index) + ":" + temp;

        }
        return s;
    }

}
