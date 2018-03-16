package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.dom.Axis;

final class LastCall extends FunctionCall {

    private int _type = -1;

    public LastCall(QName fname) {
	super(fname);
    }

    public LastCall(QName fname, int type) {
	this(fname);
	_type = type;
    }

    public boolean hasPositionCall() {
	return true;
    }

    public boolean hasLastCall() {
	return true;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {

	final InstructionList il = methodGen.getInstructionList();
	final ConstantPoolGen cpg = classGen.getConstantPool();

	boolean lastChild = false;

	if (getParent() instanceof Expression) {
	    if (getParent().getParent() instanceof Predicate) {
		Predicate pred = (Predicate)getParent().getParent();
		_type = pred.getPosType();
		if ((_type==DOM.ELEMENT) || (_type==DOM.ATTRIBUTE)) _type = -1;
	    }
	}

	if (getParent() instanceof Predicate) {
	    _type = ((Predicate)getParent()).getPosType();
	    if ((_type==DOM.ELEMENT) || (_type==DOM.ATTRIBUTE)) _type = -1;
	    if (getParent().getParent() instanceof Step) {
		lastChild = true;
	    }
	}

	if (methodGen instanceof CompareGenerator) {
	    il.append(((CompareGenerator)methodGen).loadLastNode());
	}
	else if (classGen.isExternal()) {
	    il.append(new ILOAD(LAST_INDEX));
	}
	else if (_type == -1) {
	    final int last = cpg.addInterfaceMethodref(NODE_ITERATOR,
						       "getLast", 
						       "()I");
	    final int git = cpg.addInterfaceMethodref(DOM_INTF,
						      "getTypedAxisIterator", 
						      "(II)"+NODE_ITERATOR_SIG);
	    final int start = cpg.addInterfaceMethodref(NODE_ITERATOR,
							"setStartNode", 
							"(I)"+
							NODE_ITERATOR_SIG);
	    if (lastChild) {
		il.append(methodGen.loadDOM());
		il.append(new PUSH(cpg, Axis.CHILD));
		il.append(new PUSH(cpg, DOM.ELEMENT));
		il.append(new INVOKEINTERFACE(git, 3));
		il.append(methodGen.loadCurrentNode());
		il.append(new INVOKEINTERFACE(start, 2));
	    }
	    else {
		il.append(methodGen.loadIterator());
	    }
	    il.append(new INVOKEINTERFACE(last, 1));
	}
	else {
	    final int last = cpg.addInterfaceMethodref(DOM_INTF,
						       "getTypedLast",
						       "(II)I");
	    il.append(methodGen.loadDOM());
	    il.append(new PUSH(cpg, _type));
	    il.append(methodGen.loadContextNode());
	    il.append(new INVOKEINTERFACE(last, 3));

	}
    }
}
