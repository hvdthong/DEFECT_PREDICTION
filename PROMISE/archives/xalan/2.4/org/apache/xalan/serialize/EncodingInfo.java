package org.apache.xalan.serialize;

/**
 * Holds information about a given encoding.
 */
public class EncodingInfo extends Object
{

  /**
   * The encoding name.
   */
  final String name;

  /**
   * The name used by the Java convertor.
   */
  final String javaName;

  /**
   * The last printable character.
   */
  final int lastPrintable;

  /**
   * Create an EncodingInfo object based on the name, java name, and the 
   * max character size.
   *
   * @param name non-null reference to the ISO name.
   * @param javaName non-null reference to the Java encoding name.
   * @param lastPrintable The maximum character that can be written.
   */
  public EncodingInfo(String name, String javaName, int lastPrintable)
  {

    this.name = name;
    this.javaName = javaName;
    this.lastPrintable = lastPrintable;
  }
}
