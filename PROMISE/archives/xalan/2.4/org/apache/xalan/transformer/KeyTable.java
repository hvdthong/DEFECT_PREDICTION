package org.apache.xalan.transformer;

import java.util.Hashtable;
import java.util.Vector;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;

/**
 * <meta name="usage" content="advanced"/>
 * Table of element keys, keyed by document node.  An instance of this
 * class is keyed by a Document node that should be matched with the
 * root of the current context.  It contains a table of name mappings
 * to tables that contain mappings of identifier values to nodes.
 */
public class KeyTable
{

  /**
   * The document key.  This table should only be used with contexts
   * whose Document roots match this key.
   */
  private int m_docKey;

  /**
   * Get the document root matching this key.  
   *
   *
   * @return the document root matching this key
   */
  public int getDocKey()
  {
    return m_docKey;
  }

  /** 
   * The main iterator that will walk through the source  
   * tree for this key.
   */
  private XNodeSet m_keyNodes;
  
  KeyIterator getKeyIterator()
  {
  	return (KeyIterator)(m_keyNodes.getContainedIter());
  }

  /**
   * Build a keys table.
   * @param doc The owner document key.
   * @param nscontext The stylesheet's namespace context.
   * @param name The key name
   * @param keyDeclarations The stylesheet's xsl:key declarations.
   * @param xmlLiaison The parser liaison for support of getNodeData(useNode).
   *
   * @throws javax.xml.transform.TransformerException
   */
  public KeyTable(
          int doc, PrefixResolver nscontext, QName name, Vector keyDeclarations, XPathContext xctxt)
            throws javax.xml.transform.TransformerException
  {

    m_docKey = doc;
    KeyIterator ki = new KeyIterator(name, keyDeclarations);
    
    m_keyNodes = new XNodeSet(ki);
    m_keyNodes.allowDetachToRelease(false);
    
    m_keyNodes.setRoot(doc, xctxt);

  }  

  /**
   * Given a valid element key, return the corresponding node list.
   * 
   * @param The name of the key, which must match the 'name' attribute on xsl:key.
   * @param ref The value that must match the value found by the 'match' attribute on xsl:key.
   * @return If the name was not declared with xsl:key, this will return null,
   * if the identifier is not found, it will return null,
   * otherwise it will return a LocPathIterator instance.
   */
  public XNodeSet getNodeSetDTMByKey(QName name, XMLString ref)
  {
  	Vector keyDecls = getKeyIterator().getKeyDeclarations();
  	org.apache.xml.dtm.DTMIterator keyNodes = m_keyNodes.iter();
	XNodeSet refNodes = new XNodeSet( new KeyRefIterator(name, ref, keyDecls, keyNodes) );
	
	return refNodes;

  }

  /**
   * Get Key Name for this KeyTable  
   *
   *
   * @return Key name
   */
  public QName getKeyTableName()
  {
    return getKeyIterator().getName();
  }
  
}
