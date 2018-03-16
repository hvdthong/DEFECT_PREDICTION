package org.apache.xalan.templates;

import org.apache.xml.utils.res.XResourceBundle;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.*;

import java.util.*;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import org.apache.xpath.*;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.compiler.XPathParser;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverDefault;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xalan.res.*;
import org.apache.xalan.transformer.DecimalToRoman;
import org.apache.xalan.transformer.CountersTable;
import org.apache.xalan.transformer.ResultTreeHandler;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.NodeVector;

import javax.xml.transform.TransformerException;


/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:number.
 * <pre>
 * <!ELEMENT xsl:number EMPTY>
 * <!ATTLIST xsl:number
 *    level (single|multiple|any) "single"
 *    count %pattern; #IMPLIED
 *    from %pattern; #IMPLIED
 *    value %expr; #IMPLIED
 *    format %avt; '1'
 *    lang %avt; #IMPLIED
 *    letter-value %avt; #IMPLIED
 *    grouping-separator %avt; #IMPLIED
 *    grouping-size %avt; #IMPLIED
 * >
 * </pre>
 */
public class ElemNumber extends ElemTemplateElement 
{

    private class MyPrefixResolver implements PrefixResolver {
        
        DTM dtm;
        int handle;
        boolean handleNullPrefix;
        
		/**
		 * Constructor for MyPrefixResolver.
		 * @param xpathExpressionContext
		 */
		public MyPrefixResolver(Node xpathExpressionContext, DTM dtm, int handle, boolean handleNullPrefix) {
            this.dtm = dtm;
            this.handle = handle;
            this.handleNullPrefix = handleNullPrefix;
		}

    	/**
		 * @see PrefixResolver#getNamespaceForPrefix(String, Node)
		 */
		public String getNamespaceForPrefix(String prefix) {
            return dtm.getNamespaceURI(handle);
		}
        
        /**
         * @see PrefixResolver#getNamespaceForPrefix(String, Node)
         * this shouldn't get called.
         */
        public String getNamespaceForPrefix(String prefix, Node context) {
            return getNamespaceForPrefix(prefix);
        }

		/**
		 * @see PrefixResolver#getBaseIdentifier()
		 */
		public String getBaseIdentifier() {
			return ElemNumber.this.getBaseIdentifier();
		}

		/**
		 * @see PrefixResolver#handlesNullPrefixes()
		 */
		public boolean handlesNullPrefixes() {
			return handleNullPrefix;
		}

}
    
  /**
   * Only nodes are counted that match this pattern.
   * @serial
   */
  private XPath m_countMatchPattern = null;

  /**
   * Set the "count" attribute.
   * The count attribute is a pattern that specifies what nodes
   * should be counted at those levels. If count attribute is not
   * specified, then it defaults to the pattern that matches any
   * node with the same node type as the current node and, if the
   * current node has an expanded-name, with the same expanded-name
   * as the current node.
   *
   * @param v Value to set for "count" attribute. 
   */
  public void setCount(XPath v)
  {
    m_countMatchPattern = v;
  }

  /**
   * Get the "count" attribute.
   * The count attribute is a pattern that specifies what nodes
   * should be counted at those levels. If count attribute is not
   * specified, then it defaults to the pattern that matches any
   * node with the same node type as the current node and, if the
   * current node has an expanded-name, with the same expanded-name
   * as the current node.
   *
   * @return Value of "count" attribute.
   */
  public XPath getCount()
  {
    return m_countMatchPattern;
  }

  /**
   * Specifies where to count from.
   * For level="single" or level="multiple":
   * Only ancestors that are searched are
   * those that are descendants of the nearest ancestor that matches
   * the from pattern.
   * For level="any:
   * Only nodes after the first node before the
   * current node that match the from pattern are considered.
   * @serial
   */
  private XPath m_fromMatchPattern = null;

  /**
   * Set the "from" attribute. Specifies where to count from.
   * For level="single" or level="multiple":
   * Only ancestors that are searched are
   * those that are descendants of the nearest ancestor that matches
   * the from pattern.
   * For level="any:
   * Only nodes after the first node before the
   * current node that match the from pattern are considered.
   *
   * @param v Value to set for "from" attribute.
   */
  public void setFrom(XPath v)
  {
    m_fromMatchPattern = v;
  }

  /**
   * Get the "from" attribute.
   * For level="single" or level="multiple":
   * Only ancestors that are searched are
   * those that are descendants of the nearest ancestor that matches
   * the from pattern.
   * For level="any:
   * Only nodes after the first node before the
   * current node that match the from pattern are considered.
   *
   * @return Value of "from" attribute.
   */
  public XPath getFrom()
  {
    return m_fromMatchPattern;
  }

  /**
   * When level="single", it goes up to the first node in the ancestor-or-self axis
   * that matches the count pattern, and constructs a list of length one containing
   * one plus the number of preceding siblings of that ancestor that match the count
   * pattern. If there is no such ancestor, it constructs an empty list. If the from
   * attribute is specified, then the only ancestors that are searched are those
   * that are descendants of the nearest ancestor that matches the from pattern.
   * Preceding siblings has the same meaning here as with the preceding-sibling axis.
   *
   * When level="multiple", it constructs a list of all ancestors of the current node
   * in document order followed by the element itself; it then selects from the list
   * those nodes that match the count pattern; it then maps each node in the list to
   * one plus the number of preceding siblings of that node that match the count pattern.
   * If the from attribute is specified, then the only ancestors that are searched are
   * those that are descendants of the nearest ancestor that matches the from pattern.
   * Preceding siblings has the same meaning here as with the preceding-sibling axis.
   *
   * When level="any", it constructs a list of length one containing the number of
   * nodes that match the count pattern and belong to the set containing the current
   * node and all nodes at any level of the document that are before the current node
   * in document order, excluding any namespace and attribute nodes (in other words
   * the union of the members of the preceding and ancestor-or-self axes). If the
   * from attribute is specified, then only nodes after the first node before the
   * current node that match the from pattern are considered.
   * @serial
   */
  private int m_level = Constants.NUMBERLEVEL_SINGLE;

  /**
   * Set the "level" attribute.
   * The level attribute specifies what levels of the source tree should
   * be considered; it has the values single, multiple or any. The default
   * is single.
   *
   * @param v Value to set for "level" attribute.
   */
  public void setLevel(int v)
  {
    m_level = v;
  }

  /**
   * Get the "level" attribute.
   * The level attribute specifies what levels of the source tree should
   * be considered; it has the values single, multiple or any. The default
   * is single.
   *
   * @return Value of "level" attribute.
   */
  public int getLevel()
  {
    return m_level;
  }

  /**
   * The value attribute contains an expression. The expression is evaluated
   * and the resulting object is converted to a number as if by a call to the
   * number function.
   * @serial
   */
  private XPath m_valueExpr = null;

  /**
   * Set the "value" attribute.
   * The value attribute contains an expression. The expression is evaluated
   * and the resulting object is converted to a number as if by a call to the
   * number function.
   *
   * @param v Value to set for "value" attribute.
   */
  public void setValue(XPath v)
  {
    m_valueExpr = v;
  }

  /**
   * Get the "value" attribute.
   * The value attribute contains an expression. The expression is evaluated
   * and the resulting object is converted to a number as if by a call to the
   * number function.
   *
   * @return Value of "value" attribute.
   */
  public XPath getValue()
  {
    return m_valueExpr;
  }

  /**
   * The "format" attribute is used to control conversion of a list of
   * numbers into a string.
   * @serial
   */
  private AVT m_format_avt = null;

  /**
   * Set the "format" attribute.
   * The "format" attribute is used to control conversion of a list of
   * numbers into a string.
   *
   * @param v Value to set for "format" attribute.
   */
  public void setFormat(AVT v)
  {
    m_format_avt = v;
  }

  /**
   * Get the "format" attribute.
   * The "format" attribute is used to control conversion of a list of
   * numbers into a string.
   *
   * @return Value of "format" attribute.
   */
  public AVT getFormat()
  {
    return m_format_avt;
  }

  /**
   * When numbering with an alphabetic sequence, the lang attribute
   * specifies which language's alphabet is to be used.
   * @serial
   */
  private AVT m_lang_avt = null;

  /**
   * Set the "lang" attribute.
   * When numbering with an alphabetic sequence, the lang attribute
   * specifies which language's alphabet is to be used; it has the same
   * range of values as xml:lang [XML]; if no lang value is specified,
   * the language should be determined from the system environment.
   * Implementers should document for which languages they support numbering.
   *
   * @param v Value to set for "lang" attribute.
   */
  public void setLang(AVT v)
  {
    m_lang_avt = v;
  }

  /**
   * Get the "lang" attribute.
   * When numbering with an alphabetic sequence, the lang attribute
   * specifies which language's alphabet is to be used; it has the same
   * range of values as xml:lang [XML]; if no lang value is specified,
   * the language should be determined from the system environment.
   * Implementers should document for which languages they support numbering.
   *
   * @return Value ofr "lang" attribute.
   */
  public AVT getLang()
  {
    return m_lang_avt;
  }

  /**
   * The letter-value attribute disambiguates between numbering
   * sequences that use letters.
   * @serial
   */
  private AVT m_lettervalue_avt = null;

  /**
   * Set the "letter-value" attribute.
   * The letter-value attribute disambiguates between numbering sequences
   * that use letters.
   *
   * @param v Value to set for "letter-value" attribute.
   */
  public void setLetterValue(AVT v)
  {
    m_lettervalue_avt = v;
  }

  /**
   * Get the "letter-value" attribute.
   * The letter-value attribute disambiguates between numbering sequences
   * that use letters.
   *
   * @return Value to set for "letter-value" attribute.
   */
  public AVT getLetterValue()
  {
    return m_lettervalue_avt;
  }

  /**
   * The grouping-separator attribute gives the separator
   * used as a grouping (e.g. thousands) separator in decimal
   * numbering sequences.
   * @serial
   */
  private AVT m_groupingSeparator_avt = null;

  /**
   * Set the "grouping-separator" attribute.
   * The grouping-separator attribute gives the separator
   * used as a grouping (e.g. thousands) separator in decimal
   * numbering sequences.
   *
   * @param v Value to set for "grouping-separator" attribute.
   */
  public void setGroupingSeparator(AVT v)
  {
    m_groupingSeparator_avt = v;
  }

  /**
   * Get the "grouping-separator" attribute.
   * The grouping-separator attribute gives the separator
   * used as a grouping (e.g. thousands) separator in decimal
   * numbering sequences.
   *
   * @return Value of "grouping-separator" attribute.
   */
  public AVT getGroupingSeparator()
  {
    return m_groupingSeparator_avt;
  }

  /**
   * The optional grouping-size specifies the size (normally 3) of the grouping.
   * @serial
   */
  private AVT m_groupingSize_avt = null;

  /**
   * Set the "grouping-size" attribute.
   * The optional grouping-size specifies the size (normally 3) of the grouping.
   *
   * @param v Value to set for "grouping-size" attribute.
   */
  public void setGroupingSize(AVT v)
  {
    m_groupingSize_avt = v;
  }

  /**
   * Get the "grouping-size" attribute.
   * The optional grouping-size specifies the size (normally 3) of the grouping.
   *
   * @return Value of "grouping-size" attribute.
   */
  public AVT getGroupingSize()
  {
    return m_groupingSize_avt;
  }

  /**
   * Shouldn't this be in the transformer?  Big worries about threads...
   */


  /**
   * Table to help in converting decimals to roman numerals.
   * @see org.apache.xalan.transformer.DecimalToRoman
   */
  private final static DecimalToRoman m_romanConvertTable[] = {
    new DecimalToRoman(1000, "M", 900, "CM"),
    new DecimalToRoman(500, "D", 400, "CD"),
    new DecimalToRoman(100L, "C", 90L, "XC"),
    new DecimalToRoman(50L, "L", 40L, "XL"),
    new DecimalToRoman(10L, "X", 9L, "IX"),
    new DecimalToRoman(5L, "V", 4L, "IV"),
    new DecimalToRoman(1L, "I", 1L, "I") };

  /**
   * Chars for converting integers into alpha counts.
   * @see TransformerImpl#int2alphaCount
   */
  private static char[] m_alphaCountTable = null;
  
  /**
   * This function is called after everything else has been
   * recomposed, and allows the template to set remaining
   * values that may be based on some other property that
   * depends on recomposition.
   */
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    java.util.Vector vnames = cstate.getVariableNames();
    if(null != m_countMatchPattern)
      m_countMatchPattern.fixupVariables(vnames, cstate.getGlobalsSize());
    if(null != m_format_avt)
      m_format_avt.fixupVariables(vnames, cstate.getGlobalsSize());
    if(null != m_fromMatchPattern)
      m_fromMatchPattern.fixupVariables(vnames, cstate.getGlobalsSize());
    if(null != m_groupingSeparator_avt)
      m_groupingSeparator_avt.fixupVariables(vnames, cstate.getGlobalsSize());
    if(null != m_groupingSize_avt)
      m_groupingSize_avt.fixupVariables(vnames, cstate.getGlobalsSize());
    if(null != m_lang_avt)
      m_lang_avt.fixupVariables(vnames, cstate.getGlobalsSize());
    if(null != m_lettervalue_avt)
      m_lettervalue_avt.fixupVariables(vnames, cstate.getGlobalsSize());
    if(null != m_valueExpr)
      m_valueExpr.fixupVariables(vnames, cstate.getGlobalsSize());
  }


  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_NUMBER;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_NUMBER_STRING;
  }

  /**
   * Execute an xsl:number instruction. The xsl:number element is
   * used to insert a formatted number into the result tree.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {

     if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEvent(this);

    int sourceNode = transformer.getXPathContext().getCurrentNode();
    String countString = getCountString(transformer, sourceNode);

    try
    {
      transformer.getResultTreeHandler().characters(countString.toCharArray(),
                                                    0, countString.length());
    }
    catch(SAXException se)
    {
      throw new TransformerException(se);
    }
    finally
    {
      if (TransformerImpl.S_DEBUG)
	    transformer.getTraceManager().fireTraceEndEvent(this); 
    }
  }

  /**
   * Add a child to the child list.
   *
   * @param newChild Child to add to child list
   *
   * @return Child just added to child list
   *
   * @throws DOMException
   */
  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {

    error(XSLTErrorResources.ER_CANNOT_ADD,
          new Object[]{ newChild.getNodeName(),

    return null;
  }

  /**
   * Given a 'from' pattern (ala xsl:number), a match pattern
   * and a context, find the first ancestor that matches the
   * pattern (including the context handed in).
   *
   * @param xctxt The XPath runtime state for this.
   * @param fromMatchPattern The ancestor must match this pattern.
   * @param countMatchPattern The ancestor must also match this pattern.
   * @param context The node that "." expresses.
   * @param namespaceContext The context in which namespaces in the
   * queries are supposed to be expanded.
   *
   * @return the first ancestor that matches the given pattern
   *
   * @throws javax.xml.transform.TransformerException
   */
  int findAncestor(
          XPathContext xctxt, XPath fromMatchPattern, XPath countMatchPattern, 
          int context, ElemNumber namespaceContext)
            throws javax.xml.transform.TransformerException
  {
    DTM dtm = xctxt.getDTM(context);
    while (DTM.NULL != context)
    {
      if (null != fromMatchPattern)
      {
        if (fromMatchPattern.getMatchScore(xctxt, context)
                != XPath.MATCH_SCORE_NONE)
        {

          break;
        }
      }

      if (null != countMatchPattern)
      {
        if (countMatchPattern.getMatchScore(xctxt, context)
                != XPath.MATCH_SCORE_NONE)
        {
          break;
        }
      }

      context = dtm.getParent(context);
    }

    return context;
  }

  /**
   * Given a 'from' pattern (ala xsl:number), a match pattern
   * and a context, find the first ancestor that matches the
   * pattern (including the context handed in).
   * @param xctxt The XPath runtime state for this.
   * @param fromMatchPattern The ancestor must match this pattern.
   * @param countMatchPattern The ancestor must also match this pattern.
   * @param context The node that "." expresses.
   * @param namespaceContext The context in which namespaces in the
   * queries are supposed to be expanded.
   *
   * @return the first preceding, ancestor or self node that 
   * matches the given pattern
   *
   * @throws javax.xml.transform.TransformerException
   */
  private int findPrecedingOrAncestorOrSelf(
          XPathContext xctxt, XPath fromMatchPattern, XPath countMatchPattern, 
          int context, ElemNumber namespaceContext)
            throws javax.xml.transform.TransformerException
  {
    DTM dtm = xctxt.getDTM(context);
    while (DTM.NULL != context)
    {
      if (null != fromMatchPattern)
      {
        if (fromMatchPattern.getMatchScore(xctxt, context)
                != XPath.MATCH_SCORE_NONE)
        {
          context = DTM.NULL;

          break;
        }
      }

      if (null != countMatchPattern)
      {
        if (countMatchPattern.getMatchScore(xctxt, context)
                != XPath.MATCH_SCORE_NONE)
        {
          break;
        }
      }

      int prevSibling = dtm.getPreviousSibling(context);

      if (DTM.NULL == prevSibling)
      {
        context = dtm.getParent(context);
      }
      else
      {

        context = dtm.getLastChild(prevSibling);

        if (context == DTM.NULL)
          context = prevSibling;
      }
    }

    return context;
  }

  /**
   * Get the count match pattern, or a default value.
   *
   * @param support The XPath runtime state for this.
   * @param contextNode The node that "." expresses.
   *
   * @return the count match pattern, or a default value. 
   *
   * @throws javax.xml.transform.TransformerException
   */
  XPath getCountMatchPattern(XPathContext support, int contextNode)
          throws javax.xml.transform.TransformerException
  {

    XPath countMatchPattern = m_countMatchPattern;
    DTM dtm = support.getDTM(contextNode);
    if (null == countMatchPattern)
    {
      switch (dtm.getNodeType(contextNode))
      {
      case DTM.ELEMENT_NODE :
        MyPrefixResolver resolver;

        if (dtm.getNamespaceURI(contextNode) == null) {
             resolver =  new MyPrefixResolver(dtm.getNode(contextNode), dtm,contextNode, false);
        } else {
            resolver = new MyPrefixResolver(dtm.getNode(contextNode), dtm,contextNode, true);
        }

        countMatchPattern = new XPath(dtm.getNodeName(contextNode), this, resolver,
                                      XPath.MATCH, support.getErrorListener());
        break;

      case DTM.ATTRIBUTE_NODE :

        countMatchPattern = new XPath("@" + dtm.getNodeName(contextNode), this,
                                      this, XPath.MATCH, support.getErrorListener());
        break;
      case DTM.CDATA_SECTION_NODE :
      case DTM.TEXT_NODE :

        countMatchPattern = new XPath("text()", this, this, XPath.MATCH, support.getErrorListener());
        break;
      case DTM.COMMENT_NODE :

        countMatchPattern = new XPath("comment()", this, this, XPath.MATCH, support.getErrorListener());
        break;
      case DTM.DOCUMENT_NODE :

        countMatchPattern = new XPath("/", this, this, XPath.MATCH, support.getErrorListener());
        break;
      case DTM.PROCESSING_INSTRUCTION_NODE :

        countMatchPattern = new XPath("pi(" + dtm.getNodeName(contextNode)
                                      + ")", this, this, XPath.MATCH, support.getErrorListener());
        break;
      default :
        countMatchPattern = null;
      }
    }

    return countMatchPattern;
  }

  /**
   * Given an XML source node, get the count according to the
   * parameters set up by the xsl:number attributes.
   * @param transformer non-null reference to the the current transform-time state.
   * @param sourceNode The source node being counted.
   *
   * @return The count of nodes
   *
   * @throws TransformerException
   */
  String getCountString(TransformerImpl transformer, int sourceNode)
          throws TransformerException
  {

    long[] list = null;
    XPathContext xctxt = transformer.getXPathContext();
    CountersTable ctable = transformer.getCountersTable();

    if (null != m_valueExpr)
    {
      XObject countObj = m_valueExpr.execute(xctxt, sourceNode, this);
      long count = (long)java.lang.Math.floor(countObj.num()+ 0.5);

      list = new long[1];
      list[0] = count;
    }
    else
    {
      if (Constants.NUMBERLEVEL_ANY == m_level)
      {
        list = new long[1];
        list[0] = ctable.countNode(xctxt, this, sourceNode);
      }
      else
      {
        NodeVector ancestors =
          getMatchingAncestors(xctxt, sourceNode,
                               Constants.NUMBERLEVEL_SINGLE == m_level);
        int lastIndex = ancestors.size() - 1;

        if (lastIndex >= 0)
        {
          list = new long[lastIndex + 1];

          for (int i = lastIndex; i >= 0; i--)
          {
            int target = ancestors.elementAt(i);

            list[lastIndex - i] = ctable.countNode(xctxt, this, target);
          }
        }
      }
    }

    return (null != list)
           ? formatNumberList(transformer, list, sourceNode) : "";
  }

  /**
   * Get the previous node to be counted.
   *
   * @param xctxt The XPath runtime state for this.
   * @param pos The current node
   *
   * @return the previous node to be counted.
   *
   * @throws TransformerException
   */
  public int getPreviousNode(XPathContext xctxt, int pos)
          throws TransformerException
  {

    XPath countMatchPattern = getCountMatchPattern(xctxt, pos);
    DTM dtm = xctxt.getDTM(pos);

    if (Constants.NUMBERLEVEL_ANY == m_level)
    {
      XPath fromMatchPattern = m_fromMatchPattern;

      while (DTM.NULL != pos)
      {

        int next = dtm.getPreviousSibling(pos);

        if (DTM.NULL == next)
        {
          next = dtm.getParent(pos);

          if ((DTM.NULL != next) && ((((null != fromMatchPattern) && (fromMatchPattern.getMatchScore(
                  xctxt, next) != XPath.MATCH_SCORE_NONE))) 
              || (dtm.getNodeType(next) == DTM.DOCUMENT_NODE)))
          {

          }
        }
        else
        {

          int child = next;

          while (DTM.NULL != child)
          {
            child = dtm.getLastChild(next);

            if (DTM.NULL != child)
              next = child;
          }
        }

        pos = next;

        if ((DTM.NULL != pos)
                && ((null == countMatchPattern)
                    || (countMatchPattern.getMatchScore(xctxt, pos)
                        != XPath.MATCH_SCORE_NONE)))
        {
          break;
        }
      }
    }
    {
      while (DTM.NULL != pos)
      {
        pos = dtm.getPreviousSibling(pos);

        if ((DTM.NULL != pos)
                && ((null == countMatchPattern)
                    || (countMatchPattern.getMatchScore(xctxt, pos)
                        != XPath.MATCH_SCORE_NONE)))
        {
          break;
        }
      }
    }

    return pos;
  }

  /**
   * Get the target node that will be counted..
   *
   * @param xctxt The XPath runtime state for this.
   *
   * @return the target node that will be counted
   *
   * @throws TransformerException
   */
  public int getTargetNode(XPathContext xctxt, int sourceNode)
          throws TransformerException
  {

    int target = DTM.NULL;
    XPath countMatchPattern = getCountMatchPattern(xctxt, sourceNode);

    if (Constants.NUMBERLEVEL_ANY == m_level)
    {
      target = findPrecedingOrAncestorOrSelf(xctxt, m_fromMatchPattern,
                                             countMatchPattern, sourceNode,
                                             this);
    }
    else
    {
      target = findAncestor(xctxt, m_fromMatchPattern, countMatchPattern,
                            sourceNode, this);
    }

    return target;
  }

  /**
   * Get the ancestors, up to the root, that match the
   * pattern.
   * 
   * @param patterns if non-null, count only nodes
   * that match this pattern, if null count all ancestors.
   * @param xctxt The XPath runtime state for this.
   * @param node Count this node and it's ancestors.
   * @param stopAtFirstFound Flag indicating to stop after the
   * first node is found (difference between level = single
   * or multiple)
   * @return The number of ancestors that match the pattern.
   *
   * @throws javax.xml.transform.TransformerException
   */
  NodeVector getMatchingAncestors(
          XPathContext xctxt, int node, boolean stopAtFirstFound)
            throws javax.xml.transform.TransformerException
  {

    NodeSetDTM ancestors = new NodeSetDTM(xctxt.getDTMManager());
    XPath countMatchPattern = getCountMatchPattern(xctxt, node);
    DTM dtm = xctxt.getDTM(node);

    while (DTM.NULL != node)
    {
      if ((null != m_fromMatchPattern)
              && (m_fromMatchPattern.getMatchScore(xctxt, node)
                  != XPath.MATCH_SCORE_NONE))
      {

        if (!stopAtFirstFound)
          break;
      }

      if (null == countMatchPattern)
        System.out.println(
          "Programmers error! countMatchPattern should never be null!");

      if (countMatchPattern.getMatchScore(xctxt, node)
              != XPath.MATCH_SCORE_NONE)
      {
        ancestors.addElement(node);

        if (stopAtFirstFound)
          break;
      }

      node = dtm.getParent(node);
    }

    return ancestors;

  /**
   * Get the locale we should be using.
   *
   * @param transformer non-null reference to the the current transform-time state.
   * @param contextNode The node that "." expresses.
   *
   * @return The locale to use. May be specified by "lang" attribute,
   * but if not, use default locale on the system. 
   *
   * @throws TransformerException
   */
  Locale getLocale(TransformerImpl transformer, int contextNode)
          throws TransformerException
  {

    Locale locale = null;

    if (null != m_lang_avt)
    {
      XPathContext xctxt = transformer.getXPathContext();
      String langValue = m_lang_avt.evaluate(xctxt, contextNode, this);

      if (null != langValue)
      {

        locale = new Locale(langValue.toUpperCase(), "");

        if (null == locale)
        {
          transformer.getMsgMgr().warn(this, null, xctxt.getDTM(contextNode).getNode(contextNode),
                                       XSLTErrorResources.WG_LOCALE_NOT_FOUND,

          locale = Locale.getDefault();
        }
      }
    }
    else
    {
      locale = Locale.getDefault();
    }

    return locale;
  }

  /**
   * Get the number formatter to be used the format the numbers
   *
   * @param transformer non-null reference to the the current transform-time state.
   * @param contextNode The node that "." expresses.
   *
   * ($objectName$) @return The number formatter to be used
   *
   * @throws TransformerException
   */
  private DecimalFormat getNumberFormatter(
          TransformerImpl transformer, int contextNode) throws TransformerException
  {
    Locale locale = (Locale)getLocale(transformer, contextNode).clone();

    DecimalFormat formatter = null;


    String digitGroupSepValue =
      (null != m_groupingSeparator_avt)
      ? m_groupingSeparator_avt.evaluate(
      transformer.getXPathContext(), contextNode, this) : null;
      
      
    if ((digitGroupSepValue != null) && (!m_groupingSeparator_avt.isSimple()) &&
        (digitGroupSepValue.length() != 1))
    {
            transformer.getMsgMgr().warn(
               this, XSLTErrorResources.WG_ILLEGAL_ATTRIBUTE_VALUE,
               new Object[]{ Constants.ATTRNAME_NAME, m_groupingSeparator_avt.getName()});   
    }                  
      
      
    String nDigitsPerGroupValue =
      (null != m_groupingSize_avt)
      ? m_groupingSize_avt.evaluate(
      transformer.getXPathContext(), contextNode, this) : null;

    if ((null != digitGroupSepValue) && (null != nDigitsPerGroupValue) &&
        (digitGroupSepValue.length() > 0))
    {
      try
      {
        formatter = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        formatter.setGroupingSize(
          Integer.valueOf(nDigitsPerGroupValue).intValue());
        
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(digitGroupSepValue.charAt(0));
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setGroupingUsed(true);
      }
      catch (NumberFormatException ex)
      {
        formatter.setGroupingUsed(false);
      }
    }

    return formatter;
  }

  /**
   * Format a vector of numbers into a formatted string.
   * 
   * @param xslNumberElement Element that takes %conversion-atts; attributes.
   * @param transformer non-null reference to the the current transform-time state.
   * @param list Array of one or more long integer numbers.
   * @param contextNode The node that "." expresses.
   * @return String that represents list according to
   * %conversion-atts; attributes.
   * TODO: Optimize formatNumberList so that it caches the last count and
   * reuses that info for the next count.
   *
   * @throws TransformerException
   */
  String formatNumberList(
          TransformerImpl transformer, long[] list, int contextNode)
            throws TransformerException
  {

    String numStr;
    FastStringBuffer formattedNumber = StringBufferPool.get();

    try
    {
      int nNumbers = list.length, numberWidth = 1;
      char numberType = '1';
      String formatToken, lastSepString = null, formatTokenString = null;

      String lastSep = ".";
      String formatValue =
        (null != m_format_avt)
        ? m_format_avt.evaluate(
        transformer.getXPathContext(), contextNode, this) : null;

      if (null == formatValue)
        formatValue = "1";

      NumberFormatStringTokenizer formatTokenizer =
        new NumberFormatStringTokenizer(formatValue);

      for (int i = 0; i < nNumbers; i++)
      {

        if (formatTokenizer.hasMoreTokens())
        {
          formatToken = formatTokenizer.nextToken();

          if (Character.isLetterOrDigit(
                  formatToken.charAt(formatToken.length() - 1)))
          {
            numberWidth = formatToken.length();
            numberType = formatToken.charAt(numberWidth - 1);
          }

          else if (formatTokenizer.isLetterOrDigitAhead())
          {
            formatTokenString = formatToken;

            while (formatTokenizer.nextIsSep())
            {
              formatToken = formatTokenizer.nextToken();
              formatTokenString += formatToken;
            }

            if (!isFirstToken)
              lastSep = formatTokenString;

            formatToken = formatTokenizer.nextToken();
            numberWidth = formatToken.length();
            numberType = formatToken.charAt(numberWidth - 1);
          }
          {

            lastSepString = formatToken;

            while (formatTokenizer.hasMoreTokens())
            {
              formatToken = formatTokenizer.nextToken();
              lastSepString += formatToken;
            }

        if (null != formatTokenString && isFirstToken)
        {
          formattedNumber.append(formatTokenString);
        }
        else if (null != lastSep &&!isFirstToken)
          formattedNumber.append(lastSep);

        getFormattedNumber(transformer, contextNode, numberType, numberWidth,
                           list[i], formattedNumber);


      while (formatTokenizer.isLetterOrDigitAhead())
      {
        formatTokenizer.nextToken();
      }

      if (lastSepString != null)
        formattedNumber.append(lastSepString);

      while (formatTokenizer.hasMoreTokens())
      {
        formatToken = formatTokenizer.nextToken();

        formattedNumber.append(formatToken);
      }

      numStr = formattedNumber.toString();
    }
    finally
    {
      StringBufferPool.free(formattedNumber);
    }

    return numStr;

  /*
  * Get Formatted number
  */

  /**
   * Format the given number and store it in the given buffer 
   *
   *
   * @param transformer non-null reference to the the current transform-time state.
   * @param contextNode The node that "." expresses.
   * @param numberType Type to format to
   * @param numberWidth Maximum length of formatted number
   * @param listElement Number to format
   * @param formattedNumber Buffer to store formatted number
   *
   * @throws javax.xml.transform.TransformerException
   */
  private void getFormattedNumber(
          TransformerImpl transformer, int contextNode, 
          char numberType, int numberWidth, long listElement, 
          FastStringBuffer formattedNumber)
            throws javax.xml.transform.TransformerException
  {


    String letterVal =
      (m_lettervalue_avt != null)
      ? m_lettervalue_avt.evaluate(
      transformer.getXPathContext(), contextNode, this) : null;

    switch (numberType)
    {
    case 'A' :
      if (m_alphaCountTable == null)
      {
        XResourceBundle thisBundle;

        thisBundle =
          (XResourceBundle) XResourceBundle.loadResourceBundle(
            org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, getLocale(transformer, contextNode));

        char[] alphabet;

        alphabet = (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET);
        m_alphaCountTable = alphabet;
      }

      int2alphaCount(listElement, m_alphaCountTable, formattedNumber);
      break;
    case 'a' :
      if (m_alphaCountTable == null)
      {
        XResourceBundle thisBundle;

        thisBundle =
          (XResourceBundle) XResourceBundle.loadResourceBundle(
            org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, getLocale(transformer, contextNode));

        char[] alphabet;

        alphabet = (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET);
        m_alphaCountTable = alphabet;
      }

      FastStringBuffer stringBuf = StringBufferPool.get();

      try
      {
        int2alphaCount(listElement, m_alphaCountTable, stringBuf);
        formattedNumber.append(
          stringBuf.toString().toLowerCase(
            getLocale(transformer, contextNode)));
      }
      finally
      {
        StringBufferPool.free(stringBuf);
      }
      break;
    case 'I' :
      formattedNumber.append(long2roman(listElement, true));
      break;
    case 'i' :
      formattedNumber.append(
        long2roman(listElement, true).toLowerCase(
          getLocale(transformer, contextNode)));
      break;
    case 0x3042 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("ja", "JP", "HA"));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        formattedNumber.append(
          int2singlealphaCount(
            listElement,
            (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET)));

      break;
    }
    case 0x3044 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("ja", "JP", "HI"));

      if ((letterVal != null)
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        formattedNumber.append(
          int2singlealphaCount(
            listElement,
            (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET)));

      break;
    }
    case 0x30A2 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("ja", "JP", "A"));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        formattedNumber.append(
          int2singlealphaCount(
            listElement,
            (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET)));

      break;
    }
    case 0x30A4 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("ja", "JP", "I"));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        formattedNumber.append(
          int2singlealphaCount(
            listElement,
            (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET)));

      break;
    }
    case 0x4E00 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("zh", "CN"));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
      {
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
      }
        int2alphaCount(listElement,
                       (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET),
                       formattedNumber);

      break;
    }
    case 0x58F9 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("zh", "TW"));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        int2alphaCount(listElement,
                       (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET),
                       formattedNumber);

      break;
    }
    case 0x0E51 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("th", ""));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        int2alphaCount(listElement,
                       (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET),
                       formattedNumber);

      break;
    }
    case 0x05D0 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("he", ""));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        int2alphaCount(listElement,
                       (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET),
                       formattedNumber);

      break;
    }
    case 0x10D0 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("ka", ""));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        int2alphaCount(listElement,
                       (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET),
                       formattedNumber);

      break;
    }
    case 0x03B1 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("el", ""));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        int2alphaCount(listElement,
                       (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET),
                       formattedNumber);

      break;
    }
    case 0x0430 :
    {
      XResourceBundle thisBundle;

      thisBundle = (XResourceBundle) XResourceBundle.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.LANG_BUNDLE_NAME, new Locale("cy", ""));

      if (letterVal != null
              && letterVal.equals(Constants.ATTRVAL_TRADITIONAL))
        formattedNumber.append(tradAlphaCount(listElement, thisBundle));
        int2alphaCount(listElement,
                       (char[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_ALPHABET),
                       formattedNumber);

      break;
    }
      DecimalFormat formatter = getNumberFormatter(transformer, contextNode);
      String padString = formatter == null ? String.valueOf(0) : formatter.format(0);    
      String numString = formatter == null ? String.valueOf(listElement) : formatter.format(listElement);
      int nPadding = numberWidth - numString.length();

      for (int k = 0; k < nPadding; k++)
      {
        formattedNumber.append(padString);
      }

      formattedNumber.append(numString);
    }
  }
  
  /**
   * Get a string value for zero, which is not really defined by the 1.0 spec, 
   * thought I think it might be cleared up by the erreta.
   */
   String getZeroString()
   {
     return ""+0;
   }

  /**
   * Convert a long integer into alphabetic counting, in other words
   * count using the sequence A B C ... Z.
   * 
   * @param val Value to convert -- must be greater than zero.
   * @param table a table containing one character for each digit in the radix
   * @return String representing alpha count of number.
   * @see TransformerImpl#DecimalToRoman
   *
   * Note that the radix of the conversion is inferred from the size
   * of the table.
   */
  protected String int2singlealphaCount(long val, char[] table)
  {

    int radix = table.length;

    if (val > radix)
    {
      return getZeroString();
    }
    else
  }

  /**
   * Convert a long integer into alphabetic counting, in other words
   * count using the sequence A B C ... Z AA AB AC.... etc.
   * 
   * @param val Value to convert -- must be greater than zero.
   * @param table a table containing one character for each digit in the radix
   * @param aTable Array of alpha characters representing numbers
   * @param stringBuf Buffer where to save the string representing alpha count of number.
   * 
   * @see TransformerImpl#DecimalToRoman
   *
   * Note that the radix of the conversion is inferred from the size
   * of the table.
   */
  protected void int2alphaCount(long val, char[] aTable,
                                FastStringBuffer stringBuf)
  {

    int radix = aTable.length;
    char[] table = new char[aTable.length];

    int i;

    for (i = 0; i < aTable.length - 1; i++)
    {
      table[i + 1] = aTable[i];
    }

    table[0] = aTable[i];

    char buf[] = new char[100];

    int charPos;



    long correction = 0;

    do
    {

      correction =
        ((lookupIndex == 0) || (correction != 0 && lookupIndex == radix - 1))
        ? (radix - 1) : 0;

      lookupIndex = (int)(val + correction) % radix;

      val = (val / radix);

      if (lookupIndex == 0 && val == 0)
        break;

    }
    while (val > 0);

    stringBuf.append(buf, charPos + 1, (buf.length - charPos - 1));
  }

  /**
   * Convert a long integer into traditional alphabetic counting, in other words
   * count using the traditional numbering.
   * 
   * @param val Value to convert -- must be greater than zero.
   * @param table a table containing one character for each digit in the radix
   * @param thisBundle Resource bundle to use
   * 
   * @return String representing alpha count of number.
   * @see XSLProcessor#DecimalToRoman
   *
   * Note that the radix of the conversion is inferred from the size
   * of the table.
   */
  protected String tradAlphaCount(long val, XResourceBundle thisBundle)
  {

    if (val > Long.MAX_VALUE)
    {
      this.error(XSLTErrorResources.ER_NUMBER_TOO_BIG);
      return XSLTErrorResources.ERROR_STRING;
    }
    char[] table = null;


    char buf[] = new char[100];

    int charPos;


    int[] groups = (int[]) thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_NUMBERGROUPS);

    String[] tables =
      (String[]) (thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_NUM_TABLES));

    String numbering = thisBundle.getString(org.apache.xml.utils.res.XResourceBundle.LANG_NUMBERING);

    if (numbering.equals(org.apache.xml.utils.res.XResourceBundle.LANG_MULT_ADD))
    {
      String mult_order = thisBundle.getString(org.apache.xml.utils.res.XResourceBundle.MULT_ORDER);
      long[] multiplier =
        (long[]) (thisBundle.getObject(org.apache.xml.utils.res.XResourceBundle.LANG_MULTIPLIER));
      char[] zeroChar = (char[]) thisBundle.getObject("zero");
      int i = 0;

      while (i < multiplier.length && val < multiplier[i])
      {
        i++;
      }

      do
      {
        if (i >= multiplier.length)

        if (val < multiplier[i])
        {
          if (zeroChar.length == 0)
          {
            i++;
          }
          else
          {
            if (buf[charPos - 1] != zeroChar[0])
              buf[charPos++] = zeroChar[0];

            i++;
          }
        }
        else if (val >= multiplier[i])
        {
          long mult = val / multiplier[i];


          int k = 0;

          while (k < groups.length)
          {

              k++;
            else
            {

              char[] THEletters = (char[]) thisBundle.getObject(tables[k]);

              table = new char[THEletters.length + 1];

              int j;

              for (j = 0; j < THEletters.length; j++)
              {
                table[j + 1] = THEletters[j];
              }


              lookupIndex = (int)mult / groups[k];

              if (lookupIndex == 0 && mult == 0)
                break;

              char multiplierChar = ((char[]) (thisBundle.getObject(
                org.apache.xml.utils.res.XResourceBundle.LANG_MULTIPLIER_CHAR)))[i];

              if (lookupIndex < table.length)
              {
                if (mult_order.equals(org.apache.xml.utils.res.XResourceBundle.MULT_PRECEDES))
                {
                  buf[charPos++] = multiplierChar;
                  buf[charPos++] = table[lookupIndex];
                }
                else
                {

                  if (lookupIndex == 1 && i == multiplier.length - 1){}
                  else
                    buf[charPos++] = table[lookupIndex];

                  buf[charPos++] = multiplierChar;
                }

              }
              else
                return XSLTErrorResources.ERROR_STRING;

          i++;
      while (i < multiplier.length);
    }

    int count = 0;
    String tableName;

    while (count < groups.length)
    {
        count++;
      else
      {
        char[] theletters = (char[]) thisBundle.getObject(tables[count]);

        table = new char[theletters.length + 1];

        int j;

        for (j = 0; j < theletters.length; j++)
        {
          table[j + 1] = theletters[j];
        }


        lookupIndex = (int)val / groups[count];

        val = val % groups[count];

        if (lookupIndex == 0 && val == 0)
          break;

        if (lookupIndex < table.length)
        {

        }
        else
          return XSLTErrorResources.ERROR_STRING;

        count++;
      }

    return new String(buf, 0, charPos);
  }

  /**
   * Convert a long integer into roman numerals.
   * @param val Value to convert.
   * @param prefixesAreOK true_ to enable prefix notation (e.g. 4 = "IV"),
   * false_ to disable prefix notation (e.g. 4 = "IIII").
   * @return Roman numeral string.
   * @see DecimalToRoman
   * @see m_romanConvertTable
   */
  protected String long2roman(long val, boolean prefixesAreOK)
  {

    if (val <= 0)
    {
      return getZeroString();
    }

    String roman = "";
    int place = 0;

    if (val <= 3999L)
    {
      do
      {
        while (val >= m_romanConvertTable[place].m_postValue)
        {
          roman += m_romanConvertTable[place].m_postLetter;
          val -= m_romanConvertTable[place].m_postValue;
        }

        if (prefixesAreOK)
        {
          if (val >= m_romanConvertTable[place].m_preValue)
          {
            roman += m_romanConvertTable[place].m_preLetter;
            val -= m_romanConvertTable[place].m_preValue;
          }
        }

        place++;
      }
      while (val > 0);
    }
    else
    {
      roman = XSLTErrorResources.ERROR_STRING;
    }

    return roman;
  
  /**
   * Call the children visitors.
   * @param visitor The visitor whose appropriate method will be called.
   */
  public void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
  {
  	if(callAttrs)
  	{
	  	if(null != m_countMatchPattern)
	  		m_countMatchPattern.getExpression().callVisitors(m_countMatchPattern, visitor);
	  	if(null != m_fromMatchPattern)
	  		m_fromMatchPattern.getExpression().callVisitors(m_fromMatchPattern, visitor);
	  	if(null != m_valueExpr)
	  		m_valueExpr.getExpression().callVisitors(m_valueExpr, visitor);
	
	  	if(null != m_format_avt)
	  		m_format_avt.callVisitors(visitor);
	  	if(null != m_groupingSeparator_avt)
	  		m_groupingSeparator_avt.callVisitors(visitor);
	  	if(null != m_groupingSize_avt)
	  		m_groupingSize_avt.callVisitors(visitor);
	  	if(null != m_lang_avt)
	  		m_lang_avt.callVisitors(visitor);
	  	if(null != m_lettervalue_avt)
	  		m_lettervalue_avt.callVisitors(visitor);
  	}

    super.callChildVisitors(visitor, callAttrs);
  }


  /**
   * This class returns tokens using non-alphanumberic
   * characters as delimiters.
   */
  class NumberFormatStringTokenizer
  {

    /** Current position in the format string          */
    private int currentPosition;

    /** Index of last character in the format string      */
    private int maxPosition;

    /** Format string to be tokenized        */
    private String str;

    /**
     * Construct a NumberFormatStringTokenizer.
     *
     * @param str Format string to be tokenized
     */
    public NumberFormatStringTokenizer(String str)
    {
      this.str = str;
      maxPosition = str.length();
    }

    /**
     * Reset tokenizer so that nextToken() starts from the beginning.
     */
    public void reset()
    {
      currentPosition = 0;
    }

    /**
     * Returns the next token from this string tokenizer.
     *
     * @return     the next token from this string tokenizer.
     * @throws  NoSuchElementException  if there are no more tokens in this
     *               tokenizer's string.
     */
    public String nextToken()
    {

      if (currentPosition >= maxPosition)
      {
        throw new NoSuchElementException();
      }

      int start = currentPosition;

      while ((currentPosition < maxPosition)
             && Character.isLetterOrDigit(str.charAt(currentPosition)))
      {
        currentPosition++;
      }

      if ((start == currentPosition)
              && (!Character.isLetterOrDigit(str.charAt(currentPosition))))
      {
        currentPosition++;
      }

      return str.substring(start, currentPosition);
    }

    /**
     * Tells if there is a digit or a letter character ahead.
     *
     * @return     true if there is a number or character ahead.
     */
    public boolean isLetterOrDigitAhead()
    {

      int pos = currentPosition;

      while (pos < maxPosition)
      {
        if (Character.isLetterOrDigit(str.charAt(pos)))
          return true;

        pos++;
      }

      return false;
    }

    /**
     * Tells if there is a digit or a letter character ahead.
     *
     * @return     true if there is a number or character ahead.
     */
    public boolean nextIsSep()
    {

      if (Character.isLetterOrDigit(str.charAt(currentPosition)))
        return false;
      else
        return true;
    }

    /**
     * Tells if <code>nextToken</code> will throw an exception
     * if it is called.
     *
     * @return true if <code>nextToken</code> can be called
     * without throwing an exception.
     */
    public boolean hasMoreTokens()
    {
      return (currentPosition >= maxPosition) ? false : true;
    }

    /**
     * Calculates the number of times that this tokenizer's
     * <code>nextToken</code> method can be called before it generates an
     * exception.
     *
     * @return  the number of tokens remaining in the string using the current
     *          delimiter set.
     * @see     java.util.StringTokenizer#nextToken()
     */
    public int countTokens()
    {

      int count = 0;
      int currpos = currentPosition;

      while (currpos < maxPosition)
      {
        int start = currpos;

        while ((currpos < maxPosition)
               && Character.isLetterOrDigit(str.charAt(currpos)))
        {
          currpos++;
        }

        if ((start == currpos)
                && (Character.isLetterOrDigit(str.charAt(currpos)) == false))
        {
          currpos++;
        }

        count++;
      }

      return count;
    }



}
