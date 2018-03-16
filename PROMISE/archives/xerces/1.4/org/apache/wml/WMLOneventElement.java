package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * @version $Id: WMLOneventElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLOneventElement extends WMLElement {

  public void setType(String newValue);

  public String getType();

}
