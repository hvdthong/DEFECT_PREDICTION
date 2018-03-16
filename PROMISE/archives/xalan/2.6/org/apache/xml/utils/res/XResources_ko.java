package org.apache.xml.utils.res;


/**
 * The Korean resource bundle.
 * @xsl.usage internal
 */
public class XResources_ko extends XResourceBundle
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
    { "ui_language", "ko" }, { "help_language", "ko" }, { "language", "ko" },
    { "alphabet",
      new char[]{ 0x3131, 0x3134, 0x3137, 0x3139, 0x3141, 0x3142, 0x3145, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c,
                  0x314d, 0x314e, 0x314f, 0x3151, 0x3153, 0x3155, 0x3157, 0x315b, 0x315c, 0x3160, 0x3161, 0x3163}},
    { "tradAlphabet",
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' } },

    { "orientation", "LeftToRight" },

    { "numbering", "multiplicative-additive" },
    { "multiplierOrder", "follows" },

    { "numberGroups", new int[]{ 1 } },

    { "zero", new char[0] },

    { "multiplier", new int[]{ 100000000, 10000, 1000, 100, 10 } },
    { "multiplierChar",
      new char[]{  0xc5b5, 0xb9cc, 0xcc9c, 0xbc31, 0xc2ed } },
    { "digits",
      new char[]{ 0xc77c, 0xc774, 0xc0bc, 0xc0ac, 0xc624, 0xc721, 0xce60, 0xd314, 0xad6c
                   } }, { "tables", new String[]{ "digits" } }
  };
}
