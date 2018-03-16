package org.apache.xalan.templates;

import org.apache.xpath.XPath;

/**
 * This is used as a special "fake" template that can be
 * handled by the TemplateList to do pattern matching
 * on nodes.
 */
public class WhiteSpaceInfo extends ElemTemplate
{

  /** Flag indicating whether whitespaces should be stripped.
   *  @serial        */
  private boolean m_shouldStripSpace;

  /**
   * Return true if this element specifies that the node that
   * matches the match pattern should be stripped, otherwise
   * the space should be preserved.
   *
   * @return value of m_shouldStripSpace flag
   */
  public boolean getShouldStripSpace()
  {
    return m_shouldStripSpace;
  }
  
  /**
   * Constructor WhiteSpaceInfo
   *
   *
   * @param matchPattern Match pattern
   * @param shouldStripSpace Flag indicating whether or not
   * to strip whitespaces
   * @param thisSheet The current stylesheet
   */
  public WhiteSpaceInfo(Stylesheet thisSheet)
  {
  	setStylesheet(thisSheet);
  }


  /**
   * Constructor WhiteSpaceInfo
   *
   *
   * @param matchPattern Match pattern
   * @param shouldStripSpace Flag indicating whether or not
   * to strip whitespaces
   * @param thisSheet The current stylesheet
   */
  public WhiteSpaceInfo(XPath matchPattern, boolean shouldStripSpace, Stylesheet thisSheet)
  {

    m_shouldStripSpace = shouldStripSpace;

    setMatch(matchPattern);

    setStylesheet(thisSheet);
  }

  /**
   * This function is called to recompose() all of the WhiteSpaceInfo elements.
   */
  public void recompose(StylesheetRoot root)
  {
    root.recomposeWhiteSpaceInfo(this);
  }

}
