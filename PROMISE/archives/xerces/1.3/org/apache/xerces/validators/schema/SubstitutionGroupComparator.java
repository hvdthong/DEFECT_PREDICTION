package org.apache.xerces.validators.schema;

import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.common.GrammarResolver;

import org.xml.sax.SAXException;

import java.lang.ClassCastException;
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

    private SubstitutionGroupComparator(){
    }
    public  SubstitutionGroupComparator(GrammarResolver grammarResolver, StringPool stringPool){
        fGrammarResolver = grammarResolver;
        fStringPool = stringPool;
    }

    public boolean isEquivalentTo(QName aElement, QName exemplar) throws Exception{
        if (aElement.localpart==exemplar.localpart && aElement.uri==exemplar.uri ) {
            return true;
        }

        if (fGrammarResolver == null || fStringPool == null) {
            throw new SAXException("Try to check substitutionGroup against a substitutionGroup, but no GrammarResolver is defined");
        }

        int uriIndex = aElement.uri;
        int localpartIndex = aElement.localpart;
        String uri = fStringPool.toString(aElement.uri);
        String localpart = fStringPool.toString(aElement.localpart);

        while (count >= 0) {
            if(uri==null) {
                return false;
            }
            SchemaGrammar sGrammar = null;
            try {
                sGrammar = (SchemaGrammar) fGrammarResolver.getGrammar(uri);
            }
            catch ( ClassCastException ce) {
                return false;
            }
            if(sGrammar == null) return false;

            int elementIndex = sGrammar.getElementDeclIndex(uriIndex, localpartIndex, TOP_LEVEL_SCOPE);
            if (elementIndex == -1) {
                return false;
            }

            String substitutionGroupFullName = sGrammar.getElementDeclSubstitutionGroupElementFullName(elementIndex);
            if (substitutionGroupFullName==null) {
                return false;
            }

            int commaAt = substitutionGroupFullName.indexOf(","); 
            uri = "";
            localpart = substitutionGroupFullName;
            if (  commaAt >= 0  ) {
                if (commaAt > 0 ) {
                    uri = substitutionGroupFullName.substring(0,commaAt);
                }
                localpart = substitutionGroupFullName.substring(commaAt+1);
            }
            uriIndex = fStringPool.addSymbol(uri);
            localpartIndex = fStringPool.addSymbol(localpart);

            if (uriIndex == exemplar.uri && localpartIndex == exemplar.localpart) {
                return true;
            }

            count--;
        }

        return false;
    }
}
