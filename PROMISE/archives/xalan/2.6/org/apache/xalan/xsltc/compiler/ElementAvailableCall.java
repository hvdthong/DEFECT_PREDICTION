package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class ElementAvailableCall extends FunctionCall {

    public ElementAvailableCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    /**
     * Force the argument to this function to be a literal string.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (argument() instanceof LiteralExpr) {
	    return _type = Type.Boolean;
	}
	ErrorMsg err = new ErrorMsg(ErrorMsg.NEED_LITERAL_ERR,
				    "element-available", this);
	throw new TypeCheckError(err);
    }

    /**
     * Returns an object representing the compile-time evaluation 
     * of an expression. We are only using this for function-available
     * and element-available at this time.
     */
    public Object evaluateAtCompileTime() {
	return getResult() ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result that this function will return
     */
    public boolean getResult() {
	try {
	    final LiteralExpr arg = (LiteralExpr) argument();
	    final String qname = arg.getValue();
	    final int index = qname.indexOf(':');
	    final String localName = (index > 0) ? 
		qname.substring(index + 1) : qname;
	    return getParser().elementSupported(arg.getNamespace(), 
					        localName);
	}
	catch (ClassCastException e) {
	    return false;
	}
    }

    /**
     * Calls to 'element-available' are resolved at compile time since 
     * the namespaces declared in the stylsheet are not available at run
     * time. Consequently, arguments to this function must be literals.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final boolean result = getResult();
	methodGen.getInstructionList().append(new PUSH(cpg, result));
    }
}
