package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ContainsCall extends FunctionCall {

    private Expression _base = null;
    private Expression _token = null;

    /**
     * Create a contains() call - two arguments, both strings
     */
    public ContainsCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    /**
     * This XPath function returns true/false values
     */
    public boolean isBoolean() {
	return true;
    }

    /**
     * Type check the two parameters for this function
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {

	if (argumentCount() != 2) {
	    throw new TypeCheckError(ErrorMsg.ILLEGAL_ARG_ERR, getName(), this);
	}

	_base = argument(0);
	Type baseType = _base.typeCheck(stable);	
	if (baseType != Type.String)
	    _base = new CastExpr(_base, Type.String);

	_token = argument(1);
	Type tokenType = _token.typeCheck(stable);	
	if (tokenType != Type.String)
	    _token = new CastExpr(_token, Type.String);

	return _type = Type.Boolean;
    }

    /**
     * Compile the expression - leave boolean expression on stack
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	translateDesynthesized(classGen, methodGen);
	synthesize(classGen, methodGen);
    }

    /**
     * Compile expression and update true/false-lists
     */
    public void translateDesynthesized(ClassGenerator classGen,
				       MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	_base.translate(classGen, methodGen);
	_token.translate(classGen, methodGen);
	il.append(new INVOKEVIRTUAL(cpg.addMethodref(STRING_CLASS,
						     "indexOf",
						     "("+STRING_SIG+")I")));
	_falseList.add(il.append(new IFLT(null)));
    }
}
