package org.apache.xalan.templates;


/**
 * Simple empty elem to push on the stack when nothing
 * else got pushed, so that pop() works correctly.
 * @xsl.usage internal
 */
public class ElemEmpty extends ElemTemplateElement
{
    static final long serialVersionUID = 7544753713671472252L;

  /**
   * Constructor ElemEmpty
   *
   */
  public ElemEmpty(){}
}
