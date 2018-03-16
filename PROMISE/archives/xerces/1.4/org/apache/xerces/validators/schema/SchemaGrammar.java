package org.apache.xerces.validators.schema;

import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.validators.common.CMException;
import org.apache.xerces.utils.NamespacesScope;
import org.apache.xerces.utils.QName;
import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.validators.datatype.DatatypeValidator;
import org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl;
import org.apache.xerces.validators.common.XMLAttributeDecl;
import org.apache.xerces.validators.common.XMLContentModel;
import org.apache.xerces.validators.common.XMLElementDecl;
import org.apache.xerces.validators.common.Grammar;
import org.apache.xerces.validators.common.GrammarResolver;
import org.apache.xerces.utils.StringPool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Hashtable;
import java.util.Vector;

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
    private String fElementDeclSubGroupAffFullName[][] = new String[INITIAL_CHUNK_COUNT][];
    private Vector fElementDeclSubGroupQNames[][] = new Vector[INITIAL_CHUNK_COUNT][];
    private Vector fElementDeclAllSubGroupQNamesBlock[][] = new Vector[INITIAL_CHUNK_COUNT][];
    private Vector fElementDeclAllSubGroupQNames[][] = new Vector[INITIAL_CHUNK_COUNT][];
    private int fElementDeclBlockSet[][] = new int[INITIAL_CHUNK_COUNT][];
    private int fElementDeclFinalSet[][] = new int[INITIAL_CHUNK_COUNT][];
    private int fElementDeclMiscFlags[][] = new int[INITIAL_CHUNK_COUNT][];

    private int fContentSpecMinOccurs[][] = new int[INITIAL_CHUNK_COUNT][];
    private int fContentSpecMaxOccurs[][] = new int[INITIAL_CHUNK_COUNT][];

    private Hashtable fComplexTypeRegistry = null;
    private Hashtable fAttributeDeclRegistry = null;
    private DatatypeValidatorFactoryImpl fDatatypeRegistry = null;

    Hashtable topLevelGroupDecls = new Hashtable();
    Hashtable topLevelNotationDecls = new Hashtable();
    Hashtable topLevelAttrDecls  = new Hashtable();
    Hashtable topLevelAttrGrpDecls = new Hashtable();
    Hashtable topLevelElemDecls = new Hashtable();
    Hashtable topLevelTypeDecls = new Hashtable();

    private NamespacesScope fNamespacesScope = null;
    private String fTargetNamespaceURI = "";

    private boolean deferContentSpecExpansion = false;

    private boolean checkUniqueParticleAttribution = false;
    private boolean checkingUPA = false;
    private int fContentSpecOrgUri[][] = new int[INITIAL_CHUNK_COUNT][];


    public NamespacesScope getNamespacesScope(){
        return fNamespacesScope;
    }

    public boolean getDeferContentSpecExpansion() {
        return deferContentSpecExpansion;
    }

    public boolean getCheckUniqueParticleAttribution() {
        return checkUniqueParticleAttribution;
    }

    public String getTargetNamespaceURI(){
        return fTargetNamespaceURI;
    }

    public Hashtable getAttributeDeclRegistry() {
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
    public String getElementDeclSubstitutionGroupAffFullName( int elementDeclIndex){

        if (elementDeclIndex < 0 ) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fElementDeclSubGroupAffFullName[chunk][index];

    }

    private Vector getElementDeclSubstitutionGroupQNames( int elementDeclIndex){

        if (elementDeclIndex < 0 ) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fElementDeclSubGroupQNames[chunk][index];

    }

    private Vector getElementDeclAllSubstitutionGroupQNamesBlock( int elementDeclIndex, GrammarResolver grammarResolver, StringPool stringPool) {
        if (elementDeclIndex < 0 ) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        if (fElementDeclAllSubGroupQNamesBlock[chunk][index] != null)
            return fElementDeclAllSubGroupQNamesBlock[chunk][index];

        Vector groups = new Vector();
        fElementDeclAllSubGroupQNamesBlock[chunk][index] = groups;

        TraverseSchema.ComplexTypeInfo typeInfo = getElementComplexTypeInfo(elementDeclIndex);

        Vector substitutionGroupQNames = getElementDeclSubstitutionGroupQNames(elementDeclIndex);
        int size = substitutionGroupQNames == null ? 0 : substitutionGroupQNames.size();
        for (int i = 0; i < size; i++) {
            OneSubGroup oneGroup = (OneSubGroup)substitutionGroupQNames.elementAt(i);
            SchemaGrammar sGrammar = oneGroup.grammar;
            int subElementIndex = oneGroup.eleIndex;

            int devMethod = 0, pDevMethod = 0;

            TraverseSchema.ComplexTypeInfo subTypeInfo = sGrammar.getElementComplexTypeInfo(subElementIndex);
            if (subTypeInfo == null) {
                XMLElementDecl tmpElementDecl = new XMLElementDecl();
                sGrammar.getElementDecl(subElementIndex, tmpElementDecl);
                DatatypeValidator subElementDV = tmpElementDecl.datatypeValidator;
                getElementDecl(elementDeclIndex, tmpElementDecl);
                DatatypeValidator elementDV = tmpElementDecl.datatypeValidator;
                if (subElementDV != null && subElementDV != elementDV)
                    devMethod = SchemaSymbols.RESTRICTION;
            } else {
                if (subTypeInfo != typeInfo) {
                    devMethod = subTypeInfo.derivedBy;
                    if (typeInfo != null)
                        pDevMethod = typeInfo.blockSet;
                    subTypeInfo = subTypeInfo.baseComplexTypeInfo;
                }
                for (; subTypeInfo != null && subTypeInfo != typeInfo;
                     subTypeInfo = subTypeInfo.baseComplexTypeInfo) {
                     devMethod |= subTypeInfo.derivedBy;
                     pDevMethod |= subTypeInfo.blockSet;
                }
            }

            SubGroupBlockQName oneName = new SubGroupBlockQName();
            oneName.name = oneGroup;
            oneName.method = devMethod;
            oneName.pmethod = pDevMethod;
            groups.addElement(oneName);

            Vector subSubGroup = sGrammar.getElementDeclAllSubstitutionGroupQNamesBlock(subElementIndex, grammarResolver, stringPool);
            int bsize = subSubGroup == null ? 0 : subSubGroup.size();
            for (int j = 0; j < bsize; j++) {
                SubGroupBlockQName name = (SubGroupBlockQName)subSubGroup.elementAt(j);
                oneName = new SubGroupBlockQName();
                oneName.name = name.name;
                oneName.method = name.method|devMethod;
                oneName.pmethod = name.pmethod|pDevMethod;
                groups.addElement(oneName);
            }
        }

        return groups;
    }

    public Vector getElementDeclAllSubstitutionGroupQNames( int elementDeclIndex, GrammarResolver grammarResolver, StringPool stringPool) {

        if (elementDeclIndex < 0 ) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        if (fElementDeclAllSubGroupQNames[chunk][index] != null)
            return fElementDeclAllSubGroupQNames[chunk][index];

        Vector groups = new Vector();
        fElementDeclAllSubGroupQNames[chunk][index] = groups;

        int blockSet = getElementDeclBlockSet(elementDeclIndex);
        if((blockSet & SchemaSymbols.SUBSTITUTION) != 0)
            return groups;

        Vector substitutionGroupQNamesBlock = getElementDeclAllSubstitutionGroupQNamesBlock(elementDeclIndex, grammarResolver, stringPool);
        for (int i = 0; i < substitutionGroupQNamesBlock.size(); i++) {
            SubGroupBlockQName oneName = (SubGroupBlockQName)substitutionGroupQNamesBlock.elementAt(i);
            if (((blockSet | oneName.pmethod) & oneName.method) == 0)
                groups.addElement(oneName.name);
        }

        return groups;
    }

    public TraverseSchema.ComplexTypeInfo getElementComplexTypeInfo(int elementDeclIndex){

        if (elementDeclIndex <- 1) {
            return null;
        }
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        return fComplexTypeInfo[chunk][index];
    }


    protected int convertContentSpecTree(int contentSpecIndex) {


        if ((!deferContentSpecExpansion) || (contentSpecIndex<0)) {
           return contentSpecIndex;
        }

        getContentSpec( contentSpecIndex, fTempContentSpecNode);

        int minOccurs = getContentSpecMinOccurs(contentSpecIndex);
        int maxOccurs = getContentSpecMaxOccurs(contentSpecIndex);


        if (((fTempContentSpecNode.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY) ||
            ((fTempContentSpecNode.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER) ||
            ((fTempContentSpecNode.type & 0x0f) == XMLContentSpec.CONTENTSPECNODE_ANY_NS) ||
            (fTempContentSpecNode.type == XMLContentSpec.CONTENTSPECNODE_LEAF)) {

          if (checkingUPA) {
            contentSpecIndex = addContentSpecNode(fTempContentSpecNode.type,
                                                  fTempContentSpecNode.value,
                                                  fTempContentSpecNode.otherValue,
                                                  false);
            setContentSpecOrgUri(contentSpecIndex, fTempContentSpecNode.otherValue);
            getContentSpec(contentSpecIndex, fTempContentSpecNode);
            fTempContentSpecNode.otherValue = contentSpecIndex;
            setContentSpec(contentSpecIndex, fTempContentSpecNode);
          }

          return expandContentModel(contentSpecIndex,minOccurs,maxOccurs);
        }
        else if (fTempContentSpecNode.type == XMLContentSpec.CONTENTSPECNODE_CHOICE ||
                 fTempContentSpecNode.type == XMLContentSpec.CONTENTSPECNODE_ALL ||
                 fTempContentSpecNode.type == XMLContentSpec.CONTENTSPECNODE_SEQ) {

          int left = fTempContentSpecNode.value;
          int right = fTempContentSpecNode.otherValue;
          int type = fTempContentSpecNode.type;

          left =  convertContentSpecTree(left);

          if (right == -2)
             return expandContentModel(left, minOccurs, maxOccurs);

          right =  convertContentSpecTree(right);

          if (checkingUPA) {
              contentSpecIndex = addContentSpecNode (type, left, right, false);
          } else {
          fTempContentSpecNode.type = type;
          fTempContentSpecNode.value = left;
          fTempContentSpecNode.otherValue = right;
          setContentSpec(contentSpecIndex, fTempContentSpecNode);
          }

          return expandContentModel(contentSpecIndex, minOccurs, maxOccurs);
        }
        else {
          if (checkingUPA) {
              return addContentSpecNode (fTempContentSpecNode.type,
                                         convertContentSpecTree(fTempContentSpecNode.value),
                                         convertContentSpecTree(fTempContentSpecNode.otherValue),
                                         false);
          } else {
              return contentSpecIndex;
          }
        }
    }

    public XMLContentModel getContentModel(int contentSpecIndex, int contentType, SubstitutionGroupComparator comparator) throws Exception {
        if (existContentModel(contentSpecIndex))
            return super.getContentModel(contentSpecIndex, contentType, comparator);

        if (checkUniqueParticleAttribution) {
            checkingUPA = true;
            XMLContentModel contentModel = super.getContentModel(contentSpecIndex, contentType, comparator);
            checkingUPA = false;

            if (contentModel != null) {
                contentModel.checkUniqueParticleAttribution(this);
                clearContentModel(contentSpecIndex);
            }
        }

        return super.getContentModel(contentSpecIndex, contentType, comparator);
    }

    public void setContentSpecOrgUri(int contentSpecIndex, int orgUri) {
        if (contentSpecIndex > -1 ) {
            int chunk = contentSpecIndex >> CHUNK_SHIFT;
            int index = contentSpecIndex & CHUNK_MASK;
            ensureContentSpecCapacity(chunk);
            fContentSpecOrgUri[chunk][index] = orgUri;
        }
    }
    public int getContentSpecOrgUri(int contentSpecIndex) {
        if (contentSpecIndex > -1 ) {
            int chunk = contentSpecIndex >> CHUNK_SHIFT;
            int index = contentSpecIndex & CHUNK_MASK;
            return fContentSpecOrgUri[chunk][index];
        } else {
            return -1;
        }
    }

    public void setDeferContentSpecExpansion() {
        deferContentSpecExpansion = true;
    }

    public void setCheckUniqueParticleAttribution() {
        deferContentSpecExpansion = true;
        checkUniqueParticleAttribution = true;
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

    protected void setElementDefault(int elementDeclIndex, String defaultValue) {
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
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

    protected void setElementDeclSubstitutionGroupAffFullName( int elementDeclIndex, String substitutionGroupFullName){
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            fElementDeclSubGroupAffFullName[chunk][index] = substitutionGroupFullName;
        }
    }

    protected void addElementDeclOneSubstitutionGroupQName( int elementDeclIndex, QName name, SchemaGrammar grammar, int eleIndex){
        int chunk = elementDeclIndex >> CHUNK_SHIFT;
        int index = elementDeclIndex & CHUNK_MASK;
        ensureElementDeclCapacity(chunk);
        if (elementDeclIndex > -1 ) {
            if (fElementDeclSubGroupQNames[chunk][index] == null)
                fElementDeclSubGroupQNames[chunk][index] = new Vector();
            OneSubGroup oneGroup = new OneSubGroup();
            oneGroup.name = name;
            oneGroup.grammar = grammar;
            oneGroup.eleIndex = eleIndex;
            fElementDeclSubGroupQNames[chunk][index].addElement(oneGroup);
        }
    }

    protected void setContentSpecMinOccurs(int contentSpecIndex, int minOccurs) {
        if (contentSpecIndex > -1 ) {
        int chunk = contentSpecIndex >> CHUNK_SHIFT;
        int index = contentSpecIndex & CHUNK_MASK;
        ensureContentSpecCapacity(chunk);
            fContentSpecMinOccurs[chunk][index] = minOccurs;
        }
    }

    protected int getContentSpecMinOccurs(int contentSpecIndex) {
        if (contentSpecIndex > -1 ) {
        int chunk = contentSpecIndex >> CHUNK_SHIFT;
        int index = contentSpecIndex & CHUNK_MASK;
        return fContentSpecMinOccurs[chunk][index];
        } else {
            return -1;
        }
    }

    protected int getContentSpecMaxOccurs(int contentSpecIndex) {
        if (contentSpecIndex > -1 ) {
        int chunk = contentSpecIndex >> CHUNK_SHIFT;
        int index = contentSpecIndex & CHUNK_MASK;
        return fContentSpecMaxOccurs[chunk][index];
        } else {
            return -1;
        }
    }

    protected void setContentSpecMaxOccurs(int contentSpecIndex, int maxOccurs) {
        if (contentSpecIndex > -1 ) {
        int chunk = contentSpecIndex >> CHUNK_SHIFT;
        int index = contentSpecIndex & CHUNK_MASK;
        ensureContentSpecCapacity(chunk);
            fContentSpecMaxOccurs[chunk][index] = maxOccurs;
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

    protected int cloneElementDecl(int eltNdx, int enclosingScope, int scopeDefined) {

        getElementDecl(eltNdx,fTempElementDecl);
        TraverseSchema.ComplexTypeInfo typeInfo = getElementComplexTypeInfo(eltNdx);
        int blockSet = getElementDeclBlockSet(eltNdx);
        int finalSet = getElementDeclFinalSet(eltNdx);
        int elementMiscFlags = getElementDeclMiscFlags(eltNdx);
        String defaultStr = getElementDefaultValue(eltNdx);
        String subGroupName = getElementDeclSubstitutionGroupAffFullName(eltNdx);
        int attrListHead = getFirstAttributeDeclIndex(eltNdx);
        String anotherSchema = getElementFromAnotherSchemaURI(eltNdx);

        fTempElementDecl.enclosingScope = enclosingScope;
        int newElt= addElementDecl(fTempElementDecl.name,enclosingScope,scopeDefined, 
                 fTempElementDecl.type,fTempElementDecl.contentSpecIndex,
                 attrListHead,fTempElementDecl.datatypeValidator);

        setElementComplexTypeInfo(newElt, typeInfo);
        setElementDeclBlockSet(newElt, blockSet);
        setElementDeclFinalSet(newElt, finalSet);
        setElementDeclMiscFlags(newElt, elementMiscFlags);
        setElementDefault(newElt, defaultStr);
        setElementFromAnotherSchemaURI(newElt, anotherSchema);
        return newElt;

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
        return findAttributeDecl(attDefIndex, attribute);


    public int findAttributeDecl(int attListHead, QName attribute) {

        int attDefIndex = attListHead;
        while (attDefIndex != -1) {
            getAttributeDecl(attDefIndex, fTempAttributeDecl);
            if (fTempAttributeDecl.name.localpart == attribute.localpart &&
                fTempAttributeDecl.name.uri == attribute.uri ) {
                return attDefIndex;
            }
            attDefIndex = getNextAttributeDeclIndex(attDefIndex);
        }
        return -1;
     }

    /**
      set the attribute decl index
     */
    protected void setFirstAttributeDeclIndex(int eltNdx, int attListHead) {
      super.setFirstAttributeDeclIndex(eltNdx, attListHead);
    }

    /**
     *@return the new contentSpec Index
     */
    protected int addContentSpecNode(int contentSpecType, int value, int otherValue, boolean mustBeUnique) {
        fTempContentSpecNode.type = contentSpecType;
        fTempContentSpecNode.value = value;
        fTempContentSpecNode.otherValue = otherValue;

        int contentSpecIndex = createContentSpec();
        setContentSpec(contentSpecIndex, fTempContentSpecNode);
        setContentSpecMinOccurs(contentSpecIndex, 1);
        setContentSpecMaxOccurs(contentSpecIndex, 1);
        return contentSpecIndex;
    }

    protected int expandContentModel(int index, int minOccurs, int maxOccurs) {

        int leafIndex = index;

        if (minOccurs==1 && maxOccurs==1) {

        }
        else if (minOccurs==0 && maxOccurs==1) {
            index = addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE,
                                                   index,
                                                   -1,
                                                   false);
        }
        else if (minOccurs == 0 && maxOccurs==SchemaSymbols.OCCURRENCE_UNBOUNDED) {
            index = addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE,
                                                   index,
                                                   -1,
                                                   false);
        }
        else if (minOccurs == 1 && maxOccurs==SchemaSymbols.OCCURRENCE_UNBOUNDED) {
            index = addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE,
                                                   index,
                                                   -1,
                                                   false);
        }
        else if (maxOccurs == SchemaSymbols.OCCURRENCE_UNBOUNDED) {
            if (minOccurs<2) {
            }

            index = addContentSpecNode( XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE,
                   index,
                   -1,
                   false);

            for (int i=0; i < (minOccurs-1); i++) {
                index = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                      leafIndex,
                                                      index,
                                                      false);
            }

        }
        else {


            if (minOccurs==0) {
                int optional = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE,
                                                                 leafIndex,
                                                                 -1,
                                                                 false);
                index = optional;
                for (int i=0; i < (maxOccurs-minOccurs-1); i++) {
                    index = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                              index,
                                                              optional,
                                                              false);
                }
            }
            else {
                for (int i=0; i<(minOccurs-1); i++) {
                    index = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                          index,
                                                          leafIndex,
                                                          false);
                }

                int optional = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE,
                                                                 leafIndex,
                                                                 -1,
                                                                 false);
                for (int i=0; i < (maxOccurs-minOccurs); i++) {
                    index = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_SEQ,
                                                              index,
                                                              optional,
                                                              false);
                }
            }
        }

        return index;
    }




    private boolean ensureContentSpecCapacity(int chunk) {
        try {
            return fContentSpecMinOccurs[chunk][0] == 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            fContentSpecMinOccurs = resize(fContentSpecMinOccurs, fContentSpecMinOccurs.length * 2);
            fContentSpecMaxOccurs = resize(fContentSpecMaxOccurs, fContentSpecMaxOccurs.length * 2);
            fContentSpecOrgUri = resize(fContentSpecOrgUri, fContentSpecOrgUri.length * 2);
        } catch (NullPointerException ex) {
        }
        fContentSpecMinOccurs[chunk] = new int[CHUNK_SIZE];
        fContentSpecMaxOccurs[chunk] = new int[CHUNK_SIZE];
        fContentSpecOrgUri[chunk] = new int[CHUNK_SIZE];
        return true;
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
             fElementDeclSubGroupAffFullName = resize(fElementDeclSubGroupAffFullName,fElementDeclSubGroupAffFullName.length*2);
             fElementDeclSubGroupQNames = resize(fElementDeclSubGroupQNames,fElementDeclSubGroupQNames.length*2);
             fElementDeclAllSubGroupQNames = resize(fElementDeclAllSubGroupQNames,fElementDeclAllSubGroupQNames.length*2);
             fElementDeclAllSubGroupQNamesBlock = resize(fElementDeclAllSubGroupQNamesBlock,fElementDeclAllSubGroupQNamesBlock.length*2);
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
        fElementDeclSubGroupAffFullName[chunk] = new String[CHUNK_SIZE];
        fElementDeclSubGroupQNames[chunk] = new Vector[CHUNK_SIZE];
        fElementDeclAllSubGroupQNames[chunk] = new Vector[CHUNK_SIZE];
        fElementDeclAllSubGroupQNamesBlock[chunk] = new Vector[CHUNK_SIZE];
        return true;
    }



    private int[][] resize(int array[][], int newsize) {
        int newarray[][] = new int[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
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

    private Vector[][] resize(Vector array[][], int newsize) {
        Vector newarray[][] = new Vector[newsize][];
        System.arraycopy(array, 0, newarray, 0, array.length);
        return newarray;
    }

    public class OneSubGroup {
        public QName name;
        SchemaGrammar grammar;
        int eleIndex;
    }

    private class SubGroupBlockQName {
        public OneSubGroup name;
        public int method;
        public int pmethod;
