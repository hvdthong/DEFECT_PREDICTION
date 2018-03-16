package org.apache.xalan.res;

import java.util.ListResourceBundle;

import org.apache.xpath.res.XPATHMessages;

/**
 * <meta name="usage" content="internal"/>
 * Sets things up for issuing error messages.  This class is misnamed, and
 * should be called XalanMessages, or some such.
 */
public class XSLMessages extends XPATHMessages
{

  /** The language specific resource object for Xalan messages.  */
  private static ListResourceBundle XSLTBundle = null;

  /** The class name of the Xalan error message string table.    */
  private static final String XSLT_ERROR_RESOURCES =
    "org.apache.xalan.res.XSLTErrorResources";

  /**
   * Creates a message from the specified key and replacement
   * arguments, localized to the given locale.
   *
   * @param errorCode The key for the message text.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted message string.
   */
  {
    if (XSLTBundle == null)
      XSLTBundle = loadResourceBundle(XSLT_ERROR_RESOURCES);
    
    if (XSLTBundle != null)
    {
      return createMsg(XSLTBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }
  
  /**
   * Creates a message from the specified key and replacement
   * arguments, localized to the given locale.
   *
   * @param msgKey    The key for the message text.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted warning string.
   */
  {
    if (XSLTBundle == null)
      XSLTBundle = loadResourceBundle(XSLT_ERROR_RESOURCES);

    if (XSLTBundle != null)
    {
      return createMsg(XSLTBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }
}
