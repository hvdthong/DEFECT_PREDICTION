package org.apache.xerces.validators.dtd;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.framework.XMLDTDScanner;
import org.apache.xerces.readers.XMLEntityHandler;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.common.Grammar;
import org.apache.xerces.validators.common.XMLAttributeDecl;
import org.apache.xerces.validators.common.XMLElementDecl;
import org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl;
import org.apache.xerces.validators.schema.XUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * A DTD grammar. This class is an EventHandler to receive callbacks from
 * the XMLDTDScanner. When the callbacks are received, the grammar structures
 * are directly populated from the callback information.
 * <p>
 * In addition to being a recipient of scanner callbacks, the DTD grammar
 * class can act as a pass-through filter for the DTD events. This is useful
 * for parsers that must expose the DTD information to the application. (e.g.
 * SAX2 DeclHandler callbacks.)
 *
 * @author Andy Clark
 * @version $Id: DTDGrammar.java 316513 2000-11-04 00:13:20Z ericye $
 */
public class DTDGrammar
    extends Grammar 
    implements XMLDTDScanner.EventHandler {



    /** Chunk shift. */

    /** Chunk size. */
    private static final int CHUNK_SIZE = (1 << CHUNK_SHIFT);

    /** Chunk mask. */
    private static final int CHUNK_MASK = CHUNK_SIZE - 1;

    /** Initial chunk count. */



    /** String pool. */
    private StringPool fStringPool;


    /** Element declaration. */
    private XMLElementDecl fElementDecl = new XMLElementDecl();

    /** Attribute declaration. */
    private XMLAttributeDecl fAttributeDecl = new XMLAttributeDecl();

    /** Content spec node. */
    private XMLContentSpec fContentSpec = new XMLContentSpec();


    /** Grammar document. */
    private Document fGrammarDocument;

    /** Root element. */
    private Element fRootElement;

    private QName fRootElementQName = new QName();

    /** Current element. */
    private Element fCurrentElement;


    /** flag if the elementDecl is External. */
    private int fElementDeclIsExternal[][] = new int[INITIAL_CHUNK_COUNT][];
    /** Mapping for element declarations. */
    private int fElementDeclMap[][] = new int[INITIAL_CHUNK_COUNT][];

    /** flag if the AttributeDecl is External. */
    private int fAttributeDeclIsExternal[][] = new int[INITIAL_CHUNK_COUNT][];
    /** Mapping for attribute declarations. */
    private int fAttributeDeclMap[][] = new int[INITIAL_CHUNK_COUNT][];

    /** Mapping for content spec nodes. */
    private int fContentSpecMap[][] = new int[INITIAL_CHUNK_COUNT][];


    private QName fQName = new QName();


    /** Default constructor. */
    public DTDGrammar(StringPool stringPool) {
        reset(stringPool);
    }


    /** Resets the DTD grammar. */
    public void reset(StringPool stringPool) {
        fStringPool = stringPool;
    }


    /** Start of DTD. */
    public void callStartDTD() throws Exception {
        
        setGrammarDocument(null);
        fGrammarDocument = new DocumentImpl();
        fRootElement = fGrammarDocument.createElement("dtd");
        fCurrentElement = fRootElement;


    /** End of DTD. */
    public void callEndDTD() throws Exception {

        setGrammarDocument(fGrammarDocument);


    /**
     * Signal the Text declaration of an external entity.
     *
     * @param version the handle in the string pool for the version number
     * @param encoding the handle in the string pool for the encoding
     * @exception java.lang.Exception
     */
    public void callTextDecl(int version, int encoding) throws Exception {

        Element textDecl = fGrammarDocument.createElement("textDecl");
        textDecl.setAttribute("version", fStringPool.toString(version));
        textDecl.setAttribute("encoding", fStringPool.toString(encoding));
        fCurrentElement.appendChild(textDecl);


    /**
     * Called when the doctype decl is scanned
     *
     * @param rootElementType handle of the rootElement
     * @param publicId StringPool handle of the public id
     * @param systemId StringPool handle of the system id
     * @exception java.lang.Exception
     */
    public void doctypeDecl(QName rootElement, int publicId, int systemId) 
        throws Exception {

        Element doctypeDecl = fGrammarDocument.createElement("doctypeDecl");
        doctypeDecl.setAttribute("name", fStringPool.toString(rootElement.rawname));
        if (rootElement.uri != -1) {
            doctypeDecl.setAttribute("xmlns:"+fStringPool.toString(rootElement.prefix),
                                     fStringPool.toString(rootElement.uri));
        }
        doctypeDecl.setAttribute("publicId", fStringPool.toString(publicId));
        doctypeDecl.setAttribute("systemId", fStringPool.toString(systemId));
        fCurrentElement.appendChild(doctypeDecl);

        fRootElementQName.setValues(rootElement);


    /**
     * Called when the DTDScanner starts reading from the external subset
     *
     * @param publicId StringPool handle of the public id
     * @param systemId StringPool handle of the system id
     * @exception java.lang.Exception
     */
    public void startReadingFromExternalSubset(int publicId, int systemId) 
        throws Exception {

        Element externalSubset = fGrammarDocument.createElement("external");
        externalSubset.setAttribute("publicId", fStringPool.toString(publicId));
        externalSubset.setAttribute("systemId", fStringPool.toString(systemId));
        fCurrentElement.appendChild(externalSubset);
        fCurrentElement = externalSubset;


    /**
     * Called when the DTDScanner stop reading from the external subset
     *
     * @exception java.lang.Exception
     */
    public void stopReadingFromExternalSubset() throws Exception {

        fCurrentElement = (Element)fCurrentElement.getParentNode();


    /**
     * Add an element declaration (forward reference)
     *
     * @param handle to the name of the element being declared
     * @return handle to the element whose declaration was added
     * @exception java.lang.Exception
     */
    public int addElementDecl(QName elementDecl) throws Exception {

        Element elementDeclElement = fGrammarDocument.createElement("elementDecl");
        elementDeclElement.setAttribute("name", fStringPool.toString(elementDecl.localpart));
        if (elementDecl.uri != -1) {
            elementDeclElement.setAttribute("xmlns:"+fStringPool.toString(elementDecl.prefix),
                                            fStringPool.toString(elementDecl.uri));
        }
        fCurrentElement.appendChild(elementDeclElement);

        int elementDeclIndex = createElementDecl();

        fElementDecl.clear();
        fElementDecl.name.setValues(elementDecl);
        setElementDecl(elementDeclIndex, fElementDecl);

        return elementDeclIndex;


    /**
     * Add an element declaration
     *
     * @param handle to the name of the element being declared
     * @param contentSpecType handle to the type name of the content spec
     * @param ContentSpec handle to the content spec node for the contentSpecType
     * @return handle to the element declaration that was added 
     * @exception java.lang.Exception
     */
    public int addElementDecl(QName elementDecl, 
                              int contentSpecType, 
                              int contentSpec,
                              boolean isExternal) throws Exception {

        Element elementDeclElement = fGrammarDocument.createElement("elementDecl");
        elementDeclElement.setAttribute("name", fStringPool.toString(elementDecl.localpart));
        if (elementDecl.uri != -1) {
            elementDeclElement.setAttribute("xmlns:"+fStringPool.toString(elementDecl.prefix),
                                            fStringPool.toString(elementDecl.uri));
        }
        elementDeclElement.setAttribute("type", fStringPool.toString(contentSpecType));
        fCurrentElement.appendChild(elementDeclElement);

        int elementDeclIndex = createElementDecl();

        fElementDecl.clear();
        fElementDecl.name.setValues(elementDecl);
        fElementDecl.type = contentSpecType;
        fElementDecl.contentSpecIndex = contentSpec;
        setElementDecl(elementDeclIndex, fElementDecl);
        
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        fElementDeclIsExternal[chunk][index] = isExternal? 1 : 0;

        return elementDeclIndex;


    protected void putElementNameMapping(QName name, int scope,
                                         int elementDeclIndex) {
        fQName.uri = -1;
        fQName.localpart = name.rawname;
        super.putElementNameMapping(fQName, scope, elementDeclIndex);
    }

    /***
    public int getElementDeclIndex(int localpartIndex, int scopeIndex) {
        return super.getElementDeclIndex(localpartIndex, scopeIndex);
    }

    public int getElementDeclIndex(int uriIndex, int localpartIndex, int scopeIndex) {
        return super.getElementDeclIndex(localpartIndex, -1);
    }
    /***/
    
    public int getElementDeclIndex(QName element, int scopeIndex) {
        return super.getElementDeclIndex(element.rawname, -1);
    }

    public void setElementDeclDTD(int elementDeclIndex, XMLElementDecl elementDecl) {
        super.setElementDecl(elementDeclIndex, elementDecl);
    }

    private XMLContentSpec fTempContentSpec = new XMLContentSpec();

    /***
    public void setContentSpecLeaf(int contentSpecIndex, QName elementName) {
        fTempContentSpec.setValues(XMLContentSpec.CONTENTSPECNODE_LEAF, elementName.rawname, -1);
        super.setContentSpec(contentSpecIndex, fTempContentSpec);
    }
    /***/

    public void setElementDeclIsExternal(int elementDeclIndex, boolean  isExternal) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        fElementDeclIsExternal[chunk][index] = isExternal? 1 : 0;
    }

    public boolean getElementDeclIsExternal(int elementDeclIndex) {
        if (elementDeclIndex < 0) {
            return false;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return (fElementDeclIsExternal[chunk][index] != 0);
    }

    public boolean getAttributeDeclIsExternal(int attributeDeclIndex) {
        if (attributeDeclIndex < 0) {
            return false;
        }
        int chunk = attributeDeclIndex >> CHUNK_SHIFT;
        int index = attributeDeclIndex & CHUNK_MASK;
        return (fAttributeDeclIsExternal[chunk][index] != 0);
    }

    public boolean getRootElementQName(QName root) {
        if (fRootElementQName.rawname == -1) {
            return false;
        }
        root.setValues(fRootElementQName);
        return true;
    }

    /**
     * Add an attribute definition
     *
     * @param handle to the element whose attribute is being declared
     * @param attName StringPool handle to the attribute name being declared
     * @param attType type of the attribute
     * @param enumeration StringPool handle of the attribute's enumeration list (if any)
     * @param attDefaultType an integer value denoting the DefaultDecl value
     * @param attDefaultValue StringPool handle of this attribute's default value
     * @return handle to the attribute definition
     * @exception java.lang.Exception
     */
    public int addAttDef(QName elementDecl, QName attributeDecl, 
                         int attType, boolean attList, int enumeration, 
                         int attDefaultType, int attDefaultValue, boolean isExternal) 
        throws Exception {
        /****
        System.out.println("---add attr--- "+attributeDecl.localpart
                  +","+attType        
                  +","+attDefaultType        
                  +","+isExternal);
         /****/

        Element attributeDeclElement = fGrammarDocument.createElement("attributeDecl");
        attributeDeclElement.setAttribute("element", fStringPool.toString(elementDecl.localpart));
        attributeDeclElement.setAttribute("name", fStringPool.toString(attributeDecl.localpart));
        if (attributeDecl.uri != -1) {
            attributeDeclElement.setAttribute("xmlns:"+fStringPool.toString(attributeDecl.prefix),
                                              fStringPool.toString(attributeDecl.uri));
        }
        attributeDeclElement.setAttribute("type", fStringPool.toString(attType));
        attributeDeclElement.setAttribute("defaultType", fStringPool.toString(attDefaultType));
        attributeDeclElement.setAttribute("defaultValue", fStringPool.toString(attDefaultValue));
        fCurrentElement.appendChild(attributeDeclElement);

        int attributeDeclIndex = createAttributeDecl();

        String attTypeString = "";
        switch (attType) {
        case XMLAttributeDecl.TYPE_CDATA:
            attTypeString = "string";
        case XMLAttributeDecl.TYPE_ENTITY:
            attTypeString = "ENTITY";;
        case XMLAttributeDecl.TYPE_ENUMERATION:
            attTypeString = "ENUMERATION";;
        case XMLAttributeDecl.TYPE_ID:
            attTypeString = "ID";;
        case XMLAttributeDecl.TYPE_IDREF:
            attTypeString = "IDREF";;
        case XMLAttributeDecl.TYPE_NMTOKEN:
            attTypeString = "NMTOKEN";;
        case XMLAttributeDecl.TYPE_NOTATION:
            attTypeString = "NOTATION";;
        default:
            ;
        }

        fAttributeDecl.clear();
        fAttributeDecl.name.setValues(attributeDecl);
        fAttributeDecl.type = attType;
        fAttributeDecl.list = attList;
        fAttributeDecl.enumeration = enumeration;
        /***
        fAttributeDecl.datatypeValidator = 
            DatatypeValidatorFactoryImpl.getDatatypeRegistry().getDatatypeValidator(attTypeString);
        ****/
        fAttributeDecl.defaultType = attDefaultType;
        fAttributeDecl.defaultValue = fStringPool.toString(attDefaultValue);

        int elementDeclIndex = getElementDeclIndex(elementDecl, -1);
        setAttributeDecl(elementDeclIndex, attributeDeclIndex, fAttributeDecl);

        int chunk = attributeDeclIndex >> CHUNK_SHIFT;
        int index = attributeDeclIndex & CHUNK_MASK;
        ensureAttributeDeclCapacity(chunk);
        fAttributeDeclIsExternal[chunk][index] = isExternal ?  1 : 0;

        return attributeDeclIndex;


    /**
     * create an XMLContentSpec for a leaf
     *
     * @param nameIndex StringPool handle to the name (Element) for the node
     * @return handle to the newly create XMLContentSpec
     * @exception java.lang.Exception
     */
    public int addUniqueLeafNode(int nameIndex) throws Exception {

        int contentSpecIndex = createContentSpec();

        fContentSpec.setValues(XMLContentSpec.CONTENTSPECNODE_LEAF, 
                               nameIndex, -1);
        setContentSpec(contentSpecIndex, fContentSpec);

        return contentSpecIndex;


    /**
     * Create an XMLContentSpec for a single non-leaf
     * 
     * @param nodeType the type of XMLContentSpec to create - from XMLContentSpec.CONTENTSPECNODE_*
     * @param nodeValue handle to an XMLContentSpec
     * @return handle to the newly create XMLContentSpec
     * @exception java.lang.Exception
     */
    public int addContentSpecNode(int nodeType, 
                                  int nodeValue) throws Exception {

        int contentSpecIndex = createContentSpec();

        fContentSpec.setValues(nodeType, nodeValue, -1);
        setContentSpec(contentSpecIndex, fContentSpec);

        return contentSpecIndex;


    /**
     * Create an XMLContentSpec for a two child leaf
     *
     * @param nodeType the type of XMLContentSpec to create - from XMLContentSpec.CONTENTSPECNODE_*
     * @param leftNodeIndex handle to an XMLContentSpec
     * @param rightNodeIndex handle to an XMLContentSpec
     * @return handle to the newly create XMLContentSpec
     * @exception java.lang.Exception
     */
    public int addContentSpecNode(int nodeType, 
                                  int leftNodeIndex, 
                                  int rightNodeIndex) throws Exception {

        int contentSpecIndex = createContentSpec();

        fContentSpec.setValues(nodeType, 
                               leftNodeIndex, rightNodeIndex);
        setContentSpec(contentSpecIndex, fContentSpec);

        return contentSpecIndex;


    /**
     * Create a string representation of an XMLContentSpec tree
     * 
     * @param handle to an XMLContentSpec
     * @return String representation of the content spec tree
     * @exception java.lang.Exception
     */
    public String getContentSpecNodeAsString(int nodeIndex) throws Exception {
        return XMLContentSpec.toString(this, fStringPool, nodeIndex);
    }

    /**
     * Start the scope of an entity declaration.
     *
     * @return <code>true</code> on success; otherwise
     *         <code>false</code> if the entity declaration is recursive.
     * @exception java.lang.Exception
     */
    public boolean startEntityDecl(boolean isPE, int entityName) 
        throws Exception {

        Element entityDecl = fGrammarDocument.createElement("entityDecl");
        entityDecl.setAttribute("name", fStringPool.toString(entityName));
        entityDecl.setAttribute("parameter", isPE ? "true" : "false");
        fCurrentElement.appendChild(entityDecl);
        fCurrentElement = entityDecl;

        return true;


    /**
     * End the scope of an entity declaration.
     * @exception java.lang.Exception
     */
    public void endEntityDecl() throws Exception {

        fCurrentElement = (Element)fCurrentElement.getParentNode();


    /**
     * Add a declaration for an internal parameter entity
     *
     * @param name StringPool handle of the parameter entity name
     * @param value StringPool handle of the parameter entity value
     * @return handle to the parameter entity declaration
     * @exception java.lang.Exception
     */
    public int addInternalPEDecl(int name, int value) throws Exception {

        Element internalPEDecl = fGrammarDocument.createElement("internalPEDecl");
        internalPEDecl.setAttribute("name", fStringPool.toString(name));
        internalPEDecl.setAttribute("value", fStringPool.toString(value));
        fCurrentElement.appendChild(internalPEDecl);

        int peDeclIndex = -1;

        return peDeclIndex;


    /**
     * Add a declaration for an external parameter entity
     *
     * @param name StringPool handle of the parameter entity name
     * @param publicId StringPool handle of the publicId
     * @param systemId StringPool handle of the systemId
     * @return handle to the parameter entity declaration
     * @exception java.lang.Exception
     */
    public int addExternalPEDecl(int name, 
                                 int publicId, 
                                 int systemId) throws Exception {
        
        Element externalPEDecl = fGrammarDocument.createElement("externalPEDecl");
        externalPEDecl.setAttribute("name", fStringPool.toString(name));
        externalPEDecl.setAttribute("publicId", fStringPool.toString(publicId));
        externalPEDecl.setAttribute("systemId", fStringPool.toString(systemId));
        fCurrentElement.appendChild(externalPEDecl);

        int peDeclIndex = -1;

        return peDeclIndex;


    /**
     * Add a declaration for an internal entity
     *
     * @param name StringPool handle of the entity name
     * @param value StringPool handle of the entity value
     * @return handle to the entity declaration
     * @exception java.lang.Exception
     */
    public int addInternalEntityDecl(int name, int value) throws Exception {

        Element internalEntityDecl = fGrammarDocument.createElement("internalEntityDecl");
        internalEntityDecl.setAttribute("name", fStringPool.toString(name));
        internalEntityDecl.setAttribute("value", fStringPool.toString(value));
        fCurrentElement.appendChild(internalEntityDecl);
            
        int internalEntityDeclIndex = -1;

        return internalEntityDeclIndex;


    /**
     * Add a declaration for an entity
     *
     * @param name StringPool handle of the entity name
     * @param publicId StringPool handle of the publicId
     * @param systemId StringPool handle of the systemId
     * @return handle to the entity declaration
     * @exception java.lang.Exception
     */
    public int addExternalEntityDecl(int name, 
                                     int publicId, 
                                     int systemId) throws Exception {

        Element externalEntityDecl = fGrammarDocument.createElement("externalEntityDecl");
        externalEntityDecl.setAttribute("name", fStringPool.toString(name));
        externalEntityDecl.setAttribute("publicId", fStringPool.toString(publicId));
        externalEntityDecl.setAttribute("systemId", fStringPool.toString(systemId));
        fCurrentElement.appendChild(externalEntityDecl);
            
        int externalEntityDeclIndex = -1;

        return externalEntityDeclIndex;


    /**
     * Add a declaration for an unparsed entity
     *
     * @param name StringPool handle of the entity name
     * @param publicId StringPool handle of the publicId
     * @param systemId StringPool handle of the systemId
     * @param notationName StringPool handle of the notationName
     * @return handle to the entity declaration
     * @exception java.lang.Exception
     */
    public int addUnparsedEntityDecl(int name, 
                                     int publicId, int systemId, 
                                     int notationName) throws Exception {

        Element unparsedEntityDecl = fGrammarDocument.createElement("unparsedEntityDecl");
        unparsedEntityDecl.setAttribute("name", fStringPool.toString(name));
        unparsedEntityDecl.setAttribute("publicId", fStringPool.toString(publicId));
        unparsedEntityDecl.setAttribute("systemId", fStringPool.toString(systemId));
        unparsedEntityDecl.setAttribute("notation", fStringPool.toString(notationName));
        fCurrentElement.appendChild(unparsedEntityDecl);
            
        int unparsedEntityDeclIndex = -1;

        return unparsedEntityDeclIndex;


    /**
     * Called when the scanner start scanning an enumeration
     * @return StringPool handle to a string list that will hold the enumeration names
     * @exception java.lang.Exception
     */
    public int startEnumeration() throws Exception {
            
        Element enumeration = fGrammarDocument.createElement("enumeration");
        fCurrentElement.appendChild(enumeration);
        fCurrentElement = enumeration;

        int enumIndex = fStringPool.startStringList();

        return enumIndex;


    /**
     * Add a name to an enumeration
     * @param enumIndex StringPool handle to the string list for the enumeration
     * @param elementType handle to the element that owns the attribute with the enumeration
     * @param attrName StringPool handle to the name of the attribut with the enumeration
     * @param nameIndex StringPool handle to the name to be added to the enumeration
     * @param isNotationType true if the enumeration is an enumeration of NOTATION names
     * @exception java.lang.Exception
     */
    public void addNameToEnumeration(int enumIndex, 
                                     int elementType, int attrName, 
                                     int nameIndex, 
                                     boolean isNotationType) throws Exception {
        
        Element literal = fGrammarDocument.createElement("literal");
        literal.setAttribute("element", fStringPool.toString(elementType));
        literal.setAttribute("attribute", fStringPool.toString(attrName));
        literal.setAttribute("name", fStringPool.toString(nameIndex));
        literal.setAttribute("notation", isNotationType ? "true" : "false");
        fCurrentElement.appendChild(literal);

        fStringPool.addStringToList(enumIndex, nameIndex);


    /**
     * Finish processing an enumeration
     *
     * @param enumIndex handle to the string list which holds the enumeration to be finshed.
     * @exception java.lang.Exception
     */
    public void endEnumeration(int enumIndex) throws Exception {

        fCurrentElement = (Element)fCurrentElement.getParentNode();
        
        fStringPool.finishStringList(enumIndex);


    /**
     * Add a declaration for a notation
     *
     * @param notationName
     * @param publicId
     * @param systemId
     * @return handle to the notation declaration
     * @exception java.lang.Exception
     */
    public int addNotationDecl(int notationName, 
                               int publicId, int systemId) throws Exception {

        Element notationDecl = fGrammarDocument.createElement("notationDecl");
        notationDecl.setAttribute("name", fStringPool.toString(notationName));
        notationDecl.setAttribute("publicId", fStringPool.toString(publicId));
        notationDecl.setAttribute("systemId", fStringPool.toString(systemId));
        fCurrentElement.appendChild(notationDecl);

        int notationDeclIndex = -1;

        return notationDeclIndex;


    /**
     * Called when a comment has been scanned
     *
     * @param data StringPool handle of the comment text
     * @exception java.lang.Exception
     */
    public void callComment(int data) throws Exception {
    }

    /**
     * Called when a processing instruction has been scanned
     * @param piTarget StringPool handle of the PI target
     * @param piData StringPool handle of the PI data
     * @exception java.lang.Exception
     */
    public void callProcessingInstruction(int piTarget, int piData) 
        throws Exception {

        ProcessingInstruction pi = 
            fGrammarDocument.createProcessingInstruction(fStringPool.toString(piTarget),
                                                         fStringPool.toString(piData));
        fCurrentElement.appendChild(pi);



    /**
     * Supports DOM Level 2 internalSubset additions.
     * Called when the internal subset is completely scanned.
     */
    public void internalSubset(int internalSubset) throws Exception {
    }

    protected boolean isDTD() {
        return true;
    }



    /** Ensures storage for element declaration mappings. */
    private boolean ensureElementDeclCapacity(int chunk) {
        try {
            return fElementDeclMap[chunk][0] == 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            fElementDeclMap = resize(fElementDeclMap, 
                                     fElementDeclMap.length * 2);
            fElementDeclIsExternal = resize(fElementDeclIsExternal, 
                                     fElementDeclIsExternal.length * 2);
        } catch (NullPointerException ex) {
        }
        fElementDeclMap[chunk] = new int[CHUNK_SIZE];
        fElementDeclIsExternal[chunk] = new int[CHUNK_SIZE];
        return true;
    }

    /** Ensures storage for attribute declaration mappings. */
    private boolean ensureAttributeDeclCapacity(int chunk) {
        try {
            return fAttributeDeclMap[chunk][0] == 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            fAttributeDeclMap = resize(fAttributeDeclMap, 
                                       fAttributeDeclMap.length * 2);
            fAttributeDeclIsExternal = resize(fAttributeDeclIsExternal, 
                                       fAttributeDeclIsExternal.length * 2);
        } catch (NullPointerException ex) {
        }
        fAttributeDeclMap[chunk] = new int[CHUNK_SIZE];
        fAttributeDeclIsExternal[chunk] = new int[CHUNK_SIZE];
        return true;
    }

    /** Ensures storage for content spec mappings. */
    private boolean ensureContentSpecCapacity(int chunk) {
        try {
            return fContentSpecMap[chunk][0] == 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            fContentSpecMap = resize(fContentSpecMap, 
                                     fContentSpecMap.length * 2);
        } catch (NullPointerException ex) {
        }
        fContentSpecMap[chunk] = new int[CHUNK_SIZE];
        return true;
    }


    /** Resizes chunked integer arrays. */
    private int[][] resize(int array[][], int newsize) {
        int newarray[][] = new int[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

