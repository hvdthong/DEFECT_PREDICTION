package org.apache.xml.res;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A utility class for issuing XML error messages.
 * @xsl.usage internal
 */
public class XMLMessages
{

  /** The local object to use.  */
  protected Locale fLocale = Locale.getDefault();

  /** The language specific resource object for XML messages.  */
  private static ListResourceBundle XMLBundle = null;

  /** The class name of the XML error message string table.    */
  private static final String XML_ERROR_RESOURCES =
    "org.apache.xml.res.XMLErrorResources";

  /** String to use if a bad message code is used. */
  protected static final String BAD_CODE = "BAD_CODE";

  /** String to use if the message format operation failed.  */
  protected static final String FORMAT_FAILED = "FORMAT_FAILED";
    
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
   * @param msgKey    The key for the message text.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted message string.
   */
  public static final String createXMLMessage(String msgKey, Object args[])
  {
    if (XMLBundle == null)
      XMLBundle = loadResourceBundle(XML_ERROR_RESOURCES);
    
    if (XMLBundle != null)
    {
      return createMsg(XMLBundle, msgKey, args);
    }
    else
      return "Could not load any resource bundles.";
  }

  /**
   * Creates a message from the specified key and replacement
   * arguments, localized to the given locale.
   *
   * @param fResourceBundle The resource bundle to use.
   * @param msgKey  The message key to use.
   * @param args      The arguments to be used as replacement text
   *                  in the message created.
   *
   * @return The formatted message string.
   */
  public static final String createMsg(ListResourceBundle fResourceBundle,
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
   * Return a named ResourceBundle for a particular locale.  This method mimics the behavior
   * of ResourceBundle.getBundle().
   *
   * @param className The class name of the resource bundle.
   * @return the ResourceBundle
   * @throws MissingResourceException
   */
  public static ListResourceBundle loadResourceBundle(String className)
          throws MissingResourceException
  {    
    Locale locale = Locale.getDefault();

    try
    {
      return (ListResourceBundle)ResourceBundle.getBundle(className, locale);
    }
    catch (MissingResourceException e)
    {
      {

        return (ListResourceBundle)ResourceBundle.getBundle(
          className, new Locale("en", "US"));
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
  protected static String getResourceSuffix(Locale locale)
  {

    String suffix = "_" + locale.getLanguage();
    String country = locale.getCountry();

    if (country.equals("TW"))
      suffix += "_" + country;

    return suffix;
  }
}
