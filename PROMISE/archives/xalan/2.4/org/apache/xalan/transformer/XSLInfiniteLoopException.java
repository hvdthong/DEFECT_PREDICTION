package org.apache.xalan.transformer;

/**
 * <meta name="usage" content="internal"/>
 * Class used to create an Infinite Loop Exception 
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
