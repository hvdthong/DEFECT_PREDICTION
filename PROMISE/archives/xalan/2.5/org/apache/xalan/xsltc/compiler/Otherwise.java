package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Otherwise extends Instruction {
    public void display(int indent) {
	indent(indent);
	Util.println("Otherwise");
	indent(indent + IndentIncrement);
	displayContents(indent + IndentIncrement);
    }
	
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	typeCheckContents(stable);
	return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final Parser parser = getParser();
	final ErrorMsg err = new ErrorMsg(ErrorMsg.STRAY_OTHERWISE_ERR, this);
	parser.reportError(Constants.ERROR, err);
    }
}
