package org.apache.xalan.xsltc.compiler;

import java.util.Dictionary;
import java.util.Vector;

import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

/**
 * A test sequence is a sequence of patterns that
 *
 *  (1) occured in templates in the same mode
 *  (2) share the same kernel node type (e.g. A/B and C/C/B)
 *  (3) may also contain patterns matching "*" and "node()"
 *      (element sequence only) or matching "@*" (attribute
 *      sequence only).
 *
 * A test sequence may have a default template, which will be 
 * instantiated if none of the other patterns match. 
 */
final class TestSeq {

    /**
     * Integer code for the kernel type of this test sequence
     */
    private int _kernelType;

    /**
     * Vector of all patterns in the test sequence. May include
     * patterns with "*", "@*" or "node()" kernel.
     */
    private Vector _patterns = null;

    /**
     * A reference to the Mode object.
     */
    private Mode _mode = null;

    /**
     * Default template for this test sequence
     */
    private Template _default = null;

    /**
     * Instruction list representing this test sequence.
     */
    private InstructionList _instructionList;

    /**
     * Cached handle to avoid compiling more than once.
     */
    private InstructionHandle _start = null;

    /**
     * Creates a new test sequence given a set of patterns and a mode.
     */
    public TestSeq(Vector patterns, Mode mode) {
	this(patterns, -2, mode);
    }

    public TestSeq(Vector patterns, int kernelType, Mode mode) {
	_patterns = patterns;
	_kernelType = kernelType;
	_mode = mode;
    }

    /**
     * Returns a string representation of this test sequence. Notice
     * that test sequences are mutable, so the value returned by this
     * method is different before and after calling reduce().
     */
    public String toString() {
	final int count = _patterns.size();
	final StringBuffer result = new StringBuffer();

	for (int i = 0; i < count; i++) {
	    final LocationPathPattern pattern =
		(LocationPathPattern) _patterns.elementAt(i);

	    if (i == 0) {
		result.append("Testseq for kernel " + _kernelType)
		      .append('\n');
	    }
	    result.append("   pattern " + i + ": ")
	          .append(pattern.toString())
		  .append('\n');
	}
	return result.toString();
    }

    /**
     * Returns the instruction list for this test sequence
     */
    public InstructionList getInstructionList() {
	return _instructionList;
    }

    /**
     * Return the highest priority for a pattern in this test
     * sequence. This is either the priority of the first or
     * of the default pattern.
     */
    public double getPriority() {
	final Template template = (_patterns.size() == 0) ? _default 
	    : ((Pattern) _patterns.elementAt(0)).getTemplate();
	return template.getPriority();
    }

    /**
     * Returns the position of the highest priority pattern in 
     * this test sequence.
     */
    public int getPosition() {
	final Template template = (_patterns.size() == 0) ? _default 
	    : ((Pattern) _patterns.elementAt(0)).getTemplate();
	return template.getPosition();
    }

    /**
     * Reduce the patterns in this test sequence. Creates a new
     * vector of patterns and sets the default pattern if it
     * finds a patterns that is fully reduced.
     */
    public void reduce() {
	final Vector newPatterns = new Vector();

	final int count = _patterns.size();
	for (int i = 0; i < count; i++) {
	    final LocationPathPattern pattern =
		(LocationPathPattern)_patterns.elementAt(i);
		
	    pattern.reduceKernelPattern();
			
	    if (pattern.isWildcard()) {
		_default = pattern.getTemplate();
	    }
	    else {
		newPatterns.addElement(pattern);
	    }
	}
	_patterns = newPatterns;
    }

    /**
     * Returns, by reference, the templates that are included in 
     * this test sequence. Note that a single template can occur 
     * in several test sequences if its pattern is a union.
     */
    public void findTemplates(Dictionary templates) {
	if (_default != null) {
	    templates.put(_default, this);
	}
	for (int i = 0; i < _patterns.size(); i++) {
	    final LocationPathPattern pattern =
		(LocationPathPattern)_patterns.elementAt(i);
	    templates.put(pattern.getTemplate(), this);
	}
    }

    /**
     * Get the instruction handle to a template's code. This is 
     * used when a single template occurs in several test 
     * sequences; that is, if its pattern is a union of patterns 
     * (e.g. match="A/B | A/C").
     */
    private InstructionHandle getTemplateHandle(Template template) {
	return (InstructionHandle)_mode.getTemplateInstructionHandle(template);
    }

    /**
     * Returns pattern n in this test sequence
     */
    private LocationPathPattern getPattern(int n) {
	return (LocationPathPattern)_patterns.elementAt(n);
    }

    /**
     * Compile the code for this test sequence. Compile patterns 
     * from highest to lowest priority. Note that since patterns 
     * can be share by multiple test sequences, instruction lists 
     * must be copied before backpatching.
     */
    public InstructionHandle compile(ClassGenerator classGen,
				     MethodGenerator methodGen,
				     InstructionHandle continuation) 
    {
	if (_start != null) {
	    return _start;
	}

	final int count = _patterns.size();
	if (count == 0) {
	    return (_start = getTemplateHandle(_default));
	}

	InstructionHandle fail = (_default == null) ? continuation
	    : getTemplateHandle(_default);
	
	for (int n = count - 1; n >= 0; n--) {
	    final LocationPathPattern pattern = getPattern(n);
	    final Template template = pattern.getTemplate();
	    final InstructionList il = new InstructionList();

	    il.append(methodGen.loadCurrentNode());

	    InstructionList ilist = _mode.getInstructionList(pattern);
	    if (ilist == null) {
		ilist = pattern.compile(classGen, methodGen);
		_mode.addInstructionList(pattern, ilist);
	    }

	    InstructionList copyOfilist = ilist.copy();

	    FlowList trueList = pattern.getTrueList();
	    if (trueList != null) {
		trueList = trueList.copyAndRedirect(ilist, copyOfilist);
	    }
	    FlowList falseList = pattern.getFalseList();
	    if (falseList != null) {
		falseList = falseList.copyAndRedirect(ilist, copyOfilist);
	    }

	    il.append(copyOfilist);

	    final InstructionHandle gtmpl = getTemplateHandle(template);
	    final InstructionHandle success = il.append(new GOTO_W(gtmpl));

	    if (trueList != null) {
		trueList.backPatch(success);
	    }
	    if (falseList != null) {
		falseList.backPatch(fail);
	    } 

	    fail = il.getStart();

	    if (_instructionList != null) {
		il.append(_instructionList);
	    }

	    _instructionList = il;
	}
	return (_start = fail);
    }
}
