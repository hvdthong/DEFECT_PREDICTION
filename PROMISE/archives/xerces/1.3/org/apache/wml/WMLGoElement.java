package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * @version $Id: WMLGoElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLGoElement extends WMLElement {

  public void setSendreferer(String newValue);

  public String getSendreferer();

  public void setAcceptCharset(String newValue);

  public String getAcceptCharset();

  public void setHref(String newValue);

  public String getHref();

  public void setMethod(String newValue);

  public String getMethod();

}
