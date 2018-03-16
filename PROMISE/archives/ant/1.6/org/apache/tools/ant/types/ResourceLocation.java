package org.apache.tools.ant.types;

import java.net.URL;

/**
 * <p>Helper class to handle the <code>&lt;dtd&gt;</code> and
 * <code>&lt;entity&gt;</code> nested elements.  These correspond to
 * the <code>PUBLIC</code> and <code>URI</code> catalog entry types,
 * respectively, as defined in the <a
 * OASIS "Open Catalog" standard</a>.</p>
 *
 * <p>Possible Future Enhancements:
 * <ul>
 * <li>Bring the Ant element names into conformance with the OASIS standard</li>
 * <li>Add support for additional OASIS catalog entry types</li>
 * </ul>
 * </p>
 *
 * @see org.apache.xml.resolver.Catalog
 * @since Ant 1.6
 */
public class ResourceLocation {


    /**
     * name of the catalog entry type, as per OASIS spec.
     */
    private String name = null;

    /** publicId of the dtd/entity. */
    private String publicId = null;

    /** location of the dtd/entity - a file/resource/URL. */
    private String location = null;

    /**
     * base URL of the dtd/entity, or null. If null, the Ant project
     * basedir is assumed.  If the location specifies a relative
     * URL/pathname, it is resolved using the base.  The default base
     * for an external catalog file is the directory in which it is
     * located.
     */
    private URL base = null;


    /**
     * @param publicId uniquely identifies the resource.
     */
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    /**
     * @param location the location of the resource associated with the
     *      publicId.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @param base the base URL of the resource associated with the
     * publicId.  If the location specifies a relative URL/pathname,
     * it is resolved using the base.  The default base for an
     * external catalog file is the directory in which it is located.
     */
    public void setBase(URL base) {
        this.base = base;
    }

    /**
     * @return the publicId of the resource.
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * @return the location of the resource identified by the publicId.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the base of the resource identified by the publicId.
     */
    public URL getBase() {
        return base;
    }

