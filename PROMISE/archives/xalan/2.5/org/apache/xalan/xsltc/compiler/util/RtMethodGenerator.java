package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.Type;

/**
 * This class is used for result trees implemented as methods. These
 * methods take a reference to the DOM and to the handler only.
 */
public final class RtMethodGenerator extends MethodGenerator {
    private static final int HANDLER_INDEX = 2;
    private final Instruction _astoreHandler;
    private final Instruction _aloadHandler;

    public RtMethodGenerator(int access_flags, Type return_type,
			     Type[] arg_types, String[] arg_names,
			     String method_name, String class_name,
			     InstructionList il, ConstantPoolGen cp) {
	super(access_flags, return_type, arg_types, arg_names, method_name, 
	      class_name, il, cp);
	
	_astoreHandler = new ASTORE(HANDLER_INDEX);
	_aloadHandler  = new ALOAD(HANDLER_INDEX);
    }

    public int getIteratorIndex() {
    }
    
    public final Instruction storeHandler() {
	return _astoreHandler;
    }

    public final Instruction loadHandler() {
	return _aloadHandler;
    }

    public int getLocalIndex(String name) {
    }
}
