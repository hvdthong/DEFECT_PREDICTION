package org.apache.xerces.validators.common;

import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.utils.Hash2intTable;
import org.apache.xerces.utils.QName;
import org.apache.xerces.validators.datatype.DatatypeValidator;
import org.apache.xerces.validators.common.XMLContentModel;
import org.apache.xerces.validators.common.CMException;
import org.apache.xerces.validators.schema.identity.IdentityConstraint;
import org.apache.xerces.utils.ImplementationMessages;
import org.w3c.dom.Document;
import java.util.Vector;


/**
 * @version $Id: Grammar.java 316789 2001-01-18 07:10:55Z andyc $
 */
public class Grammar
implements XMLContentSpec.Provider {


    private static final int CHUNK_SIZE = (1 << CHUNK_SHIFT);
    private static final int CHUNK_MASK = CHUNK_SIZE - 1;

    private static final int LIST_FLAG = 0x8000;
    private static final int LIST_MASK = ~LIST_FLAG;



    private int fTargetNamespace;

    private Document fGrammarDocument;


    private int fElementDeclCount = 0;
    private QName fElementDeclName[][] = new QName[INITIAL_CHUNK_COUNT][];
    private int fElementDeclType[][] = new int[INITIAL_CHUNK_COUNT][];
    private DatatypeValidator fElementDeclDatatypeValidator[][] = new DatatypeValidator[INITIAL_CHUNK_COUNT][];
    private int fElementDeclContentSpecIndex[][] = new int[INITIAL_CHUNK_COUNT][];
    private XMLContentModel fElementDeclContentModelValidator[][] = new XMLContentModel[INITIAL_CHUNK_COUNT][];
    private int fElementDeclFirstAttributeDeclIndex[][] = new int[INITIAL_CHUNK_COUNT][];
    private int fElementDeclLastAttributeDeclIndex[][] = new int[INITIAL_CHUNK_COUNT][];
    private Vector fElementDeclUnique[][] = new Vector[INITIAL_CHUNK_COUNT][];
    private Vector fElementDeclKey[][] = new Vector[INITIAL_CHUNK_COUNT][];
    private Vector fElementDeclKeyRef[][] = new Vector[INITIAL_CHUNK_COUNT][];


    private int fContentSpecCount = 0 ;
    private int fContentSpecType[][] = new int[INITIAL_CHUNK_COUNT][];
    private int fContentSpecValue[][] = new int[INITIAL_CHUNK_COUNT][];
    private int fContentSpecOtherValue[][] = new int[INITIAL_CHUNK_COUNT][];


    private int fAttributeDeclCount = 0 ;
    private QName fAttributeDeclName[][] = new QName[INITIAL_CHUNK_COUNT][];
    private int   fAttributeDeclType[][] = new int[INITIAL_CHUNK_COUNT][];
    private int   fAttributeDeclEnumeration[][] = new int[INITIAL_CHUNK_COUNT][];
    private int   fAttributeDeclDefaultType[][] = new int[INITIAL_CHUNK_COUNT][];
    private DatatypeValidator fAttributeDeclDatatypeValidator[][] = new DatatypeValidator[INITIAL_CHUNK_COUNT][];
    private String fAttributeDeclDefaultValue[][] = new String[INITIAL_CHUNK_COUNT][];
    private int fAttributeDeclNextAttributeDeclIndex[][] = new int[INITIAL_CHUNK_COUNT][];


    private Hash2intTable fElementNameAndScopeToElementDeclIndexMapping = new Hash2intTable();


    private QName fQName1 = new QName();
    private QName fQName2 = new QName();


    public Document getGrammarDocument() {
        return fGrammarDocument;
    }

    public int getElementDeclIndex(int localpartIndex, int scopeIndex) {
        if ( localpartIndex > -1 && scopeIndex >-2 ) {
            return fElementNameAndScopeToElementDeclIndexMapping.get(-1, localpartIndex, scopeIndex);
        }
        return -1;
    }
    
    public int getElementDeclIndex(int uriIndex, int localpartIndex, int scopeIndex) {
        if ( localpartIndex > -1 && scopeIndex >-2 ) {
            return fElementNameAndScopeToElementDeclIndexMapping.get(uriIndex, localpartIndex, scopeIndex);
        }
        return -1;
    }
    
    public int getElementDeclIndex(QName element, int scopeIndex) {
        if ( element.localpart > -1 && scopeIndex >-2 ) {
            return fElementNameAndScopeToElementDeclIndexMapping.get(element.uri, element.localpart, scopeIndex);
        }
        return -1;
    }

    public boolean getElementDecl(int elementDeclIndex, XMLElementDecl elementDecl) {
        if (elementDeclIndex < 0 || elementDeclIndex >= fElementDeclCount) {
            return false;
        }

        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex &  CHUNK_MASK;

        elementDecl.name.setValues(fElementDeclName[chunk][index]);
        if (fElementDeclType[chunk][index] == -1) {
            elementDecl.type                    = -1;
            elementDecl.list = false;
        }
        else {
            elementDecl.type                    = fElementDeclType[chunk][index] & LIST_MASK;
            elementDecl.list = (fElementDeclType[chunk][index] & LIST_FLAG) != 0;
        }
        elementDecl.datatypeValidator       = fElementDeclDatatypeValidator[chunk][index];       
        elementDecl.contentSpecIndex        = fElementDeclContentSpecIndex[chunk][index];        

        elementDecl.unique.removeAllElements();
        int ucount = fElementDeclUnique[chunk][index] != null
                   ? fElementDeclUnique[chunk][index].size() : 0;
        for (int i = 0; i < ucount; i++) {
            elementDecl.unique.addElement(fElementDeclUnique[chunk][index].elementAt(i));
        }
        elementDecl.key.removeAllElements();
        int kcount = fElementDeclKey[chunk][index] != null
                   ? fElementDeclKey[chunk][index].size() : 0;
        for (int i = 0; i < kcount; i++) {
            elementDecl.key.addElement(fElementDeclKey[chunk][index].elementAt(i));
        }
        elementDecl.keyRef.removeAllElements();
        int krcount = fElementDeclKeyRef[chunk][index] != null
                    ? fElementDeclKeyRef[chunk][index].size() : 0;
        for (int i = 0; i < krcount; i++) {
            elementDecl.keyRef.addElement(fElementDeclKeyRef[chunk][index].elementAt(i));
        }

        return true;
    }

    public int getFirstAttributeDeclIndex(int elementDeclIndex) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex &  CHUNK_MASK;

        return  fElementDeclFirstAttributeDeclIndex[chunk][index];
    }

    public int getNextAttributeDeclIndex(int attributeDeclIndex) {
        int chunk = attributeDeclIndex >> CHUNK_SHIFT;
        int index = attributeDeclIndex &  CHUNK_MASK;

        return fAttributeDeclNextAttributeDeclIndex[chunk][index];
    }

    public boolean getContentSpec(int contentSpecIndex, XMLContentSpec contentSpec) {
        if (contentSpecIndex < 0 || contentSpecIndex >= fContentSpecCount )
            return false;

        int chunk = contentSpecIndex >> CHUNK_SHIFT;
        int index = contentSpecIndex & CHUNK_MASK;

        contentSpec.type       = fContentSpecType[chunk][index];
        contentSpec.value      = fContentSpecValue[chunk][index];
        contentSpec.otherValue = fContentSpecOtherValue[chunk][index];
        return true;
    }

    public XMLContentModel getElementContentModel(int elementDeclIndex) throws CMException {

        if (elementDeclIndex < 0 || elementDeclIndex >= fElementDeclCount)
            return null;

        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;

        XMLContentModel contentModel    =  fElementDeclContentModelValidator[chunk][index];

        if (contentModel != null)
            return contentModel;

        int contentType = fElementDeclType[chunk][index];
        if (contentType == XMLElementDecl.TYPE_SIMPLE) {
            return null;
        }


        int contentSpecIndex = fElementDeclContentSpecIndex[chunk][index]; 

        /***
        if ( contentSpecIndex == -1 )
            return null;
        /***/

        XMLContentSpec  contentSpec = new XMLContentSpec();
        getContentSpec( contentSpecIndex, contentSpec );
        
        
        if ( contentType == XMLElementDecl.TYPE_MIXED ) {

            Vector vQName = new Vector(); 
            try {
                ChildrenList children = new ChildrenList();
                contentSpecTree(contentSpecIndex, contentSpec, children);
                contentModel = new MixedContentModel(children.qname,
                                                     children.type,
                                                     0, children.length, false, isDTD());
            }catch(  CMException ex ){
                ex.printStackTrace();
            }

        } else if (contentType == XMLElementDecl.TYPE_CHILDREN) {
            try {
            contentModel = createChildModel(contentSpecIndex);
            }catch( CMException ex ) {
                 ex.printStackTrace();
            }
        } else {
            throw new CMException(ImplementationMessages.VAL_CST);
        }


        fElementDeclContentModelValidator[chunk][index] = contentModel;

        return contentModel;
    }



    public boolean getAttributeDecl(int attributeDeclIndex, XMLAttributeDecl attributeDecl) {
        if (attributeDeclIndex < 0 || attributeDeclIndex >= fAttributeDeclCount) {
            return false;
        }
        int chunk = attributeDeclIndex >> CHUNK_SHIFT;
        int index = attributeDeclIndex & CHUNK_MASK;

        attributeDecl.name.setValues(fAttributeDeclName[chunk][index]);

        if (fAttributeDeclType[chunk][index] == -1) {
            
            attributeDecl.type = -1;
            attributeDecl.list = false;
        }
        else {
            attributeDecl.type = fAttributeDeclType[chunk][index] & LIST_MASK;
            attributeDecl.list = (fAttributeDeclType[chunk][index] & LIST_FLAG) != 0;
        }
        attributeDecl.datatypeValidator = fAttributeDeclDatatypeValidator[chunk][index];
        attributeDecl.enumeration = fAttributeDeclEnumeration[chunk][index];
        attributeDecl.defaultType = fAttributeDeclDefaultType[chunk][index];
        attributeDecl.defaultValue = fAttributeDeclDefaultValue[chunk][index];
        return true;
    }


    protected void setGrammarDocument(Document grammarDocument) {
        fGrammarDocument = grammarDocument;
    }

    protected int createElementDecl() {

        int chunk = fElementDeclCount >> CHUNK_SHIFT;
        int index = fElementDeclCount & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        fElementDeclName[chunk][index]               = new QName(); 
        fElementDeclType[chunk][index]                    = -1;  
        fElementDeclDatatypeValidator[chunk][index]       = null;
        fElementDeclContentSpecIndex[chunk][index] = -1;
        fElementDeclContentModelValidator[chunk][index] = null;
        fElementDeclFirstAttributeDeclIndex[chunk][index] = -1;
        fElementDeclLastAttributeDeclIndex[chunk][index]  = -1;
        return fElementDeclCount++;
    }

    protected void setElementDecl(int elementDeclIndex, XMLElementDecl elementDecl) {

        if (elementDeclIndex < 0 || elementDeclIndex >= fElementDeclCount) {
            return;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex &  CHUNK_MASK;

        fElementDeclName[chunk][index].setValues(elementDecl.name);
        fElementDeclType[chunk][index]                    = elementDecl.type;
        if (elementDecl.list) {
            fElementDeclType[chunk][index] |= LIST_FLAG;
        }
        fElementDeclDatatypeValidator[chunk][index]       = elementDecl.datatypeValidator;
        fElementDeclContentSpecIndex[chunk][index]        = elementDecl.contentSpecIndex;

        int ucount = elementDecl.unique.size();
        if (ucount > 0) {
            if (fElementDeclUnique[chunk][index] == null) {
                fElementDeclUnique[chunk][index] = (Vector)elementDecl.unique.clone();
            }
            else {
                fElementDeclUnique[chunk][index].removeAllElements();
                for (int i = 0; i < ucount; i++) {
                    fElementDeclUnique[chunk][index].addElement(elementDecl.unique.elementAt(i));
                }
            }
        }
        int kcount = elementDecl.key.size();
        if (kcount > 0) {
            if (fElementDeclKey[chunk][index] == null) {
                fElementDeclKey[chunk][index] = (Vector)elementDecl.key.clone();
            }
            else {
                fElementDeclKey[chunk][index].removeAllElements();
                for (int i = 0; i < kcount; i++) {
                    fElementDeclKey[chunk][index].addElement(elementDecl.key.elementAt(i));
                }
            }
        }
        int krcount = elementDecl.keyRef.size();
        if (krcount > 0) {
            if (fElementDeclKeyRef[chunk][index] == null) {
                fElementDeclKeyRef[chunk][index] = (Vector)elementDecl.keyRef.clone();
            }
            else {
                fElementDeclKeyRef[chunk][index].removeAllElements();
                for (int i = 0; i < krcount; i++) {
                    fElementDeclKeyRef[chunk][index].addElement(elementDecl.keyRef.elementAt(i));
                }
            }
        }

        putElementNameMapping(elementDecl.name,
                              elementDecl.enclosingScope, 
                              elementDeclIndex);
    }

    protected void putElementNameMapping(QName name, int scope,
                                         int elementDeclIndex) {
        fElementNameAndScopeToElementDeclIndexMapping.put(name.uri, 
                                                          name.localpart, 
                                                          scope,
                                                          elementDeclIndex);
    }

    protected void setFirstAttributeDeclIndex(int elementDeclIndex, int newFirstAttrIndex){
        
        if (elementDeclIndex < 0 || elementDeclIndex >= fElementDeclCount) {
            return;
        }
    
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex &  CHUNK_MASK;

        fElementDeclFirstAttributeDeclIndex[chunk][index] = newFirstAttrIndex;
    }


    protected int createContentSpec() {
        int chunk = fContentSpecCount >> CHUNK_SHIFT;
        int index = fContentSpecCount & CHUNK_MASK;

        ensureContentSpecCapacity(chunk);
        fContentSpecType[chunk][index]       = -1;
        fContentSpecValue[chunk][index]      = -1;
        fContentSpecOtherValue[chunk][index] = -1;

        return fContentSpecCount++;
    }

    protected void setContentSpec(int contentSpecIndex, XMLContentSpec contentSpec) {
        int   chunk = contentSpecIndex >> CHUNK_SHIFT;
        int   index = contentSpecIndex & CHUNK_MASK;

        fContentSpecType[chunk][index]       = contentSpec.type;
        fContentSpecValue[chunk][index]      = contentSpec.value;
        fContentSpecOtherValue[chunk][index] = contentSpec.otherValue;
    }

    protected int createAttributeDecl() {
        int chunk = fAttributeDeclCount >> CHUNK_SHIFT;
        int index = fAttributeDeclCount & CHUNK_MASK;

        ensureAttributeDeclCapacity(chunk);
        fAttributeDeclName[chunk][index]                    = new QName();
        fAttributeDeclType[chunk][index]                    = -1;
        fAttributeDeclDatatypeValidator[chunk][index]       = null;
        fAttributeDeclEnumeration[chunk][index] = -1;
        fAttributeDeclDefaultType[chunk][index] = XMLAttributeDecl.DEFAULT_TYPE_IMPLIED;
        fAttributeDeclDefaultValue[chunk][index]            = null;
        fAttributeDeclNextAttributeDeclIndex[chunk][index]  = -1;
        return fAttributeDeclCount++;
    }


    protected void setAttributeDecl(int elementDeclIndex, int attributeDeclIndex, XMLAttributeDecl attributeDecl) {

        int attrChunk = attributeDeclIndex >> CHUNK_SHIFT;
        int attrIndex = attributeDeclIndex &  CHUNK_MASK; 

        fAttributeDeclName[attrChunk][attrIndex].setValues(attributeDecl.name);

        fAttributeDeclType[attrChunk][attrIndex]  =  attributeDecl.type;
        if (attributeDecl.list) {
            fAttributeDeclType[attrChunk][attrIndex] |= LIST_FLAG;
        }
        fAttributeDeclEnumeration[attrChunk][attrIndex]  =  attributeDecl.enumeration;
        fAttributeDeclDefaultType[attrChunk][attrIndex]  =  attributeDecl.defaultType;
        fAttributeDeclDatatypeValidator[attrChunk][attrIndex] =  attributeDecl.datatypeValidator;
        fAttributeDeclDefaultValue[attrChunk][attrIndex]      =  attributeDecl.defaultValue;

        int elemChunk     = elementDeclIndex >> CHUNK_SHIFT;
        int elemIndex     = elementDeclIndex &  CHUNK_MASK;
        int index = fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex];
        while (index != -1) {
            if (index == attributeDeclIndex) {
                break;
            }
            attrChunk = index >> CHUNK_SHIFT;
            attrIndex = index & CHUNK_MASK;
            index = fAttributeDeclNextAttributeDeclIndex[attrChunk][attrIndex];
        }
        if (index == -1) {
            if (fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex] == -1) {
                fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex] = attributeDeclIndex;
            }
            else {
                index = fElementDeclLastAttributeDeclIndex[elemChunk][elemIndex];
                attrChunk = index >> CHUNK_SHIFT;
                attrIndex = index & CHUNK_MASK;
                fAttributeDeclNextAttributeDeclIndex[attrChunk][attrIndex] = attributeDeclIndex;
            }
            fElementDeclLastAttributeDeclIndex[elemChunk][elemIndex] = attributeDeclIndex;
        }

    }

    protected boolean isDTD() {
        return false;
    }


    public void printElements(org.apache.xerces.utils.StringPool pool) {
        int elementDeclIndex = 0;
        XMLElementDecl elementDecl = new XMLElementDecl();
        while (getElementDecl(elementDeclIndex++, elementDecl)) {
            System.out.println("element decl: "+elementDecl.name+
                               ", "+pool.toString(elementDecl.name.rawname)+
                               ", "+XMLContentSpec.toString(this, pool, elementDecl.contentSpecIndex));
        }
    }

    public void printAttributes(int elementDeclIndex) {
        int attributeDeclIndex = getFirstAttributeDeclIndex(elementDeclIndex);
        System.out.print(elementDeclIndex);
        System.out.print(" [");
        while (attributeDeclIndex != -1) {
            System.out.print(' ');
            System.out.print(attributeDeclIndex);
            printAttribute(attributeDeclIndex);
            attributeDeclIndex = getNextAttributeDeclIndex(attributeDeclIndex);
            if (attributeDeclIndex != -1) {
                System.out.print(",");
            }
        }
        System.out.println(" ]");
    }



    private void printAttribute(int attributeDeclIndex) {
        XMLAttributeDecl attributeDecl = new XMLAttributeDecl();
        if (getAttributeDecl(attributeDeclIndex, attributeDecl)) {
            System.out.print(" { ");
            System.out.print(attributeDecl.name.localpart);
            System.out.print(" }");
        }
    }


    private final XMLContentModel createChildModel(int contentSpecIndex) throws CMException
    {
        XMLContentSpec contentSpec = new XMLContentSpec();


        getContentSpec(contentSpecIndex, contentSpec);

        if ((contentSpec.type & 0x0f ) == XMLContentSpec.CONTENTSPECNODE_ANY ||
            (contentSpec.type & 0x0f ) == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER ||
            (contentSpec.type & 0x0f ) == XMLContentSpec.CONTENTSPECNODE_ANY_LOCAL) {
        }

        else if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
            if (contentSpec.value == -1 && contentSpec.otherValue == -1)
                throw new CMException(ImplementationMessages.VAL_NPCD);


            fQName1.setValues(-1, contentSpec.value, contentSpec.value, contentSpec.otherValue);
            return new SimpleContentModel(fQName1, null, contentSpec.type, isDTD());
        } 
        else if ((contentSpec.type == XMLContentSpec.CONTENTSPECNODE_CHOICE)
                   ||  (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_SEQ)) {
            XMLContentSpec contentSpecLeft  = new XMLContentSpec();
            XMLContentSpec contentSpecRight = new XMLContentSpec();

            getContentSpec(contentSpec.value, contentSpecLeft);
            getContentSpec(contentSpec.otherValue, contentSpecRight);

            if ((contentSpecLeft.type == XMLContentSpec.CONTENTSPECNODE_LEAF)
                &&  (contentSpecRight.type == XMLContentSpec.CONTENTSPECNODE_LEAF)) {
                fQName1.setValues(-1, contentSpecLeft.value, contentSpecLeft.value, contentSpecLeft.otherValue);
                fQName2.setValues(-1, contentSpecRight.value, contentSpecRight.value, contentSpecRight.otherValue);
                return new SimpleContentModel(fQName1, fQName2, contentSpec.type, isDTD());
            }
        } 
        else if ((contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE)
                   ||  (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE)
                   ||  (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE)) {
            XMLContentSpec contentSpecLeft = new XMLContentSpec();
            getContentSpec(contentSpec.value, contentSpecLeft);

            if (contentSpecLeft.type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
                fQName1.setValues(-1, contentSpecLeft.value, contentSpecLeft.value, contentSpecLeft.otherValue);
                return new SimpleContentModel(fQName1, null, contentSpec.type, isDTD());
            }
        } 
        else {
            throw new CMException(ImplementationMessages.VAL_CST);
        }

        
        fLeafCount = 0;
        CMNode cmn    = buildSyntaxTree(contentSpecIndex, contentSpec);

        return new DFAContentModel(  cmn, fLeafCount, isDTD());
    }

    private void printSyntaxTree(CMNode cmn){
        System.out.println("CMNode : " + cmn.type());

        if (cmn.type() == XMLContentSpec.CONTENTSPECNODE_LEAF) {
            System.out.println( "     Leaf: " + ((CMLeaf)cmn).getElement());
            return;
        }
        if (cmn instanceof CMBinOp) {
            printSyntaxTree( ((CMBinOp)cmn).getLeft());
            printSyntaxTree( ((CMBinOp)cmn).getRight());
        }
        if (cmn instanceof CMUniOp) {
            printSyntaxTree( ((CMUniOp)cmn).getChild());
        }
        
    }

    
    private int countLeaves(int contentSpecIndex) {
        return countLeaves(contentSpecIndex, new XMLContentSpec());
    }
    
    private int countLeaves(int contentSpecIndex, XMLContentSpec contentSpec) {
        
        if (contentSpecIndex == -1) {
            return 0;
        }
        /****
        int chunk = contentSpecIndex >> CHUNK_SHIFT;
        int index = contentSpecIndex & CHUNK_MASK;
        int type = fContentSpecType[chunk][index];
        if (type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
            return 1;
        }
        int value = fContentSpecValue[chunk][index];
        int otherValue = fContentSpecOtherValue[chunk][index];
        return countLeaves(value) + countLeaves(otherValue);
        /***/
        getContentSpec(contentSpecIndex, contentSpec);
        if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
            return 1;
        }
        int value = contentSpec.value;
        int otherValue = contentSpec.otherValue;
        return countLeaves(value, contentSpec) + countLeaves(otherValue, contentSpec);
        /***/
    }

    private int fLeafCount = 0;
    private int fEpsilonIndex = -1;
    private final CMNode buildSyntaxTree(int startNode, XMLContentSpec contentSpec) throws CMException
    {
        CMNode nodeRet = null;
        getContentSpec(startNode, contentSpec);
        if ((contentSpec.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY) {
            nodeRet = new CMAny(contentSpec.type, contentSpec.otherValue, fLeafCount++);
        }
        else if ((contentSpec.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER) {
            nodeRet = new CMAny(contentSpec.type, contentSpec.otherValue, fLeafCount++);
        }
        else if ((contentSpec.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY_LOCAL) {
            nodeRet = new CMAny(contentSpec.type, -1, fLeafCount++);
        }
        else if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
            fQName1.setValues(-1, contentSpec.value, contentSpec.value, contentSpec.otherValue);
            nodeRet = new CMLeaf(fQName1, fLeafCount++);
        } 
        else {
            final int leftNode = contentSpec.value;
            final int rightNode = contentSpec.otherValue;

            if ((contentSpec.type == XMLContentSpec.CONTENTSPECNODE_CHOICE)
                ||  (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_SEQ)) {

                nodeRet = new CMBinOp( contentSpec.type, buildSyntaxTree(leftNode, contentSpec)
                                       , buildSyntaxTree(rightNode, contentSpec));

		/* MODIFIED (Jan, 2001) */

	    } else if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE
		       || contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE
		       || contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE) {
		nodeRet = new CMUniOp(contentSpec.type, buildSyntaxTree(leftNode, contentSpec));
	    }

		/* MODIFIED (Jan, 2001) */

	    else {
		throw new CMException(ImplementationMessages.VAL_CST);
            }
        }
        return nodeRet;
    }

    /**
     * Build a vector of valid QNames from Content Spec
     * table.
     * 
     * @param contentSpecIndex
     *               Content Spec index
     * @param vectorQName
     *               Array of QName
     * @exception CMException
     */
    private void contentSpecTree(int contentSpecIndex, 
                                XMLContentSpec contentSpec,
                                ChildrenList children) throws CMException {

        getContentSpec( contentSpecIndex, contentSpec);
        if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_LEAF ||
            (contentSpec.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY ||
            (contentSpec.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY_LOCAL ||
            (contentSpec.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER) {

            if (children.length == children.qname.length) {
                QName[] newQName = new QName[children.length * 2];
                System.arraycopy(children.qname, 0, newQName, 0, children.length);
                children.qname = newQName;
                int[] newType = new int[children.length * 2];
                System.arraycopy(children.type, 0, newType, 0, children.length);
                children.type = newType;
            }

            children.qname[children.length] = new QName(-1, contentSpec.value, contentSpec.value, contentSpec.otherValue);
            children.type[children.length] = contentSpec.type;
            children.length++;
            return;
        }

        final int leftNode  = contentSpec.value;
        final int rightNode = contentSpec.otherValue;

        if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_CHOICE ||
            contentSpec.type == XMLContentSpec.CONTENTSPECNODE_SEQ) {
            contentSpecTree(leftNode, contentSpec, children);
            contentSpecTree(rightNode, contentSpec, children);
            return;
        } 

        if (contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE ||
            contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE ||
            contentSpec.type == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE) {
            contentSpecTree(leftNode, contentSpec, children);
            return;
        }

        throw new CMException(ImplementationMessages.VAL_CST);
    }




    private boolean ensureElementDeclCapacity(int chunk) {
        try {
            return fElementDeclName[chunk][0] == null;
        } catch (ArrayIndexOutOfBoundsException ex) {
            fElementDeclName = resize(fElementDeclName, fElementDeclName.length * 2);
            fElementDeclType = resize(fElementDeclType, fElementDeclType.length * 2);
            fElementDeclDatatypeValidator = resize(fElementDeclDatatypeValidator, fElementDeclDatatypeValidator.length * 2);
            fElementDeclContentSpecIndex = resize(fElementDeclContentSpecIndex, fElementDeclContentSpecIndex.length * 2);
            fElementDeclContentModelValidator = resize(fElementDeclContentModelValidator, fElementDeclContentModelValidator.length * 2);
            fElementDeclFirstAttributeDeclIndex = resize(fElementDeclFirstAttributeDeclIndex, fElementDeclFirstAttributeDeclIndex.length * 2);
            fElementDeclLastAttributeDeclIndex = resize(fElementDeclLastAttributeDeclIndex, fElementDeclLastAttributeDeclIndex.length * 2);
            fElementDeclUnique = resize(fElementDeclUnique, fElementDeclUnique.length * 2);
            fElementDeclKey = resize(fElementDeclKey, fElementDeclKey.length * 2);
            fElementDeclKeyRef = resize(fElementDeclKeyRef, fElementDeclKeyRef.length * 2);
        } catch (NullPointerException ex) {
        }
        fElementDeclName[chunk] = new QName[CHUNK_SIZE];
        fElementDeclType[chunk] = new int[CHUNK_SIZE];
        fElementDeclDatatypeValidator[chunk] = new DatatypeValidator[CHUNK_SIZE];
        fElementDeclContentSpecIndex[chunk] = new int[CHUNK_SIZE];
        fElementDeclContentModelValidator[chunk] = new XMLContentModel[CHUNK_SIZE];
        fElementDeclFirstAttributeDeclIndex[chunk] = new int[CHUNK_SIZE];
        fElementDeclLastAttributeDeclIndex[chunk] = new int[CHUNK_SIZE];
        fElementDeclUnique[chunk] = new Vector[CHUNK_SIZE];
        fElementDeclKey[chunk] = new Vector[CHUNK_SIZE];
        fElementDeclKeyRef[chunk] = new Vector[CHUNK_SIZE];
        return true;
    }

    private boolean ensureContentSpecCapacity(int chunk) {
        try {
            return fContentSpecType[chunk][0] == 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            fContentSpecType = resize(fContentSpecType, fContentSpecType.length * 2);
            fContentSpecValue = resize(fContentSpecValue, fContentSpecValue.length * 2);
            fContentSpecOtherValue = resize(fContentSpecOtherValue, fContentSpecOtherValue.length * 2);
        } catch (NullPointerException ex) {
        }
        fContentSpecType[chunk] = new int[CHUNK_SIZE];
        fContentSpecValue[chunk] = new int[CHUNK_SIZE];
        fContentSpecOtherValue[chunk] = new int[CHUNK_SIZE];
        return true;
    }

    private boolean ensureAttributeDeclCapacity(int chunk) {
        try {
            return fAttributeDeclName[chunk][0] == null;
        } catch (ArrayIndexOutOfBoundsException ex) {
            fAttributeDeclName = resize(fAttributeDeclName, fAttributeDeclName.length * 2);
            fAttributeDeclType = resize(fAttributeDeclType, fAttributeDeclType.length * 2);
            fAttributeDeclEnumeration = resize(fAttributeDeclEnumeration, fAttributeDeclEnumeration.length * 2);
            fAttributeDeclDefaultType = resize(fAttributeDeclDefaultType, fAttributeDeclDefaultType.length * 2);
            fAttributeDeclDatatypeValidator = resize(fAttributeDeclDatatypeValidator, fAttributeDeclDatatypeValidator.length * 2);
            fAttributeDeclDefaultValue = resize(fAttributeDeclDefaultValue, fAttributeDeclDefaultValue.length * 2);
            fAttributeDeclNextAttributeDeclIndex = resize(fAttributeDeclNextAttributeDeclIndex, fAttributeDeclNextAttributeDeclIndex.length * 2);
        } catch (NullPointerException ex) {
        }
        fAttributeDeclName[chunk] = new QName[CHUNK_SIZE];
        fAttributeDeclType[chunk] = new int[CHUNK_SIZE];
        fAttributeDeclEnumeration[chunk] = new int[CHUNK_SIZE];
        fAttributeDeclDefaultType[chunk] = new int[CHUNK_SIZE];
        fAttributeDeclDatatypeValidator[chunk] = new DatatypeValidator[CHUNK_SIZE];
        fAttributeDeclDefaultValue[chunk] = new String[CHUNK_SIZE];
        fAttributeDeclNextAttributeDeclIndex[chunk] = new int[CHUNK_SIZE];
        return true;
    }


    private int[][] resize(int array[][], int newsize) {
        int newarray[][] = new int[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

    private DatatypeValidator[][] resize(DatatypeValidator array[][], int newsize) {
        DatatypeValidator newarray[][] = new DatatypeValidator[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

    private XMLContentModel[][] resize(XMLContentModel array[][], int newsize) {
        XMLContentModel newarray[][] = new XMLContentModel[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

    private QName[][] resize(QName array[][], int newsize) {
        QName newarray[][] = new QName[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

    private String[][] resize(String array[][], int newsize) {
        String newarray[][] = new String[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

    private Vector[][] resize(Vector array[][], int newsize) {
        Vector newarray[][] = new Vector[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }


    /**
     * Children list for <code>contentSpecTree</code> method.
     */
    static class ChildrenList {
        public int length = 0;
        public QName[] qname = new QName[2];
        public int[] type = new int[2];
    }

