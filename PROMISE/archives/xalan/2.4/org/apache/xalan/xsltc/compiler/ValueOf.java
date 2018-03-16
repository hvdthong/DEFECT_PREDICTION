package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.*;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class ValueOf extends Instruction {
    private Expression _select;
    private boolean _escaping = true;
	
    public void display(int indent) {
	indent(indent);
	Util.println("ValueOf");
	indent(indent + IndentIncrement);
	Util.println("select " + _select.toString());
    }
		
    public void parseContents(Parser parser) {
	_select = parser.parseExpression(this, "select", null);

        if (_select.isDummy()) {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "select");
	    return;
        }
        final String str = getAttribute("disable-output-escaping");
	if ((str != null) && (str.equals("yes"))) _escaping = false;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	Type type = _select.typeCheck(stable);
	if ((type != null) && (type.identicalTo(Type.String) == false))
	    _select = new CastExpr(_select, Type.String);
	return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final int setEscaping = cpg.addInterfaceMethodref(OUTPUT_HANDLER,
							  "setEscaping","(Z)Z");
	final int characters = cpg.addMethodref(TRANSLET_CLASS,
						CHARACTERSW,
						CHARACTERSW_SIG);

	if (!_escaping) {
	    il.append(methodGen.loadHandler());
	    il.append(new PUSH(cpg,false));
	    il.append(new INVOKEINTERFACE(setEscaping,2));
	}

	il.append(classGen.loadTranslet());
	_select.translate(classGen, methodGen);	
	il.append(methodGen.loadHandler());
	il.append(new INVOKEVIRTUAL(characters));

	if (!_escaping) {
	    il.append(methodGen.loadHandler());
	    il.append(SWAP);
	    il.append(new INVOKEINTERFACE(setEscaping,2));
	    il.append(POP);
	}
    }
}
