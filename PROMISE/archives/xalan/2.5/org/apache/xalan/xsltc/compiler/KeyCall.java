package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class KeyCall extends FunctionCall {

    /**
     * The name of the key.
     */
    private Expression _name;

    /**
     * The value to look up in the key/index.
     */
    private Expression _value;

    /**
     * The value's data type.
     */

    /**
     * Expanded qname when name is literal.
     */
    private QName _resolvedQName = null;

    /**
     * Get the parameters passed to function:
     *   key(String name, String value)
     *   key(String name, NodeSet value)
     * The 'arguments' vector should contain two parameters for key() calls,
     * one holding the key name and one holding the value(s) to look up. The
     * vector has only one parameter for id() calls (the key name is always
     * "##id" for id() calls).
     *
     * @param fname The function name (should be 'key' or 'id')
     * @param arguments A vector containing the arguments the the function
     */
    public KeyCall(QName fname, Vector arguments) {
	super(fname, arguments);
	switch(argumentCount()) {
	case 1:
	    _name = null;
	    _value = argument(0);
	    break;
	case 2:
	    _name = argument(0);
	    _value = argument(1);
	    break;
	default:
	    _name = _value = null;
	    break;
	}
    }

    /**
     * Type check the parameters for the id() or key() function.
     * The index name (for key() call only) must be a string or convertable
     * to a string, and the lookup-value must be a string or a node-set.
     * @param stable The parser's symbol table
     * @throws TypeCheckError When the parameters have illegal type
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Type returnType = super.typeCheck(stable);

	if (_name != null) {
	    final Type nameType = _name.typeCheck(stable); 

	    if (_name instanceof LiteralExpr) {
		final LiteralExpr literal = (LiteralExpr) _name;
		_resolvedQName = 
		    getParser().getQNameIgnoreDefaultNs(literal.getValue());
	    }
	    else if (nameType instanceof StringType == false) {
		_name = new CastExpr(_name, Type.String);
	    }
	}

	_valueType = _value.typeCheck(stable);

	if (_valueType != Type.NodeSet && _valueType != Type.String) 
	{
	    _value = new CastExpr(_value, Type.String);
	}

	return returnType;
    }

    /**
     * This method is called when the constructor is compiled in
     * Stylesheet.compileConstructor() and not as the syntax tree is traversed.
     * This method is a wrapper for the real translation method, which is
     * the private method translateCall() below. All this method does is to
     * wrap the KeyIndex that this function returns inside a duplicate filter.
     * The duplicate filter is used both to eliminate duplicates and to
     * cache the nodes in the index.
     * @param classGen The Java class generator
     * @param methodGen The method generator
     */
    public void translate(ClassGenerator classGen,
			  MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
						 
	final int getNodeHandle = cpg.addInterfaceMethodref(DOM_INTF,
							   "getNodeHandle",
							   "(I)"+NODE_SIG);	

	final int dupInit = cpg.addMethodref(DUP_FILTERED_ITERATOR,
					     "<init>",
					     "("+NODE_ITERATOR_SIG+")V");
					     
	il.append(new NEW(cpg.addClass(DUP_FILTERED_ITERATOR)));	
	il.append(DUP);
	translateCall(classGen, methodGen);
	il.append(new INVOKESPECIAL(dupInit));
	
    }

    /**
     * Translate the actual index lookup - leaves KeyIndex (iterator) on stack
     * @param classGen The Java class generator
     * @param methodGen The method generator
     */
    private void translateCall(ClassGenerator classGen,
			      MethodGenerator methodGen) {

	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int getNodeValue = cpg.addInterfaceMethodref(DOM_INTF,
							   GET_NODE_VALUE,
							   "(I)"+STRING_SIG);

	final int getKeyIndex = cpg.addMethodref(TRANSLET_CLASS,
						 "getKeyIndex",
						 "(Ljava/lang/String;)"+
						 KEY_INDEX_SIG);

	final int lookupId = cpg.addMethodref(KEY_INDEX_CLASS,
					      "lookupId",
					      "(Ljava/lang/Object;)V");
	final int lookupKey = cpg.addMethodref(KEY_INDEX_CLASS,
					       "lookupKey",
					       "(Ljava/lang/Object;)V");

	final int merge = cpg.addMethodref(KEY_INDEX_CLASS,
					   "merge",
					   "("+KEY_INDEX_SIG+")V");

	final int indexConstructor = cpg.addMethodref(TRANSLET_CLASS,
						      "createKeyIndex",
						      "()"+KEY_INDEX_SIG);
						      
	final int keyDom = cpg.addMethodref(XSLT_PACKAGE + ".dom.KeyIndex",
					 "setDom",
					 "("+DOM_INTF_SIG+")V");				 
						      
	
	final LocalVariableGen returnIndex =
	    methodGen.addLocalVariable("returnIndex",
				       Util.getJCRefType(KEY_INDEX_SIG),
				       il.getEnd(), null);

	final LocalVariableGen searchIndex =
	    methodGen.addLocalVariable("searchIndex",
				       Util.getJCRefType(KEY_INDEX_SIG),
				       il.getEnd(), null);

	if (_valueType == Type.NodeSet) {
	    il.append(methodGen.loadCurrentNode());
	    il.append(methodGen.loadIterator());

	    _value.translate(classGen, methodGen);
	    _value.startResetIterator(classGen, methodGen);
	    il.append(methodGen.storeIterator());

	    il.append(classGen.loadTranslet());
	    il.append(new INVOKEVIRTUAL(indexConstructor));
	    il.append(DUP);
	    il.append(methodGen.loadDOM());
	    il.append(new INVOKEVIRTUAL(keyDom));
	    il.append(new ASTORE(returnIndex.getIndex()));

	    il.append(classGen.loadTranslet());
	    if (_name == null) {
		il.append(new PUSH(cpg,"##id"));
	    }
	    else if (_resolvedQName != null) {
		il.append(new PUSH(cpg, _resolvedQName.toString()));
	    }
	    else {
		_name.translate(classGen, methodGen);
	    }

	    il.append(new INVOKEVIRTUAL(getKeyIndex));
	    il.append(new ASTORE(searchIndex.getIndex()));


	    final BranchHandle nextNode = il.append(new GOTO(null));
	    final InstructionHandle loop = il.append(NOP);

	    il.append(new ALOAD(returnIndex.getIndex()));
	    
	    il.append(new ALOAD(searchIndex.getIndex()));
	    il.append(DUP);
	    il.append(methodGen.loadDOM());
	    il.append(methodGen.loadCurrentNode());
	    il.append(new INVOKEINTERFACE(getNodeValue, 2));
	    if (_name == null) {
		il.append(new INVOKEVIRTUAL(lookupId));
	    }
	    else {
		il.append(new INVOKEVIRTUAL(lookupKey));
	    }

	    il.append(new INVOKEVIRTUAL(merge));
		
	    nextNode.setTarget(il.append(methodGen.loadIterator()));
	    il.append(methodGen.nextNode());
	    il.append(DUP);
	    il.append(methodGen.storeCurrentNode());
	    il.append(new IFGT(loop));


	    il.append(methodGen.storeIterator());
	    il.append(methodGen.storeCurrentNode());

	    il.append(new ALOAD(returnIndex.getIndex()));
	}
	else {
	    il.append(classGen.loadTranslet());
	    if (_name == null) {
		il.append(new PUSH(cpg,"##id"));
	    }
	    else if (_resolvedQName != null) {
		il.append(new PUSH(cpg, _resolvedQName.toString()));
	    }
	    else {
		_name.translate(classGen, methodGen);
	    }
	    il.append(new INVOKEVIRTUAL(getKeyIndex));

	    il.append(DUP);

	    _value.translate(classGen, methodGen);

	    if (_name == null) {
		il.append(new INVOKEVIRTUAL(lookupId));
	    }
	    else {
		il.append(new INVOKEVIRTUAL(lookupKey));
	    }
	}
    }
}
