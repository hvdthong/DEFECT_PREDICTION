package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.SWITCH;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.util.InstructionFinder;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NamedMethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTM;

/**
 * Mode gathers all the templates belonging to a given mode; 
 * it is responsible for generating an appropriate 
 * applyTemplates + (mode name) method in the translet.
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 * @author Erwin Bolwidt <ejb@klomp.org>
 * @author G. Todd Miller
 */
final class Mode implements Constants {

    /**
     * The name of this mode as defined in the stylesheet.
     */
    private final QName _name;

    /**
     * A reference to the stylesheet object that owns this mode.
     */
    private final Stylesheet _stylesheet; 

    /**
     * The name of the method in which this mode is compiled.
     */
    private final String _methodName;

    /**
     * A vector of all the templates in this mode.
     */
    private Vector _templates; 

    /**
     * Group for patterns with node()-type kernel and child axis.
     */
    private Vector _childNodeGroup = null;

    /**
     * Test sequence for patterns with node()-type kernel and child axis.
     */
    private TestSeq _childNodeTestSeq = null;

    /**
     * Group for patterns with node()-type kernel and attribute axis.
     */
    private Vector _attribNodeGroup = null;

    /**
     * Test sequence for patterns with node()-type kernel and attribute axis.
     */
    private TestSeq _attribNodeTestSeq = null;
    
    /**
     * Group for patterns with id() or key()-type kernel.
     */
    private Vector _idxGroup = null;

    /**
     * Test sequence for patterns with id() or key()-type kernel.
     */
    private TestSeq _idxTestSeq = null;

    /**
     * Group for patterns with any other kernel type.
     */
    private Vector[] _patternGroups;

    /**
     * Test sequence for patterns with any other kernel type.
     */
    private TestSeq[] _testSeq;

    
    /**
     * A mapping between templates and test sequences.
     */
    private Hashtable _neededTemplates = new Hashtable();

    /**
     * A mapping between named templates and Mode objects.
     */
    private Hashtable _namedTemplates = new Hashtable();

    /**
     * A mapping between templates and instruction handles.
     */
    private Hashtable _templateIHs = new Hashtable();

    /**
     * A mapping between templates and instruction lists.
     */
    private Hashtable _templateILs = new Hashtable();

    /**
     * A reference to the pattern matching the root node.
     */
    private LocationPathPattern _rootPattern = null;

    /**
     * Stores ranges of template precendences for the compilation 
     * of apply-imports (a Hashtable for historical reasons).
     */
    private Hashtable _importLevels = null;

    /**
     * A mapping between key names and keys.
     */
    private Hashtable _keys = null;

    /**
     * Variable index for the current node used in code generation.
     */
    private int _currentIndex;

    /**
     * Creates a new Mode.
     *
     * @param name A textual representation of the mode's QName
     * @param stylesheet The Stylesheet in which the mode occured
     * @param suffix A suffix to append to the method name for this mode
     *               (normally a sequence number - still in a String).
     */
    public Mode(QName name, Stylesheet stylesheet, String suffix) {
	_name = name;
	_stylesheet = stylesheet;
	_methodName = APPLY_TEMPLATES + suffix;
	_templates = new Vector();
	_patternGroups = new Vector[32];
    }

    /**
     * Returns the name of the method (_not_ function) that will be 
     * compiled for this mode. Normally takes the form 'applyTemplates()' 
     * or * 'applyTemplates2()'.
     *
     * @return Method name for this mode
     */
    public String functionName() {
	return _methodName;
    }

    public String functionName(int min, int max) {
	if (_importLevels == null) {
	    _importLevels = new Hashtable();
	}
	_importLevels.put(new Integer(max), new Integer(min));
	return _methodName + '_' + max;
    }

    /**
     * Shortcut to get the class compiled for this mode (will be inlined).
     */
    private String getClassName() {
	return _stylesheet.getClassName();
    }

    public Stylesheet getStylesheet() {
	return _stylesheet;
    }

    public void addTemplate(Template template) {
	_templates.addElement(template);
    }

    private Vector quicksort(Vector templates, int p, int r) {
	if (p < r) {
	    final int q = partition(templates, p, r);
	    quicksort(templates, p, q);
	    quicksort(templates, q + 1, r);
	}
	return templates;
    }
    
    private int partition(Vector templates, int p, int r) {
	final Template x = (Template)templates.elementAt(p);
	int i = p - 1;
	int j = r + 1;
	while (true) {
	    while (x.compareTo((Template)templates.elementAt(--j)) > 0);
	    while (x.compareTo((Template)templates.elementAt(++i)) < 0);
	    if (i < j) {
		templates.set(j, templates.set(i, templates.elementAt(j)));
	    }
	    else {
		return j;
	    }
	}
    }

    /**
     * Process all the test patterns in this mode
     */
    public void processPatterns(Hashtable keys) {
	_keys = keys;

/*
System.out.println("Before Sort " + _name);
for (int i = 0; i < _templates.size(); i++) {
    System.out.println("name = " + ((Template)_templates.elementAt(i)).getName());
    System.out.println("pattern = " + ((Template)_templates.elementAt(i)).getPattern());
    System.out.println("priority = " + ((Template)_templates.elementAt(i)).getPriority());
    System.out.println("position = " + ((Template)_templates.elementAt(i)).getPosition());
}
*/

	_templates = quicksort(_templates, 0, _templates.size() - 1);

/*
System.out.println("\n After Sort " + _name);
for (int i = 0; i < _templates.size(); i++) {
    System.out.println("name = " + ((Template)_templates.elementAt(i)).getName());
    System.out.println("pattern = " + ((Template)_templates.elementAt(i)).getPattern());
    System.out.println("priority = " + ((Template)_templates.elementAt(i)).getPriority());
    System.out.println("position = " + ((Template)_templates.elementAt(i)).getPosition());
}
*/

	final Enumeration templates = _templates.elements();
	while (templates.hasMoreElements()) {
	    final Template template = (Template)templates.nextElement();

	    /* 
	     * Add this template to a table of named templates if it has a name.
	     * If there are multiple templates with the same name, all but one
	     * (the one with highest priority) will be disabled.
	     */
	    if (template.isNamed() && !template.disabled()) {
		_namedTemplates.put(template, this);
	    }

	    final Pattern pattern = template.getPattern();
	    if (pattern != null) {
		flattenAlternative(pattern, template, keys);
	    }
	}
	prepareTestSequences();
    }

    /**
     * This method will break up alternative patterns (ie. unions of patterns,
     * such as match="A/B | C/B") and add the basic patterns to their
     * respective pattern groups.
     */
    private void flattenAlternative(Pattern pattern,
				    Template template,
				    Hashtable keys) {
	if (pattern instanceof IdKeyPattern) {
	    final IdKeyPattern idkey = (IdKeyPattern)pattern;
	    idkey.setTemplate(template);
	    if (_idxGroup == null) _idxGroup = new Vector();
	    _idxGroup.add(pattern);
	}
	else if (pattern instanceof AlternativePattern) {
	    final AlternativePattern alt = (AlternativePattern)pattern;
	    flattenAlternative(alt.getLeft(), template, keys);
	    flattenAlternative(alt.getRight(), template, keys);
	}
	else if (pattern instanceof LocationPathPattern) {
	    final LocationPathPattern lpp = (LocationPathPattern)pattern;
	    lpp.setTemplate(template);
	    addPatternToGroup(lpp);
	}
    }

    /**
     * Group patterns by NodeTests of their last Step
     * Keep them sorted by priority within group
     */
    private void addPatternToGroup(final LocationPathPattern lpp) {
	if (lpp instanceof IdKeyPattern) {
	    addPattern(-1, lpp);
	}
	else {
	    final StepPattern kernel = lpp.getKernelPattern();
	    if (kernel != null) {
		addPattern(kernel.getNodeType(), lpp);
	    }
	    else if (_rootPattern == null ||
		     lpp.noSmallerThan(_rootPattern)) {
		_rootPattern = lpp;
	    }
	}
    }

    /**
     * Adds a pattern to a pattern group
     */
    private void addPattern(int kernelType, LocationPathPattern pattern) {
	final int oldLength = _patternGroups.length;
	if (kernelType >= oldLength) {
	    Vector[] newGroups = new Vector[kernelType * 2];
	    System.arraycopy(_patternGroups, 0, newGroups, 0, oldLength);
	    _patternGroups = newGroups;
	}
	
	Vector patterns;

	if (kernelType == DOM.NO_TYPE) {
	    if (pattern.getAxis() == Axis.ATTRIBUTE) {
		patterns = (_attribNodeGroup == null) ?
		    (_attribNodeGroup = new Vector(2)) : _attribNodeGroup;
	    }
	    else {
		patterns = (_childNodeGroup == null) ?
		    (_childNodeGroup = new Vector(2)) : _childNodeGroup;
	    }
	}
	else {
	    patterns = (_patternGroups[kernelType] == null) ?
		(_patternGroups[kernelType] = new Vector(2)) : 
		_patternGroups[kernelType];
	}

	if (patterns.size() == 0) {
	    patterns.addElement(pattern);
	}
	else {
	    boolean inserted = false;
	    for (int i = 0; i < patterns.size(); i++) {
		final LocationPathPattern lppToCompare =
		    (LocationPathPattern)patterns.elementAt(i);

		if (pattern.noSmallerThan(lppToCompare)) {
		    inserted = true;
		    patterns.insertElementAt(pattern, i);
		    break;
		}
	    }
	    if (inserted == false) {
		patterns.addElement(pattern);
	    }
	}
    }
    
    /**
     * Complete test sequences of a given type by adding all patterns
     * from a given group.
     */
    private void completeTestSequences(int nodeType, Vector patterns) {
	if (patterns != null) {
	    if (_patternGroups[nodeType] == null) {
		_patternGroups[nodeType] = patterns;
	    }
	    else {
		final int m = patterns.size();
		for (int j = 0; j < m; j++) {
		    addPattern(nodeType, 
			(LocationPathPattern) patterns.elementAt(j));
		}
	    }
	}
    }

    /**
     * Build test sequences. The first step is to complete the test sequences 
     * by including patterns of "*" and "node()" kernel to all element test 
     * sequences, and of "@*" to all attribute test sequences.
     */
    private void prepareTestSequences() {
	final Vector starGroup = _patternGroups[DTM.ELEMENT_NODE];
	final Vector atStarGroup = _patternGroups[DTM.ATTRIBUTE_NODE];

	completeTestSequences(DTM.TEXT_NODE, _childNodeGroup);
	
	completeTestSequences(DTM.ELEMENT_NODE, _childNodeGroup);
	
	completeTestSequences(DTM.PROCESSING_INSTRUCTION_NODE, _childNodeGroup);
	
	completeTestSequences(DTM.COMMENT_NODE, _childNodeGroup);
	
	completeTestSequences(DTM.ATTRIBUTE_NODE, _attribNodeGroup);

	final Vector names = _stylesheet.getXSLTC().getNamesIndex();
	if (starGroup != null || atStarGroup != null || 
	    _childNodeGroup != null || _attribNodeGroup != null) 
	{
	    final int n = _patternGroups.length;

	    for (int i = DTM.NTYPES; i < n; i++) {
		if (_patternGroups[i] == null) continue;

		final String name = (String) names.elementAt(i - DTM.NTYPES);

		if (isAttributeName(name)) {
		    completeTestSequences(i, atStarGroup);

		    completeTestSequences(i, _attribNodeGroup);
		}
		else {
		    completeTestSequences(i, starGroup);

		    completeTestSequences(i, _childNodeGroup);
		}
	    }
	}

	_testSeq = new TestSeq[DTM.NTYPES + names.size()];
	
	final int n = _patternGroups.length;
	for (int i = 0; i < n; i++) {
	    final Vector patterns = _patternGroups[i];
	    if (patterns != null) {
		final TestSeq testSeq = new TestSeq(patterns, i, this);
		testSeq.reduce();
		_testSeq[i] = testSeq;
		testSeq.findTemplates(_neededTemplates);
	    }
	}

	if (_childNodeGroup != null && _childNodeGroup.size() > 0) {
	    _childNodeTestSeq = new TestSeq(_childNodeGroup, -1, this);
	    _childNodeTestSeq.reduce();
	    _childNodeTestSeq.findTemplates(_neededTemplates);
	}

/*
	if (_attribNodeGroup != null && _attribNodeGroup.size() > 0) {
	    _attribNodeTestSeq = new TestSeq(_attribNodeGroup, -1, this);
	    _attribNodeTestSeq.reduce();
	    _attribNodeTestSeq.findTemplates(_neededTemplates);
	}
*/

	if (_idxGroup != null && _idxGroup.size() > 0) {
	    _idxTestSeq = new TestSeq(_idxGroup, this);
	    _idxTestSeq.reduce();
	    _idxTestSeq.findTemplates(_neededTemplates);
	}
	
	if (_rootPattern != null) {
	    _neededTemplates.put(_rootPattern.getTemplate(), this);
	}
    }

    private void compileNamedTemplate(Template template,
				      ClassGenerator classGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = new InstructionList();
	String methodName = Util.escape(template.getName().toString());

	int numParams = 0;
	if (template.isSimpleNamedTemplate()) {
	    Vector parameters = template.getParameters();
	    numParams = parameters.size();
	}
	
	org.apache.bcel.generic.Type[] types = 
	    new org.apache.bcel.generic.Type[4 + numParams];
	String[] names = new String[4 + numParams];
	types[0] = Util.getJCRefType(DOM_INTF_SIG);
	types[1] = Util.getJCRefType(NODE_ITERATOR_SIG);
	types[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
	types[3] = org.apache.bcel.generic.Type.INT;
	names[0] = DOCUMENT_PNAME;
	names[1] = ITERATOR_PNAME;
	names[2] = TRANSLET_OUTPUT_PNAME;
	names[3] = NODE_PNAME;
	
	for (int i = 4; i < 4 + numParams; i++) {
	    types[i] = Util.getJCRefType(OBJECT_SIG);
	    names[i] = "param" + String.valueOf(i-4);
	}
	
	NamedMethodGenerator methodGen =
	        new NamedMethodGenerator(ACC_PUBLIC,
				     org.apache.bcel.generic.Type.VOID,
				     types, names, methodName,
				     getClassName(), il, cpg);	    

	il.append(template.compile(classGen, methodGen));
	il.append(RETURN);
	
	methodGen.stripAttributes(true);
	methodGen.setMaxLocals();
	methodGen.setMaxStack();
	methodGen.removeNOPs();
	classGen.addMethod(methodGen.getMethod());
    }

    private void compileTemplates(ClassGenerator classGen,
				  MethodGenerator methodGen,
				  InstructionHandle next) 
    {
        Enumeration templates = _namedTemplates.keys();
        while (templates.hasMoreElements()) {
            final Template template = (Template)templates.nextElement();
            compileNamedTemplate(template, classGen);
        }

	templates = _neededTemplates.keys();
	while (templates.hasMoreElements()) {
	    final Template template = (Template)templates.nextElement();
	    if (template.hasContents()) {
		InstructionList til = template.compile(classGen, methodGen);
		til.append(new GOTO_W(next));
		_templateILs.put(template, til);
		_templateIHs.put(template, til.getStart());
	    }
	    else {
		_templateIHs.put(template, next);
	    }
	}
    }
	
    private void appendTemplateCode(InstructionList body) {
	final Enumeration templates = _neededTemplates.keys();
	while (templates.hasMoreElements()) {
	    final Object iList =
		_templateILs.get(templates.nextElement());
	    if (iList != null) {
		body.append((InstructionList)iList);
	    }
	}
    }

    private void appendTestSequences(InstructionList body) {
	final int n = _testSeq.length;
	for (int i = 0; i < n; i++) {
	    final TestSeq testSeq = _testSeq[i];
	    if (testSeq != null) {
		InstructionList il = testSeq.getInstructionList();
		if (il != null)
		    body.append(il);
	    }
	}
    }

    public static void compileGetChildren(ClassGenerator classGen,
					  MethodGenerator methodGen,
					  int node) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final int git = cpg.addInterfaceMethodref(DOM_INTF,
						  GET_CHILDREN,
						  GET_CHILDREN_SIG);
	il.append(methodGen.loadDOM());
	il.append(new ILOAD(node));
	il.append(new INVOKEINTERFACE(git, 2));
    }

    /**
     * Compiles the default handling for DOM elements: traverse all children
     */
    private InstructionList compileDefaultRecursion(ClassGenerator classGen,
						    MethodGenerator methodGen,
						    InstructionHandle next) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = new InstructionList();
	final String applyTemplatesSig = classGen.getApplyTemplatesSig();
	final int git = cpg.addInterfaceMethodref(DOM_INTF,
						  GET_CHILDREN,
						  GET_CHILDREN_SIG);
	final int applyTemplates = cpg.addMethodref(getClassName(),
						    functionName(),
						    applyTemplatesSig);
	il.append(classGen.loadTranslet());
	il.append(methodGen.loadDOM());
	
	il.append(methodGen.loadDOM());
	il.append(new ILOAD(_currentIndex));
	il.append(new INVOKEINTERFACE(git, 2));
	il.append(methodGen.loadHandler());
	il.append(new INVOKEVIRTUAL(applyTemplates));
	il.append(new GOTO_W(next));
	return il;
    }

    /**
     * Compiles the default action for DOM text nodes and attribute nodes:
     * output the node's text value
     */
    private InstructionList compileDefaultText(ClassGenerator classGen,
					       MethodGenerator methodGen,
					       InstructionHandle next) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = new InstructionList();

	final int chars = cpg.addInterfaceMethodref(DOM_INTF,
						    CHARACTERS,
						    CHARACTERS_SIG);
	il.append(methodGen.loadDOM());
	il.append(new ILOAD(_currentIndex));
	il.append(methodGen.loadHandler());
	il.append(new INVOKEINTERFACE(chars, 3));
	il.append(new GOTO_W(next));
	return il;
    }

    private InstructionList compileNamespaces(ClassGenerator classGen,
					      MethodGenerator methodGen,
					      boolean[] isNamespace,
					      boolean[] isAttribute,
					      boolean attrFlag,
					      InstructionHandle defaultTarget) {
	final XSLTC xsltc = classGen.getParser().getXSLTC();
	final ConstantPoolGen cpg = classGen.getConstantPool();

	final Vector namespaces = xsltc.getNamespaceIndex();
	final Vector names = xsltc.getNamesIndex();
	final int namespaceCount = namespaces.size() + 1;
	final int namesCount = names.size();

	final InstructionList il = new InstructionList();
	final int[] types = new int[namespaceCount];
	final InstructionHandle[] targets = new InstructionHandle[types.length];

	if (namespaceCount > 0) {
	    boolean compiled = false;

	    for (int i = 0; i < namespaceCount; i++) {
		targets[i] = defaultTarget;
		types[i] = i;
	    }

	    for (int i = DTM.NTYPES; i < (DTM.NTYPES+namesCount); i++) {
		if ((isNamespace[i]) && (isAttribute[i] == attrFlag)) {
		    String name = (String)names.elementAt(i-DTM.NTYPES);
		    String namespace = name.substring(0,name.lastIndexOf(':'));
		    final int type = xsltc.registerNamespace(namespace);
		    
		    if ((i < _testSeq.length) &&
			(_testSeq[i] != null)) {
			targets[type] =
			    (_testSeq[i]).compile(classGen,
						       methodGen,
						       defaultTarget);
			compiled = true;
		    }
		}
	    }

	    if (!compiled) return(null);
		
	    final int getNS = cpg.addInterfaceMethodref(DOM_INTF,
							"getNamespaceType",
							"(I)I");
	    il.append(methodGen.loadDOM());
	    il.append(new ILOAD(_currentIndex));
	    il.append(new INVOKEINTERFACE(getNS, 2));
	    il.append(new SWITCH(types, targets, defaultTarget));
	    return(il);
	}
	else {
	    return(null);
	}
    }

   /**
     * Compiles the applyTemplates() method and adds it to the translet.
     * This is the main dispatch method.
     */
    public void compileApplyTemplates(ClassGenerator classGen) {
	final XSLTC xsltc = classGen.getParser().getXSLTC();
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final Vector names = xsltc.getNamesIndex();

	final org.apache.bcel.generic.Type[] argTypes =
	    new org.apache.bcel.generic.Type[3];
	argTypes[0] = Util.getJCRefType(DOM_INTF_SIG);
	argTypes[1] = Util.getJCRefType(NODE_ITERATOR_SIG);
	argTypes[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
	
	final String[] argNames = new String[3];
	argNames[0] = DOCUMENT_PNAME;
	argNames[1] = ITERATOR_PNAME;
	argNames[2] = TRANSLET_OUTPUT_PNAME;

	final InstructionList mainIL = new InstructionList();
	final MethodGenerator methodGen =
	    new MethodGenerator(ACC_PUBLIC | ACC_FINAL, 
				org.apache.bcel.generic.Type.VOID,
				argTypes, argNames, functionName(),
				getClassName(), mainIL,
				classGen.getConstantPool());
	methodGen.addException("org.apache.xalan.xsltc.TransletException");

	final LocalVariableGen current;
	current = methodGen.addLocalVariable2("current",
					      org.apache.bcel.generic.Type.INT,
					      mainIL.getEnd());
	_currentIndex = current.getIndex();

	final InstructionList body = new InstructionList();
	body.append(NOP);

	final InstructionList ilLoop = new InstructionList();
	ilLoop.append(methodGen.loadIterator());
	ilLoop.append(methodGen.nextNode());
	ilLoop.append(DUP);
	ilLoop.append(new ISTORE(_currentIndex));

        final BranchHandle ifeq = ilLoop.append(new IFLT(null));
	final BranchHandle loop = ilLoop.append(new GOTO_W(null));
	final InstructionHandle ihLoop = ilLoop.getStart();

	InstructionList ilRecurse =
	    compileDefaultRecursion(classGen, methodGen, ihLoop);
	InstructionHandle ihRecurse = ilRecurse.getStart();

	InstructionList ilText =
	    compileDefaultText(classGen, methodGen, ihLoop);
	InstructionHandle ihText = ilText.getStart();

	final int[] types = new int[DTM.NTYPES + names.size()];
	for (int i = 0; i < types.length; i++) {
	    types[i] = i;
	}

	final boolean[] isAttribute = new boolean[types.length];
	final boolean[] isNamespace = new boolean[types.length];
	for (int i = 0; i < names.size(); i++) {
	    final String name = (String)names.elementAt(i);
	    isAttribute[i + DTM.NTYPES] = isAttributeName(name);
	    isNamespace[i + DTM.NTYPES] = isNamespaceName(name);
	}

	compileTemplates(classGen, methodGen, ihLoop);

	final TestSeq elemTest = _testSeq[DTM.ELEMENT_NODE];
	InstructionHandle ihElem = ihRecurse;
	if (elemTest != null)
	    ihElem = elemTest.compile(classGen, methodGen, ihRecurse);

	final TestSeq attrTest = _testSeq[DTM.ATTRIBUTE_NODE];
	InstructionHandle ihAttr = ihText;
	if (attrTest != null)
	    ihAttr = attrTest.compile(classGen, methodGen, ihAttr);

	InstructionList ilKey = null;
	if (_idxTestSeq != null) {
	    loop.setTarget(_idxTestSeq.compile(classGen, methodGen, body.getStart()));
	    ilKey = _idxTestSeq.getInstructionList();
	}
	else {
	    loop.setTarget(body.getStart());
	}

	if (_childNodeTestSeq != null) {
	    double nodePrio = _childNodeTestSeq.getPriority();
	    int    nodePos  = _childNodeTestSeq.getPosition();
	    double elemPrio = (0 - Double.MAX_VALUE);
	    int    elemPos  = Integer.MIN_VALUE;

	    if (elemTest != null) {
		elemPrio = elemTest.getPriority();
		elemPos  = elemTest.getPosition();
	    }
	    if (elemPrio == Double.NaN || elemPrio < nodePrio || 
		(elemPrio == nodePrio && elemPos < nodePos)) 
	    {
		ihElem = _childNodeTestSeq.compile(classGen, methodGen, ihLoop);
	    }

	    final TestSeq textTest = _testSeq[DTM.TEXT_NODE];
	    double textPrio = (0 - Double.MAX_VALUE);
	    int    textPos  = Integer.MIN_VALUE;

	    if (textTest != null) {
		textPrio = textTest.getPriority();
		textPos  = textTest.getPosition();
	    }
	    if (textPrio == Double.NaN || textPrio < nodePrio ||
	        (textPrio == nodePrio && textPos < nodePos)) 
	    {
		ihText = _childNodeTestSeq.compile(classGen, methodGen, ihLoop);
		_testSeq[DTM.TEXT_NODE] = _childNodeTestSeq;
	    }
	}

	InstructionHandle elemNamespaceHandle = ihElem;
	InstructionList nsElem = compileNamespaces(classGen, methodGen,
						   isNamespace, isAttribute,
						   false, ihElem);
	if (nsElem != null) elemNamespaceHandle = nsElem.getStart();

	InstructionHandle attrNamespaceHandle = ihAttr;
	InstructionList nsAttr = compileNamespaces(classGen, methodGen,
						   isNamespace, isAttribute,
						   true, ihAttr);
	if (nsAttr != null) attrNamespaceHandle = nsAttr.getStart();

	final InstructionHandle[] targets = new InstructionHandle[types.length];
	for (int i = DTM.NTYPES; i < targets.length; i++) {
	    final TestSeq testSeq = _testSeq[i];
	    if (isNamespace[i]) {
		if (isAttribute[i])
		    targets[i] = attrNamespaceHandle;
		else
		    targets[i] = elemNamespaceHandle;
	    }
	    else if (testSeq != null) {
		if (isAttribute[i])
		    targets[i] = testSeq.compile(classGen, methodGen,
						 attrNamespaceHandle);
		else
		    targets[i] = testSeq.compile(classGen, methodGen,
						 elemNamespaceHandle);
	    }
	    else {
		targets[i] = ihLoop;
	    }
	}


	targets[DTM.ROOT_NODE] = _rootPattern != null
	    ? getTemplateInstructionHandle(_rootPattern.getTemplate())
	    : ihRecurse;

	targets[DTM.DOCUMENT_NODE] = _rootPattern != null
	    ? getTemplateInstructionHandle(_rootPattern.getTemplate())
	    : ihRecurse;

	targets[DTM.TEXT_NODE] = _testSeq[DTM.TEXT_NODE] != null
	    ? _testSeq[DTM.TEXT_NODE].compile(classGen, methodGen, ihText)
	    : ihText;

	targets[DTM.NAMESPACE_NODE] = ihLoop;

	targets[DTM.ELEMENT_NODE] = elemNamespaceHandle;

	targets[DTM.ATTRIBUTE_NODE] = attrNamespaceHandle;

	InstructionHandle ihPI = ihLoop;
	if (_childNodeTestSeq != null) ihPI = ihElem;
	if (_testSeq[DTM.PROCESSING_INSTRUCTION_NODE] != null)
	    targets[DTM.PROCESSING_INSTRUCTION_NODE] =
		_testSeq[DTM.PROCESSING_INSTRUCTION_NODE].
		compile(classGen, methodGen, ihPI);
	else
	    targets[DTM.PROCESSING_INSTRUCTION_NODE] = ihPI;
	
	InstructionHandle ihComment = ihLoop;
	if (_childNodeTestSeq != null) ihComment = ihElem;
	targets[DTM.COMMENT_NODE] = _testSeq[DTM.COMMENT_NODE] != null
	    ? _testSeq[DTM.COMMENT_NODE].compile(classGen, methodGen, ihComment)
	    : ihComment;
	    
	targets[DTM.CDATA_SECTION_NODE] = ihLoop;

	targets[DTM.DOCUMENT_FRAGMENT_NODE] = ihLoop;
	
	targets[DTM.DOCUMENT_TYPE_NODE] = ihLoop;

	targets[DTM.ENTITY_NODE] = ihLoop;

	targets[DTM.ENTITY_REFERENCE_NODE] = ihLoop;
	
	targets[DTM.NOTATION_NODE] = ihLoop;


	for (int i = DTM.NTYPES; i < targets.length; i++) {
	    final TestSeq testSeq = _testSeq[i];
	    if ((testSeq == null) || (isNamespace[i])) {
		if (isAttribute[i])
		    targets[i] = attrNamespaceHandle;
		else
		    targets[i] = elemNamespaceHandle;
	    }
	    else {
		if (isAttribute[i])
		    targets[i] = testSeq.compile(classGen, methodGen,
						 attrNamespaceHandle);
		else
		    targets[i] = testSeq.compile(classGen, methodGen,
						 elemNamespaceHandle);
	    }
	}

	if (ilKey != null) body.insert(ilKey);

	final int getType = cpg.addInterfaceMethodref(DOM_INTF,
						      "getExpandedTypeID",
                                                      "(I)I");
	body.append(methodGen.loadDOM());
	body.append(new ILOAD(_currentIndex));
	body.append(new INVOKEINTERFACE(getType, 2));

	InstructionHandle disp = body.append(new SWITCH(types, targets, ihLoop));

	appendTestSequences(body);
	appendTemplateCode(body);

	if (nsElem != null) body.append(nsElem);
	if (nsAttr != null) body.append(nsAttr);

	body.append(ilRecurse);
	body.append(ilText);

	mainIL.append(new GOTO_W(ihLoop));
	mainIL.append(body);
	mainIL.append(ilLoop);

	peepHoleOptimization(methodGen);
	methodGen.stripAttributes(true);
	
	methodGen.setMaxLocals();
	methodGen.setMaxStack();
	methodGen.removeNOPs();
	classGen.addMethod(methodGen.getMethod());

	if (_importLevels != null) {
	    Enumeration levels = _importLevels.keys();
	    while (levels.hasMoreElements()) {
		Integer max = (Integer)levels.nextElement();
		Integer min = (Integer)_importLevels.get(max);
		compileApplyImports(classGen, min.intValue(), max.intValue());
	    }
	}
    }

    private void compileTemplateCalls(ClassGenerator classGen,
				      MethodGenerator methodGen,
				      InstructionHandle next, int min, int max){
        Enumeration templates = _neededTemplates.keys();
	while (templates.hasMoreElements()) {
	    final Template template = (Template)templates.nextElement();
	    final int prec = template.getImportPrecedence();
	    if ((prec >= min) && (prec < max)) {
		if (template.hasContents()) {
		    InstructionList til = template.compile(classGen, methodGen);
		    til.append(new GOTO_W(next));
		    _templateILs.put(template, til);
		    _templateIHs.put(template, til.getStart());
		}
		else {
		    _templateIHs.put(template, next);
		}
	    }
	}
    }


    public void compileApplyImports(ClassGenerator classGen, int min, int max) {
	final XSLTC xsltc = classGen.getParser().getXSLTC();
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final Vector names      = xsltc.getNamesIndex();

	_namedTemplates = new Hashtable();
	_neededTemplates = new Hashtable();
	_templateIHs = new Hashtable();
	_templateILs = new Hashtable();
	_patternGroups = new Vector[32];
	_rootPattern = null;

	Vector oldTemplates = _templates;

	_templates = new Vector();
	final Enumeration templates = oldTemplates.elements();
	while (templates.hasMoreElements()) {
	    final Template template = (Template)templates.nextElement();
	    final int prec = template.getImportPrecedence();
	    if ((prec >= min) && (prec < max)) addTemplate(template);
	}

	processPatterns(_keys);

	final org.apache.bcel.generic.Type[] argTypes =
	    new org.apache.bcel.generic.Type[4];
	argTypes[0] = Util.getJCRefType(DOM_INTF_SIG);
	argTypes[1] = Util.getJCRefType(NODE_ITERATOR_SIG);
	argTypes[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
	argTypes[3] = org.apache.bcel.generic.Type.INT;

	final String[] argNames = new String[4];
	argNames[0] = DOCUMENT_PNAME;
	argNames[1] = ITERATOR_PNAME;
	argNames[2] = TRANSLET_OUTPUT_PNAME;
	argNames[3] = NODE_PNAME;

	final InstructionList mainIL = new InstructionList();
	final MethodGenerator methodGen =
	    new MethodGenerator(ACC_PUBLIC | ACC_FINAL, 
				org.apache.bcel.generic.Type.VOID,
				argTypes, argNames, functionName()+'_'+max,
				getClassName(), mainIL,
				classGen.getConstantPool());
	methodGen.addException("org.apache.xalan.xsltc.TransletException");

	final LocalVariableGen current;
	current = methodGen.addLocalVariable2("current",
					      org.apache.bcel.generic.Type.INT,
					      mainIL.getEnd());
	_currentIndex = current.getIndex();

    mainIL.append(new ILOAD(methodGen.getLocalIndex(NODE_PNAME)));
    mainIL.append(new ISTORE(_currentIndex));
    
	final InstructionList body = new InstructionList();
	body.append(NOP);

	final InstructionList ilLoop = new InstructionList();
    ilLoop.append(RETURN);
	final InstructionHandle ihLoop = ilLoop.getStart();

	InstructionList ilRecurse =
	    compileDefaultRecursion(classGen, methodGen, ihLoop);
	InstructionHandle ihRecurse = ilRecurse.getStart();

	InstructionList ilText =
	    compileDefaultText(classGen, methodGen, ihLoop);
	InstructionHandle ihText = ilText.getStart();

	final int[] types = new int[DTM.NTYPES + names.size()];
	for (int i = 0; i < types.length; i++) {
	    types[i] = i;
	}

	final boolean[] isAttribute = new boolean[types.length];
	final boolean[] isNamespace = new boolean[types.length];
	for (int i = 0; i < names.size(); i++) {
	    final String name = (String)names.elementAt(i);
	    isAttribute[i+DTM.NTYPES] = isAttributeName(name);
	    isNamespace[i+DTM.NTYPES] = isNamespaceName(name);
	}

	compileTemplateCalls(classGen, methodGen, ihLoop, min, max);

	final TestSeq elemTest = _testSeq[DTM.ELEMENT_NODE];
	InstructionHandle ihElem = ihRecurse;
	if (elemTest != null) {
	    ihElem = elemTest.compile(classGen, methodGen, ihLoop);
	}

	final TestSeq attrTest = _testSeq[DTM.ATTRIBUTE_NODE];
	InstructionHandle ihAttr = ihLoop;
	if (attrTest != null) {
	    ihAttr = attrTest.compile(classGen, methodGen, ihAttr);
	}

	InstructionList ilKey = null;
	if (_idxTestSeq != null) {
	    ilKey = _idxTestSeq.getInstructionList();
	}

	if (_childNodeTestSeq != null) {
	    double nodePrio = _childNodeTestSeq.getPriority();
	    int    nodePos  = _childNodeTestSeq.getPosition();
	    double elemPrio = (0 - Double.MAX_VALUE);
	    int    elemPos  = Integer.MIN_VALUE;

	    if (elemTest != null) {
		elemPrio = elemTest.getPriority();
		elemPos  = elemTest.getPosition();
	    }

	    if (elemPrio == Double.NaN || elemPrio < nodePrio || 
		(elemPrio == nodePrio && elemPos < nodePos)) 
	    {
		ihElem = _childNodeTestSeq.compile(classGen, methodGen, ihLoop);
	    }

	    final TestSeq textTest = _testSeq[DTM.TEXT_NODE];
	    double textPrio = (0 - Double.MAX_VALUE);
	    int    textPos  = Integer.MIN_VALUE;

	    if (textTest != null) {
		textPrio = textTest.getPriority();
		textPos  = textTest.getPosition();
	    }

	    if (textPrio == Double.NaN || textPrio < nodePrio ||
	        (textPrio == nodePrio && textPos < nodePos)) 
	    {
		ihText = _childNodeTestSeq.compile(classGen, methodGen, ihLoop);
		_testSeq[DTM.TEXT_NODE] = _childNodeTestSeq;
	    }
	}

	InstructionHandle elemNamespaceHandle = ihElem;
	InstructionList nsElem = compileNamespaces(classGen, methodGen,
						   isNamespace, isAttribute,
						   false, ihElem);
	if (nsElem != null) elemNamespaceHandle = nsElem.getStart();

	InstructionList nsAttr = compileNamespaces(classGen, methodGen,
						   isNamespace, isAttribute,
						   true, ihAttr);
	InstructionHandle attrNamespaceHandle = ihAttr;
	if (nsAttr != null) attrNamespaceHandle = nsAttr.getStart();

	final InstructionHandle[] targets = new InstructionHandle[types.length];
	for (int i = DTM.NTYPES; i < targets.length; i++) {
	    final TestSeq testSeq = _testSeq[i];
	    if (isNamespace[i]) {
		if (isAttribute[i])
		    targets[i] = attrNamespaceHandle;
		else
		    targets[i] = elemNamespaceHandle;
	    }
	    else if (testSeq != null) {
		if (isAttribute[i])
		    targets[i] = testSeq.compile(classGen, methodGen,
						 attrNamespaceHandle);
		else
		    targets[i] = testSeq.compile(classGen, methodGen,
						 elemNamespaceHandle);
	    }
	    else {
		targets[i] = ihLoop;
	    }
	}

	targets[DTM.ROOT_NODE] = _rootPattern != null
	    ? getTemplateInstructionHandle(_rootPattern.getTemplate())
	    : ihRecurse;
	targets[DTM.DOCUMENT_NODE] = _rootPattern != null
	    ? getTemplateInstructionHandle(_rootPattern.getTemplate())
	
	targets[DTM.TEXT_NODE] = _testSeq[DTM.TEXT_NODE] != null
	    ? _testSeq[DTM.TEXT_NODE].compile(classGen, methodGen, ihText)
	    : ihText;

	targets[DTM.NAMESPACE_NODE] = ihLoop;

	targets[DTM.ELEMENT_NODE] = elemNamespaceHandle;

	targets[DTM.ATTRIBUTE_NODE] = attrNamespaceHandle;

	InstructionHandle ihPI = ihLoop;
	if (_childNodeTestSeq != null) ihPI = ihElem;
	if (_testSeq[DTM.PROCESSING_INSTRUCTION_NODE] != null) {
	    targets[DTM.PROCESSING_INSTRUCTION_NODE] =
		_testSeq[DTM.PROCESSING_INSTRUCTION_NODE].
		compile(classGen, methodGen, ihPI);
	}
	else {
	    targets[DTM.PROCESSING_INSTRUCTION_NODE] = ihPI;
	}
	
	InstructionHandle ihComment = ihLoop;
	if (_childNodeTestSeq != null) ihComment = ihElem;
	targets[DTM.COMMENT_NODE] = _testSeq[DTM.COMMENT_NODE] != null
	    ? _testSeq[DTM.COMMENT_NODE].compile(classGen, methodGen, ihComment)
	    : ihComment;
	    
	targets[DTM.CDATA_SECTION_NODE] = ihLoop;

	targets[DTM.DOCUMENT_FRAGMENT_NODE] = ihLoop;
	
	targets[DTM.DOCUMENT_TYPE_NODE] = ihLoop;

	targets[DTM.ENTITY_NODE] = ihLoop;

	targets[DTM.ENTITY_REFERENCE_NODE] = ihLoop;
	
	targets[DTM.NOTATION_NODE] = ihLoop;



	for (int i = DTM.NTYPES; i < targets.length; i++) {
	    final TestSeq testSeq = _testSeq[i];
	    if ((testSeq == null) || (isNamespace[i])) {
		if (isAttribute[i])
		    targets[i] = attrNamespaceHandle;
		else
		    targets[i] = elemNamespaceHandle;
	    }
	    else {
		if (isAttribute[i])
		    targets[i] = testSeq.compile(classGen, methodGen,
						 attrNamespaceHandle);
		else
		    targets[i] = testSeq.compile(classGen, methodGen,
						 elemNamespaceHandle);
	    }
	}

	if (ilKey != null) body.insert(ilKey);

	final int getType = cpg.addInterfaceMethodref(DOM_INTF,
						      "getExpandedTypeID",
                                                      "(I)I");
	body.append(methodGen.loadDOM());
	body.append(new ILOAD(_currentIndex));
	body.append(new INVOKEINTERFACE(getType, 2));

	InstructionHandle disp = body.append(new SWITCH(types,targets,ihLoop));

	appendTestSequences(body);
	appendTemplateCode(body);

	if (nsElem != null) body.append(nsElem);
	if (nsAttr != null) body.append(nsAttr);

	body.append(ilRecurse);
	body.append(ilText);

	mainIL.append(body);
	mainIL.append(ilLoop);

	peepHoleOptimization(methodGen);
	methodGen.stripAttributes(true);
	
	methodGen.setMaxLocals();
	methodGen.setMaxStack();
	methodGen.removeNOPs();
	classGen.addMethod(methodGen.getMethod());

	_templates = oldTemplates;
    }

    /**
      * Peephole optimization.
      */
    private void peepHoleOptimization(MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        InstructionFinder find = new InstructionFinder(il);
	InstructionHandle ih;
	String pattern;

	pattern = "LoadInstruction POP";
	for (Iterator iter = find.search(pattern); iter.hasNext();) {
	    InstructionHandle[] match = (InstructionHandle[]) iter.next();
	    try {
		if (!match[0].hasTargeters() && !match[1].hasTargeters()) {
                    il.delete(match[0], match[1]);
                }
	    }
	    catch (TargetLostException e) {
            }
	}
        
	pattern = "ILOAD ILOAD SWAP ISTORE";
	for (Iterator iter = find.search(pattern); iter.hasNext();) {
            InstructionHandle[] match = (InstructionHandle[]) iter.next();
            try {              
                org.apache.bcel.generic.ILOAD iload1 = 
                    (org.apache.bcel.generic.ILOAD) match[0].getInstruction();
                org.apache.bcel.generic.ILOAD iload2 = 
                    (org.apache.bcel.generic.ILOAD) match[1].getInstruction();
                org.apache.bcel.generic.ISTORE istore = 
                    (org.apache.bcel.generic.ISTORE) match[3].getInstruction();
                
                if (!match[1].hasTargeters() &&
                    !match[2].hasTargeters() &&
                    !match[3].hasTargeters() &&
                    iload1.getIndex() == iload2.getIndex() &&
                    iload2.getIndex() == istore.getIndex())
                {
                    il.delete(match[1], match[3]);
                }
            }
            catch (TargetLostException e) {
            }
        }

	pattern = "LoadInstruction LoadInstruction SWAP";
	for (Iterator iter = find.search(pattern); iter.hasNext();) {
            InstructionHandle[] match = (InstructionHandle[])iter.next();
            try {
                if (!match[0].hasTargeters() &&
                    !match[1].hasTargeters() &&
                    !match[2].hasTargeters()) 
                {
                    Instruction load_m = match[1].getInstruction();
                    il.insert(match[0], load_m);
                    il.delete(match[1], match[2]);
                }
            }
            catch (TargetLostException e) {
            }
        }
                
	pattern = "ALOAD ALOAD";
        for (Iterator iter = find.search(pattern); iter.hasNext();) {
            InstructionHandle[] match = (InstructionHandle[])iter.next();
            try {
                if (!match[1].hasTargeters()) {
                    org.apache.bcel.generic.ALOAD aload1 = 
                        (org.apache.bcel.generic.ALOAD) match[0].getInstruction();
                    org.apache.bcel.generic.ALOAD aload2 = 
                        (org.apache.bcel.generic.ALOAD) match[1].getInstruction();
                    
                    if (aload1.getIndex() == aload2.getIndex()) {
                        il.insert(match[1], new DUP());
                        il.delete(match[1]);
                    }
                }
            }
            catch (TargetLostException e) {
            }
        }
    }

    public InstructionHandle getTemplateInstructionHandle(Template template) {
	return (InstructionHandle)_templateIHs.get(template);
    }

    /**
     * Auxiliary method to determine if a qname is an attribute.
     */
    private static boolean isAttributeName(String qname) {
	final int col = qname.lastIndexOf(':') + 1;
	return (qname.charAt(col) == '@');
    }

    /**
     * Auxiliary method to determine if a qname is a namespace 
     * qualified "*".
     */
    private static boolean isNamespaceName(String qname) {
	final int col = qname.lastIndexOf(':');
	return (col > -1 && qname.charAt(qname.length()-1) == '*');
    }
}
