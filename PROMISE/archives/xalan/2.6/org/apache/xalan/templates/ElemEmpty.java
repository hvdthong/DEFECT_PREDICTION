package org.apache.xalan.templates;


/**
 * Simple empty elem to push on the stack when nothing
 * else got pushed, so that pop() works correctly.
 * @xsl.usage internal
 */
public class ElemEmpty extends ElemTemplateElement
{

  /**
   * Constructor ElemEmpty
   *
   */
  public ElemEmpty(){}
}
