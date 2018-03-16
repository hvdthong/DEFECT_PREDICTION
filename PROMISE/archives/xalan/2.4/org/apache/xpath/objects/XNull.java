package org.apache.xpath.objects;


import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;

import org.apache.xpath.XPathContext;
import org.apache.xpath.NodeSetDTM;

/**
 * <meta name="usage" content="general"/>
 * This class represents an XPath null object, and is capable of
 * converting the null to other types, such as a string.
 */
public class XNull extends XNodeSet
{

  /**
   * Create an XObject.
   */
  public XNull()
  {
    super();
  }

  /**
   * Tell what kind of class this is.
   *
   * @return type CLASS_NULL
   */
  public int getType()
  {
    return CLASS_NULL;
  }

  /**
   * Given a request type, return the equivalent string.
   * For diagnostic purposes.
   *
   * @return type string "#CLASS_NULL"
   */
  public String getTypeString()
  {
    return "#CLASS_NULL";
  }

  /**
   * Cast result object to a number.
   * 
   * @return 0.0
   */

  public double num()
  {
    return 0.0;
  }

  /**
   * Cast result object to a boolean.
   *
   * @return false
   */
  public boolean bool()
  {
    return false;
  }

  /**
   * Cast result object to a string.
   *
   * @return empty string ""
   */
  public String str()
  {
    return "";
  }

  /**
   * Cast result object to a result tree fragment.
   *
   * @param support XPath context to use for the conversion
   *
   * @return The object as a result tree fragment.
   */
  public int rtf(XPathContext support)
  {
    return DTM.NULL;
  }


  /**
   * Tell if two objects are functionally equal.
   *
   * @param obj2 Object to compare this to
   *
   * @return True if the given object is of type CLASS_NULL
   */
  public boolean equals(XObject obj2)
  {
    return obj2.getType() == CLASS_NULL;
  }
}
