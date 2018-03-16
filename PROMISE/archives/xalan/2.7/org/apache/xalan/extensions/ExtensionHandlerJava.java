package org.apache.xalan.extensions;

import java.util.Hashtable;

/**
 * Abstract base class handling the java language extensions for XPath.
 * This base class provides cache management shared by all of the
 * various java extension handlers.
 *
 * @xsl.usage internal
 */
public abstract class ExtensionHandlerJava extends ExtensionHandler
{

  /** Extension class name         */
  protected String m_className = "";

  /** Table of cached methods          */
  private Hashtable m_cachedMethods = new Hashtable();

  /**
   * Construct a new extension handler given all the information
   * needed.
   *
   * @param namespaceUri the extension namespace URI that I'm implementing
   * @param funcNames    string containing list of functions of extension NS
   * @param lang         language of code implementing the extension
   * @param srcURL       value of src attribute (if any) - treated as a URL
   *                     or a classname depending on the value of lang. If
   *                     srcURL is not null, then scriptSrc is ignored.
   * @param scriptSrc    the actual script code (if any)
   * @param scriptLang   the scripting language
   * @param className    the extension class name 
   */
  protected ExtensionHandlerJava(String namespaceUri, String scriptLang,
                                 String className)
  {

    super(namespaceUri, scriptLang);

    m_className = className;
  }

  /**
   * Look up the entry in the method cache.
   * @param methodKey   A key that uniquely identifies this invocation in
   *                    the stylesheet.
   * @param objType     A Class object or instance object representing the type
   * @param methodArgs  An array of the XObject arguments to be used for
   *                    function mangling.
   *
   * @return The given method from the method cache
   */
  public Object getFromCache(Object methodKey, Object objType,
                             Object[] methodArgs)
  {

    return m_cachedMethods.get(methodKey);
  }

  /**
   * Add a new entry into the method cache.
   * @param methodKey   A key that uniquely identifies this invocation in
   *                    the stylesheet.
   * @param objType     A Class object or instance object representing the type
   * @param methodArgs  An array of the XObject arguments to be used for
   *                    function mangling.
   * @param methodObj   A Class object or instance object representing the method
   *
   * @return The cached method object
   */
  public Object putToCache(Object methodKey, Object objType,
                           Object[] methodArgs, Object methodObj)
  {

    return m_cachedMethods.put(methodKey, methodObj);
  }
}
