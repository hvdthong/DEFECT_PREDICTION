package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.ant.taskdefs.XSLTLiaison;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Templates;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Concrete liaison for XSLT processor implementing TraX. (ie JAXP 1.1)
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @author <a href="mailto:dims@yahoo.com">Davanum Srinivas</a>
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 */
public class TraXLiaison implements XSLTLiaison {

    /** The trax TransformerFactory */
    private TransformerFactory tfactory = null;

    /** stylesheet stream, close it asap */
    private FileInputStream xslStream = null;

    /** Stylesheet template */
    private Templates templates = null;

    /** transformer */
    private Transformer transformer = null;

    public TraXLiaison() throws Exception {
        tfactory = TransformerFactory.newInstance();
    }



    public void setStylesheet(File stylesheet) throws Exception {
        xslStream = new FileInputStream(stylesheet);
        StreamSource src = new StreamSource(xslStream);
        src.setSystemId(getSystemId(stylesheet));
        templates = tfactory.newTemplates(src);
        transformer = templates.newTransformer();
    }

    public void transform(File infile, File outfile) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(infile);
            fos = new FileOutputStream(outfile);
            StreamSource src = new StreamSource(fis);
            src.setSystemId(getSystemId(infile));
            StreamResult res = new StreamResult(fos);
            res.setSystemId(getSystemId(outfile));

            transformer.transform(src, res);
        } finally {
            try {
                if (xslStream != null){
                    xslStream.close();
                }
            } catch (IOException ignored){}
            try {
                if (fis != null){
                    fis.close();
                }
            } catch (IOException ignored){}
            try {
                if (fos != null){
                    fos.close();
                }
            } catch (IOException ignored){}
        }
    }

    protected String getSystemId(File file){
      String path = file.getAbsolutePath();
      path = path.replace('\\','/');
      return FILE_PROTOCOL_PREFIX + path;
    }

    public void addParam(String name, String value){
        transformer.setParameter(name, value);
    }
