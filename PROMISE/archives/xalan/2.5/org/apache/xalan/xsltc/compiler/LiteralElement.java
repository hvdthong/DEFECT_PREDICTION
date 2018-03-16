package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class LiteralElement extends Instruction {

    private String _name;
    private LiteralElement _literalElemParent;
    private Vector _attributeElements = null;
    private Hashtable _accessedPrefixes = null;

    private final static String XMLNS_STRING = "xmlns";

    /**
     * Returns the QName for this literal element
     */
    public QName getName() {
	return _qname;
    }
 
    /**
     * Displays the contents of this literal element
     */
    public void display(int indent) {
	indent(indent);
	Util.println("LiteralElement name = " + _name);
	displayContents(indent + IndentIncrement);
    }

    /**
     * Returns the namespace URI for which a prefix is pointing to
     */
    private String accessedNamespace(String prefix) {
	if (_accessedPrefixes == null)
	    return(null);
	else
	    return((String)_accessedPrefixes.get(prefix));
    }

    /**
     * Method used to keep track of what namespaces that are references by
     * this literal element and its attributes. The output must contain a
     * definition for each namespace, so we stuff them in a hashtable.
     */
    public void registerNamespace(String prefix, String uri,
				  SymbolTable stable, boolean declared) {

	if (_literalElemParent != null) {
	    final String parentUri = _literalElemParent.accessedNamespace(prefix);
	    if (parentUri == null) {
		_literalElemParent.registerNamespace(prefix, uri, stable, declared);
		return;
	    }
	    if (parentUri.equals(uri)) return;
	}

	if (_accessedPrefixes == null) {
	    _accessedPrefixes = new Hashtable();
	}
	else {
	    if (!declared) {
		final String old = (String)_accessedPrefixes.get(prefix);
		if (old != null) {
		    if (old.equals(uri))
			return;
		    else 
			prefix = stable.generateNamespacePrefix();
		}
	    }
	}

	if (!prefix.equals("xml")) {
	    _accessedPrefixes.put(prefix,uri);
	}
    }

    /**
     * Translates the prefix of a QName according to the rules set in
     * the attributes of xsl:stylesheet. Also registers a QName to assure
     * that the output element contains the necessary namespace declarations.
     */
    private String translateQName(QName qname, SymbolTable stable) {
	String localname = qname.getLocalPart();
	String prefix = qname.getPrefix();

	if (prefix == null)
	    prefix = Constants.EMPTYSTRING;
	else if (prefix.equals(XMLNS_STRING))
	    return(XMLNS_STRING);
	
	final String alternative = stable.lookupPrefixAlias(prefix);
	if (alternative != null) {
	    stable.excludeNamespaces(prefix);
	    prefix = alternative;
	}

	String uri = lookupNamespace(prefix);
	if (uri == null) return(localname);

	registerNamespace(prefix, uri, stable, false);

	if (prefix != Constants.EMPTYSTRING)
	    return(prefix+":"+localname);
	else
	    return(localname);
    }

    /**
     * Add an attribute to this element
     */
    public void addAttribute(SyntaxTreeNode attribute) {
	if (_attributeElements == null) {
	    _attributeElements = new Vector(2);
	}
	_attributeElements.add(attribute);
    }

    /**
     * Set the first attribute of this element
     */
    public void setFirstAttribute(SyntaxTreeNode attribute) {
	if (_attributeElements == null) {
	    _attributeElements = new Vector(2);
	}
	_attributeElements.insertElementAt(attribute,0);
    }

    /**
     * Type-check the contents of this element. The element itself does not
     * need any type checking as it leaves nothign on the JVM's stack.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_attributeElements != null) {
	    final int count = _attributeElements.size();
	    for (int i = 0; i < count; i++) {
		SyntaxTreeNode node = 
		    (SyntaxTreeNode)_attributeElements.elementAt(i);
		node.typeCheck(stable);
	    }
	}
	typeCheckContents(stable);
	return Type.Void;
    }

    /**
     * This method starts at a given node, traverses all namespace mappings,
     * and assembles a list of all prefixes that (for the given node) maps
     * to _ANY_ namespace URI. Used by literal result elements to determine
     */
    public Enumeration getNamespaceScope(SyntaxTreeNode node) {
	Hashtable all = new Hashtable();
	
	while (node != null) {
	    Hashtable mapping = node.getPrefixMapping();
	    if (mapping != null) {
		Enumeration prefixes = mapping.keys();
		while (prefixes.hasMoreElements()) {
		    String prefix = (String)prefixes.nextElement();
		    if (!all.containsKey(prefix)) {
			all.put(prefix, mapping.get(prefix));
		    }
		}
	    }
	    node = node.getParent();
	}
	return(all.keys());
    }

    /**
     * Determines the final QName for the element and its attributes.
     * Registers all namespaces that are used by the element/attributes
     */
    public void parseContents(Parser parser) {
	final SymbolTable stable = parser.getSymbolTable();
	stable.setCurrentNode(this);

	SyntaxTreeNode _literalElemParent = getParent();
	while (_literalElemParent != null && !(_literalElemParent instanceof LiteralElement)) {
	    _literalElemParent = _literalElemParent.getParent();
	}

	if (!(_literalElemParent instanceof LiteralElement)) {
	    _literalElemParent = null;
	}

	_name = translateQName(_qname, stable);

	final int count = _attributes.getLength();
	for (int i = 0; i < count; i++) {
	    final QName qname = parser.getQName(_attributes.getQName(i));
	    final String uri = qname.getNamespace();
	    final String val = _attributes.getValue(i);

	    if (qname == parser.getUseAttributeSets()) {
		setFirstAttribute(new UseAttributeSets(val, parser));
	    }
	    else if (qname == parser.getExtensionElementPrefixes()) {
		stable.excludeNamespaces(val);
	    }
	    else if (qname == parser.getExcludeResultPrefixes()) {
		stable.excludeNamespaces(val);
	    }
	    else {
		final String prefix = qname.getPrefix();
		if (prefix != null && prefix.equals(XMLNS_PREFIX) ||
		    prefix == null && qname.getLocalPart().equals("xmlns") ||
		    uri != null && uri.equals(XSLT_URI))
		{
		    continue;	
		}

		final String name = translateQName(qname, stable);
		LiteralAttribute attr = new LiteralAttribute(name, val, parser);
		addAttribute(attr);
		attr.setParent(this);
		attr.parseContents(parser);
	    }
	}

	final Enumeration include = getNamespaceScope(this);
	while (include.hasMoreElements()) {
	    final String prefix = (String)include.nextElement();
	    if (!prefix.equals("xml")) {
		final String uri = lookupNamespace(prefix);
		if (uri != null && !stable.isExcludedNamespace(uri)) {
		    registerNamespace(prefix, uri, stable, true);
		}
	    }
	}

	parseChildren(parser);

	for (int i = 0; i < count; i++) {
	    final QName qname = parser.getQName(_attributes.getQName(i));
	    final String val = _attributes.getValue(i);

	    if (qname == parser.getExtensionElementPrefixes()) {
		stable.unExcludeNamespaces(val);
	    }
	    else if (qname == parser.getExcludeResultPrefixes()) {
		stable.unExcludeNamespaces(val);
	    }
	}
    }

    protected boolean contextDependent() {
	return dependentContents();
    }

    /**
     * Compiles code that emits the literal element to the output handler,
     * first the start tag, then namespace declaration, then attributes,
     * then the element contents, and then the element end tag. Since the
     * value of an attribute may depend on a variable, variables must be
     * compiled first.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {

	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	il.append(methodGen.loadHandler());
	il.append(new PUSH(cpg, _name));
	il.append(methodGen.startElement());

	for (int i = 0; i < elementCount(); i++) {
	    final SyntaxTreeNode item = (SyntaxTreeNode) elementAt(i);
	    if (item instanceof Variable) {
		item.translate(classGen, methodGen);
	    }
	}

	if (_accessedPrefixes != null) {
	    boolean declaresDefaultNS = false;
	    Enumeration e = _accessedPrefixes.keys();

	    while (e.hasMoreElements()) {
		final String prefix = (String)e.nextElement();
		final String uri = (String)_accessedPrefixes.get(prefix);

		if (uri != Constants.EMPTYSTRING || 
			prefix != Constants.EMPTYSTRING) 
		{
		    if (prefix == Constants.EMPTYSTRING) {
			declaresDefaultNS = true;
		    }
		    il.append(methodGen.loadHandler());
		    il.append(new PUSH(cpg,prefix));
		    il.append(new PUSH(cpg,uri));
		    il.append(methodGen.namespace());
		}
	    }

	    /* 
	     * If our XslElement parent redeclares the default NS, and this
	     * element doesn't, it must be redeclared one more time.
	     */
	    if (!declaresDefaultNS && (_parent instanceof XslElement)
		    && ((XslElement) _parent).declaresDefaultNS()) 
	    {
		il.append(methodGen.loadHandler());
		il.append(new PUSH(cpg, Constants.EMPTYSTRING));
		il.append(new PUSH(cpg, Constants.EMPTYSTRING));
		il.append(methodGen.namespace());
	    }
	}

	if (_attributeElements != null) {
	    final int count = _attributeElements.size();
	    for (int i = 0; i < count; i++) {
		SyntaxTreeNode node = 
		    (SyntaxTreeNode)_attributeElements.elementAt(i);
		node.translate(classGen, methodGen);
	    }
	}
	
	translateContents(classGen, methodGen);

	il.append(methodGen.endElement());
    }
}
