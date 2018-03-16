package org.apache.xml.utils;

import java.io.Serializable;

/**
 * A representation of a namespace.  One of these will
 * be pushed on the namespace stack for each
 * element.
 * @xsl.usage advanced
 */
public class NameSpace implements Serializable
{
    static final long serialVersionUID = 1471232939184881839L;

  /** Next NameSpace element on the stack.
   *  @serial             */
  public NameSpace m_next = null;

  /** Prefix of this NameSpace element.
   *  @serial          */
  public String m_prefix;

  /** Namespace URI of this NameSpace element.
   *  @serial           */

  /**
   * Construct a namespace for placement on the
   * result tree namespace stack.
   *
   * @param prefix Prefix of this element
   * @param uri URI of  this element
   */
  public NameSpace(String prefix, String uri)
  {
    m_prefix = prefix;
    m_uri = uri;
  }
}
