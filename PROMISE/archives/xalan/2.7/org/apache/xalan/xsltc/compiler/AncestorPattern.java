package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Erwin Bolwidt <ejb@klomp.org>
 */
final class AncestorPattern extends RelativePathPattern {

    private final RelativePathPattern _right;
    private InstructionHandle _loop;
		
    public AncestorPattern(RelativePathPattern right) {
	this(null, right);
    }

    public AncestorPattern(Pattern left, RelativePathPattern right) {
	_left = left;
	(_right = right).setParent(this);
	if (left != null) {
	    left.setParent(this);
	}
    }
	
    public InstructionHandle getLoopHandle() {
	return _loop;
    }

    public void setParser(Parser parser) {
	super.setParser(parser);
	if (_left != null) {
	    _left.setParser(parser);
	}
	_right.setParser(parser);
    }
    
    public boolean isWildcard() {
	return false;
    }
	
    public StepPattern getKernelPattern() {
	return _right.getKernelPattern();
    }

    public void reduceKernelPattern() {
	_right.reduceKernelPattern();
    }
	
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_left != null) {
	    _left.typeCheck(stable);
	}
	return _right.typeCheck(stable);
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	InstructionHandle parent;
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	/* 
	 * The scope of this local var must be the entire method since
	 * a another pattern may decide to jump back into the loop
	 */
	final LocalVariableGen local =
	    methodGen.addLocalVariable2("app", Util.getJCRefType(NODE_SIG),
					il.getEnd());

	final org.apache.bcel.generic.Instruction loadLocal =
	    new ILOAD(local.getIndex());
	final org.apache.bcel.generic.Instruction storeLocal =
	    new ISTORE(local.getIndex());

	if (_right instanceof StepPattern) {
	    il.append(DUP);
	    il.append(storeLocal);
	    _right.translate(classGen, methodGen);
	    il.append(methodGen.loadDOM());
	    il.append(loadLocal);
	}
	else {
	    _right.translate(classGen, methodGen);

	    if (_right instanceof AncestorPattern) {
		il.append(methodGen.loadDOM());
		il.append(SWAP);
	    }
	}

	if (_left != null) {
	    final int getParent = cpg.addInterfaceMethodref(DOM_INTF,
							    GET_PARENT,
							    GET_PARENT_SIG);
	    parent = il.append(new INVOKEINTERFACE(getParent, 2));
	    
	    il.append(DUP);
	    il.append(storeLocal);
	    _falseList.add(il.append(new IFLT(null)));
	    il.append(loadLocal);

	    _left.translate(classGen, methodGen);

	    final SyntaxTreeNode p = getParent();
	    if (p == null || p instanceof Instruction ||
		p instanceof TopLevelElement) 
	    {
	    }
	    else {
		il.append(loadLocal);
	    }

	    final BranchHandle exit = il.append(new GOTO(null));
	    _loop = il.append(methodGen.loadDOM());
	    il.append(loadLocal);
	    local.setEnd(_loop);
	    il.append(new GOTO(parent));
	    exit.setTarget(il.append(NOP));
	    _left.backPatchFalseList(_loop);

	    _trueList.append(_left._trueList);	
	}
	else {
	    il.append(POP2);
	}
	
	/* 
	 * If _right is an ancestor pattern, backpatch this pattern's false
	 * list to the loop that searches for more ancestors.
	 */
	if (_right instanceof AncestorPattern) {
	    final AncestorPattern ancestor = (AncestorPattern) _right;
	}

	_trueList.append(_right._trueList);
	_falseList.append(_right._falseList);
    }

    public String toString() {
	return "AncestorPattern(" + _left + ", " + _right + ')';
    }
}
