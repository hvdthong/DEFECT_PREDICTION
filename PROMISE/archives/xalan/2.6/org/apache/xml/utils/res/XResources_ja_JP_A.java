package org.apache.xml.utils.res;


/**
 * The Japanese (Katakana) resource bundle.
 * @xsl.usage internal
 */
public class XResources_ja_JP_A extends XResourceBundle
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
    { "ui_language", "ja" }, { "help_language", "ja" }, { "language", "ja" },
    { "alphabet",
      new char[]{ 0x30a2, 0x30a4, 0x30a6, 0x30a8, 0x30aa, 0x30ab, 0x30ad,
                  0x30af, 0x30b1, 0x30b3, 0x30b5, 0x30b7, 0x30b9, 0x30bb,
                  0x30bd, 0x30bf, 0x30c1, 0x30c4, 0x30c6, 0x30c8, 0x30ca,
                  0x30cb, 0x30cc, 0x30cd, 0x30ce, 0x30cf, 0x30d2, 0x30d5,
                  0x30d8, 0x30db, 0x30de, 0x30df, 0x30e0, 0x30e1, 0x30e2,
                  0x30e4, 0x30e6, 0x30e8, 0x30e9, 0x30ea, 0x30eb, 0x30ec,
                  0x30ed, 0x30ef, 0x30f0, 0x30f1, 0x30f2, 0x30f3 } },
    { "tradAlphabet",
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' } },

    { "orientation", "LeftToRight" },

    { "numbering", "multiplicative-additive" },
    { "multiplierOrder", "follows" },

    { "numberGroups", new int[]{ 1 } },

    { "multiplier",
      new long[]{ Long.MAX_VALUE, Long.MAX_VALUE, 100000000, 10000, 1000, 100, 10 } },
    { "multiplierChar",
      new char[]{ 0x4EAC, 0x5146, 0x5104, 0x4E07, 0x5343, 0x767e, 0x5341 } },

    { "zero", new char[0] },
    { "digits",
      new char[]{ 0x4E00, 0x4E8C, 0x4E09, 0x56DB, 0x4E94, 0x516D, 0x4E03,
                  0x516B, 0x4E5D } }, { "tables", new String[]{ "digits" } }
  };
}
