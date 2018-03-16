package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>The meta element contains meta-info of an WML deck
 * (Section 11.3.2, WAP WML Version 16-Jun-1999)</p>
 *
 * @version $Id: WMLMetaElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */
public interface WMLMetaElement extends WMLElement {

    /**
     * 'name' attribute specific the property name
     */
    public void setName(String newValue);
    public String getName();

    /**
     * 'http-equiv' attribute indicates the property should be
     * interpret as HTTP header.
     */
    public void setHttpEquiv(String newValue);
    public String getHttpEquiv();

    /**
     * 'forua' attribute specifies whether a intermediate agent should
     * remove this meta element. A value of false means the
     * intermediate agent must remove the element.
     */
    public void setForua(boolean newValue);
    public boolean getForua();

    /**
     * 'scheme' attribute specifies a form that may be used to
     * interpret the property value 
     */
    public void setScheme(String newValue);
    public String getScheme();

    /**
     * 'content' attribute specifies the property value 
     */
    public void setContent(String newValue);
    public String getContent();
}
