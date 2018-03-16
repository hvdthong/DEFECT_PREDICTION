package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;

import java.util.Vector;
import java.util.Hashtable;

/**
 * This is a default implementation of a table that manages mappings from
 * expanded names to expandedNameIDs.
 *
 * %REVIEW% Note that this is not really a separate table, or a
 * separate pool. Instead, it's an access method build on top of the
 * existing pools, using three pieces of information: the index
 * numbers for a node's namespaceURI, localName, and node type, which
 * are combined to yield a composite index number.
 *
 * %TBD% startup sequence -- how this gets access to the appropriate
 * string pools in the DTMDocument/stylesheet.
 *
 * */
public class ExpandedNameTable
{

  /** Probably a reference to static pool.     */
  private DTMStringPool m_locNamesPool;

  /** Probably a reference to static pool.   */
  private DTMStringPool m_namespaceNames;
  
  /** Vector of extended types for this document   */
  private /*static*/ Vector m_extendedTypes;
  
  /** Next available extended type   */
  private int m_nextType;
    
  public static final int ELEMENT = ((int)DTM.ELEMENT_NODE) ;
  public static final int ATTRIBUTE = ((int)DTM.ATTRIBUTE_NODE) ;
  public static final int TEXT = ((int)DTM.TEXT_NODE) ;
  public static final int CDATA_SECTION = ((int)DTM.CDATA_SECTION_NODE) ;
  public static final int ENTITY_REFERENCE = ((int)DTM.ENTITY_REFERENCE_NODE) ;
  public static final int ENTITY = ((int)DTM.ENTITY_NODE) ;
  public static final int PROCESSING_INSTRUCTION = ((int)DTM.PROCESSING_INSTRUCTION_NODE) ;
  public static final int COMMENT = ((int)DTM.COMMENT_NODE) ;
  public static final int DOCUMENT = ((int)DTM.DOCUMENT_NODE) ;
  public static final int DOCUMENT_TYPE = ((int)DTM.DOCUMENT_TYPE_NODE) ;
  public static final int DOCUMENT_FRAGMENT =((int)DTM.DOCUMENT_FRAGMENT_NODE) ;
  public static final int NOTATION = ((int)DTM.NOTATION_NODE) ;
  public static final int NAMESPACE = ((int)DTM.NAMESPACE_NODE) ;
  
  Hashtable m_hashtable = new Hashtable();
  
	/** Workspace for lookup. NOT THREAD SAFE!
	 * */
	ExtendedType hashET=new ExtendedType(-1,"","");  
  

  /**
   * Create an expanded name table that uses private string pool lookup.
   */
  public ExpandedNameTable()
  {
    m_locNamesPool = new DTMSafeStringPool();
    m_namespaceNames = new DTMSafeStringPool();
    initExtendedTypes(); 
  }

  /**
   * Constructor ExpandedNameTable
   *
   * @param locNamesPool Local element names lookup.
   * @param namespaceNames Namespace values lookup.
   */
  public ExpandedNameTable(DTMStringPool locNamesPool,
                           DTMStringPool namespaceNames)
  {
    m_locNamesPool = locNamesPool;
    m_namespaceNames = namespaceNames;
    initExtendedTypes();
  }
  
  /**
   *  Initialize the vector of extended types with the 
   *  basic DOM node types. 
   */
  private void initExtendedTypes()
  {
    m_extendedTypes = new Vector();
    int i;
    for (i = 0; i < DTM.NTYPES; i++)
    {
      ExtendedType newET = new ExtendedType(i, "", ""); 
      m_extendedTypes.addElement(newET); 
      m_hashtable.put(newET, new Integer(i));
    }
    m_nextType = m_extendedTypes.size();
  }

  /**
   * Given an expanded name, return an ID.  If the expanded-name does not
   * exist in the internal tables, the entry will be created, and the ID will
   * be returned.  Any additional nodes that are created that have this
   * expanded name will use this ID.
   *
   * @param namespace
   * @param localName
   *
   * @return the expanded-name id of the node.
   */
  public int getExpandedTypeID(String namespace, String localName, int type)
  {
    /*int nsID = (null != namespace) ? m_namespaceNames.stringToIndex(namespace) : 0;
    int lnID = m_locNamesPool.stringToIndex(localName);
    
    int expandedTypeID = (type << (BITS_PER_NAMESPACE+BITS_PER_LOCALNAME)) 
                       | (nsID << BITS_PER_LOCALNAME) | lnID;
    return expandedTypeID;
*/
    if (null == namespace) 
      namespace = "";
    if (null == localName) 
      localName = "";
    hashET.redefine(type,namespace,localName);
    
    Object eType;
    if ((eType = m_hashtable.get(hashET)) != null )
      return ((Integer)eType).intValue();
    
    ExtendedType newET=new ExtendedType(type, namespace, localName);
    m_extendedTypes.addElement(newET);
    m_hashtable.put(newET, new Integer(m_nextType));
    return m_nextType++;
  }
  
  /**
   * Given a type, return an expanded name ID.Any additional nodes that are 
   * created that have this expanded name will use this ID.
   *
   * @param namespace
   * @param localName
   *
   * @return the expanded-name id of the node.
   */
  public int getExpandedTypeID(int type)
  {    
    return type;    
  }

  /**
   * Given an expanded-name ID, return the local name part.
   *
   * @param ExpandedNameID an ID that represents an expanded-name.
   * @return String Local name of this node, or null if the node has no name.
   */
  public String getLocalName(int ExpandedNameID)
  {
    ExtendedType etype = (ExtendedType)m_extendedTypes.elementAt (ExpandedNameID);
    return etype.localName;
  }
  
  /**
   * Given an expanded-name ID, return the local name ID.
   *
   * @param ExpandedNameID an ID that represents an expanded-name.
   * @return The id of this local name.
   */
  public /*static*/ final int getLocalNameID(int ExpandedNameID)
  {
    ExtendedType etype = (ExtendedType)m_extendedTypes.elementAt (ExpandedNameID);
    if (etype.localName.equals(""))
      return 0;
    else
    return ExpandedNameID;
  }


  /**
   * Given an expanded-name ID, return the namespace URI part.
   *
   * @param ExpandedNameID an ID that represents an expanded-name.
   * @return String URI value of this node's namespace, or null if no
   * namespace was resolved.
   */
  public String getNamespace(int ExpandedNameID)
  {

    ExtendedType etype = (ExtendedType)m_extendedTypes.elementAt (ExpandedNameID);
    return (etype.namespace.equals("") ? null : etype.namespace); 
  }
  
  /**
   * Given an expanded-name ID, return the namespace URI ID.
   *
   * @param ExpandedNameID an ID that represents an expanded-name.
   * @return The id of this namespace.
   */
  public /*static*/ final int getNamespaceID(int ExpandedNameID)
  {
    ExtendedType etype = (ExtendedType)m_extendedTypes.elementAt (ExpandedNameID);
    if (etype.namespace.equals(""))
      return 0;
    else
    return ExpandedNameID;
  }
  
  /**
   * Given an expanded-name ID, return the local name ID.
   *
   * @param ExpandedNameID an ID that represents an expanded-name.
   * @return The id of this local name.
   */
  public final short getType(int ExpandedNameID)
  {
    ExtendedType etype = (ExtendedType)m_extendedTypes.elementAt (ExpandedNameID);
    return (short)etype.nodetype;
  }
  
  
  /**
   * Private class representing an extended type object 
   */
  private class ExtendedType
  {
    protected int nodetype;
    protected String namespace;
    protected String localName;
    protected int hash;
    
    protected ExtendedType (int nodetype, String namespace, String localName)
    {
      this.nodetype = nodetype;
      this.namespace = namespace;
      this.localName = localName;
      this.hash=nodetype+namespace.hashCode()+localName.hashCode();
    }

	/* This is intended to be used ONLY on the hashET
	 * object. Using it elsewhere will mess up existing
	 * hashtable entries!
	 * */
    protected void redefine(int nodetype, String namespace, String localName)
    {
      this.nodetype = nodetype;
      this.namespace = namespace;
      this.localName = localName;
      this.hash=nodetype+namespace.hashCode()+localName.hashCode();
    }
    
    /* Override super method
	 * */
    public int hashCode() 
    {
    	return hash;
    }

    /* Override super method
	 * */
	public boolean equals(Object other)
	{
		try
		{
			ExtendedType et=(ExtendedType)other;
			return et.nodetype==this.nodetype &&
				et.localName.equals(this.localName) &&
				et.namespace.equals(this.namespace);
		}
		catch(ClassCastException e)
		{
			return false;
		}
		catch(NullPointerException e)
		{
			return false;
		}
	}
  }
  
}
