package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xalan.xsltc.dom.Axis;
import org.apache.xml.utils.XMLChar;

/**
 * @author Morten Jorgensen
 * @author Santiago Pericas-Geertsen
 */
final class Key extends TopLevelElement {

    /**
     * The name of this key as defined in xsl:key.
     */
    private QName _name;

    /**
     * The pattern to match starting at the root node.
     */
    private Pattern _match; 

    /**
     * The expression that generates the values for this key.
     */
    private Expression _use;

    /**
     * The type of the _use expression.
     */
    private Type _useType;

    /**
     * Parse the <xsl:key> element and attributes
     * @param parser A reference to the stylesheet parser
     */
    public void parseContents(Parser parser) {

        final String name = getAttribute("name");
        if (!XMLChar.isValidQName(name)){
            ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_QNAME_ERR, name, this);
            parser.reportError(Constants.ERROR, err);           
        }
        _name = parser.getQNameIgnoreDefaultNs(name);
	_match = parser.parsePattern(this, "match", null);
	_use = parser.parseExpression(this, "use", null);

        if (_name == null) {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");
	    return;
        }
        if (_match.isDummy()) {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "match");
	    return;
        }
        if (_use.isDummy()) {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "use");
	    return;
        }
    }

    /**
     * Returns a String-representation of this key's name
     * @return The key's name (from the <xsl:key> elements 'name' attribute).
     */
    public String getName() {
	return _name.toString();
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	_match.typeCheck(stable);

	_useType = _use.typeCheck(stable);
	if (_useType instanceof StringType == false &&
	    _useType instanceof NodeSetType == false) 
	{
	    _use = new CastExpr(_use, Type.String);
	}

	return Type.Void;
    }

    /**
     * This method is called if the "use" attribute of the key contains a
     * node set. In this case we must traverse all nodes in the set and
     * create one entry in this key's index for each node in the set.
     */
    public void traverseNodeSet(ClassGenerator classGen,
				MethodGenerator methodGen,
				int buildKeyIndex) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int getNodeValue = cpg.addInterfaceMethodref(DOM_INTF,
							   GET_NODE_VALUE,
							   "(I)"+STRING_SIG);
							   
	final int getNodeIdent = cpg.addInterfaceMethodref(DOM_INTF,
							   "getNodeIdent",
							   "(I)"+NODE_SIG);	
							   
	final int keyDom = cpg.addMethodref(TRANSLET_CLASS,
					 "setKeyIndexDom",
					 "("+STRING_SIG+DOM_INTF_SIG+")V");				 
						   					   

	final LocalVariableGen parentNode =
	    methodGen.addLocalVariable("parentNode",
				       Util.getJCRefType("I"),
				       il.getEnd(), null);

	il.append(new ISTORE(parentNode.getIndex()));	
	il.append(methodGen.loadDOM());
	il.append(new ILOAD(parentNode.getIndex()));	
	il.append(new INVOKEINTERFACE(getNodeIdent, 2));
	il.append(new ISTORE(parentNode.getIndex()));

	il.append(methodGen.loadCurrentNode());
	il.append(methodGen.loadIterator());

	_use.translate(classGen, methodGen);
	_use.startIterator(classGen, methodGen);
	il.append(methodGen.storeIterator());

	final BranchHandle nextNode = il.append(new GOTO(null));
	final InstructionHandle loop = il.append(NOP);

	il.append(classGen.loadTranslet());
	il.append(new PUSH(cpg, _name.toString()));
	il.append(new ILOAD(parentNode.getIndex()));

	il.append(methodGen.loadDOM());
	il.append(methodGen.loadCurrentNode());
	il.append(new INVOKEINTERFACE(getNodeValue, 2));		

	il.append(new INVOKEVIRTUAL(buildKeyIndex));
	
	il.append(classGen.loadTranslet());
	il.append(new PUSH(cpg, getName()));
	il.append(methodGen.loadDOM());
	il.append(new INVOKEVIRTUAL(keyDom));

	nextNode.setTarget(il.append(methodGen.loadIterator()));
	il.append(methodGen.nextNode());	

	il.append(DUP);
	il.append(methodGen.storeCurrentNode());

	il.append(methodGen.storeIterator());
	il.append(methodGen.storeCurrentNode());
    }

    /**
     * Gather all nodes that match the expression in the attribute "match"
     * and add one (or more) entries in this key's index.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {

	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final int current = methodGen.getLocalIndex("current");

	final int key = cpg.addMethodref(TRANSLET_CLASS,
					 "buildKeyIndex",
					 "("+STRING_SIG+"I"+OBJECT_SIG+")V");
					 
	final int keyDom = cpg.addMethodref(TRANSLET_CLASS,
					 "setKeyIndexDom",
					 "("+STRING_SIG+DOM_INTF_SIG+")V");
					 
	final int getNodeIdent = cpg.addInterfaceMethodref(DOM_INTF,
							   "getNodeIdent",
							   "(I)"+NODE_SIG);					 				 

	final int git = cpg.addInterfaceMethodref(DOM_INTF,
						  "getAxisIterator",
						  "(I)"+NODE_ITERATOR_SIG);

	il.append(methodGen.loadCurrentNode());
	il.append(methodGen.loadIterator());

	il.append(methodGen.loadDOM());	
	il.append(new PUSH(cpg,Axis.DESCENDANT));
	il.append(new INVOKEINTERFACE(git, 2));

	il.append(methodGen.loadCurrentNode());
	il.append(methodGen.setStartNode());
	il.append(methodGen.storeIterator());

	final BranchHandle nextNode = il.append(new GOTO(null));
	final InstructionHandle loop = il.append(NOP);

	il.append(methodGen.loadCurrentNode());
	_match.translate(classGen, methodGen);
	final BranchHandle skipNode = il.append(new IFEQ(null));
	
	if (_useType instanceof NodeSetType) {
	    il.append(methodGen.loadCurrentNode());
	    traverseNodeSet(classGen, methodGen, key);
	}
	else {
	    il.append(classGen.loadTranslet());
	    il.append(DUP);
	    il.append(new PUSH(cpg, _name.toString()));
	    il.append(DUP_X1);
	    il.append(methodGen.loadCurrentNode());
	    _use.translate(classGen, methodGen);
	    il.append(SWAP);
	    il.append(methodGen.loadDOM());
	    il.append(SWAP);
	    il.append(new INVOKEINTERFACE(getNodeIdent, 2));
	    il.append(SWAP);
	    il.append(new INVOKEVIRTUAL(key));
	    
	    il.append(methodGen.loadDOM());
	    il.append(new INVOKEVIRTUAL(keyDom));
	}
	
	final InstructionHandle skip = il.append(NOP);
	
	il.append(methodGen.loadIterator());
	il.append(methodGen.nextNode());
	il.append(DUP);
	il.append(methodGen.storeCurrentNode());
	il.append(new IFGT(loop));

	il.append(methodGen.storeIterator());
	il.append(methodGen.storeCurrentNode());
	
	nextNode.setTarget(skip);
	skipNode.setTarget(skip);
    }
}
