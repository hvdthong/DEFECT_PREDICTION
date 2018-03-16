package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
class FilterExpr extends Expression {
    
    /**
     * Primary expression of this filter. I.e., 'e' in '(e)[p1]...[pn]'.
     */
    private Expression   _primary;
    
    /**
     * Array of predicates in '(e)[p1]...[pn]'.
     */
    private final Vector _predicates;

    public FilterExpr(Expression primary, Vector predicates) {
	_primary = primary;
	_predicates = predicates;
	primary.setParent(this);
    }

    protected Expression getExpr() {
	if (_primary instanceof CastExpr)
	    return ((CastExpr)_primary).getExpr();
	else
	    return _primary;
    }

    public void setParser(Parser parser) {
	super.setParser(parser);
	_primary.setParser(parser);
	if (_predicates != null) {
	    final int n = _predicates.size();
	    for (int i = 0; i < n; i++) {
		final Expression exp = (Expression)_predicates.elementAt(i);
		exp.setParser(parser);
		exp.setParent(this);
	    }
	}
    }
    
    public String toString() {
	return "filter-expr(" + _primary + ", " + _predicates + ")";
    }

    /**
     * Type check a FilterParentPath. If the filter is not a node-set add a 
     * cast to node-set only if it is of reference type. This type coercion 
     * is needed for expressions like $x where $x is a parameter reference.
     * All optimizations are turned off before type checking underlying
     * predicates.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	Type ptype = _primary.typeCheck(stable);

	if (ptype instanceof NodeSetType == false) {
	    if (ptype instanceof ReferenceType)  {
		_primary = new CastExpr(_primary, Type.NodeSet);
	    }
	    else {
		throw new TypeCheckError(this);
	    }
	}

	int n = _predicates.size();
	for (int i = 0; i < n; i++) {
	    Predicate pred = (Predicate) _predicates.elementAt(i);
            pred.dontOptimize();
	    pred.typeCheck(stable);
	}
	return _type = Type.NodeSet;	
    }
	
    /**
     * Translate a filter expression by pushing the appropriate iterator
     * onto the stack.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	if (_predicates.size() > 0) {
	    translatePredicates(classGen, methodGen);
	}
	else {
	    _primary.translate(classGen, methodGen);
	}
    }

    /**
     * Translate a sequence of predicates. Each predicate is translated 
     * by constructing an instance of <code>CurrentNodeListIterator</code> 
     * which is initialized from another iterator (recursive call), a 
     * filter and a closure (call to translate on the predicate) and "this". 
     */
    public void translatePredicates(ClassGenerator classGen,
				    MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (_predicates.size() == 0) {
	    translate(classGen, methodGen);
	}
	else {
	    final int initCNLI = cpg.addMethodref(CURRENT_NODE_LIST_ITERATOR,
						  "<init>",
						  "("+NODE_ITERATOR_SIG+"Z"+
						  CURRENT_NODE_LIST_FILTER_SIG +
						  NODE_SIG+TRANSLET_SIG+")V");


            Predicate predicate = (Predicate)_predicates.lastElement();
            _predicates.remove(predicate);

            translatePredicates(classGen, methodGen);

            LocalVariableGen nodeIteratorTemp =
                methodGen.addLocalVariable("filter_expr_tmp1",
                                           Util.getJCRefType(NODE_ITERATOR_SIG),
                                           il.getEnd(), null);
            il.append(new ASTORE(nodeIteratorTemp.getIndex()));

            predicate.translate(classGen, methodGen);
            LocalVariableGen filterTemp =
                methodGen.addLocalVariable("filter_expr_tmp2",
                              Util.getJCRefType(CURRENT_NODE_LIST_FILTER_SIG),
                              il.getEnd(), null);
            il.append(new ASTORE(filterTemp.getIndex()));

            il.append(new NEW(cpg.addClass(CURRENT_NODE_LIST_ITERATOR)));
            il.append(DUP);
            
            il.append(new ALOAD(nodeIteratorTemp.getIndex()));
            il.append(ICONST_1);
            il.append(new ALOAD(filterTemp.getIndex()));
            il.append(methodGen.loadCurrentNode());
            il.append(classGen.loadTranslet());
            il.append(new INVOKESPECIAL(initCNLI));
	}
    }
}
