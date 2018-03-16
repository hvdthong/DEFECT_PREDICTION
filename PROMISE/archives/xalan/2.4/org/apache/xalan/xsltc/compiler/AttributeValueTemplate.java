package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import java.util.Enumeration;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class AttributeValueTemplate extends AttributeValue {

    public AttributeValueTemplate(String value, Parser parser, 
	SyntaxTreeNode parent) 
    {
	setParent(parent);
	setParser(parser);
	if (check(value, parser)) {
	    parseAVTemplate(0, value, parser);
	}
    }

    private void parseAVTemplate(final int start, String text, Parser parser) {
	String str;

	if (text == null) return;

	int open = start - 2;
	do {
	    open = text.indexOf('{', open+2);
	} while ((open != -1) && 
		 (open < (text.length()-1)) && 
		 (text.charAt(open+1) == '{'));

	if (open != -1) {
	    int close = open - 2;
	    do {
		close = text.indexOf('}', close+2);
	    } while ((close != -1) && 
		     (close < (text.length()-1)) && 
		     (text.charAt(close+1) == '}'));
	    
	    if (open > start) {
		str = removeDuplicateBraces(text.substring(start, open));
		addElement(new LiteralExpr(str));
	    }
	    if (close > open + 1) {
		str = text.substring(open + 1, close);
		str = removeDuplicateBraces(text.substring(open+1,close));
		addElement(parser.parseExpression(this, str));
	    }
	    parseAVTemplate(close + 1, text, parser);
	    
	}
	else if (start < text.length()) {
	    str = removeDuplicateBraces(text.substring(start));
	    addElement(new LiteralExpr(str));
	}
    }

    public String removeDuplicateBraces(String orig) {
	String result = orig;
	int index;

	while ((index = result.indexOf("{{")) != -1) {
	    result = result.substring(0,index) + 
		result.substring(index+1,result.length());
	}

	while ((index = result.indexOf("}}")) != -1) {
	    result = result.substring(0,index) + 
		result.substring(index+1,result.length());
	}

	return(result);
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
	    final Enumeration enum = elements();
	    while (enum.hasMoreElements()) {
		final Expression exp = (Expression)enum.nextElement();
		exp.translate(classGen, methodGen);
		il.append(append);
	    }
	    il.append(new INVOKEVIRTUAL(toString));
	}
    }

    private boolean check(String value, Parser parser) {
	if (value == null) return true;

	final char[] chars = value.toCharArray();
	int level = 0;
	for (int i = 0; i < chars.length; i++) {
	    switch (chars[i]) {
	    case '{':
		if (((i+1) == (chars.length)) || (chars[i+1] != '{'))
		    ++level;
		else
		    i++;
		break;
	    case '}':	
		if (((i+1) == (chars.length)) || (chars[i+1] != '}'))
		    --level;
		else
		    i++;
		break;
	    default:
		continue;
	    }
	    switch (level) {
	    case 0:
	    case 1:
		continue;
	    default:
		reportError(getParent(), parser,
			    ErrorMsg.ATTR_VAL_TEMPLATE_ERR, value);
		return false;
	    }
	}
	if (level != 0) {
	    reportError(getParent(), parser,
			ErrorMsg.ATTR_VAL_TEMPLATE_ERR, value);
	    return false;
	}
	return true;
    }
}
