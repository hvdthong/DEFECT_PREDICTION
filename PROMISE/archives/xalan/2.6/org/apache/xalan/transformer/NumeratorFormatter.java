package org.apache.xalan.transformer;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;

/**
 * Converts enumerated numbers into strings, using the XSL conversion attributes.
 * Having this in a class helps avoid being forced to extract the attributes repeatedly.
 * @xsl.usage internal
 */
class NumeratorFormatter
{

  /** The owning xsl:number element.          */
  protected Element m_xslNumberElement;

  /** An instance of a Tokenizer          */
  NumberFormatStringTokenizer m_formatTokenizer;

  /** Locale we need to format in          */
  Locale m_locale;

  /** An instance of a NumberFormat         */
  java.text.NumberFormat m_formatter;

  /** An instance of a transformer          */
  TransformerImpl m_processor;

  /**
   * Table to help in converting decimals to roman numerals.
   * @see org.apache.xalan.transformer.DecimalToRoman
   */
  private final static DecimalToRoman m_romanConvertTable[] = {
    new DecimalToRoman(1000, "M", 900, "CM"),
    new DecimalToRoman(500, "D", 400, "CD"),
    new DecimalToRoman(100L, "C", 90L, "XC"),
    new DecimalToRoman(50L, "L", 40L, "XL"),
    new DecimalToRoman(10L, "X", 9L, "IX"),
    new DecimalToRoman(5L, "V", 4L, "IV"),
    new DecimalToRoman(1L, "I", 1L, "I") };

  /**
   * Chars for converting integers into alpha counts.
   * @see TransformerImpl#int2alphaCount
   */
                                                    'A', 'B', 'C', 'D', 'E',
                                                    'F', 'G', 'H', 'I', 'J',
                                                    'K', 'L', 'M', 'N', 'O',
                                                    'P', 'Q', 'R', 'S', 'T',
                                                    'U', 'V', 'W', 'X', 'Y' };

  /**
   * Construct a NumeratorFormatter using an element
   * that contains XSL number conversion attributes -
   * format, letter-value, xml:lang, digit-group-sep,
   * n-digits-per-group, and sequence-src.
   *
   * @param xslNumberElement The given xsl:number element
   * @param processor a non-null transformer instance
   */
  NumeratorFormatter(Element xslNumberElement, TransformerImpl processor)
  {
    m_xslNumberElement = xslNumberElement;
    m_processor = processor;

  /**
   * Convert a long integer into alphabetic counting, in other words
   * count using the sequence A B C ... Z AA AB AC.... etc.
   * 
   * @param val Value to convert -- must be greater than zero.
   * @param table a table containing one character for each digit in the radix
   * @return String representing alpha count of number.
   * @see org.apache.xalan.transformer.DecimalToRoman
   *
   * Note that the radix of the conversion is inferred from the size
   * of the table.
   */
  protected String int2alphaCount(int val, char[] table)
  {

    int radix = table.length;

    char buf[] = new char[100];



    int correction = 0;

    do
    {

      correction =
        ((lookupIndex == 0) || (correction != 0 && lookupIndex == radix - 1))
        ? (radix - 1) : 0;

      lookupIndex = (val + correction) % radix;

      val = (val / radix);

      if (lookupIndex == 0 && val == 0)
        break;

      buf[charPos--] = table[lookupIndex];
    }
    while (val > 0);

    return new String(buf, charPos + 1, (buf.length - charPos - 1));
  }

  /**
   * Convert a long integer into roman numerals.
   * @param val Value to convert.
   * @param prefixesAreOK true_ to enable prefix notation (e.g. 4 = "IV"),
   * false_ to disable prefix notation (e.g. 4 = "IIII").
   * @return Roman numeral string.
   * @see DecimalToRoman
   * @see m_romanConvertTable
   */
  String long2roman(long val, boolean prefixesAreOK)
  {

    if (val <= 0)
    {
      return "#E(" + val + ")";
    }

    String roman = "";
    int place = 0;

    if (val <= 3999L)
    {
      do
      {
        while (val >= m_romanConvertTable[place].m_postValue)
        {
          roman += m_romanConvertTable[place].m_postLetter;
          val -= m_romanConvertTable[place].m_postValue;
        }

        if (prefixesAreOK)
        {
          if (val >= m_romanConvertTable[place].m_preValue)
          {
            roman += m_romanConvertTable[place].m_preLetter;
            val -= m_romanConvertTable[place].m_preValue;
          }
        }

        place++;
      }
      while (val > 0);
    }
    else
    {
      roman = "#error";
    }

    return roman;

  /**
   * This class returns tokens using non-alphanumberic
   * characters as delimiters.
   */
  class NumberFormatStringTokenizer
  {

    /** Field holding the current position in the string      */
    private int currentPosition;

    /** The total length of the string          */
    private int maxPosition;

    /** The string to tokenize          */
    private String str;

    /**
     * Construct a NumberFormatStringTokenizer.
     *
     * @param str The string to tokenize
     */
    NumberFormatStringTokenizer(String str)
    {
      this.str = str;
      maxPosition = str.length();
    }
    
    /**
     * Reset tokenizer so that nextToken() starts from the beginning. 
     *
     */
    void reset()
    {
      currentPosition = 0;
    }

    /**
     * Returns the next token from this string tokenizer.
     *
     * @return     the next token from this string tokenizer.
     * @throws  NoSuchElementException  if there are no more tokens in this
     *               tokenizer's string.
     */
    String nextToken()
    {

      if (currentPosition >= maxPosition)
      {
        throw new NoSuchElementException();
      }

      int start = currentPosition;

      while ((currentPosition < maxPosition)
             && Character.isLetterOrDigit(str.charAt(currentPosition)))
      {
        currentPosition++;
      }

      if ((start == currentPosition)
              && (!Character.isLetterOrDigit(str.charAt(currentPosition))))
      {
        currentPosition++;
      }

      return str.substring(start, currentPosition);
    }

    /**
     * Tells if <code>nextToken</code> will throw an exception      * if it is called.
     *
     * @return true if <code>nextToken</code> can be called      * without throwing an exception.
     */
    boolean hasMoreTokens()
    {
      return (currentPosition >= maxPosition) ? false : true;
    }

    /**
     * Calculates the number of times that this tokenizer's
     * <code>nextToken</code> method can be called before it generates an
     * exception.
     *
     * @return  the number of tokens remaining in the string using the current
     *          delimiter set.
     * @see     java.util.StringTokenizer#nextToken()
     */
    int countTokens()
    {

      int count = 0;
      int currpos = currentPosition;

      while (currpos < maxPosition)
      {
        int start = currpos;

        while ((currpos < maxPosition)
               && Character.isLetterOrDigit(str.charAt(currpos)))
        {
          currpos++;
        }

        if ((start == currpos)
                && (Character.isLetterOrDigit(str.charAt(currpos)) == false))
        {
          currpos++;
        }

        count++;
      }

      return count;
    }
}
