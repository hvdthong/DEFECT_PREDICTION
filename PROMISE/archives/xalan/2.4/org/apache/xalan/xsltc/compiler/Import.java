package org.apache.xalan.xsltc.compiler;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Enumeration;

import javax.xml.parsers.*;

import org.xml.sax.*;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.*;

import org.apache.bcel.generic.*;

final class Import extends TopLevelElement {

    private Stylesheet _imported = null;

    public Stylesheet getImportedStylesheet() {
	return _imported;
    }

    public void parseContents(final Parser parser) {
	final Stylesheet context = parser.getCurrentStylesheet();

	try {
	    String docToLoad = getAttribute("href");
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
		File file = new File(currLoadedDoc);
		if (file.exists()) currLoadedDoc = "file:"+currLoadedDoc;
		final URL url = new URL(new URL(currLoadedDoc), docToLoad);
		docToLoad = url.toString();
		input = new InputSource(docToLoad);
	    }

	    if (input == null) {
		final ErrorMsg msg = 
		    new ErrorMsg(ErrorMsg.FILE_NOT_FOUND_ERR, docToLoad, this);
		parser.reportError(Constants.FATAL, msg);
		return;
	    }

	    SyntaxTreeNode root = parser.parse(input);

	    if (root == null) return;
	    _imported = parser.makeStylesheet(root);
	    if (_imported == null) return;

	    _imported.setSourceLoader(loader);
	    _imported.setSystemId(docToLoad);
	    _imported.setParentStylesheet(context);
	    _imported.setImportingStylesheet(context);

	    final int currPrecedence = parser.getCurrentImportPrecedence();
	    final int nextPrecedence = parser.getNextImportPrecedence();
	    _imported.setImportPrecedence(currPrecedence);
	    context.setImportPrecedence(nextPrecedence);
	    parser.setCurrentStylesheet(_imported);
	    _imported.parseContents(parser);

	    final Enumeration elements = _imported.elements();
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
