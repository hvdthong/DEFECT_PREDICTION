package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xml.utils.XMLChar;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class DecimalFormatting extends TopLevelElement {

    private static final String DFS_CLASS = "java.text.DecimalFormatSymbols";
    private static final String DFS_SIG   = "Ljava/text/DecimalFormatSymbols;";

    private QName _name = null;

    /**
     * No type check needed for the <xsl:decimal-formatting/> element
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return Type.Void;
    }

    /**
     * Parse the name of the <xsl:decimal-formatting/> element
     */
    public void parseContents(Parser parser) {
        final String name = getAttribute("name");
        if (name.length() > 0) {
            if (!XMLChar.isValidQName(name)){
                ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_QNAME_ERR, name, this);
                parser.reportError(Constants.ERROR, err);           
            }
        }
        _name = parser.getQNameIgnoreDefaultNs(name);
        if (_name == null) {
            _name = parser.getQNameIgnoreDefaultNs(EMPTYSTRING);
        }         

	SymbolTable stable = parser.getSymbolTable();
	if (stable.getDecimalFormatting(_name) != null) {
	    reportWarning(this, parser, ErrorMsg.SYMBOLS_REDEF_ERR,
		_name.toString());
	}
	else {
	    stable.addDecimalFormatting(_name, this);
	}
    }

    /**
     * This method is called when the constructor is compiled in
     * Stylesheet.compileConstructor() and not as the syntax tree is traversed.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {

	ConstantPoolGen cpg = classGen.getConstantPool();
	InstructionList il = methodGen.getInstructionList();
	
	final int init = cpg.addMethodref(DFS_CLASS, "<init>",
                                          "("+LOCALE_SIG+")V");

	il.append(classGen.loadTranslet());
	il.append(new PUSH(cpg, _name.toString()));

	il.append(new NEW(cpg.addClass(DFS_CLASS)));
	il.append(DUP);
        il.append(new GETSTATIC(cpg.addFieldref(LOCALE_CLASS, "US",
                                                LOCALE_SIG)));
	il.append(new INVOKESPECIAL(init));

	String tmp = getAttribute("NaN");
	if ((tmp == null) || (tmp.equals(EMPTYSTRING))) {
	    int nan = cpg.addMethodref(DFS_CLASS,
				       "setNaN", "(Ljava/lang/String;)V");
	    il.append(DUP);
	    il.append(new PUSH(cpg, "NaN"));
	    il.append(new INVOKEVIRTUAL(nan));
	}

	tmp = getAttribute("infinity");
	if ((tmp == null) || (tmp.equals(EMPTYSTRING))) {
	    int inf = cpg.addMethodref(DFS_CLASS,
				       "setInfinity",
				       "(Ljava/lang/String;)V");
	    il.append(DUP);
	    il.append(new PUSH(cpg, "Infinity"));
	    il.append(new INVOKEVIRTUAL(inf));
	}
	    
	final int nAttributes = _attributes.getLength();
	for (int i = 0; i < nAttributes; i++) {
	    final String name = _attributes.getQName(i);
	    final String value = _attributes.getValue(i);

	    boolean valid = true;
	    int method = 0;

	    if (name.equals("decimal-separator")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setDecimalSeparator", "(C)V");
	    }
	    else if (name.equals("grouping-separator")) {
		method =  cpg.addMethodref(DFS_CLASS,
					   "setGroupingSeparator", "(C)V");
	    }
	    else if (name.equals("minus-sign")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setMinusSign", "(C)V");
	    }
	    else if (name.equals("percent")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setPercent", "(C)V");
	    }
	    else if (name.equals("per-mille")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setPerMill", "(C)V");
	    }
	    else if (name.equals("zero-digit")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setZeroDigit", "(C)V");
	    }
	    else if (name.equals("digit")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setDigit", "(C)V");
	    }
	    else if (name.equals("pattern-separator")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setPatternSeparator", "(C)V");
	    }
	    else if (name.equals("NaN")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setNaN", "(Ljava/lang/String;)V");
	        il.append(DUP);
		il.append(new PUSH(cpg, value));
		il.append(new INVOKEVIRTUAL(method));
		valid = false;
	    }
	    else if (name.equals("infinity")) {
		method = cpg.addMethodref(DFS_CLASS,
					  "setInfinity",
					  "(Ljava/lang/String;)V");
	        il.append(DUP);
		il.append(new PUSH(cpg, value));
		il.append(new INVOKEVIRTUAL(method));
		valid = false;
	    }
	    else {
		valid = false;
	    }

	    if (valid) {
		il.append(DUP);
		il.append(new PUSH(cpg, value.charAt(0)));
		il.append(new INVOKEVIRTUAL(method));
	    }

	}

	final int put = cpg.addMethodref(TRANSLET_CLASS,
					 "addDecimalFormat",
					 "("+STRING_SIG+DFS_SIG+")V");
	il.append(new INVOKEVIRTUAL(put));
    }

    /**
     * Creates the default, nameless, DecimalFormat object in
     * AbstractTranslet's format_symbols hashtable.
     * This should be called for every stylesheet, and the entry
     * may be overridden by later nameless xsl:decimal-format instructions.
     */
    public static void translateDefaultDFS(ClassGenerator classGen,
					   MethodGenerator methodGen) {

	ConstantPoolGen cpg = classGen.getConstantPool();
	InstructionList il = methodGen.getInstructionList();
	final int init = cpg.addMethodref(DFS_CLASS, "<init>",
                                          "("+LOCALE_SIG+")V");

	il.append(classGen.loadTranslet());
	il.append(new PUSH(cpg, EMPTYSTRING));

	il.append(new NEW(cpg.addClass(DFS_CLASS)));
	il.append(DUP);
        il.append(new GETSTATIC(cpg.addFieldref(LOCALE_CLASS, "US",
                                                LOCALE_SIG)));
	il.append(new INVOKESPECIAL(init));

	int nan = cpg.addMethodref(DFS_CLASS,
				   "setNaN", "(Ljava/lang/String;)V");
	il.append(DUP);
	il.append(new PUSH(cpg, "NaN"));
	il.append(new INVOKEVIRTUAL(nan));

	int inf = cpg.addMethodref(DFS_CLASS,
				   "setInfinity",
				   "(Ljava/lang/String;)V");
	il.append(DUP);
	il.append(new PUSH(cpg, "Infinity"));
	il.append(new INVOKEVIRTUAL(inf));

	final int put = cpg.addMethodref(TRANSLET_CLASS,
					 "addDecimalFormat",
					 "("+STRING_SIG+DFS_SIG+")V");
	il.append(new INVOKEVIRTUAL(put));
    }
}
