package org.apache.xpath.patterns;

/**
 * This interface should be implemented by Nodes and/or iterators,
 * when they need to know what the node test is before they do
 * getNextChild, etc.
 */
public interface NodeTestFilter
{

  /**
   * Set the node test for this filter.
   *
   *
   * @param nodeTest Reference to a NodeTest that may be used to predetermine 
   *                 what nodes to return.
   */
  void setNodeTest(NodeTest nodeTest);
}
