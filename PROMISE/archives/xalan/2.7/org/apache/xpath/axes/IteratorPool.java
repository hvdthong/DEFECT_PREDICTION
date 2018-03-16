package org.apache.xpath.axes;

import java.util.Vector;

import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;

/**
 * Pool of object of a given type to pick from to help memory usage
 * @xsl.usage internal
 */
public class IteratorPool implements java.io.Serializable
{
    static final long serialVersionUID = -460927331149566998L;

  /** Type of objects in this pool.
   *  @serial          */
  private final DTMIterator m_orig;

  /** Vector of given objects this points to.
   *  @serial          */
  private final Vector m_freeStack;

  /**
   * Constructor IteratorPool
   *
   * @param original The original iterator from which all others will be cloned.
   */
  public IteratorPool(DTMIterator original)
  {
    m_orig = original;
    m_freeStack = new Vector();
  }
  
  /**
   * Get an instance of the given object in this pool 
   *
   * @return An instance of the given object
   */
  public synchronized DTMIterator getInstanceOrThrow()
    throws CloneNotSupportedException
  {
    if (m_freeStack.isEmpty())
    {

      return (DTMIterator)m_orig.clone();
    }
    else
    {
      DTMIterator result = (DTMIterator)m_freeStack.lastElement();

      m_freeStack.setSize(m_freeStack.size() - 1);

      return result;
    }
  }
  
  /**
   * Get an instance of the given object in this pool 
   *
   * @return An instance of the given object
   */
  public synchronized DTMIterator getInstance()
  {
    if (m_freeStack.isEmpty())
    {

      try
      {
        return (DTMIterator)m_orig.clone();
      }
      catch (Exception ex)
      {
        throw new WrappedRuntimeException(ex);
      }
    }
    else
    {
      DTMIterator result = (DTMIterator)m_freeStack.lastElement();

      m_freeStack.setSize(m_freeStack.size() - 1);

      return result;
    }
  }

  /**
   * Add an instance of the given object to the pool  
   *
   *
   * @param obj Object to add.
   */
  public synchronized void freeInstance(DTMIterator obj)
  {
    m_freeStack.addElement(obj);
  }
}
