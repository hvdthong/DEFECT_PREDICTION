package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Morten Jorgensen
 * @author Santiago Pericas-Geertsen
 */
class VariableRefBase extends Expression {

    /**
     * A reference to the associated variable.
     */
    protected final VariableBase _variable; 

    /**
     * A reference to the enclosing expression/instruction for which a
     * closure is needed (Predicate, Number or Sort).
     */
    protected Closure _closure = null;

    public VariableRefBase(VariableBase variable) {
	_variable = variable;
	variable.addReference(this);
    }

    public VariableRefBase() {
	_variable = null;
    }

    /**
     * Returns a reference to the associated variable
     */
    public VariableBase getVariable() {
	return _variable;
    }

    /**
     * Returns a reference to any parent variable
     */
    public VariableBase findParentVariable() {
	SyntaxTreeNode node = this;
	while (node != null && !(node instanceof VariableBase)) {
	    node = node.getParent();
	}
	return (VariableBase) node;
    }

    /**
     * Two variable references are deemed equal if they refer to the 
     * same variable.
     */
    public boolean equals(Object obj) {
	try {
	    return (_variable == ((VariableRefBase) obj)._variable);
	} 
	catch (ClassCastException e) {
	    return false;
	}
    }

    /**
     * Returns a string representation of this variable reference on the
     * format 'variable-ref(<var-name>)'.
     * @return Variable reference description
     */
    public String toString() {
	return "variable-ref("+_variable.getName()+'/'+_variable.getType()+')';
    }

    public Type typeCheck(SymbolTable stable) 
	throws TypeCheckError 
    {
	if (_type != null) return _type;

	if (_variable.isLocal()) {
	    SyntaxTreeNode node = getParent();
	    do {
		if (node instanceof Closure) {
		    _closure = (Closure) node;
		    break;
		}
		if (node instanceof TopLevelElement) {
		}
		node = node.getParent();
	    } while (node != null);

	    if (_closure != null) {
		_closure.addVariable(this);
	    }
	}

        VariableBase parent = findParentVariable();
        if (parent != null) {
            VariableBase var = _variable;
            if (_variable._ignore) {
                if (_variable instanceof Variable) {
                    var = parent.getSymbolTable()
                                .lookupVariable(_variable._name);
                } else if (_variable instanceof Param) {
                    var = parent.getSymbolTable().lookupParam(_variable._name);
                }
            }
            parent.addDependency(var);
        }

        _type = _variable.getType();

        if (_type == null) {
            _variable.typeCheck(stable);
            _type = _variable.getType();
        }

        return _type;
    }

}
