package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.text.Collator;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

import org.apache.xalan.xsltc.dom.*;
import org.apache.xalan.xsltc.compiler.util.*;


final class Sort extends Instruction implements Closure {

    private Expression     _select;
    private AttributeValue _order;
    private AttributeValue _caseOrder;
    private AttributeValue _dataType;

    private String         _data = null;
    public  String         _lang;
    public  String         _country;

    private String _className = null;
    private ArrayList _closureVars = null;
    private boolean _needsSortRecordFactory = false;


    /**
     * Returns true if this closure is compiled in an inner class (i.e.
     * if this is a real closure).
     */
    public boolean inInnerClass() {
	return (_className != null);
    }

    /**
     * Returns a reference to its parent closure or null if outermost.
     */
    public Closure getParentClosure() {
	return null;
    }

    /**
     * Returns the name of the auxiliary class or null if this predicate 
     * is compiled inside the Translet.
     */
    public String getInnerClassName() {
	return _className;
    }

    /**
     * Add new variable to the closure.
     */
    public void addVariable(VariableRefBase variableRef) {
	if (_closureVars == null) {
	    _closureVars = new ArrayList();
	}

	if (!_closureVars.contains(variableRef)) {
	    _closureVars.add(variableRef);
	    _needsSortRecordFactory = true;
	}
    }


    private void setInnerClassName(String className) {
	_className = className;
    }

    /**
     * Parse the attributes of the xsl:sort element
     */
    public void parseContents(Parser parser) {

	final SyntaxTreeNode parent = getParent();
	if (!(parent instanceof ApplyTemplates) &&
	    !(parent instanceof ForEach)) {
	    reportError(this, parser, ErrorMsg.STRAY_SORT_ERR, null);
	    return;
	}

	_select = parser.parseExpression(this, "select", "string(.)");

	String val = getAttribute("order");
	if (val.length() == 0) val = "ascending";
	_order = AttributeValue.create(this, val, parser);

	val = getAttribute("case-order");
	if (val.length() == 0) val = "upper-first";
	_caseOrder = AttributeValue.create(this, val, parser);

	val = getAttribute("data-type");
	if (val.length() == 0) {
	    try {
		final Type type = _select.typeCheck(parser.getSymbolTable());
		if (type instanceof IntType)
		    val = "number";
		else
		    val = "text";
	    }
	    catch (TypeCheckError e) {
		val = "text";
	    }
	}
	_dataType = AttributeValue.create(this, val, parser);

	if ((val = getAttribute("lang")) != null) {
	    try {
		StringTokenizer st = new StringTokenizer(val,"-",false);
		_lang = st.nextToken();
		_country = st.nextToken();
	    }
	    }
	}
    }
    
    /**
     * Run type checks on the attributes; expression must return a string
     * which we will use as a sort key
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Type tselect = _select.typeCheck(stable);

	if (!(tselect instanceof StringType)) {
	    _select = new CastExpr(_select, Type.String);
	}

	_order.typeCheck(stable);
	_caseOrder.typeCheck(stable);
	_dataType.typeCheck(stable);
	return Type.Void;
    }

    /**
     * These two methods are needed in the static methods that compile the
     * overloaded NodeSortRecord.compareType() and NodeSortRecord.sortOrder()
     */
    public void translateSortType(ClassGenerator classGen,
				  MethodGenerator methodGen) {
	_dataType.translate(classGen, methodGen);
    }
    
    public void translateSortOrder(ClassGenerator classGen,
				   MethodGenerator methodGen) {
	_order.translate(classGen, methodGen);
    }
    
    /**
     * This method compiles code for the select expression for this
     * xsl:sort element. The method is called from the static code-generating
     * methods in this class.
     */
    public void translateSelect(ClassGenerator classGen,
				MethodGenerator methodGen) {
	_select.translate(classGen,methodGen);
    }

    /**
     * This method should not produce any code
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }

    /**
     * Compiles code that instantiates a SortingIterator object.
     * This object's constructor needs referencdes to the current iterator
     * and a node sort record producing objects as its parameters.
     */
    public static void translateSortIterator(ClassGenerator classGen,
				      MethodGenerator methodGen,
				      Expression nodeSet,
				      Vector sortObjects) 
    {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int init = cpg.addMethodref(SORT_ITERATOR, "<init>",
					  "("
					  + NODE_ITERATOR_SIG
					  + NODE_SORT_FACTORY_SIG
					  + ")V");	

	il.append(new NEW(cpg.addClass(SORT_ITERATOR)));
	il.append(DUP);

	    final int children = cpg.addInterfaceMethodref(DOM_INTF,
							   "getAxisIterator",
							   "(I)"+
							   NODE_ITERATOR_SIG);
	    il.append(methodGen.loadDOM());
	    il.append(new PUSH(cpg, Axis.CHILD));
	    il.append(new INVOKEINTERFACE(children, 2));
	}
	else {
	    nodeSet.translate(classGen, methodGen);
	}
	
	compileSortRecordFactory(sortObjects, classGen, methodGen);
	il.append(new INVOKESPECIAL(init));
    }


    /**
     * Compiles code that instantiates a NodeSortRecordFactory object which
     * will produce NodeSortRecord objects of a specific type.
     */
    public static void compileSortRecordFactory(Vector sortObjects,
	ClassGenerator classGen, MethodGenerator methodGen) 
    {
	String sortRecordClass = 
	    compileSortRecord(sortObjects, classGen, methodGen);

	boolean needsSortRecordFactory = false;
	final int nsorts = sortObjects.size();
	for (int i = 0; i < nsorts; i++) {
	    final Sort sort = (Sort) sortObjects.elementAt(i);
	    needsSortRecordFactory |= sort._needsSortRecordFactory;
	}

	String sortRecordFactoryClass = NODE_SORT_FACTORY;
	if (needsSortRecordFactory) {
	    sortRecordFactoryClass = 
		compileSortRecordFactory(sortObjects, classGen, methodGen, 
		    sortRecordClass);
	}

	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	
	il.append(new NEW(cpg.addClass(sortRecordFactoryClass)));
	il.append(DUP);
	il.append(methodGen.loadDOM());
	il.append(new PUSH(cpg, sortRecordClass));
	il.append(classGen.loadTranslet());

	il.append(new PUSH(cpg, nsorts));
	il.append(new ANEWARRAY(cpg.addClass(STRING)));
	for (int level = 0; level < nsorts; level++) {
	    final Sort sort = (Sort)sortObjects.elementAt(level);
	    il.append(DUP);
	    il.append(new PUSH(cpg, level));
	    sort.translateSortOrder(classGen, methodGen);
	    il.append(AASTORE);
	}

	il.append(new PUSH(cpg, nsorts));
	il.append(new ANEWARRAY(cpg.addClass(STRING)));
	for (int level = 0; level < nsorts; level++) {
	    final Sort sort = (Sort)sortObjects.elementAt(level);
	    il.append(DUP);
	    il.append(new PUSH(cpg, level));
	    sort.translateSortType(classGen, methodGen);
	    il.append(AASTORE);
	}

	il.append(new INVOKESPECIAL(
	    cpg.addMethodref(sortRecordFactoryClass, "<init>", 
		"(" + DOM_INTF_SIG 
		    + STRING_SIG
		    + TRANSLET_INTF_SIG
		    + "[" + STRING_SIG
		    + "[" + STRING_SIG + ")V")));

	final ArrayList dups = new ArrayList();

	for (int j = 0; j < nsorts; j++) {
	    final Sort sort = (Sort) sortObjects.get(j);
	    final int length = (sort._closureVars == null) ? 0 : 
		sort._closureVars.size();

	    for (int i = 0; i < length; i++) {
		VariableRefBase varRef = (VariableRefBase) sort._closureVars.get(i);

		if (dups.contains(varRef)) continue;

		final VariableBase var = varRef.getVariable();

		il.append(DUP);
		il.append(var.loadInstruction());
		il.append(new PUTFIELD(
			cpg.addFieldref(sortRecordFactoryClass, var.getVariable(), 
			    var.getType().toSignature())));
		dups.add(varRef);
	    }
	}
    }

    public static String compileSortRecordFactory(Vector sortObjects,
	ClassGenerator classGen, MethodGenerator methodGen, 
	String sortRecordClass)
    {
	final XSLTC  xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
	final String className = xsltc.getHelperClassName();

	final NodeSortRecordFactGenerator sortRecordFactory =
	    new NodeSortRecordFactGenerator(className,
					NODE_SORT_FACTORY,
					className + ".java",
					ACC_PUBLIC | ACC_SUPER | ACC_FINAL,
					new String[] {},
					classGen.getStylesheet());

	ConstantPoolGen cpg = sortRecordFactory.getConstantPool();

	final int nsorts = sortObjects.size();
	final ArrayList dups = new ArrayList();

	for (int j = 0; j < nsorts; j++) {
	    final Sort sort = (Sort) sortObjects.get(j);
	    final int length = (sort._closureVars == null) ? 0 : 
		sort._closureVars.size();

	    for (int i = 0; i < length; i++) {
		final VariableRef varRef = (VariableRef) sort._closureVars.get(i);

		if (dups.contains(varRef)) continue;

		final VariableBase var = varRef.getVariable();
		sortRecordFactory.addField(new Field(ACC_PUBLIC, 
					   cpg.addUtf8(var.getVariable()),
					   cpg.addUtf8(var.getType().toSignature()),
					   null, cpg.getConstantPool()));
		dups.add(varRef);
	    }
	}

	final org.apache.bcel.generic.Type[] argTypes = 
	    new org.apache.bcel.generic.Type[5];
	argTypes[0] = Util.getJCRefType(DOM_INTF_SIG);
	argTypes[1] = Util.getJCRefType(STRING_SIG);
	argTypes[2] = Util.getJCRefType(TRANSLET_INTF_SIG);
	argTypes[3] = Util.getJCRefType("[" + STRING_SIG);
	argTypes[4] = Util.getJCRefType("[" + STRING_SIG);

	final String[] argNames = new String[5];
	argNames[0] = DOCUMENT_PNAME;
	argNames[1] = "className";
	argNames[2] = TRANSLET_PNAME;
	argNames[3] = "order";
	argNames[4] = "type";

	InstructionList il = new InstructionList();
	final MethodGenerator constructor =
	    new MethodGenerator(ACC_PUBLIC,
				org.apache.bcel.generic.Type.VOID, 
				argTypes, argNames, "<init>", 
				className, il, cpg);

	il.append(ALOAD_0);
	il.append(ALOAD_1);
	il.append(ALOAD_2);
	il.append(new ALOAD(3));
	il.append(new ALOAD(4));
	il.append(new ALOAD(5));
	il.append(new INVOKESPECIAL(cpg.addMethodref(NODE_SORT_FACTORY,
	    "<init>", 
	    "(" + DOM_INTF_SIG 
		+ STRING_SIG 
		+ TRANSLET_INTF_SIG 
		+ "[" + STRING_SIG
		+ "[" + STRING_SIG + ")V")));
	il.append(RETURN);

	il = new InstructionList(); 
	final MethodGenerator makeNodeSortRecord =
	    new MethodGenerator(ACC_PUBLIC,
		Util.getJCRefType(NODE_SORT_RECORD_SIG), 
		new org.apache.bcel.generic.Type[] { 
		    org.apache.bcel.generic.Type.INT,
		    org.apache.bcel.generic.Type.INT },
		new String[] { "node", "last" }, "makeNodeSortRecord",
		className, il, cpg);

	il.append(ALOAD_0);
	il.append(ILOAD_1);
	il.append(ILOAD_2);
	il.append(new INVOKESPECIAL(cpg.addMethodref(NODE_SORT_FACTORY,
	    "makeNodeSortRecord", "(II)" + NODE_SORT_RECORD_SIG)));
	il.append(DUP);
	il.append(new CHECKCAST(cpg.addClass(sortRecordClass)));

	final int ndups = dups.size();
	for (int i = 0; i < ndups; i++) {
	    final VariableRef varRef = (VariableRef) dups.get(i);
	    final VariableBase var = varRef.getVariable();
	    final Type varType = var.getType();
	    
	    il.append(DUP);

	    il.append(ALOAD_0);
	    il.append(new GETFIELD(
		cpg.addFieldref(className,
		    var.getVariable(), varType.toSignature())));

	    il.append(new PUTFIELD(
		cpg.addFieldref(sortRecordClass,
		    var.getVariable(), varType.toSignature())));
	}
	il.append(POP);
	il.append(ARETURN);

	constructor.setMaxLocals();
	constructor.setMaxStack();
	sortRecordFactory.addMethod(constructor.getMethod());
	makeNodeSortRecord.setMaxLocals();
	makeNodeSortRecord.setMaxStack();
	sortRecordFactory.addMethod(makeNodeSortRecord.getMethod());
	xsltc.dumpClass(sortRecordFactory.getJavaClass());

	return className;
    }

    /**
     * Create a new auxillary class extending NodeSortRecord.
     */
    private static String compileSortRecord(Vector sortObjects,
					    ClassGenerator classGen,
					    MethodGenerator methodGen) {
	final XSLTC  xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
	final String className = xsltc.getHelperClassName();

	final NodeSortRecordGenerator sortRecord =
	    new NodeSortRecordGenerator(className,
					NODE_SORT_RECORD,
					"sort$0.java",
					ACC_PUBLIC | ACC_SUPER | ACC_FINAL,
					new String[] {},
					classGen.getStylesheet());
	
	final ConstantPoolGen cpg = sortRecord.getConstantPool();	

	final int nsorts = sortObjects.size();
	final ArrayList dups = new ArrayList();

	for (int j = 0; j < nsorts; j++) {
	    final Sort sort = (Sort) sortObjects.get(j);

	    sort.setInnerClassName(className);	

	    final int length = (sort._closureVars == null) ? 0 : 
		sort._closureVars.size();
	    for (int i = 0; i < length; i++) {
		final VariableRef varRef = (VariableRef) sort._closureVars.get(i);

		if (dups.contains(varRef)) continue;

		final VariableBase var = varRef.getVariable();
		sortRecord.addField(new Field(ACC_PUBLIC, 
				    cpg.addUtf8(var.getVariable()),
				    cpg.addUtf8(var.getType().toSignature()),
				    null, cpg.getConstantPool()));
		dups.add(varRef);
	    }
	}

	Method clinit = compileClassInit(sortObjects, sortRecord,
					 cpg, className);
	Method extract = compileExtract(sortObjects, sortRecord,
					cpg, className);
	sortRecord.addMethod(clinit);
	sortRecord.addEmptyConstructor(ACC_PUBLIC);
	sortRecord.addMethod(extract);

	for (int i = 0; i < sortObjects.size(); i++) {
	    if (((Sort)(sortObjects.elementAt(i)))._lang != null) {
		sortRecord.addMethod(compileGetCollator(sortObjects,
							sortRecord,
							cpg,
							className));
		i = sortObjects.size();
	    }
	}
	
	xsltc.dumpClass(sortRecord.getJavaClass());
	return className;
    }

    /**
     * Create a class constructor for the new class. All this constructor does
     * is to initialize a couple of tables that contain information on sort
     * order and sort type. These static tables cannot be in the parent class.
     */
    private static Method compileClassInit(Vector sortObjects,
					   NodeSortRecordGenerator sortRecord,
					   ConstantPoolGen cpg,
					   String className) {
	final InstructionList il = new InstructionList();
	final CompareGenerator classInit =
	    new CompareGenerator(ACC_PUBLIC | ACC_STATIC,
				 org.apache.bcel.generic.Type.VOID, 
				 new org.apache.bcel.generic.Type[] { },
				 new String[] { },
				 "<clinit>", className, il, cpg);

	final int initLocale =  cpg.addMethodref("java/util/Locale",
						 "<init>",
						 "(Ljava/lang/String;"+
						 "Ljava/lang/String;)V");
	
	final int getCollator = cpg.addMethodref(COLLATOR_CLASS,
						 "getInstance",
						 "(Ljava/util/Locale;)"+
						 COLLATOR_SIG);

	final int setStrength = cpg.addMethodref(COLLATOR_CLASS,
						 "setStrength", "(I)V");

	final int levels = sortObjects.size();

	/*
	final int levelsField = cpg.addFieldref(className, "_levels", "I");
	il.append(new PUSH(cpg, levels));
	il.append(new PUTSTATIC(levelsField));
	*/

	String language = null;
	String country = null;
	Sort sort = (Sort)sortObjects.elementAt(0);

	for (int level = 0; level < levels; level++) {
	    if (language == null && sort._lang != null)
		language = sort._lang;
	    if (country == null && sort._country != null)
		country = sort._country;
	}

	final int collator =
	    cpg.addFieldref(className, "_collator", COLLATOR_SIG);

	if (language != null) {
	    il.append(new NEW(cpg.addClass("java/util/Locale")));
	    il.append(DUP);
	    il.append(new PUSH(cpg, language));
	    il.append(new PUSH(cpg, (country != null ? country : EMPTYSTRING)));
	    il.append(new INVOKESPECIAL(initLocale));
	    
	    il.append(new INVOKESTATIC(getCollator));
	    il.append(new PUTSTATIC(collator));
	}

	il.append(new GETSTATIC(collator));
	il.append(new ICONST(Collator.TERTIARY));
	il.append(new INVOKEVIRTUAL(setStrength));

	il.append(RETURN);

	classInit.stripAttributes(true);
	classInit.setMaxLocals();
	classInit.setMaxStack();
	classInit.removeNOPs();

	return classInit.getMethod();
    }


    /**
     * Compiles a method that overloads NodeSortRecord.extractValueFromDOM()
     */
    private static Method compileExtract(Vector sortObjects,
					 NodeSortRecordGenerator sortRecord,
					 ConstantPoolGen cpg,
					 String className) {
	final InstructionList il = new InstructionList();
	
	final CompareGenerator extractMethod =
	    new CompareGenerator(ACC_PUBLIC | ACC_FINAL,
				 org.apache.bcel.generic.Type.STRING, 
				 new org.apache.bcel.generic.Type[] {
		                     Util.getJCRefType(DOM_INTF_SIG),
				     org.apache.bcel.generic.Type.INT,
				     org.apache.bcel.generic.Type.INT,
				     Util.getJCRefType(TRANSLET_SIG),
				     org.apache.bcel.generic.Type.INT
				 },
				 new String[] { "dom",
						"current",
						"level",
						"translet",
						"last"
				 },
				 "extractValueFromDOM", className, il, cpg);

	final int levels = sortObjects.size();
	final int match[] = new int[levels];
	final InstructionHandle target[] = new InstructionHandle[levels];
	InstructionHandle tblswitch = null;

	if (levels > 1) {
	    il.append(new ILOAD(extractMethod.getLocalIndex("level")));
	    tblswitch = il.append(new NOP());
	}

	for (int level = 0; level < levels; level++) {
	    match[level] = level;
	    final Sort sort = (Sort)sortObjects.elementAt(level);
	    target[level] = il.append(NOP);
	    sort.translateSelect(sortRecord, extractMethod);
	    il.append(ARETURN);
	}
	
	if (levels > 1) {
	    InstructionHandle defaultTarget =
		il.append(new PUSH(cpg, EMPTYSTRING));
	    il.insert(tblswitch,new TABLESWITCH(match, target, defaultTarget));
	    il.append(ARETURN);
	}

	extractMethod.stripAttributes(true);
	extractMethod.setMaxLocals();
	extractMethod.setMaxStack();
	extractMethod.removeNOPs();

	return extractMethod.getMethod();
    }

    /**
     * Compiles a method that overloads NodeSortRecord.getCollator()
     * This method is only compiled if the "lang" attribute is used.
     */
    private static Method compileGetCollator(Vector sortObjects,
					     NodeSortRecordGenerator sortRecord,
					     ConstantPoolGen cpg,
					     String className) {
	final InstructionList il = new InstructionList();
	final MethodGenerator getCollator =
	    new MethodGenerator(ACC_PUBLIC | ACC_FINAL,
				Util.getJCRefType(COLLATOR_SIG),
				new org.apache.bcel.generic.Type[] {},
				new String[] { },
				"getCollator", className, il, cpg);

	final int collator =
	    cpg.addFieldref(className, "collator", COLLATOR_SIG);
	il.append(new GETSTATIC(collator));
	il.append(ARETURN);

	getCollator.stripAttributes(true);
	getCollator.setMaxLocals();
	getCollator.setMaxStack();
	getCollator.removeNOPs();

	return getCollator.getMethod();
    }
}
