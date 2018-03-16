package org.apache.xpath;

import javax.xml.transform.SourceLocator;

/**
 * A class that implements this interface can construct expressions, 
 * give information about child and parent expressions,
 * and give the originating source information.  A class that implements 
 * this interface does not lay any claim to being directly executable.
 * 
 * <p>Note: This interface should not be considered stable.  Only exprSetParent 
 * and exprGetParent can be counted on to work reliably.  Work in progress.</p>
 */
public interface ExpressionNode extends SourceLocator
{
  /** This pair of methods are used to inform the node of its
    parent. */
  public void exprSetParent(ExpressionNode n);
  public ExpressionNode exprGetParent();

  /** This method tells the node to add its argument to the node's
    list of children.  */
  public void exprAddChild(ExpressionNode n, int i);

  /** This method returns a child node.  The children are numbered
     from zero, left to right. */
  public ExpressionNode exprGetChild(int i);

  /** Return the number of children the node has. */
  public int exprGetNumChildren();
}

