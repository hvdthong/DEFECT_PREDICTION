package org.apache.xpath.functions;

/**
 * <meta name="usage" content="advanced"/>
 * An exception that is thrown if the wrong number of arguments to an exception 
 * are specified by the stylesheet.
 */
public class WrongNumberArgsException extends Exception
{

  /**
   * Constructor WrongNumberArgsException
   *
   * @param argsExpected Error message that tells the number of arguments that 
   * were expected.
   */
  public WrongNumberArgsException(String argsExpected)
  {

    super(argsExpected);
  }
}
