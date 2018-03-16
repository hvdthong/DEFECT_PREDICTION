package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.*;
import org.apache.bcel.generic.*;

final class SimpleAttributeValue extends AttributeValue {


    /**
     * Creates a new simple attribute value.
     * @param value the attribute value.
     */
    public SimpleAttributeValue(String value) {
	_value = value;
    }

    /**
     * Returns this attribute value's type (String).
     * @param stable The compiler/parser's symbol table
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return _type = Type.String;
    }
    
    public String toString() {
	return _value;
    }
	
    protected boolean contextDependent() {
	return false;
    }

    /**
     * Translate this attribute value into JVM bytecodes that pushes the
     * attribute value onto the JVM's stack.
     * @param classGen BCEL Java class generator
     * @param methodGen BCEL Java method generator
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	il.append(new PUSH(cpg, _value));
    }
}
