package org.apache.xalan.templates;

/**
 * Implement Script extension element
 * @xsl.usage internal
 */
public class ElemExtensionScript extends ElemTemplateElement
{
    static final long serialVersionUID = -6995978265966057744L;

  /**
   * Constructor ElemExtensionScript
   *
   */
  public ElemExtensionScript()
  {

  }

  /** Language used in extension.
   *  @serial          */
  private String m_lang = null;

  /**
   * Set language used by extension
   *
   *
   * @param v Language used by extension
   */
  public void setLang(String v)
  {
    m_lang = v;
  }

  /**
   * Get language used by extension
   *
   *
   * @return Language used by extension
   */
  public String getLang()
  {
    return m_lang;
  }

  /** Extension handler.
   *  @serial          */
  private String m_src = null;

  /**
   * Set Extension handler name for this extension
   *
   *
   * @param v Extension handler name to set
   */
  public void setSrc(String v)
  {
    m_src = v;
  }

  /**
   * Get Extension handler name for this extension
   *
   *
   * @return Extension handler name
   */
  public String getSrc()
  {
    return m_src;
  }

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element 
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_EXTENSIONSCRIPT;
  }
}
