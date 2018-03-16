package org.apache.xpath.objects;

import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
/*
 * 
 * @author igorh
 *
 * Simple wrapper to DTM and XPathContext objects.
 * Used in XRTreeFrag for caching references to the objects.
 */
 public final class DTMXRTreeFrag {
  private DTM m_dtm;
  private int m_dtmIdentity = DTM.NULL;
  private XPathContext m_xctxt;
 
  public DTMXRTreeFrag(int dtmIdentity, XPathContext xctxt){
      m_xctxt = xctxt;
      m_dtmIdentity = dtmIdentity;
      m_dtm = xctxt.getDTM(dtmIdentity); 
    }

  public final void destruct(){
    m_dtm = null;         
    m_xctxt = null; 
 }

final  DTM getDTM(){return m_dtm;}
public final  int getDTMIdentity(){return m_dtmIdentity;}
final  XPathContext getXPathContext(){return m_xctxt;}

public final int hashCode() { return m_dtmIdentity; }
public final boolean equals(Object obj) {
   if (obj instanceof DTMXRTreeFrag) {
       return (m_dtmIdentity == ((DTMXRTreeFrag)obj).getDTMIdentity());
   }
   return false;
 }

}
