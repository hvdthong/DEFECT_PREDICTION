package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>'table' create a set of aligned columns of text and images.
 * (Section 11.8.5, WAP WML Version 16-Jun-1999)</p>
 *
 * @version $Id: WMLTableElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLTableElement extends WMLElement {

    /**
     * 'title' specifies a title for the table
     * (Section 11.8.5, WAP WML Version 16-Jun-1999)
     */
    public void setTitle(String newValue);
    public String getTitle();

    /**
     * 'align' set the align of the table
     * (Section 11.8.5, WAP WML Version 16-Jun-1999)
     */
    public void setAlign(String newValue);
    public String getAlign();

    /**
     * 'columns' specifies the number of columns
     * (Section 11.8.5, WAP WML Version 16-Jun-1999)
     */
    public void setColumns(int newValue);
    public int getColumns();

    /**
     * The xml:lang that specifics the natural or formal language in
     * which the document is written.
     * (Section 8.8, WAP WML Version 16-Jun-1999)
     */
    public void setXmlLang(String newValue);
    public String getXmlLang();
}
