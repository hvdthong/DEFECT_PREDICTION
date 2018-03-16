package org.apache.xalan.xsltc.trax;

import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;


/**
 * skeleton extension of XMLFilterImpl for now.  
 */
public class TrAXFilter extends XMLFilterImpl {
    public TrAXFilter(Templates templates)  throws 
	TransformerConfigurationException
    {
	/* nothing yet */ 
    }
}
