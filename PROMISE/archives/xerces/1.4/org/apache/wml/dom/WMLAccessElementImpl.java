package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLAccessElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLAccessElementImpl extends WMLElementImpl implements WMLAccessElement {

  public WMLAccessElementImpl (WMLDocumentImpl owner, String tagName) {
    super( owner, tagName);
  }

  public void setClassName(String newValue) {
    setAttribute("class", newValue);
  }

  public String getClassName() {
    return getAttribute("class");
  }

  public void setDomain(String newValue) {
    setAttribute("domain", newValue);
  }

  public String getDomain() {
    return getAttribute("domain");
  }

  public void setId(String newValue) {
    setAttribute("id", newValue);
  }

  public String getId() {
    return getAttribute("id");
  }

  public void setPath(String newValue) {
    setAttribute("path", newValue);
  }

  public String getPath() {
    return getAttribute("path");
  }

}
