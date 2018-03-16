package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class UnparsedEntityUriCall extends FunctionCall {
    private Expression _entity;

    public UnparsedEntityUriCall(QName fname, Vector arguments) {
	super(fname, arguments);
	_entity = argument();
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Type entity = _entity.typeCheck(stable);
	if (entity instanceof StringType == false) {
	    _entity = new CastExpr(_entity, Type.String);
	}
	return _type = Type.String;
    }
    
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	il.append(classGen.loadTranslet());
	_entity.translate(classGen, methodGen);
	il.append(new INVOKEVIRTUAL(cpg.addMethodref(TRANSLET_CLASS,
						     "getUnparsedEntity",
						     "(Ljava/lang/String;)"+
						     "Ljava/lang/String;")));
    }
}
