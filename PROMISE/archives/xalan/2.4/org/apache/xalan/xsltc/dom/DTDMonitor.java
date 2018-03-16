package org.apache.xalan.xsltc.dom;

import java.util.Enumeration;

import org.xml.sax.XMLReader;
import org.xml.sax.DTDHandler;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import org.apache.xalan.xsltc.*;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Hashtable;

final public class DTDMonitor implements DTDHandler, DeclHandler {

    private final static String EMPTYSTRING = "";

    private final static String ID_INDEX_NAME = "##id";
    private Hashtable _idAttributes = new Hashtable(); 
    private Hashtable _unparsedEntities = new Hashtable();

    private final static String DECL_HANDLER_PROP =

    private final static String NO_DTD_SUPPORT_STR =
	"Your SAX parser does not handle DTD declarations";

    /**
     * Constructor - does nothing
     */
    public DTDMonitor() { }

    /**
     * Constructor
     */
    public DTDMonitor(XMLReader reader) throws RuntimeException {
	handleDTD(reader);
    }

    /**
     * Set an instance of this class as the DTD declaration handler for
     * an XMLReader object (using the setProperty() method).
     */
    public void handleDTD(XMLReader reader) throws RuntimeException {
	try {
	    reader.setProperty(DECL_HANDLER_PROP, this);
	    reader.setDTDHandler(this);
	}
	catch (SAXNotRecognizedException e) {
	    throw(new RuntimeException(NO_DTD_SUPPORT_STR));
	}
	catch (SAXNotSupportedException e) {
	    throw(new RuntimeException(NO_DTD_SUPPORT_STR));
	}
    }

    /**
     * SAX2: Receive notification of a notation declaration event.
     */
    public void notationDecl(String name, String publicId, String systemId)
	throws SAXException { }

    /**
     * SAX2: Receive notification of an unparsed entity declaration event.
     * The only method here that does not have to do with ID attributes.
     * Passes names of unparsed entities to the translet.
     */
    public void unparsedEntityDecl(String name, String publicId,
				   String systemId, String notation)
	throws SAXException {
	if (_unparsedEntities.containsKey(name) == false) {
	    _unparsedEntities.put(name, systemId);
	}
    }

    public Hashtable getUnparsedEntityURIs() {
	return(_unparsedEntities);
    }

    /**
     * Stores the association between the name of an ID attribute and the
     * name of element that may contain it  Such an association would be
     * represented in a DTD as in:
     *              <!ATTLIST Person SSN ID #REQUIRED>
     * where 'Person' would be elemtName and 'SSN' would be the ID attribute
     */
    public void attributeDecl(String element, String attribute, String type,
			      String[] options, String defaultValue,
			      boolean fixed, boolean required) {
	_idAttributes.put(element, "@"+attribute);
    }

    /**
     * SAX2 extension handler for DTD declaration events
     * Report an attribute type declaration
     */
    public void attributeDecl(String element, String attribute, 
			      String type, String defaultValue, String value) {
	if (type.equals("ID") || (type.equals("IDREF")))
	    _idAttributes.put(element, "@"+attribute);
    }
    
    /**
     * SAX2 extension handler for DTD declaration events
     * Report an element type declaration.
     */
    public void elementDecl(String element, String model) { }
                 
    /**
     * SAX2 extension handler for DTD declaration events
     * Report a parsed external entity declaration.
     */
    public void externalEntityDecl(String name, String pid, String sid) { }
                 

    /**
     * SAX2 extension handler for DTD declaration events
     * Report an internal entity declaration.
     */
    public void internalEntityDecl(String name, String value) { }

    /**
     * Retrieves the name of the ID attribute associated with an element type
     */
    private final String getIdAttrName(String elemtName) {
        final String idAttrName = (String)_idAttributes.get(elemtName);
        return ((idAttrName == null) ? "" : idAttrName);
    }

    /**
     * Leverages the Key Class to implement the XSLT id() function.
     * buildIdIndex creates the index (##id) that Key Class uses. 
     * The index contains the node index (int) and the id value (String).
     */
    public final void buildIdIndex(DOMImpl dom, int mask,
				   AbstractTranslet translet) {

	int node, attr, type, typeCache;

	translet.setIndexSize(dom.getSize());

	if ((_idAttributes == null) || (_idAttributes.isEmpty())) return;

	Enumeration elements = _idAttributes.keys();
	if (elements.nextElement() instanceof String) {
	    Hashtable newAttributes = new Hashtable();
	    elements = _idAttributes.keys();
	    while (elements.hasMoreElements()) {
		String element = (String)elements.nextElement();
		String attribute = (String)_idAttributes.get(element);
		int elemType = dom.getGeneralizedType(element);
		int attrType = dom.getGeneralizedType(attribute);
		newAttributes.put(new Integer(elemType), new Integer(attrType));
	    }
	    _idAttributes = newAttributes;
	}

	final NodeIterator iter = dom.getAxisIterator(Axis.DESCENDANT);
	iter.setStartNode(DOM.ROOTNODE);

	Integer E = new Integer(typeCache = 0);
	Integer A = null;

	while ((node = iter.next()) != NodeIterator.END) {
	    type = dom.getType(node);
	    if (type != typeCache) {
		E = new Integer(typeCache = type);
		A = (Integer)_idAttributes.get(E);
	    }

	    if (A != null) {
		if ((attr = dom.getAttributeNode(A.intValue(), node)) != 0) {
		    final String value = dom.getNodeValue(attr);
		    translet.buildKeyIndex(ID_INDEX_NAME, mask|node, value);
		}
	    }
	}
    }

}

