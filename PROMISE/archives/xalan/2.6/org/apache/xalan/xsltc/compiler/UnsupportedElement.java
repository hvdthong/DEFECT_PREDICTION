package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

/**
 * @author Morten Jorgensen
 */
final class UnsupportedElement extends SyntaxTreeNode {

    private Vector _fallbacks = null;
    private ErrorMsg _message = null;
    private boolean _isExtension = false;

    /**
     * Basic consutrcor - stores element uri/prefix/localname
     */
    public UnsupportedElement(String uri, String prefix, String local, boolean isExtension) {
	super(uri, prefix, local);
	_isExtension = isExtension;
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
     * Scan and process all fallback children of the unsupported element.
     */
    private void processFallbacks(Parser parser) {

	Vector children = getContents();
	if (children != null) {
	    final int count = children.size();
	    for (int i = 0; i < count; i++) {
		SyntaxTreeNode child = (SyntaxTreeNode)children.elementAt(i);
		if (child instanceof Fallback) {
		    Fallback fallback = (Fallback)child;
		    fallback.activate();
		    fallback.parseContents(parser);
		    if (_fallbacks == null) {
		    	_fallbacks = new Vector();
		    }
		    _fallbacks.addElement(child);
		}
	    }
	}
    }

    /**
     * Find any fallback in the descendant nodes; then activate & parse it
     */
    public void parseContents(Parser parser) {
    	processFallbacks(parser);
    }

    /**
     * Run type check on the fallback element (if any).
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {	
	if (_fallbacks != null) {
	    int count = _fallbacks.size();
	    for (int i = 0; i < count; i++) {
	        Fallback fallback = (Fallback)_fallbacks.elementAt(i);
	        fallback.typeCheck(stable);
	    }
	}
	return Type.Void;
    }

    /**
     * Translate the fallback element (if any).
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	if (_fallbacks != null) {
	    int count = _fallbacks.size();
	    for (int i = 0; i < count; i++) {
	        Fallback fallback = (Fallback)_fallbacks.elementAt(i);
	        fallback.translate(classGen, methodGen);
	    }
	}
	else {		
	    ConstantPoolGen cpg = classGen.getConstantPool();
	    InstructionList il = methodGen.getInstructionList();
	    
	    final int unsupportedElem = cpg.addMethodref(BASIS_LIBRARY_CLASS, "unsupported_ElementF",
                                                         "(" + STRING_SIG + "Z)V");	 
	    il.append(new PUSH(cpg, getQName().toString()));
	    il.append(new PUSH(cpg, _isExtension));
	    il.append(new INVOKESTATIC(unsupportedElem));		
	}
    }
}
