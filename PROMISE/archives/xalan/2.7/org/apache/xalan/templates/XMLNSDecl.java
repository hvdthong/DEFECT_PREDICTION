package org.apache.xalan.templates;

/**
 * Represents an xmlns declaration
 */
public class XMLNSDecl
{
    static final long serialVersionUID = 6710237366877605097L;

  /**
   * Constructor XMLNSDecl
   *
   * @param prefix non-null reference to prefix, using "" for default namespace.
   * @param uri non-null reference to namespace URI.
   * @param isExcluded true if this namespace declaration should normally be excluded.
   */
  public XMLNSDecl(String prefix, String uri, boolean isExcluded)
  {

    m_prefix = prefix;
    m_uri = uri;
    m_isExcluded = isExcluded;
  }

  /** non-null reference to prefix, using "" for default namespace.
   *  @serial */
  private String m_prefix;

  /**
   * Return the prefix.
   * @return The prefix that is associated with this URI, or null
   * if the XMLNSDecl is declaring the default namespace.
   */
  public String getPrefix()
  {
    return m_prefix;
  }

  /** non-null reference to namespace URI.
   *  @serial  */
  private String m_uri;

  /**
   * Return the URI.
   * @return The URI that is associated with this declaration.
   */
  public String getURI()
  {
    return m_uri;
  }

  /** true if this namespace declaration should normally be excluded.
   *  @serial  */
  private boolean m_isExcluded;

  /**
   * Tell if this declaration should be excluded from the
   * result namespace.
   *
   * @return true if this namespace declaration should normally be excluded.
   */
  public boolean getIsExcluded()
  {
    return m_isExcluded;
  }
}
