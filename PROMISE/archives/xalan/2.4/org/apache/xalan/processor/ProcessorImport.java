package org.apache.xalan.processor;

import org.apache.xml.utils.TreeWalker;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;

import java.net.URL;

import java.io.IOException;

import javax.xml.transform.URIResolver;

import org.w3c.dom.Node;

/**
 * This class processes parse events for an xsl:import element.
 */
class ProcessorImport extends ProcessorInclude
{

  /**
   * Get the stylesheet type associated with an imported stylesheet
   *
   * @return the type of the stylesheet
   */
  protected int getStylesheetType()
  {
    return StylesheetHandler.STYPE_IMPORT;
  }

  /**
   * Get the error number associated with this type of stylesheet importing itself
   *
   * @return the appropriate error number
   */
  protected int getStylesheetInclErr()
  {
    return XSLTErrorResources.ER_IMPORTING_ITSELF;
  }

}
