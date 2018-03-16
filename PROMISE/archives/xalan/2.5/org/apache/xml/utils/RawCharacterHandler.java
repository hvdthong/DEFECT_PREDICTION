package org.apache.xml.utils;

/**
 * <meta name="usage" content="advanced"/>
 * An interface that a Serializer/ContentHandler/ContentHandler must
 * implement in order for disable-output-escaping to work.
 */
public interface RawCharacterHandler
{

  /**
   * Serialize the characters without escaping.
   *
   * @param ch Array of characters
   * @param start Start index of characters in the array
   * @param length Number of characters in the array
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void charactersRaw(char ch[], int start, int length)
    throws javax.xml.transform.TransformerException;
}
