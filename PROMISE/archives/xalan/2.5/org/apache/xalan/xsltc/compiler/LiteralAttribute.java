package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class LiteralAttribute extends Instruction {


    /**
     * Creates a new literal attribute (but does not insert it into the AST).
     * @param name the attribute name (incl. prefix) as a String.
     * @param value the attribute value.
     * @param parser the XSLT parser (wraps XPath parser).
     */
    public LiteralAttribute(String name, String value, Parser parser) {
	_name = name;
	_value = AttributeValue.create(this, value, parser);
    }

    public void display(int indent) {
	indent(indent);
	Util.println("LiteralAttribute name=" + _name + " value=" + _value);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	_value.typeCheck(stable);
	typeCheckContents(stable);
	return Type.Void;
    }

    protected boolean contextDependent() {
	return _value.contextDependent();
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	il.append(methodGen.loadHandler());
	il.append(new PUSH(cpg, _name));
	_value.translate(classGen, methodGen);
	il.append(methodGen.attribute());
    }
}
