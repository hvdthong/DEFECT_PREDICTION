package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.FilterGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Morten Jorgensen
 */
final class LangCall extends FunctionCall {
    private Expression _lang;
    private Type _langType;

    /**
     * Get the parameters passed to function:
     *   lang(string)
     */
    public LangCall(QName fname, Vector arguments) {
	super(fname, arguments);
	_lang = argument(0);
    }

    /**
     *
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	_langType = _lang.typeCheck(stable);
	if (!(_langType instanceof StringType)) {
	    _lang = new CastExpr(_lang, Type.String);
	}
	return Type.Boolean;
    }

    /**
     *
     */
    public Type getType() {
	return(Type.Boolean);
    }

    /**
     * This method is called when the constructor is compiled in
     * Stylesheet.compileConstructor() and not as the syntax tree is traversed.
     */
    public void translate(ClassGenerator classGen,
			  MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int tst = cpg.addMethodref(BASIS_LIBRARY_CLASS,
					 "testLanguage",
					 "("+STRING_SIG+DOM_INTF_SIG+"I)Z");
	_lang.translate(classGen,methodGen);
	il.append(methodGen.loadDOM());
	if (classGen instanceof FilterGenerator)
	    il.append(new ILOAD(1));
	else
	    il.append(methodGen.loadContextNode());
	il.append(new INVOKESTATIC(tst));
    }
}
