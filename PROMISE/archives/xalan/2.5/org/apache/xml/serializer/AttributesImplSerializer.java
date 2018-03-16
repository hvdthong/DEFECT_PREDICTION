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
     * This method gets the index of an attribute given its qName.
     * @param qname the qualified name of the attribute, e.g. "prefix1:locName1"
     * @return the integer index of the attribute.
     * @see org.xml.sax.Attributes#getIndex(String)
     */
    public int getIndex(String qname)
    {
        int index;
        Integer i = (Integer) m_indexFromQName.get(qname);
        if (i==null)
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
     * @see getIndex(String)
     */
    public void addAttribute(
    String uri, String local, String qname, String type, String val)
    {
        int index = super.getLength();
        super.addAttribute(uri,local,qname,type,val);
        
        /* we have just added the attibute, its index is the old length */
        Integer i = new Integer(index);
        m_indexFromQName.put(qname, i);
    }
    

    /**
     * This method clears the accumulated attributes.
     *
     * @see org.xml.sax.helpers.AttributesImpl#clear()
     */
    public void clear()
    {
        m_indexFromQName.clear();
        super.clear();
    }    
    
    /**
     * This method sets the attributes, previous attributes are cleared,
     * it also keeps the hashtable up to date for quick lookup via
     * getIndex(qName).
     * @param atts the attributes to copy into these attributes.
     * @see org.xml.sax.helpers.AttributesImpl#setAttributes(Attributes)
     * @see getIndex(String)
     */
    public void setAttributes(Attributes atts)
    {
        super.setAttributes(atts);
        
        int numAtts = atts.getLength();
        for (int i=0; i < numAtts; i++)
        {
            String qName = atts.getQName(i);
            int index = super.getIndex(qName);
            Integer io = new Integer(index);
            m_indexFromQName.put(qName, io);            
            
        }
    }
}
