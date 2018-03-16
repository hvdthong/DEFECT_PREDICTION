package org.apache.tools.ant.taskdefs.optional;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.JAXPUtils;
import org.apache.tools.ant.taskdefs.XSLTLiaison;
import org.apache.tools.ant.taskdefs.XSLTLoggerAware;
import org.apache.tools.ant.taskdefs.XSLTLogger;

import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Templates;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;

import javax.xml.transform.sax.SAXSource;

/**
 * Concrete liaison for XSLT processor implementing TraX. (ie JAXP 1.1)
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @author <a href="mailto:dims@yahoo.com">Davanum Srinivas</a>
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 * @since Ant 1.3
 */
public class TraXLiaison implements XSLTLiaison, ErrorListener, XSLTLoggerAware {

    /** The trax TransformerFactory */
    private TransformerFactory tfactory = null;

    /** stylesheet to use for transformation */
    private File stylesheet;

    /** The In memory version of the stylesheet */
    private Templates templates = null;

    /** 
     * The modification time of the stylesheet from which the templates 
     * are read 
     */
    private long templatesModTime;
        
    /** transformer to use for processing files */
    private Transformer transformer = null;

    private XSLTLogger logger;

    /** possible resolver for publicIds */
    private EntityResolver entityResolver;

    /** possible resolver for URIs */
    private URIResolver uriResolver;

    /** transformer output properties */
    private Vector outputProperties = new Vector();

    /** stylesheet parameters */
    private Vector params = new Vector();

    public TraXLiaison() throws Exception {
    }


    /**
     * Set the output property for the current transformer.
     * Note that the stylesheet must be set prior to calling
     * this method.
     * @param name the output property name.
     * @param value the output property value.
     * @since Ant 1.5
     */
    public void setOutputProperty(String name, String value){
        final String[] pair = new String[]{name, value};
        outputProperties.addElement(pair);
    }




    public void setStylesheet(File stylesheet) throws Exception {
        if (this.stylesheet != null) {
            transformer = null;
            
            if (!this.stylesheet.equals(stylesheet)
                || (stylesheet.lastModified() != templatesModTime)) { 
                templates = null;
            }
        }
        this.stylesheet = stylesheet;
    }

    public void transform(File infile, File outfile) throws Exception {
        if (transformer == null) {
            createTransformer();
        }

        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(infile));
            fos = new BufferedOutputStream(new FileOutputStream(outfile));
            StreamResult res = new StreamResult(fos);
            res.setSystemId(JAXPUtils.getSystemId(outfile));
            Source src = getSource(fis, infile);
            transformer.transform(src, res);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public void addParam(String name, String value){
        final String[] pair = new String[]{name, value};
        params.addElement(pair);
    }

    public void setLogger(XSLTLogger l) {
        logger = l;
    }

    public void error(TransformerException e) {
        logError(e, "Error");
    }

    public void fatalError(TransformerException e) {
        logError(e, "Fatal Error");
        throw new BuildException("Fatal error during transformation", e);
    }

    public void warning(TransformerException e) {
        logError(e, "Warning");
    }

    private void logError(TransformerException e, String type) {
        if (logger == null) {
            return;
        }

        StringBuffer msg = new StringBuffer();
        SourceLocator locator = e.getLocator();
        if (locator != null) {
            String systemid = locator.getSystemId();
            if (systemid != null) {
                String url = systemid;
                    url = url.substring(8);
                }
                msg.append(url);
            } else {
                msg.append("Unknown file");
            }
            int line = locator.getLineNumber();
            if (line != -1) {
                msg.append(":" + line);
                int column = locator.getColumnNumber();
                if (column != -1) {
                    msg.append(":" + column);
                }
            }
        }
        msg.append(": " + type + "! ");
        msg.append(e.getMessage());
        if (e.getCause() != null) {
            msg.append(" Cause: " + e.getCause());
        }

        logger.log(msg.toString());
    }

    /** Set the class to resolve entities during the transformation
     */
    public void setEntityResolver(EntityResolver aResolver) {
        entityResolver = aResolver;
    }

    /** Set the class to resolve URIs during the transformation
     */
    public void setURIResolver(URIResolver aResolver) {
        uriResolver = aResolver;
    }
    
    /**
     * Get the source instance from the stream and id of the file.
     * @param is the stream containing the stylesheet data.
     * @param infile the file that will be used for the systemid.
     * @return the configured source instance matching the stylesheet.
     * @throws Exception if there is a problem creating the source.
     */
    private Source getSource(InputStream is, File infile) throws Exception {
        Source src = null;
        if (entityResolver != null) {
            if (getFactory().getFeature(SAXSource.FEATURE)) {
                SAXParserFactory spFactory = SAXParserFactory.newInstance();
                spFactory.setNamespaceAware(true);
                XMLReader reader = spFactory.newSAXParser().getXMLReader();
                reader.setEntityResolver(entityResolver);
                src = new SAXSource(reader, new InputSource(is));
            } else {
                throw new IllegalStateException("xcatalog specified, but " +
                        "parser doesn't support SAX");
            }
        } else {
            src = new StreamSource(is);
        }
        src.setSystemId(JAXPUtils.getSystemId(infile));
        return src;
    }

    /**
     * Read in templates from the stylsheet
     */
    private void readTemplates() 
        throws IOException, TransformerConfigurationException {

        InputStream xslStream = null;
        try {
            xslStream 
                = new BufferedInputStream(new FileInputStream(stylesheet));
            templatesModTime = stylesheet.lastModified();
            StreamSource src = new StreamSource(xslStream);
            src.setSystemId(JAXPUtils.getSystemId(stylesheet));
            templates = getFactory().newTemplates(src);
        } finally {
            if (xslStream != null) {
                xslStream.close();
            }
        }
    }
    
    /**
     * Create a new transformer based on the liaison settings
     * @return the newly created and configured transformer.
     * @throws Exception thrown if there is an error during creation.
     * @see #setStylesheet(java.io.File)
     * @see #addParam(java.lang.String, java.lang.String)
     * @see #setOutputProperty(java.lang.String, java.lang.String)
     */
    private void createTransformer() throws Exception {
        if (templates == null) {
            readTemplates();
        }

        transformer = templates.newTransformer();

        transformer.setErrorListener(this);
        if (uriResolver != null) {
            transformer.setURIResolver(uriResolver);
        }
        for (int i = 0; i < params.size(); i++) {
            final String[] pair = (String[]) params.elementAt(i);
            transformer.setParameter(pair[0], pair[1]);
        }
        for (int i = 0; i < outputProperties.size(); i++) {
            final String[] pair = (String[]) outputProperties.elementAt(i);
            transformer.setOutputProperty(pair[0], pair[1]);
        }
    }

    /**
     * return the Transformer factory associated to this liaison.
     * @return the Transformer factory associated to this liaison.
     * @throws BuildException thrown if there is a problem creating
     * the factory.
     * @see #setFactory(String)
     * @since Ant 1.5.2
     */
    private TransformerFactory getFactory() throws BuildException {
        if (tfactory != null) {
            return tfactory;
        }
        tfactory = TransformerFactory.newInstance();
        tfactory.setErrorListener(this);

        if (uriResolver != null) {
            tfactory.setURIResolver(uriResolver);
        }
        return tfactory;
    }

    /**
     * @deprecated use org.apache.tools.ant.util.JAXPUtils#getSystemId instead
     */
    protected String getSystemId(File file) {
        return JAXPUtils.getSystemId(file);
    }

