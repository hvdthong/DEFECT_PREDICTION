package org.apache.xml.serializer.utils;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.xml.sax.Attributes;

/**
 * Wraps a DOM attribute list in a SAX Attributes.
 * 
 * This class is a copy of the one in org.apache.xml.utils. 
 * It exists to cut the serializers dependancy on that package.
 * A minor changes from that package are:
 * DOMHelper reference changed to DOM2Helper, class is not "public"
 *  
 * This class is not a public API, it is only public because it is 
 * used in org.apache.xml.serializer.
 * 
 * @xsl.usage internal
 */
public final class AttList implements Attributes
{

  /** List of attribute nodes          */
  NamedNodeMap m_attrs;

  /** Index of last attribute node          */
  int m_lastIndex;


  /** Local reference to DOMHelper          */
  DOM2Helper m_dh;


  /**
   * Constructor AttList
   *
   *
   * @param attrs List of attributes this will contain
   * @param dh DOMHelper 
   */
  public AttList(NamedNodeMap attrs, DOM2Helper dh)
  {
    
    m_attrs = attrs;
    m_lastIndex = m_attrs.getLength() - 1;
    m_dh = dh;
  }

  /**
   * Get the number of attribute nodes in the list 
   *
   *
   * @return number of attribute nodes
   */
  public int getLength()
  {
    return m_attrs.getLength();
  }

  /**
   * Look up an attribute's Namespace URI by index.
   *
   * @param index The attribute index (zero-based).
   * @return The Namespace URI, or the empty string if none
   *         is available, or null if the index is out of
   *         range.
   */
  public String getURI(int index)
  {
    String ns = m_dh.getNamespaceOfNode(((Attr) m_attrs.item(index)));
    if(null == ns)
      ns = "";
    return ns;
  }

  /**
   * Look up an attribute's local name by index.
   *
   * @param index The attribute index (zero-based).
   * @return The local name, or the empty string if Namespace
   *         processing is not being performed, or null
   *         if the index is out of range.
   */
  public String getLocalName(int index)
  {
    return m_dh.getLocalNameOfNode(((Attr) m_attrs.item(index)));
  }

  /**
   * Look up an attribute's qualified name by index.
   *
   *
   * @param i The attribute index (zero-based).
   *
   * @return The attribute's qualified name
   */
  public String getQName(int i)
  {
    return ((Attr) m_attrs.item(i)).getName();
  }

  /**
   * Get the attribute's node type by index
   *
   *
   * @param i The attribute index (zero-based)
   *
   * @return the attribute's node type
   */
  public String getType(int i)
  {
  }

  /**
   * Get the attribute's node value by index
   *
   *
   * @param i The attribute index (zero-based)
   *
   * @return the attribute's node value
   */
  public String getValue(int i)
  {
    return ((Attr) m_attrs.item(i)).getValue();
  }

  /**
   * Get the attribute's node type by name
   *
   *
   * @param name Attribute name
   *
   * @return the attribute's node type
   */
  public String getType(String name)
  {
  }

  /**
   * Look up an attribute's type by Namespace name.
   *
   * @param uri The Namespace URI, or the empty String if the
   *        name has no Namespace URI.
   * @param localName The local name of the attribute.
   * @return The attribute type as a string, or null if the
   *         attribute is not in the list or if Namespace
   *         processing is not being performed.
   */
  public String getType(String uri, String localName)
  {
  }

  /**
   * Look up an attribute's value by name.
   *
   *
   * @param name The attribute node's name
   *
   * @return The attribute node's value
   */
  public String getValue(String name)
  {
    Attr attr = ((Attr) m_attrs.getNamedItem(name));
    return (null != attr) 
          ? attr.getValue() : null;
  }

  /**
   * Look up an attribute's value by Namespace name.
   *
   * @param uri The Namespace URI, or the empty String if the
   *        name has no Namespace URI.
   * @param localName The local name of the attribute.
   * @return The attribute value as a string, or null if the
   *         attribute is not in the list.
   */
  public String getValue(String uri, String localName)
  {
        Node a=m_attrs.getNamedItemNS(uri,localName);
        return (a==null) ? null : a.getNodeValue();
  }

  /**
   * Look up the index of an attribute by Namespace name.
   *
   * @param uri The Namespace URI, or the empty string if
   *        the name has no Namespace URI.
   * @param localPart The attribute's local name.
   * @return The index of the attribute, or -1 if it does not
   *         appear in the list.
   */
  public int getIndex(String uri, String localPart)
  {
    for(int i=m_attrs.getLength()-1;i>=0;--i)
    {
      Node a=m_attrs.item(i);
      String u=a.getNamespaceURI();
      if( (u==null ? uri==null : u.equals(uri))
      &&
      a.getLocalName().equals(localPart) )
    return i;
    }
    return -1;
  }

  /**
   * Look up the index of an attribute by raw XML 1.0 name.
   *
   * @param qName The qualified (prefixed) name.
   * @return The index of the attribute, or -1 if it does not
   *         appear in the list.
   */
  public int getIndex(String qName)
  {
    for(int i=m_attrs.getLength()-1;i>=0;--i)
    {
      Node a=m_attrs.item(i);
      if(a.getNodeName().equals(qName) )
    return i;
    }
    return -1;
  }
}

