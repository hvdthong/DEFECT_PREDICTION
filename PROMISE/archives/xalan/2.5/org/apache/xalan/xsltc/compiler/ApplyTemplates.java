package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class ApplyTemplates extends Instruction {
    private Expression _select;
    private Type       _type = null;
    private QName      _modeName;
    private String     _functionName;
	
    public void display(int indent) {
	indent(indent);
	Util.println("ApplyTemplates");
	indent(indent + IndentIncrement);
	Util.println("select " + _select.toString());
	if (_modeName != null) {
	    indent(indent + IndentIncrement);
	    Util.println("mode " + _modeName);
	}
    }

    public boolean hasWithParams() {
	return hasContents();
    }

    public void parseContents(Parser parser) {
	final String select = getAttribute("select");
	final String mode   = getAttribute("mode");
	
	if (select.length() > 0) {
	    _select = parser.parseExpression(this, "select", null);

	    final Expression fpe = new ForwardPositionExpr(_select);
	    _select.setParent(fpe);
	    fpe.setParser(_select.getParser());
	    _select = fpe;
	}
	
	if (mode.length() > 0) {
	    _modeName = parser.getQNameIgnoreDefaultNs(mode);
	}
	
	_functionName =
	    parser.getTopLevelStylesheet().getMode(_modeName).functionName();
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_select != null) {
	    _type = _select.typeCheck(stable);
	    if (_type instanceof NodeType || _type instanceof ReferenceType) {
		_select = new CastExpr(_select, Type.NodeSet);
		_type = Type.NodeSet;
	    }
	    if (_type instanceof NodeSetType||_type instanceof ResultTreeType) {
		return Type.Void;
	    }
	    throw new TypeCheckError(this);
	}
	else {
	    return Type.Void;
	}
    }

    /**
     * Translate call-template. A parameter frame is pushed only if
     * some template in the stylesheet uses parameters. 
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	boolean setStartNodeCalled = false;
	final Stylesheet stylesheet = classGen.getStylesheet();
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final int current = methodGen.getLocalIndex("current");

	final Vector sortObjects = new Vector();
	final Enumeration children = elements();
	while (children.hasMoreElements()) {
	    final Object child = children.nextElement();
	    if (child instanceof Sort) {
		sortObjects.addElement(child);
	    }
	}

	if (stylesheet.hasLocalParams() || hasContents()) {
	    il.append(classGen.loadTranslet());
	    final int pushFrame = cpg.addMethodref(TRANSLET_CLASS,
						   PUSH_PARAM_FRAME,
						   PUSH_PARAM_FRAME_SIG);
	    il.append(new INVOKEVIRTUAL(pushFrame));
	    translateContents(classGen, methodGen);
	}


	il.append(classGen.loadTranslet());

	if ((_type != null) && (_type instanceof ResultTreeType)) {
	    if (sortObjects.size() > 0) {
		ErrorMsg err = new ErrorMsg(ErrorMsg.RESULT_TREE_SORT_ERR,this);
		getParser().reportError(WARNING, err);
	    }
	    _select.translate(classGen, methodGen);	
	    _type.translateTo(classGen, methodGen, Type.NodeSet);
	}
	else {
	    il.append(methodGen.loadDOM());

	    if (sortObjects.size() > 0) {
		Sort.translateSortIterator(classGen, methodGen,
					   _select, sortObjects);
		int setStartNode = cpg.addInterfaceMethodref(NODE_ITERATOR,
							     SET_START_NODE,
							     "(I)"+
							     NODE_ITERATOR_SIG);
		il.append(methodGen.loadCurrentNode());
		il.append(new INVOKEINTERFACE(setStartNode,2));
		setStartNodeCalled = true;	
	    }
	    else {
		if (_select == null)
		    Mode.compileGetChildren(classGen, methodGen, current);
		else
		    _select.translate(classGen, methodGen);
	    }
	}

	if (_select != null && !setStartNodeCalled) {
	    _select.startResetIterator(classGen, methodGen);
	}

	final String className = classGen.getStylesheet().getClassName();
	il.append(methodGen.loadHandler());
	final String applyTemplatesSig = classGen.getApplyTemplatesSig();
	final int applyTemplates = cpg.addMethodref(className,
						    _functionName,
						    applyTemplatesSig);
	il.append(new INVOKEVIRTUAL(applyTemplates));
	
	if (stylesheet.hasLocalParams() || hasContents()) {
	    il.append(classGen.loadTranslet());
	    final int popFrame = cpg.addMethodref(TRANSLET_CLASS,
						  POP_PARAM_FRAME,
						  POP_PARAM_FRAME_SIG);
	    il.append(new INVOKEVIRTUAL(popFrame));
	}
    }
}
