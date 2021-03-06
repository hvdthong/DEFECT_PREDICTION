package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.*;

import java.util.Vector;

import org.apache.bcel.generic.*;

abstract class Expression extends SyntaxTreeNode {
    /**
     * The type of this expression. It is set after calling 
     * <code>typeCheck()</code>.
     */
    protected Type _type;

    /**
     * True if this expression is of node-set type and its corresponding
     * iterator has been started or reset.
     */
    protected boolean _startReset = false;

    /**
     * Instruction handles that comprise the true list.
     */
    protected FlowList _trueList = new FlowList();

    /**
     * Instruction handles that comprise the false list.
     */
    protected FlowList _falseList = new FlowList();

    public Type getType() {
	return _type;
    }

    public abstract String toString();

    public boolean hasPositionCall() {
    }

    public boolean hasLastCall() {
	return false;
    }
		
    /**
     * Returns an object representing the compile-time evaluation 
     * of an expression. We are only using this for function-available
     * and element-available at this time.
     */
    public Object evaluateAtCompileTime() {
	return null;
    }

    /**
     * Type check all the children of this node.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return typeCheckContents(stable);
    }

    /**
     * Translate this node into JVM bytecodes.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	ErrorMsg msg = new ErrorMsg(ErrorMsg.NOT_IMPLEMENTED_ERR,
				    getClass(), this);
	getParser().reportError(FATAL, msg);
    }
	
    /**
     * Translate this node into a fresh instruction list.
     * The original instruction list is saved and restored.
     */
    public final InstructionList compile(ClassGenerator classGen,
					 MethodGenerator methodGen) {
	final InstructionList result, save = methodGen.getInstructionList();
	methodGen.setInstructionList(result = new InstructionList());
	translate(classGen, methodGen);
	methodGen.setInstructionList(save);
	return result;
    }

    /**
     * Redefined by expressions of type boolean that use flow lists.
     */
    public void translateDesynthesized(ClassGenerator classGen,
				       MethodGenerator methodGen) {
	translate(classGen, methodGen);
	if (_type instanceof BooleanType) {
	    desynthesize(classGen, methodGen);
	}
    }

    /**
     * Expects an object on the stack and if this object can be proven
     * to be a node iterator then the iterator is reset or started
     * depending on the type of this expression.
     * If this expression is a var reference then the iterator 
     * is reset, otherwise it is started.
     */
    public void startResetIterator(ClassGenerator classGen,
				   MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (_startReset) {
	}
	_startReset = true;

	if (_type instanceof NodeSetType == false) {
	}

	if ( (this instanceof VariableRefBase) == false ) {
	    il.append(methodGen.loadContextNode());
	    il.append(methodGen.setStartNode());
	}
    }

    /**
     * Synthesize a boolean expression, i.e., either push a 0 or 1 onto the 
     * operand stack for the next statement to succeed. Returns the handle
     * of the instruction to be backpatched.
     */
    public void synthesize(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	_trueList.backPatch(il.append(ICONST_1));
	final BranchHandle truec = il.append(new GOTO_W(null));
	_falseList.backPatch(il.append(ICONST_0));
	truec.setTarget(il.append(NOP));
    }

    public void desynthesize(ClassGenerator classGen,
			     MethodGenerator methodGen) {
	final InstructionList il = methodGen.getInstructionList();
	_falseList.add(il.append(new IFEQ(null)));
    }

    public FlowList getFalseList() {
	return _falseList;
    }

    public FlowList getTrueList() {
	return _trueList;
    }

    public void backPatchFalseList(InstructionHandle ih) {
	_falseList.backPatch(ih);
    }

    public void backPatchTrueList(InstructionHandle ih) {
	_trueList.backPatch(ih);
    }

    /**
     * Search for a primop in the symbol table that matches the method type 
     * <code>ctype</code>. Two methods match if they have the same arity.
     * If a primop is overloaded then the "closest match" is returned. The
     * first entry in the vector of primops that has the right arity is 
     * considered to be the default one.
     */
    public MethodType lookupPrimop(SymbolTable stable, String op,
				   MethodType ctype) {
	MethodType result = null;
	final Vector primop = stable.lookupPrimop(op);
	if (primop != null) {
	    final int n = primop.size();
	    int minDistance = Integer.MAX_VALUE;
	    for (int i = 0; i < n; i++) {
		final MethodType ptype = (MethodType) primop.elementAt(i);
		if (ptype.argsCount() != ctype.argsCount()) {
		    continue;
		}
				
		if (result == null) {
		}

		final int distance = ctype.distanceTo(ptype);
		if (distance < minDistance) {
		    minDistance = distance;
		    result = ptype;
		}
	    }		
	}	
	return result;
    }	
}
