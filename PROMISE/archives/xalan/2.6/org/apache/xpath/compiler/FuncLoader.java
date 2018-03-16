package org.apache.xpath.compiler;

import javax.xml.transform.TransformerException;

import org.apache.xpath.functions.Function;

/**
 * Lazy load of functions into the function table as needed, so we don't 
 * have to load all the functions allowed in XPath and XSLT on startup.
 * @xsl.usage advanced
 */
public class FuncLoader
{

  /** The function ID, which may correspond to one of the FUNC_XXX values 
   *  found in {@link org.apache.xpath.compiler.FunctionTable}, but may 
   *  be a value installed by an external module.  */
  private int m_funcID;

  /** The class name of the function.  Must not be null.   */
  private String m_funcName;

  /**
   * Get the local class name of the function class.  If function name does 
   * not have a '.' in it, it is assumed to be relative to 
   * 'org.apache.xpath.functions'.
   *
   * @return The class name of the {org.apache.xpath.functions.Function} class.
   */
  public String getName()
  {
    return m_funcName;
  }

  /**
   * Construct a function loader
   *
   * @param funcName The class name of the {org.apache.xpath.functions.Function} 
   *             class, which, if it does not have a '.' in it, is assumed to 
   *             be relative to 'org.apache.xpath.functions'. 
   * @param funcID  The function ID, which may correspond to one of the FUNC_XXX 
   *    values found in {@link org.apache.xpath.compiler.FunctionTable}, but may 
   *    be a value installed by an external module. 
   */
  public FuncLoader(String funcName, int funcID)
  {

    super();

    m_funcID = funcID;
    m_funcName = funcName;
  }

  /**
   * Get a Function instance that this instance is liaisoning for.
   *
   * @return non-null reference to Function derivative.
   *
   * @throws javax.xml.transform.TransformerException if ClassNotFoundException, 
   *    IllegalAccessException, or InstantiationException is thrown.
   */
  public Function getFunction() throws TransformerException
  {
    try
    {
      String className = m_funcName;
      if (className.indexOf(".") < 0) {
        className = "org.apache.xpath.functions." + className;
      }

      return (Function) ObjectFactory.newInstance(
        className, ObjectFactory.findClassLoader(), true);
    }
    catch (ObjectFactory.ConfigurationError e)
    {
      throw new TransformerException(e.getException());
    }
  }
}
