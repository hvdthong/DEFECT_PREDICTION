package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xalan.xsltc.dom.Axis;
import org.apache.xml.dtm.DTM;

class StepPattern extends RelativePathPattern {

    private static final int NO_CONTEXT = 0;
    private static final int SIMPLE_CONTEXT = 1;
    private static final int GENERAL_CONTEXT = 2;

    protected final int _axis;
    protected final int _nodeType;
    protected Vector _predicates;

    private Step    _step = null;
    private boolean _isEpsilon = false;
    private int     _contextCase;

    private double  _priority = Double.MAX_VALUE;

    public StepPattern(int axis, int nodeType, Vector predicates) {
	_axis = axis;
	_nodeType = nodeType;
	_predicates = predicates;
    }

    public void setParser(Parser parser) {
	super.setParser(parser);
	if (_predicates != null) {
	    final int n = _predicates.size();
	    for (int i = 0; i < n; i++) {
		final Predicate exp = (Predicate)_predicates.elementAt(i);
		exp.setParser(parser);
		exp.setParent(this);
	    }
	}
    }

    public int getNodeType() {
	return _nodeType;
    }

    public void setPriority(double priority) {
	_priority = priority;
    }
    
    public StepPattern getKernelPattern() {
	return this;
    }
	
    public boolean isWildcard() {
	return _isEpsilon && hasPredicates() == false;
    }

    public StepPattern setPredicates(Vector predicates) {
	_predicates = predicates;
	return(this);
    }
    
    protected boolean hasPredicates() {
	return _predicates != null && _predicates.size() > 0;
    }

    public double getDefaultPriority() {
	if (_priority != Double.MAX_VALUE) {
	    return _priority;
	}

	if (hasPredicates()) {
	    return 0.5;
	}
	else {
	    switch(_nodeType) {
	    case -1:
	    case 0:
		return 0.0;
	    default:
		return (_nodeType >= NodeTest.GTYPE) ? 0.0 : -0.5;
	    }
	}
    }
    
    public int getAxis() {
	return _axis;
    }

    public void reduceKernelPattern() {
	_isEpsilon = true;
    }
	
    public String toString() {
	final StringBuffer buffer = new StringBuffer("stepPattern(\"");
	buffer.append(Axis.names[_axis])
	    .append("\", ")
	    .append(_isEpsilon ? 
			("epsilon{" + Integer.toString(_nodeType) + "}") :
			 Integer.toString(_nodeType));
	if (_predicates != null)
	    buffer.append(", ").append(_predicates.toString());
	return buffer.append(')').toString();
    }
    
    private int analyzeCases() {
	boolean noContext = true;
	final int n = _predicates.size();

	for (int i = 0; i < n && noContext; i++) {
	    final Predicate pred = (Predicate)_predicates.elementAt(i);
	    if (pred.getExpr().hasPositionCall()
                || pred.isNthPositionFilter()) {
		noContext = false;
	    }
	}

	if (noContext) {
	    return NO_CONTEXT;
	}
	else if (n == 1) {
	    return SIMPLE_CONTEXT;
	}
	return GENERAL_CONTEXT;
    }

    private String getNextFieldName() {
	return  "__step_pattern_iter_" + getXSLTC().nextStepPatternSerial();
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (hasPredicates()) {
            final int n = _predicates.size();
            for (int i = 0; i < n; i++) {
                final Predicate pred = (Predicate)_predicates.elementAt(i);
                pred.typeCheck(stable);
            }

            _contextCase = analyzeCases();

            Step step = null;

            if (_contextCase == SIMPLE_CONTEXT) {
                Predicate pred = (Predicate)_predicates.elementAt(0);
                if (pred.isNthPositionFilter()) {
                    _contextCase = GENERAL_CONTEXT;
                    step = new Step(_axis, _nodeType, _predicates);
                } else {
                    step = new Step(_axis, _nodeType, null);
                }
            } else if (_contextCase == GENERAL_CONTEXT) {
                final int len = _predicates.size();
                for (int i = 0; i < len; i++) {
                    ((Predicate)_predicates.elementAt(i)).dontOptimize();
                }

                step = new Step(_axis, _nodeType, _predicates);
            }
            if (step != null) {
                step.setParser(getParser());
                step.typeCheck(stable);
                _step = step;
            }
        }
        return _axis == Axis.CHILD ? Type.Element : Type.Attribute;
    }

    private void translateKernel(ClassGenerator classGen, 
				 MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	
	if (_nodeType == DTM.ELEMENT_NODE) {
	    final int check = cpg.addInterfaceMethodref(DOM_INTF,
							"isElement", "(I)Z");
	    il.append(methodGen.loadDOM());
	    il.append(SWAP);
	    il.append(new INVOKEINTERFACE(check, 2));
	
	    final BranchHandle icmp = il.append(new IFNE(null));
	    _falseList.add(il.append(new GOTO_W(null)));
	    icmp.setTarget(il.append(NOP));
	}
	else if (_nodeType == DTM.ATTRIBUTE_NODE) {
	    final int check = cpg.addInterfaceMethodref(DOM_INTF,
							"isAttribute", "(I)Z");
	    il.append(methodGen.loadDOM());
	    il.append(SWAP);
	    il.append(new INVOKEINTERFACE(check, 2));
	
	    final BranchHandle icmp = il.append(new IFNE(null));
	    _falseList.add(il.append(new GOTO_W(null)));
	    icmp.setTarget(il.append(NOP));
	}
	else {
	    final int getEType = cpg.addInterfaceMethodref(DOM_INTF,
							  "getExpandedTypeID",
                                                          "(I)I");
	    il.append(methodGen.loadDOM());
	    il.append(SWAP);
	    il.append(new INVOKEINTERFACE(getEType, 2));
	    il.append(new PUSH(cpg, _nodeType));
	
	    final BranchHandle icmp = il.append(new IF_ICMPEQ(null));
	    _falseList.add(il.append(new GOTO_W(null)));
	    icmp.setTarget(il.append(NOP));
	}
    }

    private void translateNoContext(ClassGenerator classGen, 
				    MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	il.append(methodGen.loadCurrentNode());
	il.append(SWAP);

	il.append(methodGen.storeCurrentNode());

	if (!_isEpsilon) {
	    il.append(methodGen.loadCurrentNode());
	    translateKernel(classGen, methodGen);
	}

	final int n = _predicates.size();
	for (int i = 0; i < n; i++) {
	    Predicate pred = (Predicate)_predicates.elementAt(i);
	    Expression exp = pred.getExpr();
	    exp.translateDesynthesized(classGen, methodGen);
	    _trueList.append(exp._trueList);
	    _falseList.append(exp._falseList);
	}

	InstructionHandle restore;
	restore = il.append(methodGen.storeCurrentNode());
	backPatchTrueList(restore);
	BranchHandle skipFalse = il.append(new GOTO(null));

	restore = il.append(methodGen.storeCurrentNode());
	backPatchFalseList(restore);
	_falseList.add(il.append(new GOTO(null)));

	skipFalse.setTarget(il.append(NOP));
    }

    private void translateSimpleContext(ClassGenerator classGen, 
					MethodGenerator methodGen) {
	int index;
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	LocalVariableGen match;
	match = methodGen.addLocalVariable("step_pattern_tmp1", 
					   Util.getJCRefType(NODE_SIG),
					   il.getEnd(), null);
	il.append(new ISTORE(match.getIndex()));

	if (!_isEpsilon) {
	    il.append(new ILOAD(match.getIndex()));
 	    translateKernel(classGen, methodGen);
	}

	il.append(methodGen.loadCurrentNode());
	il.append(methodGen.loadIterator());

	index = cpg.addMethodref(MATCHING_ITERATOR, "<init>", 
				 "(I" + NODE_ITERATOR_SIG + ")V");
	il.append(new NEW(cpg.addClass(MATCHING_ITERATOR)));
	il.append(DUP);
	il.append(new ILOAD(match.getIndex()));
	_step.translate(classGen, methodGen);
	il.append(new INVOKESPECIAL(index));

	il.append(methodGen.loadDOM());
	il.append(new ILOAD(match.getIndex()));
	index = cpg.addInterfaceMethodref(DOM_INTF, GET_PARENT, GET_PARENT_SIG);
	il.append(new INVOKEINTERFACE(index, 2));

	il.append(methodGen.setStartNode());

	il.append(methodGen.storeIterator());
	il.append(new ILOAD(match.getIndex()));
	il.append(methodGen.storeCurrentNode());

	Predicate pred = (Predicate) _predicates.elementAt(0);
	Expression exp = pred.getExpr();
	exp.translateDesynthesized(classGen, methodGen);

	InstructionHandle restore = il.append(methodGen.storeIterator());
	il.append(methodGen.storeCurrentNode());
	exp.backPatchTrueList(restore);
	BranchHandle skipFalse = il.append(new GOTO(null));

	restore = il.append(methodGen.storeIterator());
	il.append(methodGen.storeCurrentNode());
	exp.backPatchFalseList(restore);
	_falseList.add(il.append(new GOTO(null)));

	skipFalse.setTarget(il.append(NOP));
    }

    private void translateGeneralContext(ClassGenerator classGen, 
					 MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	int iteratorIndex = 0;
	BranchHandle ifBlock = null;
	LocalVariableGen iter, node, node2;
	final String iteratorName = getNextFieldName();

	node = methodGen.addLocalVariable("step_pattern_tmp1", 
					  Util.getJCRefType(NODE_SIG),
					  il.getEnd(), null);
	il.append(new ISTORE(node.getIndex()));

	iter = methodGen.addLocalVariable("step_pattern_tmp2", 
					  Util.getJCRefType(NODE_ITERATOR_SIG),
					  il.getEnd(), null);

	if (!classGen.isExternal()) {
	    final Field iterator =
		new Field(ACC_PRIVATE, 
			  cpg.addUtf8(iteratorName),
			  cpg.addUtf8(NODE_ITERATOR_SIG),
			  null, cpg.getConstantPool());
	    classGen.addField(iterator);
	    iteratorIndex = cpg.addFieldref(classGen.getClassName(), 
					    iteratorName,
					    NODE_ITERATOR_SIG);

	    il.append(classGen.loadTranslet());
	    il.append(new GETFIELD(iteratorIndex));
	    il.append(DUP);
	    il.append(new ASTORE(iter.getIndex()));
	    ifBlock = il.append(new IFNONNULL(null));
	    il.append(classGen.loadTranslet());
	}	

	_step.translate(classGen, methodGen);
	il.append(new ASTORE(iter.getIndex()));

	if (!classGen.isExternal()) {
	    il.append(new ALOAD(iter.getIndex()));
	    il.append(new PUTFIELD(iteratorIndex));
	    ifBlock.setTarget(il.append(NOP));
	}

	il.append(methodGen.loadDOM());
	il.append(new ILOAD(node.getIndex()));
	int index = cpg.addInterfaceMethodref(DOM_INTF,
					      GET_PARENT, GET_PARENT_SIG);
	il.append(new INVOKEINTERFACE(index, 2));

	il.append(new ALOAD(iter.getIndex()));
	il.append(SWAP);
	il.append(methodGen.setStartNode());

	/* 
	 * Inline loop:
	 *
	 * int node2;
	 * while ((node2 = iter.next()) != NodeIterator.END 
	 *		  && node2 < node);
	 * return node2 == node; 
	 */
	BranchHandle skipNext;
	InstructionHandle begin, next;
	node2 = methodGen.addLocalVariable("step_pattern_tmp3", 
					   Util.getJCRefType(NODE_SIG),
					   il.getEnd(), null);

	skipNext = il.append(new GOTO(null));
	next = il.append(new ALOAD(iter.getIndex()));
	begin = il.append(methodGen.nextNode());
	il.append(DUP);
	il.append(new ISTORE(node2.getIndex()));

	il.append(new ILOAD(node2.getIndex()));
	il.append(new ILOAD(node.getIndex()));
	il.append(new IF_ICMPLT(next));

	il.append(new ILOAD(node2.getIndex()));
	il.append(new ILOAD(node.getIndex()));
	_falseList.add(il.append(new IF_ICMPNE(null)));

	skipNext.setTarget(begin);
    }
	
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (hasPredicates()) {
	    switch (_contextCase) {
	    case NO_CONTEXT:
		translateNoContext(classGen, methodGen);
		break;
		
	    case SIMPLE_CONTEXT:
		translateSimpleContext(classGen, methodGen);
		break;
		
	    default:
		translateGeneralContext(classGen, methodGen);
		break;
	    }
	}
	else if (isWildcard()) {
	}
	else {
	    translateKernel(classGen, methodGen);
	}
    }
}
