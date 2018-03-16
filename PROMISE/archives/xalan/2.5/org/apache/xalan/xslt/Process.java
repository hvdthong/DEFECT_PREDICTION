package org.apache.xalan.xslt;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.xalan.processor.XSLProcessorVersion;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.trace.PrintTraceListener;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.XalanProperties;
import org.apache.xml.utils.DefaultErrorHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <meta name="usage" content="general"/>
 * The main() method handles the Xalan command-line interface.
 */
public class Process
{
  /**
   * Prints argument options.
   *
   * @param resbundle Resource bundle
   */
  protected static void printArgOptions(ResourceBundle resbundle)
  {
    System.out.println("\n\t\t\t" + resbundle.getString("xslProc_common_options") + "\n");


    
    System.out.println(resbundle.getString("optionMEDIA"));
    System.out.println(resbundle.getString("optionFLAVOR"));
    System.out.println(resbundle.getString("optionDIAG"));
    waitForReturnKey(resbundle);
    
    System.out.println("\n\t\t\t" + resbundle.getString("xslProc_xalan_options") + "\n");
    

    System.out.println(resbundle.getString("optionINCREMENTAL"));
    System.out.println(resbundle.getString("optionNOOPTIMIMIZE"));
    System.out.println(resbundle.getString("optionRL"));
        
    System.out.println("\n\t\t\t" + resbundle.getString("xslProc_xsltc_options") + "\n");
    System.out.println(resbundle.getString("optionXO"));
    System.out.println(resbundle.getString("optionXD"));
    waitForReturnKey(resbundle);    
    System.out.println(resbundle.getString("optionXJ"));
    System.out.println(resbundle.getString("optionXP"));
    System.out.println(resbundle.getString("optionXN"));
    System.out.println(resbundle.getString("optionXX"));
    System.out.println(resbundle.getString("optionXT"));
  }
  
  /**
   * Command line interface to transform an XML document according to
   * the instructions found in an XSL stylesheet.  
   * <p>The Process class provides basic functionality for 
   * performing transformations from the command line.  To see a 
   * list of arguments supported, call with zero arguments.</p>
   * <p>To set stylesheet parameters from the command line, use 
   * <code>-PARAM name expression</code>. If you want to set the 
   * parameter to a string value, simply pass the string value 
   * as-is, and it will be interpreted as a string.  (Note: if 
   * the value has spaces in it, you may need to quote it depending 
   * on your shell environment).</p>
   *
   * @param argv Input parameters from command line
   */
  public static void main(String argv[])
  {
    
    boolean doStackDumpOnError = false;
    boolean setQuietMode = false;
    boolean doDiag = false;


    /**
     * The default diagnostic writer...
     */
    java.io.PrintWriter diagnosticsWriter = new PrintWriter(System.err, true);
    java.io.PrintWriter dumpWriter = diagnosticsWriter;
    ResourceBundle resbundle =
      (XSLMessages.loadResourceBundle(
        org.apache.xml.utils.res.XResourceBundle.ERROR_RESOURCES));
    String flavor = "s2s";

    if (argv.length < 1)
    {
      printArgOptions(resbundle);
    }
    else
    {
      boolean useXSLTC = false;
      for (int i = 0; i < argv.length; i++)
      {
        if ("-XSLTC".equalsIgnoreCase(argv[i]))
        {
          useXSLTC = true;
        }
      }
        
      TransformerFactory tfactory;
      if (useXSLTC)
      {
	 String key = "javax.xml.transform.TransformerFactory";
	 String value = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
	 Properties props = System.getProperties();
	 props.put(key, value);
	 System.setProperties(props);      
      }
      
      try
      {
        tfactory = TransformerFactory.newInstance();
      }
      catch (TransformerFactoryConfigurationError pfe)
      {
        pfe.printStackTrace(dumpWriter);
        diagnosticsWriter.println(
          XSLMessages.createMessage(


        doExit(-1);
      }

      boolean formatOutput = false;
      boolean useSourceLocation = false;
      String inFileName = null;
      String outFileName = null;
      String dumpFileName = null;
      String xslFileName = null;
      String treedumpFileName = null;
      PrintTraceListener tracer = null;
      String outputType = null;
      String media = null;
      Vector params = new Vector();
      boolean quietConflictWarnings = false;
      URIResolver uriResolver = null;
      EntityResolver entityResolver = null;
      ContentHandler contentHandler = null;
      int recursionLimit=-1;

      for (int i = 0; i < argv.length; i++)
      {
        if ("-XSLTC".equalsIgnoreCase(argv[i]))
        {
        }
        else if ("-TT".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
          {
            if (null == tracer)
              tracer = new PrintTraceListener(diagnosticsWriter);

            tracer.m_traceTemplates = true;
          }
          else
            printInvalidXSLTCOption("-TT");

        }
        else if ("-TG".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
          {
            if (null == tracer)
              tracer = new PrintTraceListener(diagnosticsWriter);

            tracer.m_traceGeneration = true;
          }
          else
            printInvalidXSLTCOption("-TG");

        }
        else if ("-TS".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
          {
            if (null == tracer)
              tracer = new PrintTraceListener(diagnosticsWriter);

            tracer.m_traceSelection = true;
          }
          else
            printInvalidXSLTCOption("-TS");

        }
        else if ("-TTC".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
          {
            if (null == tracer)
              tracer = new PrintTraceListener(diagnosticsWriter);

            tracer.m_traceElements = true;
          }
          else
            printInvalidXSLTCOption("-TTC");

        }
        else if ("-INDENT".equalsIgnoreCase(argv[i]))
        {
          int indentAmount;

          if (((i + 1) < argv.length) && (argv[i + 1].charAt(0) != '-'))
          {
            indentAmount = Integer.parseInt(argv[++i]);
          }
          else
          {
            indentAmount = 0;
          }

        }
        else if ("-IN".equalsIgnoreCase(argv[i]))
        {
          if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-')
            inFileName = argv[++i];
          else
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
        }
        else if ("-MEDIA".equalsIgnoreCase(argv[i]))
        {
          if (i + 1 < argv.length)
            media = argv[++i];
          else
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
        }
        else if ("-OUT".equalsIgnoreCase(argv[i]))
        {
          if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-')
            outFileName = argv[++i];
          else
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
        }
        else if ("-XSL".equalsIgnoreCase(argv[i]))
        {
          if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-')
            xslFileName = argv[++i];
          else
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
        }
        else if ("-FLAVOR".equalsIgnoreCase(argv[i]))
        {
          if (i + 1 < argv.length)
          {
            flavor = argv[++i];
          }
          else
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
        }
        else if ("-PARAM".equalsIgnoreCase(argv[i]))
        {
          if (i + 2 < argv.length)
          {
            String name = argv[++i];

            params.addElement(name);

            String expression = argv[++i];

            params.addElement(expression);
          }
          else
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
        }
        else if ("-E".equalsIgnoreCase(argv[i]))
        {

        }
        else if ("-V".equalsIgnoreCase(argv[i]))
        {
                                    + XSLProcessorVersion.S_VERSION + ", " +

          /* xmlProcessorLiaison.getParserDescription()+ */
        }
        else if ("-QC".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
            quietConflictWarnings = true;
          else
            printInvalidXSLTCOption("-QC");
        }
        else if ("-Q".equalsIgnoreCase(argv[i]))
        {
          setQuietMode = true;
        }
        else if ("-DIAG".equalsIgnoreCase(argv[i]))
        {
          doDiag = true;
        }
        else if ("-XML".equalsIgnoreCase(argv[i]))
        {
          outputType = "xml";
        }
        else if ("-TEXT".equalsIgnoreCase(argv[i]))
        {
          outputType = "text";
        }
        else if ("-HTML".equalsIgnoreCase(argv[i]))
        {
          outputType = "html";
        }
        else if ("-EDUMP".equalsIgnoreCase(argv[i]))
        {
          doStackDumpOnError = true;

          if (((i + 1) < argv.length) && (argv[i + 1].charAt(0) != '-'))
          {
            dumpFileName = argv[++i];
          }
        }
        else if ("-URIRESOLVER".equalsIgnoreCase(argv[i]))
        {
          if (i + 1 < argv.length)
          {
            try
            {
              uriResolver =
                (URIResolver) Class.forName(argv[++i],true,ClassLoader.getSystemClassLoader()).newInstance();

              tfactory.setURIResolver(uriResolver);
            }
            catch (Exception cnfe)
            {
              System.err.println(
                XSLMessages.createMessage(
                  XSLTErrorResources.ER_CLASS_NOT_FOUND_FOR_OPTION,
                  new Object[]{ "-URIResolver" }));
              doExit(-1);
            }
          }
          else
          {
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
            doExit(-1);
          }
        }
        else if ("-ENTITYRESOLVER".equalsIgnoreCase(argv[i]))
        {
          if (i + 1 < argv.length)
          {
            try
            {
              entityResolver =
                (EntityResolver) Class.forName(argv[++i],true,ClassLoader.getSystemClassLoader()).newInstance();
            }
            catch (Exception cnfe)
            {
              System.err.println(
                XSLMessages.createMessage(
                  XSLTErrorResources.ER_CLASS_NOT_FOUND_FOR_OPTION,
                  new Object[]{ "-EntityResolver" }));
              doExit(-1);
            }
          }
          else
          {
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
            doExit(-1);
          }
        }
        else if ("-CONTENTHANDLER".equalsIgnoreCase(argv[i]))
        {
          if (i + 1 < argv.length)
          {
            try
            {
              contentHandler =
                (ContentHandler) Class.forName(argv[++i],true,ClassLoader.getSystemClassLoader()).newInstance();
            }
            catch (Exception cnfe)
            {
              System.err.println(
                XSLMessages.createMessage(
                  XSLTErrorResources.ER_CLASS_NOT_FOUND_FOR_OPTION,
                  new Object[]{ "-ContentHandler" }));
              doExit(-1);
            }
          }
          else
          {
            System.err.println(
              XSLMessages.createMessage(
                XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
            doExit(-1);
          }
        }
        else if ("-L".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
            useSourceLocation = true;
          else
            printInvalidXSLTCOption("-L");
        }
        else if ("-INCREMENTAL".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
            tfactory.setAttribute
               java.lang.Boolean.TRUE);
          else
            printInvalidXSLTCOption("-INCREMENTAL");
        }
        else if ("-NOOPTIMIZE".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
            tfactory.setAttribute
               java.lang.Boolean.FALSE);
          else
            printInvalidXSLTCOption("-NOOPTIMIZE");
	}
        else if ("-RL".equalsIgnoreCase(argv[i]))
        {
          if (!useXSLTC)
          {
            if (i + 1 < argv.length)
              recursionLimit = Integer.parseInt(argv[++i]);
            else
              System.err.println(
                XSLMessages.createMessage(
                  XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
          }
          else
          {
            if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-')
             i++;
             
            printInvalidXSLTCOption("-RL");
          }
        }
        else if ("-XO".equalsIgnoreCase(argv[i]))
        {
          if (useXSLTC)
          {
            if (i + 1 < argv.length && argv[i+1].charAt(0) != '-')
            {
              tfactory.setAttribute("generate-translet", "true");
              tfactory.setAttribute("translet-name", argv[++i]);
            }
            else
              tfactory.setAttribute("generate-translet", "true");
          }
          else
          {
            if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-')
             i++;
            printInvalidXalanOption("-XO");
          }
        }
        else if ("-XD".equalsIgnoreCase(argv[i]))
        {
          if (useXSLTC)
          {
            if (i + 1 < argv.length && argv[i+1].charAt(0) != '-')
              tfactory.setAttribute("destination-directory", argv[++i]);
            else
              System.err.println(
                XSLMessages.createMessage(
                  XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
            
          }          
          else
          {
            if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-')
             i++;
             
            printInvalidXalanOption("-XD");
          }
        }
        else if ("-XJ".equalsIgnoreCase(argv[i]))
        {
          if (useXSLTC)
          {
            if (i + 1 < argv.length && argv[i+1].charAt(0) != '-')
            {
              tfactory.setAttribute("generate-translet", "true");
              tfactory.setAttribute("jar-name", argv[++i]);
            }
            else
              System.err.println(
                XSLMessages.createMessage(
                  XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
          }                    
          else
          {
            if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-')
             i++;
             
            printInvalidXalanOption("-XJ");
          }
        
        }
        else if ("-XP".equalsIgnoreCase(argv[i]))
        {
          if (useXSLTC)
          {
            if (i + 1 < argv.length && argv[i+1].charAt(0) != '-')
              tfactory.setAttribute("package-name", argv[++i]);
            else
              System.err.println(
                XSLMessages.createMessage(
                  XSLTErrorResources.ER_MISSING_ARG_FOR_OPTION,
          }                              
          else
          {
            if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-')
             i++;
             
            printInvalidXalanOption("-XP");
          }
        
        }
        else if ("-XN".equalsIgnoreCase(argv[i]))
        {
          if (useXSLTC)
          {
            tfactory.setAttribute("enable-inlining", "true");
          }                                        
          else
            printInvalidXalanOption("-XN");        
        }
        else if ("-XX".equalsIgnoreCase(argv[i]))
        {
          if (useXSLTC)
          {
            tfactory.setAttribute("debug", "true");
          }                                        
          else
            printInvalidXalanOption("-XX");        
        }
        else if ("-XT".equalsIgnoreCase(argv[i]))
        {
          if (useXSLTC)
          {
            tfactory.setAttribute("auto-translet", "true");
          }                                        
          else
            printInvalidXalanOption("-XT");        
        }
        else
          System.err.println(
            XSLMessages.createMessage(
      }
      
      if (inFileName == null && xslFileName == null)
      {
        System.err.println(resbundle.getString("xslProc_no_input"));
        doExit(-1);
      }

      try
      {
        long start = System.currentTimeMillis();

        if (null != dumpFileName)
        {
          dumpWriter = new PrintWriter(new FileWriter(dumpFileName));
        }

        Templates stylesheet = null;

        if (null != xslFileName)
        {
          if (flavor.equals("d2d"))
          {

            DocumentBuilderFactory dfactory =
              DocumentBuilderFactory.newInstance();

            dfactory.setNamespaceAware(true);

            DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
            Node xslDOM = docBuilder.parse(new InputSource(xslFileName));

            stylesheet = tfactory.newTemplates(new DOMSource(xslDOM,
                    xslFileName));
          }
          else
          {
            stylesheet = tfactory.newTemplates(new StreamSource(xslFileName));
          }
        }

        PrintWriter resultWriter;
        StreamResult strResult;

        if (null != outFileName)
        {
          strResult = new StreamResult(new FileOutputStream(outFileName));
          strResult.setSystemId(outFileName);
        }
        else
        {
          strResult = new StreamResult(System.out);
        }

        SAXTransformerFactory stf = (SAXTransformerFactory) tfactory;
        
        if (!useXSLTC && useSourceLocation)
           stf.setAttribute(XalanProperties.SOURCE_LOCATION, Boolean.TRUE);        

        if (null == stylesheet)
        {
          Source source =
            stf.getAssociatedStylesheet(new StreamSource(inFileName), media,
                                        null, null);

          if (null != source)
            stylesheet = tfactory.newTemplates(source);
          else
          {
            if (null != media)
            else
          }
        }

        if (null != stylesheet)
        {
          Transformer transformer = flavor.equals("th") ? null : stylesheet.newTransformer();

          if (null != outputType)
          {
            transformer.setOutputProperty(OutputKeys.METHOD, outputType);
          }

          if (transformer instanceof org.apache.xalan.transformer.TransformerImpl)
          {
            org.apache.xalan.transformer.TransformerImpl impl = (org.apache.xalan.transformer.TransformerImpl)transformer;
            TraceManager tm = impl.getTraceManager();

            if (null != tracer)
              tm.addTraceListener(tracer);

            impl.setQuietConflictWarnings(quietConflictWarnings);

            if (useSourceLocation)
              impl.setProperty(XalanProperties.SOURCE_LOCATION, Boolean.TRUE);

	    if(recursionLimit>0)
	      impl.setRecursionLimit(recursionLimit);

          }

          int nParams = params.size();

          for (int i = 0; i < nParams; i += 2)
          {
            transformer.setParameter((String) params.elementAt(i),
                                     (String) params.elementAt(i + 1));
          }

          if (uriResolver != null)
            transformer.setURIResolver(uriResolver);

          if (null != inFileName)
          {
            if (flavor.equals("d2d"))
            {

              DocumentBuilderFactory dfactory =
                DocumentBuilderFactory.newInstance();

              dfactory.setCoalescing(true);
              dfactory.setNamespaceAware(true);

              DocumentBuilder docBuilder = dfactory.newDocumentBuilder();

              if (entityResolver != null)
                docBuilder.setEntityResolver(entityResolver);

              Node xmlDoc = docBuilder.parse(new InputSource(inFileName));
              Document doc = docBuilder.newDocument();
              org.w3c.dom.DocumentFragment outNode =
                doc.createDocumentFragment();

              transformer.transform(new DOMSource(xmlDoc, inFileName),
                                    new DOMResult(outNode));

              Transformer serializer = stf.newTransformer();
              Properties serializationProps =
                stylesheet.getOutputProperties();

              serializer.setOutputProperties(serializationProps);

              if (contentHandler != null)
              {
                SAXResult result = new SAXResult(contentHandler);

                serializer.transform(new DOMSource(outNode), result);
              }
              else
                serializer.transform(new DOMSource(outNode), strResult);
            }
            else if (flavor.equals("th"))
            {
              {

              XMLReader reader = null;

              try
              {
                javax.xml.parsers.SAXParserFactory factory =
                  javax.xml.parsers.SAXParserFactory.newInstance();

                factory.setNamespaceAware(true);

                javax.xml.parsers.SAXParser jaxpParser =
                  factory.newSAXParser();

                reader = jaxpParser.getXMLReader();
              }
              catch (javax.xml.parsers.ParserConfigurationException ex)
              {
                throw new org.xml.sax.SAXException(ex);
              }
              catch (javax.xml.parsers.FactoryConfigurationError ex1)
              {
                throw new org.xml.sax.SAXException(ex1.toString());
              }
              catch (NoSuchMethodError ex2){}
              catch (AbstractMethodError ame){}

              if (null == reader)
              {
                reader = XMLReaderFactory.createXMLReader();
              }
              
              if (!useXSLTC)
                stf.setAttribute(org.apache.xalan.processor.TransformerFactoryImpl.FEATURE_INCREMENTAL, 
                   Boolean.TRUE);
                 
              TransformerHandler th = stf.newTransformerHandler(stylesheet);
              
              reader.setContentHandler(th);
              reader.setDTDHandler(th);
              
              if(th instanceof org.xml.sax.ErrorHandler)
                reader.setErrorHandler((org.xml.sax.ErrorHandler)th);
              
              try
              {
                reader.setProperty(
              }
              catch (org.xml.sax.SAXNotRecognizedException e){}
              catch (org.xml.sax.SAXNotSupportedException e){}
              try
              {
                                  true);
              } catch (org.xml.sax.SAXException se) {}
        
              th.setResult(strResult);
              
              reader.parse(new InputSource(inFileName));
              }                            
            }
            else
            {
              if (entityResolver != null)
              {
                XMLReader reader = null;

                try
                {
                  javax.xml.parsers.SAXParserFactory factory =
                    javax.xml.parsers.SAXParserFactory.newInstance();

                  factory.setNamespaceAware(true);

                  javax.xml.parsers.SAXParser jaxpParser =
                    factory.newSAXParser();

                  reader = jaxpParser.getXMLReader();
                }
                catch (javax.xml.parsers.ParserConfigurationException ex)
                {
                  throw new org.xml.sax.SAXException(ex);
                }
                catch (javax.xml.parsers.FactoryConfigurationError ex1)
                {
                  throw new org.xml.sax.SAXException(ex1.toString());
                }
                catch (NoSuchMethodError ex2){}
                catch (AbstractMethodError ame){}

                if (null == reader)
                {
                  reader = XMLReaderFactory.createXMLReader();
                }

                reader.setEntityResolver(entityResolver);

                if (contentHandler != null)
                {
                  SAXResult result = new SAXResult(contentHandler);

                  transformer.transform(
                    new SAXSource(reader, new InputSource(inFileName)),
                    result);
                }
                else
                {
                  transformer.transform(
                    new SAXSource(reader, new InputSource(inFileName)),
                    strResult);
                }
              }
              else if (contentHandler != null)
              {
                SAXResult result = new SAXResult(contentHandler);

                transformer.transform(new StreamSource(inFileName), result);
              }
              else
              {
                transformer.transform(new StreamSource(inFileName),
                                      strResult);
              }
            }
          }
          else
          {
            StringReader reader =
              new StringReader("<?xml version=\"1.0\"?> <doc/>");

            transformer.transform(new StreamSource(reader), strResult);
          }
        }
        else
        {
          diagnosticsWriter.println(
            XSLMessages.createMessage(
          doExit(-1);
        }
        
	if (null != outFileName && strResult!=null)
	{
   	  java.io.OutputStream out = strResult.getOutputStream();
   	  java.io.Writer writer = strResult.getWriter();
   	  try
   	  {
      	    if (out != null) out.close();
      	    if (writer != null) writer.close();
   	  }
   	  catch(java.io.IOException ie) {}
	}        

        long stop = System.currentTimeMillis();
        long millisecondsDuration = stop - start;

        if (doDiag)
        {
        	Object[] msgArgs = new Object[]{ inFileName, xslFileName, new Long(millisecondsDuration) };
        	String msg = XSLMessages.createMessage("diagTiming", msgArgs);
        	diagnosticsWriter.println('\n');
          	diagnosticsWriter.println(msg);
        }
          
      }
      catch (Throwable throwable)
      {
        while (throwable
               instanceof org.apache.xml.utils.WrappedRuntimeException)
        {
          throwable =
            ((org.apache.xml.utils.WrappedRuntimeException) throwable).getException();
        }

        if ((throwable instanceof NullPointerException)
                || (throwable instanceof ClassCastException))
          doStackDumpOnError = true;

        diagnosticsWriter.println();

        if (doStackDumpOnError)
          throwable.printStackTrace(dumpWriter);
        else
        {
          DefaultErrorHandler.printLocation(diagnosticsWriter, throwable);
          diagnosticsWriter.println(
            XSLMessages.createMessage(XSLTErrorResources.ER_XSLT_ERROR, null)
            + " (" + throwable.getClass().getName() + "): "
            + throwable.getMessage());
        }

        if (null != dumpFileName)
        {
          dumpWriter.close();
        }

        doExit(-1);
      }

      if (null != dumpFileName)
      {
        dumpWriter.close();
      }

      if (null != diagnosticsWriter)
      {

      }

    }
  }
  
  /** It is _much_ easier to debug under VJ++ if I can set a single breakpoint 
   * before this blows itself out of the water...
   * (I keep checking this in, it keeps vanishing. Grr!)
   * */
  static void doExit(int i)
  {
    System.exit(i);
  }
  
  /**
   * Wait for a return key to continue
   * 
   * @param resbundle The resource bundle
   */
  private static void waitForReturnKey(ResourceBundle resbundle)
  {
    System.out.println(resbundle.getString("xslProc_return_to_continue"));
    try
    {
      while (System.in.read() != '\n');
    }
    catch (java.io.IOException e) { }  
  }
  
  /**
   * Print a message if an option cannot be used with -XSLTC.
   *
   * @param option The option String
   */
  private static void printInvalidXSLTCOption(String option)
  {
    System.err.println(XSLMessages.createMessage("xslProc_invalid_xsltc_option", new Object[]{option}));
  }
  
  /**
   * Print a message if an option can only be used with -XSLTC.
   *
   * @param option The option String
   */
  private static void printInvalidXalanOption(String option)
  {
    System.err.println(XSLMessages.createMessage("xslProc_invalid_xalan_option", new Object[]{option}));
  }
}
