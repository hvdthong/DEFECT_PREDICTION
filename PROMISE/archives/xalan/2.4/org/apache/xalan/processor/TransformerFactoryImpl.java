package org.apache.xalan.processor;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLFilter;

import org.w3c.dom.Node;

import org.apache.xml.utils.TreeWalker;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.TransformerIdentityImpl;
import org.apache.xalan.transformer.TrAXFilter;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.Templates;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.ErrorListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.StringReader;

import java.util.Properties;
import java.util.Enumeration;

import org.apache.xalan.transformer.XalanProperties;

/**
 * The TransformerFactoryImpl, which implements the TRaX TransformerFactory
 * interface, processes XSLT stylesheets into a Templates object
 * (a StylesheetRoot).
 */
public class TransformerFactoryImpl extends SAXTransformerFactory
{

  /** 
   * The path/filename of the property file: XSLTInfo.properties  
   * Maintenance note: see also org.apache.xpath.functions.FuncSystemProperty.XSLT_PROPERTIES
   */
  public static String XSLT_PROPERTIES =
    "org/apache/xalan/res/XSLTInfo.properties";

  /** Flag tells if the properties file has been loaded to the system */
  private static boolean isInited = false;

  /**
   * Constructor TransformerFactoryImpl
   *
   */
  public TransformerFactoryImpl()
  {
    loadPropertyFileToSystem(XSLT_PROPERTIES);
  }

  /** a zero length Class array used in loadPropertyFileToSystem() */
  private static final Class[] NO_CLASSES = new Class[0];

  /** a zero length Object array used in loadPropertyFileToSystem() */
  private static final Object[] NO_OBJS = new Object[0];
  
  /** Static string to be used for incremental feature */

  /** Static string to be used for optimize feature */

  /** Static string to be used for source_location feature */
  public static final String FEATURE_SOURCE_LOCATION = XalanProperties.SOURCE_LOCATION;

  /**
   * Retrieve a propery bundle from a specified file and load it
   * int the System properties.
   *
   * @param file The properties file to be processed.
   */
  private static void loadPropertyFileToSystem(String file)
  {

    if (false == isInited)
    {
      try
      {
        InputStream is = null;

        try
        {
          Properties props = new Properties();

          try {
            java.lang.reflect.Method getCCL = Thread.class.getMethod("getContextClassLoader", NO_CLASSES);
            if (getCCL != null) {
              ClassLoader contextClassLoader = (ClassLoader) getCCL.invoke(Thread.currentThread(), NO_OBJS);
            }
          }
          catch (Exception e) {}

          if (is == null) {
          }

          BufferedInputStream bis = new BufferedInputStream(is);


          Properties systemProps = System.getProperties();
          Enumeration propEnum = props.propertyNames();

          while (propEnum.hasMoreElements())
          {
            String prop = (String) propEnum.nextElement();

            if (!systemProps.containsKey(prop))
              systemProps.put(prop, props.getProperty(prop));
          }

          System.setProperties(systemProps);

          isInited = true;
        }
        catch (Exception ex){}
      }
      catch (SecurityException se)
      {

      }
    }
  }

public javax.xml.transform.Templates processFromNode(Node node)
          throws TransformerConfigurationException
  {

    try
    {
      TemplatesHandler builder = newTemplatesHandler();
      TreeWalker walker = new TreeWalker(builder, new org.apache.xpath.DOM2Helper(), builder.getSystemId());

      walker.traverse(node);

      return builder.getTemplates();
    }
    catch (org.xml.sax.SAXException se)
    {
      if (m_errorListener != null)
      {
        try
        {
          m_errorListener.fatalError(new TransformerException(se));
        }
        catch (TransformerException ex)
        {
          throw new TransformerConfigurationException(ex);
        }

        return null;
      }
      else

    }
    catch (TransformerConfigurationException tce)
    {
      throw tce;
    }
   /* catch (TransformerException tce)
    {
      throw new TransformerConfigurationException(tce.getMessage(), tce);
    }*/
    catch (Exception e)
    {
      if (m_errorListener != null)
      {
        try
        {
          m_errorListener.fatalError(new TransformerException(e));
        }
        catch (TransformerException ex)
        {
          throw new TransformerConfigurationException(ex);
        }

        return null;
      }
      else

    }
  }

  /**
   * The systemID that was specified in
   * processFromNode(Node node, String systemID).
   */
  private String m_DOMsystemID = null;

  /**
   * The systemID that was specified in
   * processFromNode(Node node, String systemID).
   *
   * @return The systemID, or null.
   */
  String getDOMsystemID()
  {
    return m_DOMsystemID;
  }

  /**
   * Process the stylesheet from a DOM tree, if the
   * feature.
   *
   * @param node A DOM tree which must contain
   * valid transform instructions that this processor understands.
   * @param systemID The systemID from where xsl:includes and xsl:imports
   * should be resolved from.
   *
   * @return A Templates object capable of being used for transformation purposes.
   *
   * @throws TransformerConfigurationException
   */
  javax.xml.transform.Templates processFromNode(Node node, String systemID)
          throws TransformerConfigurationException
  {

    m_DOMsystemID = systemID;

    return processFromNode(node);
  }

  /**
   * Get InputSource specification(s) that are associated with the
   * given document specified in the source param,
   * via the xml-stylesheet processing instruction
   * the given criteria.  Note that it is possible to return several stylesheets
   * that match the criteria, in which case they are applied as if they were
   * a list of imports or cascades.
   *
   * <p>Note that DOM2 has it's own mechanism for discovering stylesheets.
   * Therefore, there isn't a DOM version of this method.</p>
   *
   *
   * @param source The XML source that is to be searched.
   * @param media The media attribute to be matched.  May be null, in which
   *              case the prefered templates will be used (i.e. alternate = no).
   * @param title The value of the title attribute to match.  May be null.
   * @param charset The value of the charset attribute to match.  May be null.
   *
   * @return A Source object capable of being used to create a Templates object.
   *
   * @throws TransformerConfigurationException
   */
  public Source getAssociatedStylesheet(
          Source source, String media, String title, String charset)
            throws TransformerConfigurationException
  {

    String baseID;
    InputSource isource = null;
    Node node = null;
    XMLReader reader = null;

    if (source instanceof DOMSource)
    {
      DOMSource dsource = (DOMSource) source;

      node = dsource.getNode();
      baseID = dsource.getSystemId();
    }
    else
    {
      isource = SAXSource.sourceToInputSource(source);
      baseID = isource.getSystemId();
    }

    StylesheetPIHandler handler = new StylesheetPIHandler(baseID, media,
                                    title, charset);
    
    if (m_uriResolver != null) 
    {
      handler.setURIResolver(m_uriResolver); 
    }

    try
    {
      if (null != node)
      {
        TreeWalker walker = new TreeWalker(handler, new org.apache.xpath.DOM2Helper(), baseID);

        walker.traverse(node);
      }
      else
      {

        try
        {
          javax.xml.parsers.SAXParserFactory factory =
            javax.xml.parsers.SAXParserFactory.newInstance();

          factory.setNamespaceAware(true);

          javax.xml.parsers.SAXParser jaxpParser = factory.newSAXParser();

          reader = jaxpParser.getXMLReader();
        }
        catch (javax.xml.parsers.ParserConfigurationException ex)
        {
          throw new org.xml.sax.SAXException(ex);
        }
        catch (javax.xml.parsers.FactoryConfigurationError ex1)
        {
          throw new org.xml.sax.SAXException(ex1.toString());
        }
        catch (NoSuchMethodError ex2){}
        catch (AbstractMethodError ame){}

        if (null == reader)
        {
          reader = XMLReaderFactory.createXMLReader();
        }

        reader.setContentHandler(handler);
        reader.parse(isource);
      }
    }
    catch (StopParseException spe)
    {

    }
    catch (org.xml.sax.SAXException se)
    {
      throw new TransformerConfigurationException(
        "getAssociatedStylesheets failed", se);
    }
    catch (IOException ioe)
    {
      throw new TransformerConfigurationException(
        "getAssociatedStylesheets failed", ioe);
    }

    return handler.getAssociatedStylesheet();
  }

  /**
   * Create a new Transformer object that performs a copy
   * of the source to the result.
   *
   * @param source An object that holds a URI, input stream, etc.
   *
   * @return A Transformer object that may be used to perform a transformation
   * in a single thread, never null.
   *
   * @throws TransformerConfigurationException May throw this during
   *            the parse when it is constructing the
   *            Templates object and fails.
   */
  public TemplatesHandler newTemplatesHandler()
          throws TransformerConfigurationException
  {
    return new StylesheetHandler(this);
  }

  /**
   * Look up the value of a feature.
   *
   * <p>The feature name is any fully-qualified URI.  It is
   * possible for an TransformerFactory to recognize a feature name but
   * to be unable to return its value; this is especially true
   * in the case of an adapter for a SAX1 Parser, which has
   * no way of knowing whether the underlying parser is
   * validating, for example.</p>
   *
   * @param name The feature name, which is a fully-qualified URI.
   * @return The current state of the feature (true or false).
   */
  public boolean getFeature(String name)
  {

    if ((DOMResult.FEATURE == name) || (DOMSource.FEATURE == name)
            || (SAXResult.FEATURE == name) || (SAXSource.FEATURE == name)
            || (StreamResult.FEATURE == name)
            || (StreamSource.FEATURE == name)
            || (SAXTransformerFactory.FEATURE == name)
            || (SAXTransformerFactory.FEATURE_XMLFILTER == name))
      return true;
    else if ((DOMResult.FEATURE.equals(name))
             || (DOMSource.FEATURE.equals(name))
             || (SAXResult.FEATURE.equals(name))
             || (SAXSource.FEATURE.equals(name))
             || (StreamResult.FEATURE.equals(name))
             || (StreamSource.FEATURE.equals(name))
             || (SAXTransformerFactory.FEATURE.equals(name))
             || (SAXTransformerFactory.FEATURE_XMLFILTER.equals(name)))
      return true;
    else
      return false;
  }
  
  public static boolean m_optimize = true;
  
  /** Flag set by FEATURE_SOURCE_LOCATION.
   * This feature specifies whether the transformation phase should
   * keep track of line and column numbers for the input source
   * document. Note that this works only when that
   * information is available from the source -- in other words, if you
   * pass in a DOM, there's little we can do for you.
   * 
   * The default is false. Setting it true may significantly
   * increase storage cost per node. 
   * 
   * %REVIEW% SAX2DTM is explicitly reaching up to retrieve this global field.
   * We should instead have an architected pathway for passing hints of this
   * sort down from TransformerFactory to Transformer to DTMManager to DTM.
   * */
  public static boolean m_source_location = false;
  
  /**
   * Allows the user to set specific attributes on the underlying
   * implementation.
   *
   * @param name The name of the attribute.
   * @param value The value of the attribute; Boolean or String="true"|"false"
   *
   * @throws IllegalArgumentException thrown if the underlying
   * implementation doesn't recognize the attribute.
   */
  public void setAttribute(String name, Object value)
          throws IllegalArgumentException
  {
    if (name.equals(FEATURE_INCREMENTAL))
    {
      if(value instanceof Boolean)
      {
        org.apache.xml.dtm.DTMManager.setIncremental(((Boolean)value).booleanValue());
      }
      else if(value instanceof String)
      {
        org.apache.xml.dtm.DTMManager.setIncremental((new Boolean((String)value)).booleanValue());
      }
      else
      {
      }
	}
    else if (name.equals(FEATURE_OPTIMIZE))
    {
      if(value instanceof Boolean)
      {
        m_optimize = ((Boolean)value).booleanValue();
      }
      else if(value instanceof String)
      {
        m_optimize = (new Boolean((String)value)).booleanValue();
      }
      else
      {
      }
    }
    
    else if(name.equals(FEATURE_SOURCE_LOCATION))
    {
      if(value instanceof Boolean)
      {
        m_source_location = ((Boolean)value).booleanValue();
      }
      else if(value instanceof String)
      {
        m_source_location = (new Boolean((String)value)).booleanValue();
      }
      else
      {
      }
    }
    
    else
    {
    }
  }

  /**
   * Allows the user to retrieve specific attributes on the underlying
   * implementation.
   *
   * @param name The name of the attribute.
   * @return value The value of the attribute.
   *
   * @throws IllegalArgumentException thrown if the underlying
   * implementation doesn't recognize the attribute.
   */
  public Object getAttribute(String name) throws IllegalArgumentException
  {
    if (name.equals(FEATURE_INCREMENTAL))
    {
      return new Boolean(org.apache.xml.dtm.DTMManager.getIncremental());            
    }
    else if (name.equals(FEATURE_OPTIMIZE))
    {
      return new Boolean(m_optimize);
    }
    else if (name.equals(FEATURE_SOURCE_LOCATION))
    {
      return new Boolean(m_source_location);
    }
    else
  }

  /**
   * Create an XMLFilter that uses the given source as the
   * transformation instructions.
   *
   * @param src The source of the transformation instructions.
   *
   * @return An XMLFilter object, or null if this feature is not supported.
   *
   * @throws TransformerConfigurationException
   */
  public XMLFilter newXMLFilter(Source src)
          throws TransformerConfigurationException
  {

    Templates templates = newTemplates(src);
    if( templates==null ) return null;
    
    return newXMLFilter(templates);
  }

  /**
   * Create an XMLFilter that uses the given source as the
   * transformation instructions.
   *
   * @param src The source of the transformation instructions.
   *
   * @param templates non-null reference to Templates object.
   *
   * @return An XMLFilter object, or null if this feature is not supported.
   *
   * @throws TransformerConfigurationException
   */
  public XMLFilter newXMLFilter(Templates templates)
          throws TransformerConfigurationException
  {
    try {
      return new TrAXFilter(templates);
    } catch( TransformerConfigurationException ex ) {
      if( m_errorListener != null) {
        try {
          m_errorListener.fatalError( ex );
          return null;
        } catch( TransformerException ex1 ) {
          new TransformerConfigurationException(ex1);
        }
      }
      throw ex;
    }
  }

  /**
   * Get a TransformerHandler object that can process SAX
   * ContentHandler events into a Result, based on the transformation
   * instructions specified by the argument.
   *
   * @param src The source of the transformation instructions.
   *
   * @return TransformerHandler ready to transform SAX events.
   *
   * @throws TransformerConfigurationException
   */
  public TransformerHandler newTransformerHandler(Source src)
          throws TransformerConfigurationException
  {

    Templates templates = newTemplates(src);
    if( templates==null ) return null;
    
    return newTransformerHandler(templates);
  }

  /**
   * Get a TransformerHandler object that can process SAX
   * ContentHandler events into a Result, based on the Templates argument.
   *
   * @param templates The source of the transformation instructions.
   *
   * @return TransformerHandler ready to transform SAX events.
   * @throws TransformerConfigurationException
   */
  public TransformerHandler newTransformerHandler(Templates templates)
          throws TransformerConfigurationException
  {
    try {
      TransformerImpl transformer =
        (TransformerImpl) templates.newTransformer();
      transformer.setURIResolver(m_uriResolver);
      TransformerHandler th =
        (TransformerHandler) transformer.getInputContentHandler(true);

      return th;
    } catch( TransformerConfigurationException ex ) {
      if( m_errorListener != null ) {
        try {
          m_errorListener.fatalError( ex );
          return null;
        } catch (TransformerException ex1 ) {
          ex=new TransformerConfigurationException(ex1);
        }
      }
      throw ex;
    }
    
  }


  /**
   * Get a TransformerHandler object that can process SAX
   * ContentHandler events into a Result.
   *
   * @param src The source of the transformation instructions.
   *
   * @return TransformerHandler ready to transform SAX events.
   *
   * @throws TransformerConfigurationException
   */
  public TransformerHandler newTransformerHandler()
          throws TransformerConfigurationException
  {

    return new TransformerIdentityImpl();
  }

  /**
   * Process the source into a Transformer object.  Care must
   * be given to know that this object can not be used concurrently
   * in multiple threads.
   *
   * @param source An object that holds a URL, input stream, etc.
   *
   * @return A Transformer object capable of
   * being used for transformation purposes in a single thread.
   *
   * @throws TransformerConfigurationException May throw this during the parse when it
   *            is constructing the Templates object and fails.
   */
  public Transformer newTransformer(Source source)
          throws TransformerConfigurationException
  {
    try {
      Templates tmpl=newTemplates( source );
      /* this can happen if an ErrorListener is present and it doesn't
         throw any exception in fatalError. 
         The spec says: "a Transformer must use this interface
         instead of throwing an exception" - the newTemplates() does
         that, and returns null.
      */
      if( tmpl==null ) return null;
      Transformer transformer = tmpl.newTransformer();
      transformer.setURIResolver(m_uriResolver);
      return transformer;
    } catch( TransformerConfigurationException ex ) {
      if( m_errorListener != null ) {
        try {
          m_errorListener.fatalError( ex );
          return null;
        } catch( TransformerException ex1 ) {
          ex=new TransformerConfigurationException( ex1 );
        }
      }
      throw ex;
    }
  }

  /**
   * Create a new Transformer object that performs a copy
   * of the source to the result.
   *
   * @param source An object that holds a URL, input stream, etc.
   *
   * @return A Transformer object capable of
   * being used for transformation purposes in a single thread.
   *
   * @throws TransformerConfigurationException May throw this during
   *            the parse when it is constructing the
   *            Templates object and it fails.
   */
  public Transformer newTransformer() throws TransformerConfigurationException
  {

      return new TransformerIdentityImpl();
  }

  /**
   * Process the source into a Templates object, which is likely
   * a compiled representation of the source. This Templates object
   * may then be used concurrently across multiple threads.  Creating
   * a Templates object allows the TransformerFactory to do detailed
   * performance optimization of transformation instructions, without
   * penalizing runtime transformation.
   *
   * @param source An object that holds a URL, input stream, etc.
   * @return A Templates object capable of being used for transformation purposes.
   *
   * @throws TransformerConfigurationException May throw this during the parse when it
   *            is constructing the Templates object and fails.
   */
  public Templates newTemplates(Source source)
          throws TransformerConfigurationException
  {

    TemplatesHandler builder = newTemplatesHandler();
    String baseID = source.getSystemId();

    if (null == baseID)
    {
      try
      {
        String currentDir = System.getProperty("user.dir");
        
        if (currentDir.startsWith(java.io.File.separator))
                   + source.getClass().getName();
        else
                   + source.getClass().getName();
      }
      catch (SecurityException se)
      {

      }
    }
    else
    {
      try
      {
        baseID = SystemIDResolver.getAbsoluteURI(baseID);
      }
      catch (TransformerException te)
      {
        throw new TransformerConfigurationException(te);
      }
    }

    builder.setSystemId(baseID);

    if (source instanceof DOMSource)
    {
      DOMSource dsource = (DOMSource) source;
      Node node = dsource.getNode();

      if (null != node)
        return processFromNode(node, baseID);
      else
      {
        String messageStr = XSLMessages.createMessage(
          XSLTErrorResources.ER_ILLEGAL_DOMSOURCE_INPUT, null);

        throw new IllegalArgumentException(messageStr);
      }
    }

    try
    {
      InputSource isource = SAXSource.sourceToInputSource(source);
      XMLReader reader = null;

      if (source instanceof SAXSource)
        reader = ((SAXSource) source).getXMLReader();
        
      boolean isUserReader = (reader != null);

      if (null == reader)
      {

        try
        {
          javax.xml.parsers.SAXParserFactory factory =
            javax.xml.parsers.SAXParserFactory.newInstance();

          factory.setNamespaceAware(true);

          javax.xml.parsers.SAXParser jaxpParser = factory.newSAXParser();

          reader = jaxpParser.getXMLReader();
        }
        catch (javax.xml.parsers.ParserConfigurationException ex)
        {
          throw new org.xml.sax.SAXException(ex);
        }
        catch (javax.xml.parsers.FactoryConfigurationError ex1)
        {
          throw new org.xml.sax.SAXException(ex1.toString());
        }
        catch (NoSuchMethodError ex2){}
        catch (AbstractMethodError ame){}
      }

      if (null == reader)
        reader = XMLReaderFactory.createXMLReader();

      try
      {
        if(!isUserReader)
                            true);
      }
      catch (org.xml.sax.SAXException ex)
      {

      }

      reader.setContentHandler(builder);
      reader.parse(isource);
    }
    catch (org.xml.sax.SAXException se)
    {
      if (m_errorListener != null)
      {
        try
        {
          m_errorListener.fatalError(new TransformerException(se));
        }
        catch (TransformerException ex1)
        {
          throw new TransformerConfigurationException(ex1);
        }
      }
      else
        throw new TransformerConfigurationException(se.getMessage(), se);
    }
    catch (Exception e)
    {
      if (m_errorListener != null)
      {
        try
        {
          m_errorListener.fatalError(new TransformerException(e));

          return null;
        }
        catch (TransformerException ex1)
        {
          throw new TransformerConfigurationException(ex1);
        }
      }
      else
        throw new TransformerConfigurationException(e.getMessage(), e);
    }

    return builder.getTemplates();
  }

  /**
   * The object that implements the URIResolver interface,
   * or null.
   */
  URIResolver m_uriResolver;

  /**
   * Set an object that will be used to resolve URIs used in
   * xsl:import, etc.  This will be used as the default for the
   * transformation.
   * @param resolver An object that implements the URIResolver interface,
   * or null.
   */
  public void setURIResolver(URIResolver resolver)
  {
    m_uriResolver = resolver;
  }

  /**
   * Get the object that will be used to resolve URIs used in
   * xsl:import, etc.  This will be used as the default for the
   * transformation.
   *
   * @return The URIResolver that was set with setURIResolver.
   */
  public URIResolver getURIResolver()
  {
    return m_uriResolver;
  }

  /** The error listener.   */
  private ErrorListener m_errorListener = new DefaultErrorHandler();

  /**
   * Get the error listener in effect for the TransformerFactory.
   *
   * @return A non-null reference to an error listener.
   */
  public ErrorListener getErrorListener()
  {
    return m_errorListener;
  }

  /**
   * Set an error listener for the TransformerFactory.
   *
   * @param listener Must be a non-null reference to an ErrorListener.
   *
   * @throws IllegalArgumentException if the listener argument is null.
   */
  public void setErrorListener(ErrorListener listener)
          throws IllegalArgumentException
  {

    if (null == listener)
      throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_ERRORLISTENER, null));

    m_errorListener = listener;
  }
}
