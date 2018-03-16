package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>'access' element specifics the access control for the entire deck
 * (Section 11.3.1, WAP WML Version 16-Jun-1999)</p>
 *
 * @version $Id: WMLAccessElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLAccessElement extends WMLElement {

    /**
     * A deck's domain and path attributes specify which deck may
     * access it.  
     *
     * domain attribute is suffix-matched against the domain name
     * portion of the referring URI 
     */
    public void setDomain(String newValue);
    public String getDomain();

    /**
     * path attribute is prefix-matched against the path portion of
     * the referring URI 
     */
    public void setPath(String newValue);
    public String getPath();
}
