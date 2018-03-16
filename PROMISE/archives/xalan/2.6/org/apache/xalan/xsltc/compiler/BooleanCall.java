package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class BooleanCall extends FunctionCall {

    private Expression _arg = null;

    public BooleanCall(QName fname, Vector arguments) {
	super(fname, arguments);
	_arg = argument(0);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	_arg.typeCheck(stable);
	return _type = Type.Boolean;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	_arg.translate(classGen, methodGen);
	final Type targ = _arg.getType();
	if (!targ.identicalTo(Type.Boolean)) {
	    _arg.startIterator(classGen, methodGen);
	    targ.translateTo(classGen, methodGen, Type.Boolean);
	}
    }
}
