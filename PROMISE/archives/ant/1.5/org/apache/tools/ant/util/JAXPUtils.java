package org.apache.tools.ant.util;

import org.apache.tools.ant.BuildException;

import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.FactoryConfigurationError;
import java.io.File;


/**
 * Collection of helper methods that retrieve a ParserFactory or
 * Parsers and Readers.
 *
 * <p>This class will create only a single factory instance.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 *
 * @since Ant 1.5
 */
public class JAXPUtils {

    /**
     */

    /**
     * Parser factory to use to create parsers.
     * @see #getParserFactory
     *
     * @since Ant 1.5
     */
    private static SAXParserFactory parserFactory = null;

    /**
     * Returns the parser factory to use. Only one parser factory is
     * ever created by this method and is then cached for future use.
     *
     * @return a SAXParserFactory to use
     *
     * @since Ant 1.5
     */
    public synchronized static SAXParserFactory getParserFactory() 
        throws BuildException {

        if (parserFactory == null) {
            parserFactory = newParserFactory();
        }
        return parserFactory;
    }

    /**
     * Returns a new  parser factory instance.
     *
     * @since Ant 1.5
     */
    public static SAXParserFactory newParserFactory() throws BuildException {

        try {
            return SAXParserFactory.newInstance();
        } catch (FactoryConfigurationError e) {
            throw new BuildException("XML parser factory has not been "
                                     + "configured correctly: " 
                                     + e.getMessage(), e);
        }
    }

    /**
     * Returns a newly created SAX 1 Parser, using the default parser
     * factory.
     *
     * @return a SAX 1 Parser.
     * @see #getParserFactory
     * @since Ant 1.5
     */
    public static Parser getParser() throws BuildException {
        try {
            return newSAXParser().getParser();
        } catch (SAXException e) {
            throw convertToBuildException(e);
        }
    }

    /**
     * Returns a newly created SAX 2 XMLReader, using the default parser
     * factory.
     *
     * @return a SAX 2 XMLReader.
     * @see #getParserFactory
     * @since Ant 1.5
     */
    public static XMLReader getXMLReader() throws BuildException {
        try {
            return newSAXParser().getXMLReader();
        } catch (SAXException e) {
            throw convertToBuildException(e);
        }
    }

    /**
     * This is a best attempt to provide a URL.toExternalForm() from
     * a file URL. Some parsers like Crimson choke on uri that are made of
     * backslashed paths (ie windows) as it is does not conform
     * URI specifications.
     * @param file the file to create the system id from.
     * @return the systemid corresponding to the given file.
     * @since Ant 1.5.2
     */
    public static String getSystemId(File file){
        String path = file.getAbsolutePath();
        path = path.replace('\\', '/');

        if (File.separatorChar == '\\') {
            return FILE_PROTOCOL_PREFIX + "/" + path;
        }
        return FILE_PROTOCOL_PREFIX + path;
    }

    /**
     * @return a new SAXParser instance as helper for getParser and
     * getXMLReader.
     *
     * @since Ant 1.5
     */
    private static SAXParser newSAXParser() throws BuildException {
        try {
            return getParserFactory().newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new BuildException("Cannot create parser for the given "
                                     + "configuration: " + e.getMessage(), e);
        } catch (SAXException e) {
            throw convertToBuildException(e);
        }
    }

    /**
     * Translate a SAXException into a BuildException
     *
     * @since Ant 1.5
     */
    private static BuildException convertToBuildException(SAXException e) {
        Exception nested = e.getException();
        if (nested != null) {
            return new BuildException(nested);
        } else {
            return new BuildException(e);
        }
    }

}
