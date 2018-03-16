package org.apache.xml.utils.res;

import org.apache.xml.utils.res.XResourceBundle;

import java.util.*;


/**
 * <meta name="usage" content="internal"/>
 * The Chinese resource bundle.
 */
public class XResources_zh_CN extends XResourceBundle
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
      new char[]{ 0x4ebf, 0x4e07, 0x5343, 0x767e, 0x5341 } },
    { "digits",
      new char[]{ 0x4e00, 0x4e8c, 0x4e09, 0x56db, 0x4e94, 0x516d, 0x4e03,
                  0x516b, 0x4e5d } }, { "tables", new String[]{ "digits" } }
  };
}
