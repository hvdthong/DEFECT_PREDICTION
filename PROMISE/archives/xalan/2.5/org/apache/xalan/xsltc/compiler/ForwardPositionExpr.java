package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

class ForwardPositionExpr extends Expression {
    private Expression _expr;

    public ForwardPositionExpr(Expression expr) {
	_expr = expr;
    }

    public void setParser(Parser parser) {
	super.setParser(parser);
	_expr.setParser(parser);
    }
    
    public String toString() {
	return "forward-position-expr(" + _expr + ")";
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return (_type = _expr.typeCheck(stable));
    }
	
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	_expr.translate(classGen, methodGen);

	if (_type == Type.NodeSet) {
	    final ConstantPoolGen cpg = classGen.getConstantPool();
	    final InstructionList il = methodGen.getInstructionList();

	    final int init = cpg.addMethodref(FORWARD_POSITION_ITERATOR, 
		"<init>", "(" + NODE_ITERATOR_SIG + ")V");
	    il.append(new NEW(cpg.addClass(FORWARD_POSITION_ITERATOR)));
	    il.append(DUP_X1);
	    il.append(SWAP);
	    il.append(new INVOKESPECIAL(init));
	}
    }
}
