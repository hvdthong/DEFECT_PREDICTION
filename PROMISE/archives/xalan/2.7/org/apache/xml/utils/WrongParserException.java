package org.apache.xml.utils;

/**
 * Certain functions may throw this error if they are paired with
 * the incorrect parser.
 * @xsl.usage general
 */
public class WrongParserException extends RuntimeException
{
    static final long serialVersionUID = 6481643018533043846L;

  /**
   * Create a WrongParserException object.
   * @param message The error message that should be reported to the user.
   */
  public WrongParserException(String message)
  {
    super(message);
  }
}
