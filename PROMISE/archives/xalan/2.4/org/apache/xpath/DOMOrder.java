package org.apache.xpath;

/**
 * @deprecated Since the introduction of the DTM, this class will be removed.
 * Nodes that implement this index can return a document order index.
 * Eventually, this will be replaced by DOM 3 methods. 
 * (compareDocumentOrder and/or compareTreePosition.)
 */
public interface DOMOrder
{

  /**
   * Get the UID (document order index).
   *
   * @return integer whose relative value corresponds to document order
   * -- that is, if node1.getUid()<node2.getUid(), node1 comes before
   * node2, and if they're equal node1 and node2 are the same node. No
   * promises are made beyond that.
   */
  public int getUid();
}
