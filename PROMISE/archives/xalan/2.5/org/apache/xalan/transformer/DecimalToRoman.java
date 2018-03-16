package org.apache.xalan.transformer;

/**
 * <meta name="usage" content="internal"/>
 * Structure to help in converting integers to roman numerals
 */
public class DecimalToRoman
{

  /**
   * Constructor DecimalToRoman
   *
   *
   * @param postValue Minimum value for a given range of 
   * roman numbers
   * @param postLetter Correspoding letter (roman) to postValue
   * @param preValue Value of last prefixed number within 
   * that same range (i.e. IV if postval is 5 (V))
   * @param preLetter Correspoding letter(roman) to preValue
   */
  public DecimalToRoman(long postValue, String postLetter, long preValue,
                        String preLetter)
  {

    this.m_postValue = postValue;
    this.m_postLetter = postLetter;
    this.m_preValue = preValue;
    this.m_preLetter = preLetter;
  }

  /** Minimum value for a given range of roman numbers          */
  public long m_postValue;

  /** Correspoding letter (roman) to m_postValue          */
  public String m_postLetter;

  /** Value of last prefixed number within that same range  */
  public long m_preValue;

  /** Correspoding letter (roman) to m_preValue          */
  public String m_preLetter;
}
