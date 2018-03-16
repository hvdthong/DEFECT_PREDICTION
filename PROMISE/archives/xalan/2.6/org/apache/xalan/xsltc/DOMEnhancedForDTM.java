package org.apache.xalan.xsltc;

/**
 * Interface for SAXImpl which adds methods used at run-time, over and above
 * those provided by the XSLTC DOM interface. An attempt to avoid the current
 * "Is the DTM a DOM, if so is it a SAXImpl, . . .
 * which was producing some ugly replicated code
 * and introducing bugs where that multipathing had not been
 * done.  This makes it easier to provide other DOM/DOMEnhancedForDTM
 * implementations, rather than hard-wiring XSLTC to SAXImpl.
 * 
 * @author Joseph Kesselman
 *
 */
public interface DOMEnhancedForDTM extends DOM {
    public short[] getMapping(String[] names, String[] uris, int[] types);
    public int[] getReverseMapping(String[] names, String[] uris, int[] types);
    public short[] getNamespaceMapping(String[] namespaces);
    public short[] getReverseNamespaceMapping(String[] namespaces);
    public String getDocumentURI();
    public void setDocumentURI(String uri);    
    public int getExpandedTypeID2(int nodeHandle);
    public boolean hasDOMSource();
    public int getElementById(String idString);    
}
