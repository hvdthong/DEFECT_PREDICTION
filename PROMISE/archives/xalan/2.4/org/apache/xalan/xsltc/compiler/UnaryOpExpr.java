package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class UnaryOpExpr extends Expression {
    private Expression _left;
	
    public UnaryOpExpr(Expression left) {
	(_left = left).setParent(this);
    }

    /**
     * Returns true if this expressions contains a call to position(). This is
     * needed for context changes in node steps containing multiple predicates.
     */
    public boolean hasPositionCall() {
	return(_left.hasPositionCall());
    }

    public void setParser(Parser parser) {
	super.setParser(parser);
	_left.setParser(parser);
    }
    
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Type tleft = _left.typeCheck(stable); 
	final MethodType ptype = lookupPrimop(stable, "u-",
					      new MethodType(Type.Void,
							     tleft)); 
	
	if (ptype != null) {
	    final Type arg1 = (Type) ptype.argsType().elementAt(0);
	    if (!arg1.identicalTo(tleft)) {
		_left = new CastExpr(_left, arg1);
	    }
	    return _type = ptype.resultType();
	}

	throw new TypeCheckError(this);
    }

    public String toString() {
	return "u-" + '(' + _left + ')';
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	InstructionList il = methodGen.getInstructionList();
	_left.translate(classGen, methodGen);
	il.append(_type.NEG());
    }
}

