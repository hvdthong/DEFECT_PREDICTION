package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;
import org.apache.xalan.xsltc.DOM;

final class PositionCall extends FunctionCall {

    private int _type = -1;

    public PositionCall(QName fname) {
	super(fname);
    }

    public PositionCall(QName fname, int type) {
	this(fname);
	_type = type;
    }

    public boolean hasPositionCall() {
	return true;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {

	final InstructionList il = methodGen.getInstructionList();

	SyntaxTreeNode parent = getParent();
	SyntaxTreeNode granny = parent.getParent();

	if ((parent instanceof Expression) && (granny instanceof Predicate)) {
	    _type = ((Predicate)granny).getPosType();
	}
	else {
	    while ((granny != null) && !(granny instanceof StepPattern)) {
		parent = granny;
		granny = granny.getParent();
	    }
	    if ((parent instanceof Predicate) &&
		(granny instanceof StepPattern)){
		_type = ((StepPattern)granny).getNodeType();
	    }
	}

	if (methodGen instanceof CompareGenerator) {
	    il.append(((CompareGenerator)methodGen).loadCurrentNode());
	}
	else if (methodGen instanceof TestGenerator) {
	    il.append(new ILOAD(POSITION_INDEX));
	}
	else if (_type == -1) {
	    final ConstantPoolGen cpg = classGen.getConstantPool();
	    final int getPosition = cpg.addInterfaceMethodref(NODE_ITERATOR,
							      "getPosition", 
							      "()I");
	    il.append(methodGen.loadIterator());
	    il.append(new INVOKEINTERFACE(getPosition, 1));
	}
	else {
	    final ConstantPoolGen cpg = classGen.getConstantPool();
	    final int pos = cpg.addInterfaceMethodref(DOM_INTF,
						      "getTypedPosition",
						      "(II)I");
	    il.append(methodGen.loadDOM());
	    il.append(new PUSH(cpg, _type));
	    il.append(methodGen.loadContextNode());
	    il.append(new INVOKEINTERFACE(pos, 3));
	}
    }
}
