package org.w3c.dom.html;

/**
 *  Style information. See the  STYLE element definition in HTML 4.0, the  
 * module and the <code>LinkStyle</code> interface in the  module. 
 */
public interface HTMLStyleElement extends HTMLElement {
    /**
     *  Enables/disables the style sheet. 
     */
    public boolean getDisabled();
    public void setDisabled(boolean disabled);

    /**
     *  Designed for use with one or more target media. See the  media 
     * attribute definition in HTML 4.0.
     */
    public String getMedia();
    public void setMedia(String media);

    /**
     *  The content type pf the style sheet language. See the  type attribute 
     * definition in HTML 4.0.
     */
    public String getType();
    public void setType(String type);

}

