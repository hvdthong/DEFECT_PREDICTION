package org.apache.xml.serializer.utils;

/**
 * This class is for throwing important checked exceptions
 * over non-checked methods.  It should be used with care,
 * and in limited circumstances.
 * 
 * This class is a copy of the one in org.apache.xml.utils. 
 * It exists to cut the serializers dependancy on that package.
 * 
 * This class is not a public API, it is only public because it is
 * used by org.apache.xml.serializer.
 * @xsl.usage internal
 */
public final class WrappedRuntimeException extends RuntimeException
{
    static final long serialVersionUID = 7140414456714658073L;

  /** Primary checked exception.
   *  @serial          */
  private Exception m_exception;

  /**
   * Construct a WrappedRuntimeException from a
   * checked exception.
   *
   * @param e Primary checked exception
   */
  public WrappedRuntimeException(Exception e)
  {

    super(e.getMessage());

    m_exception = e;
  }

  /**
   * Constructor WrappedRuntimeException
   *
   *
   * @param msg Exception information.
   * @param e Primary checked exception
   */
  public WrappedRuntimeException(String msg, Exception e)
  {

    super(msg);

    m_exception = e;
  }
  
  /**
   * Get the checked exception that this runtime exception wraps.
   *
   * @return The primary checked exception
   */
  public Exception getException()
  {
    return m_exception;
  }
}
