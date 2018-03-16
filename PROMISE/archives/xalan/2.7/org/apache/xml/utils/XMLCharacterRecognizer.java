package org.apache.xml.utils;

/**
 * Class used to verify whether the specified <var>ch</var> 
 * conforms to the XML 1.0 definition of whitespace. 
 * @xsl.usage internal
 */
public class XMLCharacterRecognizer
{

  /**
   * Returns whether the specified <var>ch</var> conforms to the XML 1.0 definition
   * the definition of <CODE>S</CODE></A> for details.
   * @param ch Character to check as XML whitespace.
   * @return =true if <var>ch</var> is XML whitespace; otherwise =false.
   */
  public static boolean isWhiteSpace(char ch)
  {
    return (ch == 0x20) || (ch == 0x09) || (ch == 0xD) || (ch == 0xA);
  }

  /**
   * Tell if the string is whitespace.
   *
   * @param ch Character array to check as XML whitespace.
   * @param start Start index of characters in the array
   * @param length Number of characters in the array 
   * @return True if the characters in the array are 
   * XML whitespace; otherwise, false.
   */
  public static boolean isWhiteSpace(char ch[], int start, int length)
  {

    int end = start + length;

    for (int s = start; s < end; s++)
    {
      if (!isWhiteSpace(ch[s]))
        return false;
    }

    return true;
  }

  /**
   * Tell if the string is whitespace.
   *
   * @param buf StringBuffer to check as XML whitespace.
   * @return True if characters in buffer are XML whitespace, false otherwise
   */
  public static boolean isWhiteSpace(StringBuffer buf)
  {

    int n = buf.length();

    for (int i = 0; i < n; i++)
    {
      if (!isWhiteSpace(buf.charAt(i)))
        return false;
    }

    return true;
  }
  
  /**
   * Tell if the string is whitespace.
   *
   * @param s String to check as XML whitespace.
   * @return True if characters in buffer are XML whitespace, false otherwise
   */
  public static boolean isWhiteSpace(String s)
  {

    if(null != s)
    {
      int n = s.length();
  
      for (int i = 0; i < n; i++)
      {
        if (!isWhiteSpace(s.charAt(i)))
          return false;
      }
    }

    return true;
  }

}
