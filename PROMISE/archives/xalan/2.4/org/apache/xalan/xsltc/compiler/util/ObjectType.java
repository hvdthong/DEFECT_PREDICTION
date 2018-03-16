package org.apache.xalan.xsltc.compiler.util;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Constants;

public final class ObjectType extends Type {

    private String _javaClassName = "java.lang.Object"; 

    /**
     * Used to represent an 'any' type.
     */
    protected ObjectType() {}

    /**
     * Used to represent a Java Class type such is required to support 
     * non-static java functions. 
     * @param javaClassName name of the class such as 'com.foo.Processor'
     */
    public ObjectType(String javaClassName) {
	_javaClassName = javaClassName;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object obj) {
        return (obj instanceof ObjectType);
    }

    public String getJavaClassName() {
	return _javaClassName;
    }

    public String toString() {
	return "object";
    }

    public boolean identicalTo(Type other) {
	return this == other;
    }

    public String toSignature() {
	final StringBuffer result = new StringBuffer("L");
	result.append(_javaClassName.replace('.', '/')).append(';');
	return result.toString();
    }

    public org.apache.bcel.generic.Type toJCType() {
	return Util.getJCRefType(toSignature());
    }

    /**
     * Translates a void into an object of internal type <code>type</code>.
     * This translation is needed when calling external functions
     * that return void.
     *
     * @see	org.apache.xalan.xsltc.compiler.util.Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, 
			    Type type) {
	if (type == Type.String) {
	    translateTo(classGen, methodGen, (StringType) type);
	}
	else {
	    ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
					toString(), type.toString());
	    classGen.getParser().reportError(Constants.FATAL, err);
	}
    }

    /**
     * Expects an integer on the stack and pushes its string value by calling
     * <code>Integer.toString(int i)</code>.
     *
     * @see	org.apache.xalan.xsltc.compiler.util.Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, 
			    StringType type) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	il.append(DUP);
	final BranchHandle ifNull = il.append(new IFNULL(null));
	il.append(new INVOKEVIRTUAL(cpg.addMethodref(_javaClassName,
						    "toString",
						    "()" + STRING_SIG)));
	final BranchHandle gotobh = il.append(new GOTO(null));
	ifNull.setTarget(il.append(POP));
	il.append(new PUSH(cpg, ""));
	gotobh.setTarget(il.append(NOP));
    }

    /**
     * Translates an external Java type into an Object type 
     */
    public void translateFrom(ClassGenerator classGen, 
			      MethodGenerator methodGen, Class clazz) {
	methodGen.getInstructionList().append(NOP);
    }

    public Instruction LOAD(int slot) {
	return new ALOAD(slot);
    }
	
    public Instruction STORE(int slot) {
	return new ASTORE(slot);
    }
}
