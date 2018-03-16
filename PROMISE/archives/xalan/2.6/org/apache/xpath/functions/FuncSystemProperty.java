package org.apache.xpath.functions;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.apache.xpath.res.XPATHErrorResources;

/**
 * Execute the SystemProperty() function.
 * @xsl.usage advanced
 */
public class FuncSystemProperty extends FunctionOneArg
{
  /**
   * The path/filename of the property file: XSLTInfo.properties
   * Maintenance note: see also
   * org.apache.xalan.processor.TransformerFactoryImpl.XSLT_PROPERTIES
   */
  static String XSLT_PROPERTIES = "org/apache/xalan/res/XSLTInfo.properties";

  /**
   * Execute the function.  The function must return
   * a valid object.
   * @param xctxt The current execution context.
   * @return A valid XObject.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException
  {

    String fullName = m_arg0.execute(xctxt).str();
    int indexOfNSSep = fullName.indexOf(':');
    String result;
    String propName = "";

    Properties xsltInfo = new Properties();

    loadPropertyFile(XSLT_PROPERTIES, xsltInfo);

    if (indexOfNSSep > 0)
    {
      String prefix = (indexOfNSSep >= 0)
                      ? fullName.substring(0, indexOfNSSep) : "";
      String namespace;

      namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
      propName = (indexOfNSSep < 0)
                 ? fullName : fullName.substring(indexOfNSSep + 1);

      {
        result = xsltInfo.getProperty(propName);

        if (null == result)
        {
          warn(xctxt, XPATHErrorResources.WG_PROPERTY_NOT_SUPPORTED,

          return XString.EMPTYSTRING;
        }
      }
      else
      {
        warn(xctxt, XPATHErrorResources.WG_DONT_DO_ANYTHING_WITH_NS,
             new Object[]{ namespace,

        try
        {
          result = System.getProperty(propName);

          if (null == result)
          {

            return XString.EMPTYSTRING;
          }
        }
        catch (SecurityException se)
        {
          warn(xctxt, XPATHErrorResources.WG_SECURITY_EXCEPTION,

          return XString.EMPTYSTRING;
        }
      }
    }
    else
    {
      try
      {
        result = System.getProperty(fullName);

        if (null == result)
        {

          return XString.EMPTYSTRING;
        }
      }
      catch (SecurityException se)
      {
        warn(xctxt, XPATHErrorResources.WG_SECURITY_EXCEPTION,

        return XString.EMPTYSTRING;
      }
    }

    if (propName.equals("version") && result.length() > 0)
    {
      try
      {
        return new XNumber(1.0);
      }
      catch (Exception ex)
      {
        return new XString(result);
      }
    }
    else
      return new XString(result);
  }

  /**
   * Retrieve a propery bundle from a specified file
   * 
   * @param file The string name of the property file.  The name 
   * should already be fully qualified as path/filename
   * @param target The target property bag the file will be placed into.
   */
  public void loadPropertyFile(String file, Properties target)
  {
    try
    {
      SecuritySupport ss = SecuritySupport.getInstance();

      InputStream is = ss.getResourceAsStream(ObjectFactory.findClassLoader(),
                                              file);

      BufferedInputStream bis = new BufferedInputStream(is);

    }
    catch (Exception ex)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(ex);
    }
  }
}
