package org.apache.xml.utils;

/**
 * The default implementation of XMLStringFactory.
 * This implementation creates XMLStringDefault objects.
 */
public class XMLStringFactoryDefault extends XMLStringFactory
{
  private static final XMLStringDefault EMPTY_STR = new XMLStringDefault("");
  
  /**
   * Create a new XMLString from a Java string.
   *
   *
   * @param string Java String reference, which must be non-null.
   *
   * @return An XMLString object that wraps the String reference.
   */
  public XMLString newstr(String string)
  {
    return new XMLStringDefault(string);
  }

  /**
   * Create a XMLString from a FastStringBuffer.
   *
   *
   * @param fsb FastStringBuffer reference, which must be non-null.
   * @param start The start position in the array.
   * @param length The number of characters to read from the array.
   *
   * @return An XMLString object that wraps the FastStringBuffer reference.
   */
  public XMLString newstr(FastStringBuffer fsb, int start, int length)
  {
    return new XMLStringDefault(fsb.getString(start, length));
  }

  /**
   * Create a XMLString from a FastStringBuffer.
   *
   *
   * @param string FastStringBuffer reference, which must be non-null.
   * @param start The start position in the array.
   * @param length The number of characters to read from the array.
   *
   * @return An XMLString object that wraps the FastStringBuffer reference.
   */
  public XMLString newstr(char[] string, int start, int length)
  {
    return new XMLStringDefault(new String(string, start, length));
  }
                                   
  /**
   * Get a cheap representation of an empty string.
   * 
   * @return An non-null reference to an XMLString that represents "".
   */
  public XMLString emptystr()
  {
    return EMPTY_STR;
  }
}
