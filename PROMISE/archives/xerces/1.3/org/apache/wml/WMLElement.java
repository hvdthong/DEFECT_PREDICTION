package org.apache.wml;

import org.w3c.dom.Element;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>All WML Elements are derived from this class that contains two
 * core attributes defined in the DTD.</p>
 *
 * @version $Id: WMLElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLElement extends Element {

    /**
     * The element's identifier which is unique in a single deck. 
     * (Section 8.9, WAP WML Version 16-Jun-1999)
     */
    public void setId(String newValue);
    public String getId();

    /**
     * The 'class' attribute of a element that affiliates an elements
     * with one or more elements.
     * (Section 8.9, WAP WML Version 16-Jun-1999)
     */
    public void setClassName(String newValue);
    public String getClassName();
}
