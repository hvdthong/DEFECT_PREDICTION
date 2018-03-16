package org.apache.xalan.xsltc.compiler;

import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Whitespace extends TopLevelElement {
    public static final int USE_PREDICATE  = 0;
    public static final int STRIP_SPACE    = 1;
    public static final int PRESERVE_SPACE = 2;

    public static final int RULE_NONE      = 0; 

    private String _elementList;
    private int    _action;
    private int    _importPrecedence;

    /**
     * Auxillary class for encapsulating a single strip/preserve rule
     */
    private final static class WhitespaceRule {
	private final int _action;
	private int    _type;
	private int    _priority;
	
	/**
	 * Strip/preserve rule constructor
	 */
	public WhitespaceRule(int action, String element, int precedence) {
	    _action = action;

	    final int colon = element.indexOf(':');
	    if (colon >= 0) {
		_namespace = element.substring(0,colon);
		_element = element.substring(colon+1,element.length());
	    }
	    else {
		_namespace = Constants.EMPTYSTRING;
		_element = element;
	    }

	    _priority = precedence << 2;

	    if (_element.equals("*")) {
		if (_namespace == Constants.EMPTYSTRING) {
		}
		else {
		}
	    }
	    else {
	    }
	}

	/**
	 * For sorting rules depending on priority
	 */
	public int compareTo(WhitespaceRule other) {
	    return _priority < other._priority
		? -1
		: _priority > other._priority ? 1 : 0;
	}

	public int getAction() { return _action; }
	public int getStrength() { return _type; }
	public int getPriority() { return _priority; }
	public String getElement() { return _element; }
	public String getNamespace() { return _namespace; }
    }

    /**
     * Parse the attributes of the xsl:strip/preserve-space element.
     * The element should have not contents (ignored if any).
     */
    public void parseContents(Parser parser) {
	_action = _qname.getLocalPart().endsWith("strip-space") 
	    ? STRIP_SPACE : PRESERVE_SPACE;

	_importPrecedence = parser.getCurrentImportPrecedence();

	_elementList = getAttribute("elements");
	if (_elementList == null || _elementList.length() == 0) {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "elements");
	    return;
	}

	final SymbolTable stable = parser.getSymbolTable();
	StringTokenizer list = new StringTokenizer(_elementList);
	StringBuffer elements = new StringBuffer(Constants.EMPTYSTRING);

	while (list.hasMoreElements()) {
	    String token = list.nextToken();
	    String prefix;
	    String namespace;
	    int col;

	    if ((col = token.indexOf(':')) != -1) {
		prefix  = token.substring(0,col);
	    }
	    else {
		prefix = Constants.EMPTYSTRING;
	    }

	    namespace = lookupNamespace(prefix);
	    if (namespace != null)
		elements.append(namespace+":"+
				token.substring(col+1,token.length()));
	    else
		elements.append(token);

	    if (list.hasMoreElements())
		elements.append(" ");
	}
	_elementList = elements.toString();
    }


    /**
     * De-tokenize the elements listed in the 'elements' attribute and
     * instanciate a set of strip/preserve rules.
     */
    public Vector getRules() {
	final Vector rules = new Vector();
	final StringTokenizer list = new StringTokenizer(_elementList);
	while (list.hasMoreElements()) {
	    rules.add(new WhitespaceRule(_action,
					 list.nextToken(),
					 _importPrecedence));
	}
	return rules;
    }
    
    
    /**
     * Scans through the rules vector and looks for a rule of higher
     * priority that contradicts the current rule.
     */
    private static WhitespaceRule findContradictingRule(Vector rules,
							WhitespaceRule rule) {
	for (int i = 0; i < rules.size(); i++) {
	    WhitespaceRule currentRule = (WhitespaceRule)rules.elementAt(i);
	    if (currentRule == rule) {
		return null;
	    }
	    
	    /*
	     * See if there is a contradicting rule with higher priority.
	     * If the rules has the same action then this rule is redundant,
	     * if they have different action then this rule will never win.
	     */
	    switch (currentRule.getStrength()) {
	    case RULE_ALL:
		return currentRule;
		
	    case RULE_ELEMENT:
		if (!rule.getElement().equals(currentRule.getElement())) {
		    break;
		}
	    case RULE_NAMESPACE:
		if (rule.getNamespace().equals(currentRule.getNamespace())) {
		    return currentRule;
		}
		break;
	    }
	}
	return null;
    }


    /**
     * Orders a set or rules by priority, removes redundant rules and rules
     * that are shadowed by stronger, contradicting rules.
     */
    private static int prioritizeRules(Vector rules) {
	WhitespaceRule currentRule;
	int defaultAction = PRESERVE_SPACE;

	quicksort(rules, 0, rules.size()-1);

	boolean strip = false;
	for (int i = 0; i < rules.size(); i++) {
	    currentRule = (WhitespaceRule)rules.elementAt(i);
	    if (currentRule.getAction() == STRIP_SPACE) {
		strip = true;
	    }
	}
	if (!strip) {
	    rules.removeAllElements();
	    return PRESERVE_SPACE;
	}

	for (int idx = 0; idx < rules.size(); ) {
	    currentRule = (WhitespaceRule)rules.elementAt(idx);
	
	    if (findContradictingRule(rules,currentRule) != null) {
		rules.remove(idx);
	    }
	    else {
		if (currentRule.getStrength() == RULE_ALL) {
		    defaultAction = currentRule.getAction();
		    for (int i = idx; i < rules.size(); i++) {
			rules.removeElementAt(i);
		    }
		}
		idx++;
	    }
	}

	if (rules.size() == 0) {
	    return defaultAction;
	}

	do {
	    currentRule = (WhitespaceRule)rules.lastElement();
	    if (currentRule.getAction() == defaultAction) {
		rules.removeElementAt(rules.size() - 1);
	    }
	    else {
		break;
	    }
	} while (rules.size() > 0);
	
	return defaultAction;
    }

    public static void compileStripSpace(BranchHandle strip[], 
					 int sCount,
					 InstructionList il) {
	final InstructionHandle target = il.append(ICONST_1);
	il.append(IRETURN);
	for (int i = 0; i < sCount; i++) {
	    strip[i].setTarget(target);
	}
    }

    public static void compilePreserveSpace(BranchHandle preserve[], 
					    int pCount,
					    InstructionList il) {
	final InstructionHandle target = il.append(ICONST_0);
	il.append(IRETURN);
	for (int i = 0; i < pCount; i++) {
	    preserve[i].setTarget(target);
	}
    }

    /*
    private static void compileDebug(ClassGenerator classGen,
				     InstructionList il) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final int prt = cpg.addMethodref("java/lang/System/out",
					 "println",
					 "(Ljava/lang/String;)V");
	il.append(DUP);
	il.append(new INVOKESTATIC(prt));
    }
    */

    /**
     * Compiles the predicate method
     */
    private static void compilePredicate(Vector rules,
					 int defaultAction,
					 ClassGenerator classGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = new InstructionList();
	final XSLTC xsltc = classGen.getParser().getXSLTC();

	final MethodGenerator stripSpace =
	    new MethodGenerator(ACC_PUBLIC | ACC_FINAL ,
			org.apache.bcel.generic.Type.BOOLEAN, 
			new org.apache.bcel.generic.Type[] {
			    Util.getJCRefType(DOM_INTF_SIG),
			    org.apache.bcel.generic.Type.INT,
			    org.apache.bcel.generic.Type.INT
			},
			new String[] { "dom","node","type" },
			"stripSpace",classGen.getClassName(),il,cpg);

	classGen.addInterface("org/apache/xalan/xsltc/StripFilter");

	final int paramDom = stripSpace.getLocalIndex("dom");
	final int paramCurrent = stripSpace.getLocalIndex("node");
	final int paramType = stripSpace.getLocalIndex("type");

	BranchHandle strip[] = new BranchHandle[rules.size()];
	BranchHandle preserve[] = new BranchHandle[rules.size()];
	int sCount = 0;
	int pCount = 0;

	for (int i = 0; i<rules.size(); i++) {
	    WhitespaceRule rule = (WhitespaceRule)rules.elementAt(i);

	    final int gns = cpg.addInterfaceMethodref(DOM_INTF,
						      "getNamespaceName",
						      "(I)Ljava/lang/String;");

	    final int strcmp = cpg.addMethodref("java/lang/String",
						"compareTo",
						"(Ljava/lang/String;)I");

	    if (rule.getStrength() == RULE_NAMESPACE) {
		il.append(new ALOAD(paramDom));
		il.append(new ILOAD(paramCurrent));
		il.append(new INVOKEINTERFACE(gns,2));
		il.append(new PUSH(cpg, rule.getNamespace()));
		il.append(new INVOKEVIRTUAL(strcmp));
		il.append(ICONST_0);

		if (rule.getAction() == STRIP_SPACE) {
		    strip[sCount++] = il.append(new IF_ICMPEQ(null));
		}
		else {
		    preserve[pCount++] = il.append(new IF_ICMPEQ(null));
		}
	    }
	    else if (rule.getStrength() == RULE_ELEMENT) {
		final Parser parser = classGen.getParser();
		QName qname;
		if (rule.getNamespace() != Constants.EMPTYSTRING )
		    qname = parser.getQName(rule.getNamespace(), null,
					    rule.getElement());
		else
		    qname = parser.getQName(rule.getElement());

		final int elementType = xsltc.registerElement(qname);
		il.append(new ILOAD(paramType));
		il.append(new PUSH(cpg, elementType));

		if (rule.getAction() == STRIP_SPACE)
		    strip[sCount++] = il.append(new IF_ICMPEQ(null));
		else
		    preserve[pCount++] = il.append(new IF_ICMPEQ(null));
	    }
	}

	if (defaultAction == STRIP_SPACE) {
	    compileStripSpace(strip, sCount, il);
	    compilePreserveSpace(preserve, pCount, il);
	}
	else {
	    compilePreserveSpace(preserve, pCount, il);
	    compileStripSpace(strip, sCount, il);
	}

	stripSpace.stripAttributes(true);
	stripSpace.setMaxLocals();
	stripSpace.setMaxStack();
	stripSpace.removeNOPs();

	classGen.addMethod(stripSpace.getMethod());
    }

    /**
     * Compiles the predicate method
     */
    private static void compileDefault(int defaultAction,
				       ClassGenerator classGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = new InstructionList();
	final XSLTC xsltc = classGen.getParser().getXSLTC();

	final MethodGenerator stripSpace =
	    new MethodGenerator(ACC_PUBLIC | ACC_FINAL ,
			org.apache.bcel.generic.Type.BOOLEAN, 
			new org.apache.bcel.generic.Type[] {
			    Util.getJCRefType(DOM_INTF_SIG),
			    org.apache.bcel.generic.Type.INT,
			    org.apache.bcel.generic.Type.INT
			},
			new String[] { "dom","node","type" },
			"stripSpace",classGen.getClassName(),il,cpg);

	classGen.addInterface("org/apache/xalan/xsltc/StripFilter");

	if (defaultAction == STRIP_SPACE)
	    il.append(ICONST_1);
	else
	    il.append(ICONST_0);
	il.append(IRETURN);

	stripSpace.stripAttributes(true);
	stripSpace.setMaxLocals();
	stripSpace.setMaxStack();
	stripSpace.removeNOPs();

	classGen.addMethod(stripSpace.getMethod());
    }


    /**
     * Takes a vector of WhitespaceRule objects and generates a predicate
     * method. This method returns the translets default action for handling
     * whitespace text-nodes:
     *    - USE_PREDICATE  (run the method generated by this method)
     *    - STRIP_SPACE    (always strip whitespace text-nodes)
     *    - PRESERVE_SPACE (always preserve whitespace text-nodes)
     */
    public static int translateRules(Vector rules,
				     ClassGenerator classGen) {
	final int defaultAction = prioritizeRules(rules);
	if (rules.size() == 0) {
	    compileDefault(defaultAction,classGen);
	    return defaultAction;
	}
	compilePredicate(rules, defaultAction, classGen);
	return USE_PREDICATE;
    }

    /**
     * Sorts a range of rules with regard to PRIORITY only
     */
    private static void quicksort(Vector rules, int p, int r) {
	while (p < r) {
	    final int q = partition(rules, p, r);
	    quicksort(rules, p, q);
	    p = q + 1;
	}
    }
    
    /**
     * Used with quicksort method above
     */
    private static int partition(Vector rules, int p, int r) {
	final WhitespaceRule x = (WhitespaceRule)rules.elementAt((p+r) >>> 1);
	int i = p - 1, j = r + 1;
	while (true) {
	    while (x.compareTo((WhitespaceRule)rules.elementAt(--j)) < 0) {
	    }
	    while (x.compareTo((WhitespaceRule)rules.elementAt(++i)) > 0) {
	    }
	    if (i < j) {
		final WhitespaceRule tmp = (WhitespaceRule)rules.elementAt(i);
		rules.setElementAt(rules.elementAt(j), i);
		rules.setElementAt(tmp, j);
	    }
	    else {
		return j;
	    }
	}
    }
    
    /**
     * Type-check contents/attributes - nothing to do...
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    }

    /**
     * This method should not produce any code
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }
}
