package org.apache.xerces.readers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Interface for implementing basic catalog support in the parser.
 * To implement and use a Catalog, implement this interface and
 * install your catalog instance as the EntityResolver in the
 * parser's entity handler. For example:
 * <pre>
 *     XMLParser parser = new AnyParser();
 *     <font color="blue">parser.addCatalogHandler(new MyCatalog());</font>
 * </pre>
 * <p>
 * This default catalog implementation does not provide a method
 * for loading multiple catalogs from various input sources.
 * Instead, it is a convenient base class for other catalog
 * implementations.
 * <p>
 * To create a catalog implementation, simply extend this class
 * and implement the <tt>loadCatalog</tt> method. Public and system
 * identifier mappings can be stored and accessed using the 
 * convenient public methods on this class.
 *
 * @author  Andy Clark, IBM
 * @version
 *
 * @see org.xml.sax.EntityResolver
 */
public abstract class XMLCatalogHandler implements EntityResolver {

    /**
     * Loads the catalog stream specified by the given input source and
     * appends the contents to the catalog.
     *
     * @param source The catalog source.
     *
     * @exception java.lang.Exception Throws an exception if an error
     *                                occurs while loading the catalog source.
     */
    public abstract void loadCatalog(InputSource source) throws Exception;



    /** Public identifier mappings. */
    private Hashtable publicMap = new Hashtable();

    /** System identifier mappings (aliases). */
    private Hashtable systemMap = new Hashtable();


    /**
     * Adds a public to system identifier mapping.
     *
     * @param publicId The public identifier, or "key".
     * @param systemId The system identifier, or "value".
     */
    public void addPublicMapping(String publicId, String systemId) {
        publicMap.put(publicId, systemId);
    }

    /**
     * Removes a public identifier mapping.
     *
     * @param publicId The public identifier to remove.
     */
    public void removePublicMapping(System publicId) {
        publicMap.remove(publicId);
    }

    /** Returns an enumeration of public identifier mapping keys. */
    public Enumeration getPublicMappingKeys() {
        return publicMap.keys();
    }

    /**
     * Returns a public identifier mapping.
     *
     * @param publicId The public identifier, or "key".
     *
     * @return Returns the system identifier value or null if there
     *         is no mapping defined.
     */
    public String getPublicMapping(String publicId) {
        return (String)publicMap.get(publicId);
    }

    /**
     * Adds a system identifier alias.
     *
     * @param publicId The system identifier "key".
     * @param systemId The system identifier "value".
     */
    public void addSystemMapping(String systemId1, String systemId2) {
        systemMap.put(systemId1, systemId2);
    }

    /**
     * Removes a system identifier alias.
     *
     * @param systemId The system identifier to remove.
     */
    public void removeSystemMapping(String systemId) {
        systemMap.remove(systemId);
    }

    /** Returns an enumeration of system identifier mapping keys. */
    public Enumeration getSystemMappingKeys() {
        return systemMap.keys();
    }

    /**
     * Returns a system identifier alias.
     *
     * @param systemId The system identifier "key".
     *
     * @return Returns the system identifier alias value or null if there
     *         is no alias defined.
     */
    public String getSystemMapping(String systemId) {
        return (String)systemMap.get(systemId);
    }

    /**
     * Resolves external entities.
     *
     * @param publicId The public identifier used for entity resolution.
     * @param systemId If the publicId is not null, this systemId is
     *                 to be considered the default system identifier;
     *                 else a system identifier alias mapping is
     *                 requested.
     *
     * @return Returns the input source of the resolved entity or null
     *         if no resolution is possible.
     *
     * @exception org.xml.sax.SAXException Exception thrown on SAX error.
     * @exception java.io.IOException Exception thrown on i/o error. 
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException 
    {
        
        if (publicId != null) {
            String value = getPublicMapping(publicId);
            if (value != null) {
                return new InputSource(value);
            }
        }

        if (systemId != null) {
            String value = getSystemMapping(systemId);
            if (value == null) {
                value = systemId;
            }

            return new InputSource(value);
        }

        return null;

    }
}
