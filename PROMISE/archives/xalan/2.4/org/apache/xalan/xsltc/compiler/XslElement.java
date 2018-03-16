package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class XslElement extends Instruction {

    private String  _prefix;
    private boolean _ignore = false;
    private boolean _isLiteralName = true;
    private AttributeValueTemplate _name; 
    private AttributeValueTemplate _namespace;

    /**
     * Displays the contents of the element
     */
    public void display(int indent) {
	indent(indent);
	Util.println("Element " + _name);
	displayContents(indent + IndentIncrement);
    }

    /**
     * This method is now deprecated. The new implemation of this class
     * never declares the default NS.
     */
    public boolean declaresDefaultNS() {
	return false;
    }

    /**
     * Checks if <param>str</param> is a literal (i.e. not an AVT) or not.
     */
    private boolean isLiteral(String str) {
	final int length = str.length();
	for (int i = 0; i < length; i++) {
	    if (str.charAt(i) == '{' && str.charAt(i + 1) != '{') {
		return false;
	    }
	}
	return true;
    }

    /**
     * Simple check to determine if qname is legal. If it returns false
     * then <param>str</param> is illegal; if it returns true then 
     * <param>str</param> may or may not be legal.
     */
    private boolean isLegalName(String str) {
	if (str.indexOf(' ') > -1) {
	    return false;
	}
	final int colon = str.indexOf(':');
	if (colon == 0 || colon == str.length() - 1) {
	    return false;
	}
	final char first = str.charAt(0);
	if (!Character.isLetter(first) && first != '_') {
	    return false;
	}
	return true;
    }

    public void parseContents(Parser parser) {
	final SymbolTable stable = parser.getSymbolTable();

	String name = getAttribute("name");
	if (name == EMPTYSTRING) {
	    ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ELEM_NAME_ERR,
					name, this);
	    parser.reportError(WARNING, msg);
	    parseChildren(parser);
	    return;
	}

	String namespace = getAttribute("namespace");

	_isLiteralName = isLiteral(name);
	if (_isLiteralName) {
	    if (!isLegalName(name)) {
		ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ELEM_NAME_ERR,
					    name, this);
		parser.reportError(WARNING, msg);
		parseChildren(parser);
		return;
	    }

	    final QName qname = parser.getQNameSafe(name);
	    String prefix = qname.getPrefix();
	    String local = qname.getLocalPart();
	    
	    if (prefix == null) {
		prefix = EMPTYSTRING;
	    }

	    if (!hasAttribute("namespace")) {
		namespace = lookupNamespace(prefix); 
		if (namespace == null) {
		    ErrorMsg err = new ErrorMsg(ErrorMsg.NAMESPACE_UNDEF_ERR,
						prefix, this);
		    parser.reportError(WARNING, err);
		    parseChildren(parser);
		    return;
		}
		_prefix = prefix;
		_namespace = new AttributeValueTemplate(namespace, parser, this);
	    }
	    else {
		if (prefix == EMPTYSTRING) {
		    if (isLiteral(namespace)) {
			prefix = lookupPrefix(namespace);
			if (prefix == null) {
			    prefix = stable.generateNamespacePrefix();
			}
		    }

		    final StringBuffer newName = new StringBuffer(prefix);
		    if (prefix != EMPTYSTRING) {
			newName.append(':');
		    }
		    name = newName.append(local).toString();
		}
		_prefix = prefix;
		_namespace = new AttributeValueTemplate(namespace, parser, this);
	    }
	}
	else {
	    _namespace = (namespace == EMPTYSTRING) ? null :
			 new AttributeValueTemplate(namespace, parser, this);
	}

	_name = new AttributeValueTemplate(name, parser, this);

	final String useSets = getAttribute("use-attribute-sets");
	if (useSets.length() > 0) {
	    setFirstElement(new UseAttributeSets(useSets, parser));
	}

	parseChildren(parser);
    }

    /**
     * Run type check on element name & contents
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (!_ignore) {
	    _name.typeCheck(stable);
	    if (_namespace != null) {
		_namespace.typeCheck(stable);
	    }
	}
	typeCheckContents(stable);
	return Type.Void;
    }

    /**
     * This method is called when the name of the element is known at compile time.
     * In this case, there is no need to inspect the element name at runtime to
     * determine if a prefix exists, needs to be generated, etc.
     */
    public void translateLiteral(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (!_ignore) {
	    il.append(methodGen.loadHandler());
	    _name.translate(classGen, methodGen);
	    il.append(DUP2);
	    il.append(methodGen.startElement());

	    if (_namespace != null) {
		il.append(methodGen.loadHandler());
		il.append(new PUSH(cpg, _prefix));
		_namespace.translate(classGen,methodGen);
		il.append(methodGen.namespace());
	    }
	}

	translateContents(classGen, methodGen);

	if (!_ignore) {
	    il.append(methodGen.endElement());
	}
    }

    /**
     * At runtime the compilation of xsl:element results in code that: (i)
     * evaluates the avt for the name, (ii) checks for a prefix in the name
     * (iii) generates a new prefix and create a new qname when necessary
     * (iv) calls startElement() on the handler (v) looks up a uri in the XML
     * when the prefix is not known at compile time (vi) calls namespace() 
     * on the handler (vii) evaluates the contents (viii) calls endElement().
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	LocalVariableGen local = null;
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (_isLiteralName) {
	    translateLiteral(classGen, methodGen);
	    return;
	}

	if (!_ignore) {
	    il.append(methodGen.loadHandler());
	    _name.translate(classGen, methodGen);

	    il.append(DUP);
	    final int getPrefix = cpg.addMethodref(BASIS_LIBRARY_CLASS, "getPrefix",
					      "(" + STRING_SIG + ")" + STRING_SIG);
	    il.append(new INVOKESTATIC(getPrefix));
	    il.append(DUP);
	    local = methodGen.addLocalVariable("prefix", 
					       org.apache.bcel.generic.Type.STRING,
					       il.getEnd(), null);
	    il.append(new ASTORE(local.getIndex()));

	    final BranchHandle ifNotNull = il.append(new IFNONNULL(null));
	    if (_namespace != null) {
		final int generatePrefix = cpg.addMethodref(BASIS_LIBRARY_CLASS, 
							    "generatePrefix", 
							    "()" + STRING_SIG);
		il.append(new INVOKESTATIC(generatePrefix));
		il.append(DUP);
		il.append(new ASTORE(local.getIndex()));

		final int makeQName = cpg.addMethodref(BASIS_LIBRARY_CLASS, "makeQName", 
				       "(" + STRING_SIG + STRING_SIG + ")" + STRING_SIG);
		il.append(new INVOKESTATIC(makeQName));
	    }
	    ifNotNull.setTarget(il.append(DUP2));
	    il.append(methodGen.startElement());

	    if (_namespace != null) {
		il.append(methodGen.loadHandler());
		il.append(new ALOAD(local.getIndex()));
		_namespace.translate(classGen, methodGen);
		il.append(methodGen.namespace());
	    }
	    else {
		il.append(new ALOAD(local.getIndex()));
		final BranchHandle ifNull = il.append(new IFNULL(null));
		il.append(methodGen.loadHandler());
		il.append(new ALOAD(local.getIndex()));

		il.append(methodGen.loadDOM());
		il.append(methodGen.loadCurrentNode());
		il.append(new ALOAD(local.getIndex()));

		final int lookupNamespace = cpg.addInterfaceMethodref(DOM_INTF, 
					"lookupNamespace", 
				        "(I" + STRING_SIG + ")" + STRING_SIG);
		il.append(new INVOKEINTERFACE(lookupNamespace, 3));
		il.append(methodGen.namespace());
		ifNull.setTarget(il.append(NOP));
	    }
	}

	translateContents(classGen, methodGen);

	if (!_ignore) {
	    il.append(methodGen.endElement());
	}
    }

    /**
     * Override this method to make sure that xsl:attributes are not
     * copied to output if this xsl:element is to be ignored
     */
    public void translateContents(ClassGenerator classGen,
				  MethodGenerator methodGen) {
	final int n = elementCount();
	for (int i = 0; i < n; i++) {
	    final SyntaxTreeNode item =
		(SyntaxTreeNode)getContents().elementAt(i);
	    if (_ignore && item instanceof XslAttribute) continue;
	    item.translate(classGen, methodGen);
	}
    }

}
