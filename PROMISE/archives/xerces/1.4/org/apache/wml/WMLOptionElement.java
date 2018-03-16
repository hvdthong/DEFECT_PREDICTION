package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>'option' element specifies a choice in a 'select' element</p>
 *
 * @version $Id: WMLOptionElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLOptionElement extends WMLElement {

    /**
     * 'value' specifies the value to used to set the 'name' variable
     * (Section 11.6.2.2, WAP WML Version 16-Jun-1999)
     */
    public void setValue(String newValue);
    public String getValue();

    /**
     * 'title' specifies a title for this element.
     * (Section 11.6.2.2, WAP WML Version 16-Jun-1999)
     */
    public void setTitle(String newValue);
    public String getTitle();

    /**
     * 'onpick' specifies a event to occur when a user select and
     * disselect this choice.
     * (Section 11.6.2.2, WAP WML Version 16-Jun-1999) */
    public void setOnPick(String href);
    public String getOnPick();

    /**
     * 'xml:lang' specifics the natural or formal language in which
     * the document is written.  
     * (Section 8.8, WAP WML Version 16-Jun-1999) 
     */
    public void setXmlLang(String newValue);
    public String getXmlLang();
}
