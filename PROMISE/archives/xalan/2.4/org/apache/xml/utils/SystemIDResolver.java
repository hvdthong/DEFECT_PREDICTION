package org.apache.xml.utils;

import javax.xml.transform.TransformerException;

import org.apache.xml.utils.URI;
import org.apache.xml.utils.URI.MalformedURIException;

import java.io.*;

import java.lang.StringBuffer;

/**
 * <meta name="usage" content="internal"/>
 * This class is used to resolve relative URIs and SystemID 
 * strings into absolute URIs.
 *
 * <p>This is a generic utility for resolving URIs, other than the 
 * fact that it's declared to throw TransformerException.  Please 
 * see code comments for details on how resolution is performed.</p>
 */
public class SystemIDResolver
{

  /**
   * Get absolute URI from a given relative URI. 
   * 
   * <p>The URI is resolved relative to the system property "user.dir" 
   * if it is available; if not (i.e. in an Applet perhaps which 
   * throws SecurityException) then it is currently resolved 
   * relative to "" or a blank string.  Also replaces all 
   * backslashes with forward slashes.</p>
   *
   * @param uri Relative URI to resolve
   *
   * @return Resolved absolute URI or the input relative URI if 
   * it could not be resolved.
   */
  public static String getAbsoluteURIFromRelative(String uri)
  {

    String curdir = "";
    try {
      curdir = System.getProperty("user.dir");
    }

    if (null != curdir)
    {
      String base;
      if (curdir.startsWith(File.separator))
      else
        
      if (uri != null)
        uri = base + System.getProperty("file.separator") + uri;
      else
        uri = base + System.getProperty("file.separator");
    }

    if (null != uri && (uri.indexOf('\\') > -1))
      uri = uri.replace('\\', '/');

    return uri;
  }
  
  /**
   * Take a SystemID string and try and turn it into a good absolute URL.
   *
   * @param urlString url A URL string, which may be relative or absolute.
   *
   * @return The resolved absolute URI
   * @throws TransformerException thrown if the string can't be turned into a URL.
   */
  public static String getAbsoluteURI(String url)
          throws TransformerException
  {
    if (url.startsWith(".."))
      url = new File(url).getAbsolutePath();
      
    if (url.startsWith(File.separator))
    {
    }
    else if (url.indexOf(':') < 0)
    {
      url = getAbsoluteURIFromRelative(url);
    }


    return url;
  }


  /**
   * Take a SystemID string and try and turn it into a good absolute URL.
   *
   * @param urlString SystemID string
   * @param base Base URI to use to resolve the given systemID
   *
   * @return The resolved absolute URI
   * @throws TransformerException thrown if the string can't be turned into a URL.
   */
  public static String getAbsoluteURI(String urlString, String base)
          throws TransformerException
  {
    boolean isAbsouteUrl = false;
    boolean needToResolve = false;    
 
    if (urlString.indexOf(':') > 0)
    {
      isAbsouteUrl = true;
    }
    else if (urlString.startsWith(File.separator))
    {
      isAbsouteUrl = true;
    }

    if ((!isAbsouteUrl) && ((null == base)
            || (base.indexOf(':') < 0)))
    {
      if (base != null && base.startsWith(File.separator))
      else
        base = getAbsoluteURIFromRelative(base);
    }

    if ((null != base) && needToResolve) 
         
    {
      if(base.equals(urlString))
      {
        base = "";
      }
      else
      {
        urlString = urlString.substring(5);
        isAbsouteUrl = false;
      }
    }   

    if (null != base && (base.indexOf('\\') > -1))
      base = base.replace('\\', '/');

    if (null != urlString && (urlString.indexOf('\\') > -1))
      urlString = urlString.replace('\\', '/');

    URI uri;

    try
    {
      if ((null == base) || (base.length() == 0) || (isAbsouteUrl))
      {
        uri = new URI(urlString);
      }
      else
      {
        URI baseURI = new URI(base);

        uri = new URI(baseURI, urlString);
      }
    }
    catch (MalformedURIException mue)
    {
      throw new TransformerException(mue);
    }

    String uriStr = uri.toString();
    
    if((Character.isLetter(uriStr.charAt(0)) && (uriStr.charAt(1) == ':') 
     && (uriStr.charAt(2) == '/') && (uriStr.length() == 3 || uriStr.charAt(3) != '/'))
       || ((uriStr.charAt(0) == '/') && (uriStr.length() == 1 || uriStr.charAt(1) != '/')))
    {
    }
    return uriStr;
  }
}
