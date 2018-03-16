package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class Choose extends Instruction {

    /**
     * Display the element contents (a lot of when's and an otherwise)
     */
    public void display(int indent) {
	indent(indent);
	Util.println("Choose");
	indent(indent + IndentIncrement);
	displayContents(indent + IndentIncrement);
    }
	
    /**
     * Translate this Choose element. Generate a test-chain for the various
     * <xsl:when> elements and default to the <xsl:otherwise> if present.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final Vector whenElements = new Vector();
	Otherwise otherwise = null;
	Enumeration elements = elements();

	ErrorMsg error = null;
	final int line = getLineNumber();

	while (elements.hasMoreElements()) {
	    Object element = elements.nextElement();
	    if (element instanceof When) {
		whenElements.addElement(element);
	    }
	    else if (element instanceof Otherwise) {
		if (otherwise == null) {
		    otherwise = (Otherwise)element;
		}
		else {
		    error = new ErrorMsg(ErrorMsg.MULTIPLE_OTHERWISE_ERR, this);
		    getParser().reportError(Constants.ERROR, error);
		}
	    }
	    else if (element instanceof Text) {
		((Text)element).ignore();
	    }
	    else {
		error = new ErrorMsg(ErrorMsg.WHEN_ELEMENT_ERR, this);
		getParser().reportError(Constants.ERROR, error);
	    }
	}

	if (whenElements.size() == 0) {
	    error = new ErrorMsg(ErrorMsg.MISSING_WHEN_ERR, this);
	    getParser().reportError(Constants.ERROR, error);
	    return;
	}

	InstructionList il = methodGen.getInstructionList();

	BranchHandle nextElement = null;
	Vector exitHandles = new Vector();
	InstructionHandle exit = null;

	Enumeration whens = whenElements.elements();
	while (whens.hasMoreElements()) {
	    final When when = (When)whens.nextElement();
	    final Expression test = when.getTest();

	    InstructionHandle truec = il.getEnd();

	    if (nextElement != null) 
		nextElement.setTarget(il.append(NOP));
	    test.translateDesynthesized(classGen, methodGen);

	    if (test instanceof FunctionCall) {
		FunctionCall call = (FunctionCall)test;
		try {
		    Type type = call.typeCheck(getParser().getSymbolTable());
		    if (type != Type.Boolean) {
			test._falseList.add(il.append(new IFEQ(null)));
		    }
		}
		catch (TypeCheckError e) { 
		}
	    }
	    truec = il.getEnd();

	    if (!when.ignore()) when.translateContents(classGen, methodGen);

	    exitHandles.addElement(il.append(new GOTO(null)));
	    if (whens.hasMoreElements() || otherwise != null) {
		nextElement = il.append(new GOTO(null));
		test.backPatchFalseList(nextElement);
	    }
	    else
		test.backPatchFalseList(exit = il.append(NOP));
	    test.backPatchTrueList(truec.getNext());
	}
	
	if (otherwise != null) {
	    nextElement.setTarget(il.append(NOP));
	    otherwise.translateContents(classGen, methodGen);
	    exit = il.append(NOP);
	}

	Enumeration exitGotos = exitHandles.elements();
	while (exitGotos.hasMoreElements()) {
	    BranchHandle gotoExit = (BranchHandle)exitGotos.nextElement();
	    gotoExit.setTarget(exit);
	}
    }
}
