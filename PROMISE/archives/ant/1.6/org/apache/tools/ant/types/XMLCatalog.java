package org.apache.tools.ant.types;

import java.lang.reflect.Method;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;



/**
 * <p>This data type provides a catalog of resource locations (such as
 * DTDs and XML entities), based on the <a
 * OASIS "Open Catalog" standard</a>.  The catalog entries are used
 * both for Entity resolution and URI resolution, in accordance with
 * the {@link org.xml.sax.EntityResolver EntityResolver} and {@link
 * javax.xml.transform.URIResolver URIResolver} interfaces as defined
 * Processing Specification</a>.</p>
 *
 * <p>Resource locations can be specified either in-line or in
 * external catalog file(s), or both.  In order to use an external
 * catalog file, the xml-commons resolver library ("resolver.jar")
 * must be in your classpath.  External catalog files may be either <a
 * plain text format</a> or <a
 * XML format</a>.  If the xml-commons resolver library is not found
 * in the classpath, external catalog files, specified in
 * <code>&lt;catalogpath&gt;</code> paths, will be ignored and a warning will
 * be logged.  In this case, however, processing of inline entries will proceed
 * normally.</p>
 *
 * <p>Currently, only <code>&lt;dtd&gt;</code> and
 * <code>&lt;entity&gt;</code> elements may be specified inline; these
 * correspond to OASIS catalog entry types <code>PUBLIC</code> and
 * <code>URI</code> respectively.</p>
 *
 * <p>The following is a usage example:</p>
 *
 * <code>
 * &lt;xmlcatalog&gt;<br>
 * &nbsp;&nbsp;&lt;dtd publicId="" location="/path/to/file.jar" /&gt;<br>
 * &nbsp;&nbsp;&lt;dtd publicId="" location="/path/to/file2.jar" /&gt;<br>
 * &nbsp;&nbsp;&lt;entity publicId="" location="/path/to/file3.jar" /&gt;<br>
 * &nbsp;&nbsp;&lt;entity publicId="" location="/path/to/file4.jar" /&gt;<br>
 * &nbsp;&nbsp;&lt;catalogpath&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;pathelement location="/etc/sgml/catalog"/&gt;<br>
 * &nbsp;&nbsp;&lt;/catalogpath&gt;<br>
 * &nbsp;&nbsp;&lt;catalogfiles dir="/opt/catalogs/" includes="**\catalog.xml" /&gt;<br>
 * &lt;/xmlcatalog&gt;<br>
 * </code>
 * <p>
 * Tasks wishing to use <code>&lt;xmlcatalog&gt;</code> must provide a method called
 * <code>createXMLCatalog</code> which returns an instance of
 * <code>XMLCatalog</code>. Nested DTD and entity definitions are handled by
 * the XMLCatalog object and must be labeled <code>dtd</code> and
 * <code>entity</code> respectively.</p>
 *
 * <p>The following is a description of the resolution algorithm:
 * entities/URIs/dtds are looked up in each of the following contexts,
 * stopping when a valid and readable resource is found:
 * <ol>
 * <li>In the local filesystem</li>
 * <li>In the classpath</li>
 * <li>Using the Apache xml-commons resolver (if it is available)</li>
 * <li>In URL-space</li>
 * </ol>
 * </p>
 *
 * <p>See {@link
 * org.apache.tools.ant.taskdefs.optional.XMLValidateTask
 * XMLValidateTask} for an example of a task that has integrated
 * support for XMLCatalogs.</p>
 *
 * <p>Possible future extension could provide for additional OASIS
 * entry types to be specified inline.</p>
 *
 */
public class XMLCatalog extends DataType
    implements Cloneable, EntityResolver, URIResolver {

    /** helper for some File.toURL connversions */
    private static FileUtils fileUtils = FileUtils.newFileUtils();


    /** Holds dtd/entity objects until needed. */
    private Vector elements = new Vector();

    /**
     * Classpath in which to attempt to resolve resources.
     */
    private Path classpath;

    /**
     * Path listing external catalog files to search when resolving entities
     */
    private Path catalogPath;

    /**
     * The name of the bridge to the Apache xml-commons resolver
     * class, used to determine whether resolver.jar is present in the
     * classpath.
     */
    public static final String APACHE_RESOLVER
        = "org.apache.tools.ant.types.resolver.ApacheCatalogResolver";

    /**
     * Resolver base class
     */
    public static final String CATALOG_RESOLVER
        = "org.apache.xml.resolver.tools.CatalogResolver";


    /**
     * Default constructor
     */
    public XMLCatalog() {
        setChecked(false);
    }

    /**
     * Returns the elements of the catalog - ResourceLocation objects.
     *
     * @return the elements of the catalog - ResourceLocation objects
     */
    private Vector getElements() {
        return getRef().elements;
    }

    /**
     * Returns the classpath in which to attempt to resolve resources.
     *
     * @return the classpath
     */
    private Path getClasspath() {
        return getRef().classpath;
    }

    /**
     * Set the list of ResourceLocation objects in the catalog.
     * Not allowed if this catalog is itself a reference to another catalog --
     * that is, a catalog cannot both refer to another <em>and</em> contain
     * elements or other attributes.
     *
     * @param aVector the new list of ResourceLocations
     * to use in the catalog.
     */
    private void setElements(Vector aVector) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        elements = aVector;
    }

    /**
     * Allows nested classpath elements. Not allowed if this catalog
     * is itself a reference to another catalog -- that is, a catalog
     * cannot both refer to another <em>and</em> contain elements or
     * other attributes.
     *
     * @return a Path instance to be configured.
     */
    public Path createClasspath() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        setChecked(false);
        return this.classpath.createPath();
    }

    /**
     * Allows simple classpath string.  Not allowed if this catalog is
     * itself a reference to another catalog -- that is, a catalog
     * cannot both refer to another <em>and</em> contain elements or
     * other attributes.
     *
     * @param classpath the classpath to use to look up entities.
     */
    public void setClasspath(Path classpath) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
        setChecked(false);
    }

    /**
     * Allows classpath reference.  Not allowed if this catalog is
     * itself a reference to another catalog -- that is, a catalog
     * cannot both refer to another <em>and</em> contain elements or
     * other attributes.
     *
     * @param r an Ant reference containing a classpath.
     */
    public void setClasspathRef(Reference r) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createClasspath().setRefid(r);
        setChecked(false);
    }

    /** Creates a nested <code>&lt;catalogpath&gt;</code> element.
     * Not allowed if this catalog is itself a reference to another
     * catalog -- that is, a catalog cannot both refer to another
     * <em>and</em> contain elements or other attributes.
     *
     * @return a path to be configured as the catalog path.
     * @exception BuildException
     * if this is a reference and no nested elements are allowed.
     */
    public Path createCatalogPath() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.catalogPath == null) {
            this.catalogPath = new Path(getProject());
        }
        setChecked(false);
        return this.catalogPath.createPath();
    }

    /**
     * Allows catalogpath reference.  Not allowed if this catalog is
     * itself a reference to another catalog -- that is, a catalog
     * cannot both refer to another <em>and</em> contain elements or
     * other attributes.
     *
     * @param r an Ant reference containing a classpath to be used as
     * the catalog path.
     */
    public void setCatalogPathRef(Reference r) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createCatalogPath().setRefid(r);
        setChecked(false);
    }


    /**
     * Returns the catalog path in which to attempt to resolve DTDs.
     *
     * @return the catalog path
     */
    public Path getCatalogPath() {
        return getRef().catalogPath;
    }


    /**
     * Creates the nested <code>&lt;dtd&gt;</code> element.  Not
     * allowed if this catalog is itself a reference to another
     * catalog -- that is, a catalog cannot both refer to another
     * <em>and</em> contain elements or other attributes.
     *
     * @param dtd the information about the PUBLIC resource mapping to
     *            be added to the catalog
     * @exception BuildException if this is a reference and no nested
     *       elements are allowed.
     */
    public void addDTD(ResourceLocation dtd) throws BuildException {
        if (isReference()) {
            throw noChildrenAllowed();
        }

        getElements().addElement(dtd);
        setChecked(false);
    }

    /**
     * Creates the nested <code>&lt;entity&gt;</code> element.    Not
     * allowed if this catalog is itself a reference to another
     * catalog -- that is, a catalog cannot both refer to another
     * <em>and</em> contain elements or other attributes.
     *
     * @param entity the information about the URI resource mapping to be
     *       added to the catalog.
     * @exception BuildException if this is a reference and no nested
     *       elements are allowed.
     */
    public void addEntity(ResourceLocation entity) throws BuildException {
        addDTD(entity);
    }

    /**
     * Loads a nested <code>&lt;xmlcatalog&gt;</code> into our
     * definition.  Not allowed if this catalog is itself a reference
     * to another catalog -- that is, a catalog cannot both refer to
     * another <em>and</em> contain elements or other attributes.
     *
     * @param catalog Nested XMLCatalog
     */
    public void addConfiguredXMLCatalog(XMLCatalog catalog) {
        if (isReference()) {
            throw noChildrenAllowed();
        }

        Vector newElements = catalog.getElements();
        Vector ourElements = getElements();
        Enumeration e = newElements.elements();
        while (e.hasMoreElements()) {
            ourElements.addElement(e.nextElement());
        }

        Path nestedClasspath = catalog.getClasspath();
        createClasspath().append(nestedClasspath);

        Path nestedCatalogPath = catalog.getCatalogPath();
        createCatalogPath().append(nestedCatalogPath);
        setChecked(false);
    }

    /**
     * Makes this instance in effect a reference to another XMLCatalog
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.  That is, a catalog
     * cannot both refer to another <em>and</em> contain elements or
     * attributes.</p>
     *
     * @param r the reference to which this catalog instance is associated
     * @exception BuildException if this instance already has been configured.
     */
    public void setRefid(Reference r) throws BuildException {
        if (!elements.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /**
     * Implements the EntityResolver.resolveEntity() interface method.
     *
     * @see org.xml.sax.EntityResolver#resolveEntity
     */
    public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException {

        if (isReference()) {
            return getRef().resolveEntity(publicId, systemId);
        }

        if (!isChecked()) {
            Stack stk = new Stack();
            stk.push(this);
            dieOnCircularReference(stk, getProject());
        }

        log("resolveEntity: '" + publicId + "': '" + systemId + "'",
            Project.MSG_DEBUG);

        InputSource inputSource =
            getCatalogResolver().resolveEntity(publicId, systemId);

        if (inputSource == null) {
            log("No matching catalog entry found, parser will use: '"
                + systemId + "'", Project.MSG_DEBUG);
        }

        return inputSource;
    }

    /**
     * Implements the URIResolver.resolve() interface method.
     *
     * @see javax.xml.transform.URIResolver#resolve
     */
    public Source resolve(String href, String base)
        throws TransformerException {

        if (isReference()) {
            return getRef().resolve(href, base);
        }

        if (!isChecked()) {
            Stack stk = new Stack();
            stk.push(this);
            dieOnCircularReference(stk, getProject());
        }

        SAXSource source = null;

        String uri = removeFragment(href);

        log("resolve: '" + uri + "' with base: '" + base + "'", Project.MSG_DEBUG);

        source = (SAXSource) getCatalogResolver().resolve(uri, base);

        if (source == null) {
            log("No matching catalog entry found, parser will use: '"
                + href + "'", Project.MSG_DEBUG);
            source = new SAXSource();
            URL baseURL = null;
            try {
                if (base == null) {
                    baseURL = fileUtils.getFileURL(getProject().getBaseDir());
                } else {
                    baseURL = new URL(base);
                }
                URL url = (uri.length() == 0 ? baseURL : new URL(baseURL, uri));
                source.setInputSource(new InputSource(url.toString()));
            } catch (MalformedURLException ex) {
                source.setInputSource(new InputSource(uri));
            }
        }

        setEntityResolver(source);
        return source;
    }

    /**
     * @since Ant 1.6
     */
    private XMLCatalog getRef() {
        if (!isReference()) {
            return this;
        }
        return (XMLCatalog) getCheckedRef(XMLCatalog.class, "xmlcatalog");
    }

    /**
     * The instance of the CatalogResolver strategy to use.
     */
    private CatalogResolver catalogResolver = null;

    /**
     * Factory method for creating the appropriate CatalogResolver
     * strategy implementation.
     * <p> Until we query the classpath, we don't know whether the Apache
     * resolver (Norm Walsh's library from xml-commons) is available or not.
     * This method determines whether the library is available and creates the
     * appropriate implementation of CatalogResolver based on the answer.</p>
     * <p>This is an application of the Gang of Four Strategy Pattern
     * combined with Template Method.</p>
     */
    private CatalogResolver getCatalogResolver() {

        if (catalogResolver == null) {

            AntClassLoader loader = null;

            loader = getProject().createClassLoader(Path.systemClasspath);

            try {
                Class clazz = Class.forName(APACHE_RESOLVER, true, loader);

                ClassLoader apacheResolverLoader = clazz.getClassLoader();

                Class baseResolverClass
                    = Class.forName(CATALOG_RESOLVER, true, apacheResolverLoader);

                ClassLoader baseResolverLoader
                    = baseResolverClass.getClassLoader();

                clazz = Class.forName(APACHE_RESOLVER, true, baseResolverLoader);

                Object obj  = clazz.newInstance();
                catalogResolver = new ExternalResolver(clazz, obj);
            } catch (Throwable ex) {
                catalogResolver = new InternalResolver();
                if (getCatalogPath() != null
                    && getCatalogPath().list().length != 0) {
                        log("Warning: catalogpath listing external catalogs"
                            + " will be ignored", Project.MSG_WARN);
                    }
                log("Failed to load Apache resolver: " + ex, Project.MSG_DEBUG);
            }
        }
        return catalogResolver;
    }

    /**
     * <p>This is called from the URIResolver to set an EntityResolver
     * on the SAX parser to be used for new XML documents that are
     * encountered as a result of the document() function, xsl:import,
     * or xsl:include.  This is done because the XSLT processor calls
     * out to the SAXParserFactory itself to create a new SAXParser to
     * parse the new document.  The new parser does not automatically
     * inherit the EntityResolver of the original (although arguably
     * it should).  See below:</p>
     *
     * <tt>"If an application wants to set the ErrorHandler or
     * EntityResolver for an XMLReader used during a transformation,
     * it should use a URIResolver to return the SAXSource which
     * provides (with getXMLReader) a reference to the XMLReader"</tt>
     *
     * <p>...quoted from page 118 of the Java API for XML
     * Processing 1.1 specification</p>
     *
     */
    private void setEntityResolver(SAXSource source) throws TransformerException {

        XMLReader reader = source.getXMLReader();
        if (reader == null) {
            SAXParserFactory spFactory = SAXParserFactory.newInstance();
            spFactory.setNamespaceAware(true);
            try {
                reader = spFactory.newSAXParser().getXMLReader();
            } catch (ParserConfigurationException ex) {
                throw new TransformerException(ex);
            } catch (SAXException ex) {
                throw new TransformerException(ex);
            }
        }
        reader.setEntityResolver(this);
        source.setXMLReader(reader);
    }

    /**
     * Find a ResourceLocation instance for the given publicId.
     *
     * @param publicId the publicId of the Resource for which local information
     *        is required.
     * @return a ResourceLocation instance with information on the local location
     *         of the Resource or null if no such information is available.
     */
    private ResourceLocation findMatchingEntry(String publicId) {
        Enumeration e = getElements().elements();
        ResourceLocation element = null;
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (o instanceof ResourceLocation) {
                element = (ResourceLocation) o;
                if (element.getPublicId().equals(publicId)) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * Utility method to remove trailing fragment from a URI.
     * For example,
     *
     * @param uri The URI to process.  It may or may not contain a
     *            fragment.
     * @return The URI sans fragment.
     */
    private String removeFragment(String uri) {
        String result = uri;
        int hashPos = uri.indexOf("#");
        if (hashPos >= 0) {
            result = uri.substring(0, hashPos);
        }
        return result;
    }

    /**
     * Utility method to lookup a ResourceLocation in the filesystem.
     *
     * @return An InputSource for reading the file, or <code>null</code>
     *     if the file does not exist or is not readable.
     */
    private InputSource filesystemLookup(ResourceLocation matchingEntry) {

        String uri = matchingEntry.getLocation();
        uri = uri.replace(File.separatorChar, '/');
        URL baseURL = null;

        if (matchingEntry.getBase() != null) {
            baseURL = matchingEntry.getBase();
        } else {
            try {
                baseURL = fileUtils.getFileURL(getProject().getBaseDir());
            } catch (MalformedURLException ex) {
                throw new BuildException("Project basedir cannot be converted to a URL");
            }
        }

        InputSource source = null;
        URL url = null;
        try {
            url = new URL(baseURL, uri);
        } catch (MalformedURLException ex) {
            File testFile = new File(uri);
            if (testFile.exists() && testFile.canRead()) {
                log("uri : '"
                    + uri + "' matches a readable file", Project.MSG_DEBUG);
                try {
                    url = fileUtils.getFileURL(testFile);
                } catch (MalformedURLException ex1) {
                    throw new BuildException("could not find an URL for :" + testFile.getAbsolutePath());
                }
            } else {
                log("uri : '"
                    + uri + "' does not match a readable file", Project.MSG_DEBUG);

            }
        }

        if (url != null) {
            String fileName = fileUtils.fromURI(url.toString());
            if (fileName != null) {
                log("fileName " + fileName, Project.MSG_DEBUG);
                File resFile = new File(fileName);
                if (resFile.exists() && resFile.canRead()) {
                    try {
                        source = new InputSource(new FileInputStream(resFile));
                        String sysid = JAXPUtils.getSystemId(resFile);
                        source.setSystemId(sysid);
                        log("catalog entry matched a readable file: '"
                            + sysid + "'", Project.MSG_DEBUG);
                    } catch (IOException ex) {
                    }
                }
            }
        }
        return source;
    }

    /**
     * Utility method to lookup a ResourceLocation in the classpath.
     *
     * @return An InputSource for reading the resource, or <code>null</code>
     *    if the resource does not exist in the classpath or is not readable.
     */
    private InputSource classpathLookup(ResourceLocation matchingEntry) {

        InputSource source = null;

        AntClassLoader loader = null;
        Path cp = classpath;
        if (cp != null) {
            cp = classpath.concatSystemClasspath("ignore");
        } else {
            cp = (new Path(getProject())).concatSystemClasspath("last");
        }
        loader = getProject().createClassLoader(cp);

        InputStream is
            = loader.getResourceAsStream(matchingEntry.getLocation());

        if (is != null) {
            source = new InputSource(is);
            URL entryURL = loader.getResource(matchingEntry.getLocation());
            String sysid = entryURL.toExternalForm();
            source.setSystemId(sysid);
            log("catalog entry matched a resource in the classpath: '"
                + sysid + "'", Project.MSG_DEBUG);
        }

        return source;
    }

    /**
     * Utility method to lookup a ResourceLocation in URL-space.
     *
     * @return An InputSource for reading the resource, or <code>null</code>
     *    if the resource does not identify a valid URL or is not readable.
     */
    private InputSource urlLookup(ResourceLocation matchingEntry) {

        String uri = matchingEntry.getLocation();
        URL baseURL = null;

        if (matchingEntry.getBase() != null) {
            baseURL = matchingEntry.getBase();
        } else {
            try {
                baseURL = fileUtils.getFileURL(getProject().getBaseDir());
            } catch (MalformedURLException ex) {
                throw new BuildException("Project basedir cannot be converted to a URL");
            }
        }

        InputSource source = null;
        URL url = null;

        try {
            url = new URL(baseURL, uri);
        } catch (MalformedURLException ex) {
        }

        if (url != null) {
            try {
                InputStream is = url.openStream();
                if (is != null) {
                    source = new InputSource(is);
                    String sysid = url.toExternalForm();
                    source.setSystemId(sysid);
                    log("catalog entry matched as a URL: '"
                        + sysid + "'", Project.MSG_DEBUG);
                }
            } catch (IOException ex) {
            }
        }

        return source;

    }

    /**
     * Interface implemented by both the InternalResolver strategy and
     * the ExternalResolver strategy.
     */
    private interface CatalogResolver extends URIResolver, EntityResolver {

        InputSource resolveEntity(String publicId, String systemId);

        Source resolve(String href, String base) throws TransformerException;
    }

    /**
     * The InternalResolver strategy is used if the Apache resolver
     * library (Norm Walsh's library from xml-commons) is not
     * available.  In this case, external catalog files will be
     * ignored.
     *
     */
    private class InternalResolver implements CatalogResolver {

        public InternalResolver() {
            log("Apache resolver library not found, internal resolver will be used",
                Project.MSG_VERBOSE);
        }

        public InputSource resolveEntity(String publicId,
                                         String systemId) {
            InputSource result = null;
            ResourceLocation matchingEntry = findMatchingEntry(publicId);

            if (matchingEntry != null) {

                log("Matching catalog entry found for publicId: '"
                    + matchingEntry.getPublicId() + "' location: '"
                    + matchingEntry.getLocation() + "'",
                    Project.MSG_DEBUG);

                result = filesystemLookup(matchingEntry);

                if (result == null) {
                    result = classpathLookup(matchingEntry);
                }

                if (result == null) {
                    result = urlLookup(matchingEntry);
                }
            }
            return result;
        }

        public Source resolve(String href, String base)
            throws TransformerException {

            SAXSource result = null;
            InputSource source = null;

            ResourceLocation matchingEntry = findMatchingEntry(href);

            if (matchingEntry != null) {

                log("Matching catalog entry found for uri: '"
                    + matchingEntry.getPublicId() + "' location: '"
                    + matchingEntry.getLocation() + "'",
                    Project.MSG_DEBUG);

                ResourceLocation entryCopy = matchingEntry;
                if (base != null) {
                    try {
                        URL baseURL = new URL(base);
                        entryCopy = new ResourceLocation();
                        entryCopy.setBase(baseURL);
                    } catch (MalformedURLException ex) {
                    }
                }
                entryCopy.setPublicId(matchingEntry.getPublicId());
                entryCopy.setLocation(matchingEntry.getLocation());

                source = filesystemLookup(entryCopy);

                if (source == null) {
                    source = classpathLookup(entryCopy);
                }

                if (source == null) {
                    source = urlLookup(entryCopy);
                }

                if (source != null) {
                    result = new SAXSource(source);
                }
            }
            return result;
        }
    }

    /**
     * The ExternalResolver strategy is used if the Apache resolver
     * library (Norm Walsh's library from xml-commons) is available in
     * the classpath.  The ExternalResolver is a essentially a superset
     * of the InternalResolver.
     *
     */
    private class ExternalResolver implements CatalogResolver {

        private Method setXMLCatalog = null;
        private Method parseCatalog = null;
        private Method resolveEntity = null;
        private Method resolve = null;

        /** The instance of the ApacheCatalogResolver bridge class */
        private Object resolverImpl = null;

        private boolean externalCatalogsProcessed = false;

        public ExternalResolver(Class resolverImplClass,
                              Object resolverImpl) {

            this.resolverImpl = resolverImpl;

            try {
                setXMLCatalog =
                    resolverImplClass.getMethod("setXMLCatalog",
                                                new Class[] {XMLCatalog.class});

                parseCatalog =
                    resolverImplClass.getMethod("parseCatalog",
                                                new Class[] {String.class});

                resolveEntity =
                    resolverImplClass.getMethod("resolveEntity",
                                                new Class[] {String.class, String.class});

                resolve =
                    resolverImplClass.getMethod("resolve",
                                                new Class[] {String.class, String.class});
            } catch (NoSuchMethodException ex) {
                throw new BuildException(ex);
            }

            log("Apache resolver library found, xml-commons resolver will be used",
                Project.MSG_VERBOSE);
        }

        public InputSource resolveEntity(String publicId,
                                         String systemId) {
            InputSource result = null;

            processExternalCatalogs();

            ResourceLocation matchingEntry = findMatchingEntry(publicId);

            if (matchingEntry != null) {

                log("Matching catalog entry found for publicId: '"
                    + matchingEntry.getPublicId() + "' location: '"
                    + matchingEntry.getLocation() + "'",
                    Project.MSG_DEBUG);

                result = filesystemLookup(matchingEntry);

                if (result == null) {
                    result = classpathLookup(matchingEntry);
                }

                if (result == null) {
                    try {
                        result =
                            (InputSource) resolveEntity.invoke(resolverImpl,
                                                              new Object[] {publicId, systemId});
                    } catch (Exception ex) {
                        throw new BuildException(ex);
                    }
                }
            } else {
                try {
                    result =
                        (InputSource) resolveEntity.invoke(resolverImpl,
                                                          new Object[] {publicId, systemId});
                } catch (Exception ex) {
                    throw new BuildException(ex);
                }
            }

            return result;
        }

        public Source resolve(String href, String base)
            throws TransformerException {

            SAXSource result = null;
            InputSource source = null;

            processExternalCatalogs();

            ResourceLocation matchingEntry = findMatchingEntry(href);

            if (matchingEntry != null) {

                log("Matching catalog entry found for uri: '"
                    + matchingEntry.getPublicId() + "' location: '"
                    + matchingEntry.getLocation() + "'",
                    Project.MSG_DEBUG);

                ResourceLocation entryCopy = matchingEntry;
                if (base != null) {
                    try {
                        URL baseURL = new URL(base);
                        entryCopy = new ResourceLocation();
                        entryCopy.setBase(baseURL);
                    } catch (MalformedURLException ex) {
                    }
                }
                entryCopy.setPublicId(matchingEntry.getPublicId());
                entryCopy.setLocation(matchingEntry.getLocation());

                source = filesystemLookup(entryCopy);

                if (source == null) {
                    source = classpathLookup(entryCopy);
                }

                if (source != null) {
                    result = new SAXSource(source);
                } else {
                    try {
                        result =
                            (SAXSource) resolve.invoke(resolverImpl,
                                                      new Object[] {href, base});
                    } catch (Exception ex) {
                        throw new BuildException(ex);
                    }
                }
            } else {
                try {
                    result =
                        (SAXSource) resolve.invoke(resolverImpl,
                                                  new Object[] {href, base});
                } catch (Exception ex) {
                    throw new BuildException(ex);
                }
            }
            return result;
        }

        /**
         * Process each external catalog file specified in a
         * <code>&lt;catalogpath&gt;</code>.  It will be
         * parsed by the resolver library, and the individual elements
         * will be added back to us (that is, the controlling
         * XMLCatalog instance) via a callback mechanism.
         */
        private void processExternalCatalogs() {

            if (!externalCatalogsProcessed) {

                try {
                    setXMLCatalog.invoke(resolverImpl,
                                         new Object[] {XMLCatalog.this});
                } catch (Exception ex) {
                    throw new BuildException(ex);
                }

                Path catPath = getCatalogPath();
                if (catPath != null) {
                    log("Using catalogpath '" + getCatalogPath() + "'",
                        Project.MSG_DEBUG);
                    String[] catPathList = getCatalogPath().list();

                    for (int i = 0; i < catPathList.length; i++) {
                        File catFile = new File(catPathList[i]);
                        log("Parsing " + catFile, Project.MSG_DEBUG);
                        try {
                            parseCatalog.invoke(resolverImpl,
                                    new Object[] {catFile.getPath()});
                        } catch (Exception ex) {
                            throw new BuildException(ex);
                        }
                    }
                }
            }
            externalCatalogsProcessed = true;
        }
    }
