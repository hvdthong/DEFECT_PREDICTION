package org.apache.xml.utils;

import java.util.*;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.res.XSLMessages;


/**
 * <meta name="usage" content="internal"/>
 * Pool of object of a given type to pick from to help memory usage
 */
public class ObjectPool implements java.io.Serializable
{

  /** Type of objects in this pool.
   *  @serial          */
  private final Class objectType;

  /** Vector of given objects this points to.
   *  @serial          */
  private final Vector freeStack;

  /**
   * Constructor ObjectPool
   *
   * @param type Type of objects for this pool
   */
  public ObjectPool(Class type)
  {
    objectType = type;
    freeStack = new Vector();
  }
  
  /**
   * Constructor ObjectPool
   *
   * @param className Fully qualified name of the type of objects for this pool.
   */
  public ObjectPool(String className)
  {
    try
    {
      objectType = Class.forName(className);
    }
    catch(ClassNotFoundException cnfe)
    {
      throw new WrappedRuntimeException(cnfe);
    }
    freeStack = new Vector();
  }


  /**
   * Constructor ObjectPool
   *
   *
   * @param type Type of objects for this pool
   * @param size Size of vector to allocate
   */
  public ObjectPool(Class type, int size)
  {
    objectType = type;
    freeStack = new Vector(size);
  }

  /**
   * Constructor ObjectPool
   *
   */
  public ObjectPool()
  {
    objectType = null;
    freeStack = new Vector();
  }

  /**
   * Get an instance of the given object in this pool if available
   *
   *
   * @return an instance of the given object if available or null
   */
  public synchronized Object getInstanceIfFree()
  {

    if (!freeStack.isEmpty())
    {

      Object result = freeStack.lastElement();

      freeStack.setSize(freeStack.size() - 1);

      return result;
    }

    return null;
  }

  /**
   * Get an instance of the given object in this pool 
   *
   *
   * @return An instance of the given object
   */
  public synchronized Object getInstance()
  {

    if (freeStack.isEmpty())
    {

      try
      {
        return objectType.newInstance();
      }
      catch (InstantiationException ex){}
      catch (IllegalAccessException ex){}

    }
    else
    {

      Object result = freeStack.lastElement();

      freeStack.setSize(freeStack.size() - 1);

      return result;
    }
  }

  /**
   * Add an instance of the given object to the pool  
   *
   *
   * @param obj Object to add.
   */
  public synchronized void freeInstance(Object obj)
  {

    freeStack.addElement(obj);
  }
}
