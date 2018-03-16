package org.apache.xpath;


/**
 * This object represents a Source Tree, and any associated
 * information.
 * @xsl.usage internal
 */
public class SourceTree
{

  /**
   * Constructor SourceTree
   *
   *
   * @param root The root of the source tree, which may or may not be a 
   * {@link org.w3c.dom.Document} node.
   * @param url The URI of the source tree.
   */
  public SourceTree(int root, String url)
  {
    m_root = root;
    m_url = url;
  }

  /** The URI of the source tree.   */
  public String m_url;

  /** The root of the source tree, which may or may not be a 
   * {@link org.w3c.dom.Document} node.  */
  public int m_root;
}
