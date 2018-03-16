package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class DocumentCall extends FunctionCall {

    private Expression _uri = null;
    private Expression _base = null;
    private Type       _uriType;

    /**
     * Default function call constructor
     */
    public DocumentCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    /**
     * Type checks the arguments passed to the document() function. The first
     * argument can be any type (we must cast it to a string) and contains the
     * URI of the document
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final int ac = argumentCount();
	if ((ac < 1) || (ac > 2)) {
	    ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, this);
	    throw new TypeCheckError(msg);
	}

	_uri = argument(0);
	if (_uri instanceof LiteralExpr) {
	    LiteralExpr expr = (LiteralExpr)_uri;
	    if (expr.getValue().equals(EMPTYSTRING)) {
		Stylesheet stylesheet = getStylesheet();
		if (stylesheet == null) {
		    ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, this);
		    throw new TypeCheckError(msg);
		}
		_uri = new LiteralExpr(stylesheet.getSystemId(), EMPTYSTRING);
	    }
	}

	_uriType = _uri.typeCheck(stable);
	if ((_uriType != Type.NodeSet) && (_uriType != Type.String)) {
	    _uri = new CastExpr(_uri, Type.String);
	}

	if (ac == 2) {
	    _base = argument(1);
	    final Type baseType = _base.typeCheck(stable);
	    
	    if (baseType.identicalTo(Type.Node)) {
		_base = new CastExpr(_base, Type.NodeSet);
	    }
	    else if (baseType.identicalTo(Type.NodeSet)) {
	    }
	    else {
		ErrorMsg msg = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, this);
		throw new TypeCheckError(msg);
	    }
	}

	return _type = Type.NodeSet;
    }
	
    /**
     * Translates the document() function call to a call to LoadDocument()'s
     * static method document().
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int domField = cpg.addFieldref(classGen.getClassName(),
					     DOM_FIELD,
					     DOM_INTF_SIG);
	final String docParamList =
	    "("+OBJECT_SIG+STRING_SIG+STRING_SIG+TRANSLET_SIG+DOM_INTF_SIG+")"+
	    NODE_ITERATOR_SIG;
	final int docIdx = cpg.addMethodref(LOAD_DOCUMENT_CLASS,
					    "document", docParamList);

	final int uriIdx = cpg.addInterfaceMethodref(DOM_INTF,
						     "getDocumentURI",
						     "(I)"+STRING_SIG);

	final int nextIdx = cpg.addInterfaceMethodref(NODE_ITERATOR,
						      NEXT, NEXT_SIG);

	_uri.translate(classGen, methodGen);
	if (_uriType == Type.NodeSet)
	    _uri.startResetIterator(classGen, methodGen);

	if (_base != null) {
		il.append(methodGen.loadDOM());
	    _base.translate(classGen, methodGen);
	    il.append(new INVOKEINTERFACE(nextIdx, 1));
	    il.append(new INVOKEINTERFACE(uriIdx, 2));
	}
	else {
	     if (_uriType == Type.NodeSet)
	     il.append(new PUSH(cpg,""));
	     else
	     il.append(new PUSH(cpg, getStylesheet().getSystemId())); 	     
	}
	il.append(new PUSH(cpg, getStylesheet().getSystemId()));

	il.append(classGen.loadTranslet());
	il.append(DUP);
	il.append(new GETFIELD(domField));
	il.append(new INVOKESTATIC(docIdx));
    }

}
