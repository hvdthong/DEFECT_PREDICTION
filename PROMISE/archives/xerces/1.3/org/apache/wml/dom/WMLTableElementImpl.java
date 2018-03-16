package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLTableElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLTableElementImpl extends WMLElementImpl implements WMLTableElement {

  public WMLTableElementImpl (WMLDocumentImpl owner, String tagName) {
    super( owner, tagName);
  }

  public void setColumns(int newValue) {
    setAttribute("columns", newValue);
  }

  public int getColumns() {
    return getAttribute("columns", 0);
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

  public void setAlign(String newValue) {
    setAttribute("align", newValue);
  }

  public String getAlign() {
    return getAttribute("align");
  }

  public void setTitle(String newValue) {
    setAttribute("title", newValue);
  }

  public String getTitle() {
    return getAttribute("title");
  }

  public void setId(String newValue) {
    setAttribute("id", newValue);
  }

  public String getId() {
    return getAttribute("id");
  }

}
