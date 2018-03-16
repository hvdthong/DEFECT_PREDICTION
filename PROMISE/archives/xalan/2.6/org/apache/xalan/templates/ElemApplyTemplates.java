package org.apache.xalan.templates;

import java.util.Vector;

import javax.xml.transform.TransformerException;

import org.apache.xalan.transformer.StackGuard;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.QName;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.xml.sax.SAXException;

/**
 * Implement xsl:apply-templates.
 * <pre>
 * &amp;!ELEMENT xsl:apply-templates (xsl:sort|xsl:with-param)*>
 * &amp;!ATTLIST xsl:apply-templates
 *   select %expr; "node()"
 *   mode %qname; #IMPLIED
 * &amp;
 * </pre>
 * @xsl.usage advanced
 */
public class ElemApplyTemplates extends ElemCallTemplate
{

  /**
   * mode %qname; #IMPLIED
   * @serial
   */
  private QName m_mode = null;

  /**
   * Set the mode attribute for this element.
   *
   */
  public void setMode(QName mode)
  {
    m_mode = mode;
  }

  /**
   * Get the mode attribute for this element.
   *
   * @return The mode attribute for this element
   */
  public QName getMode()
  {
    return m_mode;
  }

  /**
   * Tells if this belongs to a default template,
   * in which case it will act different with
   * regard to processing modes.
   * @serial
   */
  private boolean m_isDefaultTemplate = false;
  

  /**
   * Set if this belongs to a default template,
   * in which case it will act different with
   * regard to processing modes.
   *
   * @param b boolean value to set.
   */
  public void setIsDefaultTemplate(boolean b)
  {
    m_isDefaultTemplate = b;
  }

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return Token ID for this element types
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_APPLY_TEMPLATES;
  }
  
  /**
   * This function is called after everything else has been
   * recomposed, and allows the template to set remaining
   * values that may be based on some other property that
   * depends on recomposition.
   */
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
  }

  /**
   * Return the node name.
   *
   * @return Element name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_APPLY_TEMPLATES_STRING;
  }

  /**
   * Apply the context node to the matching templates.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(TransformerImpl transformer) throws TransformerException
  {

    transformer.pushCurrentTemplateRuleIsNull(false);

    boolean pushMode = false;

    try
    {
      QName mode = transformer.getMode();

      if (!m_isDefaultTemplate)
      {
        if (((null == mode) && (null != m_mode))
                || ((null != mode) &&!mode.equals(m_mode)))
        {
          pushMode = true;

          transformer.pushMode(m_mode);
        }
      }
      if (TransformerImpl.S_DEBUG)
        transformer.getTraceManager().fireTraceEvent(this);

      transformSelectedNodes(transformer);
    }
    finally
    {
      if (TransformerImpl.S_DEBUG)
        transformer.getTraceManager().fireTraceEndEvent(this);

      if (pushMode)
        transformer.popMode();

      transformer.popCurrentTemplateRuleIsNull();
    }
  }

  
  /**
   * Perform a query if needed, and call transformNode for each child.
   *
   * @param transformer non-null reference to the the current transform-time state.
   * @param template The owning template context.
   *
   * @throws TransformerException Thrown in a variety of circumstances.
   * @xsl.usage advanced
   */
  public void transformSelectedNodes(TransformerImpl transformer)
            throws TransformerException
  {

    final XPathContext xctxt = transformer.getXPathContext();
    final int sourceNode = xctxt.getCurrentNode();
    DTMIterator sourceNodes = m_selectExpression.asIterator(xctxt, sourceNode);
    VariableStack vars = xctxt.getVarStack();
    int nParams = getParamElemCount();
    int thisframe = vars.getStackFrame();
    StackGuard guard = transformer.getStackGuard();
    boolean check = (guard.getRecursionLimit() > -1) ? true : false;
    
    boolean pushContextNodeListFlag = false;
      
    try
    {

            xctxt.pushCurrentNode(DTM.NULL);
            xctxt.pushCurrentExpressionNode(DTM.NULL);
            xctxt.pushSAXLocatorNull();
            transformer.pushElemTemplateElement(null);
      final Vector keys = (m_sortElems == null)
                          ? null
                          : transformer.processSortKeys(this, sourceNode);

      if (null != keys)
        sourceNodes = sortNodes(xctxt, keys, sourceNodes);
            
      if (TransformerImpl.S_DEBUG)
      {
        transformer.getTraceManager().fireSelectedEvent(sourceNode, this,
                "select", new XPath(m_selectExpression),
                new org.apache.xpath.objects.XNodeSet(sourceNodes));
      }

      final SerializationHandler rth = transformer.getSerializationHandler();
      final StylesheetRoot sroot = transformer.getStylesheet();
      final TemplateList tl = sroot.getTemplateListComposed();
      final boolean quiet = transformer.getQuietConflictWarnings();
      
      DTM dtm = xctxt.getDTM(sourceNode);
      
      int argsFrame = -1;
      if(nParams > 0)
      {
        argsFrame = vars.link(nParams);
        vars.setStackFrame(thisframe);
        
        for (int i = 0; i < nParams; i++) 
        {
          ElemWithParam ewp = m_paramElems[i];
          if (TransformerImpl.S_DEBUG)
            transformer.getTraceManager().fireTraceEvent(ewp);
          XObject obj = ewp.getValue(transformer, sourceNode);
          if (TransformerImpl.S_DEBUG)
            transformer.getTraceManager().fireTraceEndEvent(ewp);
          
          vars.setLocalVariable(i, obj, argsFrame);
        }
        vars.setStackFrame(argsFrame);
      }
      
      xctxt.pushContextNodeList(sourceNodes);
      pushContextNodeListFlag = true;
      
      IntStack currentNodes = xctxt.getCurrentNodeStack();
      
      IntStack currentExpressionNodes = xctxt.getCurrentExpressionNodeStack();     
      
      
      int child;
      while (DTM.NULL != (child = sourceNodes.nextNode()))
      {
        currentNodes.setTop(child);
        currentExpressionNodes.setTop(child);

        if(xctxt.getDTM(child) != dtm)
        {
          dtm = xctxt.getDTM(child);
        }
        
        final int exNodeType = dtm.getExpandedTypeID(child);

        final int nodeType = dtm.getNodeType(child);

        final QName mode = transformer.getMode();

        ElemTemplate template = tl.getTemplateFast(xctxt, child, exNodeType, mode, 
                                      -1, quiet, dtm);

        if (null == template)
        {
          switch (nodeType)
          {
          case DTM.DOCUMENT_FRAGMENT_NODE :
          case DTM.ELEMENT_NODE :
            template = sroot.getDefaultRule();
            break;
          case DTM.ATTRIBUTE_NODE :
          case DTM.CDATA_SECTION_NODE :
          case DTM.TEXT_NODE :
            transformer.pushPairCurrentMatched(sroot.getDefaultTextRule(), child);
            transformer.setCurrentElement(sroot.getDefaultTextRule());
            dtm.dispatchCharactersEvents(child, rth, false);
            transformer.popCurrentMatched();
            continue;
          case DTM.DOCUMENT_NODE :
            template = sroot.getDefaultRootRule();
            break;
          default :

            continue;
          }
        }
        else
        {
        	transformer.setCurrentElement(template);
        }
                
        transformer.pushPairCurrentMatched(template, child);
        if (check)
	        guard.checkForInfinateLoop();

        if(template.m_frameSize > 0)
        {
          xctxt.pushRTFContext();
          vars.link(template.m_frameSize);
          if(/* nParams > 0 && */ template.m_inArgsSize > 0)
          {
            int paramIndex = 0;
            for (ElemTemplateElement elem = template.getFirstChildElem(); 
                 null != elem; elem = elem.getNextSiblingElem()) 
            {
              if(Constants.ELEMNAME_PARAMVARIABLE == elem.getXSLToken())
              {
                ElemParam ep = (ElemParam)elem;
                
                int i;
                for (i = 0; i < nParams; i++) 
                {
                  ElemWithParam ewp = m_paramElems[i];
                  if(ewp.m_qnameID == ep.m_qnameID)
                  {
                    XObject obj = vars.getLocalVariable(i, argsFrame);
                    vars.setLocalVariable(paramIndex, obj);
                    break;
                  }
                }
                if(i == nParams)
                  vars.setLocalVariable(paramIndex, null);
              }
              else
                break;
              paramIndex++;
            }
            
          }
        }
        else
        	currentFrameBottom = 0;

        if (TransformerImpl.S_DEBUG)
          transformer.getTraceManager().fireTraceEvent(template);

        for (ElemTemplateElement t = template.m_firstChild; 
             t != null; t = t.m_nextSibling)
        {
          xctxt.setSAXLocator(t);
          try
          {
          	transformer.pushElemTemplateElement(t);
          	t.execute(transformer);
          }
          finally
          {
          	transformer.popElemTemplateElement();
          }
        }
        
        if (TransformerImpl.S_DEBUG)
	      transformer.getTraceManager().fireTraceEndEvent(template); 
	    
        if(template.m_frameSize > 0)
        {
          vars.unlink(currentFrameBottom);
          xctxt.popRTFContext();
        }
          
        transformer.popCurrentMatched();
        
    }
    catch (SAXException se)
    {
      transformer.getErrorListener().fatalError(new TransformerException(se));
    }
    finally
    {
      if (TransformerImpl.S_DEBUG)
        transformer.getTraceManager().fireSelectedEndEvent(sourceNode, this,
                "select", new XPath(m_selectExpression),
                new org.apache.xpath.objects.XNodeSet(sourceNodes));
      
      if(nParams > 0)
        vars.unlink(thisframe);
      xctxt.popSAXLocator();
      if (pushContextNodeListFlag) xctxt.popContextNodeList();
      transformer.popElemTemplateElement();
      xctxt.popCurrentExpressionNode();
      xctxt.popCurrentNode();
      sourceNodes.detach();
    }
  }

}
