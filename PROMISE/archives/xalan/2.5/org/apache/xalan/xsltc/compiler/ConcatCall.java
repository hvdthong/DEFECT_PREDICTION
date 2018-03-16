package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ConcatCall extends FunctionCall {
    public ConcatCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	for (int i = 0; i < argumentCount(); i++) {
	    final Expression exp = argument(i);
	    if (!exp.typeCheck(stable).identicalTo(Type.String)) {
		setArgument(i, new CastExpr(exp, Type.String));
	    }
	}
	return _type = Type.String;
    }
	
    /** translate leaves a String on the stack */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final int nArgs = argumentCount();
	
	switch (nArgs) {
	case 0:
	    il.append(new PUSH(cpg, EMPTYSTRING));
	    break;
	    
	case 1:
	    argument().translate(classGen, methodGen);
	    break;

	default:
	    final int initBuffer = cpg.addMethodref(STRING_BUFFER_CLASS,
						    "<init>", "()V");
	    final Instruction append =
		new INVOKEVIRTUAL(cpg.addMethodref(STRING_BUFFER_CLASS,
						   "append",
						   "("+STRING_SIG+")"
						   +STRING_BUFFER_SIG));
	    
	    final int toString = cpg.addMethodref(STRING_BUFFER_CLASS,
						  "toString",
						  "()"+STRING_SIG);
	    
	    il.append(new NEW(cpg.addClass(STRING_BUFFER_CLASS)));
	    il.append(DUP);
	    il.append(new INVOKESPECIAL(initBuffer));
	    for (int i = 0; i < nArgs; i++) {
		argument(i).translate(classGen, methodGen);
		il.append(append);
	    }
	    il.append(new INVOKEVIRTUAL(toString));
	}
    }
}
