package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

/**
 * @author Morten Jorgensen
 */
final class ApplyImports extends Instruction {

    private QName      _modeName;
    private String     _functionName;
    private int        _precedence;

    public void display(int indent) {
	indent(indent);
	Util.println("ApplyTemplates");
	indent(indent + IndentIncrement);
	if (_modeName != null) {
	    indent(indent + IndentIncrement);
	    Util.println("mode " + _modeName);
	}
    }

    /**
     * Returns true if this <xsl:apply-imports/> element has parameters
     */
    public boolean hasWithParams() {
	return hasContents();
    }

    /**
     * Determine the lowest import precedence for any stylesheet imported
     * or included by the stylesheet in which this <xsl:apply-imports/>
     * element occured. The templates that are imported by the stylesheet in
     * which this element occured will all have higher import precedence than
     * the integer returned by this method.
     */
    private int getMinPrecedence(int max) {
	Stylesheet stylesheet = getStylesheet();
	Stylesheet root = getParser().getTopLevelStylesheet();

	int min = max;

	Enumeration templates = root.getContents().elements();
	while (templates.hasMoreElements()) {
	    SyntaxTreeNode child = (SyntaxTreeNode)templates.nextElement();
	    if (child instanceof Template) {
		Stylesheet curr = child.getStylesheet();
		while ((curr != null) && (curr != stylesheet)) {
		    if (curr._importedFrom != null)
			curr = curr._importedFrom;
		    else if (curr._includedFrom != null)
			curr = curr._includedFrom;
		    else
			curr = null;
		}
		if (curr == stylesheet) {
		    int prec = child.getStylesheet().getImportPrecedence();
		    if (prec < min) min = prec;
		}
	    }
	}
	return (min);
    }

    /**
     * Parse the attributes and contents of an <xsl:apply-imports/> element.
     */
    public void parseContents(Parser parser) {
	Stylesheet stylesheet = getStylesheet();
	stylesheet.setTemplateInlining(false);

	Template template = getTemplate();
	_modeName = template.getModeName();
	_precedence = template.getImportPrecedence();

	stylesheet = parser.getTopLevelStylesheet();

	final int maxPrecedence = _precedence;
	final int minPrecedence = getMinPrecedence(maxPrecedence);
	final Mode mode = stylesheet.getMode(_modeName);
	_functionName = mode.functionName(minPrecedence, maxPrecedence);

    }

    /**
     * Type-check the attributes/contents of an <xsl:apply-imports/> element.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return Type.Void;
    }

    /**
     * Translate call-template. A parameter frame is pushed only if
     * some template in the stylesheet uses parameters. 
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final Stylesheet stylesheet = classGen.getStylesheet();
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final int current = methodGen.getLocalIndex("current");

	il.append(classGen.loadTranslet());
	il.append(methodGen.loadDOM());
	int init = cpg.addMethodref(SINGLETON_ITERATOR,
				    "<init>", "("+NODE_SIG+")V");
	il.append(new NEW(cpg.addClass(SINGLETON_ITERATOR)));
	il.append(DUP);
	il.append(methodGen.loadCurrentNode());
	il.append(new INVOKESPECIAL(init));

	il.append(methodGen.loadHandler());

	final String className = classGen.getStylesheet().getClassName();
	final String signature = classGen.getApplyTemplatesSig();
	final int applyTemplates = cpg.addMethodref(className,
						    _functionName,
						    signature);
	il.append(new INVOKEVIRTUAL(applyTemplates));
    }

}
