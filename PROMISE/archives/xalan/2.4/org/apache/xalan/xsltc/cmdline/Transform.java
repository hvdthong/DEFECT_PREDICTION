package org.apache.xalan.xsltc.cmdline;

import java.io.*;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.TransletOutputHandler;

import org.apache.xalan.xsltc.runtime.*;
import org.apache.xalan.xsltc.dom.DOMImpl;
import org.apache.xalan.xsltc.dom.DOMBuilder;
import org.apache.xalan.xsltc.dom.Axis;
import org.apache.xalan.xsltc.dom.DTDMonitor;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

import org.apache.xalan.xsltc.runtime.output.*;

final public class Transform {

    static private boolean _useOldOutputSystem = false;

    private TransletOutputHandler _handler;

    private String  _fileName;
    private String  _className;
    private String  _jarFileSrc;
    private boolean _isJarFileSpecified = false;
    private Vector  _params = null;
    private boolean _uri, _debug;
    private int     _iterations;

    private static boolean _allowExit = true;

    public Transform(String className, String fileName,
		     boolean uri, boolean debug, int iterations) {
	_fileName = fileName;
	_className = className;
	_uri = uri;
	_debug = debug;
	_iterations = iterations;
    }

    public void setParameters(Vector params) {
	_params = params;
    }

    private void setJarFileInputSrc(boolean flag,  String jarFile) {
	_isJarFileSpecified = flag;
	_jarFileSrc = jarFile;	
    }

    private Class loadTranslet(String name) throws ClassNotFoundException {
	try {
	    return Class.forName(name);
	}
	catch (ClassNotFoundException e) {
	}

	TransletLoader loader = new TransletLoader();
	return loader.loadTranslet(name);
    }

    private void doTransform() {
	try {
	    
	    final Class clazz = loadTranslet(_className);
	    final Translet translet = (Translet)clazz.newInstance();

	    final SAXParserFactory factory = SAXParserFactory.newInstance();
	    try {
		factory.setFeature(Constants.NAMESPACE_FEATURE,true);
	    }
	    catch (Exception e) {
		factory.setNamespaceAware(true);
	    }
	    final SAXParser parser = factory.newSAXParser();
	    final XMLReader reader = parser.getXMLReader();

	    final DOMImpl dom = new DOMImpl();
	    DOMBuilder builder = dom.getBuilder();
	    reader.setContentHandler(builder);

	    try {
		reader.setProperty(prop, builder);
	    }
	    catch (SAXException e) {
	    }
	    
	    final DTDMonitor dtdMonitor = new DTDMonitor(reader);
	    AbstractTranslet _translet = (AbstractTranslet)translet;
	    dom.setDocumentURI(_fileName);
	    if (_uri)
		reader.parse(_fileName);
	    else
		reader.parse(new File(_fileName).toURL().toExternalForm());

	    builder = null;

	    dtdMonitor.buildIdIndex(dom, 0, _translet);
	    _translet.setDTDMonitor(dtdMonitor);

	    int n = _params.size();
	    for (int i = 0; i < n; i++) {
		Parameter param = (Parameter) _params.elementAt(i);
		translet.addParameter(param._name, param._value);
	    }

	    TransletOutputHandlerFactory tohFactory = 
		TransletOutputHandlerFactory.newInstance();
	    tohFactory.setOutputType(TransletOutputHandlerFactory.STREAM);
	    tohFactory.setEncoding(_translet._encoding);
	    tohFactory.setOutputMethod(_translet._method);

	    if (_iterations == -1) {
		translet.transform(dom, _useOldOutputSystem ?
					tohFactory.getOldTransletOutputHandler() :
					tohFactory.getTransletOutputHandler());
	    }
	    else if (_iterations > 0) {
		long mm = System.currentTimeMillis();
		for (int i = 0; i < _iterations; i++) {
		    translet.transform(dom, _useOldOutputSystem ?
					    tohFactory.getOldTransletOutputHandler() :
					    tohFactory.getTransletOutputHandler());
		}
		mm = System.currentTimeMillis() - mm;

		System.err.println("\n<!--");
		System.err.println("  transform  = " + (mm / _iterations) + " ms");
		System.err.println("  throughput = " + (1000.0 / (mm / _iterations)) + " tps");
		System.err.println("-->");
	    }
	}
	catch (TransletException e) {
	    if (_debug) e.printStackTrace();
	    System.err.println(ErrorMsg.getTransletErrorMessage()+
			       e.getMessage());
	    if (_allowExit) System.exit(-1);	    
	}
	catch (RuntimeException e) {
	    if (_debug) e.printStackTrace();
	    System.err.println(ErrorMsg.getTransletErrorMessage()+
			       e.getMessage());
	    if (_allowExit) System.exit(-1);
	}
	catch (FileNotFoundException e) {
	    if (_debug) e.printStackTrace();
	    ErrorMsg err = new ErrorMsg(ErrorMsg.FILE_NOT_FOUND_ERR, _fileName);
	    System.err.println(ErrorMsg.getTransletErrorMessage()+
			       err.toString());
	    if (_allowExit) System.exit(-1);
	}
	catch (MalformedURLException e) {
	    if (_debug) e.printStackTrace();
	    ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_URI_ERR, _fileName);
	    System.err.println(ErrorMsg.getTransletErrorMessage()+
			       err.toString());
	    if (_allowExit) System.exit(-1);
	}
	catch (ClassNotFoundException e) {
	    if (_debug) e.printStackTrace();
	    ErrorMsg err= new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR,_className);
	    System.err.println(ErrorMsg.getTransletErrorMessage()+
			       err.toString());
	    if (_allowExit) System.exit(-1);
	}
        catch (UnknownHostException e) {
	    if (_debug) e.printStackTrace();
	    ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_URI_ERR, _fileName);
	    System.err.println(ErrorMsg.getTransletErrorMessage()+
			       err.toString());
	    if (_allowExit) System.exit(-1);
        }
	catch (SAXException e) {
	    Exception ex = e.getException();
	    if (_debug) {
		if (ex != null) ex.printStackTrace();
		e.printStackTrace();
	    }
	    System.err.print(ErrorMsg.getTransletErrorMessage());
	    if (ex != null)
		System.err.println(ex.getMessage());
	    else
		System.err.println(e.getMessage());
	    if (_allowExit) System.exit(-1);
	}
	catch (Exception e) {
	    if (_debug) e.printStackTrace();
	    System.err.println(ErrorMsg.getTransletErrorMessage()+
			       e.getMessage());
	    if (_allowExit) System.exit(-1);
	}
    }

    public static void printUsage() {
	System.err.println(new ErrorMsg(ErrorMsg.TRANSFORM_USAGE_STR));
	if (_allowExit) System.exit(-1);
    }

    public static void main(String[] args) {
	try {
	    if (args.length > 0) {
		int i;
		int iterations = -1;
		boolean uri = false, debug = false;
		boolean isJarFileSpecified = false;
		String  jarFile = null;

		for (i = 0; i < args.length && args[i].charAt(0) == '-'; i++) {
		    if (args[i].equals("-u")) {
			uri = true;
		    }
		    else if (args[i].equals("-x")) {
			debug = true;
		    }
		    else if (args[i].equals("-s")) {
			_allowExit = false;
		    }
		    else if (args[i].equals("-j")) {
			isJarFileSpecified = true;	
			jarFile = args[++i];
		    }
		    else if (args[i].equals("-e")) {
			_useOldOutputSystem = true;
		    }
		    else if (args[i].equals("-n")) {
			try {
			    iterations = Integer.parseInt(args[++i]);
			}
			catch (NumberFormatException e) {
			}
		    }
		    else {
			printUsage();
		    }
		}

		if (args.length - i < 2) printUsage();

		Transform handler = new Transform(args[i+1], args[i], uri,
		    debug, iterations);
		handler.setJarFileInputSrc(isJarFileSpecified,	jarFile);

		Vector params = new Vector();
		for (i += 2; i < args.length; i++) {
		    final int equal = args[i].indexOf('=');
		    if (equal > 0) {
			final String name  = args[i].substring(0, equal);
			final String value = args[i].substring(equal+1);
			params.addElement(new Parameter(name, value));
		    }
		    else {
			printUsage();
		    }
		}

		if (i == args.length) {
		    handler.setParameters(params);
		    handler.doTransform();
		    if (_allowExit) System.exit(0);
		}
	    } else {
		printUsage();
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
