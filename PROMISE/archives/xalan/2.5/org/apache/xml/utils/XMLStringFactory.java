package org.apache.xml.utils;

/**
 * A concrete class that implements this interface creates XMLString objects.
 */
public abstract class XMLStringFactory
{

  /**
   * Create a new XMLString from a Java string.
   *
   *
   * @param string Java String reference, which must be non-null.
   *
   * @return An XMLString object that wraps the String reference.
   */
  public abstract XMLString newstr(String string);

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
  public abstract XMLString newstr(FastStringBuffer string, int start, 
                                   int length);

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
  public abstract XMLString newstr(char[] string, int start, 
                                   int length);
                                   
  /**
   * Get a cheap representation of an empty string.
   * 
   * @return An non-null reference to an XMLString that represents "".
   */
  public abstract XMLString emptystr();
}
