package org.apache.xml.serializer;

import java.util.Hashtable;
import java.util.Properties;

import javax.xml.transform.OutputKeys;

import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;
import org.xml.sax.ContentHandler;

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
  private static Hashtable m_formats = new Hashtable();

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



        String className;

          className =
            format.getProperty(OutputPropertiesFactory.S_KEY_CONTENT_HANDLER);

          if (null == className)
          {
            throw new IllegalArgumentException(
              "The output format must have a '"
              + OutputPropertiesFactory.S_KEY_CONTENT_HANDLER + "' property!");
          }

          cls = Class.forName(className);



        Object obj = cls.newInstance();

        if (obj instanceof SerializationHandler)
        {
            ser = (Serializer) cls.newInstance();
            ser.setOutputFormat(format);
        }
        else
        {
              /*
               *  This  must be a user defined Serializer.
               *  It had better implement ContentHandler.
               */
               if (obj instanceof ContentHandler)
               {

                  /*
                   * The user defined serializer defines ContentHandler,
                   * but we need to wrap it with ToXMLSAXHandler which
                   * will collect SAX-like events and emit true
                   * SAX ContentHandler events to the users handler.
                   */
                  className = SerializerConstants.DEFAULT_SAX_SERIALIZER;
                  cls = Class.forName(className);
                  SerializationHandler sh =
                      (SerializationHandler) cls.newInstance();
                  sh.setContentHandler( (ContentHandler) obj);
                  sh.setOutputFormat(format);

                  ser = sh;


               }
               else
               {
                   throw new Exception(
                       XMLMessages.createXMLMessage(
                           XMLErrorResources.ER_SERIALIZER_NOT_CONTENTHANDLER,
                               new Object[] { className}));
               }

        }
      }
      catch (Exception e)
      {
        throw new org.apache.xml.utils.WrappedRuntimeException(e);
      }

      return ser;
    }
}
