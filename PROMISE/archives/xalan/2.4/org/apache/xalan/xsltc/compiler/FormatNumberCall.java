package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class FormatNumberCall extends FunctionCall {
    private Expression _value;
    private Expression _format;
    private Expression _name;
    private QName      _resolvedQName = null;

    public FormatNumberCall(QName fname, Vector arguments) {
	super(fname, arguments);
	_value = argument(0);
	_format = argument(1);
	_name = argumentCount() == 3 ? argument(2) : null;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {

	getStylesheet().numberFormattingUsed();

	final Type tvalue = _value.typeCheck(stable);
	if (tvalue instanceof RealType == false) {
	    _value = new CastExpr(_value, Type.Real);
	}
	final Type tformat = _format.typeCheck(stable);
	if (tformat instanceof StringType == false) {
	    _format = new CastExpr(_format, Type.String);
	}
	if (argumentCount() == 3) {
	    final Type tname = _name.typeCheck(stable);

	    if (_name instanceof LiteralExpr) {
		final LiteralExpr literal = (LiteralExpr) _name;
		_resolvedQName = 
		    getParser().getQNameIgnoreDefaultNs(literal.getValue());
	    }
	    else if (tname instanceof StringType == false) {
		_name = new CastExpr(_name, Type.String);
	    }
	}
	return _type = Type.String;
    }
    
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	_value.translate(classGen, methodGen);
	_format.translate(classGen, methodGen);

	final int fn3arg = cpg.addMethodref(BASIS_LIBRARY_CLASS,
					    "formatNumber",
					    "(DLjava/lang/String;"+
					    "Ljava/text/DecimalFormat;)"+
					    "Ljava/lang/String;");
	final int get = cpg.addMethodref(TRANSLET_CLASS,
					 "getDecimalFormat",
					 "(Ljava/lang/String;)"+
					 "Ljava/text/DecimalFormat;");
	
	il.append(classGen.loadTranslet());
	if (_name == null) {
	    il.append(new PUSH(cpg, EMPTYSTRING));
	}
	else if (_resolvedQName != null) {
	    il.append(new PUSH(cpg, _resolvedQName.toString()));
	}
	else {
	    _name.translate(classGen, methodGen);
	}
	il.append(new INVOKEVIRTUAL(get));
	il.append(new INVOKESTATIC(fn3arg));
    }
}
