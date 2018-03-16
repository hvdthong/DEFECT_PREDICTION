package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMWSFilter;

/**
 * A wrapper class that adapts the
 * {@link org.apache.xml.dtm.DTMWSFilter DTMWSFilter} interface to the XSLTC
 * DOM {@link org.apache.xsltc.StripFilter StripFilter} interface.
 */
public class DOMWSFilter implements DTMWSFilter {

    private AbstractTranslet m_translet;
    private StripFilter m_filter;
    
    private Hashtable m_mappings;
    
    private DTM m_currentDTM;
    private short[] m_currentMapping;

    /**
     * Construct an adapter connecting the <code>DTMWSFilter</code> interface
     * to the <code>StripFilter</code> interface.
     *
     * @param translet A translet that also implements the StripFilter
     * interface.
     *
     * @see org.apache.xml.dtm.DTMWSFilter
     * @see org.apache.xsltc.StripFilter
     */
    public DOMWSFilter(AbstractTranslet translet) {
        m_translet = translet;
        m_mappings = new Hashtable();

        if (translet instanceof StripFilter) {
            m_filter = (StripFilter) translet;
        }
    }

    /**
     * Test whether whitespace-only text nodes are visible in the logical
     * view of <code>DTM</code>. Normally, this function
     * will be called by the implementation of <code>DTM</code>;
     * it is not normally called directly from
     * user code.
     *
     * @param node int handle of the node.
     * @param dtm the DTM that owns this node
     * @return one of <code>NOTSTRIP</code>, <code>STRIP</code> or
     * <code>INHERIT</code>.
     */
    public short getShouldStripSpace(int node, DTM dtm) {
        if (m_filter != null && dtm instanceof DOM) {
            DOM dom = (DOM)dtm;
            int type = 0;

            if (dtm instanceof SAXImpl) {
                SAXImpl saxImpl = (SAXImpl)dtm;
                
                short[] mapping;
                if (dtm == m_currentDTM) {
                    mapping = m_currentMapping;
                }
                else {  
                    mapping = (short[])m_mappings.get(dtm);
                    if (mapping == null) {
                        mapping = saxImpl.getMapping(m_translet.getNamesArray());
                        m_mappings.put(dtm, mapping);
                        m_currentDTM = saxImpl;
                        m_currentMapping = mapping;
                    }
                }
                
                int expType = saxImpl.getExpandedTypeID(node);
                
                if (expType >= 0 && expType < mapping.length)
                  type = mapping[expType];
                else
                  type = -1;
                
            } 
            else {
                return INHERIT;
            }

            if (m_filter.stripSpace(dom, node, type)) {
                return STRIP;
            } else {
                return NOTSTRIP;
            }
        } else {
            return NOTSTRIP;
        }
    }
}
