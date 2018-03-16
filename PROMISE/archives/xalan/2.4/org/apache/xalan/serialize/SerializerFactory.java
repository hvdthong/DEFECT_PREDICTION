package org.apache.xalan.serialize;

import javax.xml.transform.OutputKeys;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xalan.templates.OutputProperties;
import org.apache.xml.utils.WrappedRuntimeException;

/**
 * Factory for creating serializers.
 */
public abstract class SerializerFactory
{

  /*
   * Associates output methods to serializer classes.
   * (Don't use this right now.  -sb
   */


  /**
   * Associates output methods to default output formats.
   */
  private static Hashtable _formats = new Hashtable();

  /**
   * Returns a serializer for the specified output method. Returns
   * null if no implementation exists that supports the specified
   * output method. For a list of the default output methods see
   * {@link Method}.
   *
   * @param format The output format
   * @return A suitable serializer, or null
   * @throws IllegalArgumentException (apparently -sc) if method is 
   * null or an appropriate serializer can't be found
   * @throws WrappedRuntimeException (apparently -sc) if an 
   * exception is thrown while trying to find serializer
   */
  public static Serializer getSerializer(Properties format)
  {

    Serializer ser = null;

    try
    {
      Class cls;
      String method = format.getProperty(OutputKeys.METHOD);

      if (method == null)
        throw new IllegalArgumentException(
          "The output format has a null method name");


      if (cls == null)
      {
        String className =
          format.getProperty(OutputProperties.S_KEY_CONTENT_HANDLER);

        if (null == className)
        {
          throw new IllegalArgumentException(
            "The output format must have a '"
            + OutputProperties.S_KEY_CONTENT_HANDLER + "' property!");
        }

        cls = Class.forName(className);

      }

      ser = (Serializer) cls.newInstance();

      ser.setOutputFormat(format);
    }
    catch (Exception e)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(e);
    }

    return ser;
  }
}
