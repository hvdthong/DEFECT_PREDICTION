package org.apache.tools.ant.taskdefs.optional;

import java.io.FileOutputStream;

import org.apache.tools.ant.taskdefs.XSLTLiaison;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @author <a href="mailto:dims@yahoo.com">Davanum Srinivas</a>
 * @version $Revision: 268394 $ $Date: 2001-01-04 00:48:39 +0800 (周四, 2001-01-04) $
 */
public class TraXLiaison implements XSLTLiaison {

    protected final static String FILEURL = "file:";

    /** The trax TransformerFactory */
    private TransformerFactory tfactory = null;

    /** Stylesheet template */
    private Templates templates = null;

    /** The trax Transformer itself */
    private Transformer transformer;

    public TraXLiaison() throws Exception {
        tfactory = TransformerFactory.newInstance();
    }

    public void setStylesheet(String fileName) throws Exception {
        templates = tfactory.newTemplates(new StreamSource(normalize(fileName)));
        transformer = templates.newTransformer();
    };

    public void transform(String infile, String outfile) throws Exception {
        transformer.transform(new StreamSource(normalize(infile)), new StreamResult(new FileOutputStream(outfile)));
    }

    protected String normalize(String fileName) {
        if(fileName != null && !fileName.startsWith(FILEURL)) {
            return FILEURL + fileName;
        }
        return fileName;
    }
    
    public void addParam(String name, String value){
        transformer.setParameter(name, value);
    }
