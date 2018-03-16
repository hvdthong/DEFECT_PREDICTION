package org.apache.xerces.validators.common;

import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.validators.schema.SubstitutionGroupComparator;
import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.validators.schema.SchemaMessageProvider;

/**
 * ElementWildcard is used to check whether two element declarations conflict
 */
public class ElementWildcard {
    private ElementWildcard(){}

    private static boolean uriInWildcard(QName qname, int wildcard, int wtype,
                                         SubstitutionGroupComparator comparator) throws Exception {
        int type = wtype & 0x0f;

        if (type == XMLContentSpec.CONTENTSPECNODE_ANY) {
            return true;
        }
        else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_NS) {
            if (comparator != null) {
                if (comparator.isAllowedByWildcard(qname, wildcard, false))
                    return true;
            } else {
                if (qname.uri == wildcard)
                    return true;
            }
        }
        else if (type == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER) {
            if (comparator != null) {
                if (comparator.isAllowedByWildcard(qname, wildcard, true))
                    return true;
            } else {
                if (wildcard != qname.uri)
                    return true;
            }
        }

        return false;
    }

    private static boolean wildcardIntersect(int w1, int t1, int w2, int t2) {
        int type1 = t1 & 0x0f, type2 = t2 & 0x0f;

        if (type1 == XMLContentSpec.CONTENTSPECNODE_ANY ||
            type2 == XMLContentSpec.CONTENTSPECNODE_ANY) {
            return true;
        }

        if (type1 == XMLContentSpec.CONTENTSPECNODE_ANY_NS &&
            type2 == XMLContentSpec.CONTENTSPECNODE_ANY_NS &&
            w1 == w2) {
            return true;
        }

        if (type1 == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER &&
            type2 == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER) {
            return true;
        }

        if ((type1 == XMLContentSpec.CONTENTSPECNODE_ANY_NS &&
             type2 == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER ||
             type1 == XMLContentSpec.CONTENTSPECNODE_ANY_OTHER &&
             type2 == XMLContentSpec.CONTENTSPECNODE_ANY_NS) &&
            w1 != w2) {
            return true;
        }

        return false;
    }

    private static boolean conflic(int type1, int local1, int uri1,
                                   int type2, int local2, int uri2,
                                   SubstitutionGroupComparator comparator) throws Exception {
        QName q1 = new QName(), q2 = new QName();
        q1.localpart = local1;
        q1.uri = uri1;
        q2.localpart = local2;
        q2.uri = uri2;

        if (type1 == XMLContentSpec.CONTENTSPECNODE_LEAF &&
            type2 == XMLContentSpec.CONTENTSPECNODE_LEAF) {
            if (comparator != null) {
                if (comparator.isEquivalentTo(q1, q2) ||
                    comparator.isEquivalentTo(q2, q1))
                    return true;
            } else {
                if (q1.localpart == q2.localpart &&
                    q1.uri == q2.uri)
                    return true;
            }
        } else if (type1 == XMLContentSpec.CONTENTSPECNODE_LEAF) {
            if (uriInWildcard(q1, uri2, type2, comparator))
                return true;
        } else if (type2 == XMLContentSpec.CONTENTSPECNODE_LEAF) {
            if (uriInWildcard(q2, uri1, type1, comparator))
                return true;
        } else {
            if (wildcardIntersect(uri1, type1, uri2, type2))
                return true;
        }

        return false;
    }

    public static boolean conflict(int type1, int local1, int uri1,
                                   int type2, int local2, int uri2,
                                   SubstitutionGroupComparator comparator) throws Exception {
        boolean ret = conflic(type1, local1, uri1, type2, local2, uri2, comparator);

        if (ret && comparator != null) {
            StringPool stringPool = comparator.getStringPool();
            XMLErrorReporter err = comparator.getErrorReporter();
            err.reportError(err.getLocator(),
                            SchemaMessageProvider.SCHEMA_DOMAIN,
                            SchemaMessageProvider.UniqueParticleAttribution,
                            SchemaMessageProvider.MSG_NONE,
                            new Object[]{eleString(type1, local1, uri1, stringPool),
                                         eleString(type2, local2, uri2, stringPool)},
                            XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
        }

        return ret;
    }

    private static String eleString(int type, int local, int uri, StringPool stringPool) {
        switch (type & 0x0f) {
        case XMLContentSpec.CONTENTSPECNODE_LEAF:
            return stringPool.toString(uri) + "," + stringPool.toString(local);
        case XMLContentSpec.CONTENTSPECNODE_ANY:
            return "##any,*";
        case XMLContentSpec.CONTENTSPECNODE_ANY_NS:
            return  "##any(" + stringPool.toString(uri) + "),*";
        case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER:
            return "##other(" + stringPool.toString(uri) + "),*";
        }

        return "";
    }
