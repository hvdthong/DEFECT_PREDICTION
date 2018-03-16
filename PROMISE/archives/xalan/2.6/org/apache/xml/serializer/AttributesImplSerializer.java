package org.apache.xml.serializer;

import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * This class extends org.xml.sax.helpers.AttributesImpl which implements org.
 * xml.sax.Attributes. But for optimization this class adds a Hashtable for
 * faster lookup of an index by qName, which is commonly done in the stream
 * serializer.
 * 
 * @see org.xml.sax.Attributes
 */
public class AttributesImplSerializer extends AttributesImpl
{
    /**
     * Hash table of qName/index values to quickly lookup the index
     * of an attributes qName.  qNames are in uppercase in the hash table
     * to make the search case insensitive.
     */
    private Hashtable m_indexFromQName = new Hashtable();
    
    /**
     * This is the number of attributes before switching to the hash table,
     * and can be tuned, but 12 seems good for now - bjm
     */
    public static final int MAX = 12;
    
    /**
     * One less than the number of attributes before switching to
     * the Hashtable.
     */
    private static final int MAXMinus1 = MAX - 1;

    /**
     * This method gets the index of an attribute given its qName.
     * @param qname the qualified name of the attribute, e.g. "prefix1:locName1"
     * @return the integer index of the attribute.
     * @see org.xml.sax.Attributes#getIndex(String)
     */
    public int getIndex(String qname)
    {
        int index;

        if (super.getLength() < MAX)
        {
            index = super.getIndex(qname);
            return index;
        }
        Integer i = (Integer)m_indexFromQName.get(qname);
        if (i == null)
            index = -1;
        else
            index = i.intValue();
        return index;
    }
    /**
     * This method adds the attribute, but also records its qName/index pair in
     * the hashtable for fast lookup by getIndex(qName).
     * @param uri the URI of the attribute
     * @param local the local name of the attribute
     * @param qname the qualified name of the attribute
     * @param type the type of the attribute
     * @param val the value of the attribute
     *
     * @see org.xml.sax.helpers.AttributesImpl#addAttribute(String, String, String, String, String)
     * @see #getIndex(String)
     */
    public void addAttribute(
        String uri,
        String local,
        String qname,
        String type,
        String val)
    {
        int index = super.getLength();
        super.addAttribute(uri, local, qname, type, val);

        if (index < MAXMinus1)
        {
            return;
        }
        else if (index == MAXMinus1)
        {
            switchOverToHash(MAX);
        }
        else
        {
            /* we have just added the attibute, its index is the old length */
            Integer i = new Integer(index);
            m_indexFromQName.put(qname, i);
        }
        return;
    }

    /**
     * We are switching over to having a hash table for quick look
     * up of attributes, but up until now we haven't kept any
     * information in the Hashtable, so we now update the Hashtable.
     * Future additional attributes will update the Hashtable as
     * they are added.
     * @param numAtts
     */
    private void switchOverToHash(int numAtts)
    {
        for (int index = 0; index < numAtts; index++)
        {
            String qName = super.getQName(index);
            Integer i = new Integer(index);
            m_indexFromQName.put(qName, i);
        }
    }

    /**
     * This method clears the accumulated attributes.
     *
     * @see org.xml.sax.helpers.AttributesImpl#clear()
     */
    public void clear()
    {

        int len = super.getLength();
        super.clear();
        if (MAX <= len)
        {
            m_indexFromQName.clear();
        }

    }

    /**
     * This method sets the attributes, previous attributes are cleared,
     * it also keeps the hashtable up to date for quick lookup via
     * getIndex(qName).
     * @param atts the attributes to copy into these attributes.
     * @see org.xml.sax.helpers.AttributesImpl#setAttributes(Attributes)
     * @see #getIndex(String)
     */
    public void setAttributes(Attributes atts)
    {

        super.setAttributes(atts);

        int numAtts = atts.getLength();
        if (MAX <= numAtts)
            switchOverToHash(numAtts);

    }
}
