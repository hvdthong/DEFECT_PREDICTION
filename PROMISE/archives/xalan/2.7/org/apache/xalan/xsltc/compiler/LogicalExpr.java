package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class LogicalExpr extends Expression {

    public static final int OR  = 0;
    public static final int AND = 1;
	

    private static final String[] Ops = { "or", "and" };

    /**
     * Creates a new logical expression - either OR or AND. Note that the
     * left- and right-hand side expressions can also be logical expressions,
     * thus creating logical trees representing structures such as
     * (a and (b or c) and d), etc...
     */
    public LogicalExpr(int op, Expression left, Expression right) {
	_op = op;
	(_left = left).setParent(this);
	(_right = right).setParent(this);
    }

    /**
     * Returns true if this expressions contains a call to position(). This is
     * needed for context changes in node steps containing multiple predicates.
     */
    public boolean hasPositionCall() {
	return (_left.hasPositionCall() || _right.hasPositionCall());
    }

    /**
     * Returns true if this expressions contains a call to last()
     */
    public boolean hasLastCall() {
            return (_left.hasLastCall() || _right.hasLastCall());
    }
    
    /**
     * Returns an object representing the compile-time evaluation 
     * of an expression. We are only using this for function-available
     * and element-available at this time.
     */
    public Object evaluateAtCompileTime() {
	final Object leftb = _left.evaluateAtCompileTime();
	final Object rightb = _right.evaluateAtCompileTime();

	if (leftb == null || rightb == null) {
	    return null;
	}

	if (_op == AND) {
	    return (leftb == Boolean.TRUE && rightb == Boolean.TRUE) ?
		Boolean.TRUE : Boolean.FALSE;
	}
	else {
	    return (leftb == Boolean.TRUE || rightb == Boolean.TRUE) ?
		Boolean.TRUE : Boolean.FALSE;
	}
    }

    /**
     * Returns this logical expression's operator - OR or AND represented
     * by 0 and 1 respectively.
     */
    public int getOp() {
	return(_op);
    }

    /**
     * Override the SyntaxTreeNode.setParser() method to make sure that the
     * parser is set for sub-expressions
     */
    public void setParser(Parser parser) {
	super.setParser(parser);
	_left.setParser(parser);
	_right.setParser(parser);
    }

    /**
     * Returns a string describing this expression
     */
    public String toString() {
	return Ops[_op] + '(' + _left + ", " + _right + ')';
    }

    /**
     * Type-check this expression, and possibly child expressions.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	Type tleft = _left.typeCheck(stable); 
	Type tright = _right.typeCheck(stable);

	MethodType wantType = new MethodType(Type.Void, tleft, tright);
	MethodType haveType = lookupPrimop(stable, Ops[_op], wantType);

	if (haveType != null) {
	    Type arg1 = (Type)haveType.argsType().elementAt(0);
	    if (!arg1.identicalTo(tleft))
		_left = new CastExpr(_left, arg1);
	    Type arg2 = (Type) haveType.argsType().elementAt(1);
	    if (!arg2.identicalTo(tright))
		_right = new CastExpr(_right, arg1);
	    return _type = haveType.resultType();
	}
	throw new TypeCheckError(this);
    }

    /**
     * Compile the expression - leave boolean expression on stack
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	translateDesynthesized(classGen, methodGen);
	synthesize(classGen, methodGen);
    }

    /**
     * Compile expression and update true/false-lists
     */
    public void translateDesynthesized(ClassGenerator classGen,
				       MethodGenerator methodGen) {

	final InstructionList il = methodGen.getInstructionList();
	final SyntaxTreeNode parent = getParent();

	if (_op == AND) {

	    _left.translateDesynthesized(classGen, methodGen);

	    InstructionHandle middle = il.append(NOP);

	    _right.translateDesynthesized(classGen, methodGen);

	    InstructionHandle after = il.append(NOP);

	    _falseList.append(_right._falseList.append(_left._falseList));

	    if ((_left instanceof LogicalExpr) &&
		(((LogicalExpr)_left).getOp() == OR)) {
		_left.backPatchTrueList(middle);
	    }
	    else if (_left instanceof NotCall) {
		_left.backPatchTrueList(middle);
	    }
	    else {
		_trueList.append(_left._trueList);
	    }

	    if ((_right instanceof LogicalExpr) &&
		(((LogicalExpr)_right).getOp() == OR)) {
		_right.backPatchTrueList(after);
	    }
	    else if (_right instanceof NotCall) {
		_right.backPatchTrueList(after);
	    }
	    else {
		_trueList.append(_right._trueList);
	    }
	} 
	else {
	    _left.translateDesynthesized(classGen, methodGen);

	    InstructionHandle ih = il.append(new GOTO(null));

	    _right.translateDesynthesized(classGen, methodGen);

	    _left._trueList.backPatch(ih);
	    _left._falseList.backPatch(ih.getNext());
			
	    _falseList.append(_right._falseList);
	    _trueList.add(ih).append(_right._trueList);
	}
    }
}
