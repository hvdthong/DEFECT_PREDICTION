package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class FilterParentPath extends Expression {

    private Expression _filterExpr;
    private Expression _path;
    private boolean _hasDescendantAxis = false;

    public FilterParentPath(Expression filterExpr, Expression path) {
	(_path = path).setParent(this);
	(_filterExpr = filterExpr).setParent(this);
    }
		
    public void setParser(Parser parser) {
	super.setParser(parser);
	_filterExpr.setParser(parser);
	_path.setParser(parser);
    }
    
    public String toString() {
	return "FilterParentPath(" + _filterExpr + ", " + _path + ')';
    }

    public void setDescendantAxis() {
	_hasDescendantAxis = true;
    }

    /**
     * Type check a FilterParentPath. If the filter is not a node-set add a 
     * cast to node-set only if it is of reference type. This type coercion is
     * needed for expressions like $x/LINE where $x is a parameter reference.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Type ftype = _filterExpr.typeCheck(stable);
	if (ftype instanceof NodeSetType == false) {
	    if (ftype instanceof ReferenceType)  {
		_filterExpr = new CastExpr(_filterExpr, Type.NodeSet);
	    }
	    /*
	    else if (ftype instanceof ResultTreeType)  {
		_filterExpr = new CastExpr(_filterExpr, Type.NodeSet);
	    }
	    */
	    else if (ftype instanceof NodeType)  {
		_filterExpr = new CastExpr(_filterExpr, Type.NodeSet);
	    }
	    else {
		throw new TypeCheckError(this);
	    }
	}

	final Type ptype = _path.typeCheck(stable);
	if (!(ptype instanceof NodeSetType)) {
	    _path = new CastExpr(_path, Type.NodeSet);
	}

	return _type = Type.NodeSet;	
    }
	
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final int initSI = cpg.addMethodref(STEP_ITERATOR_CLASS,
					    "<init>",
					    "("
					    +NODE_ITERATOR_SIG
					    +NODE_ITERATOR_SIG
					    +")V");
	il.append(new NEW(cpg.addClass(STEP_ITERATOR_CLASS)));
	il.append(DUP);

	_filterExpr.translate(classGen, methodGen);
	_path.translate(classGen, methodGen);

	il.append(new INVOKESPECIAL(initSI));

        if (_hasDescendantAxis) {
	    final int incl = cpg.addMethodref(NODE_ITERATOR_BASE,
					      "includeSelf",
					      "()" + NODE_ITERATOR_SIG);
	    il.append(new INVOKEVIRTUAL(incl));
	}

	if (!(getParent() instanceof RelativeLocationPath) &&
	    !(getParent() instanceof FilterParentPath)) {
	    final int order = cpg.addInterfaceMethodref(DOM_INTF,
							ORDER_ITERATOR,
							ORDER_ITERATOR_SIG);
	    il.append(methodGen.loadDOM());
	    il.append(SWAP);
	    il.append(methodGen.loadContextNode());
	    il.append(new INVOKEINTERFACE(order, 3));
	}
    }
}
