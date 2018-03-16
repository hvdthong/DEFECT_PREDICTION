package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLParser;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.utils.StringPool;

import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This catalog supports the XCatalog proposal draft 0.2 posted
 * to the xml-dev mailing list by 
 * <a href="mailto:cowan@locke.ccil.org">John Cowan</a>. XCatalog
 * is an XML representation of the SGML Open TR9401:1997 catalog
 * format. The current proposal supports public identifier maps,
 * system identifier aliases, and public identifier prefix 
 * delegates. Refer to the XCatalog DTD for the full specification 
 * of this catalog format at 
 * <p>
 * In order to use XCatalogs, you must write the catalog files
 * with the following restrictions:
 * <ul>
 * <li>You must follow the XCatalog grammar.
 * <li>You must specify the <tt>&lt;!DOCTYPE&gt;</tt> line with
 *   make sure that the system identifier is able to locate the
 *   XCatalog 0.2 DTD (which is included in the Jar file containing
 *   the org.apache.xerces.readers.XCatalog class).
 *   For example:
 *   <pre>
 *   </pre>
 * <li>The enclosing <tt>&lt;XCatalog&gt;</tt> document root 
 *   element is <b>not</b> optional -- it <b>must</b> be specified.
 * <li>The <tt>Version</tt> attribute of the <tt>&lt;XCatalog&gt;</tt>
 *   has been modified from '<tt><i>#FIXED "1.0"</i></tt>' to 
 *   '<tt><i>(0.1|0.2) "0.2"</i></tt>'.
 * </ul>
 * <p>
 * To use this catalog in a parser, set an XCatalog instance as the
 * parser's <tt>EntityResolver</tt>. For example:
 * <pre>
 *   XMLParser parser  = new AnyParser();
 *   Catalog   catalog = <font color="blue">new XCatalog()</font>;
 *   <font color="blue">parser.getEntityHandler().setEntityResolver(catalog);</font>
 * </pre>
 * <p>
 * Once installed, catalog files that conform to the XCatalog grammar
 * can be appended to the catalog by calling the <tt>loadCatalog</tt>
 * method on the parser or the catalog instance. The following example
 * loads the contents of two catalog files:
 * <pre>
 *   parser.loadCatalog(new InputSource("catalogs/cat1.xml"));
 * </pre>
 * <p>
 * <b>Limitations:</b> The following are the current limitations
 * of this XCatalog implementation:
 * <ul>
 * <li>No error checking is done to avoid circular <tt>Delegate</tt>
 *   or <tt>Extend</tt> references. Do not specify a combination of
 *   catalog files that reference each other.
 * </ul>
 *
 * @author  Andy Clark, IBM
 * @version $Id: XCatalog.java 315404 2000-04-04 21:14:25Z andyc $
 */
public class XCatalog
    extends XMLCatalogHandler
    {





    /** XCatalog DTD resource name ("xcatalog.dtd"). */
    static final String DTD = "xcatalog.dtd";

    /** XCatalog element name ("XCatalog"). */
    static final String XCATALOG = "XCatalog";

    /** Map element name ("Map"). */
    static final String MAP = "Map";

    /** PublicID attribute name ("PublicID"). */
    static final String PUBLICID = "PublicID";

    /** HRef attribute name ("HRef"). */
    static final String HREF = "HRef";

    /** Delegate element name ("Delegate"). */
    static final String DELEGATE = "Delegate";

    /** Extend element name ("Extend"). */
    static final String EXTEND = "Extend";

    /** Base element name ("Base"). */
    static final String BASE = "Base";

    /** Remap element name ("Remap"). */
    static final String REMAP = "Remap";

    /** SystemID attribute name ("SystemID"). */
    static final String SYSTEMID = "SystemID";


    /** Set to true and recompile to include debugging code in class. */
    private static final boolean DEBUG = false;


    /** Delegates. */
    private Hashtable delegate = new Hashtable();

    /** Delegates ordering. */
    private Vector delegateOrder = new Vector();


    /** 
     * Constructs an XCatalog instance.
     */
    public XCatalog() {
    }


    /**
     * Loads the catalog stream specified by the given input source and
     * appends the contents to the catalog.
     *
     * @param source The catalog source.
     *
     * @exception org.xml.sax.SAXException Throws exception on SAX error.
     * @exception java.io.IOException Throws exception on i/o error.
     */
    public void loadCatalog(InputSource source)
        throws SAXException, IOException
        {
        
        new Parser(source);

        /***
        if (DEBUG) {
            print("");
            }
        /***/


    /***
    void print(String indent) {
        System.out.println(indent+"# "+this);
        Enumeration maps = getMapKeys();
        while (maps.hasMoreElements()) {
            String key   = (String)maps.nextElement();
            String value = getMapValue(key);
            System.out.println(indent+"MAP \""+key+"\" -> \""+value+"\"");
            }
        Enumeration delegates = getDelegateKeys();
        while (delegates.hasMoreElements()) {
            String   key   = (String)delegates.nextElement();
            XCatalog value = getDelegateValue(key);
            System.out.println(indent+"DELEGATE \""+key+"\" -> "+value);
            value.print(indent+"  ");
            }
        Enumeration remaps = getRemapKeys();
        while (remaps.hasMoreElements()) {
            String key   = (String)remaps.nextElement();
            String value = getRemapValue(key);
            System.out.println(indent+"REMAP \""+key+"\" -> \""+value+"\"");
            }
        }
    /***/


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
    public InputSource resolveEntity(String publicId, String systemId) 
        throws SAXException, IOException
        {

        if (DEBUG) {
            System.out.println("resolveEntity(\""+publicId+"\", \""+systemId+"\")");
            }

        if (publicId != null) {
            String value = getPublicMapping(publicId);
            if (DEBUG) {
                System.out.println("  map: \""+publicId+"\" -> \""+value+"\"");
                }
            if (value != null) {
                InputSource source = resolveEntity(null, value);
                if (source == null) {
                    source = new InputSource(value);
                    }
                source.setPublicId(publicId);
                return source;
                }

            Enumeration delegates = getDelegateCatalogKeys();
            while (delegates.hasMoreElements()) {
                String key = (String)delegates.nextElement();
                if (DEBUG) {
                    System.out.println("  delegate: \""+key+"\"");
                    }
                if (publicId.startsWith(key)) {
                    XMLCatalogHandler catalog = getDelegateCatalog(key);
                    InputSource source = catalog.resolveEntity(publicId, systemId);
                    if (source != null) {
                        return source;
                        }
                    }
                }
            }

        String value = getSystemMapping(systemId);
        if (value != null) {
            if (DEBUG) {
                System.out.println("  remap: \""+systemId+"\" -> \""+value+"\"");
                }
            InputSource source = new InputSource(value);
            source.setPublicId(publicId);
            return source;
            }

        if (DEBUG) {
            System.out.println("  returning null!");
            }
        return null;



    /** 
     * Adds a delegate mapping. If the prefix of a public identifier
     * matches a delegate prefix, then the delegate catalog is
     * searched in order to resolve the identifier.
     * <p>
     * This method makes sure that prefixes that match each other
     * are inserted into the delegate list in order of longest prefix
     * length first.
     *
     * @param prefix  The delegate prefix.
     * @param catalog The delegate catalog.
     */
    public void addDelegateCatalog(String prefix, XCatalog catalog) {

        synchronized (delegate) {
            if (!delegate.containsKey(prefix)) {
                int size = delegateOrder.size();
                boolean found = false;
                for (int i = 0; i < size; i++) {
                    String element = (String)delegateOrder.elementAt(i);
                    if (prefix.startsWith(element) || prefix.compareTo(element) < 0) {
                        delegateOrder.insertElementAt(prefix, i);
                        found = true;
                        break;
                        }
                    }
                if (!found) {
                    delegateOrder.addElement(prefix);
                    }
                }

            delegate.put(prefix, catalog);
            }


    /** 
     * Removes a delegate. 
     *
     * @param prefix The delegate prefix to remove.
     */
    public void removeDelegateCatalog(String prefix) {

        synchronized (delegate) {
            delegate.remove(prefix);
            delegateOrder.removeElement(prefix);
            }


    /** Returns an enumeration of delegate prefixes. */
    public Enumeration getDelegateCatalogKeys() {
        return delegateOrder.elements();
        }

    /** Returns the catalog for the given delegate prefix. */
    public XCatalog getDelegateCatalog(String prefix) {
        return (XCatalog)delegate.get(prefix);
        }


    /** Returns true if the string is a valid URL. */
    boolean isURL(String str) {
        try {
            new java.net.URL(str);
            return true;
            }
        catch (java.net.MalformedURLException e) {
            }
        return false;
        }


    /** Parser for XCatalog document instances. */
    class Parser
        extends SAXParser
        implements DocumentHandler
        {


        /** The base. */
        private String base;


        /** Parses the specified input source. */
        public Parser(InputSource source) 
            throws SAXException, IOException
            {

            setEntityResolver(new Resolver());
            setDocumentHandler((DocumentHandler)this);

            setBase(source.getSystemId());
            parse(source);



        /** 
         * Sets the base from the given system identifier. The base is
         * the same as the system identifier with the least significant
         * part (the filename) removed.
         */
        protected void setBase(String systemId) throws SAXException {

            if (systemId == null) { 
                systemId = ""; 
                }

            systemId = fEntityHandler.expandSystemId(systemId);

            int index = systemId.lastIndexOf('/');
            if (index != -1) {
                systemId = systemId.substring(0, index + 1);
                }

            base = systemId;



        /** Not implemented. */
        public void processingInstruction(String target, String data) {}

        /** Not implemented. */
        public void setDocumentLocator(org.xml.sax.Locator locator) {}

        /** Not implemented. */
        public void startDocument() {}

        /** Not implemented. */
        public void endElement(String elementName) {}

        /** Not implemented. */
        public void endDocument() {}

        /** Not implemented. */
        public void characters(char ch[], int start, int length) {}

        /** Not implemented. */
        public void ignorableWhitespace(char ch[], int start, int length) {}

        /** The start of an element. */
        public void startElement(String elementName, AttributeList attrList) 
            throws SAXException
            {

            try {
                if (elementName.equals(XCATALOG)) {
                    return;
                    }
    
                if (elementName.equals(MAP)) {
                    String publicId = attrList.getValue(PUBLICID);
                    String href     = attrList.getValue(HREF);
                    if (DEBUG) {
                        System.out.println("MAP \""+publicId+"\" \""+href+"\"");
                        }
    
                    if (!isURL(href)) {
                        href = base + href;
                        }
                    addPublicMapping(publicId, href);
                    }
    
                else if (elementName.equals(DELEGATE)) {
                    String publicId = attrList.getValue(PUBLICID);
                    String href     = attrList.getValue(HREF);
                    if (DEBUG) {
                        System.out.println("DELEGATE \""+publicId+"\" \""+href+"\"");
                        }
    
                    if (!isURL(href)) {
                        href = base + href;
                        }
                    String systemId = fEntityHandler.expandSystemId(href);
    
                    XCatalog catalog = new XCatalog();
                    catalog.loadCatalog(new InputSource(systemId));
                    addDelegateCatalog(publicId, catalog);
                    }
    
                else if (elementName.equals(EXTEND)) {
                    String href = attrList.getValue(HREF);
                    if (DEBUG) {
                        System.out.println("EXTEND \""+href+"\"");
                        }
    
                    if (!isURL(href)) {
                        href = base + href;
                        }
                    String systemId = fEntityHandler.expandSystemId(href);
    
                    XCatalog.this.loadCatalog(new InputSource(systemId));
                    }
    
                else if (elementName.equals(BASE)) {
                    String href = attrList.getValue(HREF);
    
                    setBase(href);
                    if (DEBUG) {
                        System.out.println("BASE \""+href+"\" -> \""+base+"\"");
                        }
                    }
                
                else if (elementName.equals(REMAP)) {
                    String systemId = attrList.getValue(SYSTEMID);
                    String href     = attrList.getValue(HREF);
                    if (DEBUG) {
                        System.out.println("REMAP \""+systemId+"\" \""+href+"\"");
                        }
    
                    if (!isURL(href)) {
                        href = base + href;
                        }
                    addSystemMapping(systemId, href);
                    }
                }
            catch (Exception e) {
                throw new SAXException(e);
                }



        /** Resolver for locating the XCatalog DTD resource. */
        class Resolver
            implements EntityResolver
            {

            /** Resolves the XCatalog DTD entity. */
            public InputSource resolveEntity(String publicId, String systemId) 
                throws SAXException, IOException
                {

                if (publicId != null && publicId.equals(XCATALOG_DTD_PUBLICID)) {
                    InputSource src = new InputSource();
                    src.setPublicId(publicId);
                    InputStream is = getClass().getResourceAsStream(DTD);
                    src.setByteStream(is);
                    src.setCharacterStream(new InputStreamReader(is));
                    return src;
                    }

                return null;




