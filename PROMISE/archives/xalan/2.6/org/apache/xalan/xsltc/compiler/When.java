package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class When extends Instruction {

    private Expression _test;
    private boolean _ignore = false;

    public void display(int indent) {
	indent(indent);
	Util.println("When");
	indent(indent + IndentIncrement);
	System.out.print("test ");
	Util.println(_test.toString());
	displayContents(indent + IndentIncrement);
    }
		
    public Expression getTest() {
	return _test;
    }

    public boolean ignore() {
	return(_ignore);
    }

    public void parseContents(Parser parser) {
	_test = parser.parseExpression(this, "test", null);

	Object result = _test.evaluateAtCompileTime();
	if (result != null && result instanceof Boolean) {
	    _ignore = !((Boolean) result).booleanValue();
	}

	parseChildren(parser);

	if (_test.isDummy()) {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "test");
	}
    }

    /**
     * Type-check this when element. The test should always be type checked,
     * while we do not bother with the contents if we know the test fails.
     * This is important in cases where the "test" expression tests for
     * the support of a non-available element, and the <xsl:when> body contains
     * this non-available element.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_test.typeCheck(stable) instanceof BooleanType == false) {
	    _test = new CastExpr(_test, Type.Boolean);
	}
	if (!_ignore) {
	    typeCheckContents(stable);
	}

	return Type.Void;
    }

    /**
     * This method should never be called. An Otherwise object will explicitly
     * translate the "test" expression and and contents of this element.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ErrorMsg msg = new ErrorMsg(ErrorMsg.STRAY_WHEN_ERR, this);
	getParser().reportError(Constants.ERROR, msg);
    }
}
