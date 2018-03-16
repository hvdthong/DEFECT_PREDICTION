package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLTemplateElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLTemplateElementImpl extends WMLElementImpl implements WMLTemplateElement {

  public WMLTemplateElementImpl (WMLDocumentImpl owner, String tagName) {
    super( owner, tagName);
  }

  public void setOnTimer(String newValue) {
    setAttribute("ontimer", newValue);
  }

  public String getOnTimer() {
    return getAttribute("ontimer");
  }

  public void setOnEnterBackward(String newValue) {
    setAttribute("onenterbackward", newValue);
  }

  public String getOnEnterBackward() {
    return getAttribute("onenterbackward");
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

  public void setOnEnterForward(String newValue) {
    setAttribute("onenterforward", newValue);
  }

  public String getOnEnterForward() {
    return getAttribute("onenterforward");
  }

}
