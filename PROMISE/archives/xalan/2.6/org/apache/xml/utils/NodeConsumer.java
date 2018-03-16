package org.apache.xml.utils;

import org.w3c.dom.Node;

/**
 * The tree walker will test for this interface, and call
 * setOriginatingNode before calling the SAX event.  For creating
 * DOM backpointers for things that are normally created via
 * SAX events.
 */
public interface NodeConsumer
{

  /**
   * Set the node that is originating the SAX event.
   *
   * @param n Reference to node that originated the current event.
   */
  public void setOriginatingNode(Node n);
}
