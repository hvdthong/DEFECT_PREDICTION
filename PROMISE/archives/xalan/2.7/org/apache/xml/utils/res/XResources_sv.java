package org.apache.xml.utils.res;


/**
 * The Swedish resource bundle.
 * @xsl.usage internal
 */
public class XResources_sv extends XResourceBundle
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
    { "ui_language", "sv" }, { "help_language", "sv" }, { "language", "sv" },
    { "alphabet", new CharArrayWrapper(
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' }) },
    { "tradAlphabet", new CharArrayWrapper(
      new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                  'Y', 'Z' }) },

    { "orientation", "LeftToRight" },

    { "numbering", "additive" },

  };
  }
}
