package org.apache.xalan.lib;

import org.apache.xml.dtm.ref.DTMNodeProxy;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The base class for some EXSLT extension classes.
 * It contains common utility methods to be used by the sub-classes.
 */
public abstract class ExsltBase
{
  /**
   * Return the string value of a Node
   *
   * @param n The Node.
   * @return The string value of the Node
   */
  protected static String toString(Node n)
  {
    if (n instanceof DTMNodeProxy)
  	 return ((DTMNodeProxy)n).getStringValue();
    else
    {
      String value = n.getNodeValue();
      if (value == null)
      {
        NodeList nodelist = n.getChildNodes();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < nodelist.getLength(); i++)
        {
          Node childNode = nodelist.item(i);
          buf.append(toString(childNode));
        }
        return buf.toString();
      }
      else
        return value;
    }
  }
  
  /**
   * Convert the string value of a Node to a number.
   * Return NaN if the string is not a valid number.
   *
   * @param n The Node.
   * @return The number value of the Node
   */
  protected static double toNumber(Node n)
  {
    double d = 0.0;
    String str = toString(n);
    try
    {
      d = Double.valueOf(str).doubleValue();
    }
    catch (NumberFormatException e)
    {
      d= Double.NaN;  		
    }
    return d;
  }
}
