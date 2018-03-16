package org.apache.xml.utils.res;


/**
 * The Georgian resource bundle.
 * @xsl.usage internal
 */
public class XResources_ka extends XResourceBundle
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
    { "ui_language", "ka" }, { "help_language", "ka" }, { "language", "ka" },
    { "alphabet", new CharArrayWrapper(
      new char[]{ 0x10D0, 0x10D1, 0x10D2, 0x10D3, 0x10D4, 0x10D5, 0x10D6,
                  0x10f1, 0x10D7, 0x10D8, 0x10D9, 0x10DA, 0x10DB, 0x10DC,
                  0x10f2, 0x10DD, 0x10DE, 0x10DF, 0x10E0, 0x10E1, 0x10E2,
                  0x10E3, 0x10E4, 0x10E5, 0x10E6, 0x10E7, 0x10E8, 0x10E9,
                  0x10EA, 0x10EB, 0x10EC, 0x10ED, 0x10EE, 0x10F4, 0x10EF,
                  0x10F0 }) },
    { "tradAlphabet", new CharArrayWrapper(
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' }) },

    { "orientation", "LeftToRight" },

    { "numbering", "additive" },

    { "numberGroups", new IntArrayWrapper(new int[]{ 1000, 100, 10, 1 }) },

    { "digits", new CharArrayWrapper(
      new char[]{ 0x10D0, 0x10D1, 0x10D2, 0x10D3, 0x10D4, 0x10D5, 0x10D6,
                  0x10f1, 0x10D7 }) },
    { "tens", new CharArrayWrapper(
      new char[]{ 0x10D8, 0x10D9, 0x10DA, 0x10DB, 0x10DC, 0x10f2, 0x10DD,
                  0x10DE, 0x10DF }) },
    { "hundreds", new CharArrayWrapper(
      new char[]{ 0x10E0, 0x10E1, 0x10E2, 0x10E3, 0x10E4, 0x10E5, 0x10E6,
                  0x10E7, 0x10E8 }) },
    { "thousands", new CharArrayWrapper(
      new char[]{ 0x10E9, 0x10EA, 0x10EB, 0x10EC, 0x10ED, 0x10EE, 0x10F4,
                  0x10EF, 0x10F0 }) },
    { "tables", new StringArrayWrapper(new String[]{ "thousands", "hundreds", 
            "tens", "digits" }) }
  };
  }
}
