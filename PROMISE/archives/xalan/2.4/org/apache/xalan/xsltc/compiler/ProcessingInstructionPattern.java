package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.dom.Axis;

final class ProcessingInstructionPattern extends StepPattern {

    private String _name = null;
    private boolean _typeChecked = false;

    /**
     * Handles calls with no parameter (current node is implicit parameter).
     */
    public ProcessingInstructionPattern(String name) {
	super(Axis.CHILD, DOM.PROCESSING_INSTRUCTION, null);
	_name = name;
    }

    /**
     *
     */
    public String toString() {
	if (_predicates == null)
	    return "processing-instruction("+_name+")";
	else
	    return "processing-instruction("+_name+")"+_predicates;
    }

    public void reduceKernelPattern() {
	_typeChecked = true;
    }

    public boolean isWildcard() {
	return false;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (hasPredicates()) {
	    final int n = _predicates.size();
	    for (int i = 0; i < n; i++) {
		final Predicate pred = (Predicate)_predicates.elementAt(i);
		pred.typeCheck(stable);
	    }
	}
	return Type.NodeSet;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	int gname = cpg.addInterfaceMethodref(DOM_INTF,
					      "getNodeName",
					      "(I)Ljava/lang/String;");
	int cmp = cpg.addMethodref(STRING_CLASS,
				   "equals", "(Ljava/lang/Object;)Z");

	il.append(methodGen.loadCurrentNode());
	il.append(SWAP);

	il.append(methodGen.storeCurrentNode());

	if (!_typeChecked) {
	    il.append(methodGen.loadCurrentNode());
	    final int getType = cpg.addInterfaceMethodref(DOM_INTF,
							  "getType", "(I)I");
	    il.append(methodGen.loadDOM());
	    il.append(methodGen.loadCurrentNode());
	    il.append(new INVOKEINTERFACE(getType, 2));
	    il.append(new PUSH(cpg, DOM.PROCESSING_INSTRUCTION));
	    _falseList.add(il.append(new IF_ICMPEQ(null)));
	}

	il.append(new PUSH(cpg, _name));
	il.append(methodGen.loadDOM());
	il.append(methodGen.loadCurrentNode());
	il.append(new INVOKEINTERFACE(gname, 2));
	il.append(new INVOKEVIRTUAL(cmp));
	_falseList.add(il.append(new IFEQ(null)));
		
	if (hasPredicates()) {
	    final int n = _predicates.size();
	    for (int i = 0; i < n; i++) {
		Predicate pred = (Predicate)_predicates.elementAt(i);
		Expression exp = pred.getExpr();
		exp.translateDesynthesized(classGen, methodGen);
		_trueList.append(exp._trueList);
		_falseList.append(exp._falseList);
	    }
	}

	InstructionHandle restore;
	restore = il.append(methodGen.storeCurrentNode());
	backPatchTrueList(restore);
	BranchHandle skipFalse = il.append(new GOTO(null));

	restore = il.append(methodGen.storeCurrentNode());
	backPatchFalseList(restore);
	_falseList.add(il.append(new GOTO(null)));

	skipFalse.setTarget(il.append(NOP));
    }
}
