package org.apache.xalan.lib;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;

import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xpath.XPathContext;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
/**
 */
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;

/**
 * PipeDocument is a Xalan extension element to set stylesheet params and pipes an XML 
 * document through a series of 1 or more stylesheets.
 * PipeDocument is invoked from a stylesheet as the {@link #pipeDocument pipeDocument extension element}.
 * 
 * It is accessed by specifying a namespace URI as follows:
 * <pre>
 * </pre>
 *
 * @author Donald Leslie
 */
public class PipeDocument
{
/**
 * Extension element for piping an XML document through a series of 1 or more transformations.
 * 
 * <pre>Common usage pattern: A stylesheet transforms a listing of documents to be
 * transformed into a TOC. For each document in the listing calls the pipeDocument
 * extension element to pipe that document through a series of 1 or more stylesheets 
 * to the desired output document.
 * 
 * Syntax:
 * &lt;xsl:stylesheet version="1.0"
 *                extension-element-prefixes="pipe"&gt;
 * ...
 * &lt;pipe:pipeDocument   source="source.xml" target="target.xml"&gt;
 *   &lt;stylesheet href="ss1.xsl"&gt;
 *     &lt;param name="param1" value="value1"/&gt;
 *   &lt;/stylesheet&gt;
 *   &lt;stylesheet href="ss2.xsl"&gt;
 *     &lt;param name="param1" value="value1"/&gt;
 *     &lt;param name="param2" value="value2"/&gt;
 *   &lt;/stylesheet&gt;
 *   &lt;stylesheet href="ss1.xsl"/&gt;     
 * &lt;/pipe:pipeDocument&gt;
 * 
 * Notes:</pre>
 * <ul>
 *   <li>The base URI for the source attribute is the XML "listing" document.<li/>
 *   <li>The target attribute is taken as is (base is the current user directory).<li/>
 *   <li>The stylsheet containg the extension element is the base URI for the
 *   stylesheet hrefs.<li/>
 * </ul>
 */
  public void pipeDocument(XSLProcessorContext context, ElemExtensionCall elem)
	  throws TransformerException, TransformerConfigurationException, 
         SAXException, IOException, FileNotFoundException	   
  {
    try
    {
      SAXTransformerFactory saxTFactory = (SAXTransformerFactory) TransformerFactory.newInstance();
      
      String source =  elem.getAttribute("source", 
                                         context.getContextNode(),
                                         context.getTransformer());
      TransformerImpl transImpl = context.getTransformer();

      String baseURLOfSource = transImpl.getBaseURLOfSource();
      String absSourceURL = SystemIDResolver.getAbsoluteURI(source, baseURLOfSource);      

      String target =  elem.getAttribute("target", 
                                         context.getContextNode(),
                                         context.getTransformer());
      
      XPathContext xctxt = context.getTransformer().getXPathContext();
      int xt = xctxt.getDTMHandleFromNode(context.getContextNode());
 
      String sysId = elem.getSystemId();
      
      NodeList ssNodes = null;
      NodeList paramNodes = null;
      Node ssNode = null;
      Node paramNode = null;
      if (elem.hasChildNodes())
      {
        ssNodes = elem.getChildNodes();        
        Vector vTHandler = new Vector(ssNodes.getLength());
        
        for (int i = 0; i < ssNodes.getLength(); i++)
        {
          ssNode = ssNodes.item(i);
          if (ssNode.getNodeType() == Node.ELEMENT_NODE
              && ((Element)ssNode).getTagName().equals("stylesheet")
              && ssNode instanceof ElemLiteralResult)
          {
            AVT avt = ((ElemLiteralResult)ssNode).getLiteralResultAttribute("href");
            String href = avt.evaluate(xctxt,xt, elem);
            String absURI = SystemIDResolver.getAbsoluteURI(href, sysId);
            Templates tmpl = saxTFactory.newTemplates(new StreamSource(absURI));
            TransformerHandler tHandler = saxTFactory.newTransformerHandler(tmpl);
            Transformer trans = tHandler.getTransformer();
            
            vTHandler.addElement(tHandler);

            paramNodes = ssNode.getChildNodes();
            for (int j = 0; j < paramNodes.getLength(); j++)
            {
              paramNode = paramNodes.item(j);
              if (paramNode.getNodeType() == Node.ELEMENT_NODE 
                  && ((Element)paramNode).getTagName().equals("param")
                  && paramNode instanceof ElemLiteralResult)
              {
                 avt = ((ElemLiteralResult)paramNode).getLiteralResultAttribute("name");
                 String pName = avt.evaluate(xctxt,xt, elem);
                 avt = ((ElemLiteralResult)paramNode).getLiteralResultAttribute("value");
                 String pValue = avt.evaluate(xctxt,xt, elem);
                 trans.setParameter(pName, pValue);
               } 
             }
           }
         }
         usePipe(vTHandler, absSourceURL, target);
       }
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
  }
  /**
   * Uses a Vector of TransformerHandlers to pipe XML input document through
   * a series of 1 or more transformations. Called by {@link #pipeDocument}.
   * 
   * @param vTHandler Vector of Transformation Handlers (1 per stylesheet).
   * @param source absolute URI to XML input
   * @param target absolute path to transformation output.
   */
  public void usePipe(Vector vTHandler, String source, String target)
          throws TransformerException, TransformerConfigurationException, 
                 FileNotFoundException, IOException, SAXException, SAXNotRecognizedException
  {
    XMLReader reader = XMLReaderFactory.createXMLReader();
    TransformerHandler tHFirst = (TransformerHandler)vTHandler.firstElement();
    reader.setContentHandler(tHFirst);
    for (int i = 1; i < vTHandler.size(); i++)
    {
      TransformerHandler tHFrom = (TransformerHandler)vTHandler.elementAt(i-1);
      TransformerHandler tHTo = (TransformerHandler)vTHandler.elementAt(i);
      tHFrom.setResult(new SAXResult(tHTo));      
    }
    TransformerHandler tHLast = (TransformerHandler)vTHandler.lastElement();
    Transformer trans = tHLast.getTransformer();
    Properties outputProps = trans.getOutputProperties();
    Serializer serializer = SerializerFactory.getSerializer(outputProps);
    
    FileOutputStream out = new FileOutputStream(target);
    try 
    {
      serializer.setOutputStream(out);
      tHLast.setResult(new SAXResult(serializer.asContentHandler()));
      reader.parse(source);
    }
    finally 
    {
      if (out != null)
        out.close();
    }    
  }
}
