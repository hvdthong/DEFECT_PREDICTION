package org.apache.xalan.xsltc.compiler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xalan.xsltc.runtime.TransletLoader;

final class FunctionAvailableCall extends FunctionCall {

    private Expression _arg; 
    private String     _nameOfFunct = null; 
    private String     _namespaceOfFunct = null; 	
    private boolean    _isFunctionAvailable = false; 

    /**
     * Constructs a FunctionAvailableCall FunctionCall. Takes the
     * function name qname, for example, 'function-available', and 
     * a list of arguments where the arguments must be instances of 
     * LiteralExpression. 
     */
    public FunctionAvailableCall(QName fname, Vector arguments) {
	super(fname, arguments);
	_arg = (Expression)arguments.elementAt(0);
	_type = null; 

        if (_arg instanceof LiteralExpr) {
	    LiteralExpr arg = (LiteralExpr) _arg;
            _namespaceOfFunct = arg.getNamespace();
            _nameOfFunct = arg.getValue();

            if (!isInternalNamespace()) {
              _isFunctionAvailable = hasMethods();
            }
        }
    }

    /**
     * Argument of function-available call must be literal, typecheck
     * returns the type of function-available to be boolean.  
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_type != null) {
	   return _type;
	}
	if (_arg instanceof LiteralExpr) {
	    return _type = Type.Boolean;
	}
	ErrorMsg err = new ErrorMsg(ErrorMsg.NEED_LITERAL_ERR,
			"function-available", this);
	throw new TypeCheckError(err);
    }

    /**
     * Returns an object representing the compile-time evaluation 
     * of an expression. We are only using this for function-available
     * and element-available at this time.
     */
    public Object evaluateAtCompileTime() {
	return getResult() ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * for external java functions only: reports on whether or not
     * the specified method is found in the specifed class. 
     */
    private boolean hasMethods() {
	LiteralExpr arg = (LiteralExpr)_arg;
	
	String className = getClassNameFromUri(_namespaceOfFunct);
	
	String methodName = null;
	int colonIndex = _nameOfFunct.indexOf(":");
	if (colonIndex > 0) {
	  String functionName = _nameOfFunct.substring(colonIndex+1);
	  int lastDotIndex = functionName.lastIndexOf('.');
	  if (lastDotIndex > 0) {
	    methodName = functionName.substring(lastDotIndex+1);
	    if (className != null && !className.equals(""))
	      className = className + "." + functionName.substring(0, lastDotIndex);
	    else
	      className = functionName.substring(0, lastDotIndex);
	  }
	  else
	    methodName = functionName;
	}
	else
	  methodName = _nameOfFunct;
	  
	if (className == null || methodName == null) {
	    return false;
	}
	
	if (methodName.indexOf('-') > 0)
	  methodName = replaceDash(methodName);

	try {
	    TransletLoader loader = new TransletLoader();
	    final Class clazz = loader.loadClass(className);

	    if (clazz == null) {
	    	return false;
	    }
	    else {
		final Method[] methods = clazz.getMethods();

		for (int i = 0; i < methods.length; i++) {
		    final int mods = methods[i].getModifiers();

		    if (Modifier.isPublic(mods)
			&& Modifier.isStatic(mods)
			&& methods[i].getName().equals(methodName))
		    {
			return true;
		    }
		}
	    }
	}
	catch (ClassNotFoundException e) {
	  return false;
	}
        return false;   
    }

    /**
     * Reports on whether the function specified in the argument to
     * xslt function 'function-available' was found.
     */
    public boolean getResult() {
	if (_nameOfFunct == null) { 
	    return false;
	}

        if (isInternalNamespace()) {
            final Parser parser = getParser();
            _isFunctionAvailable = 
		parser.functionSupported(Util.getLocalName(_nameOfFunct));
        }
 	return _isFunctionAvailable;
    }
    
    /**
     * Return true if the namespace uri is null or it is the XSLTC translet uri.
     */
    private boolean isInternalNamespace() {
    	return (_namespaceOfFunct == null ||
            _namespaceOfFunct.equals(EMPTYSTRING) ||
	    _namespaceOfFunct.equals(TRANSLET_URI));
    }

    /**
     * Calls to 'function-available' are resolved at compile time since 
     * the namespaces declared in the stylsheet are not available at run
     * time. Consequently, arguments to this function must be literals.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	methodGen.getInstructionList().append(new PUSH(cpg, getResult()));
    }

}
