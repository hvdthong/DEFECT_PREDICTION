package org.apache.xml.utils;

/**
 * <meta name="usage" content="general"/>
 * Certain functions may throw this error if they are paired with
 * the incorrect parser.
 */
public class WrongParserException extends RuntimeException
{

  /**
   * Create a WrongParserException object.
   * @param message The error message that should be reported to the user.
   */
  public WrongParserException(String message)
  {
    super(message);
  }
}
