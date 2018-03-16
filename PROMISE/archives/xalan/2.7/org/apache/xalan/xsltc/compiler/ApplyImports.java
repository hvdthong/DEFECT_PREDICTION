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

final class ApplyImports extends Instruction {

    private QName      _modeName;
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
        Stylesheet includeRoot = getStylesheet();
        while (includeRoot._includedFrom != null) {
            includeRoot = includeRoot._includedFrom;
        }

        return includeRoot.getMinimumDescendantPrecedence();
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
    il.append(methodGen.loadIterator());
	il.append(methodGen.loadHandler());
    il.append(methodGen.loadCurrentNode());

        if (stylesheet.hasLocalParams()) {
            il.append(classGen.loadTranslet());
            final int pushFrame = cpg.addMethodref(TRANSLET_CLASS,
                                                   PUSH_PARAM_FRAME,
                                                   PUSH_PARAM_FRAME_SIG);
            il.append(new INVOKEVIRTUAL(pushFrame));
        }

	final int maxPrecedence = _precedence;
	final int minPrecedence = getMinPrecedence(maxPrecedence);
	final Mode mode = stylesheet.getMode(_modeName);

	String functionName = mode.functionName(minPrecedence, maxPrecedence);

	final String className = classGen.getStylesheet().getClassName();
	final String signature = classGen.getApplyTemplatesSigForImport();
	final int applyTemplates = cpg.addMethodref(className,
						    functionName,
						    signature);
	il.append(new INVOKEVIRTUAL(applyTemplates));

        if (stylesheet.hasLocalParams()) {
            il.append(classGen.loadTranslet());
            final int pushFrame = cpg.addMethodref(TRANSLET_CLASS,
                                                   POP_PARAM_FRAME,
                                                   POP_PARAM_FRAME_SIG);
            il.append(new INVOKEVIRTUAL(pushFrame));
        }
    }

}
