package org.apache.xalan.xsltc.compiler;

import javax.xml.parsers.*;

import org.xml.sax.*;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class CopyOf extends Instruction {
    private Expression _select;
	
    public void display(int indent) {
	indent(indent);
	Util.println("CopyOf");
	indent(indent + IndentIncrement);
	Util.println("select " + _select.toString());
    }

    public void parseContents(Parser parser) {
	_select = parser.parseExpression(this, "select", null);
        if (_select.isDummy()) {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "select");
	    return;
        }
    }
	
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Type tselect = _select.typeCheck(stable);
	if (tselect instanceof NodeType ||
	    tselect instanceof NodeSetType ||
	    tselect instanceof ReferenceType ||
	    tselect instanceof ResultTreeType) {
	}
	else {
	    _select = new CastExpr(_select, Type.String);
	}
	return Type.Void;
    }
	
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final Type tselect = _select.getType();

	final String CPY1_SIG = "("+NODE_ITERATOR_SIG+TRANSLET_OUTPUT_SIG+")V";
	final int cpy1 = cpg.addInterfaceMethodref(DOM_INTF, "copy", CPY1_SIG);

	final String CPY2_SIG = "("+NODE_SIG+TRANSLET_OUTPUT_SIG+")V";
	final int cpy2 = cpg.addInterfaceMethodref(DOM_INTF, "copy", CPY2_SIG);

	if (tselect instanceof NodeSetType) {
	    il.append(methodGen.loadDOM());

	    _select.translate(classGen, methodGen);	
	    _select.startResetIterator(classGen, methodGen);

	    il.append(methodGen.loadHandler());
	    il.append(new INVOKEINTERFACE(cpy1, 3));
	}
	else if (tselect instanceof NodeType) {
	    il.append(methodGen.loadDOM());
	    _select.translate(classGen, methodGen);	
	    il.append(methodGen.loadHandler());
	    il.append(new INVOKEINTERFACE(cpy2, 3));
	}
	else if (tselect instanceof ResultTreeType) {
	    _select.translate(classGen, methodGen);	
	    il.append(ICONST_1);
	    il.append(methodGen.loadHandler());
	    il.append(new INVOKEINTERFACE(cpy2, 3));
	}
	else if (tselect instanceof ReferenceType) {
	    _select.translate(classGen, methodGen);
	    il.append(methodGen.loadHandler());
	    il.append(methodGen.loadCurrentNode());
	    il.append(methodGen.loadDOM());
	    final int copy = cpg.addMethodref(BASIS_LIBRARY_CLASS, "copy",
					      "(" 
					      + OBJECT_SIG  
					      + TRANSLET_OUTPUT_SIG 
					      + NODE_SIG
					      + DOM_INTF_SIG
					      + ")V");
	    il.append(new INVOKESTATIC(copy));
	}
	else {
	    il.append(classGen.loadTranslet());
	    _select.translate(classGen, methodGen);
	    il.append(methodGen.loadHandler());
	    il.append(new INVOKEVIRTUAL(cpg.addMethodref(TRANSLET_CLASS,
							 CHARACTERSW,
							 CHARACTERSW_SIG)));
	}

    }
}
