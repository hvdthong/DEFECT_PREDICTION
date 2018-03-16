package org.apache.xpath.axes;

/**
 * Classes who implement this information provide information needed for 
 * static analysis of a path component.
 */
public interface PathComponent
{
  /** 
   * Get the analysis bits for this path component, as defined in the WalkerFactory.
   * @return One of WalkerFactory#BIT_DESCENDANT, etc.
   */
  public int getAnalysisBits();

}

