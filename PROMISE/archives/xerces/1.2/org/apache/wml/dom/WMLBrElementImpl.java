package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLBrElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLBrElementImpl extends WMLElementImpl implements WMLBrElement {

  public WMLBrElementImpl (WMLDocumentImpl owner, String tagName) {
    super( owner, tagName);
  }

  public void setClassName(String newValue) {
    setAttribute("class", newValue);
  }

  public String getClassName() {
    return getAttribute("class");
  }

  public void setXmlLang(String newValue) {
    setAttribute("xml:lang", newValue);
  }

  public String getXmlLang() {
    return getAttribute("xml:lang");
  }

  public void setId(String newValue) {
    setAttribute("id", newValue);
  }

  public String getId() {
    return getAttribute("id");
  }

}
