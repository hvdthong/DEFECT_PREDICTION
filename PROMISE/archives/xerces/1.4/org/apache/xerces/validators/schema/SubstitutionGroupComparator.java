package org.apache.xerces.validators.schema;

import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.common.GrammarResolver;
import org.apache.xerces.validators.common.XMLElementDecl;
import org.apache.xerces.validators.datatype.DatatypeValidator;

import org.xml.sax.SAXException;
import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.utils.XMLMessages;

import java.lang.ClassCastException;
import java.util.Vector;

/*
 * @version 1.0.  ericye, neilg
 *
  * Modified by neilg, 01/18/01
  * Note:  this class, formerly called equivClassComparator.java, has
  * been renamed to comply with schema CR changes.  It still contains
  * some outmoded terminology--such as use of the term "exemplar", now
  * referred to as the head of the substitution group.  I have
  * changed as much terminology as possible, but I thought future
  * maintainers could deal with simple and not necessarily-ill-named
  * concepts like exemplar.
 */

public class SubstitutionGroupComparator {

    private final int TOP_LEVEL_SCOPE = -1;

    private StringPool fStringPool = null;
    private GrammarResolver fGrammarResolver = null;
    private XMLErrorReporter fErrorReporter = null;

    private SubstitutionGroupComparator(){
    }
    public  SubstitutionGroupComparator(GrammarResolver grammarResolver, StringPool stringPool, XMLErrorReporter errorReporter){
        fGrammarResolver = grammarResolver;
        fStringPool = stringPool;
        fErrorReporter = errorReporter;
    }

    public StringPool getStringPool() {
        return fStringPool;
    }

    public XMLErrorReporter getErrorReporter () {
        return fErrorReporter;
    }

    public boolean isEquivalentTo(QName anElement, QName exemplar) throws Exception{
        if (anElement.localpart==exemplar.localpart && anElement.uri==exemplar.uri ) {
        }

        if (fGrammarResolver == null || fStringPool == null) {
            throw new SAXException("Internal error; tried to check an element against a substitutionGroup, but no GrammarResolver is defined");
        }


        int uriIndex = anElement.uri;
        int localpartIndex = anElement.localpart;
        String uri = fStringPool.toString(anElement.uri);
        String localpart = fStringPool.toString(anElement.localpart);

        if(uri==null) {
            return false;
        }
        SchemaGrammar sGrammar = null;
        try {
            sGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(uri);
        }
        catch ( ClassCastException ce) {
            String er = "Grammar with URI " + uri + " is not a schema grammar!";
            Object [] a = {er};
            fErrorReporter.reportError(fErrorReporter.getLocator(),
                    XMLMessages.XML_DOMAIN,
                    XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                    XMLMessages.SCHEMA_GENERIC_ERROR,
                    a, XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
            return false;
        }
        if(sGrammar == null) {
            return false;
        }

        int elementIndex = sGrammar.getElementDeclIndex(uriIndex, localpartIndex, TOP_LEVEL_SCOPE);
        int anElementIndex = elementIndex;

        String substitutionGroupFullName = sGrammar.getElementDeclSubstitutionGroupAffFullName(elementIndex);
        boolean foundIt = false;
        while (substitutionGroupFullName != null) {
            int commaAt = substitutionGroupFullName.indexOf(",");
            uri = "";
            localpart = substitutionGroupFullName;
            if (  commaAt >= 0  ) {
                if (commaAt > 0 ) {
                    uri = substitutionGroupFullName.substring(0,commaAt);
                }
                localpart = substitutionGroupFullName.substring(commaAt+1);
            }
            if(uri==null) {
                return false;
            }
            try {
                sGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(uri);
            }
            catch ( ClassCastException ce) {
                String er = "Grammar with URI " + uri + " is not a schema grammar!";
                Object [] a = {er};
                fErrorReporter.reportError(fErrorReporter.getLocator(),
                     XMLMessages.XML_DOMAIN,
                     XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                     XMLMessages.SCHEMA_GENERIC_ERROR,
                     a, XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                return false;
            }
            if(sGrammar == null) {
                return false;
            }
            uriIndex = fStringPool.addSymbol(uri);
            localpartIndex = fStringPool.addSymbol(localpart);
            elementIndex = sGrammar.getElementDeclIndex(uriIndex, localpartIndex, TOP_LEVEL_SCOPE);
            if (elementIndex == -1) {
                return false;
            }

            if (uriIndex == exemplar.uri && localpartIndex == exemplar.localpart) {
                if((sGrammar.getElementDeclBlockSet(elementIndex) & SchemaSymbols.SUBSTITUTION) != 0) {
                    return false;
                }
                foundIt = true;
                break;
            }

            substitutionGroupFullName = sGrammar.getElementDeclSubstitutionGroupAffFullName(elementIndex);

        }

        if (!foundIt) {
            return false;
        }
        TraverseSchema.ComplexTypeInfo aComplexType = sGrammar.getElementComplexTypeInfo(anElementIndex);
        int exemplarBlockSet = sGrammar.getElementDeclBlockSet(elementIndex);
        if(aComplexType == null) {
            XMLElementDecl anElementDecl = new XMLElementDecl();
            sGrammar.getElementDecl(anElementIndex, anElementDecl);
            DatatypeValidator anElementDV = anElementDecl.datatypeValidator;
            XMLElementDecl exemplarDecl = new XMLElementDecl();
            sGrammar.getElementDecl(elementIndex, exemplarDecl);
            DatatypeValidator exemplarDV = exemplarDecl.datatypeValidator;
            return((anElementDV == null) ||
                ((anElementDV == exemplarDV) ||
                ((exemplarBlockSet & SchemaSymbols.RESTRICTION) == 0)));
        }
        int anElementDerivationMethod = aComplexType.derivedBy;
        if((anElementDerivationMethod & exemplarBlockSet) != 0) return false;
        TraverseSchema.ComplexTypeInfo exemplarComplexType = sGrammar.getElementComplexTypeInfo(elementIndex);
        for(TraverseSchema.ComplexTypeInfo tempType = aComplexType;
                tempType != null && tempType != exemplarComplexType;
                tempType = tempType.baseComplexTypeInfo) {
            if((tempType.blockSet & anElementDerivationMethod) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * check whether one element or any element in its substitution group
     * is allowed by a given wildcard uri
     *
     * @param element  the QName of a given element
     * @param wuri     the uri of the wildcard
     * @param wother   whether the uri is from ##other, so wuri is excluded
     *
     * @return whether the element is allowed by the wildcard
     */
    public boolean isAllowedByWildcard(QName element, int wuri, boolean wother) throws Exception{
        if (!wother && element.uri == wuri ||
            wother && element.uri != wuri) {
            return true;
        }

        if (fGrammarResolver == null || fStringPool == null) {
            throw new SAXException("Internal error; tried to check an element against a substitutionGroup, but no GrammarResolver is defined");
        }

        String uri = fStringPool.toString(element.uri);
        if(uri==null)
            return false;
        SchemaGrammar sGrammar = null;
        try {
            sGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(uri);
        }
        catch ( ClassCastException ce) {
            String er = "Grammar with URI " + uri + " is not a schema grammar!";
            Object [] a = {er};
            fErrorReporter.reportError(fErrorReporter.getLocator(),
                    XMLMessages.XML_DOMAIN,
                    XMLMessages.MSG_GENERIC_SCHEMA_ERROR,
                    XMLMessages.SCHEMA_GENERIC_ERROR,
                    a, XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
            return false;
        }
        if(sGrammar == null)
            return false;

        int elementIndex = sGrammar.getElementDeclIndex(element, TOP_LEVEL_SCOPE);

        Vector substitutionGroupQNames = sGrammar.getElementDeclAllSubstitutionGroupQNames(elementIndex, fGrammarResolver, fStringPool);

        int size = substitutionGroupQNames == null ? 0 : substitutionGroupQNames.size();
        for (int i = 0; i < size; i++) {
            QName name = ((SchemaGrammar.OneSubGroup)substitutionGroupQNames.elementAt(i)).name;
            if (!wother && name.uri == wuri ||
                wother && name.uri != wuri) {
                return true;
            }
        }

        return false;
    }
}
