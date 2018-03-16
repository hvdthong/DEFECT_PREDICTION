package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * @version $Id: WMLSetvarElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLSetvarElement extends WMLElement {

  public void setValue(String newValue);

  public String getValue();

  public void setName(String newValue);

  public String getName();

}
