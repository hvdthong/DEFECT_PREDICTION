package org.apache.xalan.transformer;

import java.text.Collator;
import java.util.Locale;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xpath.XPath;

/**
 * <meta name="usage" content="internal"/>
 * Data structure for use by the NodeSorter class.
 */
class NodeSortKey
{

  /** Select pattern for this sort key          */
  XPath m_selectPat;

  /** Flag indicating whether to treat thee result as a number     */
  boolean m_treatAsNumbers;

  /** Flag indicating whether to sort in descending order      */
  boolean m_descending;

  /** Flag indicating by case          */
  boolean m_caseOrderUpper;

  /** Collator instance          */
  Collator m_col;

  /** Locale we're in          */
  Locale m_locale;

  /** Prefix resolver to use          */
  org.apache.xml.utils.PrefixResolver m_namespaceContext;

  /** Transformer instance          */

  /**
   * Constructor NodeSortKey
   *
   *
   * @param transformer non null transformer instance
   * @param selectPat Select pattern for this key 
   * @param treatAsNumbers Flag indicating whether the result will be a number
   * @param descending Flag indicating whether to sort in descending order
   * @param langValue Lang value to use to get locale
   * @param caseOrderUpper Flag indicating whether case is relevant
   * @param namespaceContext Prefix resolver
   *
   * @throws javax.xml.transform.TransformerException
   */
  NodeSortKey(
          TransformerImpl transformer, XPath selectPat, boolean treatAsNumbers, 
          boolean descending, String langValue, boolean caseOrderUpper, 
          org.apache.xml.utils.PrefixResolver namespaceContext)
            throws javax.xml.transform.TransformerException
  {

    m_processor = transformer;
    m_namespaceContext = namespaceContext;
    m_selectPat = selectPat;
    m_treatAsNumbers = treatAsNumbers;
    m_descending = descending;
    m_caseOrderUpper = caseOrderUpper;

    if (null != langValue && m_treatAsNumbers == false)
    {
      m_locale = new Locale(langValue.toLowerCase(), 
                  Locale.getDefault().getCountry());
                  

      if (null == m_locale)
      {

        m_locale = Locale.getDefault();
      }
    }
    else
    {
      m_locale = Locale.getDefault();
    }

    m_col = Collator.getInstance(m_locale);

    if (null == m_col)
    {
      m_processor.getMsgMgr().warn(null, XSLTErrorResources.WG_CANNOT_FIND_COLLATOR,

      m_col = Collator.getInstance();
    }
  }
}
