package org.apache.xml.utils.res;


/**
 * The Cyrillic resource bundle.
 * @xsl.usage internal
 */
public class XResources_cy extends XResourceBundle
{

  /**
   * Get the association list.
   *
   * @return The association list.
   */
  public Object[][] getContents()
  {
    return new Object[][]
  {
    { "ui_language", "cy" }, { "help_language", "cy" }, { "language", "cy" },
    { "alphabet", new CharArrayWrapper(
      new char[]{ 0x0430, 0x0432, 0x0433, 0x0434, 0x0435, 0x0437, 0x0438,
                  0x0439, 0x04A9, 0x0457, 0x043A, 0x043B, 0x043C, 0x043D,
                  0x046F, 0x043E, 0x043F, 0x0447, 0x0440, 0x0441, 0x0442,
                  0x0443, 0x0444, 0x0445, 0x0470, 0x0460, 0x0446 }) },
    { "tradAlphabet", new CharArrayWrapper(
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' }) },

    { "orientation", "LeftToRight" },

    { "numbering", "multiplicative-additive" },
    { "multiplierOrder", "precedes" },

    { "numberGroups", new IntArrayWrapper(new int[]{ 100, 10, 1 }) },

    { "multiplier", new LongArrayWrapper(new long[]{ 1000 }) },
    { "multiplierChar", new CharArrayWrapper(new char[]{ 0x03D9 }) },

    { "zero", new CharArrayWrapper(new char[0]) },

    { "digits", new CharArrayWrapper(
      new char[]{ 0x0430, 0x0432, 0x0433, 0x0434, 0x0435, 0x0437, 0x0438,
                  0x0439, 0x04A9 }) },
    { "tens", new CharArrayWrapper(
      new char[]{ 0x0457, 0x043A, 0x043B, 0x043C, 0x043D, 0x046F, 0x043E,
                  0x043F, 0x0447 }) },
    { "hundreds", new CharArrayWrapper(
      new char[]{ 0x0440, 0x0441, 0x0442, 0x0443, 0x0444, 0x0445, 0x0470,
                  0x0460, 0x0446 }) },
    { "tables", new StringArrayWrapper(new String[]{ "hundreds", "tens", "digits" }) }
  };
  }
}
