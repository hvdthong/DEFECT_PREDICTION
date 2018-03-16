package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class AttributeValueTemplate extends AttributeValue {
    
    final static int OUT_EXPR = 0;
    final static int IN_EXPR  = 1;
    final static int IN_EXPR_SQUOTES = 2;
    final static int IN_EXPR_DQUOTES = 3;

    public AttributeValueTemplate(String value, Parser parser, 
	SyntaxTreeNode parent) 
    {
	setParent(parent);
	setParser(parser);
        
        try {
            parseAVTemplate(value, parser);
        }
        catch (NoSuchElementException e) {
            reportError(parent, parser,
                        ErrorMsg.ATTR_VAL_TEMPLATE_ERR, value);            
        }
    }

    /**
     * Two-pass parsing of ATVs. In the first pass, double curly braces are 
     * replaced by one, and expressions are delimited using DELIMITER. The 
     * second pass splits up the resulting buffer into literal and non-literal
     * expressions. Errors are reported during the first pass.
     */
    private void parseAVTemplate(String text, Parser parser) {
        StringTokenizer tokenizer = 
            new StringTokenizer(text, "{}\"\'", true);
        
        /*
          * First pass: replace double curly braces and delimit expressions
          * Simple automaton to parse ATVs, delimit expressions and report
          * errors.
          */
        String t = null;
        String lookahead = null;
        StringBuffer buffer = new StringBuffer();
        int state = OUT_EXPR;
        
        while (tokenizer.hasMoreTokens()) {            
            if (lookahead != null) {
                t = lookahead;
                lookahead = null;
            }
            else {
                t = tokenizer.nextToken();
            }
            
            if (t.length() == 1) {
                switch (t.charAt(0)) {
                    case '{':
                        switch (state) {
                            case OUT_EXPR:
                                lookahead = tokenizer.nextToken();
                                if (lookahead.equals("{")) {
                                    lookahead = null;
                                }
                                else {
                                    buffer.append(DELIMITER);
                                    state = IN_EXPR;                                    
                                }
                                break;
                            case IN_EXPR:
                            case IN_EXPR_SQUOTES:
                            case IN_EXPR_DQUOTES:
                                reportError(getParent(), parser,
                                            ErrorMsg.ATTR_VAL_TEMPLATE_ERR, text);
                                break;
                        }                                                
                        break;
                    case '}':
                        switch (state) {
                            case OUT_EXPR:
                                lookahead = tokenizer.nextToken();
                                if (lookahead.equals("}")) {
                                    lookahead = null;
                                }
                                else {
                                    reportError(getParent(), parser,
                                            ErrorMsg.ATTR_VAL_TEMPLATE_ERR, text);
                                }
                                break;
                            case IN_EXPR:
                                buffer.append(DELIMITER);
                                state = OUT_EXPR;
                                break;
                            case IN_EXPR_SQUOTES:
                            case IN_EXPR_DQUOTES:
                                buffer.append(t);
                                break;
                        }
                        break;
                    case '\'':
                        switch (state) {
                            case IN_EXPR:
                                state = IN_EXPR_SQUOTES;
                                break;
                            case IN_EXPR_SQUOTES:
                                state = IN_EXPR;
                                break;
                            case OUT_EXPR:
                            case IN_EXPR_DQUOTES:
                                break;
                        }
                        buffer.append(t);
                        break;
                    case '\"':
                        switch (state) {
                            case IN_EXPR:
                                state = IN_EXPR_DQUOTES;
                                break;
                            case IN_EXPR_DQUOTES:
                                state = IN_EXPR;
                                break;
                            case OUT_EXPR:
                            case IN_EXPR_SQUOTES:
                                break;
                        }
                        buffer.append(t);
                        break;
                    default:
                        buffer.append(t);
                        break;
                }
            }
            else {
                buffer.append(t);
            }
        }

        if (state != OUT_EXPR) {
            reportError(getParent(), parser,
                        ErrorMsg.ATTR_VAL_TEMPLATE_ERR, text);
        }
        
        /*
          * Second pass: split up buffer into literal and non-literal expressions.
          */
        tokenizer = new StringTokenizer(buffer.toString(), DELIMITER, true);
        
        while (tokenizer.hasMoreTokens()) {
            t = tokenizer.nextToken();
            
            if (t.equals(DELIMITER)) {
		addElement(parser.parseExpression(this, tokenizer.nextToken()));
            }
            else {
		addElement(new LiteralExpr(t)); 
            }
        }        
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Vector contents = getContents();
	final int n = contents.size();
	for (int i = 0; i < n; i++) {
	    final Expression exp = (Expression)contents.elementAt(i);
	    if (!exp.typeCheck(stable).identicalTo(Type.String)) {
		contents.setElementAt(new CastExpr(exp, Type.String), i);
	    }
	}
	return _type = Type.String;
    }

    public String toString() {
	final StringBuffer buffer = new StringBuffer("AVT:[");
	final int count = elementCount();
	for (int i = 0; i < count; i++) {
	    buffer.append(elementAt(i).toString());
	    if (i < count - 1)
		buffer.append(' ');
	}
	return buffer.append(']').toString();
    }
		
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	if (elementCount() == 1) {
	    final Expression exp = (Expression)elementAt(0);
	    exp.translate(classGen, methodGen);
	}
	else {
	    final ConstantPoolGen cpg = classGen.getConstantPool();
	    final InstructionList il = methodGen.getInstructionList();
	    final int initBuffer = cpg.addMethodref(STRING_BUFFER_CLASS,
						    "<init>", "()V");
	    final Instruction append =
		new INVOKEVIRTUAL(cpg.addMethodref(STRING_BUFFER_CLASS,
						   "append",
						   "(" + STRING_SIG + ")"
						   + STRING_BUFFER_SIG));
	    
	    final int toString = cpg.addMethodref(STRING_BUFFER_CLASS,
						  "toString",
						  "()"+STRING_SIG);
	    il.append(new NEW(cpg.addClass(STRING_BUFFER_CLASS)));
	    il.append(DUP);
	    il.append(new INVOKESPECIAL(initBuffer));
	    final Enumeration elements = elements();
	    while (elements.hasMoreElements()) {
		final Expression exp = (Expression)elements.nextElement();
		exp.translate(classGen, methodGen);
		il.append(append);
	    }
	    il.append(new INVOKEVIRTUAL(toString));
	}
    }
}
