package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;

public final class AttributeSetMethodGenerator extends MethodGenerator {
    private static int HANDLER_INDEX = 1;
    private static int ITERATOR_INDEX = 2;

    private static final org.apache.bcel.generic.Type[] argTypes =
	new org.apache.bcel.generic.Type[2];
    private static final String[] argNames = new String[2];
    
    static {
	argTypes[0] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
	argNames[0] = TRANSLET_OUTPUT_PNAME;
	argTypes[1] = Util.getJCRefType(NODE_ITERATOR_SIG);
	argNames[1] = ITERATOR_PNAME;
    }

    private final Instruction _astoreHandler;
    private final Instruction _aloadHandler;
    private final Instruction _astoreIterator;
    private final Instruction _aloadIterator;
    
    public AttributeSetMethodGenerator(String methodName, ClassGen classGen) {
	super(org.apache.bcel.Constants.ACC_PRIVATE,
	      org.apache.bcel.generic.Type.VOID,
	      argTypes, argNames, methodName, 
	      classGen.getClassName(),
	      new InstructionList(),
	      classGen.getConstantPool());
	
	_astoreHandler  = new ASTORE(HANDLER_INDEX);
	_aloadHandler   = new ALOAD(HANDLER_INDEX);
	_astoreIterator = new ASTORE(ITERATOR_INDEX);
	_aloadIterator  = new ALOAD(ITERATOR_INDEX);
    }

    public Instruction storeIterator() {
	return _astoreIterator;
    }
    
    public Instruction loadIterator() {
	return _aloadIterator;
    }

    public int getIteratorIndex() {
	return ITERATOR_INDEX;
    }

    public Instruction storeHandler() {
	return _astoreHandler;
    }

    public Instruction loadHandler() {
	return _aloadHandler;
    }

    public int getLocalIndex(String name) {
    }
}
