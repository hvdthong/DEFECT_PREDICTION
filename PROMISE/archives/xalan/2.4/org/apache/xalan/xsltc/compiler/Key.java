package org.apache.xalan.xsltc.compiler;

import javax.xml.parsers.*;

import org.xml.sax.*;

import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;
import org.apache.xalan.xsltc.compiler.util.Type;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.dom.Axis;

final class Key extends TopLevelElement {


    /**
     * Parse the <xsl:key> element and attributes
     * @param parser A reference to the stylesheet parser
     */
    public void parseContents(Parser parser) {

	_name = parser.getQNameIgnoreDefaultNs(getAttribute("name"));
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

    /**
     * Run type check on the "use" attribute and make sure it is something
     * we can use to extract some value from nodes.
     * @param stable The stylesheet parser's symbol table
     * @return The data-type of this key (always void)
     * @throws TypeCheckError If the use attribute does not represent a string,
     *   a node-set or a number
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	_match.typeCheck(stable);
	_useType = _use.typeCheck(stable);

	if (_useType instanceof NodeType) {
	    _use = new CastExpr(_use, Type.String);
	    _useType = Type.String;
	}

	if (!(_useType instanceof StringType) &&
	    !(_useType instanceof NodeSetType) &&
	    !(_useType instanceof RealType)) {
	    ErrorMsg err = new ErrorMsg(ErrorMsg.KEY_USE_ATTR_ERR, this);
	    throw new TypeCheckError(err);
	}

	return Type.Void;
    }

    /**
     * This method is called if the "use" attribute of the key contains a
     * node set. In this case we must traverse all nodes in the set and
     * create one entry in this key's index for each node in the set.
     * @param classGen The Java class generator
     * @param methodGen The method generator
     */
    public void traverseNodeSet(ClassGenerator classGen,
				MethodGenerator methodGen,
				int buildKeyIndex) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int getNodeValue = cpg.addInterfaceMethodref(DOM_INTF,
							   "getNodeValue",
							   "(I)"+STRING_SIG);

	final LocalVariableGen parentNode =
	    methodGen.addLocalVariable("parentNode",
				       Util.getJCRefType("I"),
				       il.getEnd(), null);

	il.append(new ISTORE(parentNode.getIndex()));

	il.append(methodGen.loadCurrentNode());
	il.append(methodGen.loadIterator());

	_use.translate(classGen, methodGen);
	_use.startResetIterator(classGen, methodGen);
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
     * @param classGen The Java class generator
     * @param methodGen The method generator
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {

	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final int current = methodGen.getLocalIndex("current");

	final int key = cpg.addMethodref(TRANSLET_CLASS,
					 "buildKeyIndex",
					 "("+STRING_SIG+"I"+OBJECT_SIG+")V");

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
	
	if (_useType instanceof RealType) {
	    final int dbl = cpg.addMethodref(DOUBLE_CLASS,"<init>", "(D)V");

	    il.append(classGen.loadTranslet());
	    il.append(new PUSH(cpg, _name.toString()));
	    il.append(methodGen.loadCurrentNode());
	    il.append(new NEW(cpg.addClass(DOUBLE_CLASS)));
	    il.append(DUP);
	    _use.translate(classGen,methodGen);
	    il.append(new INVOKESPECIAL(dbl));
	    il.append(new INVOKEVIRTUAL(key));

	}
	else if (_useType instanceof StringType) {
	    il.append(classGen.loadTranslet());
	    il.append(new PUSH(cpg, _name.toString()));
	    il.append(methodGen.loadCurrentNode());
	    _use.translate(classGen,methodGen);
	    il.append(new INVOKEVIRTUAL(key));
	}
	else {
	    il.append(methodGen.loadCurrentNode());
	    traverseNodeSet(classGen,methodGen,key);
	}
	
	final InstructionHandle skip = il.append(NOP);
	
	il.append(methodGen.loadIterator());
	il.append(methodGen.nextNode());
	il.append(DUP);
	il.append(methodGen.storeCurrentNode());
	il.append(new IFNE(loop));

	il.append(methodGen.storeIterator());
	il.append(methodGen.storeCurrentNode());
	
	nextNode.setTarget(skip);
	skipNode.setTarget(skip);
    }
}
