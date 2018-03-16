package org.apache.xml.utils.res;


/**
 * <meta name="usage" content="internal"/>
 * The Chinese(Taiwan) resource bundle.
 */
public class XResources_zh_TW extends XResourceBundle
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

  /** The association list.         */
  static final Object[][] contents =
  {
    { "ui_language", "zh" }, { "help_language", "zh" }, { "language", "zh" },
    { "alphabet",
      new char[]{ 0xff21, 0xff22, 0xff23, 0xff24, 0xff25, 0xff26, 0xff27,
                  0xff28, 0xff29, 0xff2a, 0xff2b, 0xff2c, 0xff2d, 0xff2e,
                  0xff2f, 0xff30, 0xff31, 0xff32, 0xff33, 0xff34, 0xff35,
                  0xff36, 0xff37, 0xff38, 0xff39, 0xff3a } },
    { "tradAlphabet",
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' } },

    { "orientation", "LeftToRight" },

    { "numbering", "multiplicative-additive" },
    { "multiplierOrder", "follows" },

    { "numberGroups", new int[]{ 1 } },

    { "zero", new char[]{ 0x96f6 } },

    { "multiplier", new long[]{ 100000000, 10000, 1000, 100, 10 } },
    { "multiplierChar",
      new char[]{ 0x5104, 0x842c, 0x4edf, 0x4f70, 0x62fe } },
    { "digits",
      new char[]{ 0x58f9, 0x8cb3, 0x53c3, 0x8086, 0x4f0d, 0x9678, 0x67d2,
                  0x634c, 0x7396 } }, { "tables", new String[]{ "digits" } }
  };
}
