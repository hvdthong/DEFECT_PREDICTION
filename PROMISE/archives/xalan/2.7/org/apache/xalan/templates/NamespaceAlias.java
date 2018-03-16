package org.apache.xalan.templates;

/**
 * Object to hold an xsl:namespace element.
 * A stylesheet can use the xsl:namespace-alias element to declare
 * that one namespace URI is an alias for another namespace URI.
 */
public class NamespaceAlias extends ElemTemplateElement
{
    static final long serialVersionUID = 456173966637810718L;
  
  /**
   * Constructor NamespaceAlias
   * 
   * @param docOrderNumber The document order number
   *
   */
  public NamespaceAlias(int docOrderNumber)
  {
    super();
    m_docOrderNumber = docOrderNumber;
  }

  /**
   * The "stylesheet-prefix" attribute.
   * @serial
   */
  private String m_StylesheetPrefix;

  /**
   * Set the "stylesheet-prefix" attribute.
   *
   * @param v non-null prefix value.
   */
  public void setStylesheetPrefix(String v)
  {
    m_StylesheetPrefix = v;
  }

  /**
   * Get the "stylesheet-prefix" attribute.
   *
   * @return non-null prefix value.
   */
  public String getStylesheetPrefix()
  {
    return m_StylesheetPrefix;
  }
  
  /**
   * The namespace in the stylesheet space.
   * @serial
   */
  private String m_StylesheetNamespace;

  /**
   * Set the value for the stylesheet namespace.
   *
   * @param v non-null prefix value.
   */
  public void setStylesheetNamespace(String v)
  {
    m_StylesheetNamespace = v;
  }

  /**
   * Get the value for the stylesheet namespace.
   *
   * @return non-null prefix value.
   */
  public String getStylesheetNamespace()
  {
    return m_StylesheetNamespace;
  }

  /**
   * The "result-prefix" attribute.
   * @serial
   */
  private String m_ResultPrefix;

  /**
   * Set the "result-prefix" attribute.
   *
   * @param v non-null prefix value.
   */
  public void setResultPrefix(String v)
  {
    m_ResultPrefix = v;
  }

  /**
   * Get the "result-prefix" attribute.
   *
   * @return non-null prefix value.
   */
  public String getResultPrefix()
  {
    return m_ResultPrefix;
  }
  
  /**
   * The result namespace.
   * @serial
   */
  private String m_ResultNamespace;

  /**
   * Set the result namespace.
   *
   * @param v non-null namespace value
   */
  public void setResultNamespace(String v)
  {
    m_ResultNamespace = v;
  }

  /**
   * Get the result namespace value.
   *
   * @return non-null namespace value.
   */
  public String getResultNamespace()
  {
    return m_ResultNamespace;
  }

  /**
   * This function is called to recompose() all of the namespace alias properties elements.
   * 
   * @param root The owning root stylesheet
   */
  public void recompose(StylesheetRoot root)
  {
    root.recomposeNamespaceAliases(this);
  }

}
