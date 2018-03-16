package org.apache.xml.utils;

/**
 * This class pools string buffers, since they are reused so often.
 * String buffers are good candidates for pooling, because of 
 * their supporting character arrays.
 * @xsl.usage internal
 */
public class StringBufferPool
{

  /** The global pool of string buffers.   */
  private static ObjectPool m_stringBufPool =
    new ObjectPool(org.apache.xml.utils.FastStringBuffer.class);

  /**
   * Get the first free instance of a string buffer, or create one 
   * if there are no free instances.
   *
   * @return A string buffer ready for use.
   */
  public synchronized static FastStringBuffer get()
  {
    return (FastStringBuffer) m_stringBufPool.getInstance();
  }

  /**
   * Return a string buffer back to the pool.
   *
   * @param sb Must be a non-null reference to a string buffer.
   */
  public synchronized static void free(FastStringBuffer sb)
  {
    sb.setLength(0);
    m_stringBufPool.freeInstance(sb);
  }
}
