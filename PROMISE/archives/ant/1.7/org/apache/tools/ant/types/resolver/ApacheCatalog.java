package org.apache.tools.ant.types.resolver;

import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogEntry;

import org.apache.xml.resolver.helpers.PublicId;

/**
 * This class extends the Catalog class provided by Norman Walsh's
 * resolver library in xml-commons in order to add classpath entity
 * and URI resolution.  Since XMLCatalog already does classpath
 * resolution, we simply add all CatalogEntry instances back to the
 * controlling XMLCatalog instance.  This is done via a callback
 * mechanism.  ApacheCatalog is <em>only</em> used for external
 * catalog files.  Inline entries (currently <code>&lt;dtd&gt;</code>
 * and <code>&lt;entity&gt;</code>) are not added to ApacheCatalog.
 * See XMLCatalog.java for the details of the entity and URI
 * resolution algorithms.
 *
 * @see org.apache.tools.ant.types.XMLCatalog.CatalogResolver
 * @since Ant 1.6
 */
public class ApacheCatalog extends Catalog {

    /** The resolver object to callback. */
    private ApacheCatalogResolver resolver = null;

    /**
     * <p>Create a new ApacheCatalog instance.</p>
     *
     * <p>This method overrides the superclass method of the same name
     *  in order to set the resolver object for callbacks.  The reason
     *  we have to do this is that internally Catalog creates a new
     *  instance of itself for each external catalog file processed.
     *  That is, if two external catalog files are processed, there
     *  will be a total of two ApacheCatalog instances, and so on.</p>
     * @return the catalog.
     */
    protected Catalog newCatalog() {
        ApacheCatalog cat = (ApacheCatalog) super.newCatalog();
        cat.setResolver(resolver);
        return cat;
    }

    /**
     * Set the resolver object to callback.
     * @param resolver the apache catalog resolver.
     */
    public void setResolver(ApacheCatalogResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * <p>This method overrides the superclass method of the same name
     * in order to add catalog entries back to the controlling
     * XMLCatalog instance.  In this way, we can add classpath lookup
     * for these entries.</p>
     *
     * <p>When we add an external catalog file, the entries inside it
     * get parsed by this method.  Therefore, we override it to add
     * each of them back to the controlling XMLCatalog instance.  This
     * is done by performing a callback to the ApacheCatalogResolver,
     * which in turn calls the XMLCatalog.</p>
     *
     * <p>XMLCatalog currently only understands <code>PUBLIC</code>
     * and <code>URI</code> entry types, so we ignore the other types.</p>
     *
     * @param entry The CatalogEntry to process.
     */
    public void addEntry(CatalogEntry entry) {

        int type = entry.getEntryType();

        if (type == PUBLIC) {

            String publicid = PublicId.normalize(entry.getEntryArg(0));
            String systemid = normalizeURI(entry.getEntryArg(1));

            if (resolver == null) {
                catalogManager.debug
                    .message(1, "Internal Error: null ApacheCatalogResolver");
            } else {
                resolver.addPublicEntry(publicid, systemid, base);
            }

        } else if (type == URI) {

            String uri = normalizeURI(entry.getEntryArg(0));
            String altURI = normalizeURI(entry.getEntryArg(1));

            if (resolver == null) {
                catalogManager.debug
                    .message(1, "Internal Error: null ApacheCatalogResolver");
            } else {
                resolver.addURIEntry(uri, altURI, base);
            }

        }

        super.addEntry(entry);
    }

