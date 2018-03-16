package org.apache.xml.utils;

import java.io.Serializable;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * <meta name="usage" content="advanced"/>
 * Mutable version of AttributesImpl.
 */
public class MutableAttrListImpl extends AttributesImpl
        implements Serializable
{

/**
 * Construct a new, empty AttributesImpl object.
 */

public MutableAttrListImpl()
  {
    super();
  }

  /**
   * Copy an existing Attributes object.
   *
   * <p>This constructor is especially useful inside a start
   * element event.</p>
   *
   * @param atts The existing Attributes object.
   */
  public MutableAttrListImpl(Attributes atts)
  {
    super(atts);
  }

  /**
   * Add an attribute to the end of the list.
   *
   * <p>For the sake of speed, this method does no checking
   * to see if the attribute is already in the list: that is
   * the responsibility of the application.</p>
   *
   * @param uri The Namespace URI, or the empty string if
   *        none is available or Namespace processing is not
   *        being performed.
   * @param localName The local name, or the empty string if
   *        Namespace processing is not being performed.
   * @param qName The qualified (prefixed) name, or the empty string
   *        if qualified names are not available.
   * @param type The attribute type as a string.
   * @param value The attribute value.
   */
  public void addAttribute(String uri, String localName, String qName,
                           String type, String value)
  {

    if (null == uri)
      uri = "";

    int index = this.getIndex(qName);
   

    if (index >= 0)
      this.setAttribute(index, uri, localName, qName, type, value);
    else
      super.addAttribute(uri, localName, qName, type, value);
  }

  /**
   * Add the contents of the attribute list to this list.
   *
   * @param atts List of attributes to add to this list
   */
  public void addAttributes(Attributes atts)
  {

    int nAtts = atts.getLength();

    for (int i = 0; i < nAtts; i++)
    {
      String uri = atts.getURI(i);

      if (null == uri)
        uri = "";

      String localName = atts.getLocalName(i);
      String qname = atts.getQName(i);
      int index = this.getIndex(uri, localName);
      if (index >= 0)
        this.setAttribute(index, uri, localName, qname, atts.getType(i),
                          atts.getValue(i));
      else
        addAttribute(uri, localName, qname, atts.getType(i),
                     atts.getValue(i));
    }
  }

  /**
   * Return true if list contains the given (raw) attribute name.
   *
   * @param name Raw name of attribute to look for 
   *
   * @return true if an attribute is found with this name
   */
  public boolean contains(String name)
  {
    return getValue(name) != null;
  }
}

