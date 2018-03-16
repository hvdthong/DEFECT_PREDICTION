package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class UnresolvedRef extends VariableRefBase {

    private QName           _variableName = null;
    private VariableRefBase _ref = null;
    private VariableBase    _var = null;
    private Stylesheet      _sheet = null;

    public UnresolvedRef(QName name) {
	super();
	_variableName = name;
	_sheet = getStylesheet();
    }

    public QName getName() {
	return(_variableName);
    }

    private ErrorMsg reportError() {
	ErrorMsg err = new ErrorMsg(ErrorMsg.VARIABLE_UNDEF_ERR,
				    _variableName, this);
	getParser().reportError(Constants.ERROR, err);
	return(err);
    }

    private VariableRefBase resolve(Parser parser, SymbolTable stable) {
	VariableBase ref = parser.lookupVariable(_variableName);
	if (ref == null) ref = (VariableBase)stable.lookupName(_variableName);
	if (ref == null) {
	    reportError();
	    return null;
	}
	
	if ((_var = findParentVariable()) != null) _var.addDependency(ref);

	if (ref instanceof Variable)
	    return(new VariableRef((Variable)ref));
	else if (ref instanceof Param)
	    return(new ParameterRef((Param)ref));
	else
	    return null;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_ref != null) {
	    final String name = _variableName.toString();
	    ErrorMsg err = new ErrorMsg(ErrorMsg.CIRCULAR_VARIABLE_ERR,
					name, this);
	}
	if ((_ref = resolve(getParser(), stable)) != null) {
	    return (_type = _ref.typeCheck(stable));
	}
	throw new TypeCheckError(reportError());
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	if (_ref != null)
	    _ref.translate(classGen, methodGen);
	else
	    reportError();
    }

    public String toString() {
	return "unresolved-ref()";
    }

}
