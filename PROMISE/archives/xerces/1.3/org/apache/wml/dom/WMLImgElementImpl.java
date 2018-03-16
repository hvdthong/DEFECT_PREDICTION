package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLImgElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLImgElementImpl extends WMLElementImpl implements WMLImgElement {

  public WMLImgElementImpl (WMLDocumentImpl owner, String tagName) {
    super( owner, tagName);
  }

  public void setWidth(String newValue) {
    setAttribute("width", newValue);
  }

  public String getWidth() {
    return getAttribute("width");
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

  public void setLocalSrc(String newValue) {
    setAttribute("localsrc", newValue);
  }

  public String getLocalSrc() {
    return getAttribute("localsrc");
  }

  public void setHeight(String newValue) {
    setAttribute("height", newValue);
  }

  public String getHeight() {
    return getAttribute("height");
  }

  public void setAlign(String newValue) {
    setAttribute("align", newValue);
  }

  public String getAlign() {
    return getAttribute("align");
  }

  public void setVspace(String newValue) {
    setAttribute("vspace", newValue);
  }

  public String getVspace() {
    return getAttribute("vspace");
  }

  public void setAlt(String newValue) {
    setAttribute("alt", newValue);
  }

  public String getAlt() {
    return getAttribute("alt");
  }

  public void setId(String newValue) {
    setAttribute("id", newValue);
  }

  public String getId() {
    return getAttribute("id");
  }

  public void setHspace(String newValue) {
    setAttribute("hspace", newValue);
  }

  public String getHspace() {
    return getAttribute("hspace");
  }

  public void setSrc(String newValue) {
    setAttribute("src", newValue);
  }

  public String getSrc() {
    return getAttribute("src");
  }

}
