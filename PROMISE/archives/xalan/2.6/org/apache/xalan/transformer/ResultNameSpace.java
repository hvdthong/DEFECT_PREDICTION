package org.apache.xalan.transformer;

/**
 * A representation of a result namespace.  One of these will
 * be pushed on the result tree namespace stack for each
 * result tree element.
 * @xsl.usage internal
 */
public class ResultNameSpace
{

  /** Pointer to next ResultNameSpace          */
  public ResultNameSpace m_next = null;

  /** Prefix of namespace         */
  public String m_prefix;

  /** Namespace URI          */

  /**
   * Construct a namespace for placement on the
   * result tree namespace stack.
   *
   * @param prefix of result namespace
   * @param uri URI of result namespace
   */
  public ResultNameSpace(String prefix, String uri)
  {
    m_prefix = prefix;
    m_uri = uri;
  }
}
