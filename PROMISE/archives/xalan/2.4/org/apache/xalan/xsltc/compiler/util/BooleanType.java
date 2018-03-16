package org.apache.xalan.xsltc.compiler.util;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Constants;

public final class BooleanType extends Type {
    protected BooleanType() {}

    public String toString() {
	return "boolean";
    }

    public boolean identicalTo(Type other) {
	return this == other;
    }

    public String toSignature() {
	return "Z";
    }

    public boolean isSimple() {
	return true;
    }

    public org.apache.bcel.generic.Type toJCType() {
	return org.apache.bcel.generic.Type.BOOLEAN;
    }

    /**
     * Translates a real into an object of internal type <code>type</code>. The
     * translation to int is undefined since booleans are always converted to
     * reals in arithmetic expressions.
     *
     * @see	org.apache.xalan.xsltc.compiler.util.Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, 
			    Type type) {
	if (type == Type.String) {
	    translateTo(classGen, methodGen, (StringType) type);
	}
	else if (type == Type.Real) {
	    translateTo(classGen, methodGen, (RealType) type);
	}
	else if (type == Type.Reference) {
	    translateTo(classGen, methodGen, (ReferenceType) type);
	}
	else {
	    ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
					toString(), type.toString());
	    classGen.getParser().reportError(Constants.FATAL, err);
	}
    }

    /**
     * Expects a boolean on the stack and pushes a string. If the value on the
     * stack is zero, then the string 'false' is pushed. Otherwise, the string
     * 'true' is pushed.
     *
     * @see	org.apache.xalan.xsltc.compiler.util.Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, 
			    StringType type) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final BranchHandle falsec = il.append(new IFEQ(null));
	il.append(new PUSH(cpg, "true"));
	final BranchHandle truec = il.append(new GOTO(null));
	falsec.setTarget(il.append(new PUSH(cpg, "false")));
	truec.setTarget(il.append(NOP));
    }

    /**
     * Expects a boolean on the stack and pushes a real. The value "true" is
     * converted to 1.0 and the value "false" to 0.0.
     *
     * @see	org.apache.xalan.xsltc.compiler.util.Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, 
			    RealType type) {
	methodGen.getInstructionList().append(I2D);
    }

    /**
     * Expects a boolean on the stack and pushes a boxed boolean.
     * Boxed booleans are represented by an instance of
     * <code>java.lang.Boolean</code>.
     *
     * @see	org.apache.xalan.xsltc.compiler.util.Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, 
			    ReferenceType type) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	il.append(new NEW(cpg.addClass(BOOLEAN_CLASS)));
	il.append(DUP_X1);
	il.append(SWAP);
	il.append(new INVOKESPECIAL(cpg.addMethodref(BOOLEAN_CLASS,
						     "<init>",
						     "(Z)V")));
    }

    /**
     * Translates an internal boolean into an external (Java) boolean.
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, 
			    Class clazz) {
	if (clazz == java.lang.Boolean.TYPE) {
	    methodGen.getInstructionList().append(NOP);
	}
	else {
	    ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
					toString(), clazz.getName());
	    classGen.getParser().reportError(Constants.FATAL, err);
	}
    }

    /**
     * Translates an external (Java) boolean into internal boolean.
     */
    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, 
			      Class clazz) {
	translateTo(classGen, methodGen, clazz);
    }

    /**
     * Translates an object of this type to its boxed representation.
     */ 
    public void translateBox(ClassGenerator classGen,
			     MethodGenerator methodGen) {
	translateTo(classGen, methodGen, Type.Reference);
    }

    /**
     * Translates an object of this type to its unboxed representation.
     */ 
    public void translateUnBox(ClassGenerator classGen,
			       MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	il.append(new CHECKCAST(cpg.addClass(BOOLEAN_CLASS)));
	il.append(new INVOKEVIRTUAL(cpg.addMethodref(BOOLEAN_CLASS,
						     BOOLEAN_VALUE, 
						     BOOLEAN_VALUE_SIG)));
    }

    public Instruction LOAD(int slot) {
	return new ILOAD(slot);
    }
	
    public Instruction STORE(int slot) {
	return new ISTORE(slot);
    }

    public BranchInstruction GT(boolean tozero) {
	return tozero ? (BranchInstruction) new IFGT(null) : 
	    (BranchInstruction) new IF_ICMPGT(null);
    }

    public BranchInstruction GE(boolean tozero) {
	return tozero ? (BranchInstruction) new IFGE(null) : 
	    (BranchInstruction) new IF_ICMPGE(null);
    }

    public BranchInstruction LT(boolean tozero) {
	return tozero ? (BranchInstruction) new IFLT(null) : 
	    (BranchInstruction) new IF_ICMPLT(null);
    }

    public BranchInstruction LE(boolean tozero) {
	return tozero ? (BranchInstruction) new IFLE(null) : 
	    (BranchInstruction) new IF_ICMPLE(null);
    }
}
