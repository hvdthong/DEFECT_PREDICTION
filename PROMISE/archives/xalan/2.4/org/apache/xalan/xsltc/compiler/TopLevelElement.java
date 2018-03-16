package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

class TopLevelElement extends SyntaxTreeNode {

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
    public InstructionList compile(ClassGenerator classGen,
				   MethodGenerator methodGen) {
	final InstructionList result, save = methodGen.getInstructionList();
	methodGen.setInstructionList(result = new InstructionList());
	translate(classGen, methodGen);
	methodGen.setInstructionList(save);
	return result;
    }

    public void display(int indent) {
	indent(indent);
	Util.println("TopLevelElement");
	displayContents(indent + IndentIncrement);
    }
}
