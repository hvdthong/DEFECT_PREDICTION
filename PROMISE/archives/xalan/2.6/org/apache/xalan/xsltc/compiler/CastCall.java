package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Santiago Pericas-Geertsen
 */
final class CastCall extends FunctionCall {
    
    /**
     * Name of the class that is the target of the cast. Must be a 
     * fully-qualified Java class Name.
     */
    private String _className;

    /**
     * A reference to the expression being casted.
     */
    private Expression _right;

    /**
     * Constructor.
     */
    public CastCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    /**
     * Type check the two parameters for this function
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (argumentCount() != 2) {
	    throw new TypeCheckError(new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR,
                                                  getName(), this));
	}

	Expression exp = argument(0);
        if (exp instanceof LiteralExpr) {
            _className = ((LiteralExpr) exp).getValue();
            _type = Type.newObjectType(_className);
        }
        else {
	    throw new TypeCheckError(new ErrorMsg(ErrorMsg.NEED_LITERAL_ERR,
                                                  getName(), this));
        }
        
        _right = argument(1);
        Type tright = _right.typeCheck(stable);
        if (tright != Type.Reference && 
            tright instanceof ObjectType == false) 
        {
	    throw new TypeCheckError(new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                                  tright, _type, this));
        }
        
        return _type;
    }
    
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

        _right.translate(classGen, methodGen);
        il.append(new CHECKCAST(cpg.addClass(_className)));
    }
}
