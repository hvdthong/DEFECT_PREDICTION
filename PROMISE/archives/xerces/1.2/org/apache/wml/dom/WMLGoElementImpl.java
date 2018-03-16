package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLGoElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLGoElementImpl extends WMLElementImpl implements WMLGoElement {

  public WMLGoElementImpl (WMLDocumentImpl owner, String tagName) {
    super( owner, tagName);
  }

  public void setSendreferer(String newValue) {
    setAttribute("sendreferer", newValue);
  }

  public String getSendreferer() {
    return getAttribute("sendreferer");
  }

  public void setAcceptCharset(String newValue) {
    setAttribute("accept-charset", newValue);
  }

  public String getAcceptCharset() {
    return getAttribute("accept-charset");
  }

  public void setHref(String newValue) {
    setAttribute("href", newValue);
  }

  public String getHref() {
    return getAttribute("href");
  }

  public void setClassName(String newValue) {
    setAttribute("class", newValue);
  }

  public String getClassName() {
    return getAttribute("class");
  }

  public void setId(String newValue) {
    setAttribute("id", newValue);
  }

  public String getId() {
    return getAttribute("id");
  }

  public void setMethod(String newValue) {
    setAttribute("method", newValue);
  }

  public String getMethod() {
    return getAttribute("method");
  }

}
