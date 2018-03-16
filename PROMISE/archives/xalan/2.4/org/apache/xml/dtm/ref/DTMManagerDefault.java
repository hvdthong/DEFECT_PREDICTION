package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.*;

import java.util.Vector;

import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;

import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTM;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.dtm.ref.sax2dtm.SAX2RTFDTM;

/**************************************************************
import org.apache.xml.dtm.ref.xni2dtm.XNI2DTM;
**************************************************************/

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.res.XSLMessages;

/**
 * The default implementation for the DTMManager.
 *
 * %REVIEW% There is currently a reentrancy issue, since the finalizer
 * for XRTreeFrag (which runs in the GC thread) wants to call
 * DTMManager.release(), and may do so at the same time that the main
 * transformation thread is accessing the manager. Our current solution is
 * to make most of the manager's methods <code>synchronized</code>.
 * Early tests suggest that doing so is not causing a significant
 * performance hit in Xalan. However, it should be noted that there
 * is a possible alternative solution: rewrite release() so it merely
 * posts a request for release onto a threadsafe queue, and explicitly
 * process that queue on an infrequent basis during main-thread
 * activity (eg, when getDTM() is invoked). The downside of that solution
 * would be a greater delay before the DTM's storage is actually released
 * for reuse.
 * */
public class DTMManagerDefault extends DTMManager
{

  /** Set this to true if you want a dump of the DTM after creation. */
  private static final boolean DUMPTREE = false;

  /** Set this to true if you want a basic diagnostics. */
  private static final boolean DEBUG = false;

  /**
   * Map from DTM identifier numbers to DTM objects that this manager manages.
   * One DTM may have several prefix numbers, if extended node indexing
   * is in use; in that case, m_dtm_offsets[] will used to control which
   * prefix maps to which section of the DTM.
   * 
   * This array grows as necessary; see addDTM().
   * 
   * This array grows as necessary; see addDTM(). Growth is uncommon... but
   * access needs to be blindingly fast since it's used in node addressing.
   */
  protected DTM m_dtms[] = new DTM[256];
	
  /** Map from DTM identifier numbers to offsets. For small DTMs with a 
   * single identifier, this will always be 0. In overflow addressing, where
   * additional identifiers are allocated to access nodes beyond the range of
   * a single Node Handle, this table is used to map the handle's node field
   * into the actual node identifier.
   * 
   * This array grows as necessary; see addDTM().
   * 
   * This array grows as necessary; see addDTM(). Growth is uncommon... but
   * access needs to be blindingly fast since it's used in node addressing.
   * (And at the moment, that includes accessing it from DTMDefaultBase,
   * which is why this is not Protected or Private.)
   */
  int m_dtm_offsets[] = new int[256];

  /**
   * Add a DTM to the DTM table. This convenience call adds it as the 
   * "base DTM ID", with offset 0. The other version of addDTM should 
   * be used if you want to add "extended" DTM IDs with nonzero offsets.
   *
   * @param dtm Should be a valid reference to a DTM.
   * @param id Integer DTM ID to be bound to this DTM
   */
  synchronized public void addDTM(DTM dtm, int id) {	addDTM(dtm,id,0); }

	
  /**
   * Add a DTM to the DTM table.
   *
   * @param dtm Should be a valid reference to a DTM.
   * @param id Integer DTM ID to be bound to this DTM.
   * @param offset Integer addressing offset. The internal DTM Node ID is
   * obtained by adding this offset to the node-number field of the 
   * public DTM Handle. For the first DTM ID accessing each DTM, this is 0;
   * for overflow addressing it will be a multiple of 1<<IDENT_DTM_NODE_BITS.
   */
  synchronized public void addDTM(DTM dtm, int id, int offset)
  {
		if(id>=IDENT_MAX_DTMS)
		{
		}
		
		int oldlen=m_dtms.length;
		if(oldlen<=id)
		{
			int newlen=Math.min((id+256),IDENT_MAX_DTMS);

			DTM new_m_dtms[] = new DTM[newlen];
			System.arraycopy(m_dtms,0,new_m_dtms,0,oldlen);
			m_dtms=new_m_dtms;
			int new_m_dtm_offsets[] = new int[newlen];
			System.arraycopy(m_dtm_offsets,0,new_m_dtm_offsets,0,oldlen);
			m_dtm_offsets=new_m_dtm_offsets;
		}
		
    m_dtms[id] = dtm;
		m_dtm_offsets[id]=offset;
    dtm.documentRegistration();
  }

  /**
   * Get the first free DTM ID available. %OPT% Linear search is inefficient!
   */
  synchronized public int getFirstFreeDTMID()
  {
    int n = m_dtms.length;
    for (int i = 1; i < n; i++)
    {
      if(null == m_dtms[i])
      {
        return i;
      }
    }
  }

  /**
   * The default table for exandedNameID lookups.
   */
  private ExpandedNameTable m_expandedNameTable =
    new ExpandedNameTable();

  /**
   * Constructor DTMManagerDefault
   *
   */
  public DTMManagerDefault(){}


  /**
   * Get an instance of a DTM, loaded with the content from the
   * specified source.  If the unique flag is true, a new instance will
   * always be returned.  Otherwise it is up to the DTMManager to return a
   * new instance or an instance that it already created and may be being used
   * by someone else.
   * 
   * A bit of magic in this implementation: If the source is null, unique is true,
   * and incremental and doIndexing are both false, we return an instance of
   * SAX2RTFDTM, which see.
   * 
   * (I think more parameters will need to be added for error handling, and entity
   * resolution, and more explicit control of the RTF situation).
   *
   * @param source the specification of the source object.
   * @param unique true if the returned DTM must be unique, probably because it
   * is going to be mutated.
   * @param whiteSpaceFilter Enables filtering of whitespace nodes, and may
   *                         be null.
   * @param incremental true if the DTM should be built incrementally, if
   *                    possible.
   * @param doIndexing true if the caller considers it worth it to use
   *                   indexing schemes.
   *
   * @return a non-null DTM reference.
   */
  synchronized public DTM getDTM(Source source, boolean unique,
                                 DTMWSFilter whiteSpaceFilter,
                                 boolean incremental, boolean doIndexing)
  {

    if(DEBUG && null != source)
      System.out.println("Starting "+
                         (unique ? "UNIQUE" : "shared")+
                         " source: "+source.getSystemId()
                         );

    XMLStringFactory xstringFactory = m_xsf;
    int dtmPos = getFirstFreeDTMID();
    int documentID = dtmPos << IDENT_DTM_NODE_BITS;

    if ((null != source) && source instanceof DOMSource)
    {
      DOM2DTM dtm = new DOM2DTM(this, (DOMSource) source, documentID,
                                whiteSpaceFilter, xstringFactory, doIndexing);

      addDTM(dtm, dtmPos, 0);


      return dtm;
    }
    else
    {
      boolean isSAXSource = (null != source)
        ? (source instanceof SAXSource) : true;
      boolean isStreamSource = (null != source)
        ? (source instanceof StreamSource) : false;

      if (isSAXSource || isStreamSource)
      {
        XMLReader reader;
        InputSource xmlSource;

        if (null == source)
        {
          xmlSource = null;
          reader = null;
        }
        else
        {
          reader = getXMLReader(source);
          xmlSource = SAXSource.sourceToInputSource(source);

          String urlOfSource = xmlSource.getSystemId();

          if (null != urlOfSource)
          {
            try
            {
              urlOfSource = SystemIDResolver.getAbsoluteURI(urlOfSource);
            }
            catch (Exception e)
            {

              System.err.println("Can not absolutize URL: " + urlOfSource);
            }

            xmlSource.setSystemId(urlOfSource);
          }
        }

        SAX2DTM dtm;
        if(source==null && unique && !incremental && !doIndexing)
        {
          dtm = new SAX2RTFDTM(this, source, documentID, whiteSpaceFilter,
                               xstringFactory, doIndexing);
        }
        /**************************************************************
        else if(JKESS_XNI_EXPERIMENT && m_incremental)
        {        	
          dtm = new XNI2DTM(this, source, documentID, whiteSpaceFilter,
                            xstringFactory, doIndexing);
        }
        **************************************************************/
        {
          dtm = new SAX2DTM(this, source, documentID, whiteSpaceFilter,
                            xstringFactory, doIndexing);
        }

        addDTM(dtm, dtmPos, 0);


        boolean haveXercesParser =
          (null != reader)
          && (reader.getClass().getName().equals("org.apache.xerces.parsers.SAXParser") );
        
        if (haveXercesParser)
        
        if (this.m_incremental && incremental /* || ((null == reader) && incremental) */)
        {
          IncrementalSAXSource coParser=null;

          if (haveXercesParser)
          {
            try {
              coParser=org.apache.xml.dtm.ref.IncrementalSAXSource_Xerces.createIncrementalSAXSource();
            }  catch( Exception ex ) {
              ex.printStackTrace();
              coParser=null;
            }
          }

          if( coParser==null ) {
            if (null == reader)
              coParser = new IncrementalSAXSource_Filter();
            else
            {
              IncrementalSAXSource_Filter filter=new IncrementalSAXSource_Filter();
              filter.setXMLReader(reader);
              coParser=filter;
            }

          }

			
        /**************************************************************
          if(JKESS_XNI_EXPERIMENT && m_incremental & 
          	dtm instanceof XNI2DTM && 
          	coParser instanceof IncrementalSAXSource_Xerces)
          {          	
       		org.apache.xerces.xni.parser.XMLPullParserConfiguration xpc=
       			((IncrementalSAXSource_Xerces)coParser).getXNIParserConfiguration();
       		if(xpc!=null)	
          		((XNI2DTM)dtm).setIncrementalXNISource(xpc);
          	else
				dtm.setIncrementalSAXSource(coParser);
          } else
          ***************************************************************/
          
          dtm.setIncrementalSAXSource(coParser);

          if (null == xmlSource)
          {

            return dtm;
          }

          if(null == reader.getErrorHandler())
            reader.setErrorHandler(dtm);
          reader.setDTDHandler(dtm);

          try
          {

            coParser.startParse(xmlSource);
          }
          catch (RuntimeException re)
          {

            dtm.clearCoRoutine();

            throw re;
          }
          catch (Exception e)
          {

            dtm.clearCoRoutine();

            throw new org.apache.xml.utils.WrappedRuntimeException(e);
          }
        }
        else
        {
          if (null == reader)
          {

            return dtm;
          }

          reader.setContentHandler(dtm);
          reader.setDTDHandler(dtm);
          if(null == reader.getErrorHandler())
            reader.setErrorHandler(dtm);

          try
          {
            reader.setProperty(
          }
          catch (SAXNotRecognizedException e){}
          catch (SAXNotSupportedException e){}

          try
          {
            reader.parse(xmlSource);
          }
          catch (RuntimeException re)
          {

            dtm.clearCoRoutine();

            throw re;
          }
          catch (Exception e)
          {

            dtm.clearCoRoutine();

            throw new org.apache.xml.utils.WrappedRuntimeException(e);
          }
        }

        if (DUMPTREE)
        {
          System.out.println("Dumping SAX2DOM");
          dtm.dumpDTM(System.err);
        }

        return dtm;
      }
      else
      {

      }
    }
  }

  /**
   * Given a W3C DOM node, try and return a DTM handle.
   * Note: calling this may be non-optimal, and there is no guarantee that
   * the node will be found in any particular DTM.
   *
   * @param node Non-null reference to a DOM node.
   *
   * @return a valid DTM handle.
   */
  synchronized public int getDTMHandleFromNode(org.w3c.dom.Node node)
  {
    if(null == node)

    if (node instanceof org.apache.xml.dtm.ref.DTMNodeProxy)
      return ((org.apache.xml.dtm.ref.DTMNodeProxy) node).getDTMNodeNumber();
		
    else
    {
			int max = m_dtms.length;
      for(int i = 0; i < max; i++)
        {
          DTM thisDTM=m_dtms[i];
          if((null != thisDTM) && thisDTM instanceof DOM2DTM)
          {
            int handle=((DOM2DTM)thisDTM).getHandleOfNode(node);
            if(handle!=DTM.NULL) return handle;
          }
         }


      Node root = node;
      Node p = (root.getNodeType() == Node.ATTRIBUTE_NODE) ? ((org.w3c.dom.Attr)root).getOwnerElement() : root.getParentNode();
      for (; p != null; p = p.getParentNode())
      {
        root = p;
      }

      DOM2DTM dtm = (DOM2DTM) getDTM(new javax.xml.transform.dom.DOMSource(root),
																		 false, null, true, true);

      int handle;
      
      if(node instanceof org.apache.xml.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode)
      {
				handle=dtm.getHandleOfNode(((org.w3c.dom.Attr)node).getOwnerElement());
				handle=dtm.getAttributeNode(handle,node.getNamespaceURI(),node.getLocalName());
      }
      else
				handle = ((DOM2DTM)dtm).getHandleOfNode(node);

      if(DTM.NULL == handle)

      return handle;
    }
  }

  /**
   * This method returns the SAX2 parser to use with the InputSource
   * obtained from this URI.
   * It may return null if any SAX2-conformant XML parser can be used,
   * or if getInputSource() will also return null. The parser must
   * be free for use (i.e.
   * not currently in use for another parse().
   *
   * @param inputSource The value returned from the URIResolver.
   * @returns a SAX2 XMLReader to use to resolve the inputSource argument.
   *
   * @return non-null XMLReader reference ready to parse.
   */
  synchronized public XMLReader getXMLReader(Source inputSource)
  {

    try
    {
      XMLReader reader = (inputSource instanceof SAXSource)
                         ? ((SAXSource) inputSource).getXMLReader() : null;

      boolean isUserReader = (reader != null);

      if (null == reader)
      {
        try
        {
          javax.xml.parsers.SAXParserFactory factory =
            javax.xml.parsers.SAXParserFactory.newInstance();

          factory.setNamespaceAware(true);

          javax.xml.parsers.SAXParser jaxpParser = factory.newSAXParser();

          reader = jaxpParser.getXMLReader();
        }
        catch (javax.xml.parsers.ParserConfigurationException ex)
        {
          throw new org.xml.sax.SAXException(ex);
        }
        catch (javax.xml.parsers.FactoryConfigurationError ex1)
        {
          throw new org.xml.sax.SAXException(ex1.toString());
        }
        catch (NoSuchMethodError ex2){}
        catch (AbstractMethodError ame){}

        if (null == reader)
          reader = XMLReaderFactory.createXMLReader();
      }

      try
      {
                          true);
      }
      catch (org.xml.sax.SAXException se)
      {

      }


      return reader;
    }
    catch (org.xml.sax.SAXException se)
    {
      throw new DTMException(se.getMessage(), se);
    }
  }

  /**
   * Return the DTM object containing a representation of this node.
   *
   * @param nodeHandle DTM Handle indicating which node to retrieve
   *
   * @return a reference to the DTM object containing this node.
   */
  synchronized public DTM getDTM(int nodeHandle)
  {
    try
    {
      return m_dtms[nodeHandle >>> IDENT_DTM_NODE_BITS];
    }
    catch(java.lang.ArrayIndexOutOfBoundsException e)
    {
      if(nodeHandle==DTM.NULL)
      else
    }    
  }

  /**
   * Given a DTM, find the ID number in the DTM tables which addresses
   * the start of the document. If overflow addressing is in use, other
   * DTM IDs may also be assigned to this DTM.
   *
   * @param dtm The DTM which (hopefully) contains this node.
   *
   * @return The DTM ID (as the high bits of a NodeHandle, not as our
   * internal index), or -1 if the DTM doesn't belong to this manager.
   */
  synchronized public int getDTMIdentity(DTM dtm)
  {
	if(dtm instanceof DTMDefaultBase)
	{
		DTMDefaultBase dtmdb=(DTMDefaultBase)dtm;
		if(dtmdb.getManager()==this)
			return dtmdb.getDTMIDs().elementAt(0);
		else
			return -1;
	}
				
    int n = m_dtms.length;

    for (int i = 0; i < n; i++)
    {
      DTM tdtm = m_dtms[i];

      if (tdtm == dtm && m_dtm_offsets[i]==0)
        return i << IDENT_DTM_NODE_BITS;
    }

    return -1;
  }

  /**
   * Release the DTMManager's reference(s) to a DTM, making it unmanaged.
   * This is typically done as part of returning the DTM to the heap after
   * we're done with it.
   *
   * @param dtm the DTM to be released.
   * 
   * @param shouldHardDelete If false, this call is a suggestion rather than an
   * order, and we may not actually release the DTM. This is intended to 
   * support intelligent caching of documents... which is not implemented
   * in this version of the DTM manager.
   *
   * @return true if the DTM was released, false if shouldHardDelete was set
   * and we decided not to.
   */
  synchronized public boolean release(DTM dtm, boolean shouldHardDelete)
  {
    if(DEBUG)
    {
      System.out.println("Releasing "+
			 (shouldHardDelete ? "HARD" : "soft")+
			 " dtm="+
			 dtm.getDocumentBaseURI()
			 );
    }

    if (dtm instanceof SAX2DTM)
    {
      ((SAX2DTM) dtm).clearCoRoutine();
    }

		if(dtm instanceof DTMDefaultBase)
		{
			org.apache.xml.utils.SuballocatedIntVector ids=((DTMDefaultBase)dtm).getDTMIDs();
			for(int i=ids.size()-1;i>=0;--i)
				m_dtms[ids.elementAt(i)>>>DTMManager.IDENT_DTM_NODE_BITS]=null;
		}
		else
		{
			int i = getDTMIdentity(dtm);
		    if (i >= 0)
			{
				m_dtms[i >>> DTMManager.IDENT_DTM_NODE_BITS] = null;
			}
		}

    dtm.documentRelease();
    return true;
  }

  /**
   * Method createDocumentFragment
   *
   *
   * NEEDSDOC (createDocumentFragment) @return
   */
  synchronized public DTM createDocumentFragment()
  {

    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      dbf.setNamespaceAware(true);

      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.newDocument();
      Node df = doc.createDocumentFragment();

      return getDTM(new DOMSource(df), true, null, false, false);
    }
    catch (Exception e)
    {
      throw new DTMException(e);
    }
  }

  /**
   * NEEDSDOC Method createDTMIterator
   *
   *
   * NEEDSDOC @param whatToShow
   * NEEDSDOC @param filter
   * NEEDSDOC @param entityReferenceExpansion
   *
   * NEEDSDOC (createDTMIterator) @return
   */
  synchronized public DTMIterator createDTMIterator(int whatToShow, DTMFilter filter,
                                       boolean entityReferenceExpansion)
  {

    /** @todo: implement this org.apache.xml.dtm.DTMManager abstract method */
    return null;
  }

  /**
   * NEEDSDOC Method createDTMIterator
   *
   *
   * NEEDSDOC @param xpathString
   * NEEDSDOC @param presolver
   *
   * NEEDSDOC (createDTMIterator) @return
   */
  synchronized public DTMIterator createDTMIterator(String xpathString,
                                       PrefixResolver presolver)
  {

    /** @todo: implement this org.apache.xml.dtm.DTMManager abstract method */
    return null;
  }

  /**
   * NEEDSDOC Method createDTMIterator
   *
   *
   * NEEDSDOC @param node
   *
   * NEEDSDOC (createDTMIterator) @return
   */
  synchronized public DTMIterator createDTMIterator(int node)
  {

    /** @todo: implement this org.apache.xml.dtm.DTMManager abstract method */
    return null;
  }

  /**
   * NEEDSDOC Method createDTMIterator
   *
   *
   * NEEDSDOC @param xpathCompiler
   * NEEDSDOC @param pos
   *
   * NEEDSDOC (createDTMIterator) @return
   */
  synchronized public DTMIterator createDTMIterator(Object xpathCompiler, int pos)
  {

    /** @todo: implement this org.apache.xml.dtm.DTMManager abstract method */
    return null;
  }

  /**
   * return the expanded name table.
   *
   * NEEDSDOC @param dtm
   *
   * NEEDSDOC ($objectName$) @return
   */
  public ExpandedNameTable getExpandedNameTable(DTM dtm)
  {
    return m_expandedNameTable;
  }
}
