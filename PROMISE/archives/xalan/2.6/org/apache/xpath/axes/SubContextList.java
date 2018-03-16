package org.apache.xpath.axes;

import org.apache.xpath.XPathContext;
 
/**
 * A class that implements this interface is a sub context node list, meaning it
 * is a node list for a location path step for a predicate.
 * @xsl.usage advanced
 */
public interface SubContextList
{

  /**
   * Get the number of nodes in the node list, which, in the XSLT 1 based 
   * counting system, is the last index position.
   *
   *
   * @param xctxt The XPath runtime context.
   *
   * @return the number of nodes in the node list.
   */
  public int getLastPos(XPathContext xctxt);

  /**
   * Get the current sub-context position.
   *
   * @param xctxt The XPath runtime context.
   *
   * @return The position of the current node in the list.
   */
  public int getProximityPosition(XPathContext xctxt);
}
