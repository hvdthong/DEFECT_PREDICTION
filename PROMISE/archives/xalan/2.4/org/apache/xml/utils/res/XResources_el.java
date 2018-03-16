package org.apache.xml.utils.res;

import org.apache.xml.utils.res.XResourceBundle;

import java.util.*;


/**
 * <meta name="usage" content="internal"/>
 * The Greek resource bundle.
 */
public class XResources_el extends XResourceBundle
{

  /**
   * Get the association list.
   *
   * @return The association list.
   */
  public Object[][] getContents()
  {
    return contents;
  }

  /** The association list.          */
  static final Object[][] contents =
  {
    { "ui_language", "el" }, { "help_language", "el" }, { "language", "el" },
    { "alphabet",
      new char[]{ 0x03b1, 0x03b2, 0x03b3, 0x03b4, 0x03b5, 0x03b6, 0x03b7,
                  0x03b8, 0x03b9, 0x03ba, 0x03bb, 0x03bc, 0x03bd, 0x03be,
                  0x03bf, 0x03c0, 0x03c1, 0x03c2, 0x03c3, 0x03c4, 0x03c5,
                  0x03c6, 0x03c7, 0x03c8, 0x03c9 } },
    { "tradAlphabet",
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' } },

    { "orientation", "LeftToRight" },

    { "numbering", "multiplicative-additive" },
    { "multiplierOrder", "precedes" },

    { "numberGroups", new int[]{ 100, 10, 1 } },

    { "multiplier", new long[]{ 1000 } },
    { "multiplierChar", new char[]{ 0x03d9 } },

    { "zero", new char[0] },

    { "digits",
      new char[]{ 0x03b1, 0x03b2, 0x03b3, 0x03b4, 0x03b5, 0x03db, 0x03b6,
                  0x03b7, 0x03b8 } },
    { "tens",
      new char[]{ 0x03b9, 0x03ba, 0x03bb, 0x03bc, 0x03bd, 0x03be, 0x03bf,
                  0x03c0, 0x03df } },
    { "hundreds",
      new char[]{ 0x03c1, 0x03c2, 0x03c4, 0x03c5, 0x03c6, 0x03c7, 0x03c8,
                  0x03c9, 0x03e1 } },

    { "tables", new String[]{ "hundreds", "tens", "digits" } }
  };
}
