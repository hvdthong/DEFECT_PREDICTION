package org.apache.xalan.xsltc.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Enumeration;

import javax.xml.parsers.*;

import org.xml.sax.*;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.*;

import org.apache.bcel.generic.*;

final class Include extends TopLevelElement {

    private Stylesheet _included = null;

    public Stylesheet getIncludedStylesheet() {
	return _included;
    }

    public void parseContents(final Parser parser) {
	final Stylesheet context = parser.getCurrentStylesheet();

	String docToLoad = getAttribute("href");
	try {
	    if (context.checkForLoop(docToLoad)) {
		final int errno = ErrorMsg.CIRCULAR_INCLUDE_ERR;
		final ErrorMsg msg = new ErrorMsg(errno, docToLoad, this);
		parser.reportError(Constants.FATAL, msg);
		return;
	    }

	    String currLoadedDoc = context.getSystemId();
	    SourceLoader loader = context.getSourceLoader();
	    InputSource input = null;

	    if (loader != null) {
		final XSLTC xsltc = parser.getXSLTC();
		input = loader.loadSource(docToLoad, currLoadedDoc, xsltc);
	    }
	    else {
		if ((currLoadedDoc != null) && (currLoadedDoc.length() > 0)) {
		    File file = new File(currLoadedDoc);
		    if (file.exists()) {
		        currLoadedDoc = "file:" + file.getCanonicalPath();
		    }
		    final URL url = new URL(new URL(currLoadedDoc), docToLoad);
		    docToLoad = url.toString();
		    input = new InputSource(docToLoad);
		}
		else {
		    File file = new File(System.getProperty("user.dir"),
			docToLoad);
		    if (file.exists()) {
			docToLoad = "file:" + file.getCanonicalPath();
		    }
		    else {
			throw new FileNotFoundException(
			  "Could not load file " + docToLoad);
		    }
		    input = new InputSource(docToLoad);
		}
	    }

	    if (input == null) {
		final ErrorMsg msg = 
		    new ErrorMsg(ErrorMsg.FILE_NOT_FOUND_ERR, docToLoad, this);
		parser.reportError(Constants.FATAL, msg);
		return;
	    }

	    final SyntaxTreeNode root = parser.parse(input);
	    if (root == null) return;
	    _included = parser.makeStylesheet(root);
	    if (_included == null) return;

	    _included.setSourceLoader(loader);
	    _included.setSystemId(docToLoad);
	    _included.setParentStylesheet(context);
	    _included.setIncludingStylesheet(context);
	    _included.setTemplateInlining(context.getTemplateInlining());

	    final int precedence = context.getImportPrecedence();
	    _included.setImportPrecedence(precedence);
	    parser.setCurrentStylesheet(_included);
	    _included.parseContents(parser);

	    final Enumeration elements = _included.elements();
	    final Stylesheet topStylesheet = parser.getTopLevelStylesheet();
	    while (elements.hasMoreElements()) {
		final Object element = elements.nextElement();
		if (element instanceof TopLevelElement) {
		    if (element instanceof Variable) {
			topStylesheet.addVariable((Variable) element);
		    }
		    else if (element instanceof Param) {
			topStylesheet.addParam((Param) element);
		    }
		    else {
			topStylesheet.addElement((TopLevelElement) element);
		    }
		}
	    }
	}
	catch (FileNotFoundException e) {
	    context.setSystemId(getAttribute("href"));

	    final ErrorMsg msg = 
		new ErrorMsg(ErrorMsg.FILE_NOT_FOUND_ERR, docToLoad, this);
	    parser.reportError(Constants.FATAL, msg);
	}
	catch (MalformedURLException e) {
	    context.setSystemId(getAttribute("href"));

	    final ErrorMsg msg = 
		new ErrorMsg(ErrorMsg.FILE_NOT_FOUND_ERR, docToLoad, this);
	    parser.reportError(Constants.FATAL, msg);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
	    parser.setCurrentStylesheet(context);
	}
    }
    
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return Type.Void;
    }
    
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }
}
