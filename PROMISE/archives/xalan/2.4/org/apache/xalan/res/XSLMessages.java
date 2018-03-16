package org.apache.xalan.res;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.util.MissingResourceException;

import org.apache.xpath.res.XPATHErrorResources;

/**
 * <meta name="usage" content="internal"/>
 * Sets things up for issuing error messages.  This class is misnamed, and
 * should be called XalanMessages, or some such.
 */
public class XSLMessages
{

  /** The local object to use.  */
  private Locale fLocale = Locale.getDefault();

  /** The language specific resource object for Xalan messages.  */
  private static ResourceBundle XSLTBundle = null;

  /** The language specific resource object for XPath messages.  */
  private static ResourceBundle XPATHBundle = null;

  /** The class name of the Xalan error message string table.    */
  private static final String XSLT_ERROR_RESOURCES =
    "org.apache.xalan.res.XSLTErrorResources";

  /** The class name of the XPath error message string table.     */
  private static final String XPATH_ERROR_RESOURCES =
    "org.apache.xpath.res.XPATHErrorResources";

  /** String to use if a bad message code is used. */
  private static String BAD_CODE = "BAD_CODE";

  /** String to use if the message format operation failed.  */
  private static String FORMAT_FAILED = "FORMAT_FAILED";

  /**
   * Set the Locale object to use.
   * 
   * @param locale non-null reference to Locale object.
   */
   public void setLocale(Locale locale)
  {
    fLocale = locale;
  }

  /**
   * Get the Locale object that is being used.
   * 
   * @return non-null reference to Locale object.
   */
  public Locale getLocale()
  {
    return fLocale;
  }

  /**
   * Creates a message from the specified key and replacement
   * arguments, localized to the given locale.
   *
   * @param errorCode The key for the message text.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted warning string.
   */
  {

    if (XPATHBundle == null)
      XPATHBundle =
        loadResourceBundle(XPATH_ERROR_RESOURCES);

    ResourceBundle fResourceBundle = XPATHBundle;

    if (fResourceBundle != null)
    {
      String msgKey = XPATHErrorResources.getWarningKey(errorCode);

      return createXPATHMsg(fResourceBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }

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

    if (XPATHBundle == null)
      XPATHBundle =
        loadResourceBundle(XPATH_ERROR_RESOURCES);

    ResourceBundle fResourceBundle = XPATHBundle;

    if (fResourceBundle != null)
    {
      String msgKey = XPATHErrorResources.getMessageKey(errorCode);

      return createXPATHMsg(fResourceBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }
  
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

    if (XPATHBundle == null)
      XPATHBundle =
        loadResourceBundle(XPATH_ERROR_RESOURCES);

    ResourceBundle fResourceBundle = XPATHBundle;

    if (fResourceBundle != null)
    {
      return createXPATHMsg(fResourceBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }


  /**
   * Creates a message from the specified key and replacement
   * arguments, localized to the given locale.
   *
   * @param errorCode The key for the message text.
   *
   * @param fResourceBundle The resource bundle to use.
   * @param msgKey  The message key to use.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted message string.
   */
  public static final String createXPATHMsg(ResourceBundle fResourceBundle,
  {

    String fmsg = null;
    boolean throwex = false;
    String msg = null;

    if (msgKey != null)
      msg = fResourceBundle.getString(msgKey);

    if (msg == null)
    {
      msg = fResourceBundle.getString(XPATHErrorResources.BAD_CODE);
      throwex = true;
    }

    if (args != null)
    {
      try
      {

        int n = args.length;

        for (int i = 0; i < n; i++)
        {
          if (null == args[i])
            args[i] = "";
        }

        fmsg = java.text.MessageFormat.format(msg, args);
      }
      catch (Exception e)
      {
        fmsg = fResourceBundle.getString(XPATHErrorResources.FORMAT_FAILED);
        fmsg += " " + msg;
      }
    }
    else
      fmsg = msg;

    if (throwex)
    {
      throw new RuntimeException(fmsg);
    }

    return fmsg;
  }

  /**
   * Creates a message from the specified key and replacement
   * arguments, localized to the given locale.
   *
   * @param errorCode The key for the message text.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted warning string.
   */
  {

    if (XSLTBundle == null)
      XSLTBundle =
        loadResourceBundle(XSLT_ERROR_RESOURCES);

    ResourceBundle fResourceBundle = XSLTBundle;

    if (fResourceBundle != null)
    {
      String msgKey = XSLTErrorResources.getWarningKey(errorCode);

      return createMsg(fResourceBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }
  
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
      XSLTBundle =
        loadResourceBundle(XSLT_ERROR_RESOURCES);

    ResourceBundle fResourceBundle = XSLTBundle;

    if (fResourceBundle != null)
    {
      return createMsg(fResourceBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }

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
      XSLTBundle =
        loadResourceBundle(XSLT_ERROR_RESOURCES);

    ResourceBundle fResourceBundle = XSLTBundle;

    if (fResourceBundle != null)
    {
      String msgKey = XSLTErrorResources.getMessageKey(errorCode);

      return createMsg(fResourceBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }

  /**
   * Creates a message from the specified key and replacement
   * arguments, localized to the given locale.
   *
   * @param errorCode The key for the message text.
   *
   * @param fResourceBundle The resource bundle to use.
   * @param msgKey  The message key to use.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted message string.
   */
  public static final String createMsg(ResourceBundle fResourceBundle,
  {

    String fmsg = null;
    boolean throwex = false;
    String msg = null;

    if (msgKey != null)
      msg = fResourceBundle.getString(msgKey);

    if (msg == null)
    {
      msg = fResourceBundle.getString(BAD_CODE);
      throwex = true;
    }

    if (args != null)
    {
      try
      {

        int n = args.length;

        for (int i = 0; i < n; i++)
        {
          if (null == args[i])
            args[i] = "";
        }

        fmsg = java.text.MessageFormat.format(msg, args);
      }
      catch (Exception e)
      {
        fmsg = fResourceBundle.getString(FORMAT_FAILED);
        fmsg += " " + msg;
      }
    }
    else
      fmsg = msg;

    if (throwex)
    {
      throw new RuntimeException(fmsg);
    }

    return fmsg;
  }

  /**
   * Creates a message from the specified key and replacement
   * arguments, localized to the given locale.
   *
   * @param bundleName The name of the resource bundle to be
   *                  used.
   * @param errorCode The key for the message text.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted message string.
   *
   * @throws Exception if the message can not be loaded.
   */
  public String createMessage(String bundleName, int errorCode, Object args[])
          throws Exception
  {

    boolean throwex = false;
    int majorCode;
    int minorCode;
    String fmsg = null;
    ResourceBundle aResourceBundle = null;

    aResourceBundle = loadResourceBundle(bundleName);

    String msgKey = XSLTErrorResources.getMessageKey(errorCode);
    String msg = null;

    if (msgKey != null)
      msg = aResourceBundle.getString(msgKey);

    if (msg == null)
    {
      msg = aResourceBundle.getString(BAD_CODE);
      throwex = true;
    }

    if (args != null)
    {
      try
      {

        int n = args.length;

        for (int i = 0; i < n; i++)
        {
          if (null == args[i])
            args[i] = "";
        }

        fmsg = java.text.MessageFormat.format(msg, args);
      }
      catch (Exception e)
      {
        fmsg = aResourceBundle.getString(FORMAT_FAILED);
        fmsg += " " + msg;
      }
    }
    else
      fmsg = msg;

    if (throwex)
    {
      throw new RuntimeException(fmsg);
    }

    return fmsg;
  }

  /**
   * Return a named ResourceBundle for a particular locale.  This method mimics the behavior
   * of ResourceBundle.getBundle().
   *
   * @param res the name of the resource to load.
   * @param locale the locale to prefer when searching for the bundle
   *
   * @param className The class name of the resource bundle.
   * @return the ResourceBundle
   * @throws MissingResourceException
   */
  public static final ResourceBundle loadResourceBundle(String className)
          throws MissingResourceException
  {

    Locale locale = Locale.getDefault();

    try
    {

      return ResourceBundle.getBundle(className, locale);
    }
    catch (MissingResourceException e)
    {
      {

        return ResourceBundle.getBundle(
          XSLT_ERROR_RESOURCES, new Locale("en", "US"));
      }
      catch (MissingResourceException e2)
      {

        throw new MissingResourceException(
          "Could not load any resource bundles." + className, className, "");
      }
    }
  }

  /**
   * Return the resource file suffic for the indicated locale
   * For most locales, this will be based the language code.  However
   * for Chinese, we do distinguish between Taiwan and PRC
   *
   * @param locale the locale
   * @return an String suffix which can be appended to a resource name
   */
  private static final String getResourceSuffix(Locale locale)
  {

    String suffix = "_" + locale.getLanguage();
    String country = locale.getCountry();

    if (country.equals("TW"))
      suffix += "_" + country;

    return suffix;
  }
}
