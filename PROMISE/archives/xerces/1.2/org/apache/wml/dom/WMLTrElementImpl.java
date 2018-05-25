package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLTrElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLTrElementImpl extends WMLElementImpl implements WMLTrElement {

  public WMLTrElementImpl (WMLDocumentImpl owner, String tagName) {
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