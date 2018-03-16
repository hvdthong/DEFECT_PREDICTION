 
 /*
 * @author Eric Ye
 *         
 * @see    
 * @version $Id: SchemaGrammar.java 315947 2000-07-21 03:42:45Z ericye $
 */
package org.apache.xerces.validators.schema;

import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.utils.NamespacesScope;
import org.apache.xerces.utils.QName;
import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.validators.datatype.DatatypeValidator;
import org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl;
import org.apache.xerces.validators.common.XMLAttributeDecl;
import org.apache.xerces.validators.common.XMLContentModel;
import org.apache.xerces.validators.common.XMLElementDecl;
import org.apache.xerces.validators.common.Grammar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Hashtable;

public class SchemaGrammar extends Grammar{


    private static final int CHUNK_SIZE = (1 << CHUNK_SHIFT);
    private static final int CHUNK_MASK = CHUNK_SIZE - 1;

    private XMLContentSpec fTempContentSpecNode = new XMLContentSpec();
    private XMLElementDecl fTempElementDecl = new XMLElementDecl();
    private XMLAttributeDecl fTempAttributeDecl = new XMLAttributeDecl();






    private int fScopeDefinedByElement[][] = new int[INITIAL_CHUNK_COUNT][];
    private String fFromAnotherSchemaURI[][] = new String[INITIAL_CHUNK_COUNT][];
    private TraverseSchema.ComplexTypeInfo fComplexTypeInfo[][] = 
        new TraverseSchema.ComplexTypeInfo[INITIAL_CHUNK_COUNT][];
    private int fElementDeclDefaultType[][] = new int[INITIAL_CHUNK_COUNT][];
    private String fElementDeclDefaultValue[][] = new String[INITIAL_CHUNK_COUNT][];
    private String fElementDeclEquivClassFullName[][] = new String[INITIAL_CHUNK_COUNT][];
    private int fElementDeclBlockSet[][] = new int[INITIAL_CHUNK_COUNT][];
    private int fElementDeclFinalSet[][] = new int[INITIAL_CHUNK_COUNT][];
    private int fElementDeclMiscFlags[][] = new int[INITIAL_CHUNK_COUNT][];

    private Hashtable fComplexTypeRegistry = null;
    private Hashtable fAttributeDeclRegistry = null;
    private DatatypeValidatorFactoryImpl fDatatypeRegistry = null;

    Hashtable topLevelGroupDecls = new Hashtable();
    Hashtable topLevelAttrDecls  = new Hashtable();
    Hashtable topLevelAttrGrpDecls = new Hashtable();

    private NamespacesScope fNamespacesScope = null;
    private String fTargetNamespaceURI = "";


    public NamespacesScope getNamespacesScope(){
        return fNamespacesScope;
    }

    public String getTargetNamespaceURI(){
        return fTargetNamespaceURI;
    }

    public Hashtable getAttirubteDeclRegistry() {
        return fAttributeDeclRegistry;
    }

    public Hashtable getComplexTypeRegistry(){
        return fComplexTypeRegistry;
    }

    public DatatypeValidatorFactoryImpl getDatatypeRegistry(){
        return fDatatypeRegistry;
    }

    public int getElementDefinedScope(int elementDeclIndex) {
        
        if (elementDeclIndex < -1) {
            return -1;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fScopeDefinedByElement[chunk][index];

    }
    
    public int getElementDefaultTYpe(int elementDeclIndex) {
        
        if (elementDeclIndex < -1) {
            return -1;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fElementDeclDefaultType[chunk][index];

    }

    public int getElementDeclBlockSet(int elementDeclIndex) {

        if (elementDeclIndex < -1) {
            return -1;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fElementDeclBlockSet[chunk][index];
    }

    public int getElementDeclFinalSet(int elementDeclIndex) {

        if (elementDeclIndex < -1) {
            return -1;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fElementDeclFinalSet[chunk][index];
    }

    public int getElementDeclMiscFlags(int elementDeclIndex) {

        if (elementDeclIndex < -1) {
            return -1;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fElementDeclMiscFlags[chunk][index];
    }

    public String getElementFromAnotherSchemaURI(int elementDeclIndex) {
        
        if (elementDeclIndex < 0 ) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fFromAnotherSchemaURI[chunk][index];

    }

    public String getElementDefaultValue(int elementDeclIndex) {
        
        if (elementDeclIndex < 0 ) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fElementDeclDefaultValue[chunk][index];

    }
    public String getElementDeclEquivClassElementFullName( int elementDeclIndex){
        
        if (elementDeclIndex < 0 ) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fElementDeclEquivClassFullName[chunk][index];

    }

    public TraverseSchema.ComplexTypeInfo getElementComplexTypeInfo(int elementDeclIndex){

        if (elementDeclIndex <- 1) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fComplexTypeInfo[chunk][index];
    }



    protected void  setAttributeDeclRegistry(Hashtable attrReg){
        fAttributeDeclRegistry = attrReg;
    }

    protected void  setComplexTypeRegistry(Hashtable cTypeReg){
        fComplexTypeRegistry = cTypeReg;
    }

    protected void setDatatypeRegistry(DatatypeValidatorFactoryImpl dTypeReg){
        fDatatypeRegistry = dTypeReg;
    }

    protected void setNamespacesScope(NamespacesScope nsScope) {
        fNamespacesScope = nsScope;
    }

    protected void setTargetNamespaceURI(String targetNSUri) {
        fTargetNamespaceURI = targetNSUri;
    }


    protected int createElementDecl() {
        return super.createElementDecl();
    }

    protected void setElementDecl(int elementDeclIndex, XMLElementDecl elementDecl) {
        super.setElementDecl(elementDeclIndex,elementDecl);
    }



    protected int createContentSpec() {
        return super.createContentSpec();
    }

    protected void setContentSpec(int contentSpecIndex, XMLContentSpec contentSpec) {
        super.setContentSpec(contentSpecIndex, contentSpec);
    }

    protected int createAttributeDecl() {
        return super.createAttributeDecl();
    }

    protected void setAttributeDecl(int elementDeclIndex, int attributeDeclIndex, XMLAttributeDecl attributeDecl) {
        super.setAttributeDecl(elementDeclIndex, attributeDeclIndex, attributeDecl);
    }

    protected void setElementDefinedScope(int elementDeclIndex, int scopeDefined) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fScopeDefinedByElement[chunk][index] = scopeDefined;
        }
    }

    protected  void setElementFromAnotherSchemaURI(int elementDeclIndex, String anotherSchemaURI) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fFromAnotherSchemaURI[chunk][index] = anotherSchemaURI;
        }
    }

    protected void setElementComplexTypeInfo(int elementDeclIndex, TraverseSchema.ComplexTypeInfo typeInfo){
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fComplexTypeInfo[chunk][index] = typeInfo;
        }
    }

    protected void setElementDefault(int elementDeclIndex, int defaultType, String defaultValue) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fElementDeclDefaultType[chunk][index] = defaultType;
            fElementDeclDefaultValue[chunk][index] = defaultValue;
        }
    }
    
    protected void setElementDeclBlockSet(int elementDeclIndex, int blockSet) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fElementDeclBlockSet[chunk][index] = blockSet;
        }
    }

    protected void setElementDeclFinalSet(int elementDeclIndex, int finalSet) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fElementDeclFinalSet[chunk][index] = finalSet;
        }
    }

    protected void setElementDeclMiscFlags(int elementDeclIndex, int miscFlags) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fElementDeclMiscFlags[chunk][index] = miscFlags;
        }
    }

    protected void setElementDeclEquivClassElementFullName( int elementDeclIndex, String equivClassFullName){
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fElementDeclEquivClassFullName[chunk][index] = equivClassFullName;
        }
    }

    /**
     *@return elementDecl Index, 
     */

    protected int addElementDecl(QName eltQName, int enclosingScope, int scopeDefined, 
                                 int contentSpecType, int contentSpecIndex, 
                                 int attrListHead, DatatypeValidator dv){
        int elementDeclIndex = getElementDeclIndex(eltQName, enclosingScope);
        if (elementDeclIndex == -1) {
            if (enclosingScope<-1 || scopeDefined < -1 ) {
            }
            fTempElementDecl.name.setValues(eltQName);
            fTempElementDecl.enclosingScope = enclosingScope;
            fTempElementDecl.type = contentSpecType;
            fTempElementDecl.contentSpecIndex = contentSpecIndex;
            fTempElementDecl.datatypeValidator = dv;
            elementDeclIndex = createElementDecl();
            setElementDecl(elementDeclIndex,fTempElementDecl);
            setFirstAttributeDeclIndex(elementDeclIndex, attrListHead);
            setElementDefinedScope(elementDeclIndex, scopeDefined);
        }

    /*****
             XMLElementDecl fTempElementDecl = new XMLElementDecl();                                          
             getElementDecl(elementDeclIndex, fTempElementDecl);                                              
             System.out.println("elementDeclIndex in addElementDecl : " + elementDeclIndex                            
                                + " \n and itsName : '"                                                               
                                + (fTempElementDecl.name.localpart)                                       
                                +"' \n its ContentType:" + (fTempElementDecl.type)                        
                                +"\n its ContentSpecIndex : " + fTempElementDecl.contentSpecIndex +"\n"); 
    /*****/
        return elementDeclIndex;

    }

    /**
     *@return the new attribute List Head
     */
    protected void addAttDef(  int templateElementIndex, 
                      QName attQName, int attType, 
                      int enumeration, int attDefaultType, 
                      String attDefaultValue, DatatypeValidator dv, boolean isList){
        int attrDeclIndex = createAttributeDecl();
        fTempAttributeDecl.name.setValues(attQName);
        fTempAttributeDecl.datatypeValidator = dv;
        fTempAttributeDecl.type = attType;
        fTempAttributeDecl.defaultType = attDefaultType;
        fTempAttributeDecl.defaultValue = attDefaultValue;
        fTempAttributeDecl.list = isList;
        fTempAttributeDecl.enumeration = enumeration;

        super.setAttributeDecl(templateElementIndex, attrDeclIndex, fTempAttributeDecl);
    }

    public int getAttributeDeclIndex(int elementIndex, QName attribute) {
        if (elementIndex == -1) {
            return -1;
        }
        int attDefIndex = getFirstAttributeDeclIndex(elementIndex);
        while (attDefIndex != -1) {
            getAttributeDecl(attDefIndex, fTempAttributeDecl);

            if (fTempAttributeDecl.name.localpart == attribute.localpart &&
                fTempAttributeDecl.name.uri == attribute.uri ) {
                return attDefIndex;
            }
            attDefIndex = getNextAttributeDeclIndex(attDefIndex);
        }
        return -1;

    /**
     *@return the new contentSpec Index
     */
    protected int addContentSpecNode(int contentSpecType, int value, int otherValue, boolean mustBeUnique) {
        fTempContentSpecNode.type = contentSpecType;
        fTempContentSpecNode.value = value;
        fTempContentSpecNode.otherValue = otherValue;
        
        int contentSpecIndex = createContentSpec();
        setContentSpec(contentSpecIndex, fTempContentSpecNode);
        return contentSpecIndex;
    }
                                                



    private boolean ensureElementDeclCapacity(int chunk) {
        try {
            return  fScopeDefinedByElement[chunk][0] == -2;
        } 
        catch (ArrayIndexOutOfBoundsException ex) {
             fScopeDefinedByElement= resize(fScopeDefinedByElement, fScopeDefinedByElement.length * 2);
             fFromAnotherSchemaURI = resize(fFromAnotherSchemaURI, fFromAnotherSchemaURI.length *2);
             fComplexTypeInfo =      resize(fComplexTypeInfo, fComplexTypeInfo.length *2);
             fElementDeclDefaultType = resize(fElementDeclDefaultType,fElementDeclDefaultType.length*2);
             fElementDeclDefaultValue = resize(fElementDeclDefaultValue,fElementDeclDefaultValue.length*2);
             fElementDeclBlockSet = resize(fElementDeclBlockSet,fElementDeclBlockSet.length*2);
             fElementDeclFinalSet = resize(fElementDeclFinalSet,fElementDeclFinalSet.length*2);
             fElementDeclMiscFlags = resize(fElementDeclMiscFlags,fElementDeclMiscFlags.length*2);
             fElementDeclEquivClassFullName = resize(fElementDeclEquivClassFullName,fElementDeclEquivClassFullName.length*2);
        }
        catch (NullPointerException ex) {
        }
        fScopeDefinedByElement[chunk] = new int[CHUNK_SIZE];
        for (int i=0; i<CHUNK_SIZE; i++) {
        }
        fFromAnotherSchemaURI[chunk] = new String[CHUNK_SIZE];
        fComplexTypeInfo[chunk] = new TraverseSchema.ComplexTypeInfo[CHUNK_SIZE];
        fElementDeclDefaultType[chunk] = new int[CHUNK_SIZE];
        fElementDeclDefaultValue[chunk] = new String[CHUNK_SIZE];
        fElementDeclEquivClassFullName[chunk] = new String[CHUNK_SIZE];
        return true;
    }



    private int[][] resize(int array[][], int newsize) {
        int newarray[][] = new int[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

    private DatatypeValidator[][] resize(DatatypeValidator array[][], int newsize) {
        return array;
    }

    private XMLContentModel[][] resize(XMLContentModel array[][], int newsize) {
        return array;
    }

    private QName[][] resize(QName array[][], int newsize) {
        return array;
    }

    private String[][] resize(String array[][], int newsize) {
        String newarray[][] = new String[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }
    private TraverseSchema.ComplexTypeInfo[][] resize(TraverseSchema.ComplexTypeInfo array[][], int newsize) {
        TraverseSchema.ComplexTypeInfo newarray[][] = new TraverseSchema.ComplexTypeInfo[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

