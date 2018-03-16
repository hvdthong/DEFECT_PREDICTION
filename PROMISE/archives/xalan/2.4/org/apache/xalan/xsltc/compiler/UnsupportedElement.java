package org.apache.xalan.xsltc.compiler;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import javax.xml.parsers.*;

import org.xml.sax.*;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;

import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class UnsupportedElement extends SyntaxTreeNode {

    private Fallback _fallback = null;
    private ErrorMsg _message = null;

    /**
     * Basic consutrcor - stores element uri/prefix/localname
     */
    public UnsupportedElement(String uri, String prefix, String local) {
	super(uri, prefix, local);
    }

    /**
     * There are different categories of unsupported elements (believe it
     * or not): there are elements within the XSLT namespace (these would
     * be elements that are not yet implemented), there are extensions of
     * other XSLT processors and there are unrecognised extension elements
     * of this XSLT processor. The error message passed to this method
     * should describe the unsupported element itself and what category
     * the element belongs in.
     */
    public void setErrorMessage(ErrorMsg message) {
	_message = message;
    }

    /**
     * Displays the contents of this element
     */
    public void display(int indent) {
	indent(indent);
	Util.println("Unsupported element = " + _qname.getNamespace() +
		     ":" + _qname.getLocalPart());
	displayContents(indent + IndentIncrement);
    }


    /**
     * Scan all descendants and find the first xsl:fallback element (if any)
     */
    private SyntaxTreeNode findFallback(SyntaxTreeNode root) {

	if (root == null) return null;

	if (root instanceof Fallback) return((Fallback)root);

	Vector children = root.getContents();
	if (children != null) {
	    final int count = children.size();
	    for (int i = 0; i < count; i++) {
		SyntaxTreeNode child = (SyntaxTreeNode)children.elementAt(i);
		SyntaxTreeNode node = findFallback(child);
		if (node != null) return node;
	    }
	}
	return null;
    }

    /**
     * Find any fallback in the descendant nodes; then activate & parse it
     */
    public void parseContents(Parser parser) {
	_fallback = (Fallback)findFallback(this);
	if (_fallback != null) {
	    _fallback.activate();
	    _fallback.parseContents(parser);
	}
    }

    /**
     * Run type check on the fallback element (if any).
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_fallback == null) {
	    throw new TypeCheckError(_message);
	}
	return(_fallback.typeCheck(stable));
    }

    /**
     * Translate the fallback element (if any). The stylesheet should never
     * be compiled if an unsupported element does not have a fallback element,
     * so this method should never be called unless _fallback != null
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	if (_fallback != null) _fallback.translate(classGen, methodGen);
    }
}
