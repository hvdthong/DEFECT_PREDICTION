package org.apache.xalan.processor;

import org.apache.xml.utils.TreeWalker;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;

import java.net.URL;

import java.io.IOException;

import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;

import org.apache.xml.utils.SystemIDResolver;

/**
 * TransformerFactory class for xsl:include markup.
 */
class ProcessorInclude extends XSLTElementProcessor
{

  /**
   * The base URL of the XSL document.
   * @serial
   */
  private String m_href = null;

  /**
   * Get the base identifier with which this stylesheet is associated.
   *
   * @return non-null reference to the href attribute string, or 
   *         null if setHref has not been called.
   */
  public String getHref()
  {
    return m_href;
  }

  /**
   * Get the base identifier with which this stylesheet is associated.
   *
   * @param baseIdent Should be a non-null reference to a valid URL string.
   */
  public void setHref(String baseIdent)
  {
    m_href = baseIdent;
  }

  /**
   * Get the stylesheet type associated with an included stylesheet
   *
   * @return the type of the stylesheet
   */
  protected int getStylesheetType()
  {
    return StylesheetHandler.STYPE_INCLUDE;
  }

  /**
   * Get the error number associated with this type of stylesheet including itself
   *
   * @return the appropriate error number
   */
  protected int getStylesheetInclErr()
  {
    return XSLTErrorResources.ER_STYLESHEET_INCLUDES_ITSELF;
  }

  /**
   * Receive notification of the start of an xsl:include element.
   *
   * @param handler The calling StylesheetHandler/TemplatesBuilder.
   * @param uri The Namespace URI, or the empty string if the
   *        element has no Namespace URI or if Namespace
   *        processing is not being performed.
   * @param localName The local name (without prefix), or the
   *        empty string if Namespace processing is not being
   *        performed.
   * @param rawName The raw XML 1.0 name (with prefix), or the
   *        empty string if raw names are not available.
   * @param attributes The attributes attached to the element.  If
   *        there are no attributes, it shall be an empty
   *        Attributes object.
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   */
  public void startElement(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws org.xml.sax.SAXException
  {


    setPropertiesFromAttributes(handler, rawName, attributes, this);

    try
    {
      String hrefUrl = SystemIDResolver.getAbsoluteURI(getHref(),
                           handler.getBaseIdentifier());    

      if (handler.importStackContains(hrefUrl))
      {
        throw new org.xml.sax.SAXException(
          XSLMessages.createMessage(
      }

      handler.pushImportURL(hrefUrl);

      int savedStylesheetType = handler.getStylesheetType();

      handler.setStylesheetType(this.getStylesheetType());
      handler.pushNewNamespaceSupport();

      try
      {
        parse(handler, uri, localName, rawName, attributes);
      }
      finally
      {
        handler.setStylesheetType(savedStylesheetType);
        handler.popImportURL();
        handler.popNamespaceSupport();
      }
    }
    catch(TransformerException te)
    {
      handler.error(te.getMessage(), te);
    }
  }

  /**
   * Set off a new parse for an included or imported stylesheet.  This will 
   * set the {@link StylesheetHandler} to a new state, and recurse in with 
   * a new set of parse events.  Once this function returns, the state of 
   * the StylesheetHandler should be restored.
   *
   * @param handler non-null reference to current StylesheetHandler that is constructing the Templates.
   * @param uri The Namespace URI, which should be the XSLT namespace.
   * @param localName The local name (without prefix), which should be "include" or "import".
   * @param rawName The qualified name (with prefix).
   * @param attributes The list of attributes on the xsl:include or xsl:import element.
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   */
  protected void parse(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws org.xml.sax.SAXException
  {
    TransformerFactoryImpl processor = handler.getStylesheetProcessor();
    URIResolver uriresolver = processor.getURIResolver();

    try
    {
      Source source = null;

      if (null != uriresolver)
      {
        source = uriresolver.resolve(getHref(),
                                     handler.getBaseIdentifier());

        if (null != source && source instanceof DOMSource)
        {
          Node node = ((DOMSource)source).getNode();
          
          String systemId = source.getSystemId();
          if (systemId == null)
          {
            systemId = SystemIDResolver.getAbsoluteURI(getHref(),
                         handler.getBaseIdentifier());
            
          }
          
          TreeWalker walker = new TreeWalker(handler, new org.apache.xpath.DOM2Helper(), systemId);

          try
          {
            walker.traverse(node);
          }
          catch(org.xml.sax.SAXException se)
          {
            throw new TransformerException(se);
          }
          return;
        }
      }
      
      if(null == source)
      {
        String absURL = SystemIDResolver.getAbsoluteURI(getHref(),
                          handler.getBaseIdentifier());

        source = new StreamSource(absURL);
      }
      
      XMLReader reader = null;
      
      if(source instanceof SAXSource)
      {
        SAXSource saxSource = (SAXSource)source;
      }
      
      boolean isUserReader = (reader != null);
      
      InputSource inputSource = SAXSource.sourceToInputSource(source);

      if (null == reader)
      {  
        try {
          javax.xml.parsers.SAXParserFactory factory=
                                                     javax.xml.parsers.SAXParserFactory.newInstance();
          factory.setNamespaceAware( true );
          javax.xml.parsers.SAXParser jaxpParser=
                                                 factory.newSAXParser();
          reader=jaxpParser.getXMLReader();
          
        } catch( javax.xml.parsers.ParserConfigurationException ex ) {
          throw new org.xml.sax.SAXException( ex );
        } catch( javax.xml.parsers.FactoryConfigurationError ex1 ) {
            throw new org.xml.sax.SAXException( ex1.toString() );
        } 
        catch( NoSuchMethodError ex2 ) 
        {
        }
        catch (AbstractMethodError ame){}
      }
      if (null == reader)
        reader = XMLReaderFactory.createXMLReader();

      if (null != reader)
      {
        reader.setContentHandler(handler);
        try
        {
          if(!isUserReader)
                              true);
        }
        catch(org.xml.sax.SAXException se) {}
        
        handler.pushBaseIndentifier(inputSource.getSystemId());

        try
        {
          reader.parse(inputSource);
        }
        finally
        {
          handler.popBaseIndentifier();
        }
      }
    }
    catch (IOException ioe)
    {
      handler.error(XSLTErrorResources.ER_IOEXCEPTION,
                    new Object[]{ getHref() }, ioe);
    }
    catch(TransformerException te)
    {
      handler.error(te.getMessage(), te);
    }
  }
}
