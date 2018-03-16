package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>'p' element specifies a paragraph
 * (Section 11.8.3, WAP WML Version 16-Jun-1999)</p>
 *
 * @version $Id: WMLPElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLPElement extends WMLElement {

    /**
     * 'mode' specifies the wrapping mode of the paragraph.
     * The legal values are 'wrap' and 'nowrap'
     * (Section 11.8.3, WAP WML Version 16-Jun-1999)
     */
    public void setMode(String newValue);
    public String getMode();

    /**
     * 'align' specifies the align of teh paragraph
     * The legal values are 'left,' 'center,' and 'right'
     * (Section 11.8.3, WAP WML Version 16-Jun-1999)
     */
    public void setAlign(String newValue);
    public String getAlign();

    /**
     * The xml:lang that specifics the natural or formal language in
     * which the document is written.
     * (Section 8.8, WAP WML Version 16-Jun-1999)
     */
    public void setXmlLang(String newValue);
    public String getXmlLang();
}
