package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLSetvarElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLSetvarElementImpl extends WMLElementImpl implements WMLSetvarElement {

  public WMLSetvarElementImpl (WMLDocumentImpl owner, String tagName) {
    super( owner, tagName);
  }

  public void setValue(String newValue) {
    setAttribute("value", newValue);
  }

  public String getValue() {
    return getAttribute("value");
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

  public void setName(String newValue) {
    setAttribute("name", newValue);
  }

  public String getName() {
    return getAttribute("name");
  }

}
