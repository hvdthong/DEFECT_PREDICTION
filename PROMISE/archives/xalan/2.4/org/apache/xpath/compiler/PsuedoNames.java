package org.apache.xpath.compiler;

/**
 * This is used to represent names of nodes that may not be named, like a 
 * comment node.
 */
public class PsuedoNames
{

  /**
   * Psuedo name for a wild card pattern ('*').
   */
  public static final String PSEUDONAME_ANY = "*";

  /**
   * Psuedo name for the root node.
   */
  public static final String PSEUDONAME_ROOT = "/";

  /**
   * Psuedo name for a text node.
   */
  public static final String PSEUDONAME_TEXT = "#text";

  /**
   * Psuedo name for a comment node.
   */
  public static final String PSEUDONAME_COMMENT = "#comment";

  /**
   * Psuedo name for a processing instruction node.
   */
  public static final String PSEUDONAME_PI = "#pi";

  /**
   * Psuedo name for an unknown type value.
   */
  public static final String PSEUDONAME_OTHER = "*";
}
