package org.apache.xml.utils.synthetic;

/**
 * Class SynthesisException <needs-comment/>
 * @xsl.usage internal
 */
public class SynthesisException extends Exception
{

  /** Field code.
   *  @serial          */
  int code;


  /** Field SYNTAX          */
  public static final int SYNTAX = 0;

  /** Field UNSUPPORTED          */
  public static final int UNSUPPORTED = 1;

  /** Field REIFIED          */
  public static final int REIFIED = 2;

  /** Field UNREIFIED          */
  public static final int UNREIFIED = 3;

  /** Field WRONG_OWNER          */
  public static final int WRONG_OWNER = 4;

  /** Field errToString          */
  public static final String[] errToString = {
    "(Syntax error; specific message should be passed in)",
    "Feature not yet supported",
    "Can't change features of 'real' class",
    "Can't yet instantiate/invoke without 'real' class",
    "Can't add Member to an object other than its declarer", };

  /**
   * Constructor SynthesisException
   *
   *
   * @param code
   */
  public SynthesisException(int code)
  {

    super(errToString[code]);

    this.code = code;
  }

  /**
   * Constructor SynthesisException
   *
   *
   * @param code
   * @param msg
   */
  public SynthesisException(int code, String msg)
  {

    super(msg);

    this.code = code;
  }

  /**
   * Method getCode 
   *
   *
   */
  int getCode()
  {
    return code;
  }
}
