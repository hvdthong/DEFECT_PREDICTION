package org.apache.xml.utils.res;


/**
 * <meta name="usage" content="internal"/>
 * The Japanese (Hiragana) resource bundle.
 */
public class XResources_ja_JP_HA extends XResourceBundle
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

  /** The association list.        */
  static final Object[][] contents =
  {
    { "ui_language", "ja" }, { "help_language", "ja" }, { "language", "ja" },
    { "alphabet",
      new char[]{ 0x3042, 0x3044, 0x3046, 0x3048, 0x304a, 0x304b, 0x304d,
                  0x304f, 0x3051, 0x3053, 0x3055, 0x3057, 0x3059, 0x305b,
                  0x305d, 0x305f, 0x3061, 0x3064, 0x3066, 0x3068, 0x306a,
                  0x306b, 0x306c, 0x306d, 0x306e, 0x306f, 0x3072, 0x3075,
                  0x3078, 0x307b, 0x307e, 0x307f, 0x3080, 0x3081, 0x3082,
                  0x3084, 0x3086, 0x3088, 0x3089, 0x308a, 0x308b, 0x308c,
                  0x308d, 0x308f, 0x3090, 0x3091, 0x3092, 0x3093 } },
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
