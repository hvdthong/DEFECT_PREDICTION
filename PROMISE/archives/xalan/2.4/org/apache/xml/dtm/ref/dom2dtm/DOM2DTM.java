package org.apache.xml.dtm.ref.dom2dtm;

import org.apache.xml.dtm.ref.*;
import org.apache.xml.dtm.*;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.TreeWalker;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XMLCharacterRecognizer;

import org.w3c.dom.*;

import java.util.Vector;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.SourceLocator;
import org.xml.sax.ContentHandler;

import org.apache.xml.utils.NodeVector;

import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.res.XSLMessages;

/** The <code>DOM2DTM</code> class serves up a DOM's contents via the
 * DTM API.
 *
 * Note that it doesn't necessarily represent a full Document
 * tree. You can wrap a DOM2DTM around a specific node and its subtree
 * and the right things should happen. (I don't _think_ we currently
 * support DocumentFrgment nodes as roots, though that might be worth
 * considering.)
 *
 * Note too that we do not currently attempt to track document
 * mutation. If you alter the DOM after wrapping DOM2DTM around it,
 * all bets are off.
 * */
public class DOM2DTM extends DTMDefaultBaseIterators
{
  static final boolean JJK_DEBUG=false;
  static final boolean JJK_NEWCODE=true;
  
  /** Manefest constant
   */
  
  /** The current position in the DOM tree. Last node examined for
   * possible copying to DTM. */
  transient private Node m_pos;
  /** The current position in the DTM tree. Who children get appended to. */
  private int m_last_parent=0;
  /** The current position in the DTM tree. Who children reference as their 
   * previous sib. */
  private int m_last_kid=NULL;

  /** The top of the subtree.
   * */
  transient private Node m_root;

  /** True iff the first element has been processed. This is used to control
      synthesis of the implied xml: namespace declaration node. */
  boolean m_processedFirstElement=false;
        
  /** true if ALL the nodes in the m_root subtree have been processed;
   * false if our incremental build has not yet finished scanning the
   * DOM tree.  */
  transient private boolean m_nodesAreProcessed;

  /** The node objects.  The instance part of the handle indexes
   * directly into this vector.  Each DTM node may actually be
   * composed of several DOM nodes (for example, if logically-adjacent
   * Text/CDATASection nodes in the DOM have been coalesced into a
   * single DTM Text node); this table points only to the first in
   * that sequence. */
  protected Vector m_nodes = new Vector();

  /**
   * Construct a DOM2DTM object from a DOM node.
   *
   * @param mgr The DTMManager who owns this DTM.
   * @param domSource the DOM source that this DTM will wrap.
   * @param dtmIdentity The DTM identity ID for this DTM.
   * @param whiteSpaceFilter The white space filter for this DTM, which may 
   *                         be null.
   * @param xstringfactory XMLString factory for creating character content.
   * @param doIndexing true if the caller considers it worth it to use 
   *                   indexing schemes.
   */
  public DOM2DTM(DTMManager mgr, DOMSource domSource, 
                 int dtmIdentity, DTMWSFilter whiteSpaceFilter,
                 XMLStringFactory xstringfactory,
                 boolean doIndexing)
  {
    super(mgr, domSource, dtmIdentity, whiteSpaceFilter, 
          xstringfactory, doIndexing);

    m_pos=m_root = domSource.getNode();
    m_last_parent=m_last_kid=NULL;
    m_last_kid=addNode(m_root, m_last_parent,m_last_kid, NULL);

    if(ELEMENT_NODE == m_root.getNodeType())
    {
      NamedNodeMap attrs=m_root.getAttributes();
      int attrsize=(attrs==null) ? 0 : attrs.getLength();
      if(attrsize>0)
      {
        for(int i=0;i<attrsize;++i)
        {
          attrIndex=addNode(attrs.item(i),0,attrIndex,NULL);
          m_firstch.setElementAt(DTM.NULL,attrIndex);
        }
        m_nextsib.setElementAt(DTM.NULL,attrIndex);


    m_nodesAreProcessed = false;
  }

  /**
   * Construct the node map from the node.
   *
   * @param node The node that is to be added to the DTM.
   * @param parentIndex The current parent index.
   * @param previousSibling The previous sibling index.
   * @param forceNodeType If not DTM.NULL, overrides the DOM node type.
   *	Used to force nodes to Text rather than CDATASection when their
   *	coalesced value includes ordinary Text nodes (current DTM behavior).
   *
   * @return The index identity of the node that was added.
   */
  protected int addNode(Node node, int parentIndex,
                        int previousSibling, int forceNodeType)
  {
    int nodeIndex = m_nodes.size();

    if(m_dtmIdent.size() == (nodeIndex>>>DTMManager.IDENT_DTM_NODE_BITS))
    {
      try
      {
        if(m_mgr==null)
          throw new ClassCastException();
                                
        DTMManagerDefault mgrD=(DTMManagerDefault)m_mgr;
        int id=mgrD.getFirstFreeDTMID();
        mgrD.addDTM(this,id,nodeIndex);
        m_dtmIdent.addElement(id<<DTMManager.IDENT_DTM_NODE_BITS);
      }
      catch(ClassCastException e)
      {
      }
    }

    m_size++;
    
    int type;
    if(NULL==forceNodeType)
        type = node.getNodeType();
    else
        type=forceNodeType;
        
    if (Node.ATTRIBUTE_NODE == type)
    {
      String name = node.getNodeName();

      if (name.startsWith("xmlns:") || name.equals("xmlns"))
      {
        type = DTM.NAMESPACE_NODE;
      }
    }
    
    m_nodes.addElement(node);
    
    m_firstch.setElementAt(NOTPROCESSED,nodeIndex);
    m_nextsib.setElementAt(NOTPROCESSED,nodeIndex);
    m_prevsib.setElementAt(previousSibling,nodeIndex);
    m_parent.setElementAt(parentIndex,nodeIndex);
    
    if(DTM.NULL != parentIndex && 
       type != DTM.ATTRIBUTE_NODE && 
       type != DTM.NAMESPACE_NODE)
    {
      if(NOTPROCESSED == m_firstch.elementAt(parentIndex))
        m_firstch.setElementAt(nodeIndex,parentIndex);
    }
    
    String nsURI = node.getNamespaceURI();

    String localName =  (type == Node.PROCESSING_INSTRUCTION_NODE) ? 
                         node.getNodeName() :
                         node.getLocalName();
                         
    if(((type == Node.ELEMENT_NODE) || (type == Node.ATTRIBUTE_NODE)) 
        && null == localName)
      
    ExpandedNameTable exnt = m_expandedNameTable;

    if(node.getLocalName()==null &&
       (type==Node.ELEMENT_NODE || type==Node.ATTRIBUTE_NODE))
      {
      }
    
    int expandedNameID = (null != localName) 
       ? exnt.getExpandedTypeID(nsURI, localName, type) :
         exnt.getExpandedTypeID(type);

    m_exptype.setElementAt(expandedNameID,nodeIndex);
    
    indexNode(expandedNameID, nodeIndex);

    if (DTM.NULL != previousSibling)
      m_nextsib.setElementAt(nodeIndex,previousSibling);

    if (type == DTM.NAMESPACE_NODE)
        declareNamespaceInContext(parentIndex,nodeIndex);

    return nodeIndex;
  }
  
  /**
   * Get the number of nodes that have been added.
   */
  protected int getNumberOfNodes()
  {
    return m_nodes.size();
  }
  
 /**
   * This method iterates to the next node that will be added to the table.
   * Each call to this method adds a new node to the table, unless the end
   * is reached, in which case it returns null.
   *
   * @return The true if a next node is found or false if 
   *         there are no more nodes.
   */
  protected boolean nextNode()
  {
    if (m_nodesAreProcessed)
      return false;
        
    Node pos=m_pos; 
    Node next=null;
    int nexttype=NULL;

    do
      {
        if (pos.hasChildNodes()) 
          {
            next = pos.getFirstChild();

            if(next!=null && DOCUMENT_TYPE_NODE==next.getNodeType())
              next=next.getNextSibling();

            if(ENTITY_REFERENCE_NODE!=pos.getNodeType())
              {
                m_last_parent=m_last_kid;
                m_last_kid=NULL;
                if(null != m_wsfilter)
                {
                  short wsv =
                    m_wsfilter.getShouldStripSpace(makeNodeHandle(m_last_parent),this);
                  boolean shouldStrip = (DTMWSFilter.INHERIT == wsv) 
                    ? getShouldStripWhitespace() 
                    : (DTMWSFilter.STRIP == wsv);
                  pushShouldStripWhitespace(shouldStrip);
              }
          }

        else 
          {
            if(m_last_kid!=NULL)
              {
                if(m_firstch.elementAt(m_last_kid)==NOTPROCESSED)
                  m_firstch.setElementAt(NULL,m_last_kid);
              }
                        
            while(m_last_parent != NULL)
              {
                next = pos.getNextSibling();
                if(next!=null && DOCUMENT_TYPE_NODE==next.getNodeType())
                  next=next.getNextSibling();

                if(next!=null)
                
                pos=pos.getParentNode();
                if(pos==null)
                  {
                    if(JJK_DEBUG)
                      {
                        System.out.println("***** DOM2DTM Pop Control Flow problem");
                      }
                  }
                
                if(pos!=null && ENTITY_REFERENCE_NODE == pos.getNodeType())
                  {
                    if(JJK_DEBUG)
                      System.out.println("***** DOM2DTM popping EntRef");
                  }
                else
                  {
                    popShouldStripWhitespace();
                    if(m_last_kid==NULL)
                    else
                    m_last_parent=m_parent.elementAt(m_last_kid=m_last_parent);
                  }
              }
            if(m_last_parent==NULL)
              next=null;
          }
                
        if(next!=null)
          nexttype=next.getNodeType();
                
        if (ENTITY_REFERENCE_NODE == nexttype)
          pos=next;
      }
    while (ENTITY_REFERENCE_NODE == nexttype); 
        
    if(next==null)
      {
        m_nextsib.setElementAt(NULL,0);
        m_nodesAreProcessed = true;
        m_pos=null;
                
        if(JJK_DEBUG)
          {
            System.out.println("***** DOM2DTM Crosscheck:");
            for(int i=0;i<m_nodes.size();++i)
              System.out.println(i+":\t"+m_firstch.elementAt(i)+"\t"+m_nextsib.elementAt(i));
          }
                
        return false;
      }

        
    boolean suppressNode=false;
    Node lastTextNode=null;

    nexttype=next.getNodeType();
        
    if(TEXT_NODE == nexttype || CDATA_SECTION_NODE == nexttype)
      {
        suppressNode=((null != m_wsfilter) && getShouldStripWhitespace());

        Node n=next;
        while(n!=null)
          {
            lastTextNode=n;
            if(TEXT_NODE == n.getNodeType())
              nexttype=TEXT_NODE;
            suppressNode &=
              XMLCharacterRecognizer.isWhiteSpace(n.getNodeValue());
                        
            n=logicalNextDOMTextNode(n);
          }
      }
        
    else if(PROCESSING_INSTRUCTION_NODE==nexttype)
      {
        suppressNode = (pos.getNodeName().toLowerCase().equals("xml"));
      }
        
        
    if(!suppressNode)
      {
        int nextindex=addNode(next,m_last_parent,m_last_kid,
			      nexttype);
	
        m_last_kid=nextindex;

        if(ELEMENT_NODE == nexttype)
          {
            NamedNodeMap attrs=next.getAttributes();
            int attrsize=(attrs==null) ? 0 : attrs.getLength();
            if(attrsize>0)
              {
                for(int i=0;i<attrsize;++i)
                  {
                    attrIndex=addNode(attrs.item(i),
                                      nextindex,attrIndex,NULL);
                    m_firstch.setElementAt(DTM.NULL,attrIndex);

                    if(!m_processedFirstElement
                       && "xmlns:xml".equals(attrs.item(i).getNodeName()))
                      m_processedFirstElement=true; 
                  }
            if(!m_processedFirstElement)
            {
              attrIndex=addNode(new DOM2DTMdefaultNamespaceDeclarationNode(
																	(Element)next,"xml",NAMESPACE_DECL_NS,
																	makeNodeHandle(((attrIndex==NULL)?nextindex:attrIndex)+1)
																	),
                                nextindex,attrIndex,NULL);      
              m_firstch.setElementAt(DTM.NULL,attrIndex);
              m_processedFirstElement=true;
            }
            if(attrIndex!=NULL)
              m_nextsib.setElementAt(DTM.NULL,attrIndex);

    if(TEXT_NODE == nexttype || CDATA_SECTION_NODE == nexttype)
      {
                
      }
        
    m_pos=next;
    return true;
  }  


  /**
   * Return an DOM node for the given node.
   *
   * @param nodeHandle The node ID.
   *
   * @return A node representation of the DTM node.
   */
  public Node getNode(int nodeHandle)
  {

    int identity = makeNodeIdentity(nodeHandle);

    return (Node) m_nodes.elementAt(identity);
  }

  /**
   * Get a Node from an identity index.
   *
   * NEEDSDOC @param nodeIdentity
   *
   * NEEDSDOC ($objectName$) @return
   */
  protected Node lookupNode(int nodeIdentity)
  {
    return (Node) m_nodes.elementAt(nodeIdentity);
  }

  /**
   * Get the next node identity value in the list, and call the iterator
   * if it hasn't been added yet.
   *
   * @param identity The node identity (index).
   * @return identity+1, or DTM.NULL.
   */
  protected int getNextNodeIdentity(int identity)
  {

    identity += 1;

    if (identity >= m_nodes.size())
    {
      if (!nextNode())
        identity = DTM.NULL;
    }

    return identity;
  }

  /**
   * Get the handle from a Node.
   * <p>%OPT% This will be pretty slow.</p>
   *
   * <p>%OPT% An XPath-like search (walk up DOM to root, tracking path;
   * walk down DTM reconstructing path) might be considerably faster
   * on later nodes in large documents. That might also imply improving
   * this call to handle nodes which would be in this DTM but
   * have not yet been built, which might or might not be a Good Thing.</p>
   * 
   * %REVIEW% This relies on being able to test node-identity via
   * object-identity. DTM2DOM proxying is a great example of a case where
   * that doesn't work. DOM Level 3 will provide the isSameNode() method
   * to fix that, but until then this is going to be flaky.
   *
   * @param node A node, which may be null.
   *
   * @return The node handle or <code>DTM.NULL</code>.
   */
  private int getHandleFromNode(Node node)
  {
    if (null != node)
    {
      int len = m_nodes.size();        
      boolean isMore;
      int i = 0;
      do
      {          
        for (; i < len; i++)
        {
          if (m_nodes.elementAt(i) == node)
            return makeNodeHandle(i);
        }

        isMore = nextNode();
  
        len = m_nodes.size();
            
      } 
      while(isMore || i < len);
    }
    
    return DTM.NULL;
  }

  /** Get the handle from a Node. This is a more robust version of
   * getHandleFromNode, intended to be usable by the public.
   *
   * <p>%OPT% This will be pretty slow.</p>
   * 
   * %REVIEW% This relies on being able to test node-identity via
   * object-identity. DTM2DOM proxying is a great example of a case where
   * that doesn't work. DOM Level 3 will provide the isSameNode() method
   * to fix that, but until then this is going to be flaky.
   *
   * @param node A node, which may be null.
   *
   * @return The node handle or <code>DTM.NULL</code>.  */
  public int getHandleOfNode(Node node)
  {
    if (null != node)
    {
      if((m_root==node) ||
         (m_root.getNodeType()==DOCUMENT_NODE &&
          m_root==node.getOwnerDocument()) ||
         (m_root.getNodeType()!=DOCUMENT_NODE &&
          m_root.getOwnerDocument()==node.getOwnerDocument())
         )
        {
          for(Node cursor=node;
              cursor!=null;
              cursor=
                (cursor.getNodeType()!=ATTRIBUTE_NODE)
                ? cursor.getParentNode()
                : ((org.w3c.dom.Attr)cursor).getOwnerElement())
            {
              if(cursor==m_root)
                return getHandleFromNode(node); 

    return DTM.NULL;
  }

  /**
   * Retrieves an attribute node by by qualified name and namespace URI.
   *
   * @param nodeHandle int Handle of the node upon which to look up this attribute..
   * @param namespaceURI The namespace URI of the attribute to
   *   retrieve, or null.
   * @param name The local name of the attribute to
   *   retrieve.
   * @return The attribute node handle with the specified name (
   *   <code>nodeName</code>) or <code>DTM.NULL</code> if there is no such
   *   attribute.
   */
  public int getAttributeNode(int nodeHandle, String namespaceURI,
                              String name)
  {

    if (null == namespaceURI)
      namespaceURI = "";

    int type = getNodeType(nodeHandle);

    if (DTM.ELEMENT_NODE == type)
    {

      int identity = makeNodeIdentity(nodeHandle);

      while (DTM.NULL != (identity = getNextNodeIdentity(identity)))
      {
        type = _type(identity);

        if (type == DTM.ATTRIBUTE_NODE || type==DTM.NAMESPACE_NODE)
        {
          Node node = lookupNode(identity);
          String nodeuri = node.getNamespaceURI();

          if (null == nodeuri)
            nodeuri = "";

          String nodelocalname = node.getLocalName();

          if (nodeuri.equals(namespaceURI) && name.equals(nodelocalname))
            return makeNodeHandle(identity);
        }
				
        {
          break;
        }
      }
    }

    return DTM.NULL;
  }

  /**
   * Get the string-value of a node as a String object
   * for the definition of a node's string-value).
   *
   * @param nodeHandle The node ID.
   *
   * @return A string object that represents the string-value of the given node.
   */
  public XMLString getStringValue(int nodeHandle)
  {

    int type = getNodeType(nodeHandle);
    Node node = getNode(nodeHandle);
    if(DTM.ELEMENT_NODE == type || DTM.DOCUMENT_NODE == type 
    || DTM.DOCUMENT_FRAGMENT_NODE == type)
    {
      FastStringBuffer buf = StringBufferPool.get();
      String s;
  
      try
      {
        getNodeData(node, buf);
  
        s = (buf.length() > 0) ? buf.toString() : "";
      }
      finally
      {
        StringBufferPool.free(buf);
      }
  
      return m_xstrf.newstr( s );
    }
    else if(TEXT_NODE == type || CDATA_SECTION_NODE == type)
    {
      FastStringBuffer buf = StringBufferPool.get();
      while(node!=null)
      {
        buf.append(node.getNodeValue());
        node=logicalNextDOMTextNode(node);
      }
      String s=(buf.length() > 0) ? buf.toString() : "";
      StringBufferPool.free(buf);
      return m_xstrf.newstr( s );
    }
    else
      return m_xstrf.newstr( node.getNodeValue() );
  }
  
  /**
   * Retrieve the text content of a DOM subtree, appending it into a
   * user-supplied FastStringBuffer object. Note that attributes are
   * not considered part of the content of an element.
   * <p>
   * There are open questions regarding whitespace stripping. 
   * Currently we make no special effort in that regard, since the standard
   * DOM doesn't yet provide DTD-based information to distinguish
   * whitespace-in-element-context from genuine #PCDATA. Note that we
   * should probably also consider xml:space if/when we address this.
   * DOM Level 3 may solve the problem for us.
   * <p>
   * %REVIEW% Actually, since this method operates on the DOM side of the
   * fence rather than the DTM side, it SHOULDN'T do
   * any special handling. The DOM does what the DOM does; if you want
   * DTM-level abstractions, use DTM-level methods.
   *
   * @param node Node whose subtree is to be walked, gathering the
   * contents of all Text or CDATASection nodes.
   * @param buf FastStringBuffer into which the contents of the text
   * nodes are to be concatenated.
   */
  protected static void getNodeData(Node node, FastStringBuffer buf)
  {

    switch (node.getNodeType())
    {
    case Node.DOCUMENT_FRAGMENT_NODE :
    case Node.DOCUMENT_NODE :
    case Node.ELEMENT_NODE :
    {
      for (Node child = node.getFirstChild(); null != child;
              child = child.getNextSibling())
      {
        getNodeData(child, buf);
      }
    }
    break;
    case Node.TEXT_NODE :
    case Node.CDATA_SECTION_NODE :
      buf.append(node.getNodeValue());
      break;
    case Node.PROCESSING_INSTRUCTION_NODE :
      break;
    default :
      break;
    }
  }

  /**
   * Given a node handle, return its DOM-style node name. This will
   * include names such as #text or #document.
   *
   * @param nodeHandle the id of the node.
   * @return String Name of this node, which may be an empty string.
   * %REVIEW% Document when empty string is possible...
   * %REVIEW-COMMENT% It should never be empty, should it?
   */
  public String getNodeName(int nodeHandle)
  {

    Node node = getNode(nodeHandle);

    return node.getNodeName();
  }

  /**
   * Given a node handle, return the XPath node name.  This should be
   * the name as described by the XPath data model, NOT the DOM-style
   * name.
   *
   * @param nodeHandle the id of the node.
   * @return String Name of this node, which may be an empty string.
   */
  public String getNodeNameX(int nodeHandle)
  {

    String name;
    short type = getNodeType(nodeHandle);

    switch (type)
    {
    case DTM.NAMESPACE_NODE :
    {
      Node node = getNode(nodeHandle);

      name = node.getNodeName();
      if(name.startsWith("xmlns:"))
      {
        name = QName.getLocalPart(name);
      }
      else if(name.equals("xmlns"))
      {
        name = "";
      }
    }
    break;
    case DTM.ATTRIBUTE_NODE :
    case DTM.ELEMENT_NODE :
    case DTM.ENTITY_REFERENCE_NODE :
    case DTM.PROCESSING_INSTRUCTION_NODE :
    {
      Node node = getNode(nodeHandle);

      name = node.getNodeName();
    }
    break;
    default :
      name = "";
    }

    return name;
  }

  /**
   * Given a node handle, return its XPath-style localname.
   * (As defined in Namespaces, this is the portion of the name after any
   * colon character).
   *
   * @param nodeHandle the id of the node.
   * @return String Local name of this node.
   */
  public String getLocalName(int nodeHandle)
  {
    if(JJK_NEWCODE)
    {
      int id=makeNodeIdentity(nodeHandle);
      if(NULL==id) return null;
      Node newnode=(Node)m_nodes.elementAt(id);
      String newname=newnode.getLocalName();
      if (null == newname)
      {
	String qname = newnode.getNodeName();
	if('#'==newnode.getNodeName().charAt(0))
	{
	  newname="";
	}
	else
	{
	  int index = qname.indexOf(':');
	  newname = (index < 0) ? qname : qname.substring(index + 1);
	}
      }
      return newname;
    }
    else
    {
      String name;
      short type = getNodeType(nodeHandle);
      switch (type)
      {
      case DTM.ATTRIBUTE_NODE :
      case DTM.ELEMENT_NODE :
      case DTM.ENTITY_REFERENCE_NODE :
      case DTM.NAMESPACE_NODE :
      case DTM.PROCESSING_INSTRUCTION_NODE :
	{
	  Node node = getNode(nodeHandle);
	  
	  name = node.getLocalName();
	  
	  if (null == name)
	  {
	    String qname = node.getNodeName();
	    int index = qname.indexOf(':');
	    
	    name = (index < 0) ? qname : qname.substring(index + 1);
	  }
	}
	break;
      default :
	name = "";
      }
      return name;
    }
  }

  /**
   * Given a namespace handle, return the prefix that the namespace decl is
   * mapping.
   * Given a node handle, return the prefix used to map to the namespace.
   *
   * <p> %REVIEW% Are you sure you want "" for no prefix?  </p>
   * <p> %REVIEW-COMMENT% I think so... not totally sure. -sb  </p>
   *
   * @param nodeHandle the id of the node.
   * @return String prefix of this node's name, or "" if no explicit
   * namespace prefix was given.
   */
  public String getPrefix(int nodeHandle)
  {

    String prefix;
    short type = getNodeType(nodeHandle);

    switch (type)
    {
    case DTM.NAMESPACE_NODE :
    {
      Node node = getNode(nodeHandle);

      String qname = node.getNodeName();
      int index = qname.indexOf(':');

      prefix = (index < 0) ? "" : qname.substring(index + 1);
    }
    break;
    case DTM.ATTRIBUTE_NODE :
    case DTM.ELEMENT_NODE :
    {
      Node node = getNode(nodeHandle);

      String qname = node.getNodeName();
      int index = qname.indexOf(':');

      prefix = (index < 0) ? "" : qname.substring(0, index);
    }
    break;
    default :
      prefix = "";
    }

    return prefix;
  }

  /**
   * Given a node handle, return its DOM-style namespace URI
   * (As defined in Namespaces, this is the declared URI which this node's
   * prefix -- or default in lieu thereof -- was mapped to.)
   *
   * <p>%REVIEW% Null or ""? -sb</p>
   *
   * @param nodeHandle the id of the node.
   * @return String URI value of this node's namespace, or null if no
   * namespace was resolved.
   */
  public String getNamespaceURI(int nodeHandle)
  {
    if(JJK_NEWCODE)
    {
      int id=makeNodeIdentity(nodeHandle);
      if(id==NULL) return null;
      Node node=(Node)m_nodes.elementAt(id);
      return node.getNamespaceURI();
    }
    else
    {
      String nsuri;
      short type = getNodeType(nodeHandle);
      
      switch (type)
      {
      case DTM.ATTRIBUTE_NODE :
      case DTM.ELEMENT_NODE :
      case DTM.ENTITY_REFERENCE_NODE :
      case DTM.NAMESPACE_NODE :
      case DTM.PROCESSING_INSTRUCTION_NODE :
	{
	  Node node = getNode(nodeHandle);
	  
	  nsuri = node.getNamespaceURI();
	  
	}
	break;
      default :
	nsuri = null;
      }

      return nsuri;
    }
    
  }
  
  /** Utility function: Given a DOM Text node, determine whether it is
   * logically followed by another Text or CDATASection node. This may
   * involve traversing into Entity References.
   * 
   * %REVIEW% DOM Level 3 is expected to add functionality which may 
   * allow us to retire this.
   */
  private Node logicalNextDOMTextNode(Node n)
  {
        Node p=n.getNextSibling();
        if(p==null)
        {
                for(n=n.getParentNode();
                        n!=null && ENTITY_REFERENCE_NODE == n.getNodeType();
                        n=n.getParentNode())
                {
                        p=n.getNextSibling();
                        if(p!=null)
                                break;
                }
        }
        n=p;
        while(n!=null && ENTITY_REFERENCE_NODE == n.getNodeType())
        {
                if(n.hasChildNodes())
                        n=n.getFirstChild();
                else
                        n=n.getNextSibling();
        }
        if(n!=null)
        {
                int ntype=n.getNodeType();
                if(TEXT_NODE != ntype && CDATA_SECTION_NODE != ntype)
                        n=null;
        }
        return n;
  }

  /**
   * Given a node handle, return its node value. This is mostly
   * as defined by the DOM, but may ignore some conveniences.
   * <p>
   *
   * @param nodeHandle The node id.
   * @return String Value of this node, or null if not
   * meaningful for this node type.
   */
  public String getNodeValue(int nodeHandle)
  {
    int type = _exptype(makeNodeIdentity(nodeHandle));
    type=(NULL != type) ? getNodeType(nodeHandle) : NULL;
    
    if(TEXT_NODE!=type && CDATA_SECTION_NODE!=type)
      return getNode(nodeHandle).getNodeValue();
    
    Node node = getNode(nodeHandle);
    Node n=logicalNextDOMTextNode(node);
    if(n==null)
      return node.getNodeValue();
    
    FastStringBuffer buf = StringBufferPool.get();
        buf.append(node.getNodeValue());
    while(n!=null)
    {
      buf.append(n.getNodeValue());
      n=logicalNextDOMTextNode(n);
    }
    String s = (buf.length() > 0) ? buf.toString() : "";
    StringBufferPool.free(buf);
    return s;
  }

  /**
   *   A document type declaration information item has the following properties:
   *
   *     1. [system identifier] The system identifier of the external subset, if
   *        it exists. Otherwise this property has no value.
   *
   * @return the system identifier String object, or null if there is none.
   */
  public String getDocumentTypeDeclarationSystemIdentifier()
  {

    Document doc;

    if (m_root.getNodeType() == Node.DOCUMENT_NODE)
      doc = (Document) m_root;
    else
      doc = m_root.getOwnerDocument();

    if (null != doc)
    {
      DocumentType dtd = doc.getDoctype();

      if (null != dtd)
      {
        return dtd.getSystemId();
      }
    }

    return null;
  }

  /**
   * Return the public identifier of the external subset,
   * normalized as described in 4.2.2 External Entities [XML]. If there is
   * no external subset or if it has no public identifier, this property
   * has no value.
   *
   * @param the document type declaration handle
   *
   * @return the public identifier String object, or null if there is none.
   */
  public String getDocumentTypeDeclarationPublicIdentifier()
  {

    Document doc;

    if (m_root.getNodeType() == Node.DOCUMENT_NODE)
      doc = (Document) m_root;
    else
      doc = m_root.getOwnerDocument();

    if (null != doc)
    {
      DocumentType dtd = doc.getDoctype();

      if (null != dtd)
      {
        return dtd.getPublicId();
      }
    }

    return null;
  }

  /**
   * Returns the <code>Element</code> whose <code>ID</code> is given by
   * <code>elementId</code>. If no such element exists, returns
   * <code>DTM.NULL</code>. Behavior is not defined if more than one element
   * has this <code>ID</code>. Attributes (including those
   * with the name "ID") are not of type ID unless so defined by DTD/Schema
   * information available to the DTM implementation.
   * Implementations that do not know whether attributes are of type ID or
   * not are expected to return <code>DTM.NULL</code>.
   *
   * <p>%REVIEW% Presumably IDs are still scoped to a single document,
   * and this operation searches only within a single document, right?
   * Wouldn't want collisions between DTMs in the same process.</p>
   *
   * @param elementId The unique <code>id</code> value for an element.
   * @return The handle of the matching element.
   */
  public int getElementById(String elementId)
  {

    Document doc = (m_root.getNodeType() == Node.DOCUMENT_NODE) 
        ? (Document) m_root : m_root.getOwnerDocument();
        
    if(null != doc)
    {
      Node elem = doc.getElementById(elementId);
      if(null != elem)
      {
        int elemHandle = getHandleFromNode(elem);
        
        if(DTM.NULL == elemHandle)
        {
          int identity = m_nodes.size()-1;
          while (DTM.NULL != (identity = getNextNodeIdentity(identity)))
          {
            Node node = getNode(identity);
            if(node == elem)
            {
              elemHandle = getHandleFromNode(elem);
              break;
            }
           }
        }
        
        return elemHandle;
      }
    
    }
    return DTM.NULL;
  }

  /**
   * The getUnparsedEntityURI function returns the URI of the unparsed
   * entity with the specified name in the same document as the context
   * node (see [3.3 Unparsed Entities]). It returns the empty string if
   * there is no such entity.
   * <p>
   * XML processors may choose to use the System Identifier (if one
   * is provided) to resolve the entity, rather than the URI in the
   * Public Identifier. The details are dependent on the processor, and
   * we would have to support some form of plug-in resolver to handle
   * this properly. Currently, we simply return the System Identifier if
   * present, and hope that it a usable URI or that our caller can
   * map it to one.
   * TODO: Resolve Public Identifiers... or consider changing function name.
   * <p>
   * If we find a relative URI
   * reference, XML expects it to be resolved in terms of the base URI
   * of the document. The DOM doesn't do that for us, and it isn't
   * entirely clear whether that should be done here; currently that's
   * pushed up to a higher level of our application. (Note that DOM Level
   * 1 didn't store the document's base URI.)
   * TODO: Consider resolving Relative URIs.
   * <p>
   * (The DOM's statement that "An XML processor may choose to
   * completely expand entities before the structure model is passed
   * to the DOM" refers only to parsed entities, not unparsed, and hence
   * doesn't affect this function.)
   *
   * @param name A string containing the Entity Name of the unparsed
   * entity.
   *
   * @return String containing the URI of the Unparsed Entity, or an
   * empty string if no such entity exists.
   */
  public String getUnparsedEntityURI(String name)
  {

    String url = "";
    Document doc = (m_root.getNodeType() == Node.DOCUMENT_NODE) 
        ? (Document) m_root : m_root.getOwnerDocument();

    if (null != doc)
    {
      DocumentType doctype = doc.getDoctype();
  
      if (null != doctype)
      {
        NamedNodeMap entities = doctype.getEntities();
        if(null == entities)
          return url;
        Entity entity = (Entity) entities.getNamedItem(name);
        if(null == entity)
          return url;
        
        String notationName = entity.getNotationName();
  
        {
          url = entity.getSystemId();
  
          if (null == url)
          {
            url = entity.getPublicId();
          }
          else
          {
          }        
        }
      }
    }

    return url;
  }

  /**
   *     5. [specified] A flag indicating whether this attribute was actually
   *        specified in the start-tag of its element, or was defaulted from the
   *        DTD.
   *
   * @param the attribute handle
   *
   * NEEDSDOC @param attributeHandle
   * @return <code>true</code> if the attribute was specified;
   *         <code>false</code> if it was defaulted.
   */
  public boolean isAttributeSpecified(int attributeHandle)
  {
    int type = getNodeType(attributeHandle);

    if (DTM.ATTRIBUTE_NODE == type)
    {
      Attr attr = (Attr)getNode(attributeHandle);
      return attr.getSpecified();
    }
    return false;
  }

  /** Bind an IncrementalSAXSource to this DTM. NOT RELEVANT for DOM2DTM, since
   * we're wrapped around an existing DOM.
   *
   * @param source The IncrementalSAXSource that we want to recieve events from
   * on demand.
   */
  public void setIncrementalSAXSource(IncrementalSAXSource source)
  {
  }
  
  /** getContentHandler returns "our SAX builder" -- the thing that
   * someone else should send SAX events to in order to extend this
   * DTM model.
   *
   * @return null if this model doesn't respond to SAX events,
   * "this" if the DTM object has a built-in SAX ContentHandler,
   * the IncrmentalSAXSource if we're bound to one and should receive
   * the SAX stream via it for incremental build purposes...
   * */
  public org.xml.sax.ContentHandler getContentHandler()
  {
      return null;
  }
  
  /**
   * Return this DTM's lexical handler.
   *
   * %REVIEW% Should this return null if constrution already done/begun?
   *
   * @return null if this model doesn't respond to lexical SAX events,
   * "this" if the DTM object has a built-in SAX ContentHandler,
   * the IncrementalSAXSource if we're bound to one and should receive
   * the SAX stream via it for incremental build purposes...
   */
  public org.xml.sax.ext.LexicalHandler getLexicalHandler()
  {

    return null;
  }

  
  /**
   * Return this DTM's EntityResolver.
   *
   * @return null if this model doesn't respond to SAX entity ref events.
   */
  public org.xml.sax.EntityResolver getEntityResolver()
  {

    return null;
  }
  
  /**
   * Return this DTM's DTDHandler.
   *
   * @return null if this model doesn't respond to SAX dtd events.
   */
  public org.xml.sax.DTDHandler getDTDHandler()
  {

    return null;
  }

  /**
   * Return this DTM's ErrorHandler.
   *
   * @return null if this model doesn't respond to SAX error events.
   */
  public org.xml.sax.ErrorHandler getErrorHandler()
  {

    return null;
  }
  
  /**
   * Return this DTM's DeclHandler.
   *
   * @return null if this model doesn't respond to SAX Decl events.
   */
  public org.xml.sax.ext.DeclHandler getDeclHandler()
  {

    return null;
  }  

  /** @return true iff we're building this model incrementally (eg
   * we're partnered with a IncrementalSAXSource) and thus require that the
   * transformation and the parse run simultaneously. Guidance to the
   * DTMManager.
   * */
  public boolean needsTwoThreads()
  {
    return false;
  }

  
  /**
   * Returns whether the specified <var>ch</var> conforms to the XML 1.0 definition
   * the definition of <CODE>S</CODE></A> for details.
   * @param   ch      Character to check as XML whitespace.
   * @return          =true if <var>ch</var> is XML whitespace; otherwise =false.
   */
  private static boolean isSpace(char ch)
  {
  }

  /**
   * Directly call the
   * characters method on the passed ContentHandler for the
   * for the definition of a node's string-value). Multiple calls to the
   * ContentHandler's characters methods may well occur for a single call to
   * this method.
   *
   * @param nodeHandle The node ID.
   * @param ch A non-null reference to a ContentHandler.
   *
   * @throws org.xml.sax.SAXException
   */
  public void dispatchCharactersEvents(
          int nodeHandle, org.xml.sax.ContentHandler ch, 
          boolean normalize)
            throws org.xml.sax.SAXException
  {
    if(normalize)
    {
      XMLString str = getStringValue(nodeHandle);
      str = str.fixWhiteSpace(true, true, false);
      str.dispatchCharactersEvents(ch);
    }
    else
    {
      int type = getNodeType(nodeHandle);
      Node node = getNode(nodeHandle);
      dispatchNodeData(node, ch, 0);
          if(TEXT_NODE == type || CDATA_SECTION_NODE == type)
          {
                  while( null != (node=logicalNextDOMTextNode(node)) )
                  {
                      dispatchNodeData(node, ch, 0);
                  }
          }
    }
  }
  
  /**
   * Retrieve the text content of a DOM subtree, appending it into a
   * user-supplied FastStringBuffer object. Note that attributes are
   * not considered part of the content of an element.
   * <p>
   * There are open questions regarding whitespace stripping. 
   * Currently we make no special effort in that regard, since the standard
   * DOM doesn't yet provide DTD-based information to distinguish
   * whitespace-in-element-context from genuine #PCDATA. Note that we
   * should probably also consider xml:space if/when we address this.
   * DOM Level 3 may solve the problem for us.
   * <p>
   * %REVIEW% Note that as a DOM-level operation, it can be argued that this
   * routine _shouldn't_ perform any processing beyond what the DOM already
   * does, and that whitespace stripping and so on belong at the DTM level.
   * If you want a stripped DOM view, wrap DTM2DOM around DOM2DTM.
   *
   * @param node Node whose subtree is to be walked, gathering the
   * contents of all Text or CDATASection nodes.
   * @param buf FastStringBuffer into which the contents of the text
   * nodes are to be concatenated.
   */
  protected static void dispatchNodeData(Node node, 
                                         org.xml.sax.ContentHandler ch, 
                                         int depth)
            throws org.xml.sax.SAXException
  {

    switch (node.getNodeType())
    {
    case Node.DOCUMENT_FRAGMENT_NODE :
    case Node.DOCUMENT_NODE :
    case Node.ELEMENT_NODE :
    {
      for (Node child = node.getFirstChild(); null != child;
              child = child.getNextSibling())
      {
        dispatchNodeData(child, ch, depth+1);
      }
    }
    break;
    case Node.COMMENT_NODE :
      if(0 != depth)
        break;
    case Node.TEXT_NODE :
    case Node.CDATA_SECTION_NODE :
    case Node.ATTRIBUTE_NODE :
      String str = node.getNodeValue();
      if(ch instanceof CharacterNodeHandler)
      {
        ((CharacterNodeHandler)ch).characters(node);
      }
      else
      {
        ch.characters(str.toCharArray(), 0, str.length());
      }
      break;
    default :
      break;
    }
  }
  
  TreeWalker m_walker = new TreeWalker(null);
  
  /**
   * Directly create SAX parser events from a subtree.
   *
   * @param nodeHandle The node ID.
   * @param ch A non-null reference to a ContentHandler.
   *
   * @throws org.xml.sax.SAXException
   */
  public void dispatchToEvents(int nodeHandle, org.xml.sax.ContentHandler ch)
          throws org.xml.sax.SAXException
  {
    TreeWalker treeWalker = m_walker;
    ContentHandler prevCH = treeWalker.getContentHandler();
    
    if(null != prevCH)
    {
      treeWalker = new TreeWalker(null);
    }
    treeWalker.setContentHandler(ch);
    
    try
    {
      Node node = getNode(nodeHandle);
      treeWalker.traverse(node);
    }
    finally
    {
      treeWalker.setContentHandler(null);
    }
  }
  
  public interface CharacterNodeHandler
  {
    public void characters(Node node)
            throws org.xml.sax.SAXException;
  }

  /**
   * For the moment all the run time properties are ignored by this
   * class.
   *
   * @param property a <code>String</code> value
   * @param value an <code>Object</code> value
   */
  public void setProperty(String property, Object value)
  {
  }
  
  /**
   * No source information is available for DOM2DTM, so return
   * <code>null</code> here.
   *
   * @param node an <code>int</code> value
   * @return null
   */
  public SourceLocator getSourceLocatorFor(int node)
  {
    return null;
  }
}


