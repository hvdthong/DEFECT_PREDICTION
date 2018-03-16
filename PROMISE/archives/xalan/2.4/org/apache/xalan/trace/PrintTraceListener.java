package org.apache.xalan.trace;

import java.io.*;

import org.w3c.dom.*;
import org.w3c.dom.traversal.NodeIterator;

import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xalan.templates.Constants;
import org.apache.xpath.axes.ContextNodeList;

import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.DTMNodeProxy;

/**
 * <meta name="usage" content="advanced"/>
 * Implementation of the TraceListener interface that
 * prints each event to standard out as it occurs.
 *
 * @see org.apache.xalan.trace.TracerEvent
 */
public class PrintTraceListener implements TraceListenerEx2
{

  /**
   * Construct a trace listener.
   *
   * @param pw PrintWriter to use for tracing events
   */
  public PrintTraceListener(java.io.PrintWriter pw)
  {
    m_pw = pw;
  }

  /**
   * The print writer where the events should be written.
   */
  java.io.PrintWriter m_pw;

  /**
   * This needs to be set to true if the listener is to print an event whenever a template is invoked.
   */
  public boolean m_traceTemplates = false;

  /**
   * Set to true if the listener is to print events that occur as each node is 'executed' in the stylesheet.
   */
  public boolean m_traceElements = false;

  /**
   * Set to true if the listener is to print information after each result-tree generation event.
   */
  public boolean m_traceGeneration = false;

  /**
   * Set to true if the listener is to print information after each selection event.
   */
  public boolean m_traceSelection = false;

  /**
   * Print information about a TracerEvent.
   *
   * @param ev the trace event.
   */
  public void _trace(TracerEvent ev)
  {

    switch (ev.m_styleNode.getXSLToken())
    {
    case Constants.ELEMNAME_TEXTLITERALRESULT :
      if (m_traceElements)
      {
        m_pw.print(ev.m_styleNode.getSystemId()+ " Line #" + ev.m_styleNode.getLineNumber() + ", "
                   + "Column #" + ev.m_styleNode.getColumnNumber() + " -- "
                   + ev.m_styleNode.getNodeName() + ": ");

        ElemTextLiteral etl = (ElemTextLiteral) ev.m_styleNode;
        String chars = new String(etl.getChars(), 0, etl.getChars().length);

        m_pw.println("    " + chars.trim());
      }
      break;
    case Constants.ELEMNAME_TEMPLATE :
      if (m_traceTemplates || m_traceElements)
      {
        ElemTemplate et = (ElemTemplate) ev.m_styleNode;

        m_pw.print(et.getSystemId()+ " Line #" + et.getLineNumber() + ", " + "Column #"
                   + et.getColumnNumber() + ": " + et.getNodeName() + " ");

        if (null != et.getMatch())
        {
          m_pw.print("match='" + et.getMatch().getPatternString() + "' ");
        }

        if (null != et.getName())
        {
          m_pw.print("name='" + et.getName() + "' ");
        }

        m_pw.println();
      }
      break;
    default :
      if (m_traceElements)
      {
        m_pw.println(ev.m_styleNode.getSystemId()+ " Line #" + ev.m_styleNode.getLineNumber() + ", "
                     + "Column #" + ev.m_styleNode.getColumnNumber() + ": "
                     + ev.m_styleNode.getNodeName());
      }
    }
  }
  
  int m_indent = 0;
  
  /**
   * Print information about a TracerEvent.
   *
   * @param ev the trace event.
   */
  public void trace(TracerEvent ev)
  {
	_trace(ev);
  }
  
  /**
   * Method that is called when the end of a trace event occurs.
   * The method is blocking.  It must return before processing continues.
   *
   * @param ev the trace event.
   */
  public void traceEnd(TracerEvent ev)
  {
  }


  /**
   * Method that is called just after a select attribute has been evaluated.
   *
   * @param ev the generate event.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void selected(SelectionEvent ev) throws javax.xml.transform.TransformerException
  {

    if (m_traceSelection)
    {
      ElemTemplateElement ete = (ElemTemplateElement) ev.m_styleNode;
      Node sourceNode = ev.m_sourceNode;
      
      SourceLocator locator = null;
      if (sourceNode instanceof DTMNodeProxy)
      {
        int nodeHandler = ((DTMNodeProxy)sourceNode).getDTMNodeNumber();      
        locator = ((DTMNodeProxy)sourceNode).getDTM().getSourceLocatorFor(nodeHandler);
      }

      if (locator != null)      
        m_pw.println("Selected source node '" + sourceNode.getNodeName()
                 + "', at " + locator);
      else
        m_pw.println("Selected source node '" + sourceNode.getNodeName() +"'");

      if (ev.m_styleNode.getLineNumber() == 0)
      {

        ElemTemplateElement parent =
          (ElemTemplateElement) ete.getParentElem();

        if (parent == ete.getStylesheetRoot().getDefaultRootRule())
        {
          m_pw.print("(default root rule) ");
        }
        else if (parent == ete.getStylesheetRoot().getDefaultTextRule())
        {
          m_pw.print("(default text rule) ");
        }
        else if (parent == ete.getStylesheetRoot().getDefaultRule())
        {
          m_pw.print("(default rule) ");
        }

        m_pw.print(ete.getNodeName() + ", " + ev.m_attributeName + "='"
                   + ev.m_xpath.getPatternString() + "': ");
      }
      else
      {
        m_pw.print(ev.m_styleNode.getSystemId()+ " Line #" + ev.m_styleNode.getLineNumber() + ", "
                   + "Column #" + ev.m_styleNode.getColumnNumber() + ": "
                   + ete.getNodeName() + ", " + ev.m_attributeName + "='"
                   + ev.m_xpath.getPatternString() + "': ");
      }

                        if (ev.m_selection.getType() == ev.m_selection.CLASS_NODESET)
                        {
                                m_pw.println();
                                
                                org.apache.xml.dtm.DTMIterator nl = ev.m_selection.iter();

                                try
                                {
                                        nl = nl.cloneWithReset();
                                }
                                catch(CloneNotSupportedException cnse)
                                {
                                        m_pw.println("     [Can't trace nodelist because it it threw a CloneNotSupportedException]");
                                        return;
                                }
                                int pos = nl.nextNode();

                                if (DTM.NULL == pos)
                                {
                                        m_pw.println("     [empty node list]");
                                }
                                else
                                {
                                        while (DTM.NULL != pos)
                                        {
                                                DTM dtm = ev.m_processor.getXPathContext().getDTM(pos);
                                                m_pw.print("     ");
                                                m_pw.print(Integer.toHexString(pos));
                                                m_pw.print(": ");
                                                m_pw.println(dtm.getNodeName(pos));

                                                pos = nl.nextNode();
                                        }
                                }        
                        }
      else
      {
        m_pw.println(ev.m_selection.str());
      }
    }
  }
  
  /**
   * Method that is called after an xsl:apply-templates or xsl:for-each 
   * selection occurs.
   *
   * @param ev the generate event.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void selectEnd(EndSelectionEvent ev) 
     throws javax.xml.transform.TransformerException
  {
  }


  /**
   * Print information about a Generate event.
   *
   * @param ev the trace event.
   */
  public void generated(GenerateEvent ev)
  {

    if (m_traceGeneration)
    {
      switch (ev.m_eventtype)
      {
      case GenerateEvent.EVENTTYPE_STARTDOCUMENT :
        m_pw.println("STARTDOCUMENT");
        break;
      case GenerateEvent.EVENTTYPE_ENDDOCUMENT :
        m_pw.println("ENDDOCUMENT");
        break;
      case GenerateEvent.EVENTTYPE_STARTELEMENT :
        m_pw.println("STARTELEMENT: " + ev.m_name);
        break;
      case GenerateEvent.EVENTTYPE_ENDELEMENT :
        m_pw.println("ENDELEMENT: " + ev.m_name);
        break;
      case GenerateEvent.EVENTTYPE_CHARACTERS :
      {
        String chars = new String(ev.m_characters, ev.m_start, ev.m_length);

        m_pw.println("CHARACTERS: " + chars);
      }
      break;
      case GenerateEvent.EVENTTYPE_CDATA :
      {
        String chars = new String(ev.m_characters, ev.m_start, ev.m_length);

        m_pw.println("CDATA: " + chars);
      }
      break;
      case GenerateEvent.EVENTTYPE_COMMENT :
        m_pw.println("COMMENT: " + ev.m_data);
        break;
      case GenerateEvent.EVENTTYPE_PI :
        m_pw.println("PI: " + ev.m_name + ", " + ev.m_data);
        break;
      case GenerateEvent.EVENTTYPE_ENTITYREF :
        m_pw.println("ENTITYREF: " + ev.m_name);
        break;
      case GenerateEvent.EVENTTYPE_IGNORABLEWHITESPACE :
        m_pw.println("IGNORABLEWHITESPACE");
        break;
      }
    }
  }
  

}
