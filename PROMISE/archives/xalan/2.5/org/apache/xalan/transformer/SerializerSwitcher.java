package org.apache.xalan.transformer;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;

import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serialize.Method;
import org.apache.xalan.templates.OutputProperties;

import org.xml.sax.ContentHandler;

/**
 * This is a helper class that decides if Xalan needs to switch
 * serializers, based on the first output element.
 */
public class SerializerSwitcher
{

  /**
   * Switch to HTML serializer if element is HTML
   *
   *
   * @param transformer Non-null transformer instance
   * @param ns Namespace URI of the element
   * @param localName Local part of name of element
   *
   * @throws TransformerException
   */
  public static void switchSerializerIfHTML(
          TransformerImpl transformer, String ns, String localName)
            throws TransformerException
  {

    if (null == transformer)
      return;

    if (((null == ns) || (ns.length() == 0))
            && localName.equalsIgnoreCase("html"))
    {
      if (null != transformer.getOutputPropertyNoDefault(OutputKeys.METHOD))
        return;

      Properties prevProperties = transformer.getOutputFormat().getProperties();
      
      OutputProperties htmlOutputProperties = new OutputProperties(Method.HTML);

      htmlOutputProperties.copyFrom(prevProperties, true);
      Properties htmlProperties = htmlOutputProperties.getProperties();

      try
      {
        Serializer oldSerializer = null;

        if (null != oldSerializer)
        {
          Serializer serializer =
            SerializerFactory.getSerializer(htmlProperties);

          Writer writer = oldSerializer.getWriter();

          if (null != writer)
            serializer.setWriter(writer);
          else
          {
            OutputStream os = oldSerializer.getOutputStream();

            if (null != os)
              serializer.setOutputStream(os);
          }


          ContentHandler ch = serializer.asContentHandler();

          transformer.setContentHandler(ch);
        }
      }
      catch (java.io.IOException e)
      {
        throw new TransformerException(e);
      }
    }
  }
  
  /**
   * Get the value of a property, without using the default properties.  This 
   * can be used to test if a property has been explicitly set by the stylesheet 
   * or user.
   *
   * @param name The property name, which is a fully-qualified URI.
   *
   * @return The value of the property, or null if not found.
   *
   * @throws IllegalArgumentException If the property is not supported, 
   * and is not namespaced.
   */
  private static String getOutputPropertyNoDefault(String qnameString, Properties props)
    throws IllegalArgumentException
  {    
    String value = (String)props.get(qnameString);
    
    return value;
  }
  
  /**
   * Switch to HTML serializer if element is HTML
   *
   *
   * @param ns Namespace URI of the element
   * @param localName Local part of name of element
   *
   * @throws TransformerException
   * @return new contentHandler.
   */
  public static Serializer switchSerializerIfHTML(
          String ns, String localName, Properties props, Serializer oldSerializer)
            throws TransformerException
  {
    Serializer newSerializer = oldSerializer;

    if (((null == ns) || (ns.length() == 0))
            && localName.equalsIgnoreCase("html"))
    {
      if (null != getOutputPropertyNoDefault(OutputKeys.METHOD, props))
        return newSerializer;

      Properties prevProperties = props;
      
      OutputProperties htmlOutputProperties = new OutputProperties(Method.HTML);

      htmlOutputProperties.copyFrom(prevProperties, true);
      Properties htmlProperties = htmlOutputProperties.getProperties();

      {
        if (null != oldSerializer)
        {
          Serializer serializer =
            SerializerFactory.getSerializer(htmlProperties);

          Writer writer = oldSerializer.getWriter();

          if (null != writer)
            serializer.setWriter(writer);
          else
          {
            OutputStream os = serializer.getOutputStream();

            if (null != os)
              serializer.setOutputStream(os);
          }
          newSerializer = serializer;
        }
      }
    }
    return newSerializer;
  }
  
}
