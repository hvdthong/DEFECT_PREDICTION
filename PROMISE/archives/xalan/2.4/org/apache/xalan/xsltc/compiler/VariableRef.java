package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class VariableRef extends VariableRefBase {

    public VariableRef(Variable variable) {
	super(variable);
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (_type.implementedAsMethod()) return;

	final String name = _variable.getVariable();
	final String signature = _type.toSignature();

	if (_variable.isLocal()) {
	    if (classGen.isExternal()) {
		Closure variableClosure = _closure;
		while (variableClosure != null) {
		    if (variableClosure.inInnerClass()) break;
		    variableClosure = variableClosure.getParentClosure();
		}
	    
		if (variableClosure != null) {
		    il.append(ALOAD_0);
		    il.append(new GETFIELD(
			cpg.addFieldref(variableClosure.getInnerClassName(), 
			    name, signature)));
		}
		else {
		    il.append(_variable.loadInstruction());
		    _variable.removeReference(this);
		}
	    }
	    else {
		il.append(_variable.loadInstruction());
		_variable.removeReference(this);
	    }
	}
	else {
	    final String className = classGen.getClassName();
	    il.append(classGen.loadTranslet());
	    if (classGen.isExternal()) {
		il.append(new CHECKCAST(cpg.addClass(className)));
	    }
	    il.append(new GETFIELD(cpg.addFieldref(className,name,signature)));
	}

	if (_variable.getType() instanceof NodeSetType) {
	    final int clone = cpg.addInterfaceMethodref(NODE_ITERATOR,
						       "cloneIterator",
						       "()" + 
							NODE_ITERATOR_SIG);
	    il.append(new INVOKEINTERFACE(clone, 1));
	}
    }
}
