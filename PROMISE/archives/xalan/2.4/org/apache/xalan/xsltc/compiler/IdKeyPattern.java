package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.*;

abstract class IdKeyPattern extends LocationPathPattern {

    protected RelativePathPattern _left = null;;
    private String _index = null;
    private String _value = null;;

    public IdKeyPattern(String index, String value) {
	_index = index;
	_value = value;
    }

    public String getIndexName() {
	return(_index);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return Type.NodeSet;
    }
    
    public boolean isWildcard() {
	return false;
    }
    
    public void setLeft(RelativePathPattern left) {
	_left = left;
    }

    public StepPattern getKernelPattern() {
	return(null);
    }
    
    public void reduceKernelPattern() { }

    public String toString() {
	return "id/keyPattern(" + _index + ", " + _value + ')';
    }

    /**
     * This method is called when the constructor is compiled in
     * Stylesheet.compileConstructor() and not as the syntax tree is traversed.
     */
    public void translate(ClassGenerator classGen,
			  MethodGenerator methodGen) {

	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int getKeyIndex = cpg.addMethodref(TRANSLET_CLASS,
						 "getKeyIndex",
						 "(Ljava/lang/String;)"+
						 KEY_INDEX_SIG);
	
	final int lookupId = cpg.addMethodref(KEY_INDEX_CLASS,
					      "containsID",
					      "(ILjava/lang/Object;)I");
	final int lookupKey = cpg.addMethodref(KEY_INDEX_CLASS,
					       "containsKey",
					       "(ILjava/lang/Object;)I");

	il.append(classGen.loadTranslet());
	il.append(new PUSH(cpg,_index));
	il.append(new INVOKEVIRTUAL(getKeyIndex));
	
	il.append(SWAP);
	il.append(new PUSH(cpg,_value));
	if (this instanceof IdPattern)
	    il.append(new INVOKEVIRTUAL(lookupId));
	else
	    il.append(new INVOKEVIRTUAL(lookupKey));

	_trueList.add(il.append(new IFNE(null)));
	_falseList.add(il.append(new GOTO(null)));
    }

}

