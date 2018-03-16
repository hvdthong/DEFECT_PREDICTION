package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class AbsolutePathPattern extends LocationPathPattern {

    public AbsolutePathPattern(RelativePathPattern left) {
	_left = left;
	if (left != null) {
	    left.setParent(this);
	}
    }

    public void setParser(Parser parser) {
	super.setParser(parser);
	if (_left != null)
	    _left.setParser(parser);
    }
    
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return _left == null ? Type.Root : _left.typeCheck(stable);
    }

    public boolean isWildcard() {
	return false;
    }
	
    public StepPattern getKernelPattern() {
	return _left != null ? _left.getKernelPattern() : null;
    }
	
    public void reduceKernelPattern() {
	_left.reduceKernelPattern();
    }
	
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (_left != null) {
	    if (_left instanceof StepPattern) {
		final LocalVariableGen local = 
		    methodGen.addLocalVariable2("apptmp", 
						Util.getJCRefType(NODE_SIG),
						il.getEnd());
		il.append(DUP);
		il.append(new ISTORE(local.getIndex()));
		_left.translate(classGen, methodGen);
		il.append(methodGen.loadDOM());
		local.setEnd(il.append(new ILOAD(local.getIndex())));
		methodGen.removeLocalVariable(local);
	    }
	    else {
		_left.translate(classGen, methodGen);
	    }
	}

	final int getParent = cpg.addInterfaceMethodref(DOM_INTF,
							GET_PARENT,
							GET_PARENT_SIG);
	final int getType = cpg.addInterfaceMethodref(DOM_INTF,
						      "getType", "(I)I");

	InstructionHandle begin = il.append(methodGen.loadDOM());
	il.append(SWAP);
	il.append(new INVOKEINTERFACE(getParent, 2));
	if (_left instanceof AncestorPattern) {	
	    il.append(methodGen.loadDOM());
	    il.append(SWAP);
	}
	il.append(new INVOKEINTERFACE(getType, 2));
	il.append(new PUSH(cpg, DOM.ROOT));
	
	final BranchHandle skip = il.append(new IF_ICMPEQ(null));
	_falseList.add(il.append(new GOTO_W(null)));
	skip.setTarget(il.append(NOP));

	if (_left != null) {
	    _left.backPatchTrueList(begin);
	    
	    /*
	     * If _left is an ancestor pattern, backpatch this pattern's false
	     * list to the loop that searches for more ancestors.
	     */
	    if (_left instanceof AncestorPattern) {
		final AncestorPattern ancestor = (AncestorPattern) _left;
	    }
	    _falseList.append(_left._falseList);
	}
    }
	
    public String toString() {
	return "absolutePathPattern(" + (_left != null ? _left.toString() : ")");
    }
}
