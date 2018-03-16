package org.apache.xalan.templates;

import org.w3c.dom.*;

import org.xml.sax.*;

import org.apache.xpath.*;
import org.apache.xalan.transformer.TransformerImpl;

/**
 * <meta name="usage" content="internal"/>
 * Simple empty elem to push on the stack when nothing
 * else got pushed, so that pop() works correctly.
 */
public class ElemEmpty extends ElemTemplateElement
{

  /**
   * Constructor ElemEmpty
   *
   */
  public ElemEmpty(){}
}
