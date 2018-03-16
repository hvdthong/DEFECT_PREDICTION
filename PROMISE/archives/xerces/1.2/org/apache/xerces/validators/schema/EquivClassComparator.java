package org.apache.xerces.validators.schema;

import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.common.GrammarResolver;

import org.xml.sax.SAXException;

import java.lang.ClassCastException;
/* 
 * @author Eric Ye
 *         
 * @see    
 *
 * @version $Id: EquivClassComparator.java 315924 2000-07-14 03:57:10Z ericye $
 */

public class EquivClassComparator {

    private final int TOP_LEVEL_SCOPE = -1;

    private StringPool fStringPool = null;
    private GrammarResolver fGrammarResolver = null;

    private EquivClassComparator(){
    }
    public  EquivClassComparator(GrammarResolver grammarResolver, StringPool stringPool){
        fGrammarResolver = grammarResolver;
        fStringPool = stringPool;
    }

    public boolean isEquivalentTo(QName aElement, QName examplar) throws Exception{
        if (aElement.localpart==examplar.localpart && aElement.uri==examplar.uri ) {
            return true;
        }

        if (fGrammarResolver == null || fStringPool == null) {
            throw new SAXException("Try to check equivalency by equivClass, but no GrammarResolver is defined");
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

            String equivClassFullName = sGrammar.getElementDeclEquivClassElementFullName(elementIndex);
            if (equivClassFullName==null) {
                return false;
            }

            int commaAt = equivClassFullName.indexOf(","); 
            uri = "";
            localpart = equivClassFullName;
            if (  commaAt >= 0  ) {
                if (commaAt > 0 ) {
                    uri = equivClassFullName.substring(0,commaAt);
                }
                localpart = equivClassFullName.substring(commaAt+1);
            }
            uriIndex = fStringPool.addSymbol(uri);
            localpartIndex = fStringPool.addSymbol(localpart);

            if (uriIndex == examplar.uri && localpartIndex == examplar.localpart) {
                return true;
            }

            count--;
        }

        return false;
    }
}
