package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLHeadElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLHeadElementImpl extends WMLElementImpl implements WMLHeadElement {

  public WMLHeadElementImpl (WMLDocumentImpl owner, String tagName) {
    super( owner, tagName);
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

}
