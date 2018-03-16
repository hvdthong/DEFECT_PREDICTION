package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @version $Id: WMLSelectElementImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLSelectElementImpl extends WMLElementImpl implements WMLSelectElement {

    public WMLSelectElementImpl (WMLDocumentImpl owner, String tagName) {
	super( owner, tagName);
    }

    public void setMultiple(boolean newValue) {
	setAttribute("multiple", newValue);
    }

    public boolean getMultiple() {
	return getAttribute("multiple", false);
    }

    public void setValue(String newValue) {
	setAttribute("value", newValue);
    }

    public String getValue() {
	return getAttribute("value");
    }

    public void setTabIndex(int newValue) {
	setAttribute("tabindex", newValue);
    }

    public int getTabIndex() {
	return getAttribute("tabindex", 0);
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

    public void setTitle(String newValue) {
	setAttribute("title", newValue);
    }

    public String getTitle() {
	return getAttribute("title");
    }

    public void setIValue(String newValue) {
	setAttribute("ivalue", newValue);
    }

    public String getIValue() {
	return getAttribute("ivalue");
    }

    public void setId(String newValue) {
	setAttribute("id", newValue);
    }

    public String getId() {
	return getAttribute("id");
    }

    public void setIName(String newValue) {
	setAttribute("iname", newValue);
    }

    public String getIName() {
	return getAttribute("iname");
    }

    public void setName(String newValue) {
	setAttribute("name", newValue);
    }

    public String getName() {
	return getAttribute("name");
    }
}
