package org.apache.xml.utils;

/**
 * This class holds information about the namespace info
 * of a node.  It is used to optimize namespace lookup in
 * a generic DOM.
 * @xsl.usage internal
 */
public class NSInfo
{

  /**
   * Constructor NSInfo
   *
   *
   * @param hasProcessedNS Flag indicating whether namespaces
   * have been processed for this node 
   * @param hasXMLNSAttrs Flag indicating whether this node
   * has XMLNS attributes. 
   */
  public NSInfo(boolean hasProcessedNS, boolean hasXMLNSAttrs)
  {

    m_hasProcessedNS = hasProcessedNS;
    m_hasXMLNSAttrs = hasXMLNSAttrs;
    m_namespace = null;
    m_ancestorHasXMLNSAttrs = ANCESTORXMLNSUNPROCESSED;
  }


  /**
   * Constructor NSInfo
   *
   *
   * @param hasProcessedNS Flag indicating whether namespaces
   * have been processed for this node 
   * @param hasXMLNSAttrs Flag indicating whether this node
   * has XMLNS attributes. 
   * @param ancestorHasXMLNSAttrs Flag indicating whether one of this node's
   * ancestor has XMLNS attributes.
   */
  public NSInfo(boolean hasProcessedNS, boolean hasXMLNSAttrs,
                int ancestorHasXMLNSAttrs)
  {

    m_hasProcessedNS = hasProcessedNS;
    m_hasXMLNSAttrs = hasXMLNSAttrs;
    m_ancestorHasXMLNSAttrs = ancestorHasXMLNSAttrs;
    m_namespace = null;
  }

  /**
   * Constructor NSInfo
   *
   *
   * @param namespace The namespace URI 
   * @param hasXMLNSAttrs Flag indicating whether this node
   * has XMLNS attributes.
   */
  public NSInfo(String namespace, boolean hasXMLNSAttrs)
  {

    m_hasProcessedNS = true;
    m_hasXMLNSAttrs = hasXMLNSAttrs;
    m_namespace = namespace;
    m_ancestorHasXMLNSAttrs = ANCESTORXMLNSUNPROCESSED;
  }

  /** The namespace URI          */
  public String m_namespace;

  /** Flag indicating whether this node has an XMLNS attribute          */
  public boolean m_hasXMLNSAttrs;

  /** Flag indicating whether namespaces have been processed for this node */
  public boolean m_hasProcessedNS;

  /** Flag indicating whether one of this node's ancestor has an XMLNS attribute          */
  public int m_ancestorHasXMLNSAttrs;

  /** Constant for ancestors XMLNS atributes not processed          */
  public static final int ANCESTORXMLNSUNPROCESSED = 0;

  /** Constant indicating an ancestor has an XMLNS attribute           */
  public static final int ANCESTORHASXMLNS = 1;

  /** Constant indicating ancestors don't have an XMLNS attribute           */
  public static final int ANCESTORNOXMLNS = 2;
}
