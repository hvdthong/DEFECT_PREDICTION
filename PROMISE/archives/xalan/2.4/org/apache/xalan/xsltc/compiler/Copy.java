package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.xml.parsers.*;

import org.xml.sax.*;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class Copy extends Instruction {
    private UseAttributeSets _useSets;
    
    public void parseContents(Parser parser) {
	final String useSets = getAttribute("use-attribute-sets");
	if (useSets.length() > 0) {
	    _useSets = new UseAttributeSets(useSets, parser);
	}
	parseChildren(parser);
    }
    
    public void display(int indent) {
	indent(indent);
	Util.println("Copy");
	indent(indent + IndentIncrement);
	displayContents(indent + IndentIncrement);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_useSets != null) {
	    _useSets.typeCheck(stable);
	}
	typeCheckContents(stable);
	return Type.Void;
    }
	
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final LocalVariableGen name =
	    methodGen.addLocalVariable2("name",
					Util.getJCRefType(STRING_SIG),
					il.getEnd());
	final LocalVariableGen length =
	    methodGen.addLocalVariable2("length",
					Util.getJCRefType("I"),
					il.getEnd());

	il.append(methodGen.loadDOM());
	il.append(methodGen.loadCurrentNode());
	il.append(methodGen.loadHandler());
	final int cpy = cpg.addInterfaceMethodref(DOM_INTF,
						  "shallowCopy",
						  "("
						  + NODE_SIG
						  + TRANSLET_OUTPUT_SIG
						  + ")" + STRING_SIG); 
	il.append(new INVOKEINTERFACE(cpy, 3));
	il.append(DUP);
	il.append(new ASTORE(name.getIndex()));
	final BranchHandle ifBlock1 = il.append(new IFNULL(null));

	il.append(new ALOAD(name.getIndex()));
	final int lengthMethod = cpg.addMethodref(STRING_CLASS,"length","()I");
	il.append(new INVOKEVIRTUAL(lengthMethod));
	il.append(new ISTORE(length.getIndex()));

	if (_useSets != null) {
	    final SyntaxTreeNode parent = getParent();
	    if ((parent instanceof LiteralElement) ||
		(parent instanceof LiteralElement)) {
		_useSets.translate(classGen, methodGen);
	    }
	    else {
		il.append(new ILOAD(length.getIndex()));
		final BranchHandle ifBlock2 = il.append(new IFEQ(null));
		_useSets.translate(classGen, methodGen);
		ifBlock2.setTarget(il.append(NOP));
	    }
	}

	translateContents(classGen, methodGen);

	il.append(new ILOAD(length.getIndex()));
	final BranchHandle ifBlock3 = il.append(new IFEQ(null));
	il.append(methodGen.loadHandler());
	il.append(new ALOAD(name.getIndex()));
	il.append(methodGen.endElement());
	
	final InstructionHandle end = il.append(NOP);
	ifBlock1.setTarget(end);
	ifBlock3.setTarget(end);
	methodGen.removeLocalVariable(name);
	methodGen.removeLocalVariable(length);
    }
}
