package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>'img' specifies an image in a text flow
 * (Section 11.9, WAP WML Version 16-Jun-1999)</p>
 *
 * @version $Id: WMLImgElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */
public interface WMLImgElement extends WMLElement {

    /**
     * 'alt' specifies an alternative text for the image
     * (Section 11.9, WAP WML Version 16-Jun-1999)
     */
    public void setAlt(String newValue);
    public String getAlt();

    /**
     * 'src' specifies URI for the source images
     * (Section 11.9, WAP WML Version 16-Jun-1999)
     */
    public void setSrc(String newValue);
    public String getSrc();

    /**
     * 'localsrc' specifies an alternative internal representation of
     * the image.
     * (Section 11.9, WAP WML Version 16-Jun-1999) 
     */
    public void setLocalSrc(String newValue);
    public String getLocalSrc();

    /**
     * 'vspace' specifies the abount of white space to be inserted
     * above and below
     * (Section 11.9, WAP WML Version 16-Jun-1999) 
     */
    public void setVspace(String newValue);
    public String getVspace();

    /**
     * 'hspace' specifies the abount of white space to be inserted
     * left and right
     * (Section 11.9, WAP WML Version 16-Jun-1999) 
     */
    public void setHspace(String newValue);
    public String getHspace();

    /**
     * 'align' specifies the alignment of the image within the text
     * flow.
     * (Section 11.8, WAP WML Version 16-Jun-1999)
     */
    public void setAlign(String newValue);
    public String getAlign();

    /**
     * 'width' specifies the width of an image.
     * (Section 11.9, WAP WML Version 16-Jun-1999)
     */
    public void setWidth(String newValue);
    public String getWidth();

    /**
     * 'height' specifies the height of an image.
     * (Section 11.9, WAP WML Version 16-Jun-1999)
     */
    public void setHeight(String newValue);
    public String getHeight();

    /**
     * The xml:lang that specifics the natural or formal language in
     * which the document is written.
     * (Section 8.8, WAP WML Version 16-Jun-1999)
     */
    public void setXmlLang(String newValue);
    public String getXmlLang();
}
