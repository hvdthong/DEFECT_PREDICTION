package org.apache.tools.ant.types;

/**
 * Helper class to handle the DTD and Entity nested elements.
 *
 * @author Conor MacNeill
 * @author dIon Gillard
 */
public class DTDLocation {
    /** publicId of the dtd/entity */
    private String publicId = null;
    /** location of the dtd/entity - a file/resource/URL */
    private String location = null;

    /**
     * @param publicId uniquely identifies the resource
     */
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    /**
     * @param location the location of the resource associated with the
     *      publicId
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the publicId
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * @return the location of the resource identified by the publicId
     */
    public String getLocation() {
        return location;
    }
}

