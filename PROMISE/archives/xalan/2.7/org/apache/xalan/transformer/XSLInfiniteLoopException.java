package org.apache.xalan.transformer;

/**
 * Class used to create an Infinite Loop Exception 
 * @xsl.usage internal
 */
class XSLInfiniteLoopException
{

  /**
   * Constructor XSLInfiniteLoopException
   *
   */
  XSLInfiniteLoopException()
  {
    super();
  }

  /**
   * Get Message associated with the exception
   *
   *
   * @return Message associated with the exception
   */
  public String getMessage()
  {
    return "Processing Terminated.";
  }
}
