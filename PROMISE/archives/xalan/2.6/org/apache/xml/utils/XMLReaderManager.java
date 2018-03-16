package org.apache.xml.utils;

import java.util.Hashtable;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.SAXException;

/**
 * Creates XMLReader objects and caches them for re-use.
 * This class follows the singleton pattern.
 */
public class XMLReaderManager {

    private static final String NAMESPACES_FEATURE =
    private static final String NAMESPACE_PREFIXES_FEATURE =
    private static final XMLReaderManager m_singletonManager =
                                                     new XMLReaderManager();

    /**
     * Parser factory to be used to construct XMLReader objects
     */
    private static SAXParserFactory m_parserFactory;

    /**
     * Cache of XMLReader objects
     */
    private ThreadLocal m_readers;

    /**
     * Keeps track of whether an XMLReader object is in use.
     */
    private Hashtable m_inUse;

    /**
     * Hidden constructor
     */
    private XMLReaderManager() {
    }

    /**
     * Retrieves the singleton reader manager
     */
    public static XMLReaderManager getInstance() {
        return m_singletonManager;
    }

    /**
     * Retrieves a cached XMLReader for this thread, or creates a new
     * XMLReader, if the existing reader is in use.  When the caller no
     * longer needs the reader, it must release it with a call to
     * {@link releaseXMLReader}.
     */
    public synchronized XMLReader getXMLReader() throws SAXException {
        XMLReader reader;
        boolean readerInUse;

        if (m_readers == null) {
            m_readers = new ThreadLocal();
        }

        if (m_inUse == null) {
            m_inUse = new Hashtable();
        }

        reader = (XMLReader) m_readers.get();
        boolean threadHasReader = (reader != null);
        if (!threadHasReader || m_inUse.get(reader) == Boolean.TRUE) {
            try {
                try {
                    reader = XMLReaderFactory.createXMLReader();
                } catch (Exception e) {
                   try {
                        if (m_parserFactory == null) {
                            m_parserFactory = SAXParserFactory.newInstance();
                            m_parserFactory.setNamespaceAware(true);
                        }

                        reader = m_parserFactory.newSAXParser().getXMLReader();
                   } catch (ParserConfigurationException pce) {
                   }
                }
                try {
                    reader.setFeature(NAMESPACES_FEATURE, true);
                    reader.setFeature(NAMESPACE_PREFIXES_FEATURE, false);
                } catch (SAXException se) {
                }
            } catch (ParserConfigurationException ex) {
                throw new SAXException(ex);
            } catch (FactoryConfigurationError ex1) {
                throw new SAXException(ex1.toString());
            } catch (NoSuchMethodError ex2) {
            } catch (AbstractMethodError ame) {
            }

            if (!threadHasReader) {
                m_readers.set(reader);
                m_inUse.put(reader, Boolean.TRUE);
            }
        } else {
            m_inUse.put(reader, Boolean.TRUE);
        }

        return reader;
    }

    /**
     * Mark the cached XMLReader as available.  If the reader was not
     * actually in the cache, do nothing.
     *
     * @param reader The XMLReader that's being released.
     */
    public synchronized void releaseXMLReader(XMLReader reader) {
        if (m_readers.get() == reader) {
            m_inUse.put(reader, Boolean.FALSE);
        }
    }
}
